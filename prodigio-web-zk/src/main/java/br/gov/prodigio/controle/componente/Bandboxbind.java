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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.visao.helper.ProHelperView;

public class Bandboxbind extends Bandbox implements AfterCompose, FieldValidator {
	private static final Logger log = LoggerFactory.getLogger(Bandboxbind.class);

	/**
     * 
     */
	private static final long serialVersionUID = 1140459631529658907L;

	private String widthList;
	private String heightList;
	private String nomeDoObjeto;
	private String atributoQueSeraVisualizado;
	private String labelValorList;
	private Listbox listbox;
	private Object object;
	private Object objectPai;
	private String metodoSelecao;
	private String metodoFiltro;
	private String dependeDoComponente;
	private Component componentDependido;
	private Map<String, Component> componentsDependentes = null;
	private Boolean validarRegra;
	private String atribuicaoAutomatica = "S";
	private Boolean abrirPopUp = false;
	private Boolean estaSendoUsadoEmDetalhe = null;
	private Boolean upperCase = true;
	private String atualizarComponentes = "";
	private Boolean usarFiltroEmMemoria = true;

	private Boolean validarCampo;

	private Object rootObject;

	private String converter;

	private String identificador;

	private String filtro;

	private String validarQuando = "";

	private boolean aberto = false;

	private boolean pararDeAbrir = false;

	public boolean isAberto() {
		return aberto;
	}

	public void setAberto(boolean aberto) {
		this.aberto = aberto;
	}

	public Bandboxbind() {
		super();
	}

	public Boolean estaSendoUsadoEmDetalhe() {
		if (estaSendoUsadoEmDetalhe == null) {
			Component parent = null;
			while (parent != null) {
				if (parent instanceof DetBox) {
					estaSendoUsadoEmDetalhe = true;
					return estaSendoUsadoEmDetalhe;
				}
			}
			estaSendoUsadoEmDetalhe = false;
			return estaSendoUsadoEmDetalhe;
		}
		return estaSendoUsadoEmDetalhe;
	}

	public void setAbrirPopUp(Boolean abrirPopUp) {
		this.abrirPopUp = abrirPopUp;
	}

	public Boolean getAbrirPopUp() {
		return abrirPopUp;
	}

	@SuppressWarnings("rawtypes")
	public void selecionarItem(Event e) throws Exception {
		Window window = ProCtr.findWindow(this);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.onSelectBandBox(e);
	}

