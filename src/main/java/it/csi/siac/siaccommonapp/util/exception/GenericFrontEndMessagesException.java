/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.exception;


/**
 * Exception thrown in case it shall be necessary to show a page reporting messages.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/01/2014
 *
 */
public class GenericFrontEndMessagesException extends RuntimeException {
	
	/** For serialization purpose */
	private static final long serialVersionUID = -7930673941238106867L;

	/** The level of the message to be printed */
	public enum Level {
		/** Error level */
		ERROR,
		/** Warning level */
		WARNING,
		/** Information level */
		INFO
	}
	
	private final Level level;
	
	/**
	 * Contructor building an exception given the message.
	 * 
	 * @param message the message to set
	 */
	public GenericFrontEndMessagesException(String message) {
		this(message, Level.ERROR);
	}
	
	/**
	 * Constructor given the cause.
	 * 
	 * @param cause the cause to set
	 */
	public GenericFrontEndMessagesException(Throwable cause) {
		this(cause, Level.ERROR);
	}
	
	/**
	 * Constructor given the message and the cause.
	 * 
	 * @param message the message to set
	 * @param cause   the cause to set
	 */
	public GenericFrontEndMessagesException(String message, Throwable cause) {
		this(message, cause, Level.ERROR);
	}
	
	/**
	 * Constructor given the message and the level.
	 * 
	 * @param message the message to set
	 * @param level   the level to set
	 */
	public GenericFrontEndMessagesException(String message, Level level) {
		super(message);
		this.level = level;
	}
	
	/**
	 * Constructor given the cause and the level.
	 * 
	 * @param cause the cause to set
	 * @param level the level to set
	 */
	public GenericFrontEndMessagesException(Throwable cause, Level level) {
		super(cause);
		this.level = level;
	}
	
	/**
	 * Constructor given the message and the cause and the level.
	 * 
	 * @param message the message to set
	 * @param cause   the cause to set
	 * @param level   the level to set
	 */
	public GenericFrontEndMessagesException(String message, Throwable cause, Level level) {
		super(message, cause);
		this.level = level;
	}
	
	/**
	 * @return the level
	 */
	public Level getLevel() {
		return level;
	}

}
