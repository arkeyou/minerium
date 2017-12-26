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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.interfaces.IModulo;
import br.gov.prodemge.ssc.interfaces.IUnidade;
import br.gov.prodemge.ssc.interfaces.IUsuario;
import br.gov.prodemge.ssc.interfaces.IUsuarioUnidade;
import br.gov.prodemge.ssc.interfaces.IUsuarioUnidadePapel;

public class WindowIndex extends WindowPrincipal {

	public static final String PARAMETRO_SESSAO_LOGIN_UNIDADE_EXECUTORA = "j_unidadeexecutora";

	private static final long serialVersionUID = 1L;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		IUnidade unidade = (IUnidade) getTela().getAttribute(Constantes.UNIDADE_AUTENTICADA, Component.SESSION_SCOPE);
		if (unidade == null) {
			List<IUnidade> unidades = recuperaUnidadeDeAcessoDoUsuario();
			if (unidades.size() == 1) {
				unidade = unidades.get(0);
				getTela().setAttribute(Constantes.UNIDADE_AUTENTICADA, unidade, Component.SESSION_SCOPE);
				getTela().setAttribute(PARAMETRO_SESSAO_LOGIN_UNIDADE_EXECUTORA, String.valueOf(unidades.get(0).getCodigoOrgao()), Component.SESSION_SCOPE);
				exibeTelaDeIntroducao(unidade);
			} else {
				exibeTelaDeSelecaoDeUnidade(unidades);
			}
		} else {
			exibeTelaDeIntroducao(unidade);
		}
	}

	protected List<IUnidade> recuperaUnidadeDeAcessoDoUsuario() {
		IUsuario usuario = (IUsuario) getTela().getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);
		Set<IUsuarioUnidade> acessosUsuarioUnidade = usuario.getListaUsuarioUnidade();
		List<IUnidade> unidades = new ArrayList<IUnidade>();
		String nomeModulo = Executions.getCurrent().getContextPath();
		for1: for (IUsuarioUnidade usuarioUnidade : acessosUsuarioUnidade) {
			// Somente mostra as unidades do modulo que esta sendo acessado
			Set<IUsuarioUnidadePapel> usuarioUnidadePapel = usuarioUnidade.getUsuarioUnidadePapel();
			if (usuarioUnidadePapel != null) {
				for (IUsuarioUnidadePapel uup : usuarioUnidadePapel) {
					IModulo modulo = uup.getUnidadePapel().getPapel().getModulo();
					if (modulo.getContexto().equals(nomeModulo.substring(1))) {
						unidades.add(usuarioUnidade.getUnidade());
						continue for1;
					}
				}
			}
		}
		return unidades;
	}

	protected void exibeTelaDeSelecaoDeUnidade(List<IUnidade> unidades) {
		try {
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("unidades", unidades);
			mapa.put("unidadeSelecionada", unidades.get(0));
			String urlcasodeuso = "/selecaounidade.zul";
			Component component = null;
			AbstractComponent tela = getTela();

			Component includeIntroducao = tela.getFellow("introducao");
			component = includeIntroducao.getFirstChild();
			if (component != null) {
				component.detach();
			}
			includeIntroducao.getChildren().clear();
			mapa.put("casodeusoatual", urlcasodeuso);

			component = Executions.createComponents(urlcasodeuso, includeIntroducao, mapa);
			includeIntroducao.appendChild(component);
			/*
			 * popupUnidade.doModal();
			 */} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void exibeTelaDeIntroducao(IUnidade unidade) {
		AbstractComponent tela = getTela();

		tela.setAttribute(Constantes.UNIDADE_AUTENTICADA, unidade, Component.SESSION_SCOPE);
		tela.setAttribute(PARAMETRO_SESSAO_LOGIN_UNIDADE_EXECUTORA, String.valueOf(unidade.getCodigoOrgao()), Component.SESSION_SCOPE);

		Component includeIntroducao = tela.getFellow("introducao");
		String urlcasodeuso = "/introducao.zul";
		Page index = (Page) getPage().getDesktop().getPages().toArray()[0];
		// redireciona o meio da pagina
		Component component = null;
		component = includeIntroducao.getFirstChild();
		if (component != null) {
			component.detach();
		}
		includeIntroducao.getChildren().clear();

		Map attributes = tela.getAttributes(Component.SESSION_SCOPE);
		attributes.put("casodeusoatual", urlcasodeuso);

		component = Executions.createComponents(urlcasodeuso, includeIntroducao, attributes);
		includeIntroducao.appendChild(component);
	}

}
