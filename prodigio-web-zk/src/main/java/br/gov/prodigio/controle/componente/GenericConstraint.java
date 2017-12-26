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
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import br.gov.prodigio.comuns.utils.Context;
import br.gov.prodigio.controle.ProCtr;

public class GenericConstraint implements Constraint {
	SimpleConstraint simpleConstraint = null;

	@Override
	public void validate(Component comp, Object value) throws WrongValueException {

		if (comp instanceof FieldValidator && simpleConstraint != null) {
			try {
				FieldValidator input = (FieldValidator) comp;

				String evento = (String) Context.getAttribute("evento");
				// validação padrão(ao sair do campo)
				if (input.getValidarQuando() == null || input.getValidarQuando().equals("")) {
					simpleConstraint.validate(comp, value);

					// validação feita quando o atributo ValidarQuando for
					// especificado
				} else {
					if (evento != null && !evento.equals("")) {
						// valida apenas quando acionado o evento especificado
						// em validarQuando. EX.:
						// validarQuando="salvar",
						// valida apenas quando o botao salvar for pressionado
						if (input.getValidarQuando().equals(evento)) {
							simpleConstraint.validate(comp, value);
							// Não valida para o evento especificado em
							// validarQuando. EX.: validarQuando="!salvar".
							// Valida
							// para
							// todos os outros botões pressionado exceto
							// "salvar"
						} else if (input.getValidarQuando().indexOf("!") != -1 && !input.getValidarQuando().replace("!", "").equals(evento)) {
							simpleConstraint.validate(comp, value);
						}
					}
				}
			} catch (WrongValueException e) {
				Component parente = comp;
				Tabpanel tabpanel = null;
				while(parente!=null && !(parente instanceof Tabbox)){
					parente = parente.getParent();
					if(parente instanceof Tabpanel){
						tabpanel = (Tabpanel) parente;
						Tab tab = tabpanel.getLinkedTab();
						tab.setSelected(true);
					}				
				}
				throw e;
			}
		}

		Window window = ProCtr.findWindow(comp);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.valitadeField(comp, value);

	}

	public SimpleConstraint getSimpleConstraint() {
		return simpleConstraint;
	}

	public void setSimpleConstraint(SimpleConstraint simpleConstraint) {
		this.simpleConstraint = simpleConstraint;
	}

	public static void configuraConstraint(InputElement imp) {
		GenericConstraint constraint = new GenericConstraint();
		if ((imp.getConstraint() instanceof SimpleConstraint)) {
			constraint.setSimpleConstraint((SimpleConstraint) imp.getConstraint());
		}
		if (imp.getConstraint() == null || imp.getConstraint() instanceof SimpleConstraint) {
			imp.setConstraint(constraint);
		}
	}
}
