/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.logout;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import it.csi.siac.siaccommonapp.util.log.LogWebUtil;

//task-122
public class ShibbolethLogoutHandler implements LogoutHandler {

	private static final long serialVersionUID = -4740172165935704443L;
	private LogWebUtil log = new LogWebUtil(ShibbolethLogoutHandler.class);

	@Override
	public String getLogoutUrl() {
		
		HttpServletRequest req = ServletActionContext.getRequest();
		
		String logoutUrl = req.getHeader("Shib-Handler") + "/Logout";
		log.info("getLogoutUrl", "Shibboleth Logout Url = " + logoutUrl);
		
		return logoutUrl;
	}

}
