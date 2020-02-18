/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.exception;

/**
 * Eccezione rilanciata in caso di errori generici.
 */
public class ApplicationException extends Exception {

	private static final long serialVersionUID = 2549202494981605991L;

	/**
	 * @see Exception#Exception()
	 */
	public ApplicationException() {
		super();
	}

	/**
	 * Costruttore dati messaggio e causa.
	 * 
	 * @param message
	 * @param cause
	 */
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public ApplicationException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public ApplicationException(Throwable cause) {
		super(cause);
	}

}
