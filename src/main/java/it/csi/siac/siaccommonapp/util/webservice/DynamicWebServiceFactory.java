/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.webservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.handler.HandlerResolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

public class DynamicWebServiceFactory {
	private DynamicWebServiceFactory() {
		// Prevent instantiation
	}
	
	public static <T> T getService(Class<T> serviceInterface, String namespaceUri,
			String endpointAddress) throws MalformedURLException {
		return getService(serviceInterface, namespaceUri, endpointAddress, null);
	}

	public static <T> T getService(Class<T> serviceInterface, String namespaceUri,
			String endpointAddress, HandlerResolver handlerResolver) throws MalformedURLException {
		JaxWsPortProxyFactoryBean jaxWsPortProxyFactoryBean = new JaxWsPortProxyFactoryBean();

		jaxWsPortProxyFactoryBean.setServiceInterface(serviceInterface);
		jaxWsPortProxyFactoryBean.setWsdlDocumentUrl(new URL(String.format("%s?wsdl",
				endpointAddress)));
		jaxWsPortProxyFactoryBean.setNamespaceUri(namespaceUri);

		String serviceName = StringUtils.substringAfterLast(endpointAddress, "/");

		jaxWsPortProxyFactoryBean.setServiceName(serviceName);
		jaxWsPortProxyFactoryBean.setEndpointAddress(endpointAddress);
		jaxWsPortProxyFactoryBean.setPortName(serviceName + "Port");

		jaxWsPortProxyFactoryBean.setHandlerResolver(handlerResolver);

		jaxWsPortProxyFactoryBean.afterPropertiesSet();

		@SuppressWarnings("unchecked")
		T srv = (T) jaxWsPortProxyFactoryBean.getObject();

		return srv;
	}

}
