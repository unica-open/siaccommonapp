/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.model;

import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

public abstract class GenericPagedModel<T> extends GenericModel {
	private static final int DEFAULT_NUMERI_PAGINA = 5;

	private static final long serialVersionUID = -6709813454506966665L;

	private ListaPaginata<T> listaPaginata;

	private int pagina = 0;
	
	private Integer index;

	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	public ListaPaginata<T> getListaPaginata() {
		return listaPaginata;
	}

	public void setListaPaginata(ListaPaginata<T> listaPaginata) {
		this.listaPaginata = listaPaginata;
	}

	public int getNumeroPaginaInizio() {
		return 1 + DEFAULT_NUMERI_PAGINA
				* (listaPaginata.getPaginaCorrente() / DEFAULT_NUMERI_PAGINA);
	}

	public int getNumeroPaginaFine() {
		return Math.min(getNumeroPaginaInizio() + DEFAULT_NUMERI_PAGINA - 1,
				listaPaginata.getTotalePagine());
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public T getElementoByIndex() {
		return listaPaginata.get(index);
	}
}
