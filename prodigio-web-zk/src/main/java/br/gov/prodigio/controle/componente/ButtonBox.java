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

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.controle.ProTabularCtr;
import br.gov.prodigio.entidades.ProBaseVO;

public class ButtonBox extends Hbox implements AfterCompose {

	private static final long serialVersionUID = 6567878386126364932L;
	private boolean mostrarBotaoEditar = true;
	private boolean mostrarBotaoSalvar = true;
	private boolean mostrarBotaoExcluir = true;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void afterCompose() {
		final Button excluir = new Button();
		final Button salvar = new Button();
		final Button editar = new Button();
		Window window = ProCtr.findWindow(this);
		final ProTabularCtr ctrTabular = (ProTabularCtr) window.getAttribute(window.getId() + "$" + "composer");

		EventListener eventoEditar = new SerializableEventListener() {
			public void onEvent(Event e) throws Exception {
				Listitem listitem = recuperaListaItemDeButtonBox(((Button) e.getTarget()).getParent());
				editarLinha(listitem, ((Button) e.getTarget()).getParent());
			}
		};

		editar.addEventListener(Events.ON_CLICK, eventoEditar);
		editar.setImage("~./imagem/pencil-small.png");
		editar.setTooltiptext("Editar Linha");
		appendChild(editar);

		SerializableEventListener eventoSalvar = new SerializableEventListener() {
			public void onEvent(Event e) throws Exception {
				Listitem listitem = recuperaListaItemDeButtonBox(((Button) e.getTarget()).getParent());
				check(listitem);
				ctrTabular.salvarTabular((Button) e.getTarget());
				escondeBotaoSalvarExcluir(((Button) e.getTarget()).getParent());
				habilitaDesabilitaCampos(listitem, true);

			}
		};
		salvar.addEventListener(Events.ON_CLICK, eventoSalvar);
		salvar.setImage("~./imagem/tick-small.png");
		salvar.setTooltiptext("Salvar Linha");
		salvar.setVisible(false);
		appendChild(salvar);

		SerializableEventListener eventoExcluir = new SerializableEventListener() {
			public void onEvent(Event e) throws Exception {

				if (Messagebox.show("Você deseja excluir o registro?", "Confimação de Deleção", Messagebox.YES | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.YES) {

					ctrTabular.excluirTabular((Button) e.getTarget());
					escondeBotaoSalvarExcluir(((Button) e.getTarget()).getParent());

				}

			}
		};
		excluir.addEventListener(Events.ON_CLICK, eventoExcluir);
		excluir.setImage("~./imagem/cross-small.png");
		excluir.setTooltiptext("Excluir Linha");
		excluir.setVisible(false);
		appendChild(excluir);

		addEventListener(Events.ON_CREATE, new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				ctrTabular.aplicarSegurancaNoBotaoTabular(e);
			}
		});

	}

	private void escondeBotaoSalvarExcluir(Component buttonBox) {

		List children = buttonBox.getChildren();
		for (Object object : children) {
			if (object instanceof org.zkoss.zul.Button) {
				org.zkoss.zul.Button button = (org.zkoss.zul.Button) object;
				if (button.getTooltiptext().equalsIgnoreCase("Salvar Linha") || button.getTooltiptext().equalsIgnoreCase("Excluir Linha")) {
					button.setVisible(false);
				}

				else if (button.getTooltiptext().equalsIgnoreCase("Editar Linha") && mostrarBotaoEditar) {
					button.setVisible(true);
				}
			}
		}
	}

	private void mostraBotaoSalvarExcluir(Component buttonBox) {

		List children = buttonBox.getChildren();
		for (Object object : children) {
			if (object instanceof org.zkoss.zul.Button) {
				org.zkoss.zul.Button button = (org.zkoss.zul.Button) object;
				if (button.getTooltiptext().equalsIgnoreCase("Salvar Linha") && mostrarBotaoSalvar) {
					button.setVisible(true);
				} else if (button.getTooltiptext().equalsIgnoreCase("Excluir Linha") && mostrarBotaoExcluir) {
					button.setVisible(true);
				}

				else if (button.getTooltiptext().equalsIgnoreCase("Editar Linha")) {
					button.setVisible(false);
				}
			}
		}
	}

	private Listitem recuperaListaItemDeButtonBox(Component buttonBox) {
		Component component = buttonBox.getParent();
		while (component != null && !(component instanceof Listitem)) {
			component = component.getParent();
		}
		if (component instanceof Listitem) {
			return (Listitem) component;
		} else {
			return null;
		}
	}

	private boolean isNovoListItem(Listitem listitem) {
		if (listitem != null) {
			if (listitem.getValue() instanceof ProBaseVO) {
				ProBaseVO ent = (ProBaseVO) listitem.getValue();
				if (ent.getId() != null) {
					return false;
				}
			}
		}
		return true;
	}

	public void editarNovaLinha(Component buttonBox) {
		Listitem listitem = recuperaListaItemDeButtonBox(buttonBox);
		if (isNovoListItem(listitem)) {
			editarLinha(listitem, buttonBox);
		}
	}

	private void editarLinha(Listitem listitem, Component buttonBox) {
		mostraBotaoSalvarExcluir(buttonBox);
		habilitaDesabilitaCampos(listitem, false);
	}

	private void habilitaDesabilitaCampos(Component com, Boolean arg) {
		List children = com.getChildren();
		for (Object object : children) {
			if (object instanceof InputElement) {
				InputElement inputElement = (InputElement) object;
				inputElement.setInplace(arg);
				inputElement.setReadonly(arg);
			}
			if (object instanceof Component) {
				habilitaDesabilitaCampos((Component) object, arg);
			}
			if (object instanceof RadiogroupBind) {
				RadiogroupBind radiogroupBind = (RadiogroupBind) object;
				radiogroupBind.setDisabled(arg);
			}
		}
	}

	private void check(Component component) {
		checkIsValid(component);

		List<Component> children = component.getChildren();
		for (Component each : children) {
			check(each);
		}
	}

	private void checkIsValid(Component component) {
		if (component instanceof InputElement) {
			if (!((InputElement) component).isValid()) {
				((InputElement) component).getText();
			}
		}
	}

	public boolean isMostrarBotaoEditar() {
		return mostrarBotaoEditar;
	}

	public void setMostrarBotaoEditar(boolean mostrarBotaoEditar) {
		this.mostrarBotaoEditar = mostrarBotaoEditar;
	}

	public boolean isMostrarBotaoSalvar() {
		return mostrarBotaoSalvar;
	}

	public void setMostrarBotaoSalvar(boolean mostrarBotaoSalvar) {
		this.mostrarBotaoSalvar = mostrarBotaoSalvar;
	}

	public boolean isMostrarBotaoExcluir() {
		return mostrarBotaoExcluir;
	}

	public void setMostrarBotaoExcluir(boolean mostrarBotaoExcluir) {
		this.mostrarBotaoExcluir = mostrarBotaoExcluir;
	}

}