	@Override
	public void setFocus(boolean focus) {

		super.setFocus(focus);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void afterCompose() {
		this.setSclass("input-group");
		setPlaceholder("Digite aqui...");
		configura();
		GenericConstraint.configuraConstraint(this);
		Window window = ProCtr.findWindow(this);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				if (!pararDeAbrir) {
					Bandboxbind target = (Bandboxbind) e.getTarget();
					ctr.criaListParaBandBox(target, target.getNomeDoObjeto());
					ctr.filtraBandboxTeclado(target, usarFiltroEmMemoria);
					pararDeAbrir = true;
				} else {
					pararDeAbrir = false;
				}
			}
		});

		setCtrlKeys("#del#up#down");
		addEventListener(Events.ON_CTRL_KEY, new EventListener() {
			@SuppressWarnings("static-access")
			@Override
			public void onEvent(Event e) throws Exception {
				KeyEvent eventKey = (KeyEvent) e;
				Bandboxbind bandAtual = (Bandboxbind) e.getTarget();

				if (eventKey.DOWN == eventKey.getKeyCode()) {
					if (bandAtual.getFirstChild() != null && bandAtual.getFirstChild().getFirstChild() != null) {
						Listbox listbox = (Listbox) bandAtual.getFirstChild().getFirstChild();
						List<Listitem> items = listbox.getItems();
						if (items.size() > 0) {
							items.get(0).setFocus(true);
						}
					}
				} else {
					pararDeAbrir = false;
					if (!bandAtual.isReadonly()) {
						bandAtual.clear(ctr);
					}
				}
			}
		});

		addEventListener(Events.ON_BLUR, new EventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				pararDeAbrir = false;
			}
		});

		addEventListener(Events.ON_OPEN, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Bandboxbind bandboxbind = (Bandboxbind) event.getTarget();
				if (!pararDeAbrir) {
					Events.sendEvent(Events.ON_OK, bandboxbind, null);
				} else {
					pararDeAbrir = false;
				}

			}
		});

		addEventListener(Events.ON_CREATE, new EventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				ctr.registrarEventoOnKeyUpBandbox(e);
			}
		});
	}

	@Override
	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals(Events.ON_OPEN)) {
			setValue((String) request.getData().get("value"));
		}
		super.service(request, everError);
	}

	public void configura() {
		this.setMold("rounded");
		this.setAutodrop(false);
		Window window = ProCtr.findWindow(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		if (getId().endsWith("Arg") || (getNomeDoObjeto() != null && getNomeDoObjeto().contains("objetoAtualArg"))) {
			if (StringUtils.isNotBlank(getId())) {
				ctr.inicializaObjetoArg("objetoAtualArg." + getId());
			} else {
				ctr.inicializaObjetoArg(getNomeDoObjeto().replace("classecontrole.", ""));
			}
		}
		ctr.getProAnnotateDataBinderHelper().adicionaElementosNoBandbox(this);
	}

	private void configuraConstraint() {
		GenericConstraint constraint = new GenericConstraint();
		if (this.getConstraint() instanceof SimpleConstraint) {
			constraint.setSimpleConstraint((SimpleConstraint) this.getConstraint());
		}
		this.setConstraint(constraint);
	}

	public String getWidthList() {
		return widthList;
	}

	public void setWidthList(String widthList) {
		this.widthList = widthList;
	}

	public String getHeightList() {
		return heightList;
	}

	public void setHeightList(String heightList) {
		this.heightList = heightList;
	}

	public String getLabelValorList() {
		return labelValorList;
	}

	public void setLabelValorList(String labelValorList) {
		this.labelValorList = labelValorList;
	}

	@Override
	public String getNomeDoObjeto() {
		return nomeDoObjeto;
	}

	@Override
	public void setNomeDoObjeto(String nomeDoObjeto) {
		this.nomeDoObjeto = nomeDoObjeto;
	}

	public Listbox getListbox() {
		return listbox;
	}

	public void setListbox(Listbox listbox) {
		this.listbox = listbox;
	}

	@Override
	public Object getObject() {
		return object;
	}

	@Override
	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public void onChildAdded(Component child) {
		super.onChildAdded(child);
	}

	@Override
	public Object getObjectPai() {
		return objectPai;
	}

	@Override
	public void setObjectPai(Object objectPai) {
		this.objectPai = objectPai;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.controle.componente.FieldValidator#getValidarRegra()
	 */
	@Override
	public Boolean getValidarRegra() {
		return validarRegra;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.controle.componente.FieldValidator#setValidarRegra(java .lang.Boolean)
	 */
	@Override
	public void setValidarRegra(Boolean validarRegra) {
		this.validarRegra = validarRegra;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.controle.componente.FieldValidator#getValidarCampo()
	 */
	@Override
	public Boolean getValidarCampo() {
		return validarCampo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.controle.componente.FieldValidator#setValidarCampo(java .lang.Boolean)
	 */
	@Override
	public void setValidarCampo(Boolean validarCampo) {
		this.validarCampo = validarCampo;
	}

	public Component recuperaComponentParaAninhar(String nomeDoObjeto) throws Exception {
		Component parent = ProHelperView.recuperaParentContexto(this);
		Component comp = ProHelperView.recuperaComponent(parent, nomeDoObjeto);
		if (comp == null) {
			throw new Exception("Componente:<" + nomeDoObjeto + "> não encontrato.");
		}
		return comp;
	}

	public String getMetodoSelecao() {
		return metodoSelecao;
	}

	public String getMetodoFiltro() {
		return metodoFiltro;
	}

	public void setMetodoSelecao(String metodoSelecao) {
		this.metodoSelecao = metodoSelecao;
	}

	public void setMetodoFiltro(String metodoFiltro) {
		this.metodoFiltro = metodoFiltro;
	}

	@Override
	public String getAtributoQueSeraVisualizado() {
		return atributoQueSeraVisualizado;
	}

	@Override
	public void setAtributoQueSeraVisualizado(String atributoVisualizado) {
		this.atributoQueSeraVisualizado = atributoVisualizado;
	}

	@Override
	public String getDependeDoComponente() {
		return dependeDoComponente;
	}

	@Override
	public void setDependeDoComponente(String dependeDoComponent) {
		this.dependeDoComponente = dependeDoComponent;
	}

	@Override
	public Map<String, Component> recuperaComponentesDependentes() {
		return componentsDependentes;
	}

	@Override
	public void addComponentesDependentes(String nomeComponent, Component component) {
		if (this.componentsDependentes == null) {
			this.componentsDependentes = new HashMap<String, Component>();
		}
		this.componentsDependentes.put(nomeComponent, component);
	}

	@Override
	public Object getRootObject() {
		return this.rootObject;
	}

	@Override
	public void setRootObject(Object rootObject) {
		this.rootObject = rootObject;
	}

	@Override
	public String getConverter() {
		return this.converter;
	}

	@Override
	public void setConverter(String converter) {
		this.converter = converter;
	}

	public void clear(ProCtr ctr) {
		Object object = this.getObject();
		if (object != null) {
			try {
				if (object.getClass().isEnum()) {
					ctr.atribuiObjetoVazioAoBandBox(this, object);
				} else {
					Object novoObject = ctr.instanciaNovoObjeto(object.getClass());
					ctr.atribuiObjetoVazioAoBandBox(this, novoObject);
				}
			} catch (Exception e) {
				log.error("Erro ao limpar objeto ", e);
				throw new RuntimeException(e);
			}
		}
		this.setRawValue("");
		this.setObject(null);
		if (this.getListbox() != null)
			this.getListbox().setSelectedItem(null);
		final Map componentesDependentes = this.recuperaComponentesDependentes();
		if (componentesDependentes != null) {
			Set<Map.Entry<String, Component>> set = componentesDependentes.entrySet();
			Iterator i = set.iterator();
			while (i.hasNext()) {
				Map.Entry<String, Component> me = (Map.Entry) i.next();
				final Component value = me.getValue();
				if (value instanceof Bandboxbind) {
					((Bandboxbind) value).clear(ctr);
				}
			}
		}
	}

	public void clear() {
		this.setRawValue("");
		this.setObject(null);
		final Map componentesDependentes = this.recuperaComponentesDependentes();
		if (componentesDependentes != null) {
			Set<Map.Entry<String, Component>> set = componentesDependentes.entrySet();
			Iterator i = set.iterator();
			while (i.hasNext()) {
				Map.Entry<String, Component> me = (Map.Entry) i.next();
				final Component value = me.getValue();
				if (value instanceof Bandboxbind) {
					((Bandboxbind) value).clear();
				}
			}
		}
	}

	private void atribueValorAoBandBox(Bandboxbind bandbox) {
		Window window = ProCtr.findWindow(this);
		ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		List itensFiltradosPelaDigitacao = new ArrayList();
		ctr.filtrarPelaDigitacao(bandbox, itensFiltradosPelaDigitacao);
		if (itensFiltradosPelaDigitacao.size() == 1) {
			ctr.atribuiValorSelecionadoAoBandBox(bandbox, itensFiltradosPelaDigitacao.get(0));
		}

	}

	@Override
	public void setRawValue(Object value) {
		super.setRawValue(value);
	}

	@Override
	public boolean addEventListener(String evtnm, EventListener listener) {
		return super.addEventListener(evtnm, listener);
	}

	public String getAtribuicaoAutomatica() {
		return atribuicaoAutomatica;
	}

	public void setAtribuicaoAutomatica(String atribuicaoAutomatica) {
		this.atribuicaoAutomatica = atribuicaoAutomatica;
	}

	@Override
	public void setIdentificador(String identificador) {
		this.identificador = identificador;

	}

	@Override
	public String getIdentificador() {
		return this.identificador;
	}

	public String getAtualizarComponentes() {
		return atualizarComponentes;
	}

	public void setAtualizarComponentes(String atualizarComponentes) {
		this.atualizarComponentes = atualizarComponentes;
	}

	public String getUuidEvento() {
		return super.getUuid();
	}

	public Boolean getUpperCase() {
		return upperCase;
	}

	public void setUpperCase(Boolean upperCase) {
		this.upperCase = upperCase;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	@Override
	public String getValidarQuando() {
		return validarQuando;
	}

	@Override
	public void setValidarQuando(String validarQuando) {
		this.validarQuando = validarQuando;
	}

	public Component getComponentDependido() {
		return componentDependido;
	}

	public void setComponentDependido(Component componentDependido) {
		this.componentDependido = componentDependido;
	}

	public Boolean getUsarFiltroEmMemoria() {
		return usarFiltroEmMemoria;
	}

	public void setUsarFiltroEmMemoria(Boolean usarFiltroEmMemoria) {
		this.usarFiltroEmMemoria = usarFiltroEmMemoria;
	}

}
