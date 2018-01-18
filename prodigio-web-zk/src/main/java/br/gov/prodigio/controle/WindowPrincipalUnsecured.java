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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Script;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.interfaces.IUnidade;
import br.gov.prodemge.ssc.interfaces.IUnidadeEspecie;
import br.gov.prodemge.ssc.interfaces.IUnidadePapel;
import br.gov.prodemge.ssc.interfaces.base.IUsuarioBase;
import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.IProFacade;
import br.gov.prodigio.visao.helper.ProMessageHelper;

//TODO [glauco.cardoso] mudar de WindowPrincipal para ControllerPrincipalUnsecured
public class WindowPrincipalUnsecured extends GenericForwardComposer {
	private static final Logger log = LoggerFactory.getLogger(WindowPrincipalUnsecured.class);
	/**
	 * 
	 */

	private static final long serialVersionUID = 266151031140143309L;
	private AbstractComponent tela;
	private final IUsuarioBase iUsuarioAnonimo = new IUsuarioBase() {
		@Override
		public String getNome() {
			return "Usuário Anônimo";
		}

		@Override
		public String getLogin() {
			return "userlogin";
		}
	};
	private final IUnidade iUnidadeAnonima = new IUnidade() {

		@Override
		public IUnidade getUnidadePai() {
			return null;
		}

		@Override
		public String getTelefone() {
			return null;
		}

		@Override
		public String getSigla() {
			return null;
		}

		@Override
		public String getNome() {
			return "Unidade Anônima";
		}

		@Override
		public Set<IUnidadePapel> getListaUnidadePapel() {
			return null;
		}

		@Override
		public Set<IUnidadeEspecie> getListaUnidadeEspecie() {
			return null;
		}

		@Override
		public Long getId() {
			return null;
		}

		@Override
		public String getFax() {
			return null;
		}

		@Override
		public String getEmail() {
			return null;
		}

		@Override
		public String getDescricao() {
			return "Descrição da Unidade Anônima";
		}

		@Override
		public Long getCodigoOrgao() {
			return null;
		}

		@Override
		public String getCodigo() {
			return null;
		}

		@Override
		public String getCnpj() {
			return null;
		}
	};
	private Object versao;

	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		IProFacade fachadaDeNegocio = (IProFacade) comp.getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO, Component.APPLICATION_SCOPE);

		super.doAfterCompose(comp);
		registraScriptsNaTelaInicial(comp);

	}

	protected void registraScriptsNaTelaInicial(Component comp) {
		comp.setAttribute("classecontrole", this, false);
		tela = (AbstractComponent) comp;
		List<String> listaDeScripts = recuperarScriptsPadroes();
		listaDeScripts.forEach(script -> adicionarScript(tela, script));

	}

	protected List<String> recuperarScriptsPadroes() {
		List<String> listaDeScripts = new ArrayList<String>();
		listaDeScripts.add("~./js/jquery.price_format.js");
		listaDeScripts.add("~./js/jquery.maskedinput-1.2.2.js");
		listaDeScripts.add("~./js/jquery.price_format.js");
		listaDeScripts.add("~./js/jquery.autotab-1.1b.js");
		listaDeScripts.add("~./js/mascara.js");
		return listaDeScripts;
	}

	protected void adicionarScript(AbstractComponent tela, String caminho_script) {
		Script novoScript = new Script();
		novoScript.setSrc(caminho_script);
		tela.appendChild(novoScript);
	}

	@Deprecated
	public void abrir(Menuitem itemDeMenuClicado) {
		abrir((AbstractComponent) itemDeMenuClicado);
	}

	public void abrir(AbstractComponent itemDeMenuClicado) {
		Component meio = getTela().getFellow("meio");
		String urlcasodeuso = (String) itemDeMenuClicado.getAttribute("urlcasodeuso");
		Page index = (Page) getPage().getDesktop().getPages().toArray()[0];
		// redireciona o meio da pagina
		Component component = null;
		component = meio.getFirstChild();
		if (component != null) {
			component.detach();
		}

		Map attributes = getTela().getAttributes(Component.SESSION_SCOPE);
		attributes.put("casodeusoatual", urlcasodeuso);

		meio.getChildren().clear();
		component = Executions.createComponents(urlcasodeuso, meio, attributes);
		meio.appendChild(component);
	}

	public void configuraUsuarioUnidadeLogada() {
		getTela().setAttribute(Constantes.USUARIO_AUTENTICADO, iUsuarioAnonimo, Component.SESSION_SCOPE);

		getTela().setAttribute(Constantes.UNIDADE_AUTENTICADA, iUnidadeAnonima, Component.SESSION_SCOPE);
	}

	public void logout() {
		ProMessageHelper controllerMessagesHelper = ProMessageHelper.getInstance();
		if (desejaRealmente("Deseja realmente sair?")) {
			Sessions.getCurrent().invalidate();
			HttpSession s = (HttpSession) Sessions.getCurrent().getNativeSession();
			s.invalidate();

			Executions.sendRedirect("/home/timeout.zul");
		}

	}

	public void SSOGlobalLogout() {
		if (desejaRealmente("Deseja realmente sair?")) {
			Executions.sendRedirect("/?GLO=true");
		}
	}

	public boolean desejaRealmente(String msg) {
		return Messagebox.OK == Messagebox.show(msg, "Atenção!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
	}

	public String getNomeUsuario() {
		IUsuarioBase usuario = (IUsuarioBase) getTela().getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);
		if (usuario != null) {
			return usuario.getNome();
		} else {
			return " ERRO ";
		}
	}

	public AbstractComponent getTela() {
		return tela;
	}

	public void setTela(AbstractComponent tela) {
		this.tela = tela;
	}

	protected void configuraVersoes() {
		if (versao == null) {
			String jarPath;
			try {
				jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				String[] tokes = jarPath.split("/");
				for (String string : tokes) {
					if (string.endsWith(".war")) {
						String[] tokes2 = string.split("front-");
						this.versao = tokes2[1].replace(".war", "");
					}
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	public Object getVersao() {
		return versao;
	}

	public void setVersao(Object versao) {
		this.versao = versao;
	}
}
