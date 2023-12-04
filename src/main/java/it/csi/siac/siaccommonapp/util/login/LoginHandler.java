/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.login;

import java.io.Serializable;
import java.util.Map;

import it.csi.siac.siaccorser.model.Operatore;

public interface LoginHandler extends Serializable{

	public Operatore getOperatore(Map<String, Object> session);

}
