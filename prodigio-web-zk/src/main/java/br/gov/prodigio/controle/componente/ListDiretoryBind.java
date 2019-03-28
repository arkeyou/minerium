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

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.entidades.ArquivoVO;

public class ListDiretoryBind extends Tree {
	private static final Logger log = LoggerFactory.getLogger(ListDiretoryBind.class);
	final String ARQUIVO_ASSOCIADO = "arquivoVO";
	private String caminhoAbsoluto;
	private String titulo;
	private String tipoArquivoVO;
	private ProCtr ctr;

	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		EventListener evtnm = new SerializableEventListener() {
			public void onEvent(Event e) throws Exception {
				Window window = ProCtr.findWindow(e.getTarget());
				ctr = (ProCtr) window.getAttribute("$composer");
				inicializa((Tree) e.getTarget());
			}
		};
		this.addEventListener("onCreate", evtnm);
	}

	public void inicializa(Tree tree) {
		try {
			ArquivoVO example = null;
			final String tipoArquivoVO2 = getTipoArquivoVO();
			if (tipoArquivoVO2 != null && !"".equals(tipoArquivoVO2)) {
				final Class<?> forName = Class.forName(tipoArquivoVO2);
				example = (ArquivoVO) forName.newInstance();
			}
			ctr.montaExemploParaPesquisaDeArquivos(example);
			Treecell treecell = new Treecell();
			Treerow treerow = new Treerow();
			Treeitem treeitem = new Treeitem();
			final String titulo = this.titulo;
			Treechildren treechildren = addDirectory(tree);

			treecell.setLabel(titulo);
			treerow.appendChild(treecell);
			treeitem.appendChild(treerow);
			treechildren.appendChild(treeitem);

			Set<ArquivoVO> set = ctr.repositorio().listarBaseadoNoExemplo(example, example, 0, 2, "");
			final ArquivoVO arquivoVO = (ArquivoVO) set.toArray()[0];
			ctr.setObjetoAtual(arquivoVO);
			List<ArquivoVO> list = (List) arquivoVO.getArquivosFilhos();

			insereFilhos(treeitem, list);
		} catch (Exception e) {
			log.error("Erro ao inicializar componente ", e);
			ctr.getMessagesHelper().emiteMensagemErro(e.getMessage());
		}
	}

	protected void insereFilhos(Treeitem treeitem, List<ArquivoVO> filhos) {
		if (filhos == null) {
			return;
		}
		Clients.showBusy("Processando ...");
		for (ArquivoVO arquivoVO : filhos) {
			if (arquivoVO.isDiretorio()) {
				addDirectory(arquivoVO.getNome() + " - " + arquivoVO.getTitulo(), treeitem, arquivoVO);
			} else {
				addFile(arquivoVO.getNome(), treeitem, arquivoVO);
			}
		}
		Clients.clearBusy();
	}

	protected void addFile(final String nomeFile, Component com, ArquivoVO arquivoVO) {
		Treechildren treechildren = null;
		if (com instanceof Tree) {
			treechildren = ((Tree) com).getTreechildren();
		} else {
			treechildren = ((Treeitem) com).getTreechildren();
		}
		if (treechildren == null) {
			treechildren = new Treechildren();
		}
		Treeitem treeitem2 = new Treeitem();
		Treerow treerow2 = new Treerow();
		Treecell treecell2 = new Treecell();
		treecell2.setLabel(nomeFile);

		treerow2.appendChild(treecell2);
		treeitem2.appendChild(treerow2);
		treechildren.appendChild(treeitem2);
		EventListener evtnm = new SerializableEventListener() {
			public void onEvent(Event e) throws Exception {
				Treeitem treeitem = (Treeitem) e.getTarget();
				ArquivoVO arquivoVO = (ArquivoVO) treeitem.getAttribute(ARQUIVO_ASSOCIADO);
				arquivoVO = ctr.repositorio().recuperaObjeto(arquivoVO);
				ctr.imprimePadrao(arquivoVO);
			}
		};
		treeitem2.addEventListener(Events.ON_DOUBLE_CLICK, evtnm);
		treeitem2.setAttribute(ARQUIVO_ASSOCIADO, arquivoVO, COMPONENT_SCOPE);

		Menupopup menupopup = ctr.criaMenuParaListDiretoryBind(treecell2);

		treecell2.setContext(menupopup);

		com.appendChild(treechildren);
	}

	protected Treeitem addDirectory(final String nomePasta, Treeitem treeitemRaiz, ArquivoVO arquivoVO) {
		Treechildren treechildren = treeitemRaiz.getTreechildren();
		if (treeitemRaiz.getTreechildren() == null) {
			treechildren = new Treechildren();
		}
		Treeitem treeitem = new Treeitem();
		treeitem.setOpen(false);
		treeitem.setSelected(false);
		treeitem.setAttribute(ARQUIVO_ASSOCIADO, arquivoVO, Component.COMPONENT_SCOPE);
		Treerow treerow = new Treerow();
		Treecell treecell = new Treecell();
		treecell.setLabel(nomePasta);
		treerow.appendChild(treecell);
		treeitem.appendChild(treerow);
		treechildren.appendChild(treeitem);
		treeitemRaiz.appendChild(treechildren);
		Treechildren treechildren2 = new Treechildren();
		treeitem.appendChild(treechildren2);
		treeitem.setOpen(false);
		List<ArquivoVO> filhos = (List<ArquivoVO>) arquivoVO.getArquivosFilhos();
		if (filhos != null) {
			for (ArquivoVO arquivoVOFilho : filhos) {
				if (arquivoVOFilho.isDiretorio()) {
					addDirectory(arquivoVOFilho.getNome() + " - " + arquivoVOFilho.getTitulo(), treeitem, arquivoVOFilho);
				} else {
					addFile(arquivoVOFilho.getNome(), treeitem, arquivoVOFilho);
				}
			}
		}

		EventListener evtnm = new SerializableEventListener() {
			public void onEvent(Event e) throws Exception {
				/*
				 * final String ATUALIZADO = "atualizadoNatela"; Treeitem treeitem = (Treeitem) e.getTarget(); ArquivoVO arquivoVO = (ArquivoVO) treeitem.getAttribute(ARQUIVO_ASSOCIADO); List<ArquivoVO> filhos = arquivoVO.getArquivosFilhos();
				 * arquivoVO.setCaminhoAbsoluto(arquivoVO.getCaminhoAbsoluto()); if (!arquivoVO.isFilhosCarregados()) { arquivoVO.setUsuarioVO(ctr.getUsuarioVO()); arquivoVO.setUnidadeAdministrativaVO(ctr.getUnidadeAdministrativaVO()); Set<ArquivoVO>
				 * set = ctr.getFachadaDeNegocio().listarBaseadoNoExemplo(arquivoVO, arquivoVO, 0, 2, ""); filhos = (List) ((ArquivoVO) set.toArray()[0]).getArquivosFilhos(); arquivoVO.setArquivosFilhos(filhos); } Boolean atualizado = (Boolean)
				 * treeitem.getAttribute(ATUALIZADO); if (atualizado == null) { insereFilhos(treeitem, filhos); treeitem.setAttribute(ATUALIZADO, true); }
				 */
			}
		};
		treeitem.addEventListener("onOpen", evtnm);
		return treeitem;
	}

	protected Treechildren addDirectory(Tree tree) {
		Treechildren treechildren = tree.getTreechildren();
		if (tree.getTreechildren() == null) {
			treechildren = new Treechildren();
		}
		tree.appendChild(treechildren);

		return treechildren;
	}

	public String getCaminhoAbsoluto() {
		return caminhoAbsoluto;
	}

	public void setCaminhoAbsoluto(String caminhoAbsoluto) {
		this.caminhoAbsoluto = caminhoAbsoluto;
	}

	public String getTipoArquivoVO() {
		return tipoArquivoVO;
	}

	public void setTipoArquivoVO(String tipoArquivoVO) {
		this.tipoArquivoVO = tipoArquivoVO;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
