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
package br.gov.prodigio.servicoweb;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.IProFacade;
import br.gov.prodigio.comuns.utils.DataHelper;
import br.gov.prodigio.comuns.utils.Reflexao;
import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.entidades.ProVO;
import br.gov.prodigio.entidades.ProVO.SITUACAO_DO_REGISTRO;
import br.gov.prodigio.entidades.StatusDoRegistro;
import br.gov.prodigio.entidades.comando.ListarObjetoBaseadoNoExemploCMDVO;
import br.gov.prodigio.entidades.dto.Entrada;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProRestCtr {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected static IProFacade fachadaDeNegocio;
	@Context
	ServletContext servletContext;

	@POST
	@Path("/gravar")
	@Consumes({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response gravar(Entrada entrada) throws Exception {
		ProBaseVO objetoAtual;
		objetoAtual = construirObjetoAtualAPartirDaEntrada(entrada);
		preencherCamposDeAuditoriaProdigio(objetoAtual);
		Long id = (Long) getFachadaDeNegocio().gravar(objetoAtual);
		return Response.status(200).entity(id).build();
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Path("/concluir")
	@Consumes({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response concluir(Entrada entrada) throws Exception {
		ProBaseVO objetoAtual = construirObjetoAtualAPartirDaEntrada(entrada);
		Long id = (Long) getFachadaDeNegocio().concluir(objetoAtual, new HashMap());
		return Response.status(200).entity(id).build();
	}

	@POST
	@Path("/excluir")
	@Consumes({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public Response excluir(Entrada entrada) throws Exception {
		ProBaseVO objetoAtual = construirObjetoAtualAPartirDaEntrada(entrada);
		getFachadaDeNegocio().excluir(objetoAtual);
		return Response.status(200).build();
	}

	@POST
	@Path("/listarBaseadoExemplo")
	@Consumes({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List listarBaseadoExemplo(Entrada entrada) throws Exception {

		ListarObjetoBaseadoNoExemploCMDVO listarBaseadoNoExemploCMDVO = construirComandoExemploAPartirDaEntrada(entrada);

		ProBaseVO objetoExemplo = construirObjeto(entrada.getClasse(), listarBaseadoNoExemploCMDVO.getExemplo());

		String[] projecoes = montarProjecoes(listarBaseadoNoExemploCMDVO);

		Set<ProBaseVO> resultado = getFachadaDeNegocio().listarBaseadoNoExemplo(objetoExemplo, objetoExemplo, listarBaseadoNoExemploCMDVO.getPrimeiroRegistro(), listarBaseadoNoExemploCMDVO.getQuantidadeRegistros(), projecoes);
		return new ArrayList(resultado);
	}

	protected String[] montarProjecoes(ListarObjetoBaseadoNoExemploCMDVO listarBaseadoNoExemploCMDVO) {
		String[] projecoes = null;
		if (listarBaseadoNoExemploCMDVO.getProjecoes() != null && listarBaseadoNoExemploCMDVO.getProjecoes().size() > 0) {
			projecoes = new String[listarBaseadoNoExemploCMDVO.getProjecoes().size()];
			projecoes = listarBaseadoNoExemploCMDVO.getProjecoes().toArray(projecoes);
		} else {
			projecoes = new String[] { "id" };
		}
		return projecoes;
	}

	protected ProBaseVO construirObjeto(String classe, Object objeto) throws Exception {
		ProBaseVO instanciaDoObjeto = instanciarObjeto(classe);
		String json = converterObjetoParaJson(objeto);
		return (ProBaseVO) converterJsonParaObjeto(json, instanciaDoObjeto.getClass());
	}

	protected ListarObjetoBaseadoNoExemploCMDVO construirComandoExemploAPartirDaEntrada(Entrada entrada) throws Exception {
		ListarObjetoBaseadoNoExemploCMDVO listarBaseadoNoExemploCMDVO = new ListarObjetoBaseadoNoExemploCMDVO();

		String jsonDoObjeto = converterObjetoParaJson(entrada.getObjeto());
		listarBaseadoNoExemploCMDVO = (ListarObjetoBaseadoNoExemploCMDVO) converterJsonParaObjeto(jsonDoObjeto, ListarObjetoBaseadoNoExemploCMDVO.class);
		return listarBaseadoNoExemploCMDVO;
	}

	protected ProBaseVO construirObjetoAtualAPartirDaEntrada(Entrada entrada) throws Exception {
		ProBaseVO objetoAtual = instanciarObjeto(entrada.getClasse());
		String jsonComOsDadosDoObjetoAtual = converterObjetoParaJson(entrada.getObjeto());

		objetoAtual = (ProBaseVO) converterJsonParaObjeto(jsonComOsDadosDoObjetoAtual, objetoAtual.getClass());
		if (objetoAtual.getId() != null) {
			objetoAtual = atribuirValoresDoJsonParaObjeto(jsonComOsDadosDoObjetoAtual, objetoAtual);
		}
		return objetoAtual;
	}

	protected void updateObjectFromJson(Iterator<Entry<String, JsonNode>> arvoreDoObjetoJson, ProBaseVO objetoAtual, String propriedadeDoObjeto) throws Exception {
		while (arvoreDoObjetoJson.hasNext()) {
			Entry<String, JsonNode> objetoJson = arvoreDoObjetoJson.next();
			JsonNode nodoJson = objetoJson.getValue();

			if (nodoJson.isObject()) {
				String novoNomeDaPropriedadeDoObjeto = montarNomeDaPropriedadeDoObjeto(propriedadeDoObjeto, objetoJson);
				updateObjectFromJson(nodoJson.fields(), objetoAtual, novoNomeDaPropriedadeDoObjeto);

			} else if (nodoJson.isArray()) {
				tratarNodoDoTipoLista(objetoAtual, objetoJson);

			} else {
				String nomeDaPropriedadeDoObjeto = objetoJson.getKey();

				if (!nomeDaPropriedadeDoObjeto.equals("@class") && !nomeDaPropriedadeDoObjeto.equals("nrVersao") && !nomeDaPropriedadeDoObjeto.equals("statusDoRegistro")) {
					String novoNomeDaPropriedadeDoObjeto = montarNomeDaPropriedadeDoObjeto(propriedadeDoObjeto, objetoJson);
					tratarNodoDoTipoPrimitivo(objetoAtual, objetoJson, novoNomeDaPropriedadeDoObjeto);
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void tratarNodoDoTipoPrimitivo(ProBaseVO objetoAtual, Entry<String, JsonNode> objetoJson, String propriedadeDoObjeto) throws Exception {
		try {
			Class classeDoMetodoGet = Reflexao.recuperaTipoDeRetornoDoMetodoGet(propriedadeDoObjeto, objetoAtual);
			Object valorDaPropriedade;
			if (classeDoMetodoGet.isEnum()) {
				valorDaPropriedade = recuperaValorDaEnum(objetoJson.getValue(), classeDoMetodoGet);
			} else if (classeDoMetodoGet.isAssignableFrom(Date.class)) {
				valorDaPropriedade = DataHelper.converteStringParaData(objetoJson.getValue().asText());
			} else {
				Constructor construtorStringDaClasse = classeDoMetodoGet.getConstructor(String.class);
				valorDaPropriedade = construtorStringDaClasse.newInstance(objetoJson.getValue().asText());
			}
			Reflexao.atualizarCampoDoObjeto(propriedadeDoObjeto, objetoAtual, valorDaPropriedade);

		} catch (Exception e) {
			log.error("#ERRO: ", e);
			throw new IllegalArgumentException("Valor não tratado ou com formato incorreto. Nome da propriedade:" + propriedadeDoObjeto);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object recuperaValorDaEnum(JsonNode jsonNode, Class classeDoMetodoGet) {
		Object valorDaPropriedade;
		if (jsonNode.isInt()) {
			valorDaPropriedade = Reflexao.recuperaEnumPeloOrdinal(classeDoMetodoGet, jsonNode.asInt());
		} else {
			valorDaPropriedade = Reflexao.recuperaEnumPelaString(classeDoMetodoGet, jsonNode.asText());
		}
		return valorDaPropriedade;
	}

	protected String montarNomeDaPropriedadeDoObjeto(String propriedadeDoObjeto, Entry<String, JsonNode> entry) {
		String novoNomeDaPropriedadeDoObjeto = null;
		if (propriedadeDoObjeto == null) {
			novoNomeDaPropriedadeDoObjeto = entry.getKey();
		} else {
			novoNomeDaPropriedadeDoObjeto = propriedadeDoObjeto + "." + entry.getKey();
		}
		return novoNomeDaPropriedadeDoObjeto;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void tratarNodoDoTipoLista(ProBaseVO proBaseVO, Entry<String, JsonNode> entry) throws Exception {
		Class classeDaPropriedade = recuperarClasseAPartirDaPropriedade(proBaseVO, entry.getKey());
		Collection listaDeAgregadosDoObjetoAtual = (Collection) Reflexao.recuperaValorDaPropriedade(entry.getKey(), proBaseVO);

		Iterator<JsonNode> listaDeAgregadosDoObjetoAtualJSON = entry.getValue().elements();
		while (listaDeAgregadosDoObjetoAtualJSON.hasNext()) {
			JsonNode itemDaListaJSON = listaDeAgregadosDoObjetoAtualJSON.next();
			for (Object itemDaListaDeAgregados : listaDeAgregadosDoObjetoAtual) {
				ProBaseVO itemDaLista = (ProBaseVO) itemDaListaDeAgregados;
				if (itemDaLista.getId().equals(itemDaListaJSON.get("id").asLong())) {
					updateObjectFromJson(itemDaListaJSON.fields(), itemDaLista, null);
				} else {
					ProBaseVO converterJsonParaObjeto = (ProBaseVO) converterJsonParaObjeto(itemDaListaJSON.toString(), classeDaPropriedade);
					listaDeAgregadosDoObjetoAtual.add(converterJsonParaObjeto);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	protected Class recuperarClasseAPartirDaPropriedade(ProBaseVO proBaseVO, String propriedade) throws Exception {
		Method metodoGet = Reflexao.recuperaMetodoGetDoObjeto(propriedade, proBaseVO);
		return Reflexao.recuperaTipoDeParametroGenericosEmRetornoDeMetodos(metodoGet);
	}

	protected ProBaseVO instanciarObjeto(String classe) throws Exception {
		Class<?> clazz = Class.forName(classe);
		return (ProBaseVO) clazz.newInstance();
	}

	protected String converterObjetoParaJson(Object objeto) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(objeto);
	}

	protected Object converterJsonParaObjeto(String json, Class<?> classe) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json.getBytes("UTF-8"), classe);

	}

	protected ProBaseVO atribuirValoresDoJsonParaObjeto(String jsonDoObjeto, ProBaseVO objetoAtual) throws Exception {
		JsonNode node = new ObjectMapper().readTree(jsonDoObjeto);
		if (node.get("id") != null) {
			objetoAtual = getFachadaDeNegocio().recuperaObjeto((ProBaseVO) objetoAtual);
			if (objetoAtual != null) {
				updateObjectFromJson(node.fields(), objetoAtual, null);
			} else {
				throw new IllegalArgumentException("Objeto não encontrado, ID: " + node.get("id"));
			}
		}
		return objetoAtual;
	}

	protected void preencherCamposDeAuditoriaProdigio(ProBaseVO proBase) {
		proBase.setCdLoginMovimentacao("PRO-REST");
		proBase.setTpOperacao("A");
		proBase.setIpMovimentacao("servidor");
		proBase.setStatusDoRegistro(StatusDoRegistro.ATIVO);
		if (proBase instanceof ProVO) {
			((ProVO) proBase).setDsSituacao(SITUACAO_DO_REGISTRO.EM_EDICAO);
		}
	}

	protected IProFacade getFachadaDeNegocio() {
		if (fachadaDeNegocio == null) {
			fachadaDeNegocio = (IProFacade) servletContext.getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO);
		}
		return fachadaDeNegocio;
	}
}
