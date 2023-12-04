/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.exception;

/**
 * Eccezione rilanciata nelle validazioni.
 */
// FIXME: da passare a Checked
public class ParamValidationException extends FrontEndUncheckedException {

	private static final long serialVersionUID = 2549202494981605991L;

	/**
	 * @see Exception#Exception()
	 */
	public ParamValidationException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public ParamValidationException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public ParamValidationException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public ParamValidationException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
