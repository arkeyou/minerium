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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.ext.Disable;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import br.gov.prodemge.ssc.enumerations.Operacao;
import br.gov.prodemge.ssc.interfaces.IPapel;
import br.gov.prodemge.ssc.interfaces.IPermissao;
import br.gov.prodemge.ssc.interfaces.IPermissaoEspecie;
import br.gov.prodemge.ssc.interfaces.IRecursoOperacao;
import br.gov.prodemge.ssc.interfaces.IUnidade;
import br.gov.prodemge.ssc.interfaces.IUnidadeEspecie;
import br.gov.prodemge.ssc.interfaces.IUsuario;
import br.gov.prodemge.ssc.interfaces.IUsuarioUnidade;
import br.gov.prodemge.ssc.interfaces.IUsuarioUnidadePapel;
import br.gov.prodemge.ssc.interfaces.base.IUsuarioBase;
import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.anotacoes.Lookup;
import br.gov.prodigio.comuns.anotacoes.enumeracao.NotView;
import br.gov.prodigio.comuns.constantes.Constantes;
import br.gov.prodigio.comuns.utils.Reflexao;
import br.gov.prodigio.comuns.utils.imagem.ImageUtils;
import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.controle.ProTabularCtr;
import br.gov.prodigio.controle.componente.Bandboxbind;
import br.gov.prodigio.controle.componente.ButtonBox;
import br.gov.prodigio.controle.componente.ButtonRelatorioPDFBind;
import br.gov.prodigio.controle.componente.ButtonSearch;
import br.gov.prodigio.controle.componente.FieldValidator;
import br.gov.prodigio.controle.componente.ProAnnotateDataBinder;
import br.gov.prodigio.controle.componente.ProItemComboRender;
import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.entidades.ProVO;
import br.gov.prodigio.utils.ItextImagemBase64Util;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

@SuppressWarnings("rawtypes")
public class ProHelperView implements Serializable {
	private static final int PROFUNDIDADE = 3;

	private static final Logger log = LoggerFactory.getLogger(ProHelperView.class);

	protected ProHelperView() {
	}

	private static ProHelperView helperInstance;
	private static Map<Class, ProHelperView> helperInstanceMap = new HashMap<Class, ProHelperView>();

	public static ProHelperView getInstance() {
		if (helperInstance == null) {
			return create();
		} else {
			return helperInstance;
		}
	}

	private synchronized static ProHelperView create() {
		if (helperInstance == null) {
			helperInstance = new ProHelperView();
		}
		return helperInstance;
	}

	public static ProHelperView getInstance(Class controle) {
		ProHelperView helperEspecializado = helperInstanceMap.get(controle);
		if (helperEspecializado == null) {
			return create(controle);
		} else {
			return helperEspecializado;
		}
	}

	private synchronized static ProHelperView create(Class controle) {
		if (controle == Object.class) {
			return null;
		}
		ProHelperView helperViewEspecializado = helperInstanceMap.get(controle);
		if (helperViewEspecializado == null) {
			String nomeClasse = controle.getName();
			String nomeClasseHelperView = nomeClasse.replace("Ctr", "ViewHelper");
			try {
				helperViewEspecializado = (ProHelperView) Class.forName(nomeClasseHelperView).newInstance();
			} catch (Exception e) {
				try {
					nomeClasseHelperView = nomeClasse.replace(".controle.", ".visao.helper.").replace("Ctr", "HelperView");
					log.debug("Instanciando classe HelperView {} para a classe de controle {} ", nomeClasseHelperView, nomeClasse);
					try {
						helperViewEspecializado = (ProHelperView) Class.forName(nomeClasseHelperView).newInstance();
					} catch (Exception e1) {
						nomeClasseHelperView = nomeClasse.replace(".controle.", ".helper.").replace("Ctr", "HelperView");// Novo padrão
						helperViewEspecializado = (ProHelperView) Class.forName(nomeClasseHelperView).newInstance();
					}
				} catch (Exception e2) {
					controle = controle.getSuperclass();
					log.debug("Não foi possível criar a classe {}. Tentando HelperView para a classe pai {}", nomeClasseHelperView, controle);
					helperViewEspecializado = create(controle);
				}
			}
			helperInstanceMap.put(controle, helperViewEspecializado);
		}
		return helperViewEspecializado;
	}

	public void ajustaLayoutParaSelecao(Window janela) {
		Component sele = janela.getFellow("selecao");
		Component listaSelecao = janela.getFellow("listaSelecao");
		((Listbox) listaSelecao).setSelectedItem(null);
		Component manu = janela.getFellow("manutencao");
		sele.setVisible(true);
		manu.setVisible(false);
		ajustaBotoesParaTelaSelecao(janela);
	}

