/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts2.ServletActionContext;

import it.csi.siac.siaccommon.model.UserSessionInfo;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.Account;

public class LogWebUtil extends LogUtil {

	public static final String REQUEST_ID_KEY = "Siac-Request-ID";
	
	public LogWebUtil(Class<?> cls) {
		super(cls);
	}

	@Override
	protected UserSessionInfo getInternalUserSessionInfo() { 
		try {
			HttpServletRequest httpServletRequest = ServletActionContext.getRequest();
			HttpSession httpSession = httpServletRequest.getSession(false);
			
			if (httpSession == null) {			 
				return super.getInternalUserSessionInfo();
			}
			
			Account account = SessionHandler.getParametro(httpSession, CommonSessionParameter.ACCOUNT);
					
			return new UserSessionInfo(
					account == null ? null : account.getCodice(),
					httpSession.getId(), 
					ObjectUtils.defaultIfNull(httpServletRequest.getAttribute(REQUEST_ID_KEY), "-").toString()
				);
		} catch (Exception e) {
			System.out.println("WARN it.csi.siac.siaccommonapp.util.log.LogWebUtil.getInternalUserSessionInfo(): " + e.getMessage());
			return super.getInternalUserSessionInfo();
		}
	}

}
