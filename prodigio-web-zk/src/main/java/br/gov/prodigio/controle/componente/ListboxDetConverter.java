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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import jxl.common.Logger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.ListModelConverter;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListModelSet;

import br.gov.prodigio.comuns.utils.Reflexao;

public class ListboxDetConverter extends ListModelConverter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1772863563056537711L;

	@Override
	public Object coerceToBean(Object val, Component comp) {
		return super.coerceToBean(val, comp);
	}

	@Override
	public Object coerceToUi(Object val, Component comp) {
		if(val!=null){
			
			
			AuxheadFilter head = getHeaderFilter(comp);
			if(head!=null){
				boolean erroAoFiltrar = false;
				boolean filtroVazio = true;
				
				Collection lista = (Collection) val;
				List listaFiltrada = new ArrayList();
				List<Component> children = head.getChildren();
				formaior:
				for(Object objetoAtual: lista){					
					for (Component component : children) {
						if(component instanceof AuxheaderFilter){
							AuxheaderFilter headerFilter = (AuxheaderFilter)component;
							try {
								if(headerFilter.getInputTextBox()!=null && headerFilter.getInputTextBox().getValue()!=null) {
									String value = headerFilter.getInputTextBox().getValue();
									Object valorDoMetodo = Reflexao.recuperaValorDaPropriedade(headerFilter.getCampoFiltrado(), objetoAtual);
									if(valorDoMetodo instanceof String){
										if(!"".equals(value.trim())){
											filtroVazio = false;
											
											if(valorDoMetodo!=null && ((String)valorDoMetodo).toUpperCase().contains(value.toUpperCase())){
												listaFiltrada.add(objetoAtual);
											}											
										}
										
									} else {
										Logger.getLogger(getClass()).error("o campo filtrado ["+headerFilter.getCampoFiltrado()
												+"] deve ser do tipo java.lang.String");
										erroAoFiltrar = true;
										break formaior;
									}
								}
							} catch (Exception e) {
								Logger.getLogger(getClass()).error("erro ao recuperar valor do método ["+headerFilter.getCampoFiltrado()
																+"] para o objeto ["+objetoAtual.getClass()+"]", e);
								erroAoFiltrar = true;
								break formaior;
							}
							
						}
					}
					
					if(filtroVazio == true){
						break formaior;
					}
				}				
				
				if(!erroAoFiltrar && !filtroVazio){
					val = listaFiltrada;
				}
				
			}
			
			
			
			if(val instanceof Set)
				return new ListModelSet((Set)val);
			if(val instanceof List)
				return new ListModelList((List)val);
		}
		return super.coerceToUi(val, comp);
	}

	protected AuxheadFilter getHeaderFilter(Component comp) {
		List<Component> children = comp.getChildren();
		AuxheadFilter head = null;
		if(children!=null){			
			for (Component component : children) {
				if(component instanceof AuxheadFilter){
					head = (AuxheadFilter)component;
					break;
				}
			}
		}
		return head;
	}
}
