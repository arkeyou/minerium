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

import java.util.Collection;

import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Window;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.enumerations.Operacao;
import br.gov.prodemge.ssc.interfaces.IUsuario;
import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.visao.helper.ProHelperView;

public class DivSecurity extends Div implements AfterCompose {

	private static final long serialVersionUID = 7644458046813596916L;

	private String nomeRecurso;
	private Boolean useDisabled;
	private Boolean useVisible;

	public String getNomeRecurso() {
		return nomeRecurso;
	}

	public void setNomeRecurso(String nomeRecurso) {
		this.nomeRecurso = nomeRecurso;
	}
	
	public Boolean getUseDisabled() {
		return useDisabled;
	}

	public void setUseDisabled(Boolean useDisplay) {
		this.useDisabled = useDisplay;
	}

	public Boolean getUseVisible() {
		return useVisible;
	}

	public void setUseVisible(Boolean useVisible) {
		this.useVisible = useVisible;
	}

	@Override
	public void afterCompose() {
		final Window window = ProCtr.findWindow(this);
//		window.getPage().addEventListener("onChange",new SerializableEventListener() {
//		    public void onEvent(Event event) {
//		    	validaPermissao(window);
//		    }
//		});
		
		validaPermissao(window);
	}

	private void validaPermissao(Window window) {
		if (nomeRecurso!=null && !nomeRecurso.equals("")) {
			IUsuario usuario = (IUsuario) window.getAttribute(Constantes.USUARIO_AUTENTICADO, SESSION_SCOPE);
			boolean visible = ProHelperView.perfilPossuiFuncaoPorNome(usuario, Operacao.VER, nomeRecurso);
			if (useVisible != null && useVisible.booleanValue() == true) {
				setVisible(visible);
			}
			if (useDisabled != null && useDisabled.booleanValue() == true) {
				if (!visible) {
					Collection<Component> componentes = this.getChildren();
					desabilita(componentes);
				}
			}
		}
	}

	private void desabilita(Collection<Component> componentes) {
		if(componentes!=null)
			for (Component abstractComponent : componentes) {
				if(abstractComponent instanceof org.zkoss.zk.ui.ext.Disable){
					((org.zkoss.zk.ui.ext.Disable)abstractComponent).setDisabled(true);
				}
				desabilita(abstractComponent.getChildren());
			}
	}


}
