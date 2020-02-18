/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.interceptor.anchor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.apache.struts2.components.IteratorComponent;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.opensymphony.xwork2.util.ValueStack;

import it.csi.siac.siaccommonapp.interceptor.AnchorInterceptor;

/**
 * The tag for using the Anchor. Iterates over the collection of Anchors in session.
 *  
 * @author Marchino Alessandro
 * @version 1.0.0 10/10/2013
 *
 */
public class AnchorTag extends ComponentTagSupport {
	
	/** For serialization purpose */
	private static final long serialVersionUID = -4891967632779402252L;
	
	private String var;
	private String status;
	
	/**
	 * @return the var
	 */
	public String getVar() {
		return var;
	}
	
	/**
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		return new IteratorComponent(stack);
	}
	
	/**
	 * Populates the parameters of this component.
	 */
	@Override
	protected void populateParams() {
		super.populateParams();
		IteratorComponent iteratorComponent = (IteratorComponent) getComponent();
		iteratorComponent.setId(var);
		iteratorComponent.setValue("#session['" + AnchorInterceptor.ANCHOR_STACK_KEY + "'].anchors");
		iteratorComponent.setStatus(status);
	}
	
	@Override
	public int doEndTag() throws JspException {
		component = null;
		return 6;
	}
	
	@Override
	public int doAfterBody() throws JspException {
		boolean again = component.end(pageContext.getOut(), getBody());
		int response = 0;
		if(again) {
			response = 2;
		} else if(bodyContent != null) {
			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			} catch (IOException e) {
				throw new JspException(e.getMessage(), e);
			}
		}
		return response;
	}

}
