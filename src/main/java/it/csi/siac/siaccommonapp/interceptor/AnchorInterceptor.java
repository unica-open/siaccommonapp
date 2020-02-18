/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.interceptor;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.Stack;

import org.springframework.core.annotation.AnnotationUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

import it.csi.siac.siaccommonapp.interceptor.anchor.Anchor;
import it.csi.siac.siaccommonapp.interceptor.anchor.AnchorComparator;
import it.csi.siac.siaccommonapp.interceptor.anchor.AnchorStack;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;

/**
 * Interceptor for the use of the Anchor. This interceptor should be used as late as possible in the interceptor stack, 
 * so as to prevent exceptions originating from the invocation to cause errouneous data in session. 
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 04/10/2013
 *
 */
public class AnchorInterceptor extends AbstractInterceptor {
	
	private static final long serialVersionUID = -8424015237335317767L;

	/** The key according to which the stack will be stored in session */
	public static final String ANCHOR_STACK_KEY = AnchorStack.class.getSimpleName() + ":KEY";
	
	/** To synchronize the stack */
	private static final Object LOCK = new Object();

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		final AnchorAnnotation annotation = processAnnotation(invocation);

		// Only intercept the invocation when there is an annotation signaling the anchor
		if (annotation != null) {
			if(annotation.afterAction()) {
				invocation.addPreResultListener(
					new PreResultListener() {
						@Override
						public void beforeResult(ActionInvocation inv, String resultCode) {
							AnchorInterceptor.this.doIntercept(inv, annotation);
						}
					}
				);
			} else if(annotation.rewind()) {
				destroyAnchor(invocation);
			} else {
				doIntercept(invocation, annotation);
			}
		}
		return invocation.invoke();
	}
	
	/**
	 * Processes the annotation on the method of the action.
	 * 
	 * @param invocation the invocation to extract the action from
	 * 
	 * @return the anchor, if the annotation is present; <code>null</code> otherwise
	 */
	private static AnchorAnnotation processAnnotation(ActionInvocation invocation) {
		UtilTimerStack.push("AnchorInterceptor: processAnnotation");
		
		Class<?> actionClass = invocation.getAction().getClass();
		String methodName = invocation.getProxy().getMethod();
		
		// Default methodName
		if (methodName == null) {
		  methodName = "execute";
		}
		
		Method method = null;
		try {
			method = actionClass.getMethod(methodName, (Class<?>[])null);
		} catch (Exception e) {
			method = null;
		}
		
		AnchorAnnotation annotation = null;
		// Gets the annotation from the method
		if (method != null) {
			annotation = AnnotationUtils.findAnnotation(method, AnchorAnnotation.class);
		}

		UtilTimerStack.pop("AnchorInterceptor: processAnnotation");

		return annotation;
	}
	
	/**
	 * Effectively intercepts the invocation.
	 * 
	 * @param invocation the invocation from which to infer the data
	 * @param annotation the annotation from which to construct the Stack
	 */
	void doIntercept(ActionInvocation invocation, AnchorAnnotation annotation) {
		UtilTimerStack.push("AnchorInterceptor: doIntercept");

		if (annotation != null) {
			Anchor current = makeAnchor(invocation, annotation);
			AnchorStack anchorStack = getAnchorStack(invocation);
			Comparator<Anchor> comparator = new AnchorComparator();
			Stack<Anchor> anchors = anchorStack.getAnchorStack();
			// Lock the stack
			synchronized (anchors) {
				Anchor last = anchors.isEmpty() ? null : anchors.lastElement();
				if(comparator.compare(current, last) != 0) {
					int dupIdx = anchorStack.indexOf(current);
					if (dupIdx != -1) {
						anchorStack.rewindAt(dupIdx - 1);
					}
					anchors.push(current);
				} else if(!anchors.isEmpty()) {
					anchors.remove(anchors.size() - 1);
					anchors.push(current);
				}
			}
		}
		UtilTimerStack.pop("AnchorInterceptor: doIntercept");
	}
	
	/**
	 * Creates an Anchor based on the invocation of the action and the annotation.
	 * 
	 * @param invocation the invocation by which to populate the Anchor
	 * @param annotation the annotation by which to populate the Anchor
	 * 
	 * @return the Anchor corresponding to the current invocation and annotation
	 */
	private static Anchor makeAnchor(ActionInvocation invocation, AnchorAnnotation annotation) {
		ActionProxy proxy = invocation.getProxy();
		Anchor anchor = new Anchor();

		String action = proxy.getActionName();
		String method = proxy.getMethod();
		String name = annotation.name();
		String namespace = proxy.getNamespace();
		Map<String, Object> params = invocation.getInvocationContext().getParameters();
		String useCase = annotation.value();
		
		String temporaryName = annotation.value();
		
		// In case of a ValueStack-related anchor useCase 
		if (useCase.startsWith("%{") && useCase.endsWith("}")) {
			ValueStack vstack = invocation.getStack();
			Object value = vstack.findValue(temporaryName.substring(2, temporaryName.length() - 1));
			useCase = "" + value;
		}
		
		// In the case in which the name is not specified, make the name the same as the useCase
		if(name == null || name.isEmpty()) {
			name = useCase;
		}
		
		// Injection of the parameters
		anchor.setAction(action);
		anchor.setMethod(method);
		anchor.setName(name);
		anchor.setNamespace(namespace);
		anchor.setParams(params);
		anchor.setUseCase(useCase);
		
		return anchor;
	}
	
	/**
	 * Obtains the AnchorStack from the session.
	 * 
	 * @param invocation the invocation of the action
	 * 
	 * @return the AnchorStack in session
	 */
	private AnchorStack getAnchorStack(ActionInvocation invocation) {
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		AnchorStack anchorStack = (AnchorStack) session.get(ANCHOR_STACK_KEY);
		
		// Check if the anchorStack was really in session
		if (anchorStack == null) {
			// Obtain a lock, create the stack and put it in session
			synchronized (LOCK) {
				anchorStack = new AnchorStack();
				session.put(ANCHOR_STACK_KEY, anchorStack);
			}
		}
		return anchorStack;
	}
	
	/**
	 * Destroys the current anchor and rewinds the stack to the previous one.
	 * 
	 * @param invocation the invocation by which to fing the AnchorStack
	 */
	private void destroyAnchor(ActionInvocation invocation) {
		UtilTimerStack.push("AnchorInterceptor: destroy");
		
		synchronized (LOCK) {
			AnchorStack anchorStack = getAnchorStack(invocation);
			Stack<Anchor> anchors = anchorStack.getAnchorStack();
			if(!anchors.isEmpty()) {
				anchors.remove(anchors.size() - 1);
			}
		}
		UtilTimerStack.pop("AnchorInterceptor: destroy");
	}
	
}
