/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.handler.session;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;

/**
 * Classe per la gestione degli oggetti in sessione
 * 
 * @author alagna, lgallo, Domenico Lisi, AR
 * @version 1.1.0
 * 
 */
public class SessionHandler implements Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5672637553386838909L;

	private transient Map<String, Object> session;

	private Class<SessionParameter> sessionParameterClass;

	public Class<SessionParameter> getSessionParameterClass() {
		return sessionParameterClass;
	}

	public void setSessionParameterClass(Class<SessionParameter> sessionParameterClass) {
		this.sessionParameterClass = sessionParameterClass;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void cleanAll() {
		for (CommonSessionParameter sessionParam : CommonSessionParameter.values())
			session.put(sessionParam.getName(), null);

		if (sessionParameterClass != null)
			for (SessionParameter sessionParam : sessionParameterClass.getEnumConstants())
				session.put(sessionParam.getName(), null);
	}


	/**
	 * Imposta a <code>null</code> i parametri in sessione dichiarati come
	 * annullabili nell'enum {@link SessionParameter}.
	 */
	public void cleanAllSafely() {
		for (CommonSessionParameter sessionParam : CommonSessionParameter.values()) {
			if (sessionParam.isEliminabile()) {
				session.put(sessionParam.getName(), null);
			}
		}

		if (sessionParameterClass != null) {
			for (SessionParameter sessionParam : sessionParameterClass.getEnumConstants()) {
				if (sessionParam.isEliminabile()) {
					session.put(sessionParam.getName(), null); 
				}
			}
		}
	}

	/**
	 * Imposta a <code>null</code> i parametri in sessione, ad eccezione di
	 * quelli dichiarati come non annullabili nell'enum {@link SessionParameter}
	 * e di quelli passati come parametro.
	 * 
	 * @param excludeParams
	 *            i parametri da escludere per l'annullamento
	 */
	public void cleanSafelyExcluding(SessionParameter... excludeParams) {
		List<SessionParameter> listaParametriDaEscludere = Arrays.asList(excludeParams);

		for (CommonSessionParameter sessionParam : CommonSessionParameter.values()) {
			if (sessionParam.isEliminabile() && !(listaParametriDaEscludere.contains(sessionParam))) {
				session.put(sessionParam.getName(), null);
			}
		}
	}

	public void cleanAllExcluding(SessionParameter... excludeParams) {
		List<SessionParameter> listaParametriDaEscludere = Arrays.asList(excludeParams);

		for (CommonSessionParameter sessionParam : CommonSessionParameter.values()) {
			if (!(listaParametriDaEscludere.contains(sessionParam))) {
				session.put(sessionParam.getName(), null);
			}
		}

		if (sessionParameterClass != null) {
			for (SessionParameter sessionParam : sessionParameterClass.getEnumConstants()) {
				if (!(listaParametriDaEscludere.contains(sessionParam))) {
					session.put(sessionParam.getName(), null);
				}
			}
		}
	}
	
	public void clean(String... sessionKeys) {
		for (String k : sessionKeys)
			session.put(k, null);
	}

	/**
	 * Reperisce un parametro dalla sessione
	 * 
	 * @param param
	 *            il nome del parametro in sessione
	 * 
	 * @return il parametro presente in sessione corrispondente all'input
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParametro(SessionParameter param) {
		return (T) session.get(param.getName());

	}

	@SuppressWarnings("unchecked")
	public <T> T getParametro(SessionParameter param, String subParam) {
		return (T) session.get(param.getName() + "." + subParam);

	}

	/**
	 * Reperisce un parametro dalla sessione
	 * 
	 * @param param
	 *            il nome del parametro in sessione
	 * @param clazz
	 *            la classe del parametro
	 * 
	 * @return il parametro presente in sessione corrispondente all'input
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParametro(SessionParameter param, Class<T> clazz) {
		return (T) session.get(param.getName());

	}

	/**
	 * Imposta un parametro in sessione
	 * 
	 * @param param
	 *            il nome del parametro da immettere in sessione
	 * @param paramValue
	 *            il valore del parametro da immettere in sessione
	 */
	public void setParametro(SessionParameter param, Object paramValue) {
		session.put(param.getName(), paramValue);
	}

	public void setParametro(SessionParameter param, String subParam, Object paramValue) {
		session.put(param.getName() + "." + subParam, paramValue);
	}

	/**
	 * Ritorna un Bilancio recuperando in sessione l'id e l'anno.
	 * 
	 * @return il Bilancio presente in sessione
	 */
	public Bilancio getBilancio() {
		Bilancio bilancio = new Bilancio();

		int uid = Integer.parseInt(getIdBilancio());
		int anno = Integer.parseInt(getAnnoEsercizio());

		bilancio.setUid(uid);
		bilancio.setAnno(anno);
		return bilancio;
	}

	/**
	 * Ritorna Richiedente recuperando in sessione operatore e account
	 * 
	 * @return il richiedente presente in sessione
	 */
	public Richiedente getRichiedente() {
		Richiedente richiedente = new Richiedente();
		richiedente.setOperatore(getOperatore());
		richiedente.setAccount(getAccount());
		return richiedente;
	}

	/**
	 * setter specifico per parametro di sessione Operatore
	 * 
	 * @param operatore
	 */
	public void setOperatore(Operatore operatore) {
		setParametro(CommonSessionParameter.OPERATORE, operatore);
	}

	/**
	 * getter specifico per parametro di sessione Operatore
	 * 
	 * @return l'operatore presente in sessione
	 */
	public Operatore getOperatore() {
		return getParametro(CommonSessionParameter.OPERATORE);
	}

	/**
	 * setter specifico per parametro di sessione azioniConsentite
	 * 
	 * @param azioniConsentite
	 */
	public void setAzioniConsentite(List<AzioneConsentita> azioniConsentite) {
		setParametro(CommonSessionParameter.AZIONI_CONSENTITE, azioniConsentite);
	}

	/**
	 * getter specifico per parametro di sessione azioniConsentite
	 * 
	 * @return le azioni consentite presenti in sessione
	 */
	public List<AzioneConsentita> getAzioniConsentite() {
		return getParametro(CommonSessionParameter.AZIONI_CONSENTITE);
	}

	/**
	 * setter specifico per parametro di sessione AnnoEsercizio
	 * 
	 * @param annoEsercizio
	 */
	public void setAnnoEsercizio(String annoEsercizio) {
		setParametro(CommonSessionParameter.ANNO_ESERCIZIO, annoEsercizio);
	}

	/**
	 * getter specifico per parametro di sessione annoEsercizio
	 * 
	 * @return l'anno di esercizio presente in sessione
	 */
	public String getAnnoEsercizio() {
		return getParametro(CommonSessionParameter.ANNO_ESERCIZIO);
	}

	/**
	 * Imposta la descrizione e l'anno di bilancio in sessione.
	 * 
	 * @param annoEsercizio
	 */
	public void setDescrizioneAnnoBilancio(String descrizioneAnnoBilancio) {
		setParametro(CommonSessionParameter.DESCRIZIONE_ANNO_BILANCIO, descrizioneAnnoBilancio);
	}

	/**
	 * getter specifico per parametro di sessione idBilancio
	 * 
	 * @return l'anno di esercizio presente in sessione
	 */
	public String getIdBilancio() {
		return getParametro(CommonSessionParameter.ID_BILANCIO);
	}

	/**
	 * Imposta l'id di bilancio in sessione.
	 * 
	 * @param annoEsercizio
	 */
	public void setIdBilancio(String idBilancio) {
		setParametro(CommonSessionParameter.ID_BILANCIO, idBilancio);
	}

	/**
	 * Imposta l'id di bilancio in sessione.
	 * 
	 * @param annoEsercizio
	 */
	public void setFaseBilancio(String faseBilancio) {
		setParametro(CommonSessionParameter.CODICE_FASE_ANNO_BILANCIO, faseBilancio);
	}

	/**
	 * getter specifico per parametro di sessione faseBilancio
	 * 
	 * @return faseBilancio presente in sessione
	 */
	public String getFaseBilancio() {
		return getParametro(CommonSessionParameter.CODICE_FASE_ANNO_BILANCIO);
	}

	/**
	 * Imposta l'id di bilancio in sessione.
	 * 
	 * @param annoEsercizio
	 */
	public void setFaseBilancioPrecedente(String faseBilancioPrecedente) {
		setParametro(CommonSessionParameter.CODICE_FASE_ANNO_BILANCIO_PRECEDENTE, faseBilancioPrecedente);
	}

	/**
	 * getter specifico per parametro di sessione faseBilancio
	 * 
	 * @return faseBilancio presente in sessione
	 */
	public String getFaseBilancioPrecedente() {
		return getParametro(CommonSessionParameter.CODICE_FASE_ANNO_BILANCIO_PRECEDENTE);
	}

	/**
	 * Getter specifico per parametro di sessione azioneRichiesta.
	 * 
	 * @return l'azioneRichiesta
	 */
	public AzioneRichiesta getAzioneRichiesta() {
		return getParametro(CommonSessionParameter.AZIONE_RICHIESTA);
	}

	/**
	 * Imposta l'azione richiesta in sessione.
	 * 
	 * @param azioneRichiesta
	 */
	public void setAzioneRichiesta(AzioneRichiesta azioneRichiesta) {
		setParametro(CommonSessionParameter.AZIONE_RICHIESTA, azioneRichiesta);
	}

	/**
	 * Ottiene la descrizione e l'anno di bilancio dalla sessione.
	 * 
	 * @return la descrizione e l'anno di bilancio
	 */
	public String getDescrizioneAnnoBilancio() {
		return getParametro(CommonSessionParameter.DESCRIZIONE_ANNO_BILANCIO);
	}

	/**
	 * getter specifico per parametro di sessione Account
	 * 
	 * @return l'account presente in sessione
	 */
	public Account getAccount() {
		return getParametro(CommonSessionParameter.ACCOUNT);
	}

	public void setAccount(Account account) {
		setParametro(CommonSessionParameter.ACCOUNT, account);
	}

	public void setParametriAzioneSelezionata(String faseBilancio) {
		setParametro(CommonSessionParameter.PARAMETRI_AZIONE_SELEZIONATA, faseBilancio);
	}

	public String getParametriAzioneSelezionata() {
		return getParametro(CommonSessionParameter.PARAMETRI_AZIONE_SELEZIONATA);
	}

	/**
	 * getter specifico per parametro di sessione Azione
	 * 
	 * @return l'azione presente in sessione
	 */
	public Azione getAzione() {
		return getAzioneRichiesta().getAzione();
	}

	public Ente getEnte() {
		Account account = getAccount();

		if (account != null)
			return account.getEnte();

		return null;
	}

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);

		for (Map.Entry<String, Object> entry : session.entrySet())
			toStringBuilder.append(entry.getKey().toString(), entry.getValue());

		return toStringBuilder.toString();
	}

	/**
	 * Imposta un parametro in sessione non serializzabile annotato con XmlType.
	 * 
	 * @param param
	 *            la chiave per la sessione
	 * @param paramValue
	 *            l'oggetto da apporre in sessione
	 */
	public void setParametroXmlType(SessionParameter param, Object paramValue) {
		setParametro(param, JAXBUtility.marshall(paramValue));
	}

	/**
	 * Reperisce un parametro annotato con XmlType dalla sessione.
	 * 
	 * @param param
	 *            la chiave per la sessione
	 * @param clazz
	 *            la classe dell'oggetto in sessione
	 * 
	 * @return l'oggetto in sessione
	 */
	public <T> T getParametroXmlType(SessionParameter param, Class<T> clazz) {
		String xml = getParametro(param, String.class);
		return JAXBUtility.unmarshall(xml, clazz);

	}

	public void clearSession() {
		cleanAll();
		session.clear();
	}
}
