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
package br.gov.prodigio.visao.helper;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;

import br.gov.prodigio.comuns.excecoes.ViolacaoDeRegraEx;
import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.entidades.ProBaseVO;

public class ProMessageHelper implements Serializable {
	private static final Logger log = LoggerFactory.getLogger(ProMessageHelper.class);

	protected ProCtr<? extends ProBaseVO> ctr;

	protected ProMessageHelper() {
	}

	protected ProMessageHelper(ProCtr<? extends ProBaseVO> ctr) {
		this.ctr = ctr;
	}

	private static ProMessageHelper helperInstance;

	public static ProMessageHelper getInstance() {
		if (helperInstance == null) {
			return create();
		} else {
			return helperInstance;
		}
	}

	public static ProMessageHelper getInstance(ProCtr<? extends ProBaseVO> ctr) {
		if (helperInstance == null) {
			return create(ctr);
		} else {
			return helperInstance;
		}
	}

	private synchronized static ProMessageHelper create() {
		helperInstance = new ProMessageHelper();
		return helperInstance;
	}

	private synchronized static ProMessageHelper create(ProCtr<? extends ProBaseVO> ctr) {
		helperInstance = new ProMessageHelper(ctr);
		return helperInstance;
	}

	public void emiteMensagemErro(WrongValueException e) {
		Clients.clearBusy();
		Messagebox.show(e.getMessage(), e.getComponent().getId(), Messagebox.OK, Messagebox.ERROR);
	}

	public void emiteMensagemErro(String e) {
		Clients.clearBusy();
		Messagebox.show(e, "Erro!!!", Messagebox.OK, Messagebox.ERROR);
	}

	public void emiteMensagemAtencao(String e) {
		Clients.clearBusy();
		Messagebox.show(e, "Atenção!", Messagebox.OK, Messagebox.EXCLAMATION);
	}

	public void emiteMensagemCampoNaoPreenchido(String e) {
		Clients.clearBusy();
		Messagebox.show(String.format("Campo %s não preenchido!", e), "Atenção!", Messagebox.OK, Messagebox.EXCLAMATION);
	}

	public void emiteMensagemCampoObrigatorio(String e) {
		Clients.clearBusy();
		Messagebox.show(String.format("O Campo %s é obrigatório!", e), "Atenção!", Messagebox.OK, Messagebox.EXCLAMATION);
	}

	public void emiteMensagemAtencao(Exception e) {
		Clients.clearBusy();
		if (e instanceof ViolacaoDeRegraEx) {
			ViolacaoDeRegraEx violacaoDeRegraEx = (ViolacaoDeRegraEx) e;
			if (violacaoDeRegraEx.getMensagensParaCamposEspecificos().values().size() >= 1) {
				Set<Entry<String, String>> entrySet = violacaoDeRegraEx.getMensagensParaCamposEspecificos().entrySet();
				int cont = 0;
				for (Entry<String, String> entry : entrySet) {
					Component manutencao = ctr.getTela().getFellow("manutencao");
					Component component = null;
					component = ProHelperView.recuperaComponent(manutencao, entry.getKey());// tenta por id
					if (component == null) {//
						component = ProHelperView.recuperaComponent(manutencao, "classecontrole.objetoAtual." + entry.getKey());// tenta por nome do objeto
					}
					if (cont == 0 && component != null) {// Apenas o primeiro erro terá sua aba selecionada
						exibeAbaComCamposComErros(manutencao, component);
					}
					if (component == null)
						Clients.wrongValue(ctr.getTela(), entry.getValue());
					else {
						Clients.wrongValue(component, entry.getValue());
					}
					cont++;
				}
			} else {
				Messagebox.show(e.getMessage(), "Atenção!", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} else {
			Messagebox.show(e.getMessage(), "Atenção!", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	protected void exibeAbaComCamposComErros(Component manutencao, Component component) {
		Component parent = component.getParent();
		while (parent != manutencao && parent != null) {
			if (parent instanceof Tabpanel) {
				Tabpanel tabpanel = (Tabpanel) parent;
				Tab tabbox = tabpanel.getLinkedTab();
				tabbox.setSelected(true);
				break;
			}
			parent = parent.getParent();
		}
	}

	public void emiteMensagemSucesso() {
		/*
		 * try {
		 */Clients.clearBusy();
		Messagebox.show("Registro gravado com sucesso!!!", "Informação", Messagebox.OK, Messagebox.INFORMATION);
		/*
		 * } catch (InterruptedException e) { log.error("Erro ao emitir mensagem", e); }
		 */}

	public void emiteMensagemSucesso(String msg) {
		Clients.clearBusy();
		Messagebox.show(msg, "Informação", Messagebox.OK, Messagebox.INFORMATION);
	}

	public void emiteMensagemSucesso(String msg, Component component) {
		Clients.clearBusy();
		Clients.showNotification(msg, "info", component, "top_center", 2500);
	}

	public void emiteMensagemExclusaoComSucesso() {
		/*
		 * try { Clients.clearBusy();
		 */Messagebox.show("Registro excluído com sucesso!!!", "Informação", Messagebox.OK, Messagebox.INFORMATION);
		/*
		 * } catch (InterruptedException e) { log.error("Erro ao emitir mensagem", e); }
		 */}

	public boolean desejaRealmenteExcluir() {
		/*
		 * try {
		 */return Messagebox.OK == Messagebox.show("Deseja continuar?", "Atenção!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
		/*
		 * } catch (InterruptedException e) { log.error("Erro na confirmação de exclusão ", e); return false; }
		 */}

	public boolean desejaRealmenteExcluir(String mensagem) {
		/*
		 * try {
		 */return Messagebox.OK == Messagebox.show(mensagem, "Atenção!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
		/*
		 * } catch (InterruptedException e) { log.error("Erro na confirmação de exclusão ", e); return false; }
		 */}

	public boolean desejaRealmente(String msg) {
		/*
		 * try {
		 */return Messagebox.OK == Messagebox.show(msg, "Atenção!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
		/*
		 * } catch (InterruptedException e) { log.error("Erro na confirmação de exclusão ", e); return false; }
		 */}

	public boolean desejaRealmenteConcluir() {
		/*
		 * try {
		 */return Messagebox.OK == Messagebox.show("Deseja realmente concluir? Após a conclusão o registro não poderá mais ser editado.", "Atenção!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
		/*
		 * } catch (InterruptedException e) { log.error("Erro na confirmação de exclusão ", e); return false; }
		 */}

	public void emiteMensagemErro(Exception e) {
		Clients.clearBusy();
		String mensagem = e.getMessage();
		if (mensagem.contains("java.lang.Exception: org.hibernate.QueryException: could not resolve property: nome of:")) {
			mensagem = mensagem.replace("java.lang.Exception: org.hibernate.QueryException: could not resolve property: nome of:", "");
			Messagebox.show("Não existe o atributo nome na classe: " + mensagem + ". Esse atributo é padrão para ser exibido como em telas. Se o atributo não existir de fato, adicione-o como transiente.", "Erro!!!", Messagebox.OK, Messagebox.ERROR);
		} else if (e.getCause() != null && e.getCause().toString().contains("br.gov.prodigio.comuns.excessoes.ViolacaoDeRegraEx:")) {
			Messagebox.show(e.getCause().getMessage().replace("br.gov.prodigio.comuns.excessoes.ViolacaoDeRegraEx:", ""), "Erro!!!", Messagebox.OK, Messagebox.ERROR);
		} else {
			Messagebox.show(mensagem, "Erro!!!", Messagebox.OK, Messagebox.ERROR);
		}
	}

}
