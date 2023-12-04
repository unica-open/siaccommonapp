/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommonapp.model.GenericModel;

/**
 * Action per la redirezione al Cruscotto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RedirectToCruscottoAction extends GenericAction<GenericModel> {

	private static final long serialVersionUID = 3543118684341695720L;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		log.debugStart(methodName, "Redirezione al Cruscotto");

		log.debug(methodName, "Pulisco la sessione");
		sessionHandler.cleanAll();

		log.debugEnd(methodName, "Redirezione al Cruscotto");
		return SUCCESS;
	}

}
