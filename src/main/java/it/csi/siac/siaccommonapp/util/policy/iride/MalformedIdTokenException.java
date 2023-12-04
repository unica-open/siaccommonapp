/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.policy.iride;

/**
 * Exception for a malformed token representing the ID of the user
 * @version 1.0.0 - 17/04/2020
 */
public class MalformedIdTokenException extends Exception {

	/** For serialization */
	private static final long serialVersionUID = 4504903154241956986L;

	/** @see Exception#Exception() */
	public MalformedIdTokenException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message the message of the exception
	 * @param cause the cause of the exception
	 * @see Exception#Exception(String, Throwable)
	 */
	public MalformedIdTokenException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message the message of the exception
	 * @see Exception#Exception(String)
	 */
	public MalformedIdTokenException(String message) {
		super(message);
	}

	/**
	 * @param cause the cause of the exception
	 * @see Exception#Exception(Throwable)
	 */
	public MalformedIdTokenException(Throwable cause) {
		super(cause);
	}

}