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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.OneToMany;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zkplus.databind.BindingListModelSet;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listfoot;
import org.zkoss.zul.Listfooter;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.interfaces.IUnidade;
import br.gov.prodemge.ssc.interfaces.IUsuario;
import br.gov.prodemge.ssc.interfaces.base.IUnidadeBase;
import br.gov.prodemge.ssc.interfaces.base.IUsuarioBase;
import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.IProFacade;
import br.gov.prodigio.comuns.anotacoes.ExclusaoLogica;
import br.gov.prodigio.comuns.anotacoes.MetadadosVisao;
import br.gov.prodigio.comuns.excecoes.ViolacaoDeRegraEx;
import br.gov.prodigio.comuns.utils.Context;
import br.gov.prodigio.comuns.utils.Reflexao;
import br.gov.prodigio.comuns.validacoes.Validacao;
import br.gov.prodigio.controle.componente.AppListRender;
import br.gov.prodigio.controle.componente.Bandboxbind;
import br.gov.prodigio.controle.componente.ButtonRelatorioPDFBind;
import br.gov.prodigio.controle.componente.ButtonUploadBind;
import br.gov.prodigio.controle.componente.CnpjboxBind;
import br.gov.prodigio.controle.componente.CpfboxBind;
import br.gov.prodigio.controle.componente.DetBox;
import br.gov.prodigio.controle.componente.FieldValidator;
import br.gov.prodigio.controle.componente.ITabelaBuilder;
import br.gov.prodigio.controle.componente.ImageUpload;
import br.gov.prodigio.controle.componente.InternalWindow;
import br.gov.prodigio.controle.componente.Listboxdual;
import br.gov.prodigio.controle.componente.Listfootersum;
import br.gov.prodigio.controle.componente.ListheaderSort;
import br.gov.prodigio.controle.componente.ProAnnotateDataBinder;
import br.gov.prodigio.controle.componente.ProAnnotateDataBinderHelper;
import br.gov.prodigio.controle.componente.SelecaoBox;
import br.gov.prodigio.controle.componente.Sylistbox;
import br.gov.prodigio.controle.componente.TabelaExcelBuilder;
import br.gov.prodigio.controle.componente.TabelaHtmlBuilder;
import br.gov.prodigio.entidades.ArquivoVO;
import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.entidades.ProVO;
import br.gov.prodigio.entidades.ProVO.SITUACAO_DO_REGISTRO;
import br.gov.prodigio.entidades.RecordDataSet;
import br.gov.prodigio.entidades.StatusDoRegistro;
import br.gov.prodigio.entidades.validacao.Regra;
import br.gov.prodigio.visao.helper.ComparadorGenerico;
import br.gov.prodigio.visao.helper.ProHelperView;
import br.gov.prodigio.visao.helper.ProMessageHelper;

@MetadadosVisao(camposParaLista = "id")
@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
public class ProCtr<ENTITY extends ProBaseVO> extends GenericForwardComposer {

	public final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final long serialVersionUID = 7155635980407428118L;

	private InputElement inputComErro = null;

	Set<Listboxdual> dualboxRegister = new TreeSet<Listboxdual>();

	ProAnnotateDataBinder binder;
	ProMessageHelper messagesHelper;
	IUsuarioBase usuarioVO = null;
	IUnidadeBase unidadeVO = null;
	Set<ENTITY> listaDeObjetos = new TreeSet<ENTITY>();
	protected ENTITY objetoAtual;
	Window tela;
	ENTITY objetoAtualArg = null;
	ENTITY objetoAtualArg2 = null;

	protected IProFacade fachadaDeNegocio;

	private final List<IUnidade> listaUnidade = new ArrayList<IUnidade>();
	protected String sortDir;
	protected String campoDeOrdenacao;

