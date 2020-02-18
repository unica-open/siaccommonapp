/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.interceptor.anchor;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Comparator for the Anchors. Compares two different anchors through {@link Anchor#name}.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 04/10/2013
 *
 */
public class AnchorComparator implements Comparator<Anchor>, Serializable {

	/** For serialization purpose */
	private static final long serialVersionUID = 3352477421382722353L;

	@Override
	public int compare(Anchor anchor1, Anchor anchor2) {
		if(anchor1 == null && anchor2 == null) {
			return 0;
		}
		if(anchor1 == null && anchor2 != null) {
			return 1;
		}
		if(anchor1 != null && anchor2 == null) {
			return -1;
		}
		return new CompareToBuilder()
				.append(anchor1.getName(), anchor2.getName())
				.toComparison();
	}
	
}
