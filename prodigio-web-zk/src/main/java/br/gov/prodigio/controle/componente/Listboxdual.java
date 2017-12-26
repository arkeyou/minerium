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
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

public class Listboxdual extends Hbox implements AfterCompose, Comparable<Listboxdual> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5334233453991015856L;
	private String nomeDoObjeto;

	private Listbox origem;
	private Listbox destino;
	private Object objetoPai;

	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.getDualboxRegister().add(this);
		ctr.getProAnnotateDataBinderHelper().adicionaAnotacaoParaArgumentosListDual(this);
	}

	public String getNomeDoObjeto() {
		return nomeDoObjeto;
	}

	public void setNomeDoObjeto(String nomeDoObjeto) {
		this.nomeDoObjeto = nomeDoObjeto;
	}

	public Listbox getOrigem() {
		return origem;
	}

	public Listbox getDestino() {
		return destino;
	}

	public void setOrigem(Listbox origem) {
		this.origem = origem;
	}

	public void setDestino(Listbox destino) {
		this.destino = destino;
	}

	public Object getObjetoPai() {
		return objetoPai;
	}

	public void setObjetoPai(Object objetoPai) {
		this.objetoPai = objetoPai;
	}

	@Override
	public int compareTo(Listboxdual o) {// Verificar se objetos em lista não terão problemas
		return this.getNomeDoObjeto().compareTo(o.getNomeDoObjeto());
	}

}
