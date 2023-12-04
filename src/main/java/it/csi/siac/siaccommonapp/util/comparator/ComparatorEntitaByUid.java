/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siaccorser.model.Entita;

public abstract class ComparatorEntitaByUid<T extends Entita> implements Comparator<T>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5698079252405480498L;

	@Override
	public int compare(T o1, T o2) {
		if (o1 == o2) {
			return 0;
		}

		if (o1 == null) {
			return -1;
		}

		if (o2 == null) {
			return 1;
		}

		return o1.getUid() < o2.getUid()
			? -1
			: o1.getUid() == o2.getUid()
				? 0
				: 1;
	}

}
