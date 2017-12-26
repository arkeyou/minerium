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
package br.gov.prodigio.persistencia.mainframe;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comuns.anotacoes.EntityBroker;
import br.gov.prodigio.comuns.anotacoes.MColumnReceive;
import br.gov.prodigio.comuns.anotacoes.MColumnSend;
import br.gov.prodigio.comuns.anotacoes.MOneToMany;
import br.gov.prodigio.comuns.exception.BrokerException;
import br.gov.prodigio.comuns.utils.Reflexao;

public class MainFrameDAOHelper implements IMainFrameDAOHelper {

	private static final Logger logF = LoggerFactory.getLogger(MainFrameDAOHelper.class);

	private final Map<Class, ExpiringCache> cache = new HashMap<Class, ExpiringCache>();
	private static IMainFrameDAOHelper instance = null;

	public static IMainFrameDAOHelper getInstance() {
		if (instance == null) {
			createMainFrameDAOHelper();
		}

		return instance;
	}

	private synchronized static void createMainFrameDAOHelper() {
		if (instance == null) {
			instance = new MainFrameDAOHelper();
		}

	}

	private MainFrameDAOHelper() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.persistencia.mainframe.IMainFrameDAOHelper#preencheListaComCamposAnotadosComReceive(java.lang.Object, java.util.List)
	 */
	@Override
	public void preencheListaComCamposAnotadosComReceive(Object obj, List<Field> lista) throws Exception {
		lista.clear();
		Class classe = obj.getClass();
		Field[] fields = classe.getDeclaredFields();

		int tamanhoTotal = 0;

		// Adiciona campos que possuem a anotaca de mainframe send
		adicionaFieldReceive(lista, fields);
		// Adiciona campos da classe mae
		fields = classe.getSuperclass().getDeclaredFields();
		adicionaFieldReceive(lista, fields);

		// Ordena pela posicao da anotacao
		Collections.sort(lista, new Comparator<Field>() {
			@Override
			public int compare(Field o1, Field o2) {
				MColumnReceive mColumn1 = o1.getAnnotation(MColumnReceive.class);
				MColumnReceive mColumn2 = o2.getAnnotation(MColumnReceive.class);

				// pega posicao do primeiro objeto
				int posicaoo1 = 0;
				if (mColumn1 != null) {
					posicaoo1 = mColumn1.posicao();
				} else {
					MOneToMany manyColumn = o1.getAnnotation(MOneToMany.class);
					posicaoo1 = manyColumn.posicao();
				}
				// pega posicao do segundo objeto
				int posicaoo2 = 0;
				if (mColumn2 != null) {
					posicaoo2 = mColumn2.posicao();
				} else {
					MOneToMany manyColumn = o2.getAnnotation(MOneToMany.class);
					posicaoo2 = manyColumn.posicao();
				}

				return new Long(posicaoo1).compareTo(new Long(posicaoo2));
			}
		});

	}

	private void adicionaFieldReceive(List<Field> lista, Field[] fields) {
		for (Field field : fields) {
			if (field.isAnnotationPresent(MColumnReceive.class) || field.isAnnotationPresent(MOneToMany.class)) {
				lista.add(field);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.persistencia.mainframe.IMainFrameDAOHelper#preencheCamposDoObjetoComValoresRecebidos(java.util.List, java.lang.Object, java.lang.StringBuffer)
	 */
	@Override
	public void preencheCamposDoObjetoComValoresRecebidos(List<Field> fields, Object obj, StringBuffer linha) throws Exception {
		int position = 0;

		StringBuffer s = linha;

		superfor: for (Field field : fields) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(MColumnReceive.class)) {
				MColumnReceive column = field.getAnnotation(MColumnReceive.class);
				int size = new Integer(column.tamanho())/* super. getParsingSize(source, delimitador) */;
				String source = s.substring(position);
				preencheCampoEspecificoDoObjeto(source, field, obj);
				position += size;
			} else if (field.isAnnotationPresent(MOneToMany.class)) {
				List<Field> fieldsManyToOne = new ArrayList<Field>();
				MOneToMany column = field.getAnnotation(MOneToMany.class);
				List lista = new ArrayList();
				for (int i = 0; i < column.quantidade(); i++) {
					// teste
					if (linha.length() < position + column.tamanho()) {
						break;
					}

					String linhaRegistro = linha.substring(position, position + column.tamanho());
					if (!linhaRegistro.toString().trim().equals("")) {
						Object objetoInterno = Reflexao.obterClassesGenericasDeUmaColecaoLista(field).get(0).newInstance();
						preencheListaComCamposAnotadosComReceive(objetoInterno, fieldsManyToOne);
						preencheCamposDoObjetoComValoresRecebidos(fieldsManyToOne, objetoInterno, new StringBuffer(linhaRegistro));
						lista.add(objetoInterno);

					}

					position += column.tamanho();
				}

				field.set(obj, lista);
			}

			System.out.println(field.get(obj));
		}
	}

	private void preencheCampoEspecificoDoObjeto(String source, Field field, Object obj) throws Exception {
		MColumnReceive column = field.getAnnotation(MColumnReceive.class);
		int size = new Integer(column.tamanho())/* super. getParsingSize(source, delimitador) */;
		Class x = field.getType();
		if (x.getSuperclass().equals(Number.class)) {
			Number valor = stringToNumber(source.substring(0, size), column.decimals(), x);
			atribuiValor(valor, field, obj);

		} else if (x.equals(Date.class)) {
			Date valor = stringToDate(source.substring(0, size), column.formato(), size);
			atribuiValor(valor, field, obj);
		} else {
			String valor = (source == null) ? null : source.substring(0, size).trim();
			atribuiValor(valor, field, obj);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.persistencia.mainframe.IMainFrameDAOHelper#atribuiValor(java.lang.Object, java.lang.reflect.Field, java.lang.Object)
	 */
	@Override
	public void atribuiValor(Object value, Field field, Object obj) throws Exception {
		try {
			field.set(obj, value);
		} catch (Exception e) {

			e.printStackTrace();
			throw new Exception("Erro ao atribuir o valor " + value + " ao campo " + field.getName() + " :" + e.getMessage(), e);
		}
	}

	/**
	 * converte o string para Double ou Long pelo metodo DecimalFormat.parse
	 * 
	 * @throws ParseException
	 */
	private Number stringToNumber(String stringValue, int decimals, Class type) throws Exception {
		if ((stringValue == null) || (stringValue.trim().equals(""))) {
			return null;
		}

		Number valorNumerico = null;

		try {
			valorNumerico = (Number) type.getDeclaredMethod("valueOf", new Class[] { String.class })

			.invoke(type, stringValue);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao converter o valor " + stringValue + " para numerico. " + e.getMessage(), e);

		}

		// nao possui nenhum separador
		// desloca a casa decimal para a esquerda "decimals" vezes
		if (decimals > 0) {
			valorNumerico = new Double(Double.parseDouble(stringValue) / Math.pow(10, decimals));
		}

		return valorNumerico;
	}

	private Date stringToDate(String stringDate, String formato, int size) throws ParseException {
		if ((stringDate == null) || stringDate.trim().equals("") || replicate(' ', size).equals(stringDate)) {
			return null;
		}

		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(formato);
		df.setCalendar(new GregorianCalendar());
		df.setLenient(false); // Exige formatos rigidos
		df.parseObject(stringDate);
		Calendar c = df.getCalendar();
		return c.getTime();
	}

	protected String replicate(char c, int times) {
		return replicate(String.valueOf(c), times);
	}

	protected String replicate(String s, int times) {
		StringBuffer result = new StringBuffer("");
		for (int i = 0; i < times; i++) {
			result.append(s);
		}

		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.persistencia.mainframe.IMainFrameDAOHelper#preencheListaComCamposAnotadosComSend(java.lang.Object, java.util.List)
	 */
	@Override
	public void preencheListaComCamposAnotadosComSend(Object obj, List<Field> lista) throws Exception {

		lista.clear();
		Class classe = obj.getClass();
		Field[] fields = classe.getDeclaredFields();
		// lista.addAll(Arrays.asList(fields));

		adicionaFieldSend(lista, fields);
		// Adiciona campos da classe mae que possuem a anotaca de mainframe send

		fields = classe.getSuperclass().getDeclaredFields();
		adicionaFieldSend(lista, fields);

		// Ordena pela posicao da anotacao
		Collections.sort(lista, new Comparator<Field>() {
			@Override
			public int compare(Field o1, Field o2) {
				MColumnSend mColumn1 = o1.getAnnotation(MColumnSend.class);

				MColumnSend mColumn2 = o2.getAnnotation(MColumnSend.class);
				return new Long(mColumn1.posicao()).compareTo(new Long(mColumn2.posicao()));
			}

		});

	}

	private void adicionaFieldSend(List<Field> lista, Field[] fields) {
		for (Field field : fields) {
			if (field.isAnnotationPresent(MColumnSend.class)) {
				lista.add(field);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.persistencia.mainframe.IMainFrameDAOHelper#preencheCamposDoObjetoComValoresAEnviar(java.util.List, java.lang.Object, java.lang.StringBuffer)
	 */
	@Override
	public void preencheCamposDoObjetoComValoresAEnviar(List<Field> fields, Object obj, StringBuffer result) throws Exception {
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(MColumnSend.class)) {
				MColumnSend column = field.getAnnotation(MColumnSend.class);
				Object value = field.get(obj);

				int size = new Integer(column.tamanho());
				Class x = field.getType();
				if (x.getSuperclass().equals(Number.class)) {
					result.append(setAsciiValue(numberToString((Number) value, column.tamanho(), column.decimals()), column.tamanho()));
				} else if (x.equals(Date.class)) {
					result.append(setAsciiValue(dateToString((Date) value, column.formato()), column.tamanho()));
				} else {
					result.append(setAsciiValue((value == null) ? null : new StringBuffer(((String) value).trim()), column.tamanho()));

				}
			}
		}

		System.out.println(result);
	}

	protected StringBuffer numberToString(Number numberValue, int size, int decimals) {

		if (numberValue == null) {
			return null;
		}

		Double d = new Double(Math.abs(numberValue.doubleValue())); // ignora o sinal

		String s;// = strInteira + decimalSeparator + strFracionaria;

		s = Long.toString(Math.round(d.doubleValue() * Math.pow(10, decimals)));

		// acrecenta zeros a esquerda
		StringBuffer result = new StringBuffer("");
		result.append(replicate("0", size - s.length()));
		result.append(s);

		if (size > 0) {
			// trunca o string elimando os digitos de maior ordem
			result.reverse().setLength(size);
			result.reverse();
		}

		return result;
	}

	protected StringBuffer dateToString(Date date, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		// converte a data de acordo com a pattern escolhida ou devolve nulo
		return (date == null) ? null : new StringBuffer(formatter.format(date.getTime()));
	}

	/**
	 * armazena o valor do Field convertido para string (preenche com espacos a direita ate o tamanho definido para o campo)
	 */
	private StringBuffer setAsciiValue(StringBuffer value, int size) {
		StringBuffer strValue = new StringBuffer();
		if (size < 0) {
			strValue = value;
		} else {
			// cria um string de espacoes do tamanho size
			strValue = new StringBuffer(replicate(String.valueOf(' '), size));
			if (value != null) {
				// insere o objeto passado no comeco do string
				strValue.insert(0, value.toString());
				// trunca os espacos a direita que ultrapassam o tamanho
				strValue.setLength(size);

			}
		}
		return strValue;
	}

	protected BrokerClient getBrokerClient(Object object) {
		BrokerClient brokerClient = new BrokerClient();
		brokerClient.setBrokerID(System.getProperty(object.getClass().getSimpleName().toLowerCase() + ".brokerid"));
		brokerClient.setPortNumber(System.getProperty(object.getClass().getSimpleName().toLowerCase() + ".portnumber"));
		brokerClient.setTimeout(System.getProperty(object.getClass().getSimpleName().toLowerCase() + ".timeout"));
		brokerClient.setServerName(System.getProperty(object.getClass().getSimpleName().toLowerCase() + ".servername"));
		brokerClient.setServerClass(System.getProperty(object.getClass().getSimpleName().toLowerCase() + ".serverclass"));
		brokerClient.setServiceName(System.getProperty(object.getClass().getSimpleName().toLowerCase() + ".servicename"));
		brokerClient.configure();
		return brokerClient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.persistencia.mainframe.IMainFrameDAOHelper#sendReceive(java.lang.Object, int, java.lang.String)
	 */
	@Override
	public Object sendReceive(Object objeto, int posicaoInicial, String... codigos) throws BrokerException, Exception {
		List<Field> fieldList = new ArrayList<Field>();
		StringBuffer send = new StringBuffer();
		StringBuffer receive = new StringBuffer();
		preencheListaComCamposAnotadosComSend(objeto, fieldList);
		preencheCamposDoObjetoComValoresAEnviar(fieldList, objeto, send);
		EntityBroker entityBrokerAnnotation = objeto.getClass().getAnnotation(EntityBroker.class);
		String stringEnvio = entityBrokerAnnotation.programa() + send.toString();

		String log = stringEnvio.trim().replace(" ", "").replace("\0", "");
		Calendar agora = new GregorianCalendar();
		logF.info("##BROKER ENVIO Hora: " + agora.get(Calendar.HOUR) + ":" + agora.get(Calendar.MINUTE) + ":" + agora.get(Calendar.SECOND) + ":" + agora.get(Calendar.MILLISECOND) + " BROKER: " + toString() + " - " + log);

		long cronometroInicio = new GregorianCalendar().getTimeInMillis();
		String retorno = null;
		ExpiringCache cacheEntidade = cache.get(objeto.getClass());
		if (cacheEntidade == null) {
			EntityBroker annotation = objeto.getClass().getAnnotation(EntityBroker.class);
			cacheEntidade = new ExpiringCache(annotation.tempoDeVida(), annotation.limiteTempoSemAcesso(), annotation.quantidadeMaximaPorCache(), annotation.tempoAtualizacaoCache());
			cache.put(objeto.getClass(), cacheEntidade);
		} else {

			retorno = (String) cacheEntidade.recover(stringEnvio);
			agora = new GregorianCalendar();
			logF.info("##BROKER RETORNO CACHE Hora: " + agora.get(Calendar.HOUR) + ":" + agora.get(Calendar.MINUTE) + ":" + agora.get(Calendar.SECOND) + ":" + agora.get(Calendar.MILLISECOND) + " BROKER: " + this.toString() + " ##TEMPO:"
					+ (new GregorianCalendar().getTimeInMillis() - cronometroInicio) + "ms - " + retorno);
		}

		if (retorno == null) {
			retorno = getBrokerClient(objeto).callMainFrame(stringEnvio);
			agora = new GregorianCalendar();
			logF.info("##BROKER RETORNO Hora: " + agora.get(Calendar.HOUR) + ":" + agora.get(Calendar.MINUTE) + ":" + agora.get(Calendar.SECOND) + ":" + agora.get(Calendar.MILLISECOND) + " BROKER: " + this.toString() + " ##TEMPO:"
					+ (new GregorianCalendar().getTimeInMillis() - cronometroInicio) + "ms - " + retorno);
			// System.out.println("chamei o mainframe");
			// //para testes
			// retorno =
			// "000                                                                      ANGELA TESTE RENACH                     001400020002                                                                                                                                                                MG02"
			// +
			// "01/01/2011GUH0960516-91 DIRIGIR SOB A INFLUENCIA DE ALCOOL                                                                                      14:53AV. DOS ANDRADAS                   0007"
			// +
			// "01/01/2011GUH0960516-91 DIRIGIR SOB A INFLUENCIA DE ALCOOL                                                                                      14:53AV. DOS ANDRADAS                   0007";
			boolean sucesso = false;
			for (String codigo : codigos) {
				if (retorno.substring(posicaoInicial).startsWith(codigo)) {
					sucesso = true;
					break;
				}
			}

			if (sucesso) {
				cacheEntidade.admit(stringEnvio, retorno);
				// cache.put(objeto.getClass(), cacheEntidade);
			} else {
				throw new BrokerException(retorno.toString());
			}

		} else {
			// System.out.println("usando o cache");
		}

		receive = new StringBuffer(retorno);

		preencheListaComCamposAnotadosComReceive(objeto, fieldList);
		preencheCamposDoObjetoComValoresRecebidos(fieldList, objeto, receive);

		return objeto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.persistencia.mainframe.IMainFrameDAOHelper#sendReceive(java.lang.Object)
	 */
	@Override
	public Object sendReceive(Object objeto) throws BrokerException, Exception {
		return sendReceive(objeto, 0, "000", "   ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.prodigio.persistencia.mainframe.IMainFrameDAOHelper#sendReceive(java.lang.Object, int)
	 */
	@Override
	public Object sendReceive(Object objeto, int posicaoInicial) throws BrokerException, Exception {
		return sendReceive(objeto, posicaoInicial, "000", "   ");
	}

}
