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

import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.sys.ComponentCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zkplus.databind.BindingListModelSet;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.utils.DocumentoDigital;
import br.gov.prodigio.comuns.utils.Reflexao;
import br.gov.prodigio.comuns.utils.StringHelper;
import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.entidades.Arquivo;
import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.utils.ManipulacaoUploadUtil;

public class ProAnnotateDataBinderHelper  implements Serializable {
	private static final String PROPRIEDADE_FORMATO = "formato";
	private static final String PROPRIEDADE_APPLICATION_TYPE = "applicationType";
	private static final String PROPRIEDADE_NOME = "nome";
	private static final Logger log = LoggerFactory.getLogger(ProAnnotateDataBinderHelper.class);

	public void adicionaAnotacaoParaItensDaLista(Component child) {
		if (child instanceof Listitem) {
			Map contactsAnnot = new HashMap();
			contactsAnnot.put("each", "objetoDaLista");
			((Listitem) child).addAnnotation("default", contactsAnnot);
			Map contactsAnnot2 = new HashMap();
			contactsAnnot2.put("value", "objetoDaLista");
			((Listitem) child).addAnnotation("value", "default", contactsAnnot2);
		}
	}

	public void aplicaAnotacoesParaObject(final AbstractComponent comp) {
		String expressao = ((FieldValidator) comp).getNomeDoObjeto();

		Map annBand = new HashMap();
		annBand.put("value", expressao);

		if (((FieldValidator) comp).getConverter() != null && !((FieldValidator) comp).getConverter().equals("")) {
			annBand.put("converter", ((FieldValidator) comp).getConverter());
		}
		if (!(comp instanceof RadiogroupBind)) {
			comp.addAnnotation("value", "default", annBand);
		} else {
			comp.addAnnotation("selectedItem", "default", annBand);
		}
		Map annBand2 = new HashMap();
		annBand2.put("value", expressao);
		annBand2.put("save-when", "none");
		annBand2.put("load-when", "none");
		comp.addAnnotation("object", "default", annBand2);
	}

	public void aplicaAnotacoesParaObjetoPai(final AbstractComponent comp) {
		String expressao = ((FieldValidator) comp).getNomeDoObjeto();
		String[] nodosDaExpressao = expressao.split("\\.");
		int lengthNodosDaExpressao = nodosDaExpressao.length;
		String nomeDoObjetoDonoDoAtributo = "";
		for (int i = 0; i < (lengthNodosDaExpressao - 1); i++) {
			nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo + "." + nodosDaExpressao[i];
		}
		nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo.substring(1);
		Map annBand3 = new HashMap();
		annBand3.put("value", nomeDoObjetoDonoDoAtributo);
		annBand3.put("save-when", "none");
		annBand3.put("load-when", "none");
		comp.addAnnotation("objectPai", "default", annBand3);
	}

	public void aplicaAnotacoesParaObjectRoot(final AbstractComponent comp) {
		String nomeDoObjetoRoot = "";
		String expressao = ((FieldValidator) comp).getNomeDoObjeto();
		String[] nodosDaExpressao = expressao.split("\\.");
		int lengthNodosDaExpressao = nodosDaExpressao.length;
		nomeDoObjetoRoot = nodosDaExpressao[0];
		Map annBand4 = new HashMap();
		annBand4.put("value", nomeDoObjetoRoot);
		annBand4.put("save-when", "none");
		annBand4.put("load-when", "none");
		comp.addAnnotation("rootObject", "default", annBand4);
	}

