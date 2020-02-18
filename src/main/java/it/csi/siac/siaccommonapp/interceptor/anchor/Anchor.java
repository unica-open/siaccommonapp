/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.interceptor.anchor;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The anchor for the navigation, containing informations about the current action to be saved in session.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 04/10/2013
 *
 */
public class Anchor implements Serializable {
	
	private static final long serialVersionUID = -9029828115667595435L;
	
	private String action;
	private String method;
	private String name;
	private String namespace;
	private Map<String, Object> params;
	private String useCase;
	
	/** Default empty constructor */
	public Anchor() {
		super();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the useCase
	 */
	public String getUseCase() {
		return useCase;
	}

	/**
	 * @param useCase the useCase to set
	 */
	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(action)
				.append(method)
				.append(name)
				.append(namespace)
				.append(params)
				.append(useCase)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Anchor)) {
			return false;
		}
		
		Anchor other = (Anchor) obj;
		
		return new EqualsBuilder()
				// Check for the action
				.append(this.action, other.action)
				// Check for the method
				.append(this.method, other.method)
				// Check for the name
				.append(this.name, other.name)
				// Check for the namespace
				.append(this.namespace, other.namespace)
				// Check for the params
				.append(this.params, other.params)
				// // Check for the useCase
				.append(this.useCase, other.useCase)
				.isEquals();
	}

	@Override
	public String toString() {
		return "Anchor@" + Integer.toHexString(hashCode()) + "[name= " + name + ", namespace=" + namespace
				+ ", method=" + method + ", params=" + params.toString() + ", action="
				+ action + ", useCase=" + useCase + "]";
	}
	
}
