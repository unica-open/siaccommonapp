/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommonapp.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommonapp.model.DownloadFileModel;
import it.csi.siac.siaccorser.model.file.File;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DownloadFileAction extends GenericAction<DownloadFileModel> {
	private static final long serialVersionUID = 4993627706418364386L;

	public static final String FILE = String.format("%s:FILE", DownloadFileAction.class.getName());

	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";

		log.debugStart(methodName, "Preparazione della action");

		super.prepare();

		model.setFile((File) request.get(FILE));

		log.debugEnd(methodName, "");
	}

	@Override
	protected void initModel() {
		super.initModel();

		model.setTitolo(sessionHandler.getAzione().getTitolo());
	}

	@Override
	public String execute() throws Exception {
		return super.execute();
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(model.getFile().getContenuto());
	}

	public boolean getAllowCaching() {
		return false;
	}

	public String getFilename() {
		return model.getFile().getNome();
	}

	public int getContentLength() {
		return model.getFile().getContenuto().length;
	}

	public String getContentType() {
		return model.getFile().getMimeType();
	}

	public String getContentDisposition() {
		return null;
	}

}
