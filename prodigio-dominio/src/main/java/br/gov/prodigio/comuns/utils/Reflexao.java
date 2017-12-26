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
package br.gov.prodigio.comuns.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.entidades.ProBaseVO;

@SuppressWarnings("rawtypes")
public class Reflexao {
	private static final Logger log = LoggerFactory.getLogger(Reflexao.class);
	private static Map<String, Class> mapaDeTiposPrimitivos = new HashMap<String, Class>();
	static {
		mapaDeTiposPrimitivos.put("int", Integer.class);
		mapaDeTiposPrimitivos.put("long", Long.class);
		mapaDeTiposPrimitivos.put("double", Double.class);
		mapaDeTiposPrimitivos.put("float", Float.class);
		mapaDeTiposPrimitivos.put("boolean", Boolean.class);
		mapaDeTiposPrimitivos.put("char", Character.class);
		mapaDeTiposPrimitivos.put("byte", Byte.class);
		mapaDeTiposPrimitivos.put("void", Void.class);
		mapaDeTiposPrimitivos.put("short", Short.class);
	}

	/**
	 * Método responsável por recuperar o nome do atributo associado ao método.
	 * <p>
	 * Ex.:<br>
	 * <code> Method metodo = ArquivoVO.class.getDeclaredMethod("getNome", null); </code> <br>
	 * <code> recuperaNomeDoAtributo(metodo)</code>
	 * <p>
	 * O código acima ira retornar a String <code>"nome"</code>
	 * 
	 * @param method
	 *            Nome do método que se deseja recuperar o atributo
	 * @return nome do atributo
	 */
	public static String recuperaNomeDoAtributo(Method method) {
		String nomeMetodo = null;
		try {
			nomeMetodo = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nomeMetodo;
	}

	/**
	 * Método recursivo que atualiza a propriedade do objeto de acordo com o argumento.<br>
	 * Se a propriedade for simples é recuperado o método set da propriedade é o valor e atualizado.<br>
	 * Se a propriedade for composta o método é chamado recursivamente para recuperar a propriedade do valor a ser alterado.
	 * 
	 * @param propriedadade
	 *            Propriedade do objeto à ser atualizado
	 * @param objeto
	 *            Objeto que contém o atributo a ser atualizado
	 * @param argumento
	 *            Valor a ser atribuído no propriedade
	 * @throws Exception
	 *             se o atributo/objeto não for encontrado.
	 */
	public static void atualizarCampoDoObjeto(String propriedadade, Object objeto, Object argumento) {
		try {
			if (propriedadade.contains(".")) {
				Object agregadoVO = null;
				String[] objetoAgregado = propriedadade.split("\\.");
				agregadoVO = Reflexao.recuperaValorDaPropriedade(objetoAgregado[0], objeto);
				Class c = null;
				if (agregadoVO == null) {
					c = Reflexao.recuperaTipoDeRetornoDoMetodoGet(objetoAgregado[0], objeto);
					// Proxy.getProxyClass(arg0, arg1)
					agregadoVO = c.newInstance();
				} else {
					c = agregadoVO.getClass();
				}
				if (Collection.class.isAssignableFrom(c)) {
					return;
				}
				Class[] classes = new Class[1];
				classes[0] = agregadoVO.getClass();
				// Method metodo2 = objeto.getClass().getMethod("set" + objetoAgregado[0].substring(0, 1).toUpperCase()
				// + objetoAgregado[0].substring(1),classes);
				Method metodo2 = retornaMetodoSet(objetoAgregado[0], objeto, agregadoVO);
				metodo2.invoke(objeto, agregadoVO);
				String propriedadeAux = propriedadade.replace(objetoAgregado[0] + ".", "");
				atualizarCampoDoObjeto(propriedadeAux, agregadoVO, argumento);

			} else {
				Class[] classes = new Class[1];
				Class class1 = null;
				if (argumento != null) {
					if (argumento instanceof Set) {
						classes[0] = Set.class;
					} else {
						class1 = argumento.getClass();
						while (class1.getSimpleName().contains("$$")) {// para tratar classes proxy hibernate
							class1 = class1.getSuperclass();
						}
						classes[0] = class1;
					}
				} else {
					return;
				}

				if (objeto != null && Collection.class.isAssignableFrom(objeto.getClass())) {
					if (objeto instanceof HashSet) {
						objeto = ((HashSet) objeto).iterator().next();

					}

				}

				Method metodo2 = recuperaMetodoNaHierarquia(propriedadade, objeto, classes);
				metodo2.invoke(objeto, argumento);
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro ao tentar atualizar objeto", e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	private static Method recuperaMetodoNaHierarquia(String propriedadade, Object objeto, Class[] classes) {
		Method[] methods = objeto.getClass().getMethods();
		Method metodo2 = null;
		for (Method method : methods) {
			if (method.getName().equals("set" + propriedadade.substring(0, 1).toUpperCase() + propriedadade.substring(1))) {
				Class<?> classeDoParametro = method.getParameterTypes()[0];
				if (classeDoParametro.isPrimitive()) {
					Class classeDoTipoPrimitivo = mapaDeTiposPrimitivos.get(classeDoParametro.getSimpleName());
					if (classeDoTipoPrimitivo.isAssignableFrom(classes[0])) {
						metodo2 = method;
						break;
					}
				} else {
					if (classeDoParametro.isAssignableFrom(classes[0])) {
						metodo2 = method;
						break;

					}
				}
			}

		}
		return metodo2;
	}

	/**
	 * Método recursivo que recupera a propriedade do objeto.<br>
	 * Se a propriedade for simples é recuperado o método set da propriedade é o valor e atualizado.<br>
	 * Se a propriedade for composta, o método é chamado recursivamente para recuperar a propriedade set do valor a ser alterado.
	 * 
	 * @param propriedadade
	 *            Propriedade do objeto à ser atualizado
	 * @param objeto
	 *            Objeto que contém o atributo a ser atualizado
	 * @param argumento
	 *            Valor a ser atribuído no propriedade
	 * @throws Exception
	 *             se o atributo/objeto não for encontrado.
	 */
	public static Method retornaMetodoSet(String propriedadade, Object objeto, Object argumento) throws Exception {
		if (propriedadade.contains(".")) {
			Object agregadoVO = null;
			String[] objetoAgregado = propriedadade.split("\\.");
			try {
				agregadoVO = Reflexao.recuperaValorDaPropriedade(objetoAgregado[0], objeto);
				Class c = null;
				if (agregadoVO == null) {
					c = Reflexao.recuperaTipoDeRetornoDoMetodoGet(objetoAgregado[0], objeto);
					agregadoVO = c.newInstance();
				} else {
					c = agregadoVO.getClass();
				}
				if (Collection.class.isAssignableFrom(c)) {
					return null;
				}
				Class[] classes = new Class[1];
				classes[0] = agregadoVO.getClass();
				Method metodo2 = objeto.getClass().getMethod("set" + objetoAgregado[0].substring(0, 1).toUpperCase() + objetoAgregado[0].substring(1), classes);
				metodo2.invoke(objeto, agregadoVO);
				String propriedadeAux = propriedadade.replace(objetoAgregado[0] + ".", "");
				atualizarCampoDoObjeto(propriedadeAux, agregadoVO, argumento);
			} catch (Exception e) {
				throw new Exception("Erro ao recuperar método Set ", e);
			}
		} else {
			Class[] classes = new Class[1];
			Class class1 = null;
			if (argumento != null) {
				if (argumento instanceof Set) {
					classes[0] = Set.class;
				} else {
					class1 = argumento.getClass();
					while (class1.getSimpleName().contains("$$")) {// para tratar classes proxy hibernate
						class1 = class1.getSuperclass();
					}
					classes[0] = class1;
				}
			} else {
				// return;
			}
			Method metodo2 = recuperaMetodoNaHierarquia(propriedadade, objeto, classes);
			return metodo2;
		}
		return null;
	}

	/**
	 * Método responsável por atualizar o valor da propriedade do objeto para <code>null</code>.
	 * 
	 * @param propriedadade
	 *            Propriedade do objeto à ser atualizado
	 * @param objeto
	 *            Objeto que contém o atributo a ser atualizado
	 * @param argumento
	 *            Valor a ser atribuído no propriedade
	 * @throws Exception
	 *             se o atributo/objeto não for encontrado.
	 */
	public static void atualizarCampoDoObjetoParaNull(String propriedadade, Object objeto, Object argumento) throws Exception {

		Method metodo2 = retornaMetodoSet(propriedadade, objeto, argumento);
		if (metodo2 != null) {
			metodo2.invoke(objeto, (Object) null);
		}

	}

	/**
	 * Método responsável por atualizar o valor da propriedade do objeto para vazio.
	 * 
	 * @param propriedadade
	 *            Propriedade do objeto à ser atualizado
	 * @param objeto
	 *            Objeto que contém o atributo a ser atualizado
	 * @param argumento
	 *            Valor a ser atribuído no propriedade
	 * @throws Exception
	 *             se o atributo/objeto não for encontrado.
	 */
	public static void atualizarCampoDoObjetoParaVazio(String propriedadade, Object objeto, Object argumento) throws Exception {

		Method metodo2 = retornaMetodoSet(propriedadade, objeto, argumento);
		if (metodo2 != null) {
			metodo2.invoke(objeto, argumento);
		}

	}

	/**
	 * Método que atualiza a propriedade do objeto de acordo com método e o argumento passado como parâmetro.<br>
	 * 
	 * @param metodo
	 *            Método a ser atualizado
	 * @param objeto
	 *            Objeto que contém o atributo a ser atualizado
	 * @param argumento
	 *            Valor a ser atribuído no propriedade
	 * @throws Exception
	 *             se o atributo/objeto não for encontrado.
	 */
	public static void atualizarCampoDoObjeto(Method metodo, Object objeto, Object argumento) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Class[] classes = new Class[1];
		classes[0] = metodo.getReturnType();
		if (objeto instanceof Set) {
			Set colecao = (Set) objeto;
			objeto = (colecao.toArray()[0]);
		}
		Method metodo2 = objeto.getClass().getMethod("set" + metodo.getName().substring(3), classes);
		metodo2.invoke(objeto, argumento);
	}

	/**
	 * Método responsável por recuperar o getter do atributo.<br>
	 * Se o atributo for complexo, o método e chamado recursivamente para recuperar o get do atributo.
	 * 
	 * @param nomeAtributo
	 *            nome do atributo a se recuperar o método get
	 * @param objeto
	 *            objeto que contem o método
	 * @return retorna o método get do objeto
	 * @throws Exception
	 *             se o atributo não for encontrado no objeto
	 */
	public static Method recuperaMetodoGetDoObjeto(String nomeAtributo, Object objeto) throws Exception {
		if (!nomeAtributo.contains(".")) {
			if (objeto instanceof Set) {
				Set colecao = (Set) objeto;
				if (!colecao.isEmpty()) {
					objeto = colecao.toArray()[0];
				}
			}
			Method methods[] = objeto.getClass().getMethods();
			String nomeAtributoAux = "";
			for (Method method : methods) {
				nomeAtributoAux = Reflexao.recuperaNomeDoAtributo(method);
				if (eMetodoGetterBean(method)) {
					if (nomeAtributoAux.equals(nomeAtributo)) {
						return method;
					}
				}
			}
		} else {
			String nodosDaExpressao[] = nomeAtributo.split("\\.");
			Method m = recuperaMetodoGetDoObjeto(nodosDaExpressao[0], objeto);
			Class tipo = null;
			Object objAux = m.invoke(objeto);
			if (objAux == null) {
				if (m.getReturnType().isInterface()) {
					tipo = retornaTargetEntityClass(m, m.getReturnType());
					objAux = tipo.newInstance();
				} else {
					objAux = m.getReturnType().newInstance();
				}
			}
			if (Set.class.isAssignableFrom(objAux.getClass()) || List.class.isAssignableFrom(objAux.getClass()) || Set.class.equals(objAux) || List.class.equals(objAux)) {
				tipo = recuperaTipoDeParametroGenericosEmRetornoDeMetodos(m);

				try {
					String subExpressao = "";
					for (int i = 1; i < nodosDaExpressao.length; i++) {
						subExpressao = subExpressao + "." + nodosDaExpressao[i];
					}
					return recuperaMetodoGetDoObjeto(subExpressao.substring(1), tipo.newInstance());
				} catch (IllegalAccessException e) {
					log.error("Erro ao recuperar  método Get", e);
				} catch (InstantiationException e) {
					log.error("Erro ao recuperar método Get", e);
				}
			} else {
				nomeAtributo = nomeAtributo.replaceFirst(nodosDaExpressao[0] + ".", "");
				return recuperaMetodoGetDoObjeto(nomeAtributo, objAux);
			}
		}
		throw new Exception("Não  foi possível encontrar o atributo ou método: " + " " + nomeAtributo + " da classe:" + objeto.getClass().getName());
	}

	private static boolean eMetodoGetterBean(Method metodo) {
		return (metodo.getName().startsWith("get")) && (metodo.getName().indexOf("_") == -1) && (!metodo.getName().equals("getNrVersao")) && (!metodo.getName().equals("getUsuarioCriacao")) && (!metodo.getName().equals("getClass"));
	}

	private static boolean eMetodoGetterBeanPersistencia(Method metodo) {
		return (!metodo.getName().equals("getId")) && (metodo.getName().startsWith("get")) && (metodo.getName().indexOf("_") == -1) && (!metodo.getName().equals("getNrVersao")) && (!metodo.getName().equals("getUsuarioCriacao"))
				&& (!metodo.isAnnotationPresent(Transient.class)) && (!metodo.getName().equals("getClass"));
	}

	/**
	 * Método responsável por retornar o tipo genérico de uma coleção.
	 * 
	 * @param objeto
	 *            objeto que contem o método a ser pesquisado
	 * @param nomeMetodo
	 *            nome do método a ser pesquisado
	 * @return tipo genérico da <code>Collection</code>
	 * @throws Exception
	 *             se o atributo nomeMetodo não for encontrado
	 */
	public static Class recuperaTipoDeParametroGenerico(Object objeto, String nomeMetodo) throws Exception {
		Class<? extends Object> classe = objeto.getClass();
		while (classe.getSimpleName().contains("$$")) {// Resolve questões relacionadas as classes de proxy hibernate
			classe = classe.getSuperclass();
		}

		Method metodos[] = classe.getMethods();
		for (Method metodo : metodos) {
			if (metodo.getName().equals(nomeMetodo)) {
				return recuperaTipoDeParametroGenericosEmRetornoDeMetodos(metodo);
			}
		}
		return null;
	}

	/**
	 * Método responsável por retornar o tipo genérico de uma coleção
	 * 
	 * @param metodo
	 *            método a ser pesquisado
	 * @return tipo genérico da <code>Collection</code>
	 * @throws Exception
	 *             se o tipo do <code>metodo</code> não for uma <code>Collection</code>
	 */
	public static Class recuperaTipoDeParametroGenericosEmRetornoDeMetodos(Method metodo) throws Exception {
		int beginIndex = metodo.getGenericReturnType().toString().indexOf("<");
		int endIndex = metodo.getGenericReturnType().toString().indexOf(">");
		String nomeDoTipo = metodo.getGenericReturnType().toString().substring(beginIndex + 1, endIndex);
		try {
			Class clazz = Class.forName(nomeDoTipo);
			if (clazz.isInterface()) {
				clazz = retornaTargetEntityClass(metodo, clazz);
			}
			return clazz;
		} catch (ClassNotFoundException e) {
			throw new Exception("Não foi possível encontrar a classe " + nomeDoTipo, e);
		}
	}

	private static Class retornaTargetEntityClass(Method metodo, Class clazz) {
		if (metodo.isAnnotationPresent(OneToMany.class)) {
			clazz = metodo.getAnnotation(OneToMany.class).targetEntity();
		} else if (metodo.isAnnotationPresent(ManyToOne.class)) {
			clazz = metodo.getAnnotation(ManyToOne.class).targetEntity();
		} else {
			clazz = metodo.getAnnotation(ManyToMany.class).targetEntity();
		}
		return clazz;
	}

	/**
	 * Método responsável por verificar se o método passado como parâmetro não possui como retorno um objeto <code>Set</code>
	 * 
	 * @param metodo
	 *            método a ser verificado
	 * @return boolean
	 */
	public static boolean eMetodoGetterBeanSemSet(Method metodo) {
		return (!metodo.getName().equals("getId")) && (metodo.getName().startsWith("get")) && (metodo.getName().indexOf("_") == -1) && (!metodo.getName().equals("getClass")) && (!metodo.getName().equals("getNrVersao"))
				&& (!metodo.getName().equals("getDataCriacao")) && (!metodo.getName().equals("getDataAlteracao")) && (!metodo.getName().equals("getUnidadeCriacao")) && (!metodo.getName().equals("getUsuarioCriacao"))
				&& (!metodo.getReturnType().equals(Set.class));
	}

	/**
	 * Método responsável por recuperar o valor da propriedade do objeto.<br>
	 * Se a propriedade for complexa, o método e chamado recursivamente, varrendo todo o grafo do objeto para recuperar a propriedade.
	 * 
	 * @param propriedade
	 *            Propriedade a ser recuperada
	 * @param objetoAtual
	 *            Objeto que contém a a propriedade
	 * @return o valor da propriedade passada como parâmetro
	 * @throws Exception
	 *             se a propriedade não for encontrada
	 */
	public static Object recuperaValorDaPropriedade(String propriedade, Object objetoAtual) throws Exception {
		if (ePropriedadeSimples(propriedade)) {
			Method metodo = recuperaMetodoGetDoObjeto(propriedade, objetoAtual);
			if (metodo == null) {
				throw new IllegalArgumentException("A propriedade " + propriedade + " não existe no objeto");
			}
			return recuperaValorDeMetodoGet(metodo, objetoAtual);
		} else {
			String propriedadeAux = "";
			Object objetoAux = null;
			String propriedades[] = propriedade.split("\\.");
			Method metodo = recuperaMetodoGetDoObjeto(propriedades[0], objetoAtual);
			objetoAux = recuperaValorDeMetodoGet(metodo, objetoAtual);
			if (objetoAux == null && (ProBaseVO.class.isAssignableFrom(metodo.getReturnType()))) {
				objetoAux = metodo.getReturnType().newInstance();
				Reflexao.atualizarCampoDoObjeto(propriedades[0], objetoAtual, objetoAux);
			}
			// despreza a primeira string
			for (int i = 1; i < propriedades.length; ++i) {// operador pré-posto discarta a primeira posicao
				if (i < propriedades.length) {
					propriedadeAux = propriedadeAux + "." + propriedades[i];
				}
			}
			return recuperaValorDaPropriedade(propriedadeAux.substring(1), objetoAux);
		}
	}

	private static boolean ePropriedadeSimples(String propriedade) {
		return propriedade.indexOf(".") == -1;
	}

	/**
	 * Método responsável por recuperar o valor getter do método passado como parâmetro.
	 * 
	 * @param getter
	 *            método que sera invocado para retornar o valor
	 * @param objetoAtual
	 *            objeto que contem o método
	 * @return valor do método get
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 *             se o método não existir
	 */
	public static Object recuperaValorDeMetodoGet(Method getter, Object objetoAtual) throws IllegalAccessException, InvocationTargetException {
		Object objetoRetornado;
		objetoRetornado = getter.invoke(objetoAtual);
		return objetoRetornado;
	}

	/**
	 * Método responsável por recuperar o valor getter do método passado como parâmetro, se o retorno for igual a <code>null</code> é retornado uma nova instância do objeto de retorno
	 * 
	 * @param getter
	 *            método que sera invocado para retornar o valor
	 * @param objetoAtual
	 *            objeto que contem o método
	 * @return valor do método get ou uma nova instância, case retorno seja igual a <code>null</code>
	 * @throws Exception
	 *             se o método getter do parâmetro não existir no objeto atual
	 */
	public static Object recuperaValorDeMetodoGetNotNull(Method getter, Object objetoAtual) throws Exception {
		Object objetoRetornado;
		if (objetoAtual instanceof Set) {
			Set colecao = (Set) objetoAtual;
			if (!colecao.isEmpty()) {
				objetoAtual = (colecao.toArray()[0]);
			}
		}
		objetoRetornado = getter.invoke(objetoAtual);
		if (Set.class.isAssignableFrom(getter.getReturnType())) {
			Class tipoGenerico = recuperaTipoDeParametroGenericosEmRetornoDeMetodos(getter);
			Set objetoRetornadoSet = new HashSet();
			objetoRetornadoSet.add(tipoGenerico.newInstance());
			objetoRetornado = objetoRetornadoSet;
		} else if (objetoRetornado == null) {
			objetoRetornado = getter.getReturnType().newInstance();
		}
		return objetoRetornado;
	}

	/**
	 * Método responsável por recuperar o valor getter do atributo passado como parâmetro
	 * 
	 * @param nomeDoMetodoGet
	 *            nome do método a recuperar o valor
	 * @param objetoAtual
	 *            objeto que contem o método get
	 * @return valor do método get
	 * @throws Exception
	 *             se o método get não existir no objeto
	 */
	public static Object recuperaValorDeMetodoGet(String nomeDoMetodoGet, Object objetoAtual) throws Exception {
		Method getter = recuperaMetodoGetDoObjeto(nomeDoMetodoGet, objetoAtual);
		Object objetoRetornado;
		objetoRetornado = getter.invoke(objetoAtual);
		return objetoRetornado;
	}

	/**
	 * Método responsável por recuperar o tipo de retorno do método get.
	 * 
	 * @param nomeDoAtributo
	 *            nome do atributo a recuperar o get
	 * @param objetoAtual
	 *            objeto que contem o método get do atributo
	 * @return retorna o tipo de retorno do método get
	 * @throws Exception
	 *             se o não existir o getter do atributo informado
	 */
	public static Class recuperaTipoDeRetornoDoMetodoGet(String nomeDoAtributo, Object objetoAtual) throws Exception {
		Class classe = null;
		if (nomeDoAtributo.contains(".")) {
			String[] nodos = nomeDoAtributo.split("\\.");
			String novoCampo = "";
			for (int i = 1; i < nodos.length; i++) {
				novoCampo = novoCampo + "." + nodos[i];
			}
			Method getter = null;
			getter = recuperaMetodoGetDoObjeto(nodos[0], objetoAtual);
			try {
				Class<?> returnType = getter.getReturnType();
				if (Collection.class.isAssignableFrom(returnType)) {
					returnType = recuperaTipoDeParametroGenericosEmRetornoDeMetodos(getter);
				}
				classe = recuperaTipoDeRetornoDoMetodoGet(novoCampo.substring(1), returnType.newInstance());
			} catch (InstantiationException e) {
				log.error("Erro ao recuperar tipo de retorno do método Get", e);
			}
		} else {
			classe = recuperaMetodoGetDoObjeto(nomeDoAtributo, objetoAtual).getReturnType();
		}
		return classe;
	}

	/**
	 * Método responsável por recuperar o tipo de retorno do método get. Se o atributo for complexo o método é chamado recursivamente para varrer todo o grafo para recuperar o retorno do atributo
	 * 
	 * @param nomeDoAtributo
	 *            nome do atributo a recuperar o get
	 * @param objetoAtual
	 *            objeto que contem o método get do atributo
	 * @return retorna o tipo de retorno do método get
	 * @throws Exception
	 *             se o não existir o getter do atributo informado
	 */
	public static Class recuperaTipoDeRetornoDoMetodoGetNoGrafo(String nomeDoAtributo, Object objetoAtual) throws Exception {
		Class classe = null;
		if (nomeDoAtributo.contains(".")) {
			String[] nodos = nomeDoAtributo.split("\\.");
			String novoCampo = "";
			for (int i = 1; i < nodos.length; i++) {
				novoCampo = novoCampo + "." + nodos[i];
			}
			Method getter = null;
			getter = recuperaMetodoGetDoObjeto(nodos[0], objetoAtual);
			Class<?> returnType = null;
			try {
				returnType = getter.invoke(objetoAtual).getClass();
				log.info("Recuperou o tipo de  retorno do método através da instancia");
			} catch (Exception e) {
				log.warn("Não foi possível recuperar tipo de retorno do método através da instancia. Vai tentar pelo tipo declarado estaticamente no metodo");
				returnType = getter.getReturnType();
			}
			if (Collection.class.isAssignableFrom(returnType)) {
				returnType = recuperaTipoDeParametroGenericosEmRetornoDeMetodos(getter);
			}
			classe = recuperaTipoDeRetornoDoMetodoGetNoGrafo(novoCampo.substring(1), returnType.newInstance());
		} else {
			classe = recuperaMetodoGetDoObjeto(nomeDoAtributo, objetoAtual).getReturnType();
		}
		return classe;
	}

	public static String recuperaNomeDeMetdoPorClasse(Object object) {
		return object.getClass().getSimpleName().substring(0, 1).toLowerCase() + object.getClass().getSimpleName().substring(1);
	}

	/**
	 * Método responsável por recuperar todos os atributos da classe informada no parâmetro.
	 * 
	 * @param object
	 *            classe que se deseja recuperar os atributos
	 * @return parâmetros da classe
	 */
	public static String[] recuperaNomeDeAtributosPorClasse(Object object) {
		Method[] methods = object.getClass().getMethods();
		String[] atributos = new String[methods.length];
		for (int i = 0; i < methods.length; i++) {
			atributos[i] = methods[i].getName().substring(3, 4).toLowerCase() + methods[i].getName().substring(4);
		}
		return atributos;
	}

	/**
	 * Método responsável por recuperar todos os atributos da classe informada no parâmetro.
	 * 
	 * @param object
	 *            classe que se deseja recuperar os atributos
	 * @return parâmetros da classe
	 */
	public static List<String> recuperaNomeDeAtributosBeansPorClasse(Object object) {
		Method[] methods = object.getClass().getMethods();
		List<String> atributos = new ArrayList<String>();
		for (int i = 0; i < methods.length; i++) {
			if (eMetodoGetterBeanPersistencia(methods[i])) {
				atributos.add(methods[i].getName().substring(3, 4).toLowerCase() + methods[i].getName().substring(4));
			}
		}
		return atributos;
	}

	public static void transfereDadosEntreObjetos(ProBaseVO appBaseVOOrigem, ProBaseVO appBaseVODestino) throws Exception {
		List<String> atributos = recuperaNomeDeAtributosBeansPorClasse(appBaseVOOrigem);
		for (String atributo : atributos) {
			try {
				Method metodoGet = recuperaMetodoGetDoObjeto(atributo, appBaseVOOrigem);
				Object valor = recuperaValorDeMetodoGet(metodoGet, appBaseVOOrigem);
				if (valor instanceof Collection) {
					continue;
					/*
					 * valor = clonaColecao((Collection)valor); atualizarCampoDoObjeto(atributo, appBaseVODestino, valor);
					 */} else if (eMetodoGetterBeanPersistencia(metodoGet)) {
					atualizarCampoDoObjeto(atributo, appBaseVODestino, valor);
				}
			} catch (NoSuchMethodException e) {
				throw new Exception("Erro ao tranferir dados de objeto para outro: " + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new Exception("Erro ao tranferir dados de objeto para outro: " + e.getMessage());
			} catch (InvocationTargetException e) {
				throw new Exception("Erro ao tranferir dados de objeto para outro: " + e.getMessage());
			}
		}
	}

	public static Collection clonaColecao(Collection colecaoASerClonada) throws Exception {
		Collection colecaoClone = null;
		try {
			colecaoClone = new HashSet();
			ProBaseVO appBaseVOClone = null;
			ProBaseVO appBaseVOASerClonado = null;
			for (Object o : colecaoASerClonada) {
				appBaseVOASerClonado = (ProBaseVO) o;
				appBaseVOClone = appBaseVOASerClonado.getClass().newInstance();
				transfereDadosEntreObjetos(appBaseVOASerClonado, appBaseVOClone);
				colecaoClone.add(appBaseVOClone);
			}

		} catch (InstantiationException e) {
			throw new Exception("Erro ao tentar clonar colecao", e);
		} catch (IllegalAccessException e) {
			throw new Exception("Erro ao tentar clonar colecao", e);
		}
		return colecaoClone;
	}

	public static Class[] getGenericTypes(Class<?> clazz) {
		TypeVariable[] typeVariables = clazz.getTypeParameters();

		List<Class> lista = new ArrayList<Class>();
		for (TypeVariable variable : typeVariables) {
			lista.add((Class) variable.getBounds()[0]);
		}
		return lista.toArray(new Class[lista.size()]);
	}

	public static Class[] getGenericTypes2(Class<?> clazz) {
		try {
			ParameterizedType genericSuperclass = (ParameterizedType) clazz.getGenericSuperclass();
			Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
			Class[] array = new Class[actualTypeArguments.length];
			for (int i = 0; i < actualTypeArguments.length; i++) {
				Type type = actualTypeArguments[i];
				array[i] = (Class) type;
			}
			return array;
		} catch (Exception e) {
			throw new RuntimeException("Não foi possível descobrir o tipo genérico da classe " + clazz);
		}
	}

	public static List<Class> obterClassesGenericasDeUmaColecaoLista(Field field) throws ClassNotFoundException {
		List<Class> listaRetorno = new ArrayList<Class>();
		Type type = field.getGenericType();
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			for (Type t : pt.getActualTypeArguments()) {
				listaRetorno.add(Class.forName(t.toString().replaceAll("class ", "")));
			}
		}
		return listaRetorno;
	}

	public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationClass) throws NoSuchMethodException, SecurityException {
		A annotation = method.getAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}
		Class<?> cl = method.getDeclaringClass();
		while (annotation == null) {
			cl = cl.getSuperclass();
			if (cl == null || cl == Object.class) {
				break;
			}
			try {
				Method equivalentMethod = cl.getDeclaredMethod(method.getName(), method.getParameterTypes());
				annotation = equivalentMethod.getAnnotation(annotationClass);
			} catch (NoSuchMethodException e) {
				log.debug("O método findAnnotation da classe Reflexao não encontrou o método {} na classe {}.", method.getName(), cl.getName());
			}
		}
		return annotation;
	}

