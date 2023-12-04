/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.exception;

/**
 * Eccezione rilanciata nel caso di errore nell'invocazione dei serviz&icirc;.
 */
public class WebServiceInvocationFailureException extends FrontEndCheckedException {

	private static final long serialVersionUID = 6965771740594098644L;

	/**
	 * @see Exception#Exception()
	 */
	public WebServiceInvocationFailureException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public WebServiceInvocationFailureException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public WebServiceInvocationFailureException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public WebServiceInvocationFailureException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
