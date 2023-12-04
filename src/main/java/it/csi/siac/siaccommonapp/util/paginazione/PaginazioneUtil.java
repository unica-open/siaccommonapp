/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.paginazione;

import java.util.Collection;

import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;

/**
 * Utility class for collection usage.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/11/2014
 *
 */
public final class PaginazioneUtil {
	
	/** Just an util class with static methods */
	private PaginazioneUtil() {
	}

	/**
	 * Obtains a paginated list from a canonical collection, the page number and the total number of records.
	 * @param <T> the collected type
	 * @param collection   the collection to paginate
	 * @param pageNumber   the page number
	 * @param totalRecords the total number of records
	 * 
	 * @return the paginated list as wrapper
	 */
	public static <T> ListaPaginata<T> toListaPaginata(Collection<T> collection, int pageNumber, int totalRecords) {
		ListaPaginataImpl<T> listaPaginata = new ListaPaginataImpl<T>();
		if(collection != null) {
			listaPaginata.addAll(collection);
		}
		
		listaPaginata.setTotaleElementi(totalRecords);
		listaPaginata.setPaginaCorrente(pageNumber);
		
		return listaPaginata;
	}
	
}