	public void ajustaBotoesParaTelaSelecao(Window main) {

		Arrays.asList("aprovar", "concluir", "excluir", "salvar", "editar", "clonar", "novo", "abrir", "imprimir").forEach(id -> trataVisibilidadeComponente(main, id, false));

		Arrays.asList("pesquisar", "limpar").forEach(id -> trataVisibilidadeComponente(main, id, true));

		Div barradebotoes = (Div) main.getFellowIfAny(Constantes.BOTOES.DIV_BOTOES);
		final ProCtr controller = getController(main);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.PESQUISAR);
		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.PESQUISAR, "limpar", "Limpar");
		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.NOVO);
	}

	/**
	 * @param windowatual
	 */
	public void ajustaBotoesParaTelaManutenca(Window windowatual) {
		ProCtr controller = getController(windowatual);
		Div barradebotoes = (Div) windowatual.getFellowIfAny(Constantes.BOTOES.DIV_BOTOES);
		ProBaseVO objetoAtual = controller.getObjetoAtual();
		IUsuarioBase usuarioVO = controller.getUsuarioVO();
		String recurso = recuperaCasoDeUsoAtual(controller);

		// boolean segurancaHabilitada = (usuarioVO instanceof IUsuario);

		detachButton(windowatual, "pesquisar");
		detachButton(windowatual, "limpar");

		if (cadastroConcluido(objetoAtual))
			aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.APROVAR);
		else {
			final String idBotao = Operacao.APROVAR.getDescricao().toLowerCase();
			detachButton(windowatual, idBotao);
		}

		if (!cadastroNovo(objetoAtual) && !cadastroConcluido(objetoAtual) && perfilPossuiFuncao(usuarioVO, recurso, Operacao.CONCLUIR))
			aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.CONCLUIR);
		else {
			final String idBotao = Operacao.CONCLUIR.getDescricao().toLowerCase();
			detachButton(windowatual, idBotao);
		}

		if (!cadastroNovo(objetoAtual) && cadastroEmEdicao(objetoAtual) && perfilPossuiFuncao(usuarioVO, recurso, Operacao.CLONAR))
			aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.CLONAR);
		else {
			final String idBotao = Operacao.CLONAR.getDescricao().toLowerCase();
			detachButton(windowatual, idBotao);
		}

		if (cadastroEmEdicao(objetoAtual) && !cadastroNovo(objetoAtual) && perfilPossuiFuncao(usuarioVO, recurso, Operacao.EXCLUIR))
			aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.EXCLUIR);
		else {
			final String idBotao = Operacao.EXCLUIR.getDescricao().toLowerCase();
			detachButton(windowatual, idBotao);
		}

		if (!cadastroConcluido(objetoAtual) && !cadastroAprovado(objetoAtual) && cadastroEmEdicao(objetoAtual) && perfilPossuiFuncao(usuarioVO, recurso, Operacao.SALVAR))
			aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.SALVAR);
		else {
			final String idBotao = Operacao.SALVAR.getDescricao().toLowerCase();
			detachButton(windowatual, idBotao);
		}

		if ((cadastroConcluido(objetoAtual) || cadastroAprovado(objetoAtual)) && perfilPossuiFuncao(usuarioVO, recurso, Operacao.EDITAR))
			aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.EDITAR);
		else {
			final String idBotao = Operacao.EDITAR.getDescricao().toLowerCase();
			detachButton(windowatual, idBotao);
		}

		if (perfilPossuiFuncao(usuarioVO, recurso, Operacao.NOVO))
			aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.NOVO);

		else {
			final String idBotao = Operacao.NOVO.getDescricao().toLowerCase();
			detachButton(windowatual, idBotao);
		}
		if (cadastroEmEdicao(objetoAtual) && perfilPossuiFuncao(usuarioVO, recurso, Operacao.ABRIR))
			aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.ABRIR);

		else {
			final String idBotao = Operacao.ABRIR.getDescricao().toLowerCase();
			detachButton(windowatual, idBotao);
		}
		if (!cadastroNovo(objetoAtual) && cadastroEmEdicao(objetoAtual) && perfilPossuiFuncao(usuarioVO, recurso, Operacao.IMPRIMIR))
			aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.IMPRIMIR);
		else {
			final String idBotao = Operacao.IMPRIMIR.getDescricao().toLowerCase();
			detachButton(windowatual, idBotao);
		}

		// TODO [DESIDERIO] Melhorar a lógica nos trechos abaixo.
		Collection<Component> componentes = windowatual.getFellows();// Só componentes com id
		final boolean abilita = false;
		final boolean desabilita = !abilita;
		for (Component abstractComponent : componentes) {
			if (cadastroAprovado(objetoAtual) || cadastroConcluido(objetoAtual)) {
				desabilitaHabilitaComponente(abstractComponent, false, desabilita);
			} else {
				if (perfilPossuiFuncao(usuarioVO, recurso, Operacao.SALVAR) || perfilPossuiFuncao(usuarioVO, recurso, Operacao.INCLUIR) || perfilPossuiFuncao(usuarioVO, recurso, Operacao.EDITAR)
						|| perfilPossuiFuncao(usuarioVO, recurso, Operacao.EXCLUIR)) {
					desabilitaHabilitaComponente(abstractComponent, false, abilita);
				} else {
					desabilitaHabilitaComponente(abstractComponent, false, desabilita);
				}
			}

		}

		Component anexar = windowatual.getFellowIfAny("anexar");
		if (anexar != null) {
			if (cadastroConcluido(objetoAtual) || cadastroAprovado(objetoAtual)) {
				anexar.detach();
			} else {
				anexar.setVisible(true);
			}
		}

		if (cadastroConcluido(objetoAtual) || cadastroAprovado(objetoAtual)) {
			Component detalhe = windowatual.getFellowIfAny("novoDetalhe");
			int index = 1;
			while (detalhe != null) {
				if (detalhe != null) {
					detalhe.setVisible(false);
					index++;
					detalhe = windowatual.getFellowIfAny("novoDetalhe" + index);
				}
			}
		}

		if (cadastroEmEdicao(objetoAtual)) {
			Component detalhe = windowatual.getFellowIfAny("novoDetalhe");
			int index = 1;
			while (detalhe != null) {
				if (detalhe != null) {
					detalhe.setVisible(true);
					index++;
					detalhe = windowatual.getFellowIfAny("novoDetalhe" + index);
				}
			}
		}

		if (cadastroConcluido(objetoAtual) || cadastroAprovado(objetoAtual)) {
			Component detalhe = windowatual.getFellowIfAny("excluiDetalhe");
			int index = 1;
			while (detalhe != null) {
				if (detalhe != null) {
					detalhe.setVisible(false);
					index++;
					detalhe = windowatual.getFellowIfAny("excluiDetalhe" + index);
				}
			}
		}

		if (cadastroEmEdicao(objetoAtual)) {
			Component detalhe = windowatual.getFellowIfAny("excluiDetalhe");
			int index = 1;
			while (detalhe != null) {
				if (detalhe != null) {
					detalhe.setVisible(true);
					index++;
					detalhe = windowatual.getFellowIfAny("excluiDetalhe" + index);
				}
			}
		}
	}

	private void detachButton(Window windowatual, final String idBotao) {
		final Component fellowIfAny = windowatual.getFellowIfAny(idBotao);
		if (fellowIfAny != null) {
			fellowIfAny.setVisible(false);
		}
	}

	private String recuperaCasoDeUsoAtual(ProCtr controller) {
		String recurso = null;
		if (controller.getArg() != null) {
			recurso = (String) controller.getArg().get("casodeusoatual");
		}
		if (recurso == null) {
			recurso = (String) controller.getTela().getAttribute("casodeusoatual", Component.SESSION_SCOPE);
		}
		return recurso;
	}

	/**
	 * @param objetoAtual
	 * @return
	 */
	private boolean cadastroNovo(ProBaseVO objetoAtual) {
		return objetoAtual.getId() == null;
	}

	public static void desabilitaHabilitaComponente(Component abstractComponent, boolean incluiFilhos, boolean desabilita) {
		final boolean componenteDeArgumento = abstractComponent.getId() == null || (abstractComponent.getId() != null && abstractComponent.getId().indexOf("Arg") == -1);
		if (abstractComponent instanceof Disable) {
			Disable disableComponent = (Disable) abstractComponent;
			// Se tiver setado disabled = true, prevalecer o comportamento da tela
			if (componenteDeArgumento && !disableComponent.isDisabled()) {
				disableComponent.setDisabled(desabilita);
			}
		} else if (abstractComponent instanceof ButtonSearch) {
			// Se tiver setado visible = false, prevalecer o comportamento da tela
			if (componenteDeArgumento && abstractComponent.isVisible()) {
				((ButtonSearch) abstractComponent).setVisible(!desabilita);
			}
		}
		List<Component> abstractComponents = abstractComponent.getChildren();
		if (abstractComponent.getId() != null && abstractComponent.getId().equals("manutencao")) {
			incluiFilhos = true;
		}
		if (incluiFilhos) {
			for (Component abstractComponent2 : abstractComponents) {
				desabilitaHabilitaComponente(abstractComponent2, incluiFilhos, desabilita);
			}
		}
	}

	public static ProCtr getController(Window windowatual) {
		ProCtr controller = (ProCtr) windowatual.getAttribute("_$composer$_", false);
		return controller;
	}

	public static boolean cadastroConcluido(ProBaseVO objetoAtual) {
		if (objetoAtual instanceof ProVO) {
			if (((ProVO) objetoAtual).getDsSituacao() == null) {
				return false;
			}
			return (((ProVO) objetoAtual).getDsSituacao().equals(ProVO.SITUACAO_DO_REGISTRO.CONCLUIDO));
		}
		return false;
	}

	public static boolean cadastroEmEdicao(ProBaseVO objetoAtual) {
		if (objetoAtual instanceof ProVO) {
			if (((ProVO) objetoAtual).getDsSituacao() == null) {
				return false;
			}
			return (((ProVO) objetoAtual).getDsSituacao().equals(ProVO.SITUACAO_DO_REGISTRO.EM_EDICAO));
		}
		return false;
	}

	public static boolean cadastroAprovado(ProBaseVO objetoAtual) {
		if (objetoAtual instanceof ProVO) {
			if (((ProVO) objetoAtual).getDsSituacao() == null) {
				return false;
			}
			return (((ProVO) objetoAtual).getDsSituacao().equals(ProVO.SITUACAO_DO_REGISTRO.APROVADO));
		}
		return false;
	}

	public static boolean perfilPossuiFuncao(IUsuarioBase usuarioBase, String url, Operacao operacao) {
		if (usuarioBase instanceof IUsuario) {
			IUsuario usuario = (IUsuario) usuarioBase;
			boolean possuiFuncao = false;
			Set<IUsuarioUnidade> acessosUsuarioUnidade = usuario.getListaUsuarioUnidade();
			if (acessosUsuarioUnidade == null)
				return false;
			forMaior: for (IUsuarioUnidade acesso : acessosUsuarioUnidade) {
				Set<IUsuarioUnidadePapel> usuarioUnidadePapel = acesso.getUsuarioUnidadePapel();
				for (IUsuarioUnidadePapel uup : usuarioUnidadePapel) {

					IPapel papel = uup.getUnidadePapel().getPapel();
					IPapel papelPai = null;

					Set<IPermissao> listaPermissoes = new HashSet<IPermissao>();
					if (papel != null) {
						listaPermissoes = papel.getListaPermissoes();
						papelPai = uup.getUnidadePapel().getPapel().getPapelPai();
					}

					Set<IPermissao> listaPermissoesPai = new HashSet<IPermissao>();
					if (papelPai != null) {
						listaPermissoesPai = papelPai.getListaPermissoes();
					}

					Set<IPermissao> permissoes = new HashSet<IPermissao>();

					if (listaPermissoes != null && !listaPermissoes.isEmpty()) {
						permissoes.addAll(listaPermissoes);
					}

					if (listaPermissoesPai != null && !listaPermissoesPai.isEmpty()) {
						permissoes.addAll(listaPermissoesPai);
					}

					if (permissoes.isEmpty())
						continue;
					for (IPermissao permissao : permissoes) {
						IRecursoOperacao recursoOperacao = permissao.getRecursoOperacao();
						List<IPermissaoEspecie> listaPermissaoEspecie = recursoOperacao.getListaPermissaoEspecie();
						String urlSemParametros = url.split("\\?")[0];
						String urlRecurso = recursoOperacao.getRecurso().getUrl();
						String urlRecursoParametros = urlRecurso.split("\\?")[0];
						if (urlRecursoParametros.equals(urlSemParametros) && recursoOperacao.getOperacao().equals(operacao)) {
							// Caso exista Especie deve verificar se a especie é da Unidade do Usuario
							if (listaPermissaoEspecie != null && !listaPermissaoEspecie.isEmpty()) {
								for (IPermissaoEspecie iPermissaoEspecie : listaPermissaoEspecie) {
									Set<IUnidadeEspecie> listaEspecieUnidade = iPermissaoEspecie.getEspecie().getUnidadeEspecie();

									if (listaEspecieUnidade != null && !listaEspecieUnidade.isEmpty()) {
										for (IUnidadeEspecie especieUnidade : listaEspecieUnidade) {
											IUnidade unidade = especieUnidade.getUnidade();
											if (acesso.getUnidade().getId().longValue() == unidade.getId().longValue()) {
												possuiFuncao = true;
												break forMaior;
											}
										}
									}
								}

							} else {
								// Se não possui especie já está autorizado
								possuiFuncao = true;
								break forMaior;
							}

						}
					}
				}
			}

			return possuiFuncao;
		} else {
			if (operacao.equals(Operacao.APROVAR) || operacao.equals(Operacao.CONCLUIR))
				return false;
			return true;
		}
	}

	public static boolean perfilPossuiFuncaoPorNome(IUsuarioBase usuarioBase, Operacao operacao, String nomeRecurso) {
		if (usuarioBase instanceof IUsuario) {
			boolean possuiFuncao = false;
			IUsuario usuario = (IUsuario) usuarioBase;
			if (usuario != null) {
				Set<IUsuarioUnidade> acessosUsuarioUnidade = usuario.getListaUsuarioUnidade();
				forMaior: for (IUsuarioUnidade acesso : acessosUsuarioUnidade) {
					Set<IUsuarioUnidadePapel> usuarioUnidadePapel = acesso.getUsuarioUnidadePapel();
					for (IUsuarioUnidadePapel uup : usuarioUnidadePapel) {
						IPapel papel = uup.getUnidadePapel().getPapel();
						IPapel papelPai = null;

						Set<IPermissao> listaPermissoes = new HashSet<IPermissao>();
						if (papel != null) {
							listaPermissoes = papel.getListaPermissoes();
							papelPai = uup.getUnidadePapel().getPapel().getPapelPai();
						}

						Set<IPermissao> listaPermissoesPai = new HashSet<IPermissao>();
						if (papelPai != null) {
							listaPermissoesPai = papelPai.getListaPermissoes();
						}

						Set<IPermissao> permissoes = new HashSet<IPermissao>();

						if (listaPermissoes != null && !listaPermissoes.isEmpty()) {
							permissoes.addAll(listaPermissoes);
						}

						if (listaPermissoesPai != null && !listaPermissoesPai.isEmpty()) {
							permissoes.addAll(listaPermissoesPai);
						}
						if (permissoes.isEmpty())
							continue;
						for (IPermissao permissao : permissoes) {
							IRecursoOperacao recursoOperacao = permissao.getRecursoOperacao();
							List<IPermissaoEspecie> listaPermissaoEspecie = recursoOperacao.getListaPermissaoEspecie();
							if (recursoOperacao.getRecurso().getNome().equals(nomeRecurso) && recursoOperacao.getOperacao().equals(operacao)) {
								// Caso exista Especie deve verificar se a especie é da Unidade do Usuario
								if (listaPermissaoEspecie != null && !listaPermissaoEspecie.isEmpty()) {
									for (IPermissaoEspecie iPermissaoEspecie : listaPermissaoEspecie) {
										Set<IUnidadeEspecie> listaEspecieUnidade = iPermissaoEspecie.getEspecie().getUnidadeEspecie();
										for (IUnidadeEspecie especieUnidade : listaEspecieUnidade) {
											IUnidade unidade = especieUnidade.getUnidade();
											if (acesso.getUnidade().getId().longValue() == unidade.getId().longValue()) {
												possuiFuncao = true;
												break forMaior;
											}
										}
									}
								} else {
									// Se não possui especie já está autorizado
									possuiFuncao = true;
									break forMaior;
								}
							}

						}
					}
				}
			}
			return possuiFuncao;
		} else {
			return true;
		}

	}

	public void ajustaLayoutParaManutencao(Window janela) {
		Component list = janela.getFellow("selecao");
		Component manu = janela.getFellow("manutencao");
		preparaTelaDeManutencao(janela);
		list.setVisible(false);
		manu.setVisible(true);
		ajustaBotoesParaTelaManutenca(janela);
	}

	protected void preparaTelaDeManutencao(Window janela) {

	}

	protected void montaBarraDeBotoes(Window tela) {

		Component barradebotoes = tela.getFellowIfAny("barradebotoes");

		if (barradebotoes != null) {

			if (barradebotoes instanceof Div) {
				Div div = (Div) barradebotoes;

				montaBotoes(div, tela);

			} else if (barradebotoes instanceof Toolbar) {
				Div div = new Div();
				div.setId("barradebotoes");

				montaBotoes(div, tela);

				div.setParent(barradebotoes);
				barradebotoes.appendChild(div);

			} else if (barradebotoes instanceof Grid) {

				Columns columns = new Columns();
				Column column = new Column();

				column.setWidth("100%");
				column.setHeight("100%");
				column.setAlign("right");

				Div div = new Div();

				div.setStyle("vertical-align: middle;");
				div.setWidth("100%");
				div.setId("barradebotoes");

				montaBotoes(div, tela);

				div.setParent(column);

				column.appendChild(div);
				column.setParent(columns);

				columns.appendChild(column);
				columns.setParent(barradebotoes);

				barradebotoes.appendChild(columns);

				((Grid) barradebotoes).renderAll();

			}
			try {
				Button button = (Button) tela.getFellowIfAny("novo");
				if (button != null)
					button.setSclass("btn btn-default btn-novo");

				button = (Button) tela.getFellowIfAny("limpar");
				if (button != null)
					button.setSclass("btn btn-default btn btn-limpar");

				button = (Button) tela.getFellowIfAny("excluir");
				if (button != null)
					button.setSclass("btn btn-default btn-excluir");

				button = (Button) tela.getFellowIfAny("salvar");
				if (button != null)
					button.setSclass("btn btn-default btn-salvar");

				button = (Button) tela.getFellowIfAny("concluir");
				if (button != null)
					button.setSclass("btn btn-default btn-concluir");

				button = (Button) tela.getFellowIfAny("pesquisar");
				if (button != null)
					button.setSclass("btn btn-default btn-pesquisar");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	protected void montaBotoes(Div barradebotoes, Window tela) {

		ProCtr controller = getController(tela);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.PESQUISAR);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.PESQUISAR, "limpar", "Limpar");

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.NOVO);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.APROVAR);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.CONCLUIR);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.CLONAR);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.EXCLUIR);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.SALVAR);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.EDITAR);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.ABRIR);

		aplicaRegraDeSegurancaParaBotao(barradebotoes, controller, Operacao.IMPRIMIR);

	}

	protected void aplicaRegraDeSegurancaParaBotao(Div div, final ProCtr controller, final Operacao operacao) {
		final String idDoBotao = operacao.getDescricao().toLowerCase();
		aplicaRegraDeSegurancaParaBotao(div, controller, operacao, idDoBotao, operacao.getDescricao());
	}

	protected void aplicaRegraDeSegurancaParaBotao(Div div, final ProCtr controller, final Operacao operacao, final String idDoBotao, final String descricao) {

		if (div == null)
			return;
		final String largura = "";
		final String altura = "";
		IUsuarioBase usuarioVO = controller.getUsuarioVO();
		String recurso = recuperaCasoDeUsoAtual(controller);
		if (perfilPossuiFuncao(usuarioVO, recurso, operacao)) {
			final SerializableEventListener<Event> eventListenerOnClick = new SerializableEventListener<Event>() {
				@Override
				public void onEvent(Event e) throws Exception {
					controller.getClass().getMethod(idDoBotao).invoke(controller);
				}
			};
			@SuppressWarnings("unchecked")
			Button botao = criaBotao(controller, idDoBotao, true, descricao, largura, altura, eventListenerOnClick);
			botao.setParent(div);
			botao.setSclass("btn btn-default btn-" + idDoBotao);
			div.appendChild(botao);
		} else {
			Component comp = controller.getTela().getFellowIfAny(idDoBotao);
			if (comp != null) {
				comp.setVisible(false);
			}
		}
	}

	protected Button criaBotao(final ProCtr controller, final String id, final boolean visibilidade, final String label, final String largura, final String altura, SerializableEventListener<? extends Event> eventListenerOnClick) {
		Button botao = null;
		final Component comp = controller.getTela().getFellowIfAny(id);
		if (comp != null) {
			botao = (Button) comp;
			botao.setVisible(visibilidade);
		} else {
			botao = new Button();
			botao.setId(id);
			botao.setVisible(visibilidade);
			botao.setLabel(label);
			botao.setWidth(largura);
			botao.setHeight(altura);
			botao.addEventListener("onClick", eventListenerOnClick);
			botao.setAutodisable("self");
		}
		return botao;
	}

	protected boolean temLogicaDeManutencao(Window tela) {
		Component manutencao = tela.getFellowIfAny("manutencao");
		if (manutencao != null) {
			return true;
		}
		return false;
	}

	public void configuraTelaDeEntrada(Component comp) throws Exception {
		montaBarraDeBotoes((Window) comp);
		ajustaBotoesParaTelaSelecao((Window) comp);
		inicializaLookUps(comp);
	}

	public void montaComponenteDeTelaList(Window janela, List<String> atributosEnuns, Object objetoAtual) {
		for (String nomeCampo : atributosEnuns) {
			montaComponenteList(janela, nomeCampo, objetoAtual);
		}
		for (String nomeCampo : atributosEnuns) {
			montaComponenteList(janela, nomeCampo + "Arg", objetoAtual);
		}
	}

	@SuppressWarnings("unchecked")
	public void montaComponenteList(Window tela, String campo, Object objetoAtual) {
		try {
			Component component = tela.getFellowIfAny(campo);
			if (component instanceof Listbox) {
				Listbox lb = (Listbox) component;
				BindingListModelList b;
				if (lb != null) {
					List listEnum = recuperaColecaoParaMontarTela(tela, campo, objetoAtual);
					if (listEnum != null) {
						b = new BindingListModelList(listEnum, true);
						if (lb != null) {
							lb.setModel(b);
						}
					}
				}
			}
			if (component instanceof Combobox) {
				Combobox cb = (Combobox) component;
				if (cb != null) {
					List listEnum = recuperaColecaoParaMontarTela(tela, campo, objetoAtual);
					montaComboDeObjetos(cb, listEnum);
				}
			}

		} catch (Exception e) {
			log.error("Erro montar componente ", e);
		}
	}

	public void montaComboDeObjetos(Combobox cb, List listEnum) {
		ListModel dictModel = new BindingListModelList(listEnum, true);
		cb.setItemRenderer(new ProItemComboRender());
		cb.setModel(dictModel);
	}

	public List recuperaColecaoParaMontarTela(Window tela, String nomeMetodo, Object objetoAtual) {
		Method method = null;
		List filtrada = null;
		try {
			if (nomeMetodo.endsWith("Arg")) {
				nomeMetodo = nomeMetodo.replace("Arg", "");
			}
			String string = "get" + nomeMetodo.substring(0, 1).toUpperCase() + nomeMetodo.substring(1);
			method = objetoAtual.getClass().getMethod(string, new Class[] {});
			String nomeLista = method.getReturnType().getSimpleName().substring(0, 1).toLowerCase() + method.getReturnType().getSimpleName().substring(1);
			Collection aux = (Collection) tela.getAttribute(ContextParameters.PREFIX_ENUM + nomeLista.substring(0, 1).toUpperCase() + nomeLista.substring(1), Component.APPLICATION_SCOPE);
			tela.setAttribute(ContextParameters.PREFIX_ENUM + nomeLista.substring(0, 1).toUpperCase() + nomeLista.substring(1), aux, false);
			List listEnum = new ArrayList();
			listEnum.addAll(aux);
			filtrada = filtrarLookups(listEnum, tela, nomeMetodo, objetoAtual);
		} catch (SecurityException e) {
			log.error("Erro ao recuperar coleção para montagem da tela");
		} catch (Exception e) {
			log.error("Erro ao recuperar coleção para montagem da tela");
		}
		return filtrada;
	}

	protected List filtrarLookups(List listEnum, Component comp, String nomeMetodo, Object objetoAtual) {
		return listEnum;
	}

	public Object recuperaValorTela(Method metodo, Object objetoAtual, Window janela) {
		Object argumento;
		if (((ProBaseVO) objetoAtual).getId() != null && ((ProBaseVO) objetoAtual).getId() < 0) {// Todo objeto com id negativo será utilizado apenas como argumento para QBE
			argumento = recuperaValorDaTela(Reflexao.recuperaNomeDoAtributo(metodo) + "Arg", janela);
		} else {
			argumento = recuperaValorDaTela(Reflexao.recuperaNomeDoAtributo(metodo), janela);
		}
		return argumento;
	}

	public Object recuperaValorDaTela(String idDoCampo, Window janela) throws WrongValueException {
		Component campo = janela.getFellowIfAny(idDoCampo);
		return recuperaValorDaTela(campo, janela);
	}

	public Object recuperaValorDaTela(Component campo, Window janela) throws WrongValueException {
		if (campo == null) {
			return null;
		}
		Object valor = "";
		if (campo instanceof Datebox) {
			valor = ((Datebox) campo).getValue();
		} else if (campo instanceof Intbox) {
			valor = ((Intbox) campo).getValue();
		} else if (campo instanceof Doublebox) {
			valor = ((Doublebox) campo).getValue();
		} else if (campo instanceof org.zkoss.zul.Image) {
			org.zkoss.zul.Image campoImagem = (org.zkoss.zul.Image) campo;
			org.zkoss.image.Image img = campoImagem.getContent();
			valor = img.getByteData();
		} else if (campo instanceof Listbox) {
			if (((Listbox) campo).getModel() != null) {
				int index = ((Listbox) campo).getSelectedIndex();
				if (index < 1) {
					index = 0;
				}
				valor = ((Listbox) campo).getModel().getElementAt(index);
			} else {
				valor = ((Listbox) campo).getItemAtIndex(((Listbox) campo).getSelectedIndex()).getValue();
			}
		} else if (campo instanceof Combobox) {
			Comboitem comboitem = ((Combobox) campo).getSelectedItem();
			if (comboitem != null) {
				valor = comboitem.getValue();
			}
		} else if (campo instanceof Textbox) {
			valor = ((Textbox) campo).getValue();
		}

		return valor;
	}

	public void transfereDaTelaParaVO(Object objeto, Window janela) {
		Method metodos[] = objeto.getClass().getMethods();
		for (Method metodo : metodos) {
			if (Reflexao.eMetodoGetterBeanSemSet(metodo)) {
				Object argumento = null;
				argumento = recuperaValorTela(metodo, objeto, janela);
				atualizarCampoDoObjeto(metodo, objeto, janela, argumento);
			}
		}
	}

	public void atualizarCampoDoObjeto(Method metodo, Object objeto, Window janela, Object argumento) {
		try {
			Reflexao.atualizarCampoDoObjeto(metodo, objeto, argumento);
		} catch (IllegalArgumentException e) {
			Component campo = null;
			if (objeto != null && ((ProBaseVO) objeto).getId() == -1) {
				campo = janela.getFellow(metodo.getName().substring(3, 4).toLowerCase() + metodo.getName().substring(4) + "Arg");
			} else {
				campo = janela.getFellow(metodo.getName().substring(3, 4).toLowerCase() + metodo.getName().substring(4));
			}
			throw new WrongValueException(campo, "Não é possivel preencher o campo " + metodo.getName() + " do tipo " + metodo.getReturnType().getName() + " da classe " + objeto.getClass().getSimpleName() + " com argumentos do tipo: "
					+ argumento.getClass().getSimpleName());
		} catch (IllegalAccessException e) {
			log.error("Erro ao atualizar campo do objeto", e);
		} catch (InvocationTargetException e) {
			log.error("Erro ao atualizar campo do objeto", e);
		} catch (SecurityException e) {
			log.error("Erro ao atualizar campo do objeto", e);
		} catch (NoSuchMethodException e) {
			Component campo = janela.getFellow(metodo.getName());
			throw new WrongValueException(campo, "Não é possivel preencher o campo " + metodo.getName() + " do tipo " + metodo.getReturnType().getName() + " da classe " + objeto.getClass().getSimpleName() + " com argumentos do tipo: "
					+ argumento.getClass().getSimpleName());
		}
	}

	public void inicializaLookUps(Component comp) throws Exception {
		ProCtr controller = getController((Window) comp);
		ProBaseVO objetoAtual = controller.getObjetoAtual();
		List<String> listaEnum = new ArrayList<String>();
		recuperaNomeDeCamposEnusDoObjeto(objetoAtual, listaEnum, PROFUNDIDADE);
		if (listaEnum != null && !listaEnum.isEmpty()) {
			montaComponenteDeTelaList((Window) comp, listaEnum, objetoAtual);
		}

		List<String> listaLookUps = new ArrayList<String>();
		recuperaNomeDeCamposLokuUpsDoObjeto(objetoAtual, listaLookUps, PROFUNDIDADE);
		if (listaLookUps != null) {
			montaComponenteDeTelaList((Window) comp, listaLookUps, objetoAtual);
		}
	}

	protected List<String> recuperaNomeDeCamposEnusDoObjeto(Object objetoAtual, List<String> lista, int profundidade) throws Exception {
		profundidade--;
		if (objetoAtual != null) {
			Method methods[] = objetoAtual.getClass().getMethods();
			for (Method method : methods) {
				if (Set.class.isAssignableFrom(method.getReturnType())) {// recupera enuns dos detalhes

					Class c = Reflexao.recuperaTipoDeParametroGenericosEmRetornoDeMetodos(method);
					try {
						if (Modifier.isAbstract(c.getModifiers())) {
							log.warn("Não foi possível recuperar os campos Lookup para a classe abstrata: {}", c.toString());
						} else {
							if (profundidade > 0) {
								recuperaNomeDeCamposEnusDoObjeto(c.newInstance(), lista, profundidade);
							} else {
								return lista;
							}
						}
					} catch (InstantiationException e) {
						log.error("Erro ao carregar nome de campo do objeto ", e);
					} catch (IllegalAccessException e) {
						log.error("Erro ao carregar nome de campo do objeto ", e);
					}
				} else if (method.getReturnType().isEnum()) {
					lista.add(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
				}
			}
		}
		return lista;
	}

	protected List<String> recuperaNomeDeCamposLokuUpsDoObjeto(Object objetoAtual, List<String> listaDeLookUps, int profundidade) throws Exception {
		profundidade--;
		if (objetoAtual != null) {
			Method methods[] = objetoAtual.getClass().getMethods();
			for (Method method : methods) {
				if (Set.class.isAssignableFrom(method.getReturnType())) {// recupera lookups dos detalhes
					Class c = Reflexao.recuperaTipoDeParametroGenericosEmRetornoDeMetodos(method);
					try {
						if (Modifier.isAbstract(c.getModifiers())) {
							log.warn("Não foi possível recuperar os campos Lookup para a classe abstrata: {}", c.toString());
						} else {
							if (profundidade > 0) {
								recuperaNomeDeCamposLokuUpsDoObjeto(c.newInstance(), listaDeLookUps, profundidade);
							} else {
								return listaDeLookUps;
							}
						}
					} catch (InstantiationException e) {
						log.error("Erro ao recuperar nome de campos lookup do objeto ");
					} catch (IllegalAccessException e) {
						log.error("Erro ao recuperar nome de campos lookup do objeto ");
					}
				} else if (method.getReturnType().getAnnotation(Lookup.class) != null) {
					if (method.getAnnotation(Lookup.class) == null) {
						if (method.getName().substring(3).equals(method.getName().substring(3).toUpperCase())) {
							// o campo que estiver fora do padrão camel-case, usa o nome sem transformação
							listaDeLookUps.add(method.getName().substring(3)); // Ex.: CPF
						} else {
							listaDeLookUps.add(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
						}
					} else if (!method.getAnnotation(Lookup.class).desprezar()) {
						listaDeLookUps.add(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
					}
				}
			}
		}
		return listaDeLookUps;
	}

	public Object recuperaValorDoCampoDoObjetoAtual(Method getter, Object objetoAtual, Window janela) {
		Object objetoRetornado = null;
		try {
			objetoRetornado = Reflexao.recuperaValorDeMetodoGet(getter, objetoAtual);
		} catch (IllegalArgumentException e) {
			janela.getFellow(getter.getName());
		} catch (IllegalAccessException e) {
			log.error("Erro ao recuperar valor do objeto", e);
		} catch (InvocationTargetException e) {
			log.error("Erro ao recuperar valor do objeto", e);
		} catch (SecurityException e) {
			log.error("Erro ao recuperar valor do objeto", e);
		}
		return objetoRetornado;
	}

	public AImage recuperaImagemDoObjetoAtual(String idCampoImagem, Object objetoAtual, Window janela) throws Exception {
		Method metodo = Reflexao.recuperaMetodoGetDoObjeto(idCampoImagem, objetoAtual);
		Object objeto = recuperaValorDoCampoDoObjetoAtual(metodo, objetoAtual, janela);
		byte[] blob = ((byte[]) objeto);
		AImage img = null;
		if (blob != null) {
			img = new AImage("p.jpg", blob);
		}
		return img;
	}

	public void atualizaImagemNaTela(Object media, String idCampoImagem, Window janela) throws IOException {
		try {
			org.zkoss.zul.Image imageTela = (org.zkoss.zul.Image) janela.getFellowIfAny(idCampoImagem);
			if (imageTela != null) {
				imageTela.setContent((Image) media);
			}

		} catch (ComponentNotFoundException e) {
			log.error("Erro ao atualizar imagem", e);
		}
	}

	public void atualizaImagensDoVoParaTela(Object objetoAtual, Window janela) {
		if (objetoAtual != null) {
			Method metodos[] = objetoAtual.getClass().getMethods();
			for (Method metodo : metodos) {
				if (metodo.getReturnType().equals(byte[].class) && !metodo.isAnnotationPresent(NotView.class)) {
					try {
						String nomeAtributo = Reflexao.recuperaNomeDoAtributo(metodo);
						Object img = recuperaImagemDoObjetoAtual(nomeAtributo, objetoAtual, janela);
						atualizaImagemNaTela(img, nomeAtributo, janela);
					} catch (Exception e) {
						log.error("Erro ao atualizar imagem na entidade", e);
					}
				}
			}
		}
	}

	public void atualizaLookUp(ProBaseVO appVO, Window window) throws Exception {
		Class c = appVO.getClass();
		Lookup lookup = appVO.getClass().getAnnotation(Lookup.class);
		if (lookup != null) {
			try {
				if (lookup != null) {
					String namedQuery = lookup.namedQuery();
					if (namedQuery.equals("")) {
						namedQuery = "listaDe" + c.getSimpleName();
					}
					Set set = (Set) window.getAttribute(namedQuery, Component.APPLICATION_SCOPE);
					if (set != null) {
						boolean incluiu = set.add(appVO);
						if (!incluiu) {// Implementação necessaria para atualizar objetos, pois o método add
										// não atualiza se o objeto já esta incluido
							set.remove(appVO);
							set.add(appVO);
						}
					} else {
						throw new Exception("A lookup " + namedQuery + " não foi encontrado.");
					}
					window.setAttribute(namedQuery, set);
					log.info("Atualizando lookup ...:{} tamanho: {}", namedQuery, set.size());
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public void realizaBind(Object objeto, Window janela) {
		Method metodos[] = objeto.getClass().getMethods();
		for (Method metodo : metodos) {
			if (Reflexao.eMetodoGetterBeanSemSet(metodo)) {
				Object argumento = null;
				argumento = recuperaValorTela(metodo, objeto, janela);
				atualizarCampoDoObjeto(metodo, objeto, janela, argumento);
			}
		}
	}

	public static Component recuperaComponent(Component com, String nomeDoObjeto) {
		List children = com.getChildren();
		Component comp = null;
		for (Object object : children) {
			if (object instanceof FieldValidator) {
				String nomeDoObjeto2 = ((FieldValidator) object).getIdentificador();
				if (nomeDoObjeto2 != null && nomeDoObjeto2.equals(nomeDoObjeto)) {
					comp = (Component) object;
					break;
				}
				nomeDoObjeto2 = ((FieldValidator) object).getNomeDoObjeto();
				if (nomeDoObjeto2 != null && nomeDoObjeto2.equals(nomeDoObjeto)) {
					comp = (Component) object;
					break;
				}
				nomeDoObjeto2 = ((Component) object).getId();
				if (nomeDoObjeto2 != null && nomeDoObjeto2.equals(nomeDoObjeto)) {
					comp = (Component) object;
					break;
				}
			} else {
				String id = ((Component) object).getId();
				if (object instanceof Component && id != null && id.equals(nomeDoObjeto)) {
					comp = (Component) object;
					break;
				}
				comp = recuperaComponent((Component) object, nomeDoObjeto);
				if (comp != null) {
					break;
				}
			}
		}
		return comp;
	}

	public static Component recuperaParentContexto(Component component) {
		Component parent = component.getParent();
		while (parent != null && !parent.getId().equals("manutencao") && !(parent instanceof Listitem) && !parent.getId().equals("selecao")) {
			parent = parent.getParent();
		}
		return parent;
	}

	public static Component recuperaBandboxParaAninhar(Component com, String nomeDoObjeto) {
		List children = com.getChildren();
		Component band = null;
		for (Object object : children) {
			if (object instanceof Bandboxbind) {
				String nomeDoObjeto2 = ((Bandboxbind) object).getIdentificador();
				if ("".equals(nomeDoObjeto2) || nomeDoObjeto2 == null) {
					nomeDoObjeto2 = ((Bandboxbind) object).getNomeDoObjeto();
				}
				if (nomeDoObjeto2.equals(nomeDoObjeto)) {
					band = (Bandboxbind) object;
					break;
				}
			} else {
				band = recuperaBandboxParaAninhar((Component) object, nomeDoObjeto);
				if (band != null) {
					break;
				}
			}
		}
		return band;
	}

	public static void aninharComponenteAoComponentePai(FieldValidator field) {
		String aninhado = field.getDependeDoComponente();
		if (aninhado != null && !"".equals(aninhado)) {
			Component parent = recuperaParentContexto((Component) field);
			FieldValidator component = (FieldValidator) ProHelperView.recuperaBandboxParaAninhar(parent, aninhado);
			if (component != null) {
				((Component) field).setAttribute(aninhado, component, Component.COMPONENT_SCOPE);
				String nomeDoObjeto = field.getIdentificador();
				if ("".equals(nomeDoObjeto) || nomeDoObjeto == null) {
					nomeDoObjeto = field.getNomeDoObjeto();
				}
				component.addComponentesDependentes(nomeDoObjeto, (Component) field);
			}
		}
	}

	public static void preencheListaDeComponentes(Component com, Class tipo, List componentes) {
		List children = com.getChildren();
		for (Object object : children) {
			if (object.getClass().equals(tipo)) {
				componentes.add(object);
			} else {
				preencheListaDeComponentes((Component) object, tipo, componentes);
			}
		}
	}

	public static boolean existeAlgumCampoPreenchido(Component com) {
		List children = com.getChildren();
		boolean existeCampoPreenchido = false;
		for (Object object : children) {
			if (object instanceof InputElement) {
				final InputElement inputElement = (InputElement) object;
				final boolean campoPreenchido = !inputElement.getText().equals("");
				final boolean somenteLeitura = inputElement.isReadonly();
				if (campoPreenchido && !somenteLeitura) {
					return true;
				}
			} else {
				existeCampoPreenchido = existeAlgumCampoPreenchido((Component) object);
				if (existeCampoPreenchido) {
					return true;
				}
			}
		}
		return existeCampoPreenchido;
	}

	public void controlarExibicaoBotoes(Component component, boolean exibir, String... idBotoes) {
		for (String idBotao : idBotoes) {
			Component botao = component.getFellowIfAny(idBotao);
			if (botao != null) {
				botao.setVisible(exibir);
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void gerarPopupBotaoExportar(Window windowatual) {
		final ProCtr controller = getController(windowatual);

		Popup popupExportar = (Popup) windowatual.getFellowIfAny("popupexport");

		if (popupExportar == null) {
			popupExportar = new Popup();
			popupExportar.setWidth("240px");
			popupExportar.setId("popupexport");
			Grid grid = new Grid();
			Rows rows = new Rows();
			grid.appendChild(rows);

			Row rowLabel = new Row();
			rowLabel.appendChild(new Label("Selecione o formato:"));
			rows.appendChild(rowLabel);

			Radio radioXls = new Radio();
			radioXls.setChecked(true);
			radioXls.setValue("xls");
			radioXls.setLabel("Excel (.xls)");

			Radio radioPdf = new Radio();
			radioPdf.setValue("pdf");
			radioPdf.setLabel("PDF");

			final Radiogroup radiogroupTipoRelatorio = new Radiogroup();
			radiogroupTipoRelatorio.setOrient("vertical");
			radiogroupTipoRelatorio.appendChild(radioXls);
			radiogroupTipoRelatorio.appendChild(radioPdf);

			Row rowRadioTipoRelatorio = new Row();
			rowRadioTipoRelatorio.appendChild(radiogroupTipoRelatorio);
			rows.appendChild(rowRadioTipoRelatorio);

			Radio radioRetrato = new Radio();
			radioRetrato.setChecked(true);
			radioRetrato.setValue("retrato");
			radioRetrato.setLabel("Retrato");

			Radio radioPaisagem = new Radio();
			radioPaisagem.setValue("paisagem");
			radioPaisagem.setLabel("Paisagem");

			final Radiogroup radiogroupOrientacao = new Radiogroup();
			radiogroupOrientacao.setId("ProOrientacaoRelatorio");
			radiogroupOrientacao.setOrient("horizontal");
			radiogroupOrientacao.appendChild(radioRetrato);
			radiogroupOrientacao.appendChild(radioPaisagem);

			Row rowOrientacao = new Row();
			rowOrientacao.setVisible(false);
			rowOrientacao.appendChild(radiogroupOrientacao);
			rows.appendChild(rowOrientacao);

			radioPdf.addEventListener("onClick", new SerializableEventListener() {
				@Override
				public void onEvent(Event e) throws Exception {
					rowOrientacao.setVisible(true);
				}
			});
			radioXls.addEventListener("onClick", new SerializableEventListener() {
				@Override
				public void onEvent(Event e) throws Exception {
					rowOrientacao.setVisible(false);
				}
			});

			Row rowPagAtual = new Row();
			final Checkbox exportarPaginaAtual = new Checkbox();
			exportarPaginaAtual.setLabel("Exportar somente página atual?");
			rowPagAtual.appendChild(exportarPaginaAtual);
			rows.appendChild(rowPagAtual);
			Row rowButton = new Row();
			rowButton.setAlign("right");
			Button botaoExportar = new Button();
			botaoExportar.setLabel("Exportar");
			botaoExportar.setSclass("");
			botaoExportar.addEventListener("onClick", new SerializableEventListener() {
				@Override
				public void onEvent(Event e) throws Exception {
					boolean somentePagAtual = exportarPaginaAtual.isChecked();

					Radio selectedItem = radiogroupTipoRelatorio.getSelectedItem();
					String label = selectedItem.getValue();
					if (label.equals("xls")) {
						controller.exportarArquivo(somentePagAtual, "EXCEL");
					} else if (label.equals("pdf")) {
						controller.exportarArquivo(somentePagAtual, "PDF");
					}
				}
			});
			rowButton.appendChild(botaoExportar);
			rows.appendChild(rowButton);
			popupExportar.appendChild(grid);
			windowatual.appendChild(popupExportar);
		}
	}

	public String retornaHeadersLabelFromSylist(Window tela) throws Exception {
		Listhead listhead = (Listhead) tela.getFellow("cabecalho");
		List filhos = listhead.getChildren();
		String campos = "";
		String orderBy = "";
		for (Object object : filhos) {
			if (object instanceof Listheader) {
				Listheader listheader = (Listheader) object;
				if (listheader.isVisible()) {
					if (listheader.getValue() != null) {
						Object value = listheader.getLabel();
						if (value != null) {
							if (!listheader.getSortDirection().equals("natural")) {
								orderBy = orderBy + "," + value;
								continue;
							}
							campos = campos + "," + value;
						}
					}
				}
			}
		}
		String camposParaProjecaoEOrdenacao = orderBy + campos;
		if (camposParaProjecaoEOrdenacao.length() > 1) {
			return camposParaProjecaoEOrdenacao.substring(1);
		}
		return camposParaProjecaoEOrdenacao;
	}

	public void controlarVisibilidadeBotaoExportar(Window windowatual, boolean visibilidade) {
		final Component exportar = windowatual.getFellowIfAny("exportar");
		if (exportar != null)
			exportar.setVisible(visibilidade);
	}

	public static InputStream recuperaNoClassPathRecursoDetronDoJar(String diretorioRaiz, String nomeDoJar, String pathDoArquivo) throws IOException {
		final String PROTOCOLO = "jar:file:";
		InputStream inputStreamAux;
		URL url = null;
		Enumeration<URL> urls = ProHelperView.class.getClassLoader().getResources(diretorioRaiz);
		String pathCompleto = "";
		while (urls.hasMoreElements()) {
			url = urls.nextElement();
			log.info(pathCompleto);
			pathCompleto = url.getPath().substring(0, url.getPath().length() - 1);
			if (pathCompleto.contains(nomeDoJar)) {
				final String string = PROTOCOLO + pathCompleto + "!" + pathDoArquivo;
				URL urlArquivo = new URL(string);
				inputStreamAux = urlArquivo.openStream();
				return inputStreamAux;
			}
		}
		return null;
	}

	public static void insereNovoConteudoNoCentroDaJanela(String urlcasodeuso, AbstractComponent tela, Map attributes) {
		Component meio = tela.getFellowIfAny("meio");
		while (meio == null) {
			meio = tela.getParent();
			if (meio != null && meio instanceof Window) {
				break;
			}
		}
		// redireciona o meio da pagina
		Component component = null;
		component = meio.getFirstChild();
		if (component != null) {
			component.setVisible(false);
		}
		meio.getChildren().clear();

		String[] partesDaUrl = urlcasodeuso.split("\\?");
		String urlSemParametros = partesDaUrl[0];
		attributes.put("casodeusoatual", urlcasodeuso);
		if (partesDaUrl.length > 1) {
			String[] parametrosValores = partesDaUrl[1].split("&");
			for (String parametroValor : parametrosValores) {
				if (parametroValor.contains("=")) {
					String[] split = parametroValor.split("=");
					attributes.put(split[0], split[1]);
				}
			}
		}
		if (urlSemParametros.contains("prodigio"))
			component = Executions.createComponents("~." + urlSemParametros, meio, attributes);
		else
			component = Executions.createComponents(urlSemParametros, meio, attributes);
		meio.appendChild(component);
	}

	public static boolean perfilPossuiFuncaoPorCodigo(IUsuario usuario, Operacao operacao, String codigoRecurso) {
		boolean possuiFuncao = false;

		if (usuario != null) {
			Set<IUsuarioUnidade> acessosUsuarioUnidade = usuario.getListaUsuarioUnidade();
			forMaior: for (IUsuarioUnidade acesso : acessosUsuarioUnidade) {
				Set<IUsuarioUnidadePapel> usuarioUnidadePapel = acesso.getUsuarioUnidadePapel();
				if (usuarioUnidadePapel == null)
					continue;
				for (IUsuarioUnidadePapel uup : usuarioUnidadePapel) {

					IPapel papel = uup.getUnidadePapel().getPapel();
					IPapel papelPai = null;

					Set<IPermissao> listaPermissoes = new HashSet<IPermissao>();
					if (papel != null) {
						listaPermissoes = papel.getListaPermissoes();
						papelPai = uup.getUnidadePapel().getPapel().getPapelPai();
					}

					Set<IPermissao> listaPermissoesPai = new HashSet<IPermissao>();
					if (papelPai != null) {
						listaPermissoesPai = papelPai.getListaPermissoes();
					}

					Set<IPermissao> permissoes = new HashSet<IPermissao>();

					if (listaPermissoes != null && !listaPermissoes.isEmpty()) {
						permissoes.addAll(listaPermissoes);
					}

					if (listaPermissoesPai != null && !listaPermissoesPai.isEmpty()) {
						permissoes.addAll(listaPermissoesPai);
					}

					if (permissoes.isEmpty())
						continue;
					for (IPermissao permissao : permissoes) {
						IRecursoOperacao recursoOperacao = permissao.getRecursoOperacao();
						List<IPermissaoEspecie> listaPermissaoEspecie = recursoOperacao.getListaPermissaoEspecie();
						if (recursoOperacao.getRecurso().getCodigoRecurso().equals(codigoRecurso) && recursoOperacao.getOperacao().equals(operacao)) {
							// Caso exista Especie deve verificar se a
							// especie é da Unidade do Usuario
							if (listaPermissaoEspecie != null && !listaPermissaoEspecie.isEmpty()) {
								for (IPermissaoEspecie iPermissaoEspecie : listaPermissaoEspecie) {
									Set<IUnidadeEspecie> listaEspecieUnidade = iPermissaoEspecie.getEspecie().getUnidadeEspecie();
									if (listaEspecieUnidade != null) {
										for (IUnidadeEspecie especieUnidade : listaEspecieUnidade) {
											IUnidade unidade = especieUnidade.getUnidade();
											if (acesso.getUnidade().getId().longValue() == unidade.getId().longValue()) {
												possuiFuncao = true;
												break forMaior;
											}
										}
									}
								}
							} else {
								// Se não possui especie jé está autorizado
								possuiFuncao = true;
								break forMaior;
							}
						}

					}
				}
			}
		}

		return possuiFuncao;
	}

	public void inicializarAtualizacaoComponentes(Window window, Component... componentes) {
		Desktop desktop = window.getDesktop();
		if (!desktop.isServerPushEnabled())
			desktop.enableServerPush(true);

		_mutex = new Object();
		running = true;

		WorkingThread workingThread = new WorkingThread(componentes, desktop, window);
		workingThread.start();
	}

	public void atualizarComponentes() {
		try {
			Executions.wait(_mutex);
		} catch (Exception e) {
			log.error("Erro ao atualizar componentes.", e);
		}
	}

	public void finalizarAtualizacaoComponentes() {
		running = false;
	}

	protected boolean running;
	protected Object _mutex;

	protected class WorkingThread extends Thread {
		private Component[] componentes;
		private Desktop desktop;
		private ProAnnotateDataBinder binder;

		public WorkingThread(Component[] componentes, Desktop desktop, Window window) {
			this.componentes = componentes;
			this.desktop = desktop;
			this.binder = new ProAnnotateDataBinder(window);
		}

		@Override
		public void run() {
			try {

				while (running) {
					try {
						Executions.activate(desktop);
						for (Component componente : componentes) {
							binder.loadComponent(componente);
							Executions.notify(_mutex);
							Executions.wait(_mutex);
						}
					} catch (Exception e) {
						Executions.deactivate(desktop);
					}
				}
			} catch (Exception e) {
				log.error("Ocorreu um erro ao processar o metodo conclusão em lote.", e);
			} finally {
				desktop.enableServerPush(false);

			}
		}
	}

	public Object redimencionaImagem(Object media, Media mediaZK, int type, int width, int height) throws IOException {
		byte[] foto = mediaZK.getByteData();
		java.awt.Image imagem = ImageIO.read(new ByteArrayInputStream(foto));
		BufferedImage resizeImage = ImageUtils.resizeImage(imagem, 0, 100, 100);
		ByteArrayOutputStream imagemAtualizada = new ByteArrayOutputStream();
		if (ImageIO.write(resizeImage, "png", imagemAtualizada)) {
			foto = imagemAtualizada.toByteArray();
			media = new AImage("foto", foto);
		}
		return media;
	}

	protected void trataVisibilidadeComponente(Window main, String id, boolean visualizar) {
		Component componente = main.getFellowIfAny(id);
		if (componente != null) {
			componente.setVisible(visualizar);
		}
	}

	@SuppressWarnings("unchecked")
	public void voltar(Window tela) {
		LinkedHashMap<String, Window> windowHistory = (LinkedHashMap<String, Window>) tela.getPage().getAttribute("history");
		if (windowHistory != null) {
			Set<Entry<String, Window>> entrySet = windowHistory.entrySet();
			Entry<String, Window> last = null;
			// recupera a última janela do histórico
			for (Entry<String, Window> entry : entrySet) {
				last = entry;
			}
			if (last != null && last != tela) {
				tela.setVisible(false);
				// garante que todas as janelas do histórico fiquem invisíveis
				for (Window window : windowHistory.values()) {
					window.setVisible(false);
				}
				// remove a ultima janela do histórico
				windowHistory.remove(last.getKey());
				// Torna a ultima janela do histórico (agora a atual) visível.
				last.getValue().setVisible(true);
				// nao permite que o zk faca uso de cache de tela.
				tela.detach();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void abreCasoDeUsoSecundario(String urlcasodeuso, Window telaOrigem, Map attributes, boolean hitorico, String modo, String width) {
		Component meio = recuperaComponenteMeioDaTela(telaOrigem);

		configuraAtributosPassadosParaCasoDeUsoSecundario(urlcasodeuso, attributes);

		String[] partesDaUrl = urlcasodeuso.split("\\?");
		String urlSemParametros = partesDaUrl[0];
		Window window = null;

		if (getHistoryMap(telaOrigem).containsKey(urlSemParametros)) {
			window = getHistoryMap(telaOrigem).get(urlSemParametros);
		} else {
			Window windowAux = (Window) Executions.createComponents(urlSemParametros, null, attributes);
			String id = windowAux.getId();
			window = (Window) meio.getFellowIfAny(id);
			if (window == null) {
				window = windowAux;
				meio.appendChild(windowAux);
			} else {
				windowAux.detach();// descarta a janela criada
			}
		}

		if (hitorico) {
			adcionaTelaNoHistorico(telaOrigem);
		}
		window.setPosition("center,center");
		if ("highlighted".equalsIgnoreCase(modo)) {
			window.doHighlighted();
		} else if ("modal".equalsIgnoreCase(modo)) {
			window.doModal();
		} else if ("overlapped".equalsIgnoreCase(modo)) {
			window.doOverlapped();
		} else if ("popup".equalsIgnoreCase(modo)) {
			window.doPopup();
		} else if ("embedded".equalsIgnoreCase(modo)) {
			window.doEmbedded();
			telaOrigem.setVisible(false);
		} else {
			window.doEmbedded();
			telaOrigem.setVisible(false);
		}
		window.setWidth(width);
	}

	@SuppressWarnings("unchecked")
	protected void adcionaTelaNoHistorico(Window telaOrigem) {
		ProCtr<ProBaseVO> controllerOrigem = (ProCtr<ProBaseVO>) telaOrigem.getAttribute("_$composer$_");
		String url = (String) controllerOrigem.getArg().get("casodeusoatual");
		Map<String, Window> windowHistory = (Map<String, Window>) telaOrigem.getPage().getAttribute("history");
		if (windowHistory == null) {
			windowHistory = new LinkedHashMap<String, Window>();
			telaOrigem.getPage().setAttribute("history", windowHistory);
		}
		if (windowHistory.containsKey(url))
			windowHistory.remove(telaOrigem);
		windowHistory.put(url, telaOrigem);
	}

	protected void configuraAtributosPassadosParaCasoDeUsoSecundario(String urlcasodeuso, Map<String, Object> attributes) {
		String[] partesDaUrl = urlcasodeuso.split("\\?");
		if (partesDaUrl.length > 1) {
			String[] parametrosValores = partesDaUrl[1].split("&");
			for (String parametroValor : parametrosValores) {
				if (parametroValor.contains("=")) {
					String[] split = parametroValor.split("=");
					attributes.put(split[0], split[1]);
				}
			}
		}
		attributes.put("casodeusoatual", urlcasodeuso);
	}

	protected Component recuperaComponenteMeioDaTela(AbstractComponent telaOrigem) {
		Component component = telaOrigem.getFellowIfAny("meio", true);
		if (component != null) {
			return component;
		} else {
			component = telaOrigem.getParent();
			while (component != null && !component.getId().equals("meio")) {
				component = component.getParent();
			}
			return component;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Window> getHistory(Window telaOrigem) {
		Map<String, Window> windowHistory = (Map<String, Window>) telaOrigem.getPage().getAttribute("history");
		LinkedList<Window> linkedList = new LinkedList<Window>();
		if (windowHistory != null) {
			linkedList.addAll(windowHistory.values());
			return linkedList;
		}
		return linkedList;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Window> getHistoryMap(Window telaOrigem) {
		Map<String, Window> windowHistory = (Map<String, Window>) telaOrigem.getPage().getAttribute("history");
		if (windowHistory != null) {
			return windowHistory;
		}
		return new LinkedHashMap<String, Window>();
	}

	private boolean temRodape(ButtonRelatorioPDFBind relatorio) {
		return StringUtils.isNotBlank(relatorio.getMetodoMontarRodape());
	}

	private boolean temCabecalho(ButtonRelatorioPDFBind relatorio) {
		return StringUtils.isNotBlank(relatorio.getMetodoMontarCabecalho());
	}

	protected Document instanciarDocumento(String orientacaoDaPagina) {
		Document document;
		if ("PAISAGEM".equalsIgnoreCase(orientacaoDaPagina)) {
			document = new Document(PageSize.A4.rotate());
		} else {
			document = new Document();
		}
		return document;
	}

	protected XMLWorker configurarParser(Document document, PdfWriter pdfWriter) {
		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);

		HtmlPipelineContext htmlPipelineContext = new HtmlPipelineContext(null);
		htmlPipelineContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		htmlPipelineContext.setImageProvider(new ItextImagemBase64Util());

		PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(document, pdfWriter);
		HtmlPipeline htmlPipeline = new HtmlPipeline(htmlPipelineContext, pdfWriterPipeline);
		CssResolverPipeline cssResolverPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

		XMLWorker xmlWorker = new XMLWorker(cssResolverPipeline, true);
		return xmlWorker;
	}

	public byte[] converteHTMLParaPDF(ButtonRelatorioPDFBind relatorio, Window windowAtual) throws Exception {

		Document document = instanciarDocumento(relatorio.getOrientacao());
		document.setMarginMirroring(false);
		ByteArrayOutputStream pdfDoCorpoDoRelatorio = new ByteArrayOutputStream();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, pdfDoCorpoDoRelatorio);

		document.setMargins(relatorio.getMargemEsquerdaEmPontos(), relatorio.getMargemDireitaEmPontos(), relatorio.getMargemSuperiorComCabecalhoEmPontos(), relatorio.getMargemInferiorComRodapeEmPontos());

		document.open();
		XMLWorker xmlWorker = configurarParser(document, pdfWriter);
		XMLParser xmlParser = new XMLParser(xmlWorker);
		try {
			xmlParser.parse(new ByteArrayInputStream(relatorio.getConteudoCorpo()), true);
		} catch (RuntimeWorkerException e) {
			log.error("Esse erro normalmente acontece quando seu html tem problemas. Verifique se seu HTML tem as tags (html e body) e teste retirar essas tags, deixando apenas o conteúdo do body.", e);
		}

		document.close();
		pdfWriter.close();

		if (temCabecalho(relatorio) || temRodape(relatorio)) {
			pdfDoCorpoDoRelatorio = montarCabecalhoRodape(pdfDoCorpoDoRelatorio, relatorio, getController(windowAtual));
		}

		return pdfDoCorpoDoRelatorio.toByteArray();

	}

	public ByteArrayOutputStream montarCabecalhoRodape(ByteArrayOutputStream pdfDoCorpoDoRelatorio, ButtonRelatorioPDFBind relatorio, ProCtr proCtr) throws Exception {
		PdfReader documento = new PdfReader(pdfDoCorpoDoRelatorio.toByteArray());
		ByteArrayOutputStream pdfCompleto = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(documento, pdfCompleto);
		PdfContentByte pagina;
		int quantidadePaginas = documento.getNumberOfPages();
		for (int i = 1; i <= quantidadePaginas; i++) {
			pagina = stamper.getOverContent(i);

			float larguraInicial = documento.getPageSizeWithRotation(i).getLeft(relatorio.getMargemEsquerdaEmPontos());
			float larguraFinal = documento.getPageSizeWithRotation(i).getRight(relatorio.getMargemDireitaEmPontos());
			if (temCabecalho(relatorio)) {
				float alturaInicialCabecalho = documento.getPageSizeWithRotation(i).getTop(relatorio.getMargemSuperiorEmPontos());
				float alturaFinalCabecalho = alturaInicialCabecalho - relatorio.getTamanhoCabecalhoEmPontos();
				try {

					Method metodoGerarHTML = proCtr.getClass().getMethod(relatorio.getMetodoMontarCabecalho(), int.class, int.class);
					Object retorno = metodoGerarHTML.invoke(proCtr, i, quantidadePaginas);
					ElementList cabecalho = parseHtmlParaElementosPDF((byte[]) retorno);
					ColumnText colunaCabecalho = new ColumnText(pagina);
					Rectangle areaDoCabecalho = new Rectangle(larguraInicial, alturaInicialCabecalho, larguraFinal, alturaFinalCabecalho);
					colunaCabecalho.setSimpleColumn(areaDoCabecalho);
					for (com.itextpdf.text.Element e : cabecalho) {
						colunaCabecalho.addElement(e);
					}
					colunaCabecalho.go();
				} catch (NoSuchMethodException e) {
					log.error("Verifique se o método de montar o cabeçalho recebe dois parâmetros inteiros. (número da página atual e quantidade de páginas)", e);
				}
			}
			if (temRodape(relatorio)) {
				float alturaInicialRodape = documento.getPageSizeWithRotation(i).getBottom(relatorio.getMargemInferiorEmPontos());
				float alturaFinalRodape = alturaInicialRodape + relatorio.getTamanhoRodapeEmPontos();
				try {

					Method metodoGerarHTML = proCtr.getClass().getMethod(relatorio.getMetodoMontarRodape(), int.class, int.class);
					Object retorno = metodoGerarHTML.invoke(proCtr, i, quantidadePaginas);
					ElementList rodape = parseHtmlParaElementosPDF((byte[]) retorno);

					ColumnText colunaRodape = new ColumnText(pagina);
					Rectangle areaDoRodape = new Rectangle(larguraInicial, alturaInicialRodape, larguraFinal, alturaFinalRodape);
					colunaRodape.setSimpleColumn(areaDoRodape);
					for (com.itextpdf.text.Element e : rodape) {
						colunaRodape.addElement(e);
					}
					colunaRodape.go();
				} catch (NoSuchMethodException e) {
					log.error("Verifique se o método de montar o rodapé recebe dois parâmetros inteiros. (número da página atual e quantidade de páginas)", e);
				}
			}

		}
		stamper.close();
		documento.close();
		pdfDoCorpoDoRelatorio.close();
		return pdfCompleto;
	}

	public ElementList parseHtmlParaElementosPDF(byte[] html) throws IOException {
		// CSS
		CSSResolver cssResolver = new StyleAttrCSSResolver();

		// HTML
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		htmlContext.setImageProvider(new ItextImagemBase64Util());

		// Pipelines
		ElementList elements = new ElementList();
		ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
		HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
		CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

		// XML Worker
		XMLWorker worker = new XMLWorker(cssPipeline, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new ByteArrayInputStream(html), true);

		return elements;
	}

	public void aplicarSegurancaNoBotaoTabular(ProTabularCtr ctr, ButtonBox botaoEditarTabular) {

		String urlCasoDeUsoAtual = recuperaCasoDeUsoAtual(ctr);
		if (perfilPossuiFuncao(ctr.getUsuarioVO(), urlCasoDeUsoAtual, Operacao.SALVAR)) {
			botaoEditarTabular.setMostrarBotaoSalvar(true);
		} else {
			botaoEditarTabular.setMostrarBotaoSalvar(false);
		}
		if (perfilPossuiFuncao(ctr.getUsuarioVO(), urlCasoDeUsoAtual, Operacao.EXCLUIR)) {
			botaoEditarTabular.setMostrarBotaoExcluir(true);
		} else {
			botaoEditarTabular.setMostrarBotaoExcluir(false);
		}
	}
}
