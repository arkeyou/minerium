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

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.BindingListModelSet;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import br.gov.prodigio.comuns.excecoes.ViolacaoDeRegraEx;
import br.gov.prodigio.controle.componente.ButtonBox;
import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.entidades.ProVO;
import br.gov.prodigio.entidades.RecordDataSet;
import br.gov.prodigio.entidades.StatusDoRegistro;

@SuppressWarnings("deprecation")
public class ProTabularCtr<ENTITY extends ProBaseVO> extends ProCtr<ENTITY> {

	private static final long serialVersionUID = -5419459587609540810L;
	public static final Logger log = LoggerFactory.getLogger(ProTabularCtr.class);

	@Override
	public void doAfterCompose(Component tela) throws Exception {
		super.doAfterCompose(tela);
		pesquisar();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void novo() {
		super.novo();
		Set objetos = getListaDeObjetos();
		RecordDataSet recordDataSetAux = new RecordDataSet(new LinkedList());
		recordDataSetAux.add(objetoAtual);
		for (Object object : objetos) {
			recordDataSetAux.add(object);
		}
		BindingListModelSet listSelBind = new BindingListModelSet(recordDataSetAux, true);
		Listbox listbox;
		try {
			listbox = (Listbox) getWindowAtual().getFellow("listaSelecao");
			listbox.setModel(listSelBind);
		} catch (Exception e) {
			log.error("Erro ao criar um novo objeto ", e);
		}
	}

	@Override
	protected Set<ProBaseVO> listarBaseadoNoExemplo(ProBaseVO exemplo, ProBaseVO exemplo2, int primeiroRegistro, int quantidadeDeRegistros, String campos) throws Exception {
		campos = campos + ",nrVersao";
		return super.listarBaseadoNoExemplo(exemplo, exemplo2, primeiroRegistro, quantidadeDeRegistros, campos);

	}

	public void salvarTabular(Button button) throws Exception {
		Listitem itemSelecionado = getItemSelecionado(button);
		setObjetoAtual(itemSelecionado.getValue());
		try {
			super.salvar();
			pesquisar();
		} catch (Exception e) {
			if (e.getCause() instanceof ViolacaoDeRegraEx) {
				getMessagesHelper().emiteMensagemAtencao(e.getMessage());
			} else if (e.getCause() instanceof WrongValueException) {
				throw e;
			} else if (e.getCause() instanceof InvocationTargetException) {
				getMessagesHelper().emiteMensagemErro(e.getMessage());
			}
		}

	}

	private Listitem getItemSelecionado(Button button) {
		Component itemSelecionado = button.getParent();
		while (itemSelecionado != null && !(itemSelecionado instanceof Listitem)) {
			itemSelecionado = itemSelecionado.getParent();
		}
		return (Listitem) itemSelecionado;
	}

	public void excluirTabular(Button button) {
		Listitem itemSelecionado = getItemSelecionado(button);
		itemSelecionado.setSelected(true);
		try {
			Listbox listbox = (Listbox) getWindowAtual().getFellow("listaSelecao");
			ProBaseVO objeto = (ProBaseVO) listbox.getSelectedItem().getValue();
			repositorio().excluir(objeto);
			getListaDeObjetos().remove(objeto);
			pesquisar();
		} catch (Exception e) {
			log.error("Erro ao excluir ", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void atualizaObjetoAtual(Object objetoAtual) throws Exception {
		this.objetoAtual = (ENTITY) objetoAtual;
		getBinder().loadAll();
	}

	public void aplicarSegurancaNoBotaoTabular(Event e) {
		getHelperView().aplicarSegurancaNoBotaoTabular(this, (ButtonBox) e.getTarget());
	}

	public void bloquearDesbloquearRegistro() throws Exception {
		if (!StatusDoRegistro.ATIVO.equals(((ProVO) getObjetoAtual()).getStatusDoRegistro())) {
			if (getMessagesHelper().desejaRealmente("Deseja desbloquear o registro selecionado?")) {
				((ProVO) getObjetoAtual()).setStatusDoRegistro(StatusDoRegistro.ATIVO);
				salvar();
			}
		} else {
			if (getMessagesHelper().desejaRealmente("Deseja bloquear o registro selecionado?")) {
				((ProVO) getObjetoAtual()).setStatusDoRegistro(StatusDoRegistro.BLOQUEADO);
				salvar();
			}
		}
		pesquisar();
	}

}
