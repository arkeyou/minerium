/*******************************************************************************
 * Minerium Meta Framework
 *
 * Licença: GNU Lesser General Public License (LGPL), version 3.
 *  
 * Copyright (C) (2013-2018) Prodemge. Todos os direitos reservados.
 *   
 * Este arquivo é parte do Minerium Meta Framework
 * O Minerium Meta Framework é um software livre; você pode redistribuí-lo e/ou 
 * modificá-lo dentro dos termos da GNU Lesser General Public License (LGPL), version 3.
 *
 * Este framework é distribuído na esperança de que possa ser  útil, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implícita de ADEQUAÇÃO
 * a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. 
 * Ver arquivo LICENSE.md no diretório raiz ou acessar <https://www.gnu.org/licenses/lgpl.html>
 *******************************************************************************/
/** * Classe criada em 01/01/2010 * Este código pode ser usado apenas para fins não comerciais * @author Sândalo Bessa */
package br.gov.prodigio.controle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.interfaces.IUnidade;
import br.gov.prodigio.visao.helper.ProMessageHelper;

//TODO [glauco.cardoso] mudar de WindowPrincipal para ControllerPrincipal
public class WindowPrincipal extends WindowPrincipalUnsecured {
	// public static final String UNIDADE_LOGADA = "unidadeLogada";
	private static final Logger log = LoggerFactory.getLogger(WindowPrincipal.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 266151031140143309L;
	private List<IUnidade> listaUnidade;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		IUnidade unidade = (IUnidade) getTela().getAttribute(Constantes.UNIDADE_AUTENTICADA, Component.SESSION_SCOPE);
		if (unidade != null) {
			try {
				Page index = (Page) getPage().getDesktop().getPages().toArray()[0];
				AbstractComponent wind = (AbstractComponent) index.getFellow("index");
				getTela().setAttribute(Constantes.UNIDADE_AUTENTICADA, unidade, Component.SESSION_SCOPE);
				Label label = (Label) wind.getFellowIfAny("labelUnidade");
				label.setValue(unidade.getNome());
				Label labelNomeUsuarioCabecalho = (Label) wind.getFellowIfAny("nomeUsuarioCabecalho");
				labelNomeUsuarioCabecalho.setValue(getNomeUsuario());
				Label labelDataCabecalho = (Label) wind.getFellowIfAny("labelData");
				SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
				labelDataCabecalho.setValue(out.format(new Date()));
			} catch (Exception e) {
				log.warn("Não foi possivel montar variaveis de cabeçalho...:" + e.getMessage());
			}
		}

	}

	public void configuraUnidadeLogada(IUnidade unidade) {
		getTela().setAttribute(Constantes.UNIDADE_AUTENTICADA, unidade, Component.SESSION_SCOPE);
	}

	@Override
	public void logout() {
		if (desejaRealmente("Deseja realmente sair?")) {
			Sessions.getCurrent().invalidate();
			HttpSession s = (HttpSession) Sessions.getCurrent().getNativeSession();
			s.invalidate();
			Executions.sendRedirect("/timeout.zul");
		}

	}

	@Override
	public void SSOGlobalLogout() {
		if (desejaRealmente("Deseja realmente sair?")) {
			Executions.sendRedirect("/?GLO=true");
		}
	}

	@Override
	public boolean desejaRealmente(String msg) {
		return Messagebox.OK == Messagebox.show(msg, "Atenção!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
	}

	public List<IUnidade> getListaUnidade() {
		return listaUnidade;
	}

	public void setListaUnidade(List<IUnidade> listaUnidade) {
		this.listaUnidade = listaUnidade;
	}

}
