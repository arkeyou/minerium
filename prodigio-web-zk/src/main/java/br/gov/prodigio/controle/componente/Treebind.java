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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import br.gov.prodigio.controle.ProCtr;
import br.gov.prodigio.entidades.TreeNodo;

public class Treebind extends Tree implements AfterCompose {
	private static final Logger log = LoggerFactory.getLogger(Treebind.class);
	private ProCtr ctr;
	private TreeNodo nodoRaiz;
	private TreeNodo nodoSelecionado;

	@Override
	public void afterCompose() {
		Window window = ProCtr.findWindow(this);
		ctr = (ProCtr) window.getAttribute(window.getId() + "$" + "composer");
	}

	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		EventListener evtnm = new EventListener() {
			public void onEvent(Event e) throws Exception {
				inicializa();
			}
		};
		this.addEventListener("onCreate", evtnm);
	}

	public void inicializa() {
		try {
			Treeitem treeitem = inicializaNodoRaiz();
			insereFilhos(treeitem, nodoRaiz.getNodosFilhos());
		} catch (Exception e) {
			log.error("Erro ao inicializar componente ", e);
		}
	}

	protected void insereFilhos(Treeitem treeitem, List<TreeNodo> filhos) {
		if (filhos != null) {
			for (TreeNodo nodoFilho : filhos) {
				if (nodoFilho.isDiretorio()) {
					addDirectory(nodoFilho.getNome(), treeitem, (TreeNodo) nodoFilho);
				} else {
					addFile(nodoFilho.getNome(), treeitem, (TreeNodo) nodoFilho);
				}
			}
		}
	}

	protected void addFile(final String nomeFile, Component com, TreeNodo treeNodo) {
		Treechildren treechildren = ((Treeitem) com).getTreechildren();
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
		atribuiEventoAoClickarNoItem(treeitem2);
		com.appendChild(treechildren);
	}

	/**
	 * @param treeitem2
	 */
	private void atribuiEventoAoClickarNoItem(Treeitem treeitem2) {
		EventListener evtnm = new EventListener() {
			public void onEvent(Event e) throws Exception {
				Treeitem treeitem = (Treeitem) e.getTarget();
				setNodoSelecionado((TreeNodo) treeitem.getValue());
				Tree tree = treeitem.getTree();
				ctr.getBinder().saveComponent(tree);
			}
		};
		treeitem2.addEventListener(Events.ON_CLICK, evtnm);
	}

	protected Treeitem addDirectory(final String nomePasta, Treeitem treeitemRaiz, TreeNodo treeNodo) {
		Treechildren treechildren = treeitemRaiz.getTreechildren();
		if (treeitemRaiz.getTreechildren() == null) {
			treechildren = new Treechildren();
		}
		Treeitem treeitem = new Treeitem();
		treeitem.setOpen(false);
		treeitem.setSelected(false);
		treeitem.setValue(treeNodo);
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
		List<TreeNodo> filhos = treeNodo.getNodosFilhos();
		atribuiEventoAoClickarNoItem(treeitem);
		if (filhos != null) {
			for (TreeNodo nodo : filhos) {
				if (nodo.isDiretorio()) {
					addDirectory(nodo.getNome(), treeitem, nodo);
				} else {
					addFile(nodo.getNome(), treeitem, nodo);
				}
			}
		}

		EventListener evtnm = new EventListener() {
			public void onEvent(Event e) throws Exception {
			}
		};
		treeitem.addEventListener("onOpen", evtnm);
		return treeitem;
	}

	protected Treeitem inicializaNodoRaiz() {
		Treechildren treechildren = this.getTreechildren();
		if (this.getTreechildren() == null) {
			treechildren = new Treechildren();
		}
		this.appendChild(treechildren);

		Treecell treecell = new Treecell();
		Treerow treerow = new Treerow();
		Treeitem treeitem = new Treeitem();
		treecell.setLabel(nodoRaiz.getNome());
		treerow.appendChild(treecell);
		treeitem.appendChild(treerow);
		treechildren.appendChild(treeitem);

		return treeitem;
	}

	@Override
	public void setSelectedItem(Treeitem item) {
		super.setSelectedItem(item);
	}

	public TreeNodo getNodoRaiz() {
		return nodoRaiz;
	}

	public void setNodoRaiz(TreeNodo nodoRaiz) {
		this.nodoRaiz = nodoRaiz;
	}

	public TreeNodo getNodoSelecionado() {
		return nodoSelecionado;
	}

	public void setNodoSelecionado(TreeNodo nodoSelecionado) {
		this.nodoSelecionado = nodoSelecionado;
	}

}
