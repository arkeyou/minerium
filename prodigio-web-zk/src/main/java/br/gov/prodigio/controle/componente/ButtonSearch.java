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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.visao.helper.ProHelperView;

public class ButtonSearch extends Button implements AfterCompose {

	private static final long serialVersionUID = -7697679390092381138L;
	private String bandboxname;
	private String nomeProximoCampo;
	private String identificador;
	private String atributoQueSeraVisualizado;
	private String validarQuando = "";

	@SuppressWarnings("unchecked")
	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		addEventListener(Events.ON_CLICK, new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				Bandboxbind bandboxbind = (Bandboxbind) e.getTarget().getParent().getFirstChild();
				Clients.evalJavaScript("document.getElementById('" + bandboxbind.getUuid() + "-btn').click()");

			}
		});

		addEventListener(Events.ON_FOCUS, new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				ctr.onFocusCtl(e);
			}
		});
	}

	public Component recuperaComponentParaAninhar(String nomeDoObjeto) throws Exception {
		Component parent = ProHelperView.recuperaParentContexto(this);
		Component comp = ProHelperView.recuperaComponent(parent, nomeDoObjeto);
		if (comp == null) {
			throw new Exception("Componente:<" + nomeDoObjeto + "> não encontrato.");
		}
		return comp;
	}

	public String getBandboxname() {
		return bandboxname;
	}

	public void setBandboxname(String bandboxname) {
		this.bandboxname = bandboxname;
	}

	public String getNomeProximoCampo() {
		return nomeProximoCampo;
	}

	public void setNomeProximoCampo(String nomeProximoCampo) {
		this.nomeProximoCampo = nomeProximoCampo;
	}

	public String getAtributoQueSeraVisualizado() {
		return atributoQueSeraVisualizado;
	}

	public void setAtributoQueSeraVisualizado(String atributoQueSeraVisualizado) {
		this.atributoQueSeraVisualizado = atributoQueSeraVisualizado;
	}

	public String getValidarQuando() {
		return validarQuando;
	}

	public void setValidarQuando(String validarQuando) {
		this.validarQuando = validarQuando;
	}

}
