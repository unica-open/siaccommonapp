/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.handler.session;

//import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import xyz.timedrain.arianna.plugin.BreadCrumbInterceptor;

import it.csi.siac.siaccommonapp.interceptor.AnchorInterceptor;

/**
 * Enumeration contenente i parametri della sessione.
 * 
 * @author Domenico Lisi, Alessandro Marchino, AR
 * @version 1.1.0
 * 
 */
public enum CommonSessionParameter implements SessionParameter {
	OPERATORE("it.csi.siac.siaccommonapp.session.operatore", false), 
	ACCOUNT("it.csi.siac.siaccommonapp.session.account", false), 
	AZIONI_CONSENTITE("it.csi.siac.siaccommonapp.session.azioniConsentite", false), 

	ANNO_ESERCIZIO("it.csi.siac.siaccorser.model.annoBilancio", false),
	DESCRIZIONE_ANNO_BILANCIO("it.csi.siac.siaccorser.model.descrizioneAnnoBilancio", false),
	ID_BILANCIO("it.csi.siac.siaccorser.model.idBilancio", false),
	CODICE_FASE_ANNO_BILANCIO("it.csi.siac.siaccorser.model.codiceFaseAnnoBilancio", false),
	CODICE_FASE_ANNO_BILANCIO_PRECEDENTE("it.csi.siac.siaccorser.model.codiceFaseAnnoBilancioPrecedente", false),
	
	AZIONE_RICHIESTA("it.csi.siac.siaccommonapp.session.azioneRichiesta", false),

	/* Breadcrumb trail */
	BREADCRUMB_TRAIL(BreadCrumbInterceptor.CRUMB_KEY, false),
	
	ANCHOR_STACK(AnchorInterceptor.ANCHOR_STACK_KEY, false),
	
	// SIAC-5022
	EVIDENZIA_ANNO_SELEZIONATO("it.csi.siac.siaccommonapp.session.evidenziaAnnoSelezionato", false),
	
	PARAMETRI_AZIONE_SELEZIONATA("it.csi.siac.siaccommonapp.session.parametriAzioneSelezionata", true),	
	;

	private String paramName;
	private boolean eliminabile;

	/**
	 * Costruttore vuoto di default
	 */
	private CommonSessionParameter() {
		this(null, true);
	}

	/**
	 * Costruttore utilizzante il nome del parametro e la condizione di
	 * eliminabilit&agrave;.
	 * 
	 * @param paramName
	 *            il nome del parametro. Nel caso sia impostato a
	 *            <code>null</code>, viene considerato come nome del parametro
	 *            quanto ottenuto dal metodo {@link Enum#name()}
	 * @param isEliminabile
	 */
	private CommonSessionParameter(String paramName, boolean eliminabile) {
		String nomeParametro = paramName;

		if (nomeParametro == null)
			nomeParametro = this.name();

		this.paramName = nomeParametro;
		this.eliminabile = eliminabile;
	}


	/**
	 * @return the isEliminabile
	 */
	@Override
	public boolean isEliminabile() {
		return eliminabile;
	}

	@Override
	public String getName() {
		return paramName;
	}

}