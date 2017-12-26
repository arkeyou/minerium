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
package br.gov.prodigio.servico;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import br.gov.prodigio.comuns.utils.CollectionHelper;
import br.gov.prodigio.comuns.utils.StringHelper;
import br.gov.prodigio.json.ProJSONDeserializer;
import br.gov.prodigio.json.ProObjectFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ClienteRest {

	private String uri;
	private String password;
	private String username;
	private Map<TipoParametro, Map<String, String>> parametros;
	private String proxyHost;
	private int proxyPorta;
	private String proxyUsuario;
	private String proxySenha;
	private boolean validaHostnameSSL;
	private String contentTypeRequest = "application/json; charset=UTF-8";
	private String charsetResponse = StandardCharsets.UTF_8.name();
	private String charsetRequest = StandardCharsets.UTF_8.name();
	private boolean usaProxy;
	private ArrayList<String> bypass;
	private ProJSONDeserializer<?> jsonDeserializer = null;
	private Class<?> classeRetorno;

	/**
	 * VALIDA SE PARAMETRO URL FOI INFORMADO.
	 * 
	 * @param uri
	 *            Caminho do servico - <b>obrigatório</b>
	 */
	public void validarUri(String uri) {
		if (StringHelper.isEmpty(uri)) {
			throw new IllegalArgumentException("O PARAMETRO URI DEVE SER INFORMADO");
		}
	}

	/**
	 * Cria instancia do client de servico rest.
	 * 
	 * @param uri
	 *            Caminho do servico
	 * @return instancia de {@link ClienteRest}
	 */
	public static ClienteRest newInstance(String uri) {
		if (StringHelper.isEmpty(uri)) {
			throw new IllegalArgumentException("O PARAMETRO URI DEVE SER INFORMADO");
		}
		ClienteRest retorno = new ClienteRest();
		retorno.uri = uri;
		retorno.parametros = inicializaMapaDeParametros();
		return retorno;
	}

	private static HashMap<TipoParametro, Map<String, String>> inicializaMapaDeParametros() {
		HashMap<TipoParametro, Map<String, String>> retorno = new HashMap<ClienteRest.TipoParametro, Map<String, String>>();
		List<TipoParametro> listaTiposParametro = Arrays.asList(TipoParametro.values());
		for (TipoParametro tipoParametro : listaTiposParametro) {
			retorno.put(tipoParametro, new HashMap());
		}
		return retorno;
	}

	/**
	 * * CONFIGURA SEGURANCA PARA ACESSO AO SERVICO.
	 * 
	 * @param req
	 *            Requisicao
	 * @param username
	 *            Usuario
	 * @return instancia
	 */
	public ClienteRest configurarSeguranca(String username, String password) {
		this.username = username;
		this.password = password;
		return this;
	}

	/**
	 * CONFIGURA SEGURANCA PARA ACESSO AO SERVICO.
	 * 
	 * @param req
	 *            Requisicao
	 * @param username
	 *            Usuario
	 * @param password
	 *            Senha
	 */
	private void configuraSeguranca(HttpRequestBase req) {
		if (StringHelper.isNotEmpty(username) && StringHelper.isNotEmpty(password)) {
			String auth = username.concat(":").concat(password);
			byte[] encodedAuth = DatatypeConverter.printBase64Binary(auth.getBytes()).getBytes(Charset.forName("ISO-8859-1"));
			String authHeader = "Basic " + new String(encodedAuth);
			req.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		}
	}

	// ###################### GET ######################
	/**
	 * REALIZA CHAMADA DE SERVICO REST COM BASIC AUTHENTICATION.
	 * 
	 * @param parametros
	 *            Parametros para requisicao
	 * @return {@link String} com retorno do servico
	 */
	public Object get() {
		try {
			validarUri(uri);
			return executarServico(new HttpGet(uri), null);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao chamar serviço GET", e);
		}

	}

	// ###################### POST ######################
	/**
	 * @param entidadeRequest
	 *            Entidade que sera enviada via json
	 * @return {@link String} com retorno do servico
	 */
	public Object post(Object entidadeRequest) {
		try {
			validarUri(uri);
			HttpPost requisicao = criarHttpPost(entidadeRequest, contentTypeRequest);
			return executarServico(requisicao, null);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao chamar serviço POST", e);
		}
	}

	/**
	 * cria HttpPost
	 * 
	 * @param uri
	 *            Caminho do servico - <b>obrigatório</b>
	 * @param entidadeRequest
	 *            Entidade que sera enviada via json
	 * @param contentType
	 *            Content type da entidade request
	 * @return instancia de {@link HttpPost}
	 * @throws Exception
	 *             erro ao tentar criar o objeto
	 */
	public HttpPost criarHttpPost(Object entidadeRequest, String contentType) throws Exception {
		HttpPost req = new HttpPost(uri);
		if (entidadeRequest != null) {
			req.setEntity(criarObjetoStringEntity(entidadeRequest));
		}
		return req;
	}

	// ###################### PUT ######################
	public Object put(Object entidadeRequest) {
		try {
			validarUri(uri);
			return executarServico(criarHttpPut(entidadeRequest), null);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao chamar serviço PUT", e);
		}
	}

	private HttpRequestBase criarHttpPut(Object entidadeRequest) throws Exception {
		HttpPut req = new HttpPut(uri);
		if (entidadeRequest != null) {
			req.setEntity(criarObjetoStringEntity(entidadeRequest));
		}
		return req;
	}

	// ###################### DELETE ######################
	public Object delete() {
		try {
			validarUri(uri);
			return executarServico(new HttpDelete(uri), null);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao chamar serviço DELETE", e);
		}
	}

	private Object executarServico(HttpRequestBase req, String charsetRequest) throws IOException, ClientProtocolException {
		configuraSeguranca(req);
		adicionarParametros(req, parametros, charsetRequest);
		HttpClient client = retornarHttpClient();
		HttpResponse response = client.execute(req);
		HttpEntity entity = response.getEntity();
		String retornoString = EntityUtils.toString(entity, charsetResponse);

		if (classeRetorno != null) {
			try {
				return jsonDeserializer.deserialize(retornoString, classeRetorno);
			} catch (Exception e) {
				ObjectMapper objectMapper = new ObjectMapper();
				return objectMapper.readValue(retornoString, classeRetorno);
			}
		}
		return retornoString;
	}

	private CloseableHttpClient retornarHttpClient() {
		HttpClientBuilder builder = HttpClientBuilder.create();
		configurarProxy(builder);
		desabilitarValidacaoDeHostName(builder);
		return builder.build();
	}

	public void desabilitarValidacaoDeHostName(HttpClientBuilder builder) {
		if (Boolean.TRUE.equals(validaHostnameSSL)) {
			SSLContext context;
			try {
				context = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						return true;
					}
				}).build();
				builder.setHostnameVerifier(new AllowAllHostnameVerifier()).setSslcontext(context);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void configurarProxy(HttpClientBuilder builder) {

		if (usaProxy) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPorta);
			HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy) {
				@Override
				public HttpRoute determineRoute(final HttpHost host, final HttpRequest request, final HttpContext context) throws HttpException {
					String hostname = host.getHostName();
					if (CollectionHelper.isNotEmpty(bypass)) {
						for (String item : bypass) {
							if (hostname.contains(item)) {
								return new HttpRoute(host);
							}
						}
					}
					return super.determineRoute(host, request, context);
				}
			};
			builder.setRoutePlanner(routePlanner);
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(proxyHost, proxyPorta), new UsernamePasswordCredentials(proxyUsuario, proxySenha));
			builder.setDefaultCredentialsProvider(credsProvider);
		}
	}

	private StringEntity criarObjetoStringEntity(Object parametro) throws IOException, JsonGenerationException, JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		String json;
		if (parametro instanceof String) {
			json = (String) parametro;
		} else {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			json = mapper.writeValueAsString(parametro);
		}

		StringEntity objetoStringEntity = new StringEntity(json, charsetRequest);
		objetoStringEntity.setContentType(contentTypeRequest);
		return objetoStringEntity;
	}

	public static JsonNode criarArvoreDeObjetosJson(String json) {
		try {
			return new ObjectMapper().readTree(json);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao transformar json em objeto.", e);
		}
	}

	private static void adicionarParametros(HttpRequestBase req, Map<TipoParametro, Map<String, String>> parametros, String charset) {
		if (parametros != null && !parametros.isEmpty()) {
			Set<Entry<TipoParametro, Map<String, String>>> entrySet = parametros.entrySet();
			for (Entry<TipoParametro, Map<String, String>> entry : entrySet) {
				TipoParametro tipoParametro = entry.getKey();
				tipoParametro.configurarParametros(req, entry.getValue(), charset);
			}
		}
	}

	/**
	 * Enum usado para montar parametros de requisicao.
	 *
	 */
	enum TipoParametro {
		/**
		 * PARAMETROS QUE SAO PASSADOS VIA HEADER
		 */
		HEADER {
			@Override
			public void configurarParametros(HttpRequestBase req, Map<String, String> parametros, String charset) {
				if (parametros != null) {
					Set<Entry<String, String>> entrySet = parametros.entrySet();
					for (Entry<String, String> entry : entrySet) {
						req.addHeader(entry.getKey(), entry.getValue());
					}

				}
			}
		},
		/**
		 * 
		 */
		URL_ENTITY_FORM {
			@Override
			public void configurarParametros(HttpRequestBase req, Map<String, String> parametros, String charset) {
				if (req instanceof HttpEntityEnclosingRequestBase && parametros != null && !parametros.isEmpty()) {
					List<NameValuePair> postParams = new ArrayList<NameValuePair>();
					for (Entry<String, String> item : parametros.entrySet()) {
						postParams.add(new BasicNameValuePair(item.getKey(), item.getValue()));
					}
					UrlEncodedFormEntity entity;
					try {
						entity = new UrlEncodedFormEntity(postParams, charset);
						((HttpEntityEnclosingRequestBase) req).setEntity(entity);
					} catch (UnsupportedEncodingException e) {
						throw new IllegalArgumentException("CHARSET INVÁLIDO");
					}
				}
			}
		};

		/**
		 * Confiqura parametros para a requisicao
		 * 
		 * @param req
		 *            requisicao
		 * @param parametros
		 *            mapa de parametros
		 * @param charset
		 *            charset do parametro
		 */
		public abstract void configurarParametros(HttpRequestBase req, Map<String, String> parametros, String charset);

	}

	/**
	 * Adiciona parametros no header da requisicao.
	 * 
	 * @param headerParams
	 * @return
	 */
	public ClienteRest adicionarParametrosNoHeader(Map<String, String> headerParams) {
		if (headerParams != null && !headerParams.isEmpty()) {
			parametros.get(TipoParametro.HEADER).putAll(headerParams);
		}
		return this;
	}

	public ClienteRest adicionarParametrosForm(Map<String, String> headerParams) {
		if (headerParams != null && !headerParams.isEmpty()) {
			parametros.get(TipoParametro.URL_ENTITY_FORM).putAll(headerParams);
		}
		return this;
	}

	public ClienteRest adicionarParametroNoHeader(String key, String value) {
		if (StringHelper.isNotEmpty(key) && StringHelper.isNotEmpty(value)) {
			parametros.get(TipoParametro.HEADER).put(key, value);
		}
		return this;
	}

	/**
	 * Configura parametros de proxy
	 * 
	 * @param host
	 * @param porta
	 * @param usuario
	 * @param senha
	 * @param bypass
	 * @param usaProxy
	 * @return
	 */
	public ClienteRest definirProxy(String host, int porta, String usuario, String senha, boolean usaProxy, ArrayList<String> bypass) {
		if (usaProxy && StringHelper.isEmpty(host) || porta < 1) {
			throw new IllegalArgumentException("O proxy e/ou a porta não foram informados ou são inválidos!");
		}
		this.proxyHost = host;
		this.proxyPorta = porta;
		this.proxyUsuario = usuario;
		this.proxySenha = senha;
		this.usaProxy = usaProxy;
		this.bypass = bypass;
		return this;
	}

	public ClienteRest validaHostnameSSL(Boolean validaHostnameSSL) {
		this.validaHostnameSSL = validaHostnameSSL;
		return this;
	}

	public ClienteRest definirContentTypeRequest(String contentTypeRequest) {
		this.contentTypeRequest = contentTypeRequest;
		adicionarParametroNoHeader(HttpHeaders.CONTENT_TYPE, contentTypeRequest);
		return this;
	}

	public ClienteRest definirCharsetRequest(String charset) {
		charsetRequest = charset;
		return this;
	}

	public ClienteRest definirCharsetResponse(String charset) {
		charsetResponse = charset;
		return this;
	}

	/**
	 * Define o tipo de retorno da chamada rest.
	 * 
	 * @param classeRetorno
	 *            objeto de retorno
	 * @param mapInterfaceResolver
	 *            mapa que resolve qual implementacao sera usada para interface mapeada como propriedade
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ClienteRest definirTipoDeRetorno(Class classeRetorno, Map<Class, Class> mapInterfaceResolver) {
		if (classeRetorno == null) {
			throw new IllegalArgumentException("A classe de retorno na foi informada!");
		}
		this.classeRetorno = classeRetorno;
		this.jsonDeserializer = new ProJSONDeserializer();
		if (mapInterfaceResolver != null) {
			Set<Entry<Class, Class>> entrySet = mapInterfaceResolver.entrySet();
			ProObjectFactory factory = new ProObjectFactory();
			for (Entry<Class, Class> entry : entrySet) {
				jsonDeserializer.use(entry.getKey(), entry.getValue(), factory);
			}
		}
		return this;
	}

	public ClienteRest aceitarCertificadosSSL() {
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");

			if (sc != null) {
				sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());

				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				});
			}
			return this;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao aceitar certificado SSL");
		}
	}

}
