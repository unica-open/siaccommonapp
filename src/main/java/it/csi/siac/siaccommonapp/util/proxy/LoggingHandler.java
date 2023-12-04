/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.proxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import it.csi.siac.siaccommonapp.util.exception.FrontEndCheckedException;
import it.csi.siac.siaccommonapp.util.log.LogWebUtil;

/**
 * Handler per il log della request e della response JAX-WS.
 */
public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {
	private final LogWebUtil log = new LogWebUtil(getClass());

	@Override
	public boolean handleMessage(final SOAPMessageContext context) {
		final SOAPMessage msg = context.getMessage();
		final boolean request = ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
		if (request) {
			// This is a request message.
			logRequestMessage(msg);
		} else {
			// This is the response message
			logResponseMessage(msg);
		}
		return true;
	}

	@Override
	public boolean handleFault(final SOAPMessageContext context) {
		logErrorMessage(context.getMessage());
		return false;
	}

	/**
	 * Writes the message to the given appendable.
	 * 
	 * @param appendable the appndable to which add the message
	 * @param soapMessage the message to write
	 * @throws FrontEndCheckedException if an exception in writing the message is raised
	 */
	private void writeMessage(Appendable appendable, final SOAPMessage soapMessage) throws FrontEndCheckedException {
		ByteArrayOutputStream baos = null;
		try {
			// Write the message to the output stream
			baos = new ByteArrayOutputStream();
			soapMessage.writeTo(baos);
			appendable.append(baos.toString("UTF-8"));
			baos.close();
		} catch (final Exception e) {
			if(baos != null) {
				try {
					baos.close();
				} catch (IOException e1) {
					throw new FrontEndCheckedException("IOException in closing stream: " + e1, e1);
				}
			}
			throw new FrontEndCheckedException("Exception in handling message writing: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Logs the request message
	 * @param requestMessage the request
	 */
	private void logRequestMessage(final SOAPMessage requestMessage) {
		if(!log.isDebugEnabled()) {
//		if(!log.isInfoEnabled()) {
			return;
		}
		final String methodName = "logRequestMessage";
		StringBuilder sb = new StringBuilder();
		sb.append("REQUEST:\n");
		try {
			writeMessage(sb, requestMessage);
			log.debug(methodName, sb.toString());
		//	log.info(methodName, sb.toString());
		} catch(FrontEndCheckedException e) {
			log.error(methodName, "Caught exception for request message: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Logs the response message
	 * @param msg the response message
	 */
	private void logResponseMessage(final SOAPMessage msg) {
		if(!log.isDebugEnabled()) {
//		if(!log.isInfoEnabled()) {
			return;
		}
		final String methodName = "logResponseMessage";
		StringBuilder sb = new StringBuilder();
		sb.append("RESPONSE:\n");
		try {
			writeMessage(sb, msg);
			//log.debug(methodName, sb.toString());
			log.info(methodName, sb.toString());
		} catch(FrontEndCheckedException e) {
			log.error(methodName, "Caught exception for response message: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Logs the error message
	 * @param msg the error message
	 */
	private void logErrorMessage(final SOAPMessage msg) {
		if(!log.isDebugEnabled()) {
			return;
		}
		final String methodName = "logErrorMessage";
		StringBuilder sb = new StringBuilder();
		sb.append("ERROR:\n");
		try {
			writeMessage(sb, msg);
			log.debug(methodName, sb.toString());
		} catch(FrontEndCheckedException e) {
			log.error(methodName, "Caught exception for error message: " + e.getMessage(), e);
		}
	}

	@Override
	public void close(final MessageContext context) {
		// Not required for logging
	}

	@Override
	public Set<QName> getHeaders() {
		// Not required for logging
		return new HashSet<QName>();
	}
}
