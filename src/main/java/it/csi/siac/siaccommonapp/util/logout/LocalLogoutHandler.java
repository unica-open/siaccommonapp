/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.logout;

//task-122
public class LocalLogoutHandler implements LogoutHandler {

	private static final long serialVersionUID = 505979177579868546L;

	@Override
	public String getLogoutUrl() {
		return "http://localhost";
	}

}
