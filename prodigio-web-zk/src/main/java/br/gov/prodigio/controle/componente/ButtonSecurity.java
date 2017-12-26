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
package br.gov.prodigio.controle.componente;

import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.enumerations.Operacao;
import br.gov.prodemge.ssc.interfaces.IUsuario;
import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.visao.helper.ProHelperView;

public class ButtonSecurity extends Button implements AfterCompose {

	private static final long serialVersionUID = 7644458046813596916L;

	private String nomeRecurso;

	public String getNomeRecurso() {
		return nomeRecurso;
	}

	public void setNomeRecurso(String nomeRecurso) {
		this.nomeRecurso = nomeRecurso;
	}

	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		IUsuario usuario = (IUsuario) window.getAttribute(Constantes.USUARIO_AUTENTICADO, SESSION_SCOPE);

		setVisible(ProHelperView.perfilPossuiFuncaoPorNome(usuario, Operacao.VER, nomeRecurso));

	}

}
