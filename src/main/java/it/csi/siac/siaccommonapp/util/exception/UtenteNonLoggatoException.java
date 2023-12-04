/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.exception;

/**
 * Eccezione rilanciata nel caso in cui l'utente non sia loggato.
 *
 */
public class UtenteNonLoggatoException extends RuntimeException {

	private static final long serialVersionUID = -6003581712044672089L;
	
	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public UtenteNonLoggatoException() {
		super();
	}
	
	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public UtenteNonLoggatoException(String message) {
		super(message);
	}
	
	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public UtenteNonLoggatoException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public UtenteNonLoggatoException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