	public static List<Method> recuperaMetodosQuePossuemEstaAnotacaoNestaClasse(Class<? extends Annotation> annotationClass, Class cls) {
		Method[] methods = cls.getMethods();
		List<Method> methodsList = new ArrayList<Method>();
		for (Method method : methods) {
			try {
				if (findAnnotation(method, annotationClass) != null) {
					methodsList.add(method);
				}
			} catch (NoSuchMethodException e) {
				log.debug("O método recuperaMetodosQuePossuemEstaAnotacaoNestaClasse da classe Reflexao não encontrou o método {} na classe {}.", method.getName(), cls.getName());
			} catch (SecurityException e) {
				log.debug("O método recuperaMetodosQuePossuemEstaAnotacaoNestaClasse da classe Reflexao não encontrou o método {} na classe {}. {}", method.getName(), cls.getName(), e.getMessage());
			}
		}
		return methodsList;
	}

	@SuppressWarnings("unchecked")
	public static Enum recuperaEnumPeloOrdinal(Class<Enum> classeDaEnum, int ordinalEnum) {
		Object[] array = EnumSet.allOf(classeDaEnum).toArray();
		return (Enum) array[ordinalEnum];

	}

	@SuppressWarnings("unchecked")
	public static Enum recuperaEnumPelaString(Class<Enum> classeDaEnum, String stringEnum) {
		return Enum.valueOf(classeDaEnum, stringEnum);
	}

}
