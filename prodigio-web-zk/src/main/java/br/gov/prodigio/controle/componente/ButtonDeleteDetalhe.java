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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import br.gov.prodigio.comuns.utils.DocumentoDigital;
import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.utils.ManipulacaoUploadUtil;

public class ButtonDeleteDetalhe extends Button implements AfterCompose {

	private String mensagem = "Deseja realmente excluir o registro selecionado?";
	private Boolean emEstruturaDeArquivos;
	public static final Logger log = LoggerFactory.getLogger("ButtonDeleteDetalhe");

	@Override
	public void afterCompose() {
		setWidth("30px");
		Window window = ProCtr.findWindow(this);
		final ProCtr ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
		addEventListener(Events.ON_CLICK, new SerializableEventListener() {
			public void onEvent(Event e) throws Exception {
				if ((ctr.getMessagesHelper().desejaRealmenteExcluir(getMensagem()))) {
					Component com = e.getTarget().getParent();
					Listbox listbox = null;
					Listitem listitem = null;
					while (com != null && !com.getClass().equals(DetBox.class)) {
						if (com instanceof Listitem) {
							listitem = (Listitem) com;
						}
						if (com instanceof Listbox) {
							listbox = (Listbox) com;
						}
						com = com.getParent();
					}
					listitem.setSelected(true);
					listbox.setSelectedItem(listitem);
					try {
						manipulacaoDelecaoArquivoEstruturaDiretorio((ButtonDeleteDetalhe) e.getTarget(), listbox);
						ctr.excluiDetalhe((DetBox) com);
					} catch (Exception e2) {
						ctr.getMessagesHelper().emiteMensagemErro("Occoreu um erro ao deletar o arquivo!");
					}
				}
			}
		});
	}

	private void manipulacaoDelecaoArquivoEstruturaDiretorio(ButtonDeleteDetalhe button, Listbox listbox) {
		final Boolean deleteEmEstruturaDiretorios = manipulacaoArquivosEstruturaDiretorios(button);

		if (deleteEmEstruturaDiretorios) {
			int indexASerExcluido = listbox.getSelectedIndex();
			Object objectASerExcluido = listbox.getModel().getElementAt(indexASerExcluido);

			if (objectASerExcluido instanceof DocumentoDigital) {
				String nomeDoArquivo = ((DocumentoDigital) objectASerExcluido).nome();
				String localizacaoArquivo = ((DocumentoDigital) objectASerExcluido).caminho();
				ManipulacaoUploadUtil.getInstance().deletarArquivoArmazenado(nomeDoArquivo, localizacaoArquivo);

			} else {
				log.error("A entidade representada pelo Objeto de upload de arquivos deve ser implementada pela inteface Documento Digital");
				throw new IllegalArgumentException();
			}

		}
	}

	private Boolean manipulacaoArquivosEstruturaDiretorios(ButtonDeleteDetalhe button) {
		if (button.getEmEstruturaDeArquivos() == null) {
			return false;
		} else {
			return button.getEmEstruturaDeArquivos();
		}
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Boolean getEmEstruturaDeArquivos() {
		return emEstruturaDeArquivos;
	}

	public void setEmEstruturaDeArquivos(Boolean emEstruturaDeArquivos) {
		this.emEstruturaDeArquivos = emEstruturaDeArquivos;
	}

}
