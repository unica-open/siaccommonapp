/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siaccorser.model.Entita;

public abstract class ComparatorEntitaByDataCreazione<T extends Entita> implements Comparator<T>, Serializable {
	private static final long serialVersionUID = 4186583133931303199L;

	@Override
	public int compare(T o1, T o2) {
		if (o1 == o2)
			return 0;

		if (o1 == null || o1.getDataCreazione() == null)
			return -1;

		if (o2 == null || o2.getDataCreazione() == null)
			return 1;

		if (o1.getDataCreazione().after(o2.getDataCreazione()))
			return 1;

		if (o1.getDataCreazione().before(o2.getDataCreazione()))
			return -1;

		return 0;
	}

}
