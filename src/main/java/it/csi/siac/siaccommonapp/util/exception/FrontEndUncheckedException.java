/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.exception;

/**
 * Superclasse per le eccezioni unchecked per il front-end.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/ott/2014
 *
 */
public class FrontEndUncheckedException extends RuntimeException {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 17677033044657837L;

	/**
	 * @see Exception#Exception()
	 */
	public FrontEndUncheckedException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public FrontEndUncheckedException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public FrontEndUncheckedException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public FrontEndUncheckedException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
