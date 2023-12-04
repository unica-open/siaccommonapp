/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.action;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import xyz.timedrain.arianna.plugin.BreadCrumbTrail;
import xyz.timedrain.arianna.plugin.Crumb;
/*
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import it.csi.siac.siaccommon.util.MimeType;
import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccommonapp.model.GenericModel;
import it.csi.siac.siaccommonapp.util.exception.UtenteNonLoggatoException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccommonapp.util.log.LogWebUtil;
import it.csi.siac.siaccommonapp.util.login.LoginHandler;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.exception.SystemException;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetParametroConfigurazioneEnte;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetParametroConfigurazioneEnteResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.exception.UtenteNonAutenticatoException;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.TipoFileHandler;
import it.csi.siac.siaccorser.model.file.TipoFileIntf;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Action base per tutte le action. Prepara il Model, la sessione, e l'Operatore
 * che ha fatto login.
 * 
 * 
 * @param <M>
 *            Model di riferimento
 */
public abstract class GenericAction<M extends GenericModel> extends ActionSupport implements
		ModelDriven<M>, SessionAware, RequestAware, Preparable {

	private static final long serialVersionUID = -3474331964452951L;

	/** Utility per il log */
	protected transient LogWebUtil log = new LogWebUtil(this.getClass());

	/** Modello per Struts */
	protected M model;

	@Resource(name = "sessionHandlerBean")
	protected SessionHandler sessionHandler;
	protected transient Map<String, Object> session;
	protected transient Map<String, Object> request;

	@Resource(name = "loginHandlerBean")
	protected LoginHandler loginHandler;

	
	@Autowired protected transient CoreService coreService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debug(methodName, "Inizializzazione del modello");
		initModel();
		log.debug(methodName, "Inizializzazione dell'operatore");
		initOperatore();
	}

	/**
	 * Inizializza il modello utilizzando il costruttore vuoto solo se il
	 * modello &eacute; <code>null</code>.
	 */
	protected void initModel() {
		if (model == null) {
			model = instantiateNewModel();
		}
		Boolean evidenziaAnnoSelezionato = sessionHandler.getParametro(CommonSessionParameter.EVIDENZIA_ANNO_SELEZIONATO);
		model.setEvidenziaAnnoSelezionato(Boolean.TRUE.equals(evidenziaAnnoSelezionato));
	}

	/**
	 * Inizializza in sessione l'oggetto Operatore rappresentante l'utente
	 * loggato.
	 * 
	 * @throws IllegalStateException
	 */
	private void initOperatore() {
		final String messaggioErrore = "Si e' verificato un problema tecnico nell'inizializzazione dell'Operatore.";
		final String methodName = "initOperatore";
		try {
			Operatore operatoreLogin = loginHandler.getOperatore(session);
			checkReloginOperatore(operatoreLogin);
			sessionHandler.setOperatore(operatoreLogin);
		} catch (UtenteNonAutenticatoException e) {
			log.error(methodName, messaggioErrore, e);
			throw e;
		} catch (UtenteNonLoggatoException e) {
			log.error(methodName, messaggioErrore, e);
			throw e;
		} catch (Exception e) {
			log.error(methodName, messaggioErrore, e);
			throw new IllegalStateException(messaggioErrore, e);
		}

	}

	/**
	 * Controlla e gestisce l'eventualit&agrave; che venga fatto login con un
	 * altro operatore all'interno della stessa sessione (ad esempio in caso la
	 * sessione non sia stata veramente invalidata dal precedente logout). Nel
	 * caso in cui venga rilevato un nuovo login ripulisce la sessione.
	 * 
	 * @param operatoreLogin
	 *            l'operatore che ha effettuato il login
	 */
	private void checkReloginOperatore(Operatore operatoreLogin) {
		final String methodName = "checkReloginOperatore";
		try {
			Operatore operatoreSession = sessionHandler.getOperatore();

			if (!operatoreLogin.getCodiceFiscale().equals(operatoreSession.getCodiceFiscale())) {
				sessionHandler.cleanAll();
				log.info(methodName, "Sessione ripulita. Nuovo codicefiscale operatore: "
						+ operatoreLogin.getCodiceFiscale() + ". Login precedente: "
						+ operatoreSession.getCodiceFiscale());
			}
		} catch (Exception e) {
			log.debug(methodName, "Ok. Nessun login precedente diverso da quello attuale.");
			return;
		}
	}

	public Bilancio getBilancio() {
		return sessionHandler.getBilancio();
	}

	public Integer getAnnoBilancio() {
		return Integer.valueOf(getBilancio().getAnno());
	}

	
	/**
	 * Pulisce gli errori dal model e dalla action.
	 */
	protected void cleanErrori() {
		model.getErrori().clear();
		Collection<String> actionErrors = getActionErrors();
		actionErrors.clear();
		setActionErrors(actionErrors);
	}

	/**
	 * Pulisce i messaggi dal model.
	 */
	protected void cleanMessaggi() {
		model.getMessaggi().clear();
		Collection<String> actionMessages = getActionMessages();
		actionMessages.clear();
		setActionMessages(actionMessages);
	}

	/**
	 * Pulisce le informazioni dal model.
	 */
	protected void cleanInformazioni() {
		model.getInformazioni().clear();
	}

	/**
	 * Permette di aggiungere gli errori di una response di un servizio a
	 * livello di Model e di Action.
	 * 
	 * @param response
	 *            la Response del servizio
	 */
	public void addErrori(ServiceResponse response) {
		model.addErrori(response.getErrori());
		for (Errore e : response.getErrori()) {
			addActionError(e.getTesto());
		}
	}

	/**
	 * Permette di aggiungere gli errori di una response di un servizio a
	 * livello di Model e di Action. Effettua contestualmente un log degli
	 * errori individuati.
	 * 
	 * @param methodName
	 *            il nome del metodo chiamante
	 * @param response
	 *            la Response del servizio
	 */
	protected void addErrori(String methodName, ServiceResponse response) {
		model.addErrori(response.getErrori());
		for (Errore e : response.getErrori()) {
			log.error(methodName, e);
			addActionError(e.getTesto());
		}
	}

	/**
	 * Permette di aggiungere gli errori a livello di Model e di Action.
	 * 
	 * @param errori
	 *            la lista degli errori da comunicare
	 */
	protected void addErrori(List<Errore> errori) {
		model.addErrori(errori);
		for (Errore errore : errori) {
			addActionError(errore.getTesto());
		}
	}

	/**
	 * Permette di aggiungere gli errori a livello di Model e di Action.
	 * Effettua contestualmente un log degli errori individuati.
	 * 
	 * @param methodName
	 *            il nome del metodo chiamante
	 * @param errori
	 *            la lista degli errori da comunicare
	 */
	protected void addErrori(String methodName, List<Errore> errori) {
		model.addErrori(errori);
		for (Errore errore : errori) {
			log.error(methodName, errore);
			addActionError(errore.getTesto());
		}
	}

	/**
	 * Permette di aggiungere gli errori a livello di Model e di Action.
	 * Effettua contestualmente un log degli errori individuati. Metodo
	 * overloadato per i fieldErrors.
	 * 
	 * @param methodName
	 *            il nome del metodo chiamante
	 * @param fieldErrors
	 *            la mappa degli errori da comunicare
	 */
	protected void addErrori(String methodName, Map<String, List<String>> fieldErrors) {
		for (Map.Entry<String, List<String>> entry : fieldErrors.entrySet()) {
			for (String errore : entry.getValue()) {
				Errore e = ErroreCore.FORMATO_NON_VALIDO.getErrore(entry.getKey(), errore);
				addErrore(e);
			}
		}
	}

	/**
	 * Permette di aggiungere un errore a livello di Model e di Action. Effettua
	 * contestualmente un log degli errori individuati.
	 * 
	 * @param methodName
	 *            il nome del metodo chiamante
	 * @param errore
	 *            l'errore da comunicare
	 */
	protected void addErrore(String methodName, Errore errore) {
		log.error(methodName, errore);
		model.addErrore(errore);
		addActionError(errore.getTesto());
	}

	protected void addErroreDiSistema(String methodName, Exception e) {
		addErroreDiSistema(methodName, e.getMessage());
	}

	protected void addErroreDiSistema(String methodName, String text) {
		addErrore(methodName, ErroreCore.ERRORE_DI_SISTEMA.getErrore(text));
	}

	/**
	 * Permette di aggiungere un errore a livello di Model e di Action.
	 * 
	 * @param errore
	 *            l'errore da comunicare
	 */
	public void addErrore(Errore errore) {
		model.addErrore(errore);
		addActionError(errore.getTesto());
	}

	/**
	 * Permette di aggiungere i messaggi a livello di Model e di Action.
	 * 
	 * @param errori
	 *            la lista degli errori da comunicare
	 */
	protected void addMessaggi(List<Messaggio> messaggi) {
		model.addMessaggi(messaggi);
		for (Messaggio messaggio : messaggi) {
			addActionMessage(messaggio.getCodice() + " - " + messaggio.getDescrizione());
		}
	}

	/**
	 * Permette di aggiungere un messaggio a livello di Model e di Action.
	 * 
	 * @param messaggio
	 *            il messaggio da comunicare
	 */
	protected void addMessaggio(Messaggio messaggio) {
		model.addMessaggio(messaggio);
		addActionMessage(messaggio.getCodice() + " - " + messaggio.getDescrizione());
	}

	/**
	 * Permette di aggiungere un messaggio a livello di Model e di Action.
	 * 
	 * @param errore
	 *            il messaggio da comunicare
	 */
	protected void addMessaggio(Errore errore) {
		Messaggio messaggio = new Messaggio(errore.getCodice(), errore.getDescrizione());
		addMessaggio(messaggio);
	}

	/**
	 * Permette di aggiungere le informazioni a livello di Model.
	 * 
	 * @param informazioni
	 *            la lista delle informazioni da comunicare
	 */
	protected void addInformazioni(List<Informazione> informazioni) {
		model.addInformazioni(informazioni);
	}

	/**
	 * Permette di aggiungere un'nformazione a livello di Model.
	 * 
	 * @param informazione
	 *            il messaggio da comunicare
	 */
	protected void addInformazione(Informazione informazione) {
		model.addInformazione(informazione);
	}

	/**
	 * Aggiunge un'informazione a livello di Model
	 * 
	 * @param messaggio
	 *            il messaggio da comunicare
	 */
	protected void addInformazione(Messaggio messaggio) {
		if (messaggio == null) {
			model.addInformazione(null);
		} else {
			addInformazione(new Informazione(messaggio.getCodice(), messaggio.getDescrizione()));
		}
	}

	/**
	 * Controlla se vi sono degli errori.
	 * 
	 * @return <code>true</code> se vi sono degli errori; <code>false</code> in
	 *         caso contrario
	 */
	public boolean hasErrori() {
		return !model.getErrori().isEmpty();
	}

	/**
	 * Controlla se vi sono dei messaggi.
	 * 
	 * @return <code>true</code> se vi sono dei messaggi; <code>false</code> in
	 *         caso contrario
	 */
	public boolean hasMessaggi() {
		return !model.getMessaggi().isEmpty();
	}

	/**
	 * Controlla se vi sono delle informazioni.
	 * 
	 * @return <code>true</code> se vi sono delle informazioni;
	 *         <code>false</code> in caso contrario
	 */
	public boolean hasInformazioni() {
		return !model.getInformazioni().isEmpty();
	}

	/**
	 * @return the model
	 */
	@Override
	public M getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(M model) {
		this.model = model;
	}

	/**
	 * @param loginHandler
	 *            the loginHandler to set
	 */
	public void setLoginHandler(LoginHandler loginHandler) {
		this.loginHandler = loginHandler;
	}

	/**
	 * Invocato da Struts
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		if (sessionHandler == null) {
			sessionHandler = new SessionHandler();
		}
		sessionHandler.setSession(session);
	}

	/**
	 * @param sessionHandler
	 *            the sessionHandler to set
	 */
	public void setSessionHandler(SessionHandler sessionHandler) {
		this.sessionHandler = sessionHandler;
	}

	/**
	 * @return the session
	 */
	public Map<String, Object> getSession() {
		return session;
	}

	/**
	 * @return the loginHandler
	 */
	public LoginHandler getLoginHandler() {
		return loginHandler;
	}

	/**
	 * @return the sessionHandler
	 */
	public SessionHandler getSessionHandler() {
		return sessionHandler;
	}

	/**
	 * Ottiene il crumb precedente a partire dallo stack dei breadcrumbs.
	 * 
	 * @return the previousCrumb
	 */
	public Crumb getPreviousCrumb() {
		final String methodName = "getPreviousCrumb";
		// Il valore di default quando non vi sono precedenti actions è null
		// Qualora sulla pagina si trovi un null, l'indietro deve redirigere
		// verso il cruscotto
		Crumb previousCrumb = null;
		BreadCrumbTrail trail = sessionHandler.getParametro(
				CommonSessionParameter.BREADCRUMB_TRAIL, BreadCrumbTrail.class);
		try {
			int numeroCrumbs = trail.getCrumbs().size();
			// Ottengo il crumb precedente a quello attuale
			previousCrumb = trail.getCrumbs().get(numeroCrumbs - 2);
		} catch (Exception e) {
			log.debug(methodName,
					"Il trail delle azioni precedenti non contiene sufficienti crumbs");
		}
		return previousCrumb;
	}

	/**
	 * Instanzia automaticamente il Model. <br>
	 * Pu&ograve; essere sovrascritto in casi particolari.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected M instantiateNewModel() {
		final String methodName = "instantiateNewModel";
		try {
			// task-xxxxx
			Class<?>[] genericTypeArguments = GenericTypeResolver.resolveTypeArguments(
					this.getClass(), GenericAction.class);
			return (M) genericTypeArguments[0].newInstance();
		} catch (InstantiationException e) {
			IllegalArgumentException exception = new IllegalArgumentException(
					"Errore instanziamento automatico del Model. "
							+ "Deve esistere un costruttore vuoto. Per esigenze più complesse "
							+ "sovrascrivere il metodo instantiateNewModel a livello di action.", e);
			log.error(methodName, exception, exception);
			throw exception;
		} catch (IllegalAccessException e) {
			IllegalArgumentException exception = new IllegalArgumentException(
					"Errore instanziamento automatico Model. "
							+ "Il costruttore vuoto non è accessibile.", e);
			log.error(methodName, exception, exception);
			throw exception;
		} catch (Exception e) {
			IllegalArgumentException exception = new IllegalArgumentException(
					"Errore instanziamento automatico Model. ", e);
			log.error(methodName, exception, exception);
			throw exception;
		}
	}

	/**
	 * Effettua un log per le ServiceRequest.
	 * 
	 * @param req
	 *            la request da loggare
	 */
	public void logServiceRequest(ServiceRequest req) {
		log.logXmlTypeObject(req, "Service Request param");
	}

	/**
	 * Effettua un log per le ServiceResponse.
	 * 
	 * @param response
	 *            la response da loggare
	 */
	public void logServiceResponse(ServiceResponse response) {
		log.logXmlTypeObject(response, "Service Response param");
	}

	/**
	 * Restituisce l'azione chiamante.
	 * 
	 * @return l'azione chiamante
	 */
	public String getAzioneChiamante() {
		return getPreviousCrumb().getAction();
	}

	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}
	

	/**
	 * Per la lettura dei dati serializzati
	 * @param in lo stream dei dati in entrata
	 * @throws IOException in caso di errore di IO
	 * @throws ClassNotFoundException in caso di errore di construzione delle classi
	 */
