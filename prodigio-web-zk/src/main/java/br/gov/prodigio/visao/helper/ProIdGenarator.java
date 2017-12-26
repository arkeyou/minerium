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

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.metainfo.Property;
import org.zkoss.zk.ui.sys.IdGenerator;
import org.zkoss.zul.Window;

public class ProIdGenarator implements IdGenerator {

	public String nextComponentUuid(Desktop desktop, Component comp) {
		int i = Integer.parseInt(desktop.getAttribute("Id_Num").toString());
		i++;
		desktop.setAttribute("Id_Num", String.valueOf(i));
		return "zk_comp_" + i;
	}

	public String nextDesktopId(Desktop desktop) {
		if (desktop.getAttribute("Id_Num") == null) {
			String number = "0";
			desktop.setAttribute("Id_Num", number);
		}
		return null;
	}

	public String nextPageUuid(Page page) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.zkoss.zk.ui.sys.IdGenerator#nextComponentUuid(org.zkoss.zk.ui.Desktop, org.zkoss.zk.ui.Component, org.zkoss.zk.ui.metainfo.ComponentInfo)
	 */
	@Override
	public String nextComponentUuid(Desktop desktop, Component comp, ComponentInfo arg2) {
		String property = System.getProperty("environment");
		if (property != null && property.equals("test")) {
			Integer i = null;
			Component parent = comp.getParent();
			if (parent != null && parent.getId().equals("meio") && comp instanceof Window) {
				i = Integer.parseInt(desktop.getAttribute("Id_Num").toString());
				Integer iAux = (Integer) parent.getAttribute("numero_id_inicial_da_tela");
				if (iAux == null) {
					parent.setAttribute("numero_id_inicial_da_tela", i);
				} else {
					i = iAux;
				}
			} else {
				i = Integer.parseInt(desktop.getAttribute("Id_Num").toString());
			}
			if (arg2 != null) {
				i++;

				StringBuilder uuid = new StringBuilder("");

				desktop.setAttribute("Id_Num", String.valueOf(i));
				if (arg2 != null) {
					String id = getId(arg2);
					if (id != null) {
						uuid.append(id).append("_");
					}
					String tag = arg2.getTag();
					if (tag != null) {
						uuid.append(tag).append("_");
					}
				}
				return uuid.length() == 0 ? "zkcomp_" + i : uuid.append(i).toString();
			} else {
				i++;
				StringBuilder uuid = new StringBuilder("");
				desktop.setAttribute("Id_Num", String.valueOf(i));
				if (comp != null) {
					String id = comp.getId();
					if (id != null) {
						uuid.append(id).append("_");
					}
					String tag = comp.getClass().getSimpleName().toLowerCase();
					if (tag != null) {
						uuid.append(tag).append("_");
					}
				}
				return uuid.length() == 0 ? "zkcomp_" + i : uuid.append(i).toString();
			}
		} else {
			return null;
		}
	}

	public String getId(ComponentInfo compInfo) {
		List<Property> properties = compInfo.getProperties();
		for (Property property : properties) {
			if ("id".equals(property.getName())) {
				return property.getRawValue();
			}
		}
		return null;
	}

}