	public void adicionaElementosNoBandbox(final Bandboxbind bandbox) {
		Window window = ProCtr.findWindow(bandbox);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		bandbox.setAutodrop(false);
		bandbox.setButtonVisible(true);
		ctr.aplicaRegraDeNomeacaoDeComponentes(bandbox);
		aplicaAnotacoesParaObject(bandbox);
		aplicaAnotacoesParaObjetoPai(bandbox);
		aplicaAnotacoesParaObjectRoot(bandbox);

		String atributoVisualizado = ".toString";
		if (bandbox.getAtributoQueSeraVisualizado() != null && !"".equals(bandbox.getAtributoQueSeraVisualizado())) {
			atributoVisualizado = "." + bandbox.getAtributoQueSeraVisualizado() + ".toString";
		}

		String expressao = bandbox.getNomeDoObjeto();
		String expressaoMaisAtributoVisualizado = expressao + atributoVisualizado;
		Map annBand = new HashMap();
		annBand.put("value", expressaoMaisAtributoVisualizado);
		annBand.put("save-when", "none");
		annBand.put("load-when", "none");
		annBand.put("converter", BandboxbindConverter.class.getName());
		bandbox.addAnnotation("value", "default", annBand);

		EventListener evento2 = new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				Bandboxbind bandboxbind = (Bandboxbind) e.getTarget();
				// TODO Verficar o impacto de retornar com a cláusula de verificação.
				if (bandboxbind.getUpperCase() != null && bandboxbind.getUpperCase().booleanValue()) {
					Clients.evalJavaScript("(\"" + bandboxbind.getUuid() + "-real\")");
				}
			}
		};

		bandbox.addEventListener("onFocus", evento2);
	}

	public void adicionaAnotacaoParaCampoTexto(TextboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);

		EventListener evento = new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				TextboxBind textboxBind = (TextboxBind) e.getTarget();
				Clients.evalJavaScript("autotab(\"" + textboxBind.getUuid() + "\")");
			}
		};
		if (StringHelper.isNotEmpty(comp.getMask())) {
			comp.setWidgetListener("onBind", "jq(this).mask('" + comp.getMask() + "');");
		}
		if (comp.getUpper()) {
			comp.setWidgetListener("onKeyUp", "upper(\"" + comp.getUuid() + "\")");
		}
		if (!comp.getPaste()) {
			comp.setWidgetListener("onFocus", "onPaste(\"" + comp.getUuid() + "\")");
		}

	}

	public void adicionaAnotacaoParaCampoUploadArquivo(UploadArquivo uploadArquivo) {
		TextboxBind textboxBind = new TextboxBind();
		textboxBind.setParent(uploadArquivo);
		textboxBind.setWidth("99%");
		textboxBind.setReadonly(true);
		textboxBind.setDisabled(true);
		String[] caminhoNomeArquivo = uploadArquivo.getNomeDoObjeto().split("\\.");
		caminhoNomeArquivo[caminhoNomeArquivo.length - 1] = PROPRIEDADE_NOME;
		String nomeObjetoAux = StringUtils.join(caminhoNomeArquivo, ".");
		textboxBind.setNomeDoObjeto(nomeObjetoAux);
		adicionaAnotacaoParaCampoTexto(textboxBind);

		ButtonUploadBind buttonUploadBind = new ButtonUploadBind();
		buttonUploadBind.setParent(uploadArquivo);
		buttonUploadBind.setNomeDoObjeto(uploadArquivo.getNomeDoObjeto());
		buttonUploadBind.setImage("~./imagem/ico-anexar.png");
		adicionaAnotacaoParaCampoButtonUploadBind(buttonUploadBind);

		ButtonDownloadBind buttonDownloadBind = new ButtonDownloadBind();
		buttonDownloadBind.setParent(uploadArquivo);
		buttonDownloadBind.setNomeDoObjeto(uploadArquivo.getNomeDoObjeto());
		buttonDownloadBind.setImage("~./imagem/ico-download.png");
		adicionaAnotacaoParaCampoButtonDownloadBind(buttonDownloadBind);

		ButtonDeleteDownloadBind buttonDeleteDownloadBind = new ButtonDeleteDownloadBind();
		buttonDeleteDownloadBind.setParent(uploadArquivo);
		buttonDeleteDownloadBind.setNomeDoObjeto(uploadArquivo.getNomeDoObjeto());
		buttonDeleteDownloadBind.setImage("~./imagem/ico-delete.png");
		adicionaAnotacaoParaCampoButtonDeleteDownloadBind(buttonDeleteDownloadBind);

		uploadArquivo.appendChild(textboxBind);
		uploadArquivo.appendChild(buttonUploadBind);
		uploadArquivo.appendChild(buttonDownloadBind);
		uploadArquivo.appendChild(buttonDeleteDownloadBind);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionaAnotacaoParaCampoButtonDeleteDownloadBind(ButtonDeleteDownloadBind buttonBind) {
		Window window = ProCtr.findWindow(buttonBind);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		String expressao = buttonBind.getNomeDoObjeto();
		if ("".equals(expressao) || expressao == null) {
			expressao = "classecontrole.objetoAtual." + buttonBind.getId();
		}
		String[] nodosDaExpressao = expressao.split("\\.");
		int lengthNodosDaExpressao = nodosDaExpressao.length;
		String nomeDoObjetoDonoDoAtributo = "";
		for (int i = 0; i < (lengthNodosDaExpressao - 2); i++) {
			nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo + "." + nodosDaExpressao[i];
		}
		String nomeDoObjetoDoAtributo = "";
		for (int i = 0; i < (lengthNodosDaExpressao - 1); i++) {
			nomeDoObjetoDoAtributo = nomeDoObjetoDoAtributo + "." + nodosDaExpressao[i];
		}
		nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo.substring(1);
		nomeDoObjetoDoAtributo = nomeDoObjetoDoAtributo.substring(1);
		Map annBand3 = new HashMap();
		annBand3.put("value", expressao);
		annBand3.put("save-when", "none");
		annBand3.put("load-when", "none");
		buttonBind.addAnnotation("object", "default", annBand3);
		Map annBand2 = new HashMap();
		annBand2.put("value", nomeDoObjetoDonoDoAtributo);
		annBand2.put("save-when", "none");
		annBand2.put("load-when", "none");
		buttonBind.addAnnotation("objectPai", "default", annBand2);
		Map annBand = new HashMap();
		annBand.put("value", nomeDoObjetoDoAtributo);
		annBand.put("save-when", "none");
		annBand.put("load-when", "none");
		buttonBind.addAnnotation("rootObject", "default", annBand);
		final Boolean deleteEmEstruturaDiretorios = manipulacaoArquivosEstruturaDiretorios(buttonBind);

		buttonBind.addEventListener("onClick", new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				ButtonDeleteDownloadBind target = (ButtonDeleteDownloadBind) e.getTarget();
				String expressao = target.getNomeDoObjeto();
				String[] nodosDaExpressao = expressao.split("\\.");
				int lengthNodosDaExpressao = nodosDaExpressao.length;
				String nomeDoAtributo = nodosDaExpressao[lengthNodosDaExpressao - 2];
				Object object = target.getObjectPai();

				Object objetoAux = target.getRootObject();
				if (deleteEmEstruturaDiretorios) {
					try {
						if (objetoAux instanceof DocumentoDigital) {
							String nomeDoArquivo = ((DocumentoDigital) objetoAux).nome();
							String localizacaoArquivo = ((DocumentoDigital) objetoAux).caminho();
							ManipulacaoUploadUtil.getInstance().deletarArquivoArmazenado(nomeDoArquivo, localizacaoArquivo);
						} else {
							ctr.getMessagesHelper().emiteMensagemErro("Occoreu um erro ao deletar o arquivo!");
							log.error("A entidade representada pelo Objeto :" + objetoAux.toString() + "deve ser implementada pela inteface Documento Digital");
						}

					} catch (Exception e2) {
						ctr.getMessagesHelper().emiteMensagemErro("Occoreu um erro ao deletar o arquivo!");
					}
				}
				Reflexao.atualizarCampoDoObjetoParaNull(nomeDoAtributo, object, objetoAux);

				ctr.getBinder().loadAll();
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionaAnotacaoParaCampoButtonUploadBind(ButtonUploadBind buttonBind) {
		final Window window = ProCtr.findWindow(buttonBind);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		String expressao = buttonBind.getNomeDoObjeto();
		if ("".equals(expressao) || expressao == null) {
			expressao = "classecontrole.objetoAtual." + buttonBind.getId();
		}
		String[] nodosDaExpressao = expressao.split("\\.");
		int lengthNodosDaExpressao = nodosDaExpressao.length;
		String nomeDoObjetoDonoDoAtributo = "";
		for (int i = 0; i < (lengthNodosDaExpressao - 1); i++) {
			nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo + "." + nodosDaExpressao[i];
		}
		nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo.substring(1);
		String nomeDoAtributo = nodosDaExpressao[lengthNodosDaExpressao - 1];
		Map annBand3 = new HashMap();
		annBand3.put("value", nomeDoAtributo);
		annBand3.put("save-when", "none");
		annBand3.put("load-when", "none");
		buttonBind.addAnnotation("object", "default", annBand3);
		Map annBand2 = new HashMap();
		annBand2.put("value", nomeDoObjetoDonoDoAtributo);
		annBand2.put("save-when", "none");
		annBand2.put("load-when", "none");
		buttonBind.addAnnotation("objectPai", "default", annBand2);
		final Boolean uploadEmEstruturaDiretorios = manipulacaoArquivosEstruturaDiretorios(buttonBind);

		buttonBind.addEventListener("onClick", new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				ButtonUploadBind target = (ButtonUploadBind) e.getTarget();
				ctr.antesUploadDeArquivo(target);
				String expressao = target.getNomeDoObjeto();
				String[] nodosDaExpressao = expressao.split("\\.");
				int lengthNodosDaExpressao = nodosDaExpressao.length;
				String nomeDoAtributo = nodosDaExpressao[lengthNodosDaExpressao - 1];
				Media media = Fileupload.get();

				if (media == null) {
					return;
				}

				String name = ManipulacaoUploadUtil.formataNomeArquivo() + media.getName();

				if (uploadEmEstruturaDiretorios) {
					try {
						ManipulacaoUploadUtil.getInstance().armazenarArquivo(media, name);
						Object object = target.getObjectPai();
						Reflexao.atualizarCampoDoObjeto(ManipulacaoUploadUtil.PROPRIEDADE_NOME, object, name);
						Reflexao.atualizarCampoDoObjeto(ManipulacaoUploadUtil.PROPRIEDADE_CONTENT_TYPE, object, media.getContentType());
						Reflexao.atualizarCampoDoObjeto(ManipulacaoUploadUtil.LOCALIZACAO_ARQUIVO, object, ManipulacaoUploadUtil.getInstance().getCaminho_base() + ManipulacaoUploadUtil.getInstance().criarEstruturaDiretorios());
						Reflexao.atualizarCampoDoObjeto(ManipulacaoUploadUtil.PROPRIEDADE_USUARIO_LOGADO, object, ManipulacaoUploadUtil.getInstance().recuparCpfUsuarioSessaoUpload(window));
						Reflexao.atualizarCampoDoObjeto(ManipulacaoUploadUtil.PROPRIEDADE_UNIDADE_USUARIO_LOGADO, object, ManipulacaoUploadUtil.getInstance().recuparCodigoUnidadeUsuarioSessaoUpload(window));
					} catch (Exception e2) {
						ctr.getMessagesHelper().emiteMensagemErro("Ocorreu um erro no upload de arquivos!");
					}
				} else {

					byte[] arquivo = null;
					try {
						arquivo = media.getByteData();
					} catch (Exception e2) {
						int mediaSize = 1;
						if (media.inMemory()) {
							if (media.isBinary()) {
								arquivo = media.getByteData();
							} else {
								arquivo = media.getStringData().getBytes("UTF-8");
							}
						} else {
							if (media.isBinary()) {
								mediaSize = media.getStreamData().available();
								arquivo = new byte[mediaSize];
								media.getStreamData().read(arquivo, 0, mediaSize);
							} else {
								InputStreamReader is = (InputStreamReader) media.getReaderData();
								StringBuffer strBuffer = new StringBuffer(1024 * 64);
								char[] chArray = new char[512];
								int nRead;
								do {
									nRead = is.read(chArray);
									if (nRead > 0) {
										strBuffer.append(chArray, 0, nRead);
									}
								} while (nRead > 0);
								mediaSize = strBuffer.length();
								arquivo = strBuffer.toString().getBytes("UTF-8");
							}
						}

					}
					ctr.aposUpLoadDeArquivo(arquivo, media);
					Object object = target.getObjectPai();
					Reflexao.atualizarCampoDoObjeto(nomeDoAtributo, object, arquivo);
					Reflexao.atualizarCampoDoObjeto(PROPRIEDADE_NOME, object, name);
					Reflexao.atualizarCampoDoObjeto(PROPRIEDADE_APPLICATION_TYPE, object, media.getContentType());
					Reflexao.atualizarCampoDoObjeto(PROPRIEDADE_FORMATO, object, media.getFormat());
					ctr.aposUpLoadDeArquivo(arquivo, media, object);
				}

				ctr.getBinder().loadAll();
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionaAnotacaoParaCampoButtonDownloadBind(ButtonDownloadBind buttonDownloadBind) {
		Window window = ProCtr.findWindow(buttonDownloadBind);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		String expressao = buttonDownloadBind.getNomeDoObjeto();
		if ("".equals(expressao) || expressao == null) {
			expressao = "classecontrole.objetoAtual." + buttonDownloadBind.getId();
		}
		String[] nodosDaExpressao = expressao.split("\\.");
		int lengthNodosDaExpressao = nodosDaExpressao.length;
		String nomeDoObjetoDonoDoAtributo = "";
		for (int i = 0; i < (lengthNodosDaExpressao - 1); i++) {
			nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo + "." + nodosDaExpressao[i];
		}
		nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo.substring(1);
		Map annBand3 = new HashMap();
		annBand3.put("value", expressao);
		annBand3.put("save-when", "none");
		annBand3.put("load-when", "none");
		buttonDownloadBind.addAnnotation("object", "default", annBand3);
		Map annBand2 = new HashMap();
		annBand2.put("value", nomeDoObjetoDonoDoAtributo);
		annBand2.put("save-when", "none");
		annBand2.put("load-when", "none");
		buttonDownloadBind.addAnnotation("objectPai", "default", annBand2);
		final Boolean downloadEmEstruturaDiretorios = buttonDownloadBind.isEmEstruturaDeArquivos() == null ? false : buttonDownloadBind.isEmEstruturaDeArquivos();
		final Boolean downloadPdfAssinado = buttonDownloadBind.isImpAssinaturaPdf() == null ? false : buttonDownloadBind.isImpAssinaturaPdf();

		buttonDownloadBind.addEventListener("onClick", new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				ButtonDownloadBind target = (ButtonDownloadBind) e.getTarget();
				Object objetoDoArquivo = target.getObjectPai();

				if (downloadEmEstruturaDiretorios) {
					try {
						if (objetoDoArquivo instanceof DocumentoDigital) {
							String nomeDoArquivo = ((DocumentoDigital) objetoDoArquivo).nome();
							String localizacaoArquivo = ((DocumentoDigital) objetoDoArquivo).caminho();
							String contentType = ((DocumentoDigital) objetoDoArquivo).contentType();
							ManipulacaoUploadUtil.getInstance().downloadArquivo(nomeDoArquivo, localizacaoArquivo, contentType);
						} else {
							ctr.getMessagesHelper().emiteMensagemErro("Não foi possível realizar o download do arquivo!");
							log.error("A entidade representada pelo Objeto :" + objetoDoArquivo.toString() + "deve ser implementada pela inteface Documento Digital");
						}

					} catch (Exception e2) {
						ctr.getMessagesHelper().emiteMensagemErro("Não foi possível realizar o download do arquivo!");
					}
				} else {
					Arquivo arquivo = (Arquivo) objetoDoArquivo;
					byte[] object = (byte[]) target.getObject();
					String nomeDoArquivo = (String) Reflexao.recuperaValorDaPropriedade(PROPRIEDADE_NOME, objetoDoArquivo);
					String applicationType = (String) Reflexao.recuperaValorDaPropriedade(PROPRIEDADE_APPLICATION_TYPE, objetoDoArquivo);
					Filedownload.save(object, applicationType, nomeDoArquivo);
				}
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionaAnotacaoParaCampoRadiogroupBind(RadiogroupBind radiogroupBind) {
		Window window = ProCtr.findWindow(radiogroupBind);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.aplicaRegraDeNomeacaoDeComponentes(radiogroupBind);
		aplicaAnotacoesParaObject(radiogroupBind);
		aplicaAnotacoesParaObjetoPai(radiogroupBind);
		aplicaAnotacoesParaObjectRoot(radiogroupBind);
		radiogroupBind.addEventListener("onCreate", new SerializableEventListener() {
			@Override
			public void onEvent(Event event) {
				try {
					ctr.getBinder().loadComponent(event.getTarget());
					RadiogroupBind radiogroupBind = (RadiogroupBind) event.getTarget();
					if (radiogroupBind.getObjectPai() != null) {
						radiogroupBind.getItemCount();
						String[] nodos = radiogroupBind.getNomeDoObjeto().split("\\.");
						String nomeDoCampo = nodos[nodos.length - 1];
						Class tipo = Reflexao.recuperaTipoDeRetornoDoMetodoGet(nomeDoCampo, radiogroupBind.getObjectPai());
						String nomeDoTipo = tipo.getSimpleName().substring(0, 1).toUpperCase() + tipo.getSimpleName().substring(1);
						List objetos = ctr.getLista("listaDe" + nomeDoTipo);
						Components.removeAllChildren(radiogroupBind);
						Radio radio = null;
						Hlayout hlayout = new Hlayout();
						for (Object o : objetos) {
							Class classe = o.getClass();
							radio = new Radio();
							if (classe.isEnum()) {
								Enum en = (Enum) o;
								radio.setValue(en.name());
							} else {
								if (o instanceof ProBaseVO) {
									radio.setValue(((ProBaseVO) o).getId() + "");
								}
							}
							radio.setAttribute("objeto", o);
							radio.setLabel(o + "");
							radio.setDisabled(radiogroupBind.isDisabled());
							hlayout.appendChild(radio);
						}
						radiogroupBind.appendChild(hlayout);
					}
				} catch (Exception e) {
					log.error("Erro ao adicionar anotação para campo radio group", e);
				}
				ctr.getBinder().loadComponent(event.getTarget());
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionaAnotacaoParaListaBoxDet(ListboxDet listbox) {
		Window window = ProCtr.findWindow(listbox);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.aplicaRegraDeNomeacaoDeComponentes(listbox);
		aplicaAnotacoesParaObject(listbox);
		aplicaAnotacoesParaObjetoPai(listbox);
		aplicaAnotacoesParaObjectRoot(listbox);
		Map annDet = new HashMap();
		annDet.put("value", listbox.getNomeDoObjeto());
		annDet.put("converter", ListboxDetConverter.class.getName());
		listbox.addAnnotation("model", "default", annDet);
	}

	public void adicionaAnotacaoParaLista(Sylistbox campoNaTela) {
		if (campoNaTela.isSelecionavel()) {
			Map anns = new HashMap();
			anns.put("selectedItem", "classecontrole.objetoAtual");
			campoNaTela.addAnnotation("bind", anns);
		}
	}

	public void adicionaAnotacaoParaItensDaListaBoxDet(Component child) {
		if (child instanceof Listitem) {
			try {
				Map contactsAnnot = new HashMap();
				Listitem listitem = (Listitem) child;
				ListboxDet listboxDet = (ListboxDet) listitem.getListbox();
				String nodos[] = listboxDet.getNomeDoObjeto().split("\\.");
				contactsAnnot.put("each", nodos[nodos.length - 1]);
				listitem.addAnnotation("default", contactsAnnot);
			} catch (Exception e) {
				log.warn("########## Não pode adicionar anotação para ListaBoxDet");
			}
		}
	}

	public void adicionaAnotacaoParaCelula(ListCellBind campoNaTela) {
		Map ann = new HashMap();
		if (campoNaTela.getLabel().equals("objetoDaLista")) {
			ann.put("label", campoNaTela.getLabel());
		} else {
			ann.put("label", "objetoDaLista." + campoNaTela.getLabel());
		}
		campoNaTela.addAnnotation("bind", ann);
	}

	public void adicionaAnotacaoParaCampoIntbox(IntboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);
		if (comp.getMask() != null && !"".equals(comp.getMask())) {
			Clients.evalJavaScript("mascara(\"" + comp.getUuid() + "\",'" + comp.getMask() + "')");
		}
	}

	public void adicionaAnotacaoParaCampoCheckboxBind(CheckboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		String expressao = ((FieldValidator) comp).getNomeDoObjeto();
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		Map ann = new HashMap();
		String nomeDoObjeto = comp.getNomeDoObjeto();
		ann.put("value", nomeDoObjeto);
		ann.put("save-when", "self.onCheck");
		comp.addAnnotation("checked", "default", ann);
		EventListener evento = new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				CheckboxBind campoNaTela = (CheckboxBind) e.getTarget();
				GenericConstraint constraint = new GenericConstraint();
				constraint.validate(campoNaTela, campoNaTela.isChecked());
			}
		};
		comp.addEventListener("onChange", evento);
	}

	public void adicionaAnotacaoParaCampoTime(TimeboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);

	}

	public void adicionaAnotacaoParaCampoData(DateboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);
	}

	public void adicionaAnotacaoParaCampoMoney(MoneyboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		comp.setConverter(MoneyBindConverter.class.getName());
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		comp.setWidgetListener("onBind", "monetario('" + comp.getUuid() + "'," + comp.getNumeroDeCasasDecimais() + "," + comp.getLimit() + ");");
	}

	public void adicionaAnotacaoParaCampoCpf(CpfboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		comp.setConverter(CpfBindConverter.class.getName());
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);

		comp.setWidgetListener("onBind", "jq(this).mask('999.999.999-99');");

	}

	public void adicionaAnotacaoParaCampoCnpj(CnpjboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		comp.setConverter(CnpjBindConverter.class.getName());
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);

		comp.setWidgetListener("onBind", "jq(this).mask('99.999.999/9999-99');");

	}

	public void adicionaAnotacaoParaCampoCep(CepboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		comp.setConverter(CepBindConverter.class.getName());
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);

		comp.setWidgetListener("onBind", "jq(this).mask('99.999-999');");

	}

	public void adicionaAnotacaoParaCampoPlaca(PlacaboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		comp.setConverter(PlacaBindConverter.class.getName());
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);
		comp.setWidgetListener("onBind", "jq(this).mask('aaa-9999');");
		if (comp.getUpper()) {
			comp.setWidgetListener("onKeyUp", "upper(\"" + comp.getUuid() + "\")");
		}

	}

	public void adicionaAnotacaoParaCampoTelefone(TelefoneboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		comp.setConverter(TelefoneBindConverter.class.getName());
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);
		comp.setWidgetListener("onBind", "telefone(this);");
	}

	public void adicionaAnotacaoParaArgumentos(ComponentCtrl campoNaTela) {
		AbstractComponent com = (AbstractComponent) campoNaTela;
		Map ann = new HashMap();
		String propriedade = com.getId().substring(0, com.getId().length() - 3);
		ann.put("value", "classecontrole.objetoAtualArg." + propriedade);
		com.addAnnotation("bind", ann);
	}

	public void adicionaAnotacaoParaArgumentosList(ComponentCtrl campoNaTela) {
		ListboxArg com = (ListboxArg) campoNaTela;
		Map ann = new HashMap();
		String propriedade = com.getId().substring(0, com.getId().length() - 3);
		ann.put("selectedItem", "classecontrole.objetoAtualArg." + propriedade);
		com.addAnnotation("bind", ann);
		Collection colecao = (Collection) com.getAttribute(ContextParameters.PREFIX_ENUM + propriedade.substring(0, 1).toUpperCase() + propriedade.substring(1), Component.APPLICATION_SCOPE);

		if (colecao instanceof Set) {
			BindingListModelSet listSelBind = new BindingListModelSet((Set) colecao, true);
			com.setModel(listSelBind);
		} else {
			BindingListModelList listSelBind = new BindingListModelList((List) colecao, true);
			com.setModel(listSelBind);
		}

	}

	public void adicionaAnotacaoParaArgumentosListDual(Listboxdual listboxdual) {
		Window window = ProCtr.findWindow(listboxdual);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		listboxdual.setAlign("center");
		Listbox listboxOrigem = new Listbox();
		listboxOrigem.setMultiple(true);
		listboxOrigem.setWidth("400px");
		listboxOrigem.setMold("select");
		listboxOrigem.setHeight("200px");

		Vbox vbox = new Vbox();

		Button righ = new Button();
		righ.setImage("/imagens/rightrightarrow_g.png");

		Button left = new Button();
		left.setImage("/imagens/leftleftarrow_g.png");

		vbox.appendChild(righ);
		vbox.appendChild(left);
		vbox.setAlign("center");

		String expressao = listboxdual.getNomeDoObjeto();
		if ("".equals(expressao) || expressao == null) {
			expressao = "classecontrole.objetoAtual." + listboxdual.getId();
		}
		if (listboxdual.getNomeDoObjeto() == null) {
			if (((Component) listboxdual).getId() != null) {
				listboxdual.setNomeDoObjeto(((Component) listboxdual).getId());
				if (listboxdual.getNomeDoObjeto().endsWith("Arg")) {
					listboxdual.setNomeDoObjeto("classecontrole.objetoAtualArg." + listboxdual.getNomeDoObjeto().substring(0, listboxdual.getNomeDoObjeto().length() - 3));
				} else {
					listboxdual.setNomeDoObjeto("classecontrole.objetoAtual." + listboxdual.getNomeDoObjeto().substring(0, listboxdual.getNomeDoObjeto().length()));
				}
			}
		}
		String nodosDaExpressao[] = expressao.split("\\.");

		String nomeDoPai = "";

		for (int i = 0; i < nodosDaExpressao.length - 2; i++) {
			nomeDoPai = nomeDoPai + "." + nodosDaExpressao[i];
		}
		nomeDoPai = nomeDoPai.substring(1);
		Listbox listboxDestino = new Listbox();
		listboxDestino.setMultiple(true);
		listboxDestino.setWidth("400px");
		listboxDestino.setMold("select");
		listboxDestino.setHeight("200px");

		Map annBand = new HashMap();
		annBand.put("value", expressao);
		annBand.put("save-when", "none");
		annBand.put("load-when", "none");
		annBand.put("converter", "br.gov.prodigio.controle.componente.ProModelSetConverter");
		listboxDestino.addAnnotation("model", "default", annBand);

		Map annBand2 = new HashMap();
		annBand2.put("value", nomeDoPai);
		annBand2.put("save-when", "none");
		annBand2.put("load-when", "none");

		listboxdual.addAnnotation("objetoPai", "default", annBand2);

		listboxdual.setDestino(listboxDestino);
		listboxdual.setOrigem(listboxOrigem);

		listboxdual.appendChild(listboxOrigem);
		listboxdual.appendChild(vbox);
		listboxdual.appendChild(listboxDestino);

		righ.addEventListener("onClick", new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				Listboxdual listboxdual = (Listboxdual) e.getTarget().getParent().getParent();
				ctr.alternaValorParaListaDual(listboxdual.getOrigem(), listboxdual.getDestino());
			}
		});

		left.addEventListener("onClick", new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				Listboxdual listboxdual = (Listboxdual) e.getTarget().getParent().getParent();
				ctr.alternaValorParaListaDual(listboxdual.getDestino(), listboxdual.getOrigem());
			}
		});
		listboxdual.addEventListener("onCreate", new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				Listboxdual listboxdual = (Listboxdual) e.getTarget();
				ctr.inicializaDualListBoxOrigem(listboxdual);
			}

		});
	}

	protected void aplicaAnotacoesGenericas(final AbstractComponent comp) {
		String expressao = ((FieldValidator) comp).getNomeDoObjeto();
		aplicaAnotacoesParaObject(comp);

		String[] nodosDaExpressao = expressao.split("\\.");

		aplicaAnotacoesParaObjetoPai(comp);

		aplicaAnotacoesParaObjectRoot(comp);
	}

	public void adicionaElementosNoDetalhe(final DetBox detalhe) {
		Window window = ProCtr.findWindow(detalhe);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");

		Map annDet = new HashMap();
		ctr.aplicaRegraDeNomeacaoDeComponentes(detalhe);
		aplicaAnotacoesParaObject(detalhe);
		aplicaAnotacoesParaObjetoPai(detalhe);
		aplicaAnotacoesParaObjectRoot(detalhe);

		Component list = detalhe.getFirstChild();

		Grid grid = new Grid();
		detalhe.setAlign("right");
		detalhe.appendChild(grid);
		Rows rows = new Rows();
		grid.appendChild(rows);
		if (!"".equals(detalhe.getLabel())) {
			Group group = new Group();
			group.setLabel(detalhe.getLabel());
			rows.appendChild(group);
		}

		if (!ctr.detalheSomenteLeitura(detalhe)) {
			Row row = new Row();
			rows.appendChild(row);

			Div div = new Div();
			row.appendChild(div);

			div.setWidth("100%");
			div.setAlign("right");
			if (ctr.criarBotaoNovoParaDetalhe(detalhe)) {
				Button buttonNovoDetalhe = new Button();
				buttonNovoDetalhe.setLabel("Novo");
				buttonNovoDetalhe.setSclass("btn-default btn btn-novo-detalhe");
				buttonNovoDetalhe.addEventListener("onClick", new SerializableEventListener() {
					@Override
					public void onEvent(Event e) throws Exception {
						Component parent = e.getTarget().getParent();
						while (!(parent instanceof DetBox) && !(parent instanceof Window) && parent != null) {
							parent = parent.getParent();
						}
						if (!(parent instanceof DetBox)) {
							ctr.getMessagesHelper().emiteMensagemErro("Não encontrou o elemento DetBox");
						}
						ctr.novoDetalhe((DetBox) parent);
					}
				});
				div.appendChild(buttonNovoDetalhe);
			}
			if (ctr.criarBotaoExcluirParaDetalhe(detalhe)) {
				Button buttonExcluiDetalhe = new Button();
				buttonExcluiDetalhe.setLabel("Excluir");
				buttonExcluiDetalhe.setSclass("btn-default btn btn-excluir-detalhe");
				buttonExcluiDetalhe.addEventListener("onClick", new SerializableEventListener() {
					@Override
					public void onEvent(Event e) throws Exception {
						Component parent = e.getTarget().getParent();
						while (!(parent instanceof DetBox) && !(parent instanceof Window) && parent != null) {
							parent = parent.getParent();
						}
						if (!(parent instanceof DetBox)) {
							ctr.getMessagesHelper().emiteMensagemErro("Não encontrou o elemento DetBox");
						}
						ctr.excluiDetalhe((DetBox) parent);
					}
				});

				div.appendChild(buttonExcluiDetalhe);
			}
		}
		Row row2 = new Row();
		rows.appendChild(row2);
		list.setParent(row2);
		row2.appendChild(list);
	}

	protected Boolean criaBotaoNovoParaDetalhe(DetBox detalhe) {
		return true;
	}

	protected Boolean criaBotaoExcluirParaDetalhe(DetBox detalhe) {
		return true;
	}

	private Boolean manipulacaoArquivosEstruturaDiretorios(ButtonUploadBind button) {
		if (button.isEmEstruturaDeArquivos() == null) {
			return false;
		} else {
			return button.isEmEstruturaDeArquivos();
		}
	}

	public void adicionaAnotacaoParaCampoImagem(ImageUpload campoNaTela) {
		Map ann = new HashMap();
		String expressao = campoNaTela.getNomeDoObjeto();
		if ("".equals(expressao) || expressao == null) {
			expressao = "classecontrole.objetoAtual." + campoNaTela.getId();
		}
		String[] nodosDaExpressao = expressao.split("\\.");
		int lengthNodosDaExpressao = nodosDaExpressao.length;
		String nomeDoObjetoDonoDoAtributo = "";
		for (int i = 0; i < (lengthNodosDaExpressao - 1); i++) {
			nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo + "." + nodosDaExpressao[i];
		}
		nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo.substring(1);
		String nomeDoAtributo = nodosDaExpressao[lengthNodosDaExpressao - 1];
		ann.put("value", expressao);
		ann.put("converter", "br.gov.prodigio.controle.componente.ImageUploadConverter");
		campoNaTela.getImagem().addAnnotation("content", "default", ann);
		Map annBand3 = new HashMap();
		annBand3.put("content", nomeDoAtributo);
		annBand3.put("save-when", "none");
		annBand3.put("load-when", "none");
		Map annBand2 = new HashMap();
		annBand2.put("value", nomeDoObjetoDonoDoAtributo);
		annBand2.put("save-when", "none");
		annBand2.put("load-when", "none");
		campoNaTela.addAnnotation("objectPai", "default", annBand2);

	}

	public void adicionaAnotacaoParaCampoInt(IntboxBind comp) {
		Window window = ProCtr.findWindow(comp);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		comp.setConverter("br.gov.prodigio.controle.componente.IntBindConverter");
		ctr.aplicaRegraDeNomeacaoDeComponentes(comp);
		aplicaAnotacoesParaObject(comp);
		aplicaAnotacoesParaObjetoPai(comp);
		aplicaAnotacoesParaObjectRoot(comp);
	}

	public void adicionaAnotacaoParaArgumentosRadiogroup(RadiogroupArg radiogroupArg) {
		Window window = ProCtr.findWindow(radiogroupArg);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		Map ann = new HashMap();
		String propriedade = radiogroupArg.getId().substring(0, radiogroupArg.getId().length() - 3);
		Map annBand = new HashMap();
		annBand.put("value", "classecontrole.objetoAtualArg." + propriedade);
		annBand.put("converter", "br.gov.prodigio.controle.componente.ProRadiogroupArgSelectItemConverter");
		radiogroupArg.addAnnotation("selectedItem", "default", annBand);

		radiogroupArg.addEventListener("onCreate", new SerializableEventListener() {
			@Override
			public void onEvent(Event e) throws Exception {
				RadiogroupArg radiogroupArg = (RadiogroupArg) e.getTarget();
				radiogroupArg.getItemCount();
				String nomeDoCampo = radiogroupArg.getId();
				nomeDoCampo = nomeDoCampo.replace("Arg", "");
				Class tipo = Reflexao.recuperaTipoDeRetornoDoMetodoGet(nomeDoCampo, ctr.getObjetoAtualArg());
				nomeDoCampo = tipo.getSimpleName().substring(0, 1).toUpperCase() + tipo.getSimpleName().substring(1);
				List objetos = ctr.getLista("listaDe" + nomeDoCampo);
				Components.removeAllChildren(radiogroupArg);
				Radio radio = null;
				Hlayout hlayout = new Hlayout();
				for (Object o : objetos) {
					Class classe = o.getClass();
					radio = new Radio();
					if (classe.isEnum()) {
						Enum en = (Enum) o;
						radio.setValue(en.name());
					} else {
						if (o instanceof ProBaseVO) {
							radio.setValue(((ProBaseVO) o).getId() + "");
						}
					}
					radio.setAttribute("objeto", o);
					radio.setLabel(o + "");
					hlayout.appendChild(radio);
				}
				radiogroupArg.appendChild(hlayout);
			}
		});

	}

	private static ProAnnotateDataBinderHelper proAnnotateInstanceHelper;
	private static Map<Class, ProAnnotateDataBinderHelper> proAnnotateHelperInstanceMap = new HashMap<Class, ProAnnotateDataBinderHelper>();

	public static ProAnnotateDataBinderHelper getInstance() {
		if (proAnnotateInstanceHelper == null) {
			return create();
		} else {
			return proAnnotateInstanceHelper;
		}
	}

	public static ProAnnotateDataBinderHelper getInstance(Class controle) {
		ProAnnotateDataBinderHelper proAnnotateEspecializadoHelper = proAnnotateHelperInstanceMap.get(controle);
		if (proAnnotateEspecializadoHelper == null) {
			return create(controle);
		} else {
			return proAnnotateEspecializadoHelper;
		}
	}

	private synchronized static ProAnnotateDataBinderHelper create() {
		if (proAnnotateInstanceHelper == null) {
			proAnnotateInstanceHelper = new ProAnnotateDataBinderHelper();
		}
		return proAnnotateInstanceHelper;
	}

	private synchronized static ProAnnotateDataBinderHelper create(Class controle) {
		if (controle == Object.class) {
			return null;
		}
		ProAnnotateDataBinderHelper proAnnotateEspecializadoHelper = proAnnotateHelperInstanceMap.get(controle);
		if (proAnnotateEspecializadoHelper == null) {
			String nomeClasse = controle.getName();
			String nomeClasseProAnnotate = nomeClasse.replace("Ctr", "BinderHelper");
			try {
				proAnnotateEspecializadoHelper = (ProAnnotateDataBinderHelper) Class.forName(nomeClasseProAnnotate).newInstance();
			} catch (Exception e) {
				try {
					nomeClasseProAnnotate = nomeClasse.replace(".controle.", ".controle.componente.").replace("Ctr", "AnnotateDataBinderHelper");
					log.debug("Instanciando classe AnnotateDataBinder {} para a classe de controle {} ", nomeClasseProAnnotate, nomeClasse);
					try {
						proAnnotateEspecializadoHelper = (ProAnnotateDataBinderHelper) Class.forName(nomeClasseProAnnotate).newInstance();
					} catch (Exception e1) {
						nomeClasseProAnnotate = nomeClasse.replace(".controle.", ".binder.").replace("Ctr", "AnnotateDataBinderHelper");
						proAnnotateEspecializadoHelper = (ProAnnotateDataBinderHelper) Class.forName(nomeClasseProAnnotate).newInstance();
					}
				} catch (Exception e2) {
					controle = controle.getSuperclass();
					log.debug("Não foi possível criar a classe {}. Tentando AnnotateDataBinder para a classe pai {}", nomeClasseProAnnotate, controle);
					proAnnotateEspecializadoHelper = create(controle);
				}
			}
			proAnnotateHelperInstanceMap.put(controle, proAnnotateEspecializadoHelper);
		}

		return proAnnotateEspecializadoHelper;
	}

	public void adicionaAnotacaoParaBarcodeImagem(CodigoDeBarrasBind codigoDeBarrasBind) {

		String expressao = codigoDeBarrasBind.getNomeDoObjeto();
		Map ann = new HashMap();
		ann.put("value", expressao);
		ann.put("converter", CodigoDeBarrasConverter.class.getName());
		codigoDeBarrasBind.getImagem().addAnnotation("content", "default", ann);

		String[] nodosDaExpressao = expressao.split("\\.");
		int lengthNodosDaExpressao = nodosDaExpressao.length;
		String nomeDoObjetoDonoDoAtributo = "";
		for (int i = 0; i < (lengthNodosDaExpressao - 1); i++) {
			nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo + "." + nodosDaExpressao[i];
		}
		nomeDoObjetoDonoDoAtributo = nomeDoObjetoDonoDoAtributo.substring(1);

		Map annBand2 = new HashMap();
		annBand2.put("value", nomeDoObjetoDonoDoAtributo);
		annBand2.put("save-when", "none");
		annBand2.put("load-when", "none");
		codigoDeBarrasBind.addAnnotation("objectPai", "default", annBand2);

		String nomeDoAtributo = nodosDaExpressao[lengthNodosDaExpressao - 1];
		Map annBand3 = new HashMap();
		annBand3.put("content", nomeDoAtributo);
		annBand3.put("save-when", "none");
		annBand3.put("load-when", "none");

	}

}
