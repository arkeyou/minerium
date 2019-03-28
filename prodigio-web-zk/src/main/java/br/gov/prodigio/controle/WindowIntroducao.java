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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.impl.LabelElement;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.interfaces.IPapel;
import br.gov.prodemge.ssc.interfaces.IPermissao;
import br.gov.prodemge.ssc.interfaces.IPermissaoEspecie;
import br.gov.prodemge.ssc.interfaces.IRecurso;
import br.gov.prodemge.ssc.interfaces.IRecursoOperacao;
import br.gov.prodemge.ssc.interfaces.IUnidade;
import br.gov.prodemge.ssc.interfaces.IUnidadeEspecie;
import br.gov.prodemge.ssc.interfaces.IUsuario;
import br.gov.prodemge.ssc.interfaces.IUsuarioUnidade;
import br.gov.prodemge.ssc.interfaces.IUsuarioUnidadePapel;
import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.IProFacade;
import br.gov.prodigio.visao.helper.ProHelperView;
import br.gov.prodigio.visao.helper.ProMessageHelper;

public class WindowIntroducao extends WindowPrincipalUnsecured {

	public static final String PARAMETRO_SESSAO_LOGIN_UNIDADE_EXECUTORA = "j_unidadeexecutora";

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(WindowIntroducao.class);

