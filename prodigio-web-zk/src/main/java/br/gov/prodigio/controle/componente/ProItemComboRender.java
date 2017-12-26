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

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import br.gov.prodigio.entidades.ProBaseVO;

public class ProItemComboRender implements ComboitemRenderer {
	public void render(Comboitem comboitem, Object data, int index) throws Exception {
		ProBaseVO appBaseVO = (ProBaseVO) data;
		comboitem.setLabel(appBaseVO.toString());
		comboitem.setValue(appBaseVO);
		if (appBaseVO.getTitulo() == null)
			comboitem.setDescription("Titulo: vazio");
		else
			comboitem.setDescription(appBaseVO.getTitulo());
	}
}
