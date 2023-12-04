/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.policy.iride;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Lettore dell'identit&agrave; digitale
 * @version 1.0.0 - 17/04/2020
 */
public class Identita implements Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3452047680267308578L;

	public static final int AUTENTICAZIONE_USERNAME_PASSWORD_UNVERIFIED = 1;
	public static final int AUTENTICAZIONE_USERNAME_PASSWORD = 2;
	public static final int AUTENTICAZIONE_USERNAME_PASSWORD_PIN = 4;
	public static final int AUTENTICAZIONE_CERTIFICATO = 8;
	public static final int AUTENTICAZIONE_CERTIFICATO_FORTE = 16;

	private static final String SLASH = "/";

	private String nome;
	private String cognome;
	private String codFiscale;
	private String idProvider;
	private String timestamp;
	private int livelloAutenticazione;
	private String mac;

	/**
	 * Costruttore
	 * @param codFiscale codice fiscale
	 * @param nome nome
	 * @param cognome cognome
	 * @param idProvider id provider
	 * @param timestamp timestamp
	 * @param livelloAutenticazione livello autenticazione
	 */
	public Identita(String codFiscale, String nome, String cognome, String idProvider, String timestamp, int livelloAutenticazione) {
		this.codFiscale = codFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.idProvider = idProvider;
		this.timestamp = timestamp;
		this.livelloAutenticazione = livelloAutenticazione;
	}

	/**
	 * Costuttore vuoto
	 */
	public Identita() {
		this.codFiscale = null;
		this.nome = null;
		this.cognome = null;
		this.idProvider = null;
		this.timestamp = null;
		this.livelloAutenticazione = 0;
	}

	/**
	 * Costruttore con token
	 * @param token il token da parsificare
	 * @throws MalformedIdTokenException nel caso in cui il token non sia formalmente valido
	 */
	public Identita(String token) throws MalformedIdTokenException {
		int slash1Index = token.indexOf(47);
		if (slash1Index == -1) {
			throw new MalformedIdTokenException(token);
		}
		this.codFiscale = token.substring(0, slash1Index);
		int slash2Index = token.indexOf(47, slash1Index + 1);
		if (slash2Index == -1) {
			throw new MalformedIdTokenException(token);
		}
		this.nome = token.substring(slash1Index + 1, slash2Index);
		int slash3Index = token.indexOf(47, slash2Index + 1);
		if (slash3Index == -1) {
			throw new MalformedIdTokenException(token);
		}
		this.cognome = token.substring(slash2Index + 1, slash3Index);
		int slash4Index = token.indexOf(47, slash3Index + 1);
		if (slash4Index == -1) {
			throw new MalformedIdTokenException(token);
		}
		this.idProvider = token.substring(slash3Index + 1, slash4Index);
		int slash5Index = token.indexOf(47, slash4Index + 1);
		if (slash5Index == -1) {
			throw new MalformedIdTokenException(token);
		}
		this.timestamp = token.substring(slash4Index + 1, slash5Index);
		int slash6Index = token.indexOf(47, slash5Index + 1);
		if (slash6Index == -1) {
			throw new MalformedIdTokenException(token);
		}
		this.livelloAutenticazione = Integer
				.parseInt(token.substring(slash5Index + 1, slash6Index));
		this.mac = token.substring(slash6Index + 1);
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the cognome
	 */
	public String getCognome() {
		return this.cognome;
	}

	/**
	 * @param cognome the cognome to set
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * @return the codFiscale
	 */
	public String getCodFiscale() {
		return this.codFiscale;
	}

	/**
	 * @param codFiscale the codFiscale to set
	 */
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	/**
	 * @return the idProvider
	 */
	public String getIdProvider() {
		return this.idProvider;
	}

	/**
	 * @param idProvider the idProvider to set
	 */
	public void setIdProvider(String idProvider) {
		this.idProvider = idProvider;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the livelloAutenticazione
	 */
	public int getLivelloAutenticazione() {
		return this.livelloAutenticazione;
	}

	/**
	 * @param livelloAutenticazione the livelloAutenticazione to set
	 */
	public void setLivelloAutenticazione(int livelloAutenticazione) {
		this.livelloAutenticazione = livelloAutenticazione;
	}

	/**
	 * @return the mac
	 */
	public String getMac() {
		return this.mac;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(codFiscale)
				.append(nome)
				.append(cognome)
				.append(mac)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Identita)) {
			return false;
		}
		Identita other = (Identita) obj;
		return new EqualsBuilder()
				.append(this.codFiscale, other.codFiscale)
				.append(this.nome, other.nome)
				.append(this.cognome, other.cognome)
				.append(this.mac, other.mac)
				.isEquals();
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(codFiscale).append(SLASH)
				.append(nome).append(SLASH)
				.append(cognome).append(SLASH)
				.append(idProvider).append(SLASH)
				.append(timestamp).append(SLASH)
				.append(livelloAutenticazione).append(SLASH)
				.append(mac)
				.toString();
	}

	/**
	 * Ottiene la rappresentazione interna dell'identit&agrave;
	 * @return la rappresentazione interna
	 */
	public String getRappresentazioneInterna() {
		return new StringBuilder()
				.append(codFiscale).append(SLASH)
				.append(nome).append(SLASH)
				.append(cognome).append(SLASH)
				.append(idProvider).append(SLASH)
				.append(timestamp).append(SLASH)
				.append(livelloAutenticazione)
				.toString();
	}

}
