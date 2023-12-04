/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonapp.util.log.LogWebUtil;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.VariabileProcesso;

/**
 * Model base per tutte le action
 * 
 * @author alagna
 * 
 */
public class GenericModel implements Serializable{

	private static final long serialVersionUID = 426873589273120770L;
	
	protected transient LogWebUtil log = new LogWebUtil(this.getClass());

	private String titolo;
	private List<Errore> errori = new ArrayList<Errore>();
	private transient List<Messaggio> messaggi = new ArrayList<Messaggio>();
	private List<Informazione> informazioni = new ArrayList<Informazione>();
	private Account account;
	private String descrizioneAnnoBilancio;
	
	/** Definisce il caso d'uso che effettua una chiamata alla presente Action */
	protected String cduChiamante;
	protected String cdu;
	
	// SIAC-5022
	private boolean evidenziaAnnoSelezionato = false;
	
	/**
	 * @param errori the errori to set
	 */
	public void setErrori(List<Errore> errori) {
		this.errori = errori;
	}

	/**
	 * @param messaggi the messaggi to set
	 */
	public void setMessaggi(List<Messaggio> messaggi) {
		this.messaggi = messaggi;
	}

	/**
	 * @param informazioni the informazioni to set
	 */
	public void setInformazioni(List<Informazione> informazioni) {
		this.informazioni = informazioni;
	}

	/**
	 * @return the titolo
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * @param titolo
	 *            the titolo to set
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	/**
	 * @return the errori
	 */
	public List<Errore> getErrori() {
		return errori;
	}

	/**
	 * @return <boolean> true if has error or false if not
	 */
	public boolean hasErrori() {
		return getErrori().size() > 0 ? true : false;
	}

	/**
	 * Cancella gli errori presenti.
	 */
	public void resetErrori() {
		this.errori = new ArrayList<Errore>();
	}

	/**
	 * Aggiunge un errore.
	 * 
	 * @param errore l'errore da aggiungere
	 */
	public void addErrore(Errore errore) {
		this.errori.add(errore);
	}

	/**
	 * Aggiunge degli errori.
	 * 
	 * @param list la lista degli errori da aggiungere
	 */
	public void addErrori(List<Errore> list) {
		this.errori.addAll(list);
	}

	/**
	 * @return the messaggi
	 */
	public List<Messaggio> getMessaggi() {
		return messaggi;
	}

	/**
	 * Cancella i messaggi presenti
	 */
	public void resetMessaggi() {
		this.messaggi = new ArrayList<Messaggio>();
	}

	/**
	 * Aggiunge un messaggio.
	 * 
	 * @param messaggio il messaggio da aggiungere
	 */
	public void addMessaggio(Messaggio messaggio) {
		this.messaggi.add(messaggio);
	}

	/**
	 * Aggiunge dei messaggi.
	 *  
	 * @param messaggi la lista dei messaggi da aggiungere
	 */
	public void addMessaggi(List<Messaggio> messaggi) {
		this.messaggi.addAll(messaggi);
	}
	
	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}
	
	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
	
	/**
	 * @return the descrizioneAnnoBilancio
	 */
	public String getDescrizioneAnnoBilancio() {
		return descrizioneAnnoBilancio;
	}

	/**
	 * @param descrizioneAnnoBilancio the descrizioneAnnoBilancio to set
	 */
	public void setDescrizioneAnnoBilancio(String descrizioneAnnoBilancio) {
		this.descrizioneAnnoBilancio = descrizioneAnnoBilancio;
	}

	/**
	 * @return the informazioni
	 */
	public List<Informazione> getInformazioni() {
		return informazioni;
	}

	/**
	 * Resetta le informazioni
	 */
	public void resetInformazioni() {
		this.informazioni = new ArrayList<Informazione>();
	}

	/**
	 * Aggiunge un'informazione alla lista.
	 * 
	 * @param informazione l'informazione da aggiungere
	 */
	public void addInformazione(Informazione informazione) {
		this.informazioni.add(informazione);
	}

	/**
	 * Aggiunge delle informazioni alla lista.
	 * 
	 * @param informazioni le informazioni da aggiungere
	 */
	public void addInformazioni(List<Informazione> informazioni) {
		this.informazioni.addAll(informazioni);
	}

	/**
	 * @param cduChiamante the cduChiamante to set
	 */
	public void setCduChiamante(String cduChiamante) {
		this.cduChiamante = cduChiamante;
	}
	
	/**
	 * @param cdu the cdu to set
	 */
	public void setCdu(String cdu) {
		this.cdu = cdu;
	}

	/**
	 * @return the cdu
	 */
	public String getCdu() {
		return cdu;
	}
	
	/**
	 * @return the evidenziaAnnoSelezionato
	 */
	public boolean isEvidenziaAnnoSelezionato() {
		return evidenziaAnnoSelezionato;
	}

	/**
	 * @param evidenziaAnnoSelezionato the evidenziaAnnoSelezionato to set
	 */
	public void setEvidenziaAnnoSelezionato(boolean evidenziaAnnoSelezionato) {
		this.evidenziaAnnoSelezionato = evidenziaAnnoSelezionato;
	}

	/**
	 * Ottiene la variabile di processo relativa ad un determinato nome dall'azione richiesta.
	 * 
	 * @param azione l'azione richiesta da cui ottenere la variabile di processo
	 * @param nomeVariabile   il nome della variabile di processo
	 * 
	 * @return la variabile di processo relativa al nome, se presente
	 */
	public VariabileProcesso getVariabileProcesso(AzioneRichiesta azione, String nomeVariabile) {		
		
		for (VariabileProcesso variabile : azione.getVariabiliProcesso()) {
			if (nomeVariabile.equals(variabile.getNome())) {
				return variabile;
			}
		}
		return null;
	}
	
	/**
	 * Per la lettura dei dati serializzati
	 * @param in lo stream dei dati in entrata
	 * @throws IOException in caso di errore di IO
	 * @throws ClassNotFoundException in caso di errore di construzione delle classi
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.log = new LogWebUtil(this.getClass());
		this.messaggi = new ArrayList<Messaggio>();
	}
	
}
