/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.interceptor.anchor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark an anchor for the Struts2 interceptors. This annotation should be to mark method that must be 
 * considered as anchors for the navigation to and from other actions.
 * <br>
 * A parsing class should use this annotation to signal the storage to save the informations relative to the navigation
 * up to this point, so as to make this annotation behave the same as the <tt>\hypertarget</tt> marker used in TeX-derived
 * markup languages.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 04/10/2013
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface AnchorAnnotation {
	
	/** The value of the anchor, referring to the useCase. */
	String value();
	
	/** An optional name for the Anchor, in case the useCase and the displayed name should be different */
	String name() default "";
	
	/** Whether the annotation should be parsed after the invocation of the action */
	boolean afterAction() default false;
	
	/** Whether to rewind to the previous anchor. */
	boolean rewind() default false;
	
}
