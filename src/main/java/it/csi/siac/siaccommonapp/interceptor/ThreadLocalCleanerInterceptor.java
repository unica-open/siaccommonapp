/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.csi.siac.siaccommon.util.threadlocal.ThreadLocalUtil;

/**
 * Cleaner for registered thread local instances
 * @author Marchino Alessandro
 *
 */
public class ThreadLocalCleanerInterceptor extends AbstractInterceptor {
	
	/** For serialization */
	private static final long serialVersionUID = 2656833295878826099L;

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		String result;
		try {
			result = actionInvocation.invoke();
		} finally {
			ThreadLocalUtil.cleanThreadLocals();
		}
		
		return result;
	}
	
}
