/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.login;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import it.csi.siac.siaccommonapp.util.log.LogWebUtil;
import it.csi.siac.siaccorser.model.Operatore;

public class TestFileLoginHandler implements LoginHandler {
	private static final long serialVersionUID = 6589268336444125090L;

	protected transient LogWebUtil log = new LogWebUtil(this.getClass());

	/**
	 * @return the operatore
	 * @throws Exception 
	 * @throws IOException
	 */
	@Override
	public Operatore getOperatore(Map<String, Object> session) {
		try {
			return getOperatoreFromProperty();
		} catch (IOException e) {
			log.error("TestFileLoginHandler::getOperatore", e.getMessage(), e);
		}
		
		return null;
	}

	/**
	 * @return the operatore
	 * @throws IOException
	 */
	private Operatore getOperatoreFromProperty() throws IOException {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(System.getProperty("file.loginHandler.location"));
			prop.load(input);
		} finally {
			if(input != null) {
				input.close();
			}
		}

		String[] tmp = prop.getProperty("utente").split(":");

		String codiceFiscale = tmp[0];
		String nome = tmp[1];

		Operatore operatore = new Operatore();
		operatore.setNome(nome);
		operatore.setCognome("");
		operatore.setCodiceFiscale(codiceFiscale);

		return operatore;
	}
}
