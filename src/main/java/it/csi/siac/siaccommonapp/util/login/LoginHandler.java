/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.login;

import java.io.Serializable;
import java.util.Map;

import it.csi.siac.siaccorser.model.Operatore;


public abstract class LoginHandler implements Serializable{

	private static final long serialVersionUID = -551275319017545387L;

	public abstract Operatore getOperatore(Map<String, Object> session);
	
}
