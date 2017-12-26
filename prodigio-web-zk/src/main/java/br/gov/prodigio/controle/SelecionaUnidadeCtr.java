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
package br.gov.prodigio.controle;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.interfaces.IUnidade;
import br.gov.prodigio.visao.helper.ProMessageHelper;

@SuppressWarnings("rawtypes")
public class SelecionaUnidadeCtr extends GenericForwardComposer {

	private Window tela;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		tela = (Window) comp;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4531173886447694353L;

	public void configuraUnidadeLogada(Component component, Window window) {

		Listitem list = (Listitem) component;

		IUnidade unidade = list!=null?(IUnidade) list.getValue():null;

		if (null != unidade) {
			// Pega a window e fecha
			tela.setAttribute(Constantes.UNIDADE_AUTENTICADA, unidade, Component.SESSION_SCOPE);

			// Após selecionar a unidade manda para home.zul
			Executions.sendRedirect("/");
		} else {
			ProMessageHelper.getInstance().emiteMensagemErro("Favor selecionar uma unidade!");
		}
	}

	public Window getTela() {
		return tela;
	}

	public void setTela(Window tela) {
		this.tela = tela;
	}

}
