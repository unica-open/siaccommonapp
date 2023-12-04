/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.proxy;

import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

/**
 * Handler resolver
 * @author Alessandro Marchino
 * @version 1.0.0
 *
 */
@SuppressWarnings("rawtypes")
public class SiacHandlerResolver implements HandlerResolver {
	
	private List<Handler> handlerList;
	 
	@Override
    public List<Handler> getHandlerChain(final PortInfo portInfo) {
        return handlerList;
    }
 
	/**
	 * @param handlerList the handlerList
	 */
    public void setHandlerList(final List<Handler> handlerList) {
        this.handlerList = handlerList;
    }

}
