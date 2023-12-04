/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.model;

import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiesta;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Richiedente;

/**
 * Model per la AzioneRichiesta
 * 
 * @author AR
 * 
 */
public class AzioneRichiestaModel extends GenericModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7079261840622462610L;
	
	private String azioneRichiesta;
	
	private String nomeAzione;
	
	/** Costruttore vuoto di default */
	public AzioneRichiestaModel() {
		super();
		setTitolo("Azione richiesta");
	}

	/**
	 * @return the azioneRichiesta
	 */
	public String getAzioneRichiesta() {
		return azioneRichiesta;
	}

	/**
	 * @param azioneRichiesta the azioneRichiesta to set
	 */
	public void setAzioneRichiesta(String azioneRichiesta) {
		this.azioneRichiesta = azioneRichiesta;
	}

	/**
	 * @return the nomeAzione
	 */
	public String getNomeAzione() {
		return nomeAzione;
	}

	/**
	 * @param nomeAzione the nomeAzione to set
	 */
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}

	/**
	 * Crea una request per il servizio di {@link GetAzioneRichiesta} a partire dal model e da un richiedente.
	 * 
	 * @param richiedente il richiedente per il servizio
	 * 
	 * @return la request creata
	 */
	public GetAzioneRichiesta creaRequestGetAzioneRichiesta(Richiedente richiedente) {
		GetAzioneRichiesta req = new GetAzioneRichiesta();
		int idAzioneRichiesta = Integer.parseInt(azioneRichiesta);
		AzioneRichiesta ar = new AzioneRichiesta();		
		ar.setUid(idAzioneRichiesta);
		req.setAzioneRichiesta(ar);
		req.setRichiedente(richiedente);
		return req;
	}

}
