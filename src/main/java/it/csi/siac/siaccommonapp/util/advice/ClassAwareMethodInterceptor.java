/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.advice;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * Interceptor di un metodo che &eacute; a conoscenza della classe per cui sta lavorando.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/08/2015
 *
 */
public interface ClassAwareMethodInterceptor extends MethodInterceptor {
	
	/**
	 * Imposta la classe advised.
	 * 
	 * @param cls la classe advised
	 */
	void setAdvisedClass(Class<?> cls);
	
}