	public ProCtr() {
		messagesHelper = ProMessageHelper.getInstance(this);
		try {
			objetoAtual = instanciaNovoObjeto();
		} catch (Exception e) {
			objetoAtual = (ENTITY) new ProBaseVO();
		}

	}

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		configuraUnidadeLogada(comp);

	}

	@Override
	public void doAfterCompose(Component tela) throws Exception {
		super.doAfterCompose(tela);
		configurafachadaDeNegocio(tela);

		// TODO (glauco.cardoso) fazer de uma forma melhor
		// listaDeObjetos.add(instanciaNovoObjeto());// Apenas para montar uma linha na tela de selação
		if (getObjetoAtual() == null) {
			this.objetoAtual = instanciaNovoObjeto();
		}
		setTela((Window) tela);
		configurarVariavelDeControle(tela);
		configuraListaDeUnidades(tela);
		configuraUsuarioLogado(tela);

		if (getObjetoAtualArg() == null) {// Pode ter sido iniciarlizado no metodo inicializaObjetoArg. Hoje é chamado na criação de Bandboxbind
			ENTITY pArg = newObject();
			setObjetoAtualArg(pArg);
		}
		if (getObjetoAtualArg2() == null) {
			ENTITY pArg2 = newObject();
			setObjetoAtualArg2(pArg2);
		}
		configuraValoresPadroesParaObjetoArg(getObjetoAtualArg(), getObjetoAtualArg2());
		configuraValoresPadroesParaObjeto();
		configuraTelaDeEntrada(tela);
		realizarBind(tela);
	}

	protected void configuraListaDeUnidades(Component comp) {
		// UnidadeAdministrativaVO unidade = new UnidadeAdministrativaVO();
		// Set unidades = null;
		// try {
		// final Object attribute =
		// comp.getAttribute("listaDeUnidadeAdministrativaVO",
		// Component.SESSION_SCOPE);
		// if (attribute == null) {
		// unidades = getFachadaDeNegocio().listarBaseadoNoExemplo(unidade,
		// unidade, 0, 100, "id", "descricao");
		// comp.setAttribute("listaDeUnidadeAdministrativaVO", unidades,
		// Component.SESSION_SCOPE);
		// } else {
		// unidades = (Set) attribute;
		// }
		// } catch (Exception e) {
		// log.error("Erro ao configurar lista de unidades ", e);
		// }
		// listaUnidade.addAll(unidades);
	}

	protected void configuraTelaDeEntrada(Component comp) throws Exception {
		getHelperView().configuraTelaDeEntrada(comp);
		configuraOrdenacaoDeLista();
	}

	protected void configurafachadaDeNegocio(Component comp) {
		fachadaDeNegocio = (IProFacade) comp.getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO, Component.APPLICATION_SCOPE);
	}

	protected void configuraUnidadeLogada(Component comp) {
		unidadeVO = (IUnidadeBase) comp.getAttribute(Constantes.UNIDADE_AUTENTICADA, Component.SESSION_SCOPE);
		if (unidadeVO == null) {
			Executions.sendRedirect("/");
		}
	}

	protected void configuraUsuarioLogado(Component comp) {
		usuarioVO = (IUsuarioBase) comp.getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);
	}

	@Override
	public final ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		ComponentInfo com = super.doBeforeCompose(page, parent, compInfo);
		if (!page.getDesktop().getFirstPage().getRequestPath().equals("/index.zul")) {
			// throw new RuntimeException();
		}
		return com;
	}

	private void configuraOrdenacaoDeLista() {

	}

	protected void configurarVariavelDeControle(Component comp) throws Exception {
		comp.setAttribute("classecontrole", this, false);
	}

	protected void realizarBind(Component comp) {
		inicializaBinder(comp);
		try {
			getBinder().loadAll();
		} catch (ComponentNotFoundException e) {
			log.error("Erro ao realizar bind ", e);
		} catch (Exception e) {
			log.error("Erro ao realizar bind ", e);
		}
	}

	public void inicializaBinder(Component comp) {
		if (binder == null) {
			createBinder(comp);
		}
	}

	private synchronized void createBinder(Component comp) {
		binder = new ProAnnotateDataBinder(comp);
	}

	public void pesquisar() {
		try {
			Paging paginal = (Paging) (getWindowAtual().getFellow("paginador"));
			paginal.setActivePage(0);
			Intbox numeroDeRegistro = (Intbox) getWindowAtual().getFellow("numeroDeRegistros");
			Listbox listbox = (Listbox) getWindowAtual().getFellow("listaSelecao");
			Integer value = numeroDeRegistro.getValue();
			if (value != null) {
				listbox.getPaginal().setPageSize(value);
				paginal.setPageSize(value);
			} else {
				numeroDeRegistro.setValue(paginal.getPageSize());
			}
			int pageSize = paginal.getPageSize();
			pesquisar(0, pageSize);
			Clients.clearBusy();
		} catch (Exception e) {
			log.error("Erro ao pesquisar", e);
			getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
	}

	public void pesquisar(int activePage, int pageSize) {
		RecordDataSet<ProBaseVO> setAux = null;
		ENTITY pArg = getObjetoAtualArg();
		ENTITY pArg2 = getObjetoAtualArg2();
		if (pArg == null) {
			pArg = newObject();
		}
		try {
			antesPesquisar(pArg);
			antesPesquisar(pArg, pArg2);
			Listbox listbox = (Listbox) getWindowAtual().getFellow("listaSelecao");
			Paging paginal = (Paging) getWindowAtual().getFellow("paginador");
			setAux = (RecordDataSet<ProBaseVO>) recuperaLista(pArg, pArg2, activePage * pageSize, pageSize);
			setListaDeObjetos(setAux);
			BindingListModelSet listSelBind = new BindingListModelSet(getListaDeObjetos(), true);
			listSelBind.setMultiple(listbox.isMultiple());
			listbox.setModel(listSelBind);
			int total = setAux.getTotal().intValue();
			paginal.setTotalSize(total);
			aposPesquisar(setAux);
			if (total <= pageSize) {
				getWindowAtual().getFellow("divRegistrosPorPagina").setVisible(false);
			} else {
				getWindowAtual().getFellow("divRegistrosPorPagina").setVisible(true);
			}
			if (total == 0) {
				final String mensagemRetornoPesquisa = getMensagemRetornoPesquisa();
				if (mensagemRetornoPesquisa != null) {
					Component component = getTela().getFellowIfAny(br.gov.prodigio.comuns.constantes.Constantes.BOTOES.DIV_BOTOES);
					if (component == null)
						component = getTela();
					getMessagesHelper().emiteMensagemSucesso("Nenhum registro encontrado!", component);
				}
			}
		} catch (Exception e) {
			log.error("Erro ao pesquisar", e);
			getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
	}

	protected String getMensagemRetornoPesquisa() {
		return "Nenhum registro encontrado";
	}

	public void aposPesquisar(Set<? extends ProBaseVO> setAux) throws Exception {
	}

	public void antesPesquisar(ProBaseVO pArg) throws Exception {
	}

	public void antesPesquisar(ProBaseVO pArg, ProBaseVO pArg2) throws Exception {

	}

	public Set<ProBaseVO> recuperaLista(ProBaseVO exemplo, ProBaseVO exemplo2, int primeiroRegistro, int quantidadeDeRegistros) throws Exception {
		Set<ProBaseVO> setAux = new HashSet<ProBaseVO>();
		String campos = montaCamposParaProjecao();
		setAux = listarBaseadoNoExemplo(exemplo, exemplo2, primeiroRegistro, quantidadeDeRegistros, campos);
		return setAux;
	}

	protected String montaCamposParaProjecao() throws Exception {
		Listhead listhead = (Listhead) getWindowAtual().getFellow("cabecalho");
		List filhos = listhead.getChildren();
		String campos = "";
		String orderBy = "";
		for (Object object : filhos) {
			if (object instanceof Listheader) {
				Listheader listheader = (Listheader) object;
				Object value = listheader.getValue();
				if (value != null) {
					if (!listheader.getSortDirection().equals("natural")) {
						if (listheader.getSortDirection().equals("ascending")) {
							orderBy = orderBy + "," + value + " asc";
						} else if (listheader.getSortDirection().equals("descending")) {
							orderBy = orderBy + "," + value + " desc";
						}
						continue;
					}
					campos = campos + "," + value;
				}
			}
		}
		String camposParaProjecaoEOrdenacao = orderBy + campos;
		if (camposParaProjecaoEOrdenacao.length() > 1) {
			return camposParaProjecaoEOrdenacao.substring(1);
		}
		return camposParaProjecaoEOrdenacao;
	}

	protected Set<ProBaseVO> listarBaseadoNoExemplo(ProBaseVO exemplo, ProBaseVO exemplo2, int primeiroRegistro, int quantidadeDeRegistros, String campos) throws Exception {
		Set<ProBaseVO> setAux;
		setAux = repositorio().listarBaseadoNoExemplo(exemplo, exemplo2, primeiroRegistro, quantidadeDeRegistros, campos.split(","));
		return setAux;
	}

	public Set<? extends Object> getListaDeObjetos() {
		return listaDeObjetos;
	}

	public void setListaDeObjetos(Set lista) {
		this.listaDeObjetos = lista;
	}

	public ENTITY getObjetoAtual() {
		return objetoAtual;
	}

	public void setObjetoAtual(Object objetoAtual) {
		try {
			if (objetoAtual != null) {
				atualizaObjetoAtual(objetoAtual);
			}
		} catch (Exception e) {
			log.error("Erro ao atribuir o objeto atual", e);
			getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
	}

	protected void atualizaObjetoAtual(Object objetoAtual) throws Exception {
		this.objetoAtual = (ENTITY) repositorio().recuperaObjeto((ProBaseVO) objetoAtual);
		ajustarLayoutParaManutencao();
		if (objetoAtual != null) {
			getHelperView().atualizaImagensDoVoParaTela(this.objetoAtual, getWindowAtual());
		}
		antesAtualizaObjetoAtual(this.objetoAtual);
		getBinder().loadComponent(getWindowAtual().getFellow("manutencao"));
		/*
		 * Tem que ter essa implementação para formar o aninhamento de bandbox
		 */
	}

	protected void antesAtualizaObjetoAtual(Object objetoAtual) {
	}

	protected void validarAoSalvar() {
		Component tela = getTela().getFellow("manutencao");
		validaComponente(tela);
	}

	private void validaComponente(Component component) {
		validaComponenteInput(component);

		List<Component> children = component.getChildren();
		for (Component each : children) {
			validaComponente(each);
		}
	}

	private void validaComponenteInput(Component component) {

		// Validação apenas para os componentes customizados da arquitetura de base
		if (component instanceof FieldValidator && component instanceof InputElement) {
			// habilita o componente para uma nova validação
			((InputElement) component).clearErrorMessage(true);
			// if (!((InputElement) component).isValid()) {
			// Força a exibição da mensagem de erro
			((InputElement) component).getText();
			// }
		}
	}

	public void salvar() {
		try {
			configuraAuditoria(objetoAtual);
			Context.setAttribute("usuarioLogado", getUsuarioVO());
			Context.setAttribute("evento", "salvar");
			transfereDadosDaTelaParaObjeto();
			Clients.showBusy("Processando ...");
			antesSalvar(objetoAtual);
			if (fachadaDeNegocio == null)
				throw new Exception("Não foi possível recuperar a interface de negócio.");
			Long id = (Long) fachadaDeNegocio.gravar(objetoAtual);
			if (id == null) {
				throw new Exception("Não foi possível recuperar objeto após gravação.");
			}

			objetoAtual.setId(id);
			recuperaObjetoAposSalvar();
			aposSalvar(objetoAtual);
			Clients.clearBusy();
			getHelperView().atualizaLookUp(objetoAtual, getWindowAtual());
			getBinder().loadAll();
			getHelperView().atualizaImagensDoVoParaTela(objetoAtual, getWindowAtual());
			ajustarLayoutParaManutencao();
			Context.setAttribute("evento", null);
			Component component = getTela().getFellowIfAny(br.gov.prodigio.comuns.constantes.Constantes.BOTOES.DIV_BOTOES);
			if (component == null)
				component = getTela();
			getMessagesHelper().emiteMensagemSucesso("Registro gravado com sucesso", component);
		} catch (WrongValueException e) {
			log.warn("Erro de validacão ao salvar", e.getMessage());
			aposErroEmGravacao(objetoAtual);
			((InputElement) e.getComponent()).setFocus(true);
			Clients.wrongValue(e.getComponent(), e.getMessage());
		} catch (ViolacaoDeRegraEx e) {
			log.warn("Violação de regra ao salvar");
			aposErroEmGravacao(objetoAtual);
			getMessagesHelper().emiteMensagemAtencao(e);
		} catch (ComponentNotFoundException e) {
			log.error("Erro ao salvar", e);
			aposErroEmGravacao(objetoAtual);
			getMessagesHelper().emiteMensagemErro("Ao tentar copiar valores da tela para a entidade, o campo de nome" + recuperaNomeDoCampoNaoEncontrado(e) + " não foi encontrado na tela");
		} catch (InvocationTargetException e) {
			log.error("Erro ao salvar", e);
			aposErroEmGravacao(objetoAtual);
			getMessagesHelper().emiteMensagemErro(e.getTargetException().getMessage());
		} catch (Exception e) {
			aposErroEmGravacao(objetoAtual);
			if (e.getCause() != null && e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
				Set<ConstraintViolation<?>> cons = constraintViolationException.getConstraintViolations();
				String erro = "";
				for (ConstraintViolation<?> constraintViolation : cons) {
					erro = erro + constraintViolation.getMessage();
					log.warn("Erro de validação de campos :", constraintViolation.getMessage());
				}
				getMessagesHelper().emiteMensagemErro(erro);
			} else if (e.getCause() != null && e.getCause() instanceof ViolacaoDeRegraEx) {
				log.error("Erro ao salvar", e);
				getMessagesHelper().emiteMensagemAtencao(((ViolacaoDeRegraEx) e.getCause()).getMessage());
			} else if ((e.getCause() != null && e.getCause() instanceof Exception && e.getCause().getCause() != null && e.getCause().getCause() instanceof ViolacaoDeRegraEx)) {
				log.error("Erro ao salvar", e);
				getMessagesHelper().emiteMensagemAtencao(((ViolacaoDeRegraEx) e.getCause().getCause()).getMessage());
			} else {
				log.error("Erro ao salvar", e);
				getMessagesHelper().emiteMensagemErro(e.getMessage());
			}
		}
	}

	public static void iniciarProcessoAssinaturaDigital() {
		String mutex = "";
		Clients.evalJavaScript("zAu.send(new zk.Event(zk.Widget.$('$assinaturaDigital'), 'onClick', {toServer:true}));");
		Clients.evalJavaScript("abrirPopupProSigner()");
		try {
			Executions.wait(mutex);
		} catch (Exception e) {

		}
	}

	protected void aposSalvar(ProBaseVO objetoAtual) {
	}

	protected void antesSalvar(ProBaseVO objetoAtual) {
	}

	protected void antesConcluir(ProBaseVO objetoAtual) {
	}

	protected void aposErroEmGravacao(Object objetoAtual) {

	}

	protected void transfereDadosDaTelaParaObjeto() {
		getBinder().saveAll();
	}

	public void editar() {
		try {
			if (objetoAtual instanceof ProVO) {
				Context.setAttribute("usuarioLogado", getUsuarioVO());
				/*
				 * Este atributo permanecerá durante todo o ciclo de vida da thread
				 */
				((ProVO) objetoAtual).setCdLoginMovimentacao(getUsuarioVO().getLogin());
			}
			fachadaDeNegocio.editar(objetoAtual);
			if (objetoAtual.getId() != null) {
				objetoAtual = fachadaDeNegocio.recuperaObjeto(objetoAtual);
			}
			if (objetoAtual == null) {
				throw new Exception("Não foi possível recuperar objeto após gravação.");
			}
			getHelperView().atualizaLookUp(objetoAtual, getWindowAtual());
			getBinder().loadAll();
			getHelperView().atualizaImagensDoVoParaTela(objetoAtual, getWindowAtual());
			ajustarLayoutParaManutencao();
			Component component = getTela().getFellowIfAny(br.gov.prodigio.comuns.constantes.Constantes.BOTOES.DIV_BOTOES);
			if (component == null)
				component = getTela();
			getMessagesHelper().emiteMensagemSucesso("Registro retornado para a edição!", component);
		} catch (ViolacaoDeRegraEx e) {
			log.error("Erro ao editar", e);
			getMessagesHelper().emiteMensagemAtencao(e.getMessage());
		} catch (WrongValueException e) {
			log.error("Erro ao editar", e);
			getMessagesHelper().emiteMensagemErro(e);
		} catch (ComponentNotFoundException e) {
			log.error("Erro ao editar", e);
			getMessagesHelper().emiteMensagemErro("Ao tentar copiar valores da tela para a entidade, o campo de nome" + recuperaNomeDoCampoNaoEncontrado(e) + " não foi encontrado na tela");
		} catch (InvocationTargetException e) {
			log.error("Erro ao editar", e);
			getMessagesHelper().emiteMensagemErro(e.getTargetException().getMessage());
		} catch (Exception e) {
			log.error("Erro ao editar", e);
			getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
	}

	protected String recuperaNomeDoCampoNaoEncontrado(ComponentNotFoundException e) {
		return e.getMessage().substring(e.getMessage().indexOf(":"), e.getMessage().length());
	}

	protected ENTITY instanciaNovoObjeto() {

		ENTITY newObject = newObject();
		if (newObject instanceof ProVO) {
			((ProVO) newObject).setDsSituacao(SITUACAO_DO_REGISTRO.EM_EDICAO);
			((ProVO) newObject).setStatusDoRegistro(StatusDoRegistro.INATIVO);
		}
		if (newObject instanceof ProBaseVO) {
			newObject.setIpMovimentacao(Executions.getCurrent().getRemoteAddr());
			newObject.setTpOperacao("A");
			newObject.setTsMovimentacao(new Date());
		}

		return newObject;

	}

	/**
	 * @return
	 */
	protected ENTITY newObject() {
		try {
			Class[] classes = Reflexao.getGenericTypes(this.getClass());
			if (classes.length == 0) {
				classes = Reflexao.getGenericTypes2(this.getClass());
			}
			log.debug("Instanciando entidade {} para classe de controle {} ", classes[0], this.getClass());
			return (ENTITY) classes[0].newInstance();
		} catch (Exception e) {
			log.error("Erro ao instanciar um novo objeto", e);
			return null;
		}

	}

	public ProBaseVO instanciaNovoObjeto(Class c) {

		try {
			log.debug("Instanciando entidade {} ", c);
			ProBaseVO vo = (ProBaseVO) c.newInstance();
			return vo;
		} catch (Exception e) {
			log.error("Erro ao instanciar um novo objeto", e);
			return null;
		}

	}

	public ENTITY getObjetoAtualArg() {
		return objetoAtualArg;
	}

	public void setObjetoAtualArg(ENTITY objetoAtualArg) {
		this.objetoAtualArg = objetoAtualArg;
	}

	public ENTITY getObjetoAtualArg2() {
		return objetoAtualArg2;
	}

	public void setObjetoAtualArg2(ENTITY objetoAtualArg) {
		this.objetoAtualArg2 = objetoAtualArg;
	}

	public void novo() {
		objetoAtual = instanciaNovoObjeto();
		if (objetoAtual instanceof ProVO) {
			((ProVO) objetoAtual).setDsSituacao(SITUACAO_DO_REGISTRO.EM_EDICAO);
			((ProVO) objetoAtual).setStatusDoRegistro(StatusDoRegistro.INATIVO);
		}
		if (objetoAtual instanceof ProBaseVO) {

			objetoAtual.setIpMovimentacao(Executions.getCurrent().getRemoteAddr());
			objetoAtual.setTpOperacao("A");
			objetoAtual.setTsMovimentacao(new Date());

		}

		configuraValoresPadroesParaObjeto();
		if (objetoAtual.getId() != null) {
			setObjetoAtual(objetoAtual);
		}
		try {
			ajustarLayoutParaManutencao();
			getHelperView().atualizaImagensDoVoParaTela(objetoAtual, getWindowAtual());
		} catch (Exception e) {
			e.printStackTrace();
			getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
		getBinder().loadAll();
		Set dualList = getDualboxRegister();
		for (Object object : dualList) {
			try {
				inicializaDualListBoxOrigem((Listboxdual) object);
			} catch (IllegalAccessException e) {
				log.error("Erro ao criar um novo objeto", e);
			} catch (InstantiationException e) {
				log.error("Erro ao criar um novo objeto", e);
			} catch (NoSuchMethodException e) {
				log.error("Erro ao criar um novo objeto", e);
			} catch (InvocationTargetException e) {
				log.error("Erro ao criar um novo objeto", e);
			}
		}
	}

	public void clonar() {
		ProBaseVO objetoAtualAux = getObjetoAtual();
		objetoAtual = instanciaNovoObjeto();
		if (objetoAtualAux instanceof ProVO) {
			((ProVO) objetoAtualAux).setTsMovimentacao(null);
			((ProVO) objetoAtualAux).setId(null);
		}
		try {
			Reflexao.transfereDadosEntreObjetos(objetoAtualAux, objetoAtual);
		} catch (Exception e1) {
			getMessagesHelper().emiteMensagemErro("Erro no evento clonar( " + e1.getMessage() + " )");
			objetoAtual = instanciaNovoObjeto();
			return;
		}
		if (objetoAtual instanceof ProVO) {
			((ProVO) objetoAtual).setDsSituacao(SITUACAO_DO_REGISTRO.EM_EDICAO);
			((ProVO) objetoAtual).setImagem(null);
		}
		configuraValoresPadroesParaObjeto();
		if (objetoAtual.getId() != null) {
			setObjetoAtual(objetoAtual);
		}
		try {
			ajustarLayoutParaManutencao();
			getHelperView().atualizaImagensDoVoParaTela(objetoAtual, getWindowAtual());
		} catch (Exception e) {
			getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
		getBinder().loadAll();
		Component component = getTela().getFellowIfAny(br.gov.prodigio.comuns.constantes.Constantes.BOTOES.DIV_BOTOES);
		if (component == null)
			component = getTela();
		getMessagesHelper().emiteMensagemSucesso("Registro clonado com sucesso", component);
	}

	protected void configuraValoresPadroesParaObjeto() {
	}

	protected void configuraValoresPadroesParaObjetoArg(ENTITY pArg, ENTITY pArg2) {
	}

	public void anexar(String idCampoImagem) {
		Object media = abrePopUpSelecaoDeAquivo();
		if (media instanceof org.zkoss.image.Image) {
			try {
				getHelperView().atualizaImagemNaTela(media, idCampoImagem, getWindowAtual());
				Object objetoAtual = getObjetoAtual();
				if (objetoAtual == null) {
					objetoAtual = instanciaNovoObjeto();
				}
				Method atributoQueSeraAtualizado = Reflexao.recuperaMetodoGetDoObjeto(idCampoImagem, objetoAtual);
				Object argumento = null;
				argumento = getHelperView().recuperaValorTela(atributoQueSeraAtualizado, objetoAtual, getWindowAtual());
				getHelperView().atualizarCampoDoObjeto(atributoQueSeraAtualizado, objetoAtual, getWindowAtual(), argumento);
			} catch (Exception e) {
				getMessagesHelper().emiteMensagemErro(e.getMessage());
			}
		} else {
			if (null != media) {
				Messagebox.show("Imagem inválida: " + media, "Error", Messagebox.OK, Messagebox.ERROR);
			} else {
				Messagebox.show("Nenhum arquivo escolhido!", "Error", Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	public Object abrePopUpSelecaoDeAquivo() {
		Object media = null;
		media = Fileupload.get();
		return media;
	}

	public void antesUploadDeArquivo(ButtonUploadBind buttonUploadBind) {

	}

	public void aposUpLoadDeArquivo(byte[] arquivo, Media media) {

	}

	public void aposUpLoadDeArquivo(byte[] arquivo, Media media, Object object) {

	}

	private String getIdWindowAtual() {
		if (objetoAtual == null) {
			objetoAtual = instanciaNovoObjeto();
		}
		String idWindow = objetoAtual.getClass().getSimpleName().toLowerCase().substring(0, objetoAtual.getClass().getSimpleName().toLowerCase().length() - 2) + "window";
		return idWindow;
	}

	private String getIdWindowAtual(Class classe) {
		String idWindow = classe.getSimpleName().toLowerCase().substring(0, classe.getSimpleName().toLowerCase().length() - 2) + "window";
		return idWindow;
	}

	public void abrir() {
		objetoAtual = instanciaNovoObjeto();
		try {
			getHelperView().ajustaLayoutParaSelecao(getWindowAtual());
			pesquisar();
		} catch (Exception e) {
			getMessagesHelper().emiteMensagemErro(e.getMessage());
			log.error("Erro ao abrir tela", e);
		}
	}

	protected Window getWindowAtual() {
		Window tela = getTela();
		if (tela != null) {
			return tela;
		} else {
			String idWindow = getIdWindowAtual();
			tela = (Window) page.getFellowIfAny(idWindow);
			if (tela == null) {
				Class cl = getObjetoAtual().getClass().getSuperclass();
				idWindow = getIdWindowAtual(cl);
				tela = (Window) page.getFellowIfAny(idWindow);
				while (tela == null && !cl.getClass().equals(Object.class)) {
					cl = cl.getClass().getSuperclass();
					if (cl.getClass().equals(ProBaseVO.class)) {
						tela = null;
						break;
					}
					idWindow = getIdWindowAtual(cl);
					tela = (Window) page.getFellowIfAny(idWindow);
				}
			}
		}
		return tela;
	}

	protected void setWindowAtual() throws Exception {

	}

	public void excluir() {
		if ((podeExcluir() && getMessagesHelper().desejaRealmente(montaMensagemDesejaExclusao()))) {
			try {
				if (objetoAtual instanceof ProVO) {
					Context.setAttribute("usuarioLogado", getUsuarioVO());
					/*
					 * Este atributo permanecerá durante todo o ciclo de vida da thread
					 */
					((ProVO) objetoAtual).setCdLoginMovimentacao(getUsuarioVO().getLogin());
				}
				Context.setAttribute("evento", "excluir");
				excluirPadrao();
				Component component = getTela().getFellowIfAny(br.gov.prodigio.comuns.constantes.Constantes.BOTOES.DIV_BOTOES);
				if (component == null)
					component = getTela();
				getMessagesHelper().emiteMensagemSucesso("Registro excluído com sucesso!", component);
			} catch (Exception e) {
				log.error("Erro ao excluir entidade", e);
				String message = e.getMessage();
				if (message.contains("ORA-02292")) {
					getMessagesHelper().emiteMensagemErro("Não é possível excluir pois existe(m) outro(s) registro(s) vinculado(s) a essa entidade.");
				} else {
					getMessagesHelper().emiteMensagemErro(message);
				}
			}
		}
	}

	protected String montaMensagemExclusaoComSucesso() {
		return "Registro excluído com sucesso!";
	}

	protected String montaMensagemDesejaExclusao() {
		return "Deseja realmente excluir?";
	}

	protected boolean podeExcluir() {
		if (objetoAtual.getId() == null) {
			alert("O registro ainda não foi gravado.");
			return false;
		}
		return true;
	}

	public void excluirPadrao() throws Exception {
		repositorio().excluir(objetoAtual);
		listaDeObjetos.remove(objetoAtual);
		novo();
	}

	public void inicializaObjetoArg(String propriedade) {
		if (this.objetoAtualArg == null) {
			this.objetoAtualArg = newObject();
		}
		Object auxArg = this;
		if (propriedade.contains(".")) {
			inicializaObjetosAgregados(propriedade, auxArg);
		}
	}

	private void inicializaObjetosAgregados(String propriedade, Object auxArg) {
		String[] camposDeObjetos;
		try {
			Object argumento = null;
			Object aux2 = null;
			int indice = 0;
			camposDeObjetos = propriedade.split("\\.");
			while (indice < camposDeObjetos.length) {
				String nomeAtributo = camposDeObjetos[indice];
				Method atributoQueSeraAtualizado = Reflexao.recuperaMetodoGetDoObjeto(nomeAtributo, auxArg);
				argumento = Reflexao.recuperaValorDeMetodoGetNotNull(atributoQueSeraAtualizado, auxArg);
				indice++;
				if (indice == camposDeObjetos.length || (!(ProBaseVO.class.isAssignableFrom(atributoQueSeraAtualizado.getReturnType()))) && (!(Set.class.isAssignableFrom(atributoQueSeraAtualizado.getReturnType())))) {
					Reflexao.atualizarCampoDoObjeto(atributoQueSeraAtualizado, auxArg, argumento);
					break;
				}
				aux2 = Reflexao.recuperaValorDeMetodoGetNotNull(atributoQueSeraAtualizado, auxArg);
				Reflexao.atualizarCampoDoObjeto(atributoQueSeraAtualizado, auxArg, aux2);
				auxArg = aux2;
			}
		} catch (Exception e) {
			log.error("Erro ao inicializar objeto", e);
		}
	}

	public void atualizaObjetoArg(Object campoArgDaTela) {
		String nomeCampo = ((HtmlBasedComponent) campoArgDaTela).getId().substring(0, ((HtmlBasedComponent) campoArgDaTela).getId().length() - 3);
		ENTITY objArg = getObjetoAtualArg();
		if (objArg == null) {
			objArg = instanciaNovoObjeto();
		}
		String[] camposDeObjetos = null;
		ENTITY auxArg = getObjetoAtualArg();
		if (nomeCampo.indexOf("$") != -1) {
			try {
				Object argumento = null;
				ENTITY aux2 = null;
				if (campoArgDaTela instanceof Combobox) {
					if (((Combobox) campoArgDaTela).getSelectedItem() == null) {
						argumento = null;
					}
				} else {
					argumento = getHelperView().recuperaValorDaTela(nomeCampo + "Arg", getWindowAtual());
				}
				int indice = 0;
				camposDeObjetos = nomeCampo.split("\\$");
				while (indice < camposDeObjetos.length) {
					Method atributoQueSeraAtualizado = Reflexao.recuperaMetodoGetDoObjeto(camposDeObjetos[indice], auxArg);
					indice++;
					if (indice == camposDeObjetos.length) {
						Reflexao.atualizarCampoDoObjeto(atributoQueSeraAtualizado, auxArg, argumento);
						break;
					}
					aux2 = (ENTITY) Reflexao.recuperaValorDeMetodoGetNotNull(atributoQueSeraAtualizado, auxArg);
					Reflexao.atualizarCampoDoObjeto(atributoQueSeraAtualizado, auxArg, aux2);
					auxArg = aux2;
				}
			} catch (Exception e) {
				log.error("Erro ao atualizar objeto", e);
			}
		} else {
			Method atributoQueSeraAtualizado = null;
			;
			try {
				atributoQueSeraAtualizado = Reflexao.recuperaMetodoGetDoObjeto(nomeCampo, getObjetoAtualArg());
				if (atributoQueSeraAtualizado == null) {
					getMessagesHelper().emiteMensagemErro("O argumento " + nomeCampo + " não existe no Objeto " + getObjetoAtualArg().getClass().getCanonicalName());
				} else {
					Object argumento = null;
					try {
						if (campoArgDaTela instanceof Combobox) {
							if (((Combobox) campoArgDaTela).getSelectedItem() == null) {
								argumento = null;
							} else {
								argumento = getHelperView().recuperaValorDaTela(nomeCampo + "Arg", getWindowAtual());
							}
						} else {
							argumento = getHelperView().recuperaValorDaTela(nomeCampo + "Arg", getWindowAtual());
						}
						if (argumento instanceof ProVO) {// Para pesquisa basta o id.
							argumento = argumento.getClass().newInstance();
							Long id = ((ProVO) argumento).getId();
							((ProVO) argumento).setDsSituacao(null);
							((ProVO) argumento).setId(id);
						}

						getHelperView().atualizarCampoDoObjeto(atributoQueSeraAtualizado, objetoAtualArg, getWindowAtual(), argumento);
					} catch (Exception e) {
						getMessagesHelper().emiteMensagemErro(e.getMessage());
					}
				}
			} catch (Exception e1) {
				log.error("Erro ao atualizar objeto", e1);
			}
		}
	}

	public void novoDetalhe(DetBox detBox) {
		String[] nodos = detBox.getNomeDoObjeto().split("\\.");
		String nomeDoAtributoDetalhe = nodos[nodos.length - 1];
		if (nomeDoAtributoDetalhe.endsWith("Det")) {
			nomeDoAtributoDetalhe = nomeDoAtributoDetalhe.substring(0, nomeDoAtributoDetalhe.length() - 3);
		}
		Object objetoPai = detBox.getObjectPai();
		Method m = null;
		Collection listDetAtual = null;
		try {
			m = Reflexao.recuperaMetodoGetDoObjeto(nomeDoAtributoDetalhe, objetoPai);
			listDetAtual = (Collection) getHelperView().recuperaValorDoCampoDoObjetoAtual(m, objetoPai, getWindowAtual());

			if (listDetAtual != null && !listDetAtual.isEmpty()) {
				if (listDetAtual instanceof Set) {

					listDetAtual = new LinkedHashSet(listDetAtual);

				} else if (listDetAtual instanceof List) {

					listDetAtual = new LinkedList(listDetAtual);
				}
			}

		} catch (Exception e1) {
			log.error("Erro ao criar um novo detalhe", e1);
		}

		try {
			String nomeDoMetodoQueRepresenta_O_Detalhe = "get" + nomeDoAtributoDetalhe.substring(0, 1).toUpperCase() + nomeDoAtributoDetalhe.substring(1, nomeDoAtributoDetalhe.length());
			Class classe = Reflexao.recuperaTipoDeParametroGenerico(objetoPai, nomeDoMetodoQueRepresenta_O_Detalhe);
			Object detalheNovo = instaciarNovoDetalhe(classe, nomeDoMetodoQueRepresenta_O_Detalhe);
			inicializaNovoDetalhe(detalheNovo);
			inicializaNovoDetalhe(detalheNovo, detBox);
			configuraAuditoria(detalheNovo);
			Object objetoAtual = objetoPai;
			Method metodoDetalhe = Reflexao.recuperaMetodoGetDoObjeto(nomeDoAtributoDetalhe, objetoAtual);
			Annotation annotation = null;
			if (listDetAtual == null) {
				listDetAtual = new LinkedHashSet();
			}
			String nomeDoAtributoMestre = "";
			annotation = Reflexao.findAnnotation(metodoDetalhe, OneToMany.class);
			OneToMany oneToMany = (OneToMany) annotation;
			if (oneToMany != null) {
				nomeDoAtributoMestre = oneToMany.mappedBy();
				Method methodMestre = recuperaMetodoGetDoMestre(nomeDoAtributoMestre, detalheNovo, objetoPai);
				Reflexao.atualizarCampoDoObjeto(methodMestre, detalheNovo, objetoPai);
			} else {
				nomeDoAtributoMestre = detBox.getNomeDoAtributoMestre();
				Method methodMestre = recuperaMetodoGetDoMestre(nomeDoAtributoMestre, detalheNovo, objetoPai);
				Reflexao.atualizarCampoDoObjeto(methodMestre, detalheNovo, objetoPai);
			}
			if (nomeDoAtributoMestre == null || "".equals(nomeDoAtributoMestre)) {
				getMessagesHelper().emiteMensagemErro("Não foi possível identificar o atributo referente ao objeto mestre.");
			}
			Method methodDetalhe = Reflexao.recuperaMetodoGetDoObjeto(nomeDoAtributoDetalhe, objetoPai);
			listDetAtual.add(detalheNovo);
			Reflexao.atualizarCampoDoObjeto(methodDetalhe, objetoPai, listDetAtual);
			AbstractListModel listSelBind = null;
			Listbox listbox = detBox.getListboxDet();
			if (Set.class.isAssignableFrom(listDetAtual.getClass())) {
				listSelBind = new BindingListModelSet((Set) listDetAtual, true);
			} else {
				listSelBind = new BindingListModelList((List) listDetAtual, true);
			}
			listbox.setModel(listSelBind);
			listbox.setActivePage(listbox.getPageCount() - 1);
		} catch (Exception e) {
			log.error("Erro ao criar um novo detalhe", e);
		}
	}

	protected void inicializaNovoDetalhe(Object detalheNovo, DetBox detBox) {

	}

	/**
	 * @param objeto
	 */
	public void configuraAuditoria(Object objeto) {
		if (objeto instanceof ProBaseVO) {
			ProBaseVO proBaseVO = (ProBaseVO) objeto;
			proBaseVO.setIpMovimentacao(Executions.getCurrent().getRemoteAddr());
			proBaseVO.setTsMovimentacao(new Date());
			proBaseVO.setCdLoginMovimentacao(getUsuarioVO().getLogin());

			if (proBaseVO.getId() == null) {
				proBaseVO.setTpOperacao("I");
			} else {
				proBaseVO.setTpOperacao("A");
			}

			if (proBaseVO.getStatusDoRegistro() == null) {
				proBaseVO.setStatusDoRegistro(StatusDoRegistro.ATIVO);
			}
		}
		if (objeto instanceof ProVO) {
			ProVO proVO = (ProVO) objeto;
			if (proVO.getDsSituacao() == null) {
				proVO.setDsSituacao(SITUACAO_DO_REGISTRO.EM_EDICAO);
			}
		}
	}

	protected Object instaciarNovoDetalhe(Class classe, String nomeDetalhe) throws InstantiationException, IllegalAccessException {
		Object detalheNovo = classe.newInstance();
		return detalheNovo;
	}

	protected Method recuperaMetodoGetDoMestre(String nomeDoAtributoMestre, Object objetoDetalhe, Object mestre) throws InstantiationException, IllegalAccessException {
		Method methodMestre = null;
		try {
			methodMestre = Reflexao.recuperaMetodoGetDoObjeto(nomeDoAtributoMestre, objetoDetalhe);
			if (methodMestre == null && mestre.getClass() != Object.class) {
				Class<?> superclass = mestre.getClass().getSuperclass();
				String simpleName = superclass.getSimpleName();
				nomeDoAtributoMestre = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
				methodMestre = recuperaMetodoGetDoMestre(nomeDoAtributoMestre, objetoDetalhe, superclass.newInstance());
			}
		} catch (Exception e) {
			log.error("Erro ao recuperar metodo get", e);
		}
		return methodMestre;
	}

	public void excluiDetalhe(DetBox detBox) {
		try {
			String[] nodos = detBox.getNomeDoObjeto().split("\\.");
			String nomeDoAtributo = nodos[nodos.length - 1];
			Object objetoPai = detBox.getObjectPai();
			Listbox listbox = detBox.getListboxDet();
			int indexASerExcluido = listbox.getSelectedIndex();
			if (indexASerExcluido != -1) {
				Object objectASerExcluido = listbox.getModel().getElementAt(indexASerExcluido);
				String nomeDoMestreNoDetalhe = "";
				Method metodoDetalhe = Reflexao.recuperaMetodoGetDoObjeto(nomeDoAtributo, objetoPai);
				Annotation annotation = Reflexao.findAnnotation(metodoDetalhe, OneToMany.class);
				OneToMany oneToMany = (OneToMany) annotation;

				if (!objectASerExcluido.getClass().isAnnotationPresent(ExclusaoLogica.class)) {
					if (oneToMany != null) {
						nomeDoMestreNoDetalhe = oneToMany.mappedBy();
						Method methodMestre = Reflexao.recuperaMetodoGetDoObjeto(nomeDoMestreNoDetalhe, objectASerExcluido);
						Reflexao.atualizarCampoDoObjeto(methodMestre, objectASerExcluido, null);
					}
				}

				Collection listDetAtual = (Collection) getHelperView().recuperaValorDoCampoDoObjetoAtual(metodoDetalhe, objetoPai, getWindowAtual());

				if (!objectASerExcluido.getClass().isAnnotationPresent(ExclusaoLogica.class)) {
					boolean removido = listDetAtual.remove(objectASerExcluido);
					if (!removido) {
						getMessagesHelper().emiteMensagemErro("Não foi possível remover o item, provavelmente o metodos equals não foi implementado para o objeto " + objectASerExcluido.getClass().getSimpleName() + ".");
						return;
					}
				}
				try {
					if (((ProBaseVO) objectASerExcluido).getId() != null) {
						((ProBaseVO) objetoPai).setExcluindoDetalhe(true);
						if (excluirDetalheNaLinha(detBox)) {
							if (objectASerExcluido.getClass().isAnnotationPresent(ExclusaoLogica.class)) {
								((ProBaseVO) objectASerExcluido).setStatusDoRegistro(StatusDoRegistro.EXCLUIDO);
								// listDetAtual.add(objectASerExcluido);
							}

							salvar();

						}
					}
					listDetAtual = (Collection) getHelperView().recuperaValorDoCampoDoObjetoAtual(metodoDetalhe, objetoPai, getWindowAtual());
					AbstractListModel listSelBind = null;
					if (listDetAtual instanceof Set) {
						listSelBind = new BindingListModelSet((Set) listDetAtual, true);
					} else {
						listSelBind = new BindingListModelList((List) listDetAtual, true);
					}
					listbox.setModel(listSelBind);
					if (!listDetAtual.isEmpty()) {
						if (indexASerExcluido > 0) {
							listbox.setSelectedIndex(indexASerExcluido - 1);
						} else {
							listbox.setSelectedIndex(indexASerExcluido);
						}
					}
				} catch (Exception e) {
					throw e;
				}
			} else {
				getMessagesHelper().emiteMensagemErro("Selecione um item para excluí-lo.");
				return;
			}
		} catch (Exception e) {
			getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
	}

	public boolean excluirDetalheNaLinha(DetBox detalhe) {
		return true;
	}

	public boolean detalheSomenteLeitura(DetBox detalhe) {
		return false;
	}

	public IProFacade repositorio() {
		return fachadaDeNegocio;
	}

	public ProAnnotateDataBinder getBinder() {
		return binder;
	}

	public void setBinder(ProAnnotateDataBinder binder) {
		this.binder = binder;
	}

	public ProMessageHelper getMessagesHelper() {
		return messagesHelper;
	}

	public void setMessagesHelper(ProMessageHelper messagesHelper) {
		this.messagesHelper = messagesHelper;
	}

	public ProHelperView getHelperView() {
		return ProHelperView.getInstance(this.getClass());
	}

	public ProAnnotateDataBinderHelper getProAnnotateDataBinderHelper() {
		return ProAnnotateDataBinderHelper.getInstance(this.getClass());
	}

	public IUsuarioBase getUsuarioVO() {
		return usuarioVO;
	}

	public void setUsuarioVO(IUsuario usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public IUnidadeBase getUnidadeAdministrativaVO() {
		return unidadeVO;
	}

	public void setUnidadeAdministrativaVO(IUnidade unidadeVO) {
		this.unidadeVO = unidadeVO;
	}

	public void aprovar() {
		try {
			Context.setAttribute("usuarioLogado", getUsuarioVO());
			repositorio().aprovar(getObjetoAtual());
			this.objetoAtual = (ENTITY) fachadaDeNegocio.recuperaObjeto((ProVO) objetoAtual);
			if (objetoAtual == null) {
				throw new Exception("Não foi possível recuperar objeto após aprovação.");
			}
			getBinder().loadAll();
			ajustarLayoutParaManutencao();
			Component component = getTela().getFellowIfAny(br.gov.prodigio.comuns.constantes.Constantes.BOTOES.DIV_BOTOES);
			if (component == null)
				component = getTela();
			getMessagesHelper().emiteMensagemSucesso("Registro aprovado com sucesso!", component);
		} catch (Exception e) {
			getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
	}

	public void concluir() {
		if (getMessagesHelper().desejaRealmenteConcluir()) {
			try {
				Context.setAttribute("evento", "concluir");
				antesConcluir(objetoAtual);
				this.objetoAtual = (ENTITY) repositorio().concluir(getObjetoAtual(), new HashMap());
				Context.setAttribute("usuarioLogado", getUsuarioVO());
				if (objetoAtual == null) {
					throw new Exception("Não foi possível recuperar objeto após conclusão.");
				}
				getBinder().loadAll();
				ajustarLayoutParaManutencao();
				Component component = getTela().getFellowIfAny(br.gov.prodigio.comuns.constantes.Constantes.BOTOES.DIV_BOTOES);
				if (component == null)
					component = getTela();
				getMessagesHelper().emiteMensagemSucesso("Registro concluído com sucesso!", component);
				Context.setAttribute("evento", null);
				aposConcluir();
			} catch (Exception e) {
				getMessagesHelper().emiteMensagemErro(e.getMessage());
			}
		}
	}

	protected void ajustarLayoutParaManutencao() {
		getHelperView().ajustaLayoutParaManutencao(getWindowAtual());
	}

	public void aposConcluir() {
	}

	public List<IUnidade> getListaUnidade() {
		return listaUnidade;
	}

	public void proximo() throws Exception, InterruptedException {
		log.debug("teste");
	}

	public void imprimir() throws Exception, InterruptedException {
		/*
		 * AppBaseVO appBaseVO = (AppBaseVO) getObjetoAtual(); byte[] pdfBytes = null; try { if (appBaseVO.getSituacao().equals(SITUACAO_DO_REGISTRO.EM_EDICAO)) { pdfBytes = getFachadaDeNegocio().recuperaPDFView(appBaseVO); } else { if
		 * (appBaseVO.getFoto() != null) { pdfBytes = appBaseVO.getFoto(); } else { pdfBytes = getFachadaDeNegocio().recuperaPDFView(appBaseVO); } } String agente = Executions.getCurrent().getUserAgent(); if (agente.contains("iPad"))
		 * imprimeParaTablets(pdfBytes); else { imprimePadrao(pdfBytes); } } catch (DocumentException e1) { e1.printStackTrace(); }
		 */
	}

	public void imprimePadrao(byte[] pdfBytes) throws Exception, InterruptedException {
		Window popupImpressao = (Window) getWindowAtual().getFellowIfAny("popup");
		if (popupImpressao == null) {
			popupImpressao = (Window) Executions.createComponents("impressao.zul", getWindowAtual(), null);
		}
		Iframe iframe = (Iframe) popupImpressao.getFellowIfAny("iframe");
		AMedia am = new AMedia(null, "pdf", "application/pdf", pdfBytes);
		iframe.setContent(am);
		popupImpressao.doModal();
	}

	public void download(byte[] pdfBytes, String tipo, String nome) throws Exception, InterruptedException {
		if (tipo != null && !tipo.isEmpty()) {
			Filedownload.save(pdfBytes, tipo, nome);
		} else {
			Filedownload.save(pdfBytes, "application/notepad", nome);
		}
		/*
		 * Window popupImpressao = (Window) getWindowAtual().getFellowIfAny("popup"); if (popupImpressao == null) { popupImpressao = (Window) Executions.createComponents("impressao.zul", getWindowAtual(), null); } Iframe iframe = (Iframe)
		 * popupImpressao.getFellowIfAny("iframe"); AMedia am = null; am = new AMedia(null, "pdf", "application/pdf", pdfBytes); iframe.setContent(am); popupImpressao.doModal();
		 */
	}

	// private void imprimeParaTablets(byte[] pdfBytes) throws FileNotFoundException, IOException {
	// HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
	// request.getSession().setAttribute("impressao", pdfBytes);
	// Clients.evalJavaScript("window.open('impressao.jsp','" + getObjetoAtual() + "','fullscreen=yes')");
	// }

	public void configuraOrdenacao(Listheader listheader) {
		ComparadorGenerico comparadorAsc = new ComparadorGenerico(true, listheader.getValue().toString());
		listheader.setSortAscending(comparadorAsc);
		ComparadorGenerico comparadorDsc = new ComparadorGenerico(false, listheader.getValue().toString());
		listheader.setSortDescending(comparadorDsc);
	}

	@SuppressWarnings("static-access")
	public List retornaListaDoEscopoDeAplicacao(String nomeDaLista) throws Exception {
		Collection colecao = (Collection) getWindowAtual().getAttribute(nomeDaLista, getWindowAtual().APPLICATION_SCOPE);
		if (colecao == null) {
			colecao = retornaListaDoEscopoDeAplicacao(nomeDaLista, null);
		}
		return new ArrayList(colecao);
	}

	@SuppressWarnings("static-access")
	public List recuperaListaDaClasseLookUp(String nomeDaLista) {
		List lista = new ArrayList();
		Collection collection = null;
		try {
			collection = (Collection) getWindowAtual().getAttribute(nomeDaLista, getWindowAtual().APPLICATION_SCOPE);
			if (collection == null || collection.isEmpty()) {
				log.warn(" {} está vazia, provavelmente a entidade(VO) não esta anotada como @Lookup", nomeDaLista);
			}
			lista = new ArrayList(collection);
		} catch (Exception e) {
			log.error(" {} está vazia, provavelmente a entidade(VO) não esta anotada como @Lookup", nomeDaLista);
		}
		return lista;
	}

	@SuppressWarnings("static-access")
	protected Collection retornaListaDoEscopoDeAplicacao(String nomeDaLista, Bandboxbind bandbox) throws Exception, InstantiationException, IllegalAccessException {
		Object colecao = null;
		Class cl = null;
		try {
			if (bandbox.getObject() == null) {
				String atributo = bandbox.getNomeDoObjeto().replace("classecontrole.objetoAtual.", "");
				atributo = atributo.replace("classecontrole.objetoAtualArg.", "");
				atributo = atributo.replace("objetoDaLista.", "");
				cl = Reflexao.recuperaTipoDeRetornoDoMetodoGetNoGrafo(atributo, getObjetoAtual());
			} else {
				cl = bandbox.getObject().getClass();
			}
			if (cl.isEnum()) {
				colecao = getWindowAtual().getAttribute("listaDe" + cl.getSimpleName(), getWindowAtual().APPLICATION_SCOPE);
			} else {
				ProBaseVO vo = (ProBaseVO) cl.newInstance();
				colecao = getWindowAtual().getAttribute("listaDe" + cl.getSimpleName(), getWindowAtual().APPLICATION_SCOPE);
				if (colecao == null) {
					colecao = repositorio().listarBaseadoNoExemplo(vo, vo, 0, 5000, "nrVersao");
					getWindowAtual().setAttribute("listaDe" + cl.getSimpleName(), colecao, getWindowAtual().APPLICATION_SCOPE);
				}
			}
		} catch (Exception e) {
			getMessagesHelper().emiteMensagemErro("Não foi possível recuperar a lista: " + cl.getSimpleName() + ". Implemente o método filtro no controle do caso de uso.");
		}
		if (colecao != null) {
			ArrayList lst = new ArrayList((Collection) colecao);
			ordenaLista(lst);
			return lst;
		} else {
			return new ArrayList();
		}

	}

	protected void ordenaLista(List lst) {

	}

	public List getLista(String nomeDaLista) {
		try {
			return retornaListaDoEscopoDeAplicacao(nomeDaLista);
		} catch (Exception e) {
			log.error("Erro ao recuperar lista", e);
		}
		return new ArrayList();
	}

	public Window getTela() {
		return tela;
	}

	public void setTela(Window tela) {
		this.tela = tela;
	}

	@SuppressWarnings("static-access")
	public String getDiretorioDaAplicacao() {
		String caminho = "";
		try {
			caminho = (String) getWindowAtual().getAttribute(ContextParameters.DIRETORIO_DA_APLICACAO, getWindowAtual().APPLICATION_SCOPE);
		} catch (Exception e) {
			log.error("Erro ao recuperar diretório da aplicação", e);
		}
		return caminho;
	}

	/**
	 * @param listaLadoDireto
	 * @param listaLadoEsquerdo
	 * @author P606018
	 */
	public void alternaValorParaListaDual(Listbox listaLadoDireto, Listbox listaLadoEsquerdo) {
		BindingListModelSet bindingListModelSetDestino = null;
		BindingListModelSet bindingListModelSetOrigem = new BindingListModelSet(new HashSet(), true);

		if (listaLadoEsquerdo.getModel() instanceof BindingListModelSet) {
			bindingListModelSetDestino = (BindingListModelSet) listaLadoEsquerdo.getModel();
		} else {
			bindingListModelSetDestino = new BindingListModelSet(new HashSet(), true);
		}

		if (listaLadoDireto.getModel() instanceof BindingListModelList) {
			BindingListModelList lista = (BindingListModelList) listaLadoDireto.getModel();
			for (Object object : lista) {
				bindingListModelSetOrigem.add(object);
			}
		} else {
			bindingListModelSetOrigem = (BindingListModelSet) listaLadoDireto.getModel();
		}

		if (bindingListModelSetDestino == null) {
			bindingListModelSetDestino = new BindingListModelSet(new HashSet(), true);
		}
		Set<Listitem> itensSeleciondadosNoOrigem = new HashSet<Listitem>(listaLadoDireto.getSelectedItems());

		for (Listitem itemSelecionado : itensSeleciondadosNoOrigem) {
			bindingListModelSetOrigem.getInnerSet().remove(itemSelecionado.getValue());
			if (!bindingListModelSetDestino.getInnerSet().contains(itemSelecionado.getValue())) {
				bindingListModelSetDestino.getInnerSet().add(itemSelecionado.getValue());
			}
		}
		listaLadoDireto.setModel(bindingListModelSetOrigem);

		if (listaLadoEsquerdo.getModel() instanceof BindingListModelList) {
			BindingListModelList binding = new BindingListModelList(new ArrayList(), true);
			binding.addAll(bindingListModelSetDestino);
			listaLadoEsquerdo.setModel(binding);
		} else {
			listaLadoEsquerdo.setModel(bindingListModelSetDestino);
		}
	}

	public void inicializaDualListBoxOrigem(Listboxdual listboxdual) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		String expressao = listboxdual.getNomeDoObjeto();
		BindingListModelSet setBindDestino = new BindingListModelSet(new HashSet(), true);

		if ("".equals(expressao) || expressao == null) {
			expressao = "classecontrole.objetoAtual." + listboxdual.getId();
		}
		String[] nodosDaExpressao = expressao.split("\\.");
		int lengthNodosDaExpressao = nodosDaExpressao.length;
		String nomeObjetoMany = "";
		for (int i = (lengthNodosDaExpressao - 2); i < (lengthNodosDaExpressao); i++) {
			nomeObjetoMany = nomeObjetoMany + "." + nodosDaExpressao[i];
		}
		String nomeDaListaMany = nodosDaExpressao[lengthNodosDaExpressao - 2];

		nomeObjetoMany = nomeObjetoMany.substring(1);
		Object objetoOne = listboxdual.getObjetoPai();
		if (objetoOne == null) {
			return;
		}
		Method metodoQueRecuperaObjetoDoLadoMany = null;
		Collection<? extends ProBaseVO> colecaoDeObjetosQuejaEstaoNaEntidade = null;
		try {
			metodoQueRecuperaObjetoDoLadoMany = Reflexao.recuperaMetodoGetDoObjeto(nomeObjetoMany, objetoOne);
			colecaoDeObjetosQuejaEstaoNaEntidade = (Collection<? extends ProBaseVO>) Reflexao.recuperaValorDaPropriedade(nomeDaListaMany, objetoOne);

		} catch (Exception e) {
			log.error("Erro ao inicializar DualListBox", e);
		}
		Class classeDonaDoMetodo = metodoQueRecuperaObjetoDoLadoMany.getDeclaringClass();
		String nomeDaClasseDoOjetoMany = metodoQueRecuperaObjetoDoLadoMany.getReturnType().getSimpleName();

		Collection colecaoDeObjetosParaEscolhaDoUsuario = retornaListaParaDualListBox(nomeDaClasseDoOjetoMany);
		if (colecaoDeObjetosParaEscolhaDoUsuario instanceof Set) {
			BindingListModelSet listSelBind = new BindingListModelSet((Set) colecaoDeObjetosParaEscolhaDoUsuario, true);
			listboxdual.getOrigem().setModel(listSelBind);
		} else {
			Set listaDoObjetoMany = new HashSet();
			for (Object objetoLookUp : colecaoDeObjetosParaEscolhaDoUsuario) {
				Method metodoQueConfiguaOObjetoLookup = classeDonaDoMetodo.getMethod("set" + metodoQueRecuperaObjetoDoLadoMany.getName().substring(3), metodoQueRecuperaObjetoDoLadoMany.getReturnType());
				Object objetoMany = instaciarNovoDetalhe(classeDonaDoMetodo, metodoQueConfiguaOObjetoLookup.toString());
				boolean existe = false;
				if (colecaoDeObjetosQuejaEstaoNaEntidade != null) {
					for (Object objeto : colecaoDeObjetosQuejaEstaoNaEntidade) {
						if (objeto.toString().equals(objetoLookUp.toString())) {
							setBindDestino.add(objeto);
							existe = true;
							break;
						}
					}
				}
				if (!existe) {
					metodoQueConfiguaOObjetoLookup.invoke(objetoMany, objetoLookUp);
					listaDoObjetoMany.add(objetoMany);
				}
			}
			BindingListModelSet setBind = new BindingListModelSet(listaDoObjetoMany, true);

			listboxdual.getOrigem().setModel(setBind);
			listboxdual.getDestino().setModel(setBindDestino);
		}
	}

	protected Collection retornaListaParaDualListBox(String nomeDaClasse) {
		Collection colecao = getLista("listaDe" + nomeDaClasse);
		return colecao;
	}

	public void adicionaComparadorParaCampos(ListheaderSort campoNaTela) {
		campoNaTela.setSort("auto");
		ComparadorGenerico comparadorAsc = new ComparadorGenerico(true, campoNaTela.getValue().toString());
		campoNaTela.setSortAscending(comparadorAsc);
		ComparadorGenerico comparadorDsc = new ComparadorGenerico(false, campoNaTela.getValue().toString());
		campoNaTela.setSortDescending(comparadorDsc);
	}

	public void somaCelulas(Listfootersum campoNaTela) {
		campoNaTela.getParent();
	}

	public void aplicaRegraDeNomeacaoDeComponentes(final FieldValidator comp) {
		if (comp.getNomeDoObjeto() == null) {
			if (((Component) comp).getId() != null) {
				comp.setNomeDoObjeto(((Component) comp).getId());
				if (comp.getNomeDoObjeto().endsWith("Arg")) {
					comp.setNomeDoObjeto("classecontrole.objetoAtualArg." + comp.getNomeDoObjeto().substring(0, comp.getNomeDoObjeto().length() - 3));
				} else {
					comp.setNomeDoObjeto("classecontrole.objetoAtual." + comp.getNomeDoObjeto().substring(0, comp.getNomeDoObjeto().length()));
				}
			}
		}
	}

	public void onSelectBandBox(Event e) throws Exception {
		Listitem listitem = null;
		Bandboxbind bandbox = null;
		if (e.getTarget() instanceof Listbox) {
			listitem = ((Listbox) e.getTarget()).getSelectedItem();
			bandbox = (Bandboxbind) e.getTarget().getParent().getParent();
		} else if (e.getTarget() instanceof Listitem) {
			listitem = (Listitem) e.getTarget();
			bandbox = (Bandboxbind) e.getTarget().getParent().getParent().getParent();
		}

		if (listitem != null) {
			Object objectPai = bandbox.getObjectPai();
			Object object = listitem.getValue();
			atribuiValorSelecionadoAoBandBox(bandbox, object);
			aposOnSelectBandBox(e, objectPai);
			Clients.evalJavaScript("corrigeCursorBandBox(\"" + bandbox.getUuid() + "-real\")");
			bandbox.close();
			bandbox.setOpen(false);
			bandbox.setFocus(true);
		}
	}

	public void registrarEventoOnKeyUpBandbox(Event e) {
		Bandboxbind target = (Bandboxbind) e.getTarget();
		target.setWidgetAttribute("onkeyup", "verificarBandboxLenght(event);");
	}

	protected String getAtributoDoBandBox(Bandboxbind bandbox) {
		String nomeDoAtributo = bandbox.getNomeDoObjeto().replace("classecontrole.objetoAtual.", "").replace("classecontrole.objetoAtualArg.", "");
		if (!("".equals(nomeDoAtributo) || nomeDoAtributo == null)) {
			String[] split = bandbox.getNomeDoObjeto().split("\\.");
			nomeDoAtributo = split[split.length - 1];
		}
		return nomeDoAtributo;
	}

	public void atribuiObjetoVazioAoBandBox(Bandboxbind bandbox, Object object) throws Exception, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		if (object.getClass().isEnum()) {
			String nomeDoAtributo = bandbox.getNomeDoObjeto().replace("classecontrole.objetoAtual.", "").replace("classecontrole.objetoAtualArg.", "");
			if (!("".equals(nomeDoAtributo) || nomeDoAtributo == null)) {
				String[] split = bandbox.getNomeDoObjeto().split("\\.");
				nomeDoAtributo = split[split.length - 1];
			}
			try {
				Reflexao.atualizarCampoDoObjetoParaNull(nomeDoAtributo, bandbox.getObjectPai(), object);
			} catch (Exception e) {
				log.error("Erro ao limpar objeto ", e);
			}
		} else {
			object = instanciaNovoObjeto(object.getClass());
			String nomeDoAtributo = bandbox.getNomeDoObjeto().replace("classecontrole.objetoAtual.", "").replace("classecontrole.objetoAtualArg.", "");
			if (!("".equals(nomeDoAtributo) || nomeDoAtributo == null)) {
				String[] split = bandbox.getNomeDoObjeto().split("\\.");
				nomeDoAtributo = split[split.length - 1];
			}
			Object objectPai = bandbox.getObjectPai();
			if (objectPai == null) {
				getMessagesHelper().emiteMensagemErro("O atributo " + bandbox.getNomeDoObjeto() + " não esta inicializado. ");
				return;
			}
			if (bandbox.getNomeDoObjeto().contains("classecontrole.objetoAtualArg")) {
				if (object != null && !object.getClass().isEnum()) {
					ProBaseVO proVO = (ProBaseVO) object;
					Long id = proVO.getId();
					proVO = proVO.getClass().newInstance();
					proVO.setId(id);
					Reflexao.atualizarCampoDoObjetoParaNull(nomeDoAtributo, objectPai, proVO);
				} else {
					Reflexao.atualizarCampoDoObjetoParaNull(nomeDoAtributo, objectPai, object);
				}
			} else {
				Reflexao.atualizarCampoDoObjetoParaNull(nomeDoAtributo, objectPai, object);
			}
			Object aux = object;
			if (bandbox.getAtributoQueSeraVisualizado() != null && !"".equals(bandbox.getAtributoQueSeraVisualizado())) {
				aux = Reflexao.recuperaValorDaPropriedade(bandbox.getAtributoQueSeraVisualizado(), object);
			}
			bandbox.setObject(object);
			if (aux != null) {
				bandbox.setValue(aux.toString());
			}

		}
	}

	public void atribuiValorSelecionadoAoBandBox(Bandboxbind bandbox, Object object) {
		String nomeDoAtributo = bandbox.getNomeDoObjeto().replace("classecontrole.objetoAtual.", "").replace("classecontrole.objetoAtualArg.", "");
		if (!("".equals(nomeDoAtributo) || nomeDoAtributo == null)) {
			String[] split = bandbox.getNomeDoObjeto().split("\\.");
			nomeDoAtributo = split[split.length - 1];
		}
		Object objectPai = bandbox.getObjectPai();
		if (objectPai == null) {
			getMessagesHelper().emiteMensagemErro("O atributo " + bandbox.getNomeDoObjeto() + " não esta inicializado. ");
			return;
		}

		atualizarAtributoDaEntidade(bandbox, object, nomeDoAtributo, objectPai);

		Object aux = object;
		String propriedadeVisualizada = bandbox.getAtributoQueSeraVisualizado();
		if (propriedadeVisualizada != null && !"".equals(propriedadeVisualizada)) {
			try {
				aux = Reflexao.recuperaValorDaPropriedade(propriedadeVisualizada, object);
			} catch (Exception e) {
				log.error("Erro ao atribuir valor ao bandbox", e);
				getMessagesHelper().emiteMensagemErro(String.format("Não conseguiu atualizar o atributo %s do objeto %s com o valor selecionado.", nomeDoAtributo, object.getClass().getName()));
			}
		}
		bandbox.setObject(object);
		if (aux != null) {
			bandbox.setValue(aux.toString());
		}
		String metodo = bandbox.getMetodoSelecao();
		Method method = null;
		Map<String, Component> dependentes = bandbox.recuperaComponentesDependentes();
		if (dependentes != null) {
			for (Map.Entry<String, Component> entry : dependentes.entrySet()) {
				Component bandboxDependentes = entry.getValue();
				if (bandboxDependentes instanceof Bandboxbind) {
					((Bandboxbind) bandboxDependentes).clear(this);
				}
			}
		}
		if (metodo != null && !"".equals(metodo)) {
			try {
				method = this.getClass().getMethod(metodo, new Class[] { Bandboxbind.class, Bandboxbind.class });
			} catch (SecurityException e) {
				log.error("Erro ao atribuir valor ao bandbox", e);
			} catch (NoSuchMethodException e) {
				log.error("Erro ao atribuir valor ao bandbox", e);
			}
			Bandboxbind bandboxpai = (Bandboxbind) bandbox.getAttribute(bandbox.getDependeDoComponente());
			try {
				method.invoke(this, new Object[] { bandbox, bandboxpai });
			} catch (IllegalArgumentException e) {
				log.error("Erro ao atribuir valor ao bandbox", e);
			} catch (IllegalAccessException e) {
				log.error("Erro ao atribuir valor ao bandbox", e);
			} catch (InvocationTargetException e) {
				log.error("Erro ao atribuir valor ao bandbox", e);
			}
		}
		if (bandbox.getAtualizarComponentes() != null && !"".equals(bandbox.getAtualizarComponentes())) {
			String[] nomeDosComponentes = bandbox.getAtualizarComponentes().split(",");
			for (String componente : nomeDosComponentes) {
				try {
					Component parent = ProHelperView.recuperaParentContexto(bandbox);
					Component comp = ProHelperView.recuperaComponent(parent, bandbox.getAtualizarComponentes());
					getBinder().loadComponent(comp);
				} catch (Exception e) {
					getMessagesHelper().emiteMensagemErro("Não foi possível atualizar o componente: " + componente);
				}
			}
		}
	}

	private void atualizarAtributoDaEntidade(Bandboxbind bandbox, Object object, String nomeDoAtributo, Object objectPai) {
		if (bandbox.getNomeDoObjeto().contains("classecontrole.objetoAtualArg")) {
			atualizarAtributoEntidadePesquisa(object, nomeDoAtributo, objectPai);
		} else {
			atualizarAtributoEntidadeManutencao(object, nomeDoAtributo, objectPai);
		}
	}

	private void atualizarAtributoEntidadeManutencao(Object object, String nomeDoAtributo, Object objectPai) {
		try {
			Reflexao.atualizarCampoDoObjeto(nomeDoAtributo, objectPai, object);
		} catch (Exception e) {
			log.error("Erro ao atualizar atributo da entidade na manutenção", e);
			getMessagesHelper().emiteMensagemErro(String.format("Não conseguiu atualizar o atributo %s do objeto %s com o valor selecionado.", nomeDoAtributo, object.getClass().getName()));
		}
	}

	protected void atualizarAtributoEntidadePesquisa(Object object, String nomeDoAtributo, Object objectPai) {
		if (!object.getClass().isEnum()) {
			try {
				Reflexao.atualizarCampoDoObjeto(nomeDoAtributo, objectPai, object);
			} catch (Exception e) {
				log.error("Erro ao atualizar campo do objeto", e);
				getMessagesHelper().emiteMensagemErro(String.format("Não conseguiu atualizar o atributo %s do objeto %s com o valor selecionado.", nomeDoAtributo, object.getClass().getName()));
			}
		} else {
			try {
				Reflexao.atualizarCampoDoObjeto(nomeDoAtributo, objectPai, object);
			} catch (Exception e) {
				log.error("Erro ao atualizar campo do objeto", e);
				getMessagesHelper().emiteMensagemErro(String.format("Não conseguiu atualizar o atributo %s do objeto %s com o valor selecionado.", nomeDoAtributo, object.getClass().getName()));
			}
		}
	}

	protected void aposOnSelectBandBox(Event e, Object objectPai) {
	}

	public void criaListParaBandBox(final Bandboxbind bandbox, String objeto) {
		if (bandbox.getChildren() == null || bandbox.getChildren().size() == 0) {
			String defaultListHeight = "300px";
			Bandpopup bandpopup = new Bandpopup();
			Listbox listbox = new Listbox();
			bandpopup.appendChild(listbox);
			bandbox.setListbox(listbox);
			listbox.setParent(bandpopup);
			listbox.setHeight(bandbox.getHeightList());
			listbox.setWidth(bandbox.getWidthList());
			listbox.setMold("paging");
			listbox.setPageSize(10);
			Map ann = new HashMap();
			ann.put("value", objeto);
			listbox.addAnnotation("selectedItem", "default", ann);
			Listhead listhead = new Listhead();
			listbox.appendChild(listhead);
			listbox.setItemRenderer(new AppListRender());
			Listitem listitem = new Listitem();
			Map annItem = new HashMap();
			String obj = "";
			if (bandbox.getId() != null && !bandbox.getId().equals("")) {
				obj = bandbox.getId();
			} else {
				obj = objeto;
			}
			annItem.put("each", obj);
			annItem.put("value", obj);
			listitem.addAnnotation("value", "default", annItem);
			listbox.appendChild(listitem);

			if (bandbox.getHeightList() != null && !bandbox.getHeightList().equals("")) {
				bandpopup.setHeight(bandbox.getHeightList());
			} else {
				bandpopup.setHeight(defaultListHeight);
				listbox.setVflex("1");
			}
			if (bandbox.getWidthList() != null && !bandbox.getWidthList().equals("")) {
				String listWidth = bandbox.getWidthList();
				bandpopup.setWidth(listWidth);
			} else {
				bandpopup.setWidth("500px");
			}
			if (bandbox.getLabelValorList() != null && !bandbox.getLabelValorList().equals("")) {
				String[] labelValores = bandbox.getLabelValorList().split(";");
				for (int i = 0; i < labelValores.length; i++) {
					String[] labelValor = labelValores[i].split(":");
					String[] width = labelValor[0].split("-");
					Listheader listheader = new Listheader();
					if (width != null && width.length == 2) {
						labelValor[0] = width[0];
						listheader.setWidth(width[1]);
					}

					listhead.appendChild(listheader);
					listheader.setLabel(labelValor[0].toUpperCase());
				}
			} else {
				Listheader listheader = new Listheader();
				listhead.appendChild(listheader);
				listheader.setLabel("Nome");
			}
			bandbox.appendChild(bandpopup);
		}
	}

	public void filtraBandboxButtonSearch(Bandboxbind bandbox, boolean usarFiltroEmMemoria) {
		try {
			atualizarInplaceBandBox(bandbox);

			List itensFiltradosPelaDigitacao = new ArrayList();

			if (usarFiltroEmMemoria) {
				filtrarPelaDigitacao(bandbox, itensFiltradosPelaDigitacao);
			} else {
				preencherItensBandbox(bandbox, itensFiltradosPelaDigitacao);
			}

			popularListDoBandBox(bandbox, itensFiltradosPelaDigitacao);
			abrirBandBox(bandbox, itensFiltradosPelaDigitacao);

		} catch (ComponentNotFoundException e) {
			log.error("Erro no filtro do bandbox", e);
			getMessagesHelper().emiteMensagemErro(e);
		}
	}

	public void filtraBandboxTeclado(Bandboxbind bandbox, boolean usarFiltroEmMemoria) {
		this.filtraBandbox(bandbox, usarFiltroEmMemoria);
		this.ajustarAlturaListaBandBox(bandbox);
	}

	public void filtraBandbox(Bandboxbind bandbox, boolean usarFiltroEmMemoria) {
		try {
			List itensFiltradosPelaDigitacao = new ArrayList();

			if (usarFiltroEmMemoria) {
				filtrarPelaDigitacao(bandbox, itensFiltradosPelaDigitacao);
			} else {
				preencherItensBandbox(bandbox, itensFiltradosPelaDigitacao);
			}

			boolean verificarAtribuicaoAuto = verificarAtribuicaoAutomatica(bandbox, itensFiltradosPelaDigitacao);

			if (verificarAtribuicaoAuto) {
				atribuiValorSelecionadoAoBandBox(bandbox, itensFiltradosPelaDigitacao.get(0));
			}

			popularListDoBandBox(bandbox, itensFiltradosPelaDigitacao);
			abrirBandBox(bandbox, itensFiltradosPelaDigitacao);

		} catch (ComponentNotFoundException e) {
			log.error("Erro no filtro do bandbox", e);
			getMessagesHelper().emiteMensagemErro(e);
		}
	}

	private void atualizarInplaceBandBox(Bandboxbind bandbox) {
		bandbox.setInplace(true);
		bandbox.setInplace(false);
	}

	private void abrirBandBox(Bandboxbind bandbox, List listaItensBandBox) {
		if (!listaItensBandBox.isEmpty()) {
			bandbox.open();
			bandbox.setAberto(true);
		} else {
			InputElement input = bandbox;
			input.setFocus(true);
			String erroFiltro = null;
			if (bandbox.getAttribute("erroFiltro") != null && !"".equals(bandbox.getAttribute("erroFiltro"))) {
				erroFiltro = (String) bandbox.getAttribute("erroFiltro");
				throw new WrongValueException(input, erroFiltro);
			} else {
				throw new WrongValueException(input, "Nenhum registro encontrado!");
			}
		}
	}

	protected void ajustarAlturaListaBandBox(Bandboxbind bandbox) {
		if (bandbox.getHeightList() == null || bandbox.getHeightList().equals("")) {
			Listbox listbox = bandbox.getListbox();
			Bandpopup bandpopup = ((Bandpopup) bandbox.getChildren().get(0));
			Integer quantidadeItens = listbox.getModel().getSize();
			if (quantidadeItens < 10) {
				bandpopup.setHeight(50 + (quantidadeItens * getAlturaDoItemDoBandboxbind()) + "px");
			} else {
				bandpopup.setHeight(30 + (12 * getAlturaDoItemDoBandboxbind()) + "px");
			}
		}
	}

	protected int getAlturaDoItemDoBandboxbind() {
		return 30;
	}

	private Listbox popularListDoBandBox(Bandboxbind bandbox, List lista) {
		Listbox listbox = bandbox.getListbox();

		if (listbox != null) {
			ListModel dictModel2 = new BindingListModelList(lista, true);
			listbox.setModel(dictModel2);
		}
		return listbox;
	}

	private boolean verificarAtribuicaoAutomatica(Bandboxbind bandbox, List itensFiltradosPelaDigitacao) {
		return itensFiltradosPelaDigitacao.size() == 1 && bandbox.getAtribuicaoAutomatica().equals("S");
	}

	public void filtrarPelaDigitacao(Bandboxbind bandbox, List aux) {
		try {
			List listaItensBandBox = recuperaDadosDaListaBandBox(bandbox);
			if (listaItensBandBox != null) {
				String[] filtroArray = getListaFiltro(bandbox);
				Object vlrToString = null;
				for (Object itemLista : listaItensBandBox) {
					if (bandbox.getAtributoQueSeraVisualizado() != null && !"".equals(bandbox.getAtributoQueSeraVisualizado()) && !bandbox.getAtributoQueSeraVisualizado().equals("toString")) {
						vlrToString = Reflexao.recuperaValorDaPropriedade(bandbox.getAtributoQueSeraVisualizado(), itemLista);
					} else {
						vlrToString = itemLista;
					}

					String valueBandBox = "";
					valueBandBox = bandbox.getText();
					Set filtroSet = criaFiltro(filtroArray, bandbox, itemLista);
					filtroSet.add(vlrToString);

					if (filtraEntidade(filtroSet, valueBandBox) && filtraEntidade(vlrToString)) {
						aux.add(itemLista);
					}
				}
			}
		} catch (Exception e) {
			log.warn("Erro ao filtrar pela digitação", e);
			getMessagesHelper().emiteMensagemErro(String.format("Erro ao filtrar pela digitação: %s", e.getMessage()));
		}
	}

	public void preencherItensBandbox(Bandboxbind bandbox, List aux) {
		try {
			List listaItensBandBox = recuperaDadosDaListaBandBox(bandbox);
			if (listaItensBandBox != null) {
				for (Object itemLista : listaItensBandBox) {
					aux.add(itemLista);
				}
			}
		} catch (Exception e) {
			log.warn("Erro ao preencher itens do componente", e);
			getMessagesHelper().emiteMensagemErro(String.format("Erro ao preencher itens do componente: %s", e.getMessage()));
		}
	}

	private String[] getListaFiltro(Bandboxbind bandbox) {
		String filtroStr = bandbox.getFiltro();
		String[] filtroArray = new String[0];
		if (filtroStr != null && !filtroStr.equals("")) {
			filtroArray = filtroStr.split("\\;");
		}
		return filtroArray;

	}

	private Set criaFiltro(String[] filtroArray, Bandboxbind bandbox, Object itemLista) throws Exception {
		Set filtroSet = new HashSet();
		Object vlrToString = null;
		for (String filtro : filtroArray) {
			vlrToString = Reflexao.recuperaValorDaPropriedade(filtro, itemLista);
			if (vlrToString != null) {
				filtroSet.add(vlrToString);
			}

		}
		return filtroSet;
	}

	private boolean filtraEntidade(Set filtro, String valueBandBox) {
		Iterator iter = filtro.iterator();
		Object vlrToString = null;
		while (iter.hasNext()) {
			vlrToString = iter.next();
			if (vlrToString != null && vlrToString.toString().toUpperCase().contains(valueBandBox.trim().toUpperCase())) {
				return true;
			}
		}

		return false;
	}

	private List recuperaDadosDaListaBandBox(Bandboxbind bandbox) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		String nomeAtributoSimples = " ";
		String[] divisaoNomeCompletoAtributo = bandbox.getNomeDoObjeto().split("\\.");
		int indiceNomeDoObjetoSimples = divisaoNomeCompletoAtributo.length - 1;
		nomeAtributoSimples = divisaoNomeCompletoAtributo[indiceNomeDoObjetoSimples];

		String metodo = bandbox.getMetodoFiltro();
		Method method = null;
		List lista = new ArrayList<>();
		if (metodo != null && !"".equals(metodo)) {
			method = this.getClass().getMethod(metodo, new Class[] { Bandboxbind.class });
			lista = (List) method.invoke(this, new Object[] { bandbox });
		} else {
			String componentDependido = bandbox.getDependeDoComponente();
			if (componentDependido != null) {
				Component comp = getWindowAtual();
				if (bandbox.getNomeDoObjeto() != null && bandbox.getNomeDoObjeto().contains("objetoAtual.")) {
					comp = comp.getFellow("manutencao");
				} else if (bandbox.getNomeDoObjeto() != null && bandbox.getNomeDoObjeto().contains("objetoAtualArg")) {
					comp = comp.getFellow("selecao");
				}
				Bandboxbind componentDependidoBand = (Bandboxbind) ProHelperView.recuperaBandboxParaAninhar(comp, componentDependido);
				if (componentDependidoBand != null) {
					ProBaseVO object = (ProBaseVO) componentDependidoBand.getObject();
					if (object != null && object.getId() != null) {
						lista = recuperaListaNoObjetoDependido(bandbox, componentDependidoBand);
					} else {
						lista = montaListaParaFiltroDeBandBox(nomeAtributoSimples, bandbox);
					}
				} else {
					lista = montaListaParaFiltroDeBandBox(nomeAtributoSimples, bandbox);
				}
			} else {
				lista = montaListaParaFiltroDeBandBox(nomeAtributoSimples, bandbox);
			}
		}
		return lista;
	}

	@SuppressWarnings({})
	protected List recuperaListaNoObjetoDependido(Bandboxbind bandboxbind, Bandboxbind componentDependidoBand) {
		List list = new LinkedList();
		try {
			Object object = componentDependidoBand.getObject();
			list.addAll((Collection) Reflexao.recuperaValorDaPropriedade(bandboxbind.getIdentificador(), object));
		} catch (Exception e) {
			log.debug("Erro ao recuperar lista do objeto dependido. Verifique se o nome que recupera a lista no objeto dependido é o mesmo declarado no atributo identificador do componente dependente.");
			getMessagesHelper().emiteMensagemErro("Erro ao recuperar lista do objeto dependido.");
		}
		return list;
	}

	protected boolean filtraEntidade(Object obj) {

		return true;
	}

	protected List montaListaParaFiltroDeBandBox(String nomeAtributo, Bandboxbind bandbox) {
		try {
			return (List) retornaListaDoEscopoDeAplicacao(nomeAtributo, bandbox);
		} catch (InstantiationException e) {
			log.error("Erro ao montar lista do filtro", e);
		} catch (IllegalAccessException e) {
			log.error("Erro ao montar lista do filtro", e);
		} catch (Exception e) {
			log.error("Erro ao montar lista do filtro", e);
		}
		return null;
	}

	public static Window findWindow(Component component) {
		Window window = null;
		if (component.getParent() instanceof Window && !(component.getParent() instanceof InternalWindow)) {
			window = (Window) component.getParent();
			ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
			if (ctr.getTela() == null) {
				ctr.setTela(window);
			}
		} else if (component.getParent() == null) {
			return null;
		} else {
			window = findWindow(component.getParent());
		}

		return window;
	}

	public void adicionaElementosNaSelcao(SelecaoBox selecaoBox) {

		Div container = new Div();
		container.setSclass("paginacao container-fluid");

		Div row1 = new Div();
		row1.setSclass("row");
		row1.setParent(container);

		Div cell1 = new Div();
		cell1.setSclass("col-md-8");
		cell1.setParent(row1);

		Div cell2 = new Div();
		cell2.setSclass("col-md-2");
		cell2.setParent(row1);

		Div cell3 = new Div();
		cell3.setSclass("col-md-2 text-right");
		cell3.setParent(row1);

		Div divRegistrosPorPagina = new Div();
		divRegistrosPorPagina.setId("divRegistrosPorPagina");
		divRegistrosPorPagina.setVisible(false);

		Label labelRegistrosPorPagina = new Label("Registros por página ");

		Intbox numeroDeRegistros = new Intbox();
		numeroDeRegistros.setMaxlength(2);
		numeroDeRegistros.setWidth("30px");
		numeroDeRegistros.setId("numeroDeRegistros");

		EventListener eventoEnterRegistrosPorPagina = new EventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				pesquisar();
			}
		};
		numeroDeRegistros.addEventListener(Events.ON_OK, eventoEnterRegistrosPorPagina);

		Paging paging = new Paging();
		paging.setZclass(".");
		paging.setId("paginador");
		paging.setAutohide(true);

		getTela().setAttribute("paginador_default", paging, false);

		Map annBand = new HashMap();
		annBand.put("value", "classecontrole.paginadorDetalheInfo");
		Label detalhe = new Label("-");
		detalhe.addAnnotation("value", "default", annBand);

		cell1.appendChild(paging);
		cell2.appendChild(detalhe);

		divRegistrosPorPagina.appendChild(labelRegistrosPorPagina);
		divRegistrosPorPagina.appendChild(numeroDeRegistros);
		cell3.appendChild(divRegistrosPorPagina);

		List<Component> children = selecaoBox.getChildren();
		int index = 0;
		boolean temSylistbox = false;
		Sylistbox sylistbox = null;
		for (Component component : children) {
			if (component instanceof Sylistbox) {
				sylistbox = (Sylistbox) component;
				paging.setPageSize(sylistbox.getPageSize());
				temSylistbox = true;
				break;
			}
			index++;
		}

		if (temSylistbox) {
			Listfoot listFoot = new Listfoot();
			Listfooter listFooter = new Listfooter();

			listFooter.appendChild(container);

			listFooter.setParent(listFoot);

			listFooter.setSpan(sylistbox.getListhead().getChildren().size());

			sylistbox.appendChild(listFoot);

			sylistbox.setSpan("true");

		}

		List newChildren = new ArrayList(children);
		if (!temSylistbox) {
			if (newChildren.isEmpty()) {
				newChildren.add(index, container);
			}
		}

		Components.replaceChildren(selecaoBox, newChildren);
		EventListener evn = new EventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				Paging paginal = (Paging) e.getTarget();
				paginal.setId("paginador");
				int activePage = paginal.getActivePage();
				int pageSize = paginal.getPageSize();
				pesquisar(activePage, pageSize);
			}
		};
		paging.addEventListener(org.zkoss.zul.event.ZulEvents.ON_PAGING, evn);

	}

	public void valitadeField(Component comp1, Object value) {
		try {
			if (comp1 instanceof CpfboxBind) {
				if (!"".equals(value) && !"___.___.___/__".equals(value) && !Validacao.CPF(value.toString())) {
					InputElement input = (InputElement) comp1;
					input.setFocus(true);
					throw new WrongValueException(input, "CPF inválido");
				}
			}

			if (comp1 instanceof CnpjboxBind) {
				if (!"".equals(value) && !Validacao.CNPJ(value.toString())) {
					InputElement input = (InputElement) comp1;
					input.setFocus(true);
					throw new WrongValueException(input, " CNPJ inválido");
				}
			}
			validacaoGenericaDeCampo(comp1, value);
			if (!(comp1 instanceof Bandboxbind)) {
				validacaoGenericaDeBeanvalidator(comp1, value);
			}
			inputComErro = null;
		} catch (WrongValueException e) {
			InputElement input = (InputElement) comp1;
			if (inputComErro == null) {
				input.setFocus(true);
				inputComErro = input;
				throw e;
			} else {
				inputComErro.setFocus(true);
				throw e;
			}
		}

	}

	/**
	 * @param comp1
	 * @param value
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected void validacaoGenericaDeBeanvalidator(Component comp1, Object value) {
		InputElement input = (InputElement) comp1;
		FieldValidator fieldValidator = (FieldValidator) input;
		Object objetoPai = fieldValidator.getObjectPai();
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		String[] nodos = fieldValidator.getNomeDoObjeto().split("\\.");
		String propriedadade = nodos[nodos.length - 1];
		if (objetoPai != null) {
			Reflexao.atualizarCampoDoObjeto(propriedadade, objetoPai, value);
			Set<ConstraintViolation<Object>> constraintViolations = validator.validateProperty(objetoPai, propriedadade);
			String erro = "";
			for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
				erro = erro + " " + constraintViolation.getMessage();
			}
			if (!constraintViolations.isEmpty()) {
				input.setFocus(true);
				throw new WrongValueException(input, erro);
			}
		}
	}

	private void validacaoGenericaDeCampo(Component comp1, Object value) {
		InputElement input = (InputElement) comp1;
		FieldValidator fieldValidator = (FieldValidator) input;
		Object objectDonoDaPropriedade = fieldValidator.getObjectPai();
		try {
			if (objectDonoDaPropriedade != null && null != value && !value.equals("null") && !value.equals("")) {
				String expressao = fieldValidator.getNomeDoObjeto();
				if (fieldValidator.getAtributoQueSeraVisualizado() != null && !fieldValidator.getAtributoQueSeraVisualizado().equals("")) {
					expressao = expressao + "." + fieldValidator.getAtributoQueSeraVisualizado();
				}
				if (fieldValidator.getValidarCampo() != null && fieldValidator.getValidarCampo()) {
					String[] nodes = fieldValidator.getNomeDoObjeto().split("\\.");
					String propriedadade = nodes[nodes.length - 1];
					Reflexao.atualizarCampoDoObjeto(propriedadade, objectDonoDaPropriedade, value);
				}
				String[] nodos = fieldValidator.getNomeDoObjeto().split("\\.");
				expressao = expressao.replaceAll(nodos[0] + ".", "");
				String campoNaTela = expressao.replace(".", "$");
				if (fieldValidator.getValidarRegra() != null && fieldValidator.getValidarRegra()) {
					Reflexao.atualizarCampoDoObjeto(expressao, objectDonoDaPropriedade, value);
					String campo = "validar" + campoNaTela.substring(0, 1).toUpperCase() + campoNaTela.substring(1);
					repositorio().validarCampo((ProBaseVO) objectDonoDaPropriedade, campo);
				} else {
					objectDonoDaPropriedade = fieldValidator.getObjectPai();
					String[] nodes = fieldValidator.getNomeDoObjeto().split("\\.");
					String propriedadade = nodes[nodes.length - 1];
					Method method = Reflexao.recuperaMetodoGetDoObjeto(propriedadade, objectDonoDaPropriedade);
					boolean annotationPresent = method.isAnnotationPresent(Regra.class);
					if (annotationPresent) {
						Regra regra = method.getAnnotation(Regra.class);
						Reflexao.atualizarCampoDoObjeto(propriedadade, objectDonoDaPropriedade, value);
						repositorio().validarCampo((ProBaseVO) objectDonoDaPropriedade, regra.codigo());
					}
				}

			}
		} catch (ViolacaoDeRegraEx e) {
			input.setFocus(true);
			throw new WrongValueException(input, e.getMessage());
		} catch (InvocationTargetException e) {
			log.error("Erro ao validar campo", e);
			input.setFocus(true);
			throw new WrongValueException(input, e.getTargetException().getMessage());
		} catch (NoSuchMethodException e) {
			log.error("Erro ao validar campo", e);
			NoSuchMethodException noSuchMethodException = e;
			input.setFocus(true);
			throw new WrongValueException(input, noSuchMethodException.getMessage());
		} catch (Exception e) {
			log.error("Erro ao validar campo");
			input.setFocus(true);
			if (e.getCause() instanceof ViolacaoDeRegraEx) {
				throw new WrongValueException(input, e.getCause().getMessage());
			}
			throw new WrongValueException(input, e.getMessage());
		}
	}

	public void setDualboxRegister(Set<Listboxdual> dualboxRegister) {
		this.dualboxRegister = dualboxRegister;
	}

	public Set getDualboxRegister() {
		return this.dualboxRegister;
	}

	@Override
	public boolean doCatch(Throwable ex) throws Exception {
		return super.doCatch(ex);
	}

	public void anexar(org.zkoss.zul.Image image, ImageUpload imageUpload) {
		Object media = abrePopUpSelecaoDeAquivo();
		if (media instanceof org.zkoss.image.Image) {
			try {
				image.setContent((org.zkoss.image.Image) media);

				Object objetoAtual = imageUpload.getObjectPai();
				if (objetoAtual == null) {
					objetoAtual = instanciaNovoObjeto();
				}
				Method atributoQueSeraAtualizado = Reflexao.recuperaMetodoGetDoObjeto("foto1", objetoAtual);
				Object argumento = image.getContent().getByteData();
				getHelperView().atualizarCampoDoObjeto(atributoQueSeraAtualizado, objetoAtual, getWindowAtual(), argumento);
			} catch (Exception e) {
				getMessagesHelper().emiteMensagemErro(e.getMessage());
			}
		} else {
			Messagebox.show("Not an image: " + media, "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}

	protected void inicializaNovoDetalhe(Object object) {

	}

	protected void abrePopup(Map map, String pagina) {
		Window win = null;
		try {
			win = (Window) Executions.createComponents(pagina, getWindowAtual(), map);
		} catch (Exception e1) {
			log.error("Erro ao criar popup", e1);
		}
		if (win instanceof Window) {
			try {
				win.setClosable(false);
				win.setPosition("center");
				win.doModal();
			} catch (SuspendNotAllowedException e) {
				log.error("Erro ao criar popup", e);
			}
		}
	}

	public Object aposInicializacaoDeBind(Object bean, Object beanChild, String beanid) {
		return null;
	}

	public void onBlurBandBox(Event e) {

	}

	public void onFocusCtl(Event e) {

	}

	public void atualizaTela(Component com) {
		Component parent = com.getParent();
		getBinder().loadComponent(parent.getChildren().get(1));
	}

	public Menupopup criaMenuParaListDiretoryBind(Treecell treecell2) {
		Menupopup menupopup = new Menupopup();
		Menuitem menuitem1 = new Menuitem("menu 1");
		Menuitem menuitem2 = new Menuitem("menu 2");
		Menuitem menuitem3 = new Menuitem("menu 3");
		menupopup.appendChild(menuitem1);
		menupopup.appendChild(menuitem2);
		menupopup.appendChild(menuitem3);
		menupopup.setParent(treecell2);
		return menupopup;
	}

	public void montaExemploParaPesquisaDeArquivos(ArquivoVO example) {
		example.setCaminhoAbsoluto(example.getCaminhoAbsoluto());
		example.setUsuarioVO(getUsuarioVO());
		example.setUnidadeAdministrativaVO(getUnidadeAdministrativaVO());
	}

	public void limpar() {
		Listbox listbox = (Listbox) getWindowAtual().getFellow("listaSelecao");
		Paging paginal = (Paging) getWindowAtual().getFellow("paginador");
		paginal.setTotalSize(0);
		SelecaoBox selecaoBox = (SelecaoBox) getWindowAtual().getFellow("selecao");

		BindingListModelSet listSelBind = new BindingListModelSet(new HashSet(), true);
		listbox.setModel(listSelBind);
		setObjetoAtualArg(newObject());
		setObjetoAtualArg2(newObject());
		limpaComponente(selecaoBox);
		configuraValoresPadroesParaObjetoArg(getObjetoAtualArg(), getObjetoAtualArg2());
		getHelperView().controlarVisibilidadeBotaoExportar(getTela(), false);
		getBinder().loadAll();
	}

	private void limpaComponente(Component component) {
		List<Component> children = component.getChildren();
		for (Component component2 : children) {
			if (component2 instanceof Textbox) {
				Textbox textbox = (Textbox) component2;
				textbox.setValue(null);
			} else if (component2 instanceof Datebox) {
				Datebox datebox = (Datebox) component2;
				datebox.setText(null);
			} else {
				limpaComponente(component2);
			}
		}
	}

	/**
	 * Mapa de argumentos compartilhado entre telas.
	 * 
	 * @return mapa contendo argumentos.
	 */
	public Map getArg() {
		return this.arg;
	}

	public void exportar() throws SuspendNotAllowedException, InterruptedException {
		Popup popupExportar = (Popup) getWindowAtual().getFellowIfAny("popupexport");
		if (popupExportar == null) {
			getHelperView().gerarPopupBotaoExportar(getTela());
			popupExportar = (Popup) getWindowAtual().getFellowIfAny("popupexport");
		}
		Button botaoExportar = (Button) getWindowAtual().getFellowIfAny("exportar");
		popupExportar.open(botaoExportar, "after_end");
	}

	private RecordDataSet<ProBaseVO> recuperarTodosRegistrosPesquisa() {
		RecordDataSet<ProBaseVO> setAux = null;

		ENTITY pArg = getObjetoAtualArg();
		ENTITY pArg2 = instanciaNovoObjeto();
		if (pArg == null) {
			pArg = instanciaNovoObjeto();
		}
		try {
			antesPesquisar(pArg);
			antesPesquisar(pArg, pArg2);
			setAux = (RecordDataSet<ProBaseVO>) recuperaLista(pArg, pArg2, 0, Integer.MAX_VALUE);
			aposPesquisar(setAux);
		} catch (Exception e) {
			e.printStackTrace();
			getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
		return setAux;
	}

	/**
	 * Metodo responsavel por exibir um relatorio.
	 * <p>
	 * É necessário informar os seguintes parametros no map:
	 * <p>
	 * <code>String source</code> - source do relatorio (arquivo *.jasper , ex: "/relatorio/tela_tarefas.jasper")<br>
	 * <code>Map parameters</code> - parametros a serem enviados para o relatório<br>
	 * <code>Collection listaObjetos</code> - collection contendo os objetos do relatório<br>
	 * 
	 * @param parametros
	 *            objetos a serem repassados ao jasper para renderização do relatório
	 * @param resgistrosDaPesquisa
	 *            exibir no relatório os registros da tela de pesquisa do prodigio
	 */
	public void exibirRelatorio(Map parametros, boolean registrosDaPesquisa) {
		if (registrosDaPesquisa && parametros.containsKey("source") && parametros.containsKey("parameters")) {
			parametros.put("listaDeObjetos", recuperarTodosRegistrosPesquisa());
			((Map) parametros.get("parameters")).put("totalDeRegistros", String.valueOf(((Collection) parametros.get("listaDeObjetos")).size()));
			abrePopup(parametros, "br/gov/prodemge/visao/nucleo/Relatorio.zul");
		} else if (parametros.containsKey("source") && parametros.containsKey("parameters") && parametros.containsKey("listaObjetos")) {
			abrePopup(parametros, "br/gov/prodemge/visao/nucleo/Relatorio.zul");
		} else {
			log.error("Parametros obrigatórios nao informado.");
			throw new IllegalArgumentException("Parametros obrigatórios nao informado. Consulte javadoc do método.");
		}
	}

	public void exportarArquivo(boolean pagAtual, String tipoRelatorio) {
		Paging paginal = (Paging) getWindowAtual().getFellow("paginador");
		Integer quantidadeRegistros = paginal.getTotalSize();

		if (quantidadeRegistros != 0) {
			RecordDataSet<ProBaseVO> setAux = null;
			ENTITY pArg = getObjetoAtualArg();
			ENTITY pArg2 = getObjetoAtualArg2();
			if (pArg == null) {
				pArg = instanciaNovoObjeto();
			}

			try {
				if (!pagAtual) {
					antesPesquisar(pArg);
					antesPesquisar(pArg, pArg2);
					setAux = (RecordDataSet<ProBaseVO>) recuperaLista(pArg, pArg2, 0, quantidadeRegistros);
					aposPesquisar(setAux);
				} else {
					setAux = (RecordDataSet<ProBaseVO>) listaDeObjetos;
				}

				gerarRelatorio(setAux, tipoRelatorio);

			} catch (Exception e) {
				e.printStackTrace();
				getMessagesHelper().emiteMensagemErro(e.getMessage());
			}
		} else {
			getMessagesHelper().emiteMensagemAtencao("Não há registros a serem exportados.");
		}
	}

	protected void gerarRelatorio(RecordDataSet<ProBaseVO> setAux, String tipoRelatorio) {
		try {
			String[] cabecalhos = montarCabecalhoParaExportar(tipoRelatorio);
			String[] atributos = montarCamposParaExportar(tipoRelatorio);

			if ("PDF".equalsIgnoreCase(tipoRelatorio)) {
				ITabelaBuilder builderPDF = instanciarRelatorioPDF(cabecalhos.length);
				byte[] html = montarRelatorioParaExportar(setAux, cabecalhos, atributos, builderPDF);
				ButtonRelatorioPDFBind relatorio = configurarObjetoRelatorioPDF(html);
				byte[] relatorioPDF = getHelperView().converteHTMLParaPDF(relatorio, getWindowAtual());
				download(relatorioPDF, "application/pdf", "relatorio.pdf");

			} else if ("EXCEL".equalsIgnoreCase(tipoRelatorio)) {
				ITabelaBuilder builderExcel = instanciarRelatorioExcel(cabecalhos);
				byte[] relatorio = montarRelatorioParaExportar(setAux, cabecalhos, atributos, builderExcel);
				download(relatorio, "application/vnd.ms-excel", "relatorio.xls");
			} else {
				throw new RuntimeException("Tipo do relatório não suportado");
			}
		} catch (Exception e) {
			log.error("ERRO: ", e);
			throw new RuntimeException("Erro ao gerar relatório");
		}

	}

	protected ButtonRelatorioPDFBind configurarObjetoRelatorioPDF(byte[] html) {
		Radiogroup radiogroupOrientacao = (Radiogroup) getWindowAtual().getFellow("ProOrientacaoRelatorio");
		ButtonRelatorioPDFBind relatorio = new ButtonRelatorioPDFBind();
		relatorio.setOrientacao(radiogroupOrientacao.getSelectedItem().getValue());
		relatorio.setConteudoCorpo(html);
		return relatorio;
	}

	protected TabelaExcelBuilder instanciarRelatorioExcel(String[] cabecalhos) {
		return new TabelaExcelBuilder(cabecalhos.length);
	}

	protected byte[] montarRelatorioParaExportar(RecordDataSet<ProBaseVO> setAux, String[] cabecalhos, String[] atributos, ITabelaBuilder builder) {
		builder.abrirLinha();
		for (int i = 0; i < cabecalhos.length; i++) {
			builder.addColunaDestaque(cabecalhos[i]);
		}
		builder.fecharLinha();
		for (ProBaseVO objeto : setAux) {
			builder.abrirLinha();
			for (int j = 0; j < atributos.length; j++) {
				Object aux;
				String nomeAtributo = atributos[j];
				nomeAtributo = nomeAtributo.replace(" asc", "").replace(" desc", "").trim();
				try {
					aux = Reflexao.recuperaValorDaPropriedade(nomeAtributo, objeto);
					builder.addColuna(aux);
				} catch (Exception e) {
					builder.addColuna("");
					log.error("Erro ao adicionar coluna:" + e.getMessage());
				}
			}
			builder.fecharLinha();
		}
		return builder.construir();
	}

	protected TabelaHtmlBuilder instanciarRelatorioPDF(int qtdeColunas) {
		return new TabelaHtmlBuilder(qtdeColunas);
	}

	protected String[] montarCabecalhoParaExportar(String tipoRelatorio) throws Exception {
		String[] cabecalhos = getHelperView().retornaHeadersLabelFromSylist(getTela()).split(",");
		return cabecalhos;
	}

	protected String[] montarCamposParaExportar(String tipoRelatorio) throws Exception {
		Listhead listhead = (Listhead) getWindowAtual().getFellow("cabecalho");
		List filhos = listhead.getChildren();
		String campos = "";
		String orderBy = "";
		for (Object object : filhos) {
			if (object instanceof Listheader) {
				Listheader listheader = (Listheader) object;
				if (listheader.isVisible()) {
					Object value = listheader.getValue();
					if (value != null) {
						if (!listheader.getSortDirection().equals("natural")) {
							if (listheader.getSortDirection().equals("ascending")) {
								orderBy = orderBy + "," + value + " asc";
							} else if (listheader.getSortDirection().equals("descending")) {
								orderBy = orderBy + "," + value + " desc";
							}
							continue;
						}
						campos = campos + "," + value;
					}
				}
			}
		}
		String camposParaProjecaoEOrdenacao = orderBy + campos;
		if (camposParaProjecaoEOrdenacao.length() > 1) {
			camposParaProjecaoEOrdenacao = camposParaProjecaoEOrdenacao.substring(1);
		}

		return camposParaProjecaoEOrdenacao.split(",");
	}

	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}

	public String getCampoDeOrdenacao() {
		return campoDeOrdenacao;
	}

	public void setCampoDeOrdenacao(String campoDeOrdenacao) {
		this.campoDeOrdenacao = campoDeOrdenacao;
	}

	public void imprimePadrao(ArquivoVO arquivoVO) throws Exception, InterruptedException {
		imprimePadrao(arquivoVO.getBinario());
	}

	public void exibirRelatorio() {

	}

	protected void recuperaObjetoAposSalvar() throws Exception {
		if (objetoAtual.getId() != null) {
			objetoAtual = fachadaDeNegocio.recuperaObjeto(objetoAtual);
		}
		if (objetoAtual == null) {
			throw new Exception("Não foi possível recuperar objeto após gravação.");
		}
	}

	public void exibirOcultarModal(Component window) throws Exception {
		if (!window.isVisible()) {
			((InternalWindow) window).doModal();
		} else {
			window.setVisible(false);
		}
	}

	public String getPaginadorDetalheInfo() {
		Paging paging = (Paging) getTela().getAttribute("paginador_default");
		int total = paging.getTotalSize();
		if (total > paging.getPageIncrement()) {
			int registroInicial = paging.getActivePage() * paging.getPageSize() + 1;
			int ate = registroInicial + paging.getPageSize() - 1;
			if (ate > total)
				ate = total;
			return "[" + registroInicial + " - " + (ate) + " / " + total + " ]";
		} else {
			return "";
		}
	}

	public void abrecasoDeUso(String src, boolean historico, String modo, String width) {
		HashMap attributes = new HashMap();
		attributes.put("telaOrigem", getTela());
		getHelperView().abreCasoDeUsoSecundario(src, getTela(), attributes, historico, modo, width);
	}

	public void voltar() {
		Window tela = getTela();
		getHelperView().voltar(tela);
	}

	public boolean criarBotaoNovoParaDetalhe(DetBox detalhe) {
		return true;
	}

	public boolean criarBotaoExcluirParaDetalhe(DetBox detalhe) {
		return true;
	}

	protected void recarregarComponente(String idComponente) {
		Component componente = getTela().getFellowIfAny(idComponente);
		if (componente != null) {
			getBinder().loadComponent(componente);
		}
	}

	public <T> T getComponentById(String id) {
		Component fellow = getWindowAtual().getFellowIfAny(id);
		if (fellow != null) {
			Class<T> classe = (Class<T>) fellow.getClass();
			return classe.cast(fellow);
		}
		return null;
	}
}
