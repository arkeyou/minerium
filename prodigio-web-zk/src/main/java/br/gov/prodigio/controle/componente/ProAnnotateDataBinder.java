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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ManyToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.CollectionItem;
import org.zkoss.zul.Window;

import br.gov.prodemge.ssc.enumerations.Operacao;
import br.gov.prodemge.ssc.interfaces.IUsuario;
import br.gov.prodemge.ssc.interfaces.base.IUsuarioBase;
import br.gov.prodigio.comuns.utils.Reflexao;
import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.entidades.Arquivo;
import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.visao.helper.ProHelperView;

public class ProAnnotateDataBinder extends AnnotateDataBinder {
	private static final Logger log = LoggerFactory.getLogger(ProAnnotateDataBinder.class);
	Window window = null;

	public ProAnnotateDataBinder(Component comp) {
		super(comp);
		window = (Window) comp;
	}

	@Override
	public void addCollectionItem(Class item, Class owner, CollectionItem decor) {
		super.addCollectionItem(item, owner, decor);
	}

	/*
	 * @Override public void addCollectionItem(String comp, CollectionItem decor) { super.addCollectionItem(comp, decor); }
	 */

	@Override
	public void bindBean(String beanid, Object bean) {
		super.bindBean(beanid, bean);
	}

	@Override
	public void loadComponent(Component arg0) {
		super.loadComponent(arg0);
		if (arg0 instanceof ButtonBox) {
			((ButtonBox) arg0).editarNovaLinha(arg0);
			habilitaDesabilita(arg0);
		} else if (arg0 instanceof CpfboxBind) {
			((CpfboxBind) arg0).afterCompose();
			habilitaDesabilita(arg0);
		} else if (arg0 instanceof Bandboxbind) {
			((Bandboxbind) arg0).configura();
			ProHelperView.aninharComponenteAoComponentePai(((Bandboxbind) arg0));
			habilitaDesabilita(arg0);
		} else if (arg0 instanceof TextboxBind) {
			((TextboxBind) arg0).afterCompose();
			ProHelperView.aninharComponenteAoComponentePai(((TextboxBind) arg0));
			habilitaDesabilita(arg0);
		} else if (arg0 instanceof IntboxBind) {
			((IntboxBind) arg0).afterCompose();
			habilitaDesabilita(arg0);
		} else if (arg0 instanceof DivSecurity) {
			((DivSecurity) arg0).afterCompose();
		}
	}

	protected void habilitaDesabilita(Component arg0) {
		ProCtr ctr = ProHelperView.getController(window);
		final boolean habilita = false;
		final boolean desabilita = true;
		final ProBaseVO objetoAtual = (ProBaseVO) ctr.getObjetoAtual();
		if (ProHelperView.cadastroAprovado(objetoAtual) || ProHelperView.cadastroConcluido(objetoAtual)) {
			ProHelperView.desabilitaHabilitaComponente(arg0, false, desabilita);
		} else {
			IUsuarioBase usuario = ctr.getUsuarioVO();
			Map arg = ctr.getArg();
			String recurso = null;
			if (arg != null) {
				recurso = (String) ctr.getArg().get("casodeusoatual");
			}

			if (recurso == null) {
				recurso = (String) ctr.getTela().getAttribute("casodeusoatual", Component.SESSION_SCOPE);
			}
			// Se ele Não for instancia de IUsuario é pq não está usando a segurança, logo mostra todos os botões
			if (!(usuario instanceof IUsuario) || ProHelperView.perfilPossuiFuncao((IUsuario) usuario, recurso, Operacao.INCLUIR) || ProHelperView.perfilPossuiFuncao((IUsuario) usuario, recurso, Operacao.EDITAR)
					|| window.getFellow("selecao").isVisible()) {
				ProHelperView.desabilitaHabilitaComponente(arg0, false, habilita);
			} else {
				ProHelperView.desabilitaHabilitaComponente(arg0, false, desabilita);
			}
		}
	}

	@Override
	public void saveComponent(Component arg0) {
		super.saveComponent(arg0);
	}

	@Override
	public void saveAttribute(Component comp, String attr) {
		super.saveAttribute(comp, attr);
	}

	/*
	 * public void inicializaObjeto(Component component, Object bean) { List children = component.getChildren(); try { for (Object child : children) { if (child instanceof FieldValidator) { FieldValidator fieldValidator = (FieldValidator) child;
	 * String [] nodos = fieldValidator.getNomeDoObjeto().split("\\."); String nomeDoObejto = fieldValidator.getNomeDoObjeto().replace(nodos[0]+".", ""); Object objeto = Reflexao.recuperaTipoDeRetornoDoMetodoGetNoGrafo(nomeDoObejto, bean);
	 * Reflexao.atualizarCampoDoObjeto(nomeDoObejto, bean, objeto); } else { inicializaObjeto((Component) child, bean); } } } catch (Exception e) { e.printStackTrace(); } }
	 */

