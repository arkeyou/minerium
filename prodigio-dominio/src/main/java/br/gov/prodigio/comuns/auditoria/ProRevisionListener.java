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
package br.gov.prodigio.comuns.auditoria;

//import org.hibernate.envers.RevisionListener;

import br.gov.prodigio.comuns.utils.Context;
import br.gov.prodigio.entidades.ProVO;

public class ProRevisionListener /*implements RevisionListener*/ {

	/*@Override
	public void newRevision(Object arg0) {

		ProAuditoriaVO auditoria = (ProAuditoriaVO) arg0;
		ProVO appVO = (ProVO) Context.getAttribute("usuarioLogado");
		if(appVO == null)
			appVO = (ProVO) Context.getAttribute("objetoAtual");
		auditoria.setCdLoginUsuario(appVO.getCdLoginMovimentacao());

	}*/
}