//	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
//		in.defaultReadObject();
//		this.log = new LogUtil(this.getClass());
//	}

	
	/**
	 * Verifica se l'azione indicata e' presente tra quelle consentite 
	 * @param nomeAzione
	 * @return
	 */
	protected boolean isAzioneConsentita(AzioneConsentitaEnum azione) {
		return AzioneConsentitaEnum.isConsentito(azione, sessionHandler.getAzioniConsentite());
	}
	
	protected boolean isAzioneRichiesta(AzioneConsentitaEnum azione) {
		return azione.getNomeAzione().equals(sessionHandler.getAzioneRichiesta().getAzione().getNome());
	}
	
	public String[] getAzioniTipoFile(String codiceTipoFile) {

		TipoFileHandler tipoFileHandler = getTipoFileHandler(codiceTipoFile);

		return tipoFileHandler == null ? null : tipoFileHandler.getAzioni();
	}

	protected TipoFileHandler getTipoFileHandler(String codiceTipoFile) {
		TipoFileIntf tipoFileIntf = getTipoFileIntf(codiceTipoFile);

		if (tipoFileIntf == null) {
			return null;
		}
		
		if (tipoFileIntf.getHandler() == null) {
			return null;
		}
		
		return tipoFileIntf.getHandler();
	}

	protected TipoFileIntf getTipoFileIntf(String codiceTipoFile) {
		throw new UnsupportedOperationException("Must implement in subclasses");
	}

	
	protected String getParametroConfigurazioneEnte(ParametroConfigurazioneEnteEnum parametro, Richiedente richiedente) throws WebServiceInvocationFailureException {
		GetParametroConfigurazioneEnte getParametroConfigurazioneEnte = new GetParametroConfigurazioneEnte();
		getParametroConfigurazioneEnte.setNomeParametro(parametro.getNomeParametro());
		getParametroConfigurazioneEnte.setRichiedente(richiedente);
		
		GetParametroConfigurazioneEnteResponse getParametroConfigurazioneEnteResponse = 
				coreService.getParametroConfigurazioneEnte(getParametroConfigurazioneEnte);
		
		checkServiceResponse(GetParametroConfigurazioneEnte.class, getParametroConfigurazioneEnteResponse);
		
		
		return getParametroConfigurazioneEnteResponse.getValoreParametro();
	}

	protected String getParametroConfigurazioneEnte(ParametroConfigurazioneEnteEnum parametro) {
		return sessionHandler.getEnte().getParametroConfigurazione(parametro);
	}

	protected void checkServiceResponse(Class<? extends ServiceRequest> reqClass, ServiceResponse res) throws WebServiceInvocationFailureException {
		if (res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(reqClass, res));
		}
	}

		
	public String createErrorInServiceInvocationString(Class<? extends ServiceRequest> cls, ServiceResponse res) {

		StringBuilder sb = new StringBuilder()
			.append("Errore nell'invocazione del servizio ")
			.append(cls.getSimpleName());
		if(res != null && res.getErrori() != null) {

			for(Errore errore : res.getErrori()) {
				sb.append(" - ")
					.append(errore.getTesto());
			}
		}

		return sb.toString();
	}

	protected void throwSystemExceptionErroreDiSistema(Exception e) {
		throw new SystemException(ErroreCore.ERRORE_DI_SISTEMA.getErrore(e.getMessage()));
	}
	
	public void initFileDownload(String name, MimeType mimeType, byte[] data)  {
		initFileDownload(name, mimeType.getMimeType(), data);
	}

	public void initFileDownload(String name, String mimeType, byte[] data)  {
		request.put(DownloadFileAction.FILE, new File(name, mimeType, data));
	}
}
