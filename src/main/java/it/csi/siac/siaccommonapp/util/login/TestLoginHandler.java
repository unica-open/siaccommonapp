/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.login;

import java.util.Map;

import it.csi.siac.siaccorser.model.Operatore;


public class TestLoginHandler extends LoginHandler {

	private static final long serialVersionUID = 6589268336444125090L;

	/**
	 * @return the operatore
	 */
	@Override
	public Operatore getOperatore(Map<String, Object> session) {
		String codiceFiscale = (String)session.get("it.csi.siac.siaccruapp.login.test.codiceFiscaleOperatore");
		if (codiceFiscale==null) {
//			throw new IdentitaDigitaleNonConformeException("identita");
//			codiceFiscale = "AAAAAA00B77B000F"; // Demo 20
//			codiceFiscale = "AAAAAA00A11B000J"; // Demo 21
//			codiceFiscale = "AAAAAA00A11K000S"; // Demo 30
//			codiceFiscale = "AAAAAA00A11L000T"; // Demo 31

			// MULT
			//codiceFiscale = "AAAAAA00A11E000M"; // Demo 24
			// REGP
			codiceFiscale = "AAAAAA00A11C000K"; // Demo 22
			// CMTO
			//codiceFiscale = "AAAAAA00A11D000L"; // Demo 23
			//PROD
			//codiceFiscale = "MSPMRC71T30H355Z"; // Demo 24
		}
		Operatore operatore = new Operatore();
		operatore.setNome("Raffaela");
		operatore.setCognome("Montuori");
		operatore.setCodiceFiscale(codiceFiscale);
		return operatore;
	}
	
	

}