	// private Charts chart;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		/*
		 * chart.setModel(ColumnBasicData.getCategoryModel()); chart.getXAxis().setMin(0); chart.getXAxis().getTitle().setText("Rainfall (mm)"); Tooltip tooltip = chart.getTooltip();
		 * tooltip.setHeaderFormat("<span style=\"font-size:10px\">{point.key}</span><table>"); tooltip.setPointFormat("<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>" +
		 * "<td style=\"padding:0\"><b>{point.y:.1f} mm</b></td></tr>"); tooltip.setFooterFormat("</table>"); tooltip.setShared(true); tooltip.setUseHTML(true); chart.getPlotOptions().getColumn().setPointPadding(0.2);
		 * chart.getPlotOptions().getColumn().setBorderWidth(0);
		 */
	}

	public void chamaTelaDoMeio() {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		try {
			ProHelperView.insereNovoConteudoNoCentroDaJanela("/", getTela(), attributes);
		} catch (Exception e) {
			// faz nada
		}
	}

	@Override
	@Deprecated
	public void abrir(Menuitem itemDeMenuClicado) {
		abrir((AbstractComponent) itemDeMenuClicado);
	}

	@Override
	public void abrir(AbstractComponent itemDeMenuClicado) {
		String urlcasodeuso = (String) itemDeMenuClicado.getAttribute("urlcasodeuso");
		Map attributes = getTela().getAttributes(Component.SESSION_SCOPE);
		ProHelperView.insereNovoConteudoNoCentroDaJanela(urlcasodeuso, getTela(), attributes);
	}

	public void insereMenuPopUp(AbstractComponent menu) {
		AbstractComponent menupopup;
		// Nav está disponível apenas no zk EE
		// if (menu instanceof Nav) {
		// insereItemDeMenu(menu);
		// } else {
		menupopup = new Menupopup();
		menupopup.setParent(menu);
		menu.appendChild(menupopup);
		insereItemDeMenu(menupopup);
		// }
	}

	protected void insereItemDeMenu(AbstractComponent menu) {
		// TODO alterar processo de montagem do menu
		ProMessageHelper controllerMessagesHelper = ProMessageHelper.getInstance();
		IProFacade fachadaDeNegocio = (IProFacade) getTela().getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO, Component.APPLICATION_SCOPE);

		try {

			IUsuario usuario = (IUsuario) getTela().getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);
			IUnidade unidade = (IUnidade) getTela().getAttribute(Constantes.UNIDADE_AUTENTICADA, Component.SESSION_SCOPE);
			// Se não selecionou a unidade nao monta o menu
			if (unidade == null) {
				return;
			}

			List<LabelElement> listaMenuInseridos = new ArrayList<LabelElement>();
			Set<IUsuarioUnidade> acessosUsuarioUnidade = usuario.getListaUsuarioUnidade();
			List<String> itensMenu = new ArrayList<String>();
			for (IUsuarioUnidade acesso : acessosUsuarioUnidade) {
				// Só deve filtrar pela unidade selecionada
				if (acesso.getUnidade().getId().longValue() != unidade.getId().longValue()) {
					continue;
				}

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
						LabelElement menuPai = null;
						// Nav está disponível apenas no zk EE
						// if (menu instanceof Nav) {
						// menuPai = (LabelElement) menu;
						// } else {
						menuPai = (LabelElement) menu.getParent();
						// }
						IRecurso recurso = recursoOperacao.getRecurso();
						boolean possuiFuncao = true;
						// System.out.print(menu.getLabel() + "  => ");
						// System.out.println(recurso.getChaveMenu());
						if (recurso.getChaveMenu() != null && recurso.getChaveMenu().equalsIgnoreCase(menuPai.getLabel())) {
							List<IPermissaoEspecie> listaPermissaoEspecie = recursoOperacao.getListaPermissaoEspecie();
							// Caso exista Especie deve verificar se a especie é
							// da Unidade do Usuario
							if (listaPermissaoEspecie != null && !listaPermissaoEspecie.isEmpty()) {
								possuiFuncao = false;
								forEspecie: for (IPermissaoEspecie iPermissaoEspecie : listaPermissaoEspecie) {
									Set<IUnidadeEspecie> listaEspecieUnidade = iPermissaoEspecie.getEspecie().getUnidadeEspecie();
									for (IUnidadeEspecie especieUnidade : listaEspecieUnidade) {
										IUnidade unidadeEspecie = especieUnidade.getUnidade();
										if (acesso.getUnidade().getId().longValue() == unidadeEspecie.getId().longValue()) {
											possuiFuncao = true;
											break forEspecie;
										}
									}
								}

							}

							String itemMenuString = recurso.getChaveMenu() + recurso.getUrl();
							boolean itemJaInserido = itensMenu.contains(itemMenuString);
							// Se a especie nao liberar nao exibe o menu
							// Nao insere item de menu repetido
							if (!possuiFuncao || itemJaInserido) {
								continue;
							}
							menuPai.setVisible(true);
							LabelElement itemMenu = null;
							// Nav está disponível apenas no zk EE
							// if (menu instanceof Nav) {
							// itemMenu = new Navitem();
							// } else {
							itemMenu = new Menuitem();
							// }

							itemMenu.setAttribute("urlcasodeuso", recurso.getUrl());
							itemMenu.addEventListener("onClick", evento());
							itemMenu.setLabel(recurso.getNome());
							itensMenu.add(itemMenuString);
							itemMenu.setParent(menu);
							// menupopup.appendChild(itemMenu);
							// Somente obtem a lista dos menus a serem inseridos
							listaMenuInseridos.add(itemMenu);
						}
					}
				}
			}

			ordenaMenu(listaMenuInseridos);
			// Insere os itens de menu ordenados
			for (LabelElement itemMenu : listaMenuInseridos) {
				menu.appendChild(itemMenu);
			}

		} catch (Exception e) {
			log.error("Erro ao inserir item de menu ", e);
			controllerMessagesHelper.emiteMensagemErro(e.getMessage());
		}
	}

	protected void ordenaMenu(List<LabelElement> listaMenuInseridos) {
		// Ordena em ordem alfabetica
		Collections.sort(listaMenuInseridos, new Comparator<LabelElement>() {
			@Override
			public int compare(LabelElement menu1, LabelElement menu2) {
				return menu1.getLabel().compareTo(menu2.getLabel());
			}
		});

	}

	protected EventListener evento() {
		return new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				abrir((AbstractComponent) e.getTarget());
			}
		};
	}

}
