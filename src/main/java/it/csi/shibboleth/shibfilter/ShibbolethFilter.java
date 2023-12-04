/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.shibboleth.shibfilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonapp.util.policy.iride.Identita;
import it.csi.siac.siaccommonapp.util.policy.iride.MalformedIdTokenException;

/**
 * Duplicazione filtro per gestione Shibboleth da versione standard CSI
 * @version 1.0.0 - 17/04/2020
 */
public class ShibbolethFilter implements Filter {
	private static final LogUtil LOG = new LogUtil(ShibbolethFilter.class);
	public static final String AUTH_ID_MARKER = "Shib-Iride-IdentitaDigitale";

	private String irideIdSessionAttr;
	private String noCheckPage;

	/**
	 * @return the irideIdSessionAttr
	 */
	public String getIrideIdSessionAttr() {
		return this.irideIdSessionAttr;
	}

	/**
	 * @param irideIdSessionAttr the irideIdSessionAttr to set
	 */
	public void setIrideIdSessionAttr(String irideIdSessionAttr) {
		this.irideIdSessionAttr = irideIdSessionAttr;
	}

	/**
	 * @return the noCheckPage
	 */
	public String getNoCheckPage() {
		return this.noCheckPage;
	}

	/**
	 * @param noCheckPage the noCheckPage to set
	 */
	public void setNoCheckPage(String noCheckPage) {
		this.noCheckPage = noCheckPage;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fchn) throws IOException, ServletException {
		final String methodName = "doFilter";
		HttpServletRequest hreq = (HttpServletRequest) req;
		if (hreq.getSession().getAttribute(irideIdSessionAttr) == null) {
			String marker = getToken(hreq);
			if (marker != null) {
				try {
					Identita identita = new Identita(this.normalizeToken(marker));
					LOG.debug(methodName, "Inserito in sessione marcatore IRIDE: " + identita);
					hreq.getSession().setAttribute(irideIdSessionAttr, identita);
				} catch (MalformedIdTokenException mite) {
					LOG.error(methodName, mite.toString(), mite);
				}
			} else if (mustCheckPage(hreq.getRequestURI())) {
				LOG.error(methodName, "Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
				throw new ServletException("Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
			}
		}
		fchn.doFilter(req, resp);
	}

	@Override
	public void destroy() {
		// Nothing to do
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.setIrideIdSessionAttr(config.getInitParameter("IRIDE_ID_SESSIONATTR"));
		this.setNoCheckPage(config.getInitParameter("NO_CHECK_PAGE"));
	}

	private boolean mustCheckPage(String requestURI) {
		return this.noCheckPage.indexOf(requestURI) <= -1;
	}

	private String getToken(HttpServletRequest httpreq) {
		return httpreq.getHeader("Shib-Iride-IdentitaDigitale");
	}

	private String normalizeToken(String token) {
		return token;
	}

}
