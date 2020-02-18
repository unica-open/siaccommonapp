/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.util.login;

import java.util.Map;

import it.csi.iride2.policy.entity.Identita;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.exception.IdentitaDigitaleNonConformeException;
import it.csi.siac.siaccorser.model.exception.UtenteNonAutenticatoException;

public class SsoBartLoginHandler extends LoginHandler { 

	private static final long serialVersionUID = 9190962361943589880L;

	/**
	 * @return the operatore
	 */
	@Override
	public Operatore getOperatore(Map<String, Object> session) {
		Operatore operatore = new Operatore();
		Object identita = session.get("edu.yale.its.tp.cas.client.filter.user");
		if (identita==null) {
			throw new UtenteNonAutenticatoException();
		}
		try {
			Identita identitaDigitale = new Identita(identita.toString());
			operatore.setNome(identitaDigitale.getNome());
			operatore.setCognome(identitaDigitale.getCognome());
			operatore.setCodiceFiscale(identitaDigitale.getCodFiscale());
		} catch(Exception ex) {
			throw new IdentitaDigitaleNonConformeException(identita);
		}
		return operatore;
	}

}
