/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommonapp.model.GenericModel;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public final class EmptyAction extends GenericAction<GenericModel> {
	private static final long serialVersionUID = 866629222798745566L;
}
