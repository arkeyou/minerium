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
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;

public class CpfboxBind extends TextboxBind implements AfterCompose, FieldValidator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7424727966318992050L;

	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		GenericConstraint.configuraConstraint(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.getProAnnotateDataBinderHelper().adicionaAnotacaoParaCampoCpf(this);
		GenericConstraint.configuraConstraint(this);
		this.setSclass("form-control");

	}

	private void configuraConstraint() {
		GenericConstraint constraint = new GenericConstraint();
		if (this.getConstraint() instanceof SimpleConstraint) {
			constraint.setSimpleConstraint((SimpleConstraint) this.getConstraint());
		}
		this.setConstraint(constraint);
	}

}
