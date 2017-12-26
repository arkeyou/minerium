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

import java.util.Map;

import org.zkoss.zk.ui.Component;

public interface FieldValidator {

	public abstract Boolean getValidarRegra();

	public abstract void setValidarRegra(Boolean validarRegra);

	public abstract Boolean getValidarCampo();

	public abstract void setValidarCampo(Boolean validarCampo);

	public abstract String getNomeDoObjeto();

	public abstract void setNomeDoObjeto(String nomeDoObjeto);

	public abstract Object getObject();

	public abstract void setObject(Object o);

	public abstract Object getObjectPai();

	public abstract void setObjectPai(Object o);

	public abstract Object getRootObject();

	public abstract void setRootObject(Object o);

	public abstract String getConverter();

	public abstract void setConverter(String o);

	public String getDependeDoComponente();

	public void setDependeDoComponente(String dependeDoComponent);

	public Map<String, Component> recuperaComponentesDependentes();

	void addComponentesDependentes(String nomeDoObjeto, Component field);

	public void setIdentificador(String identificador);

	public String getIdentificador();

	public String getAtributoQueSeraVisualizado();

	public void setAtributoQueSeraVisualizado(String atributoQueSeraVisualizado);

	public String getValidarQuando();

	public void setValidarQuando(String validarQuando);

}
