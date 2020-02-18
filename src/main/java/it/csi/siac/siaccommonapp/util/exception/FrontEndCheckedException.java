/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.exception;

/**
 * Superclasse per le eccezioni checked per il front-end.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/giu/2014
 *
 */
public class FrontEndCheckedException extends Exception {
	
	private static final long serialVersionUID = 5883343344179048278L;

	/**
	 * @see Exception#Exception()
	 */
	public FrontEndCheckedException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public FrontEndCheckedException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public FrontEndCheckedException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public FrontEndCheckedException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
