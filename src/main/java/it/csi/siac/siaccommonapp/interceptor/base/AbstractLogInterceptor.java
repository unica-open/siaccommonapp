/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.interceptor.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.csi.siac.siaccommonapp.util.log.LogWebUtil;

public abstract class AbstractLogInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -5089340636785106216L;

	protected void initRequestUID(ActionInvocation invocation) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
		httpServletRequest.setAttribute(LogWebUtil.REQUEST_ID_KEY, RandomStringUtils.randomAlphanumeric(10));
	}

}