	public void loadComponentTree(Component component) {
		loadComponent(component);
		List children = component.getChildren();
		try {
			for (Object child : children) {
				if (child instanceof Component) {
					loadComponentTree((Component) child);
				}
			}
		} catch (Exception e) {
			log.error("Erro ao carregar componente ", e);
		}
	}

	//@Override
	protected Object beforeGetFields(Object bean, String beanid) {
		if (bean instanceof Collection) {
			Collection collection = (Collection) bean;
			if (!collection.isEmpty()) {
				bean = collection.toArray()[0];
			}
		}
		return bean;
	}

	//@Override
	protected Object afterGetFields(Object bean, Object beanChild, String beanid) {
		ProCtr<ProBaseVO> proCtr = (ProCtr<ProBaseVO>) window.getAttribute("$composer");
		SelecaoBox selecaoBox = (SelecaoBox) proCtr.getTela().getFellowIfAny("selecao");
		if (beanChild == null) {
			try {
				if (beanid.equals("toString")) {
					beanChild = bean.toString();
				} else {
					Method method = Reflexao.recuperaMetodoGetDoObjeto(beanid, bean);
					Class<?> returnType = method.getReturnType();
					if (Set.class.isAssignableFrom(returnType)) {// Implementar quando a lista estivr vazia
						Class classe = Reflexao.recuperaTipoDeParametroGenericosEmRetornoDeMetodos(method);
						Set set = new HashSet();
						beanChild = classe.newInstance();
						set.add(beanChild);
						Reflexao.atualizarCampoDoObjeto(beanid, bean, set);

					} else if (ProBaseVO.class.isAssignableFrom(returnType) || (Arquivo.class.isAssignableFrom(returnType))) {
						try {
							ManyToOne annotation = method.getAnnotation(ManyToOne.class);
							if (annotation != null) {
								//return super.afterGetFields(bean, beanChild, beanid);
								return afterGetFields(bean, beanChild, beanid);
							}
							beanChild = Reflexao.recuperaValorDaPropriedade(beanid, bean);
							if (beanChild == null) {
								beanChild = returnType.newInstance();
								if (estaEmModoDeEdicao(selecaoBox)) {
									proCtr.configuraAuditoria(beanChild);
								}
							}
							Reflexao.atualizarCampoDoObjeto(beanid, bean, beanChild);
						} catch (Exception e) {
							log.error("Não foi possivel  inicializar o objeto do tipo: {} para o método {}.", returnType, method.getName());
						}
					}
				}
				Object o = proCtr.aposInicializacaoDeBind(bean, beanChild, beanid);
				if (o != null) {
					beanChild = o;
					Reflexao.atualizarCampoDoObjeto(beanid, bean, beanChild);
				}
				if (estaEmModoDeEdicao(selecaoBox)) {
					proCtr.configuraAuditoria(beanChild);
				}
				return beanChild;
			} catch (Exception e) {
				log.error("Erro ao inicializar o objeto ", e);
			}
		}
		//return super.afterGetFields(bean, beanChild, beanid);
		return afterGetFields(bean, beanChild, beanid);
	}

	private boolean estaEmModoDeEdicao(SelecaoBox selecaoBox) {
		return selecaoBox != null && !selecaoBox.isVisible();
	}

	//@Override
	protected boolean fetchByDefault(Component comp) {
		if (comp instanceof FieldValidator) {
			return false;
		}
		//return super.fetchByDefault(comp);
		return fetchByDefault(comp);
	}

	@Override
	public void loadAll() {
		super.loadAll();
		// Atualiza todos os DivSecurity
		desabilitaSecurityComponents(this.window.getChildren());

	}

	/**
	 * @author p057693 Procura em todos os filhos os DivSecurity e executa as regras de visibilidade e desabilitação
	 * @param componentes
	 */
	@SuppressWarnings("unchecked")
	private void desabilitaSecurityComponents(Collection<Component> componentes) {
		if (componentes != null)
			for (Component component : componentes) {
				if (component instanceof ButtonSecurity) {
					((ButtonSecurity) component).afterCompose();
				} else if (component instanceof DivSecurity) {
					((DivSecurity) component).afterCompose();
				} else {
					desabilitaSecurityComponents(component.getChildren());
				}
			}
	}
}
