/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.action;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommonapp.model.GenericModel;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class LogoutAction extends GenericAction<GenericModel> {

	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {

		String methodName = "execute";

		HttpSession httpSession = ServletActionContext.getRequest().getSession();
		
		log.info(methodName, "Logout - Session ID: " + httpSession.getId());

		sessionHandler.clearSession();

		httpSession.invalidate();
		
		return SUCCESS;
	}
}