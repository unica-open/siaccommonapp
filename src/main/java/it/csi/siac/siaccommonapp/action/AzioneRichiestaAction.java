/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccommonapp.model.AzioneRichiestaModel;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.ParametroAzioneRichiesta;

/**
 * Action per azione richiesta
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AzioneRichiestaAction extends GenericAction<AzioneRichiestaModel> {

	private static final long serialVersionUID = -7152300961767870228L;

	@Autowired
	private transient CoreService coreService;

	/**
	 * Instanzia una nuova action a partire dal Model.
	 * 
	 * @param azioneRichiestaModel
	 *            il model da associare
	 */
	public AzioneRichiestaAction(AzioneRichiestaModel azioneRichiestaModel) {
		super();
		this.model = azioneRichiestaModel;
	}

	/** Costruttore vuoto di default */
	public AzioneRichiestaAction() {
		super();
	}

	@Override
	@AnchorAnnotation(value = "%{model.nomeAzione}", afterAction = true)
	public String execute() throws Exception {
		final String methodName = "execute";

		log.debugStart(methodName, "");
		try {
			GetAzioneRichiestaResponse response = getAzioneRichiestaResponse();
			AzioneRichiesta azioneRichiesta = response.getAzioneRichiesta();
			
//			ActionContext.getContext().getSession().put(CommonSessionParameter.BREADCRUMB_TRAIL.getName(), null);
			sessionHandler.setParametro(CommonSessionParameter.BREADCRUMB_TRAIL, null);
			
			impostaParametriInSessioneDaAzioneRichiestaResponse(response);

			return StringUtils.trim(azioneRichiesta.getAzione().getNome());
		} catch (Exception e) {
			log.error(methodName, e, e);
			throw e;
		} finally {
			log.debugEnd("execute", "");
		}
	}

	/**
	 * Imposta i parametri della reponse del servizio di
	 * {@link GetAzioneRichiesta} in sessione.
	 * 
	 * @param azioneRichiestaResponse
	 *            la response da cui popolare i dati
	 */
	protected void impostaParametriInSessioneDaAzioneRichiestaResponse(GetAzioneRichiestaResponse azioneRichiestaResponse) {
		final String methodName = "impostaParametriInSessioneDaAzioneRichiesta";

		AzioneRichiesta azioneRichiesta = azioneRichiestaResponse.getAzioneRichiesta();

		/* Impostazione dei dati in sessione */
		sessionHandler.setAccount(azioneRichiesta.getAccount());
		sessionHandler.setAzioniConsentite(azioneRichiestaResponse.getAzioniConsentite());
		sessionHandler.setAnnoEsercizio(getParametro(ParametroAzioneRichiesta.ANNO_BILANCIO, azioneRichiesta.getParametri()).getValore());
		// sessionHandler.setAzione(azioneRichiesta.getAzione());
		sessionHandler.setDescrizioneAnnoBilancio(getParametro(ParametroAzioneRichiesta.DESCRIZIONE_ANNO_BILANCIO, azioneRichiesta.getParametri())
				.getValore());
		sessionHandler.setIdBilancio(getParametro(ParametroAzioneRichiesta.ID_BILANCIO, azioneRichiesta.getParametri()).getValore());
		sessionHandler.setFaseBilancio(getParametro(ParametroAzioneRichiesta.CODICE_FASE_ANNO_BILANCIO, azioneRichiesta.getParametri()).getValore());
		
		if(getParametro(ParametroAzioneRichiesta.CODICE_FASE_ANNO_BILANCIO_PRECEDENTE, azioneRichiesta.getParametri()) != null ){
			sessionHandler.setFaseBilancioPrecedente(getParametro(ParametroAzioneRichiesta.CODICE_FASE_ANNO_BILANCIO_PRECEDENTE, azioneRichiesta.getParametri()).getValore());
		}else{
			sessionHandler.setFaseBilancioPrecedente("");
		}
		// SIAC-5022
		ParametroAzioneRichiesta parametroBilancioAnnoPrecedente = getParametro(ParametroAzioneRichiesta.BILANCIO_ANNO_PRECEDENTE, azioneRichiesta.getParametri());
		sessionHandler.setParametro(CommonSessionParameter.EVIDENZIA_ANNO_SELEZIONATO, parametroBilancioAnnoPrecedente != null ? Boolean.valueOf(parametroBilancioAnnoPrecedente.getValore()) : null);
		
		// SIAC-6257
		ParametroAzioneRichiesta parametriAzioneSelezionata = getParametro(CommonSessionParameter.PARAMETRI_AZIONE_SELEZIONATA.getName(), azioneRichiesta.getParametri());
		sessionHandler.setParametriAzioneSelezionata(parametriAzioneSelezionata!= null ? parametriAzioneSelezionata.getValore() : null);

		sessionHandler.setAzioneRichiesta(azioneRichiesta);

		/* Log dei dati impostati in sessione */
		log.debug(methodName, "AnnoEsercizio: " + sessionHandler.getAnnoEsercizio()); 
		log.debug(methodName, "Account: " + sessionHandler.getAccount());
		log.debug(methodName, "Ente: " + sessionHandler.getAccount().getEnte().getUid());

		// Nome della action cui redirigere
		String nomeAzione = azioneRichiesta.getAzione().getNome();
		log.info(methodName, "Redirect to: " + nomeAzione);
		model.setNomeAzione(nomeAzione);

	}

	/**
	 * Ottiene la response per il servizio di {@link GetAzioneRichiesta}.
	 * 
	 * @return la response
	 */
	protected GetAzioneRichiestaResponse getAzioneRichiestaResponse() {
		// L'azione richiesta fornita da Cruscotto
		GetAzioneRichiesta req = model.creaRequestGetAzioneRichiesta(sessionHandler.getRichiedente());
		// Effettua un log della request
		logServiceRequest(req);

		// Invoco il servizio
		GetAzioneRichiestaResponse response = coreService.getAzioneRichiesta(req);
		// Effettua un log della response
		logServiceResponse(response);

		return response;
	}

	/**
	 * Ottiene il parametro richiesto.
	 * 
	 * @param nome
	 *            il nome del parametro
	 * @param parametri
	 *            i parametri tra cui cercare
	 * 
	 * @return il Parametro trovato, se presente; <code>null</code> altrimenti
	 */
	private ParametroAzioneRichiesta getParametro(String nome, List<ParametroAzioneRichiesta> parametri) {
		Map<String, ParametroAzioneRichiesta> mapParametri = new HashMap<String, ParametroAzioneRichiesta>();
		// Imposta la mappa con il nome del parametro come chiave
		for (ParametroAzioneRichiesta parametro : parametri) {
			mapParametri.put(parametro.getNome(), parametro);
		}
		// Ottiene il parametro cercato
		return mapParametri.get(nome);
	}

}
