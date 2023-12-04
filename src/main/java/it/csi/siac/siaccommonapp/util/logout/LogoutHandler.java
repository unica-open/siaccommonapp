/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.logout;

import java.io.Serializable;

//task-122
public interface LogoutHandler extends Serializable{

	public String getLogoutUrl();
	
}
