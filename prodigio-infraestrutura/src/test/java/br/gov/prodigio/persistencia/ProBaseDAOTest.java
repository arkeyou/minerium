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
package br.gov.prodigio.persistencia;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.BetweenExpression;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.CriterionEntry;
import org.hibernate.internal.CriteriaImpl.OrderEntry;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.hibernate.sql.JoinType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import br.gov.prodemge.test.AbstractDbUnitJpaTest;
import br.gov.prodemge.teste.entidades.AlbumVO;
import br.gov.prodemge.teste.entidades.BandaVO;
import br.gov.prodemge.teste.entidades.InstrumentoVO;
import br.gov.prodemge.teste.entidades.IntegranteVO;
import br.gov.prodemge.teste.entidades.PaisVO;
import br.gov.prodigio.comuns.excecoes.RuntimeExceptionDao;
import br.gov.prodigio.persistencia.ProBaseDAO.DadosDeSuporteAPesquisa;

public class ProBaseDAOTest {

	private static ProBaseDAO baseDAO;
	private static EntityManager em;

	private static Date dataInicial;
	private static Date dataFinal;

	@BeforeClass
	public static void setup() {
		try {
			baseDAO = spy(ProBaseDAO.class.newInstance());
			em = spy(AbstractDbUnitJpaTest.getInstance());

			Mockito.doReturn(em).when(baseDAO).getEntityManager();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataInicial = sdf.parse("01/01/2018");
			dataFinal = sdf.parse("10/01/2018");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Before
	public void beforeEachTest() {
		try {
			baseDAO.iniciarTransacao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @After
	public void afterEachTest() {
		try {
			baseDAO.confirmarTransacao();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				baseDAO.cancelarTransacao(e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	// @Test
	// public void deveGravarEntidadeNaoPersistida() throws Exception {
	// BandaVO banda = new BandaVO();
	// banda.setDescricao("teste");
	//
	// Object gravar = baseDAO.gravar(banda);
	//
	// verify(em, times(1)).persist(Mockito.any());
	// }

	// @Test
	// public void deveGravarEntidadeNaoPersistidaComID() throws Exception {
	// ExemploVO exemploVO = new ExemploVO();
	// exemploVO.setId(2L);
	// exemploVO.setDescricao("bbb");
	//
	// Object gravar = baseDAO.gravar(exemploVO);
	//
	// // verify(em, times(1)).persist(Mockito.any());
	//
	// }

	@Test
	public void deveRetornarEntityManager() {
		EntityManager entityManager = baseDAO.getEntityManager();
		assertThat(entityManager, is(instanceOf(EntityManager.class)));
	}

	@Test
	public void acrecentaIdNasProjecoes_ListaProjecaoVazia() {
		String[] projecoesIniciais = null;

		String[] projecoesFinais = baseDAO.acrecentaIdNasProjecoes(projecoesIniciais);

		assertThat(projecoesFinais, arrayWithSize(1));
		assertThat(projecoesFinais, arrayContaining("id"));
	}

	@Test
	public void acrecentaIdNasProjecoes_ListaProjecaoSemId() {
		String[] projecoesIniciais = new String[] { "nome", "descricao" };

		String[] projecoesFinais = baseDAO.acrecentaIdNasProjecoes(projecoesIniciais);

		assertThat(projecoesFinais, arrayWithSize(3));
		assertThat(projecoesFinais, arrayContaining("nome", "descricao", "id"));
	}

	@Test
	public void aplicarFiltroExclusaoLogica_AplicarFiltroExclusaoLogica() {
		String[] projecoes = new String[] { "nome", "descricao" };
		BandaVO banda = new BandaVO();

		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = null;
		criteria = hibernateSession.createCriteria(banda.getClass());

		baseDAO.aplicarFiltroExclusaoLogica(banda, criteria, projecoes);

		String sql = "statusDoRegistro<>EXCLUIDO";
		boolean containsFiltroExclusaoLogica = checkIfContainsCriteria(criteria, sql);

		assertThat(containsFiltroExclusaoLogica, is(true));

	}

	@Test
	public void aplicarFiltroExclusaoLogica_NaoAplicarFiltroExclusaoLogica() {
		String[] projecoes = new String[] { "nome", "descricao", "statusDoRegistro" };
		BandaVO banda = new BandaVO();

		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = null;
		criteria = hibernateSession.createCriteria(banda.getClass());

		baseDAO.aplicarFiltroExclusaoLogica(banda, criteria, projecoes);

		String sql = "statusDoRegistro<>EXCLUIDO";
		boolean containsFiltroExclusaoLogica = checkIfContainsCriteria(criteria, sql);

		assertThat(containsFiltroExclusaoLogica, is(false));
	}

	@Test
	public void configuraListaOrdenacao_AtributoComOrdenacao() throws Exception {
		String[] projecoes = new String[] { "nome", "descricao ASC", "statusDoRegistro" };
		List<String> novaLista = baseDAO.configuraListaOrdenacao(projecoes);

		assertThat(novaLista, contains("descricao ASC", "nome", "statusDoRegistro"));
	}

	@Test
	public void configuraListaOrdenacao_AtributoSemOrdenacao() throws Exception {
		String[] projecoes = new String[] { "nome", "descricao", "statusDoRegistro" };
		List<String> novaLista = baseDAO.configuraListaOrdenacao(projecoes);

		assertThat(novaLista, contains("nome", "descricao", "statusDoRegistro"));
	}

	@Test
	public void removeComandosSQLdasProjecoes_AtributosComSQL() throws Exception {
		String[] projecoes = new String[] { "nome desc", "descricao asc", "statusDoRegistro asc" };
		String[] novaLista = baseDAO.removeComandosSQLdasProjecoes(projecoes);

		assertThat(novaLista, arrayContaining("nome", "descricao", "statusDoRegistro"));

	}

	@Test
	public void removeComandosSQLdasProjecoes_AtributosSemSQL() throws Exception {
		String[] projecoes = new String[] { "nome", "descricao", "statusDoRegistro" };
		String[] novaLista = baseDAO.removeComandosSQLdasProjecoes(projecoes);

		assertThat(novaLista, arrayContaining("nome", "descricao", "statusDoRegistro"));

	}

	@Test
	public void retiraTransientesDaProjecao_SemTransientes() {
		String[] projecoes = new String[] { "nome", "descricao", "statusDoRegistro", "nrVersao" };

		BandaVO exemplo = new BandaVO();
		String[] novaLista = baseDAO.retiraTransientesDaProjecao(exemplo, projecoes);

		assertThat(novaLista, arrayContaining("nome", "descricao", "statusDoRegistro", "nrVersao"));
	}

	@Test
	public void retiraTransientesDaProjecao_TransientesPrimeiroNivel() {
		String[] projecoes = new String[] { "nome", "descricao", "statusDoRegistro", "nrVersao", "formacao" };

		BandaVO exemplo = new BandaVO();
		String[] novaLista = baseDAO.retiraTransientesDaProjecao(exemplo, projecoes);

		assertThat(novaLista, arrayContaining("nome", "descricao", "statusDoRegistro", "nrVersao"));
	}

	@Test
	public void retiraTransientesDaProjecao_TransientesSegundoNivel() {
		String[] projecoes = new String[] { "nome", "descricao", "statusDoRegistro", "nrVersao", "estiloMusical.ano" };

		BandaVO exemplo = new BandaVO();
		String[] novaLista = baseDAO.retiraTransientesDaProjecao(exemplo, projecoes);

		assertThat(novaLista, arrayContaining("nome", "descricao", "statusDoRegistro", "nrVersao"));
	}

	@Test(expected = RuntimeExceptionDao.class)
	public void retiraTransientesDaProjecao_AtributoInexistente() {
		String[] projecoes = new String[] { "atributoInexistente" };

		BandaVO exemplo = new BandaVO();
		String[] novaLista = baseDAO.retiraTransientesDaProjecao(exemplo, projecoes);
	}

	@Test
	public void retiraTransientesDaOrdenacao_ListaComTransiente() {
		String[] projecoes = new String[] { "nome" };
		String[] ordenacao = new String[] { "formacao desc", "nome" };

		List<String> projecoesSemTransientes = baseDAO.retiraTransientesDaOrdenacao(Arrays.asList(ordenacao), projecoes);

		assertThat(projecoesSemTransientes, contains("nome"));
	}

	@Test
	public void retiraTransientesDaOrdenacao_ListaSemTransiente() {
		String[] projecoes = new String[] { "nome", "descricao" };
		String[] ordenacao = new String[] { "nome", "descricao" };

		List<String> projecoesSemTransientes = baseDAO.retiraTransientesDaOrdenacao(Arrays.asList(ordenacao), projecoes);

		assertThat(projecoesSemTransientes, contains("nome", "descricao"));
	}

	@Test
	public void recuperaProjecaoDeColecoes_ProjecaoComColecao() {
		String[] projecoes = new String[] { "nome", "nrVersao", "integrantes.nome", "integrantes.descricao" };

		BandaVO exemplo = new BandaVO();
		String[] projecoesDeColecoes = baseDAO.recuperaProjecaoDeColecoes(exemplo, projecoes);

		assertThat(projecoesDeColecoes, arrayContaining("integrantes.nome", "integrantes.descricao"));
	}

	@Test
	public void recuperaProjecaoDeColecoes_ProjecaoSemColecao() {
		String[] projecoes = new String[] { "nome", "nrVersao" };

		BandaVO exemplo = new BandaVO();
		String[] projecoesDeColecoes = baseDAO.recuperaProjecaoDeColecoes(exemplo, projecoes);

		assertThat(projecoesDeColecoes, is(emptyArray()));
	}

	@Test(expected = RuntimeExceptionDao.class)
	public void recuperaProjecaoDeColecoes_ProjecaoComColecaoInexistente() {
		String[] projecoes = new String[] { "nome", "nrVersao", "atributoInexistente.nome" };

		BandaVO exemplo = new BandaVO();
		String[] projecoesDeColecoes = baseDAO.recuperaProjecaoDeColecoes(exemplo, projecoes);

	}

	@Test
	public void retiraProjecoesDeColecaoDaOrdenacaoPrincipal_ProjecaoComColecoes() {
		String[] projecoesDeColecoes = new String[] { "integrantes.nome", "integrantes.descricao" };
		String[] ordenacao = new String[] { "nome", "descricao", "integrantes.nome", "integrantes.descricao" };

		List<String> novaLista = baseDAO.retiraProjecoesDeColecaoDaOrdenacaoPrincipal(projecoesDeColecoes, Arrays.asList(ordenacao));

		assertThat(novaLista, contains("nome", "descricao"));
	}

	@Test
	public void retiraProjecoesDeColecaoDaOrdenacaoPrincipal_ProjecaoSemColecoes() {
		String[] projecoesDeColecoes = new String[0];
		String[] ordenacao = new String[] { "nome", "descricao" };

		List<String> novaLista = baseDAO.retiraProjecoesDeColecaoDaOrdenacaoPrincipal(projecoesDeColecoes, Arrays.asList(ordenacao));

		assertThat(novaLista, contains("nome", "descricao"));
	}

	@Test
	public void recuperaProjecaoDeBytes_AtributoBytePrimeiroNivel() {
		String[] projecoes = new String[] { "nome", "descricao", "logo" };

		BandaVO banda = new BandaVO();
		String[] projecoesCamposByte = baseDAO.recuperaProjecaoDeBytes(banda, projecoes);

		assertThat(projecoesCamposByte, arrayContaining("logo"));

	}

	@Test
	public void recuperaProjecaoDeBytes_AtributoByteSegundoNivel() {
		String[] projecoes = new String[] { "nome", "descricao", "logo", "estiloMusical.imagem" };

		BandaVO banda = new BandaVO();
		String[] projecoesCamposByte = baseDAO.recuperaProjecaoDeBytes(banda, projecoes);

		assertThat(projecoesCamposByte, arrayContaining("logo"));

	}

	@Test
	public void recuperaProjecaoDeBytes_SemAtributoByte() {
		String[] projecoes = new String[] { "nome", "descricao" };

		BandaVO banda = new BandaVO();
		String[] projecoesCamposByte = baseDAO.recuperaProjecaoDeBytes(banda, projecoes);

		assertThat(projecoesCamposByte, is(emptyArray()));

	}

	@Test
	public void retiraProjecoesDeColecaoDaProjecaoPrincipal() {
		String[] projecoes = new String[] { "primeiroValor", "segundoValor" };
		String[] projecaoColecoes = new String[] { "segundoValor" };

		String[] novaProjecao = baseDAO.retiraProjecoesDeColecaoDaProjecaoPrincipal(projecaoColecoes, projecoes);

		assertThat(novaProjecao, arrayContaining("primeiroValor"));

	}

	@Test
	public void retiraProjecoesDeColecaoDaProjecaoPrincipal_ProjecaoColecaoVazia() {
		String[] projecoes = new String[] { "primeiroValor", "segundoValor" };
		String[] projecaoColecoes = new String[0];

		String[] novaProjecao = baseDAO.retiraProjecoesDeColecaoDaProjecaoPrincipal(projecaoColecoes, projecoes);

		assertThat(novaProjecao, arrayContaining("primeiroValor", "segundoValor"));

	}

	@Test
	public void recuperaObjetosComSuasProjecoes() {
		String[] projecoes = new String[] { "nome", "descricao", "integrantes.nome", "estiloMusical.descricao" };
		Map<String, Set<String>> objeto = baseDAO.recuperaObjetosComSuasProjecoes(projecoes);

		assertThat(objeto, hasKey(""));
		Set<String> objetoPrincipal = objeto.get("");
		assertThat(objetoPrincipal, containsInAnyOrder("nome", "descricao"));

		assertThat(objeto, hasKey("integrantes"));
		Set<String> integrantes = objeto.get("integrantes");
		assertThat(integrantes, containsInAnyOrder("nome"));

		assertThat(objeto, hasKey("estiloMusical"));
		Set<String> estiloMusical = objeto.get("estiloMusical");
		assertThat(estiloMusical, containsInAnyOrder("descricao"));
	}

	@Test
	public void acrecentaIdNasProjecoes_ProjecoesComId() {
		String[] projecoes = new String[] { "id", "nome", };
		Map<String, Set<String>> agregados = new HashMap<String, Set<String>>();

		Set<String> valores = new HashSet<String>();
		valores.add("nome");
		valores.add("id");
		agregados.put("", valores);

		String[] projecoesComId = baseDAO.acrecentaIdNasProjecoes(projecoes, agregados);

		assertThat(projecoesComId, arrayContainingInAnyOrder("nome", "id"));

	}

	@Test
	public void acrecentaIdNasProjecoes_ProjecoesSemId() {
		String[] projecoes = new String[] { "nome", "descricao", "integrantes.nome", "estiloMusical.descricao" };
		Map<String, Set<String>> agregados = new HashMap<String, Set<String>>();

		Set<String> valores = new HashSet<String>();
		valores.add("nome");
		valores.add("descricao");
		agregados.put("", valores);

		valores = new HashSet<String>();
		valores.add("nome");
		agregados.put("integrantes", valores);

		valores = new HashSet<String>();
		valores.add("descricao");
		agregados.put("estiloMusical", valores);

		String[] projecoesComId = baseDAO.acrecentaIdNasProjecoes(projecoes, agregados);

		assertThat(projecoesComId, arrayContainingInAnyOrder("nome", "descricao", "integrantes.nome", "estiloMusical.descricao", "id", "integrantes.id", "estiloMusical.id"));
	}

	@Test
	public void montaRestricoes_ExemploComId() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setId(1L);

		BandaVO banda2 = new BandaVO();

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String sql = "id=1";

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorString() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setNome("Metallica");

		BandaVO banda2 = new BandaVO();

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String sql = "nome=Metallica";

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorStringPercentInicio() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setNome("%Metallica");

		BandaVO banda2 = new BandaVO();

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String sql = "nome=%%Metallica";

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorStringPercentFim() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setNome("Metallica%");

		BandaVO banda2 = new BandaVO();

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String sql = "nome=Metallica%%";

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorStringPercenIniciotFim() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setNome("%Metallica%");

		BandaVO banda2 = new BandaVO();

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String sql = "nome=%%Metallica%%";

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorStringBarraIniciotFim() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setNome("\"Metallica\"");

		BandaVO banda2 = new BandaVO();

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String sql = "nome=Metallica";

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorDateSomenteDataInicio() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setFundacao(dataInicial);

		BandaVO banda2 = new BandaVO();

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String[] sql = new String[] { "fundacao>=mon jan 01 00:00:00 brst 2018", "fundacao<=tue jan 02 00:00:00 brst 2018" };

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorDateInicioEFim() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setFundacao(dataInicial);

		BandaVO banda2 = new BandaVO();
		banda2.setFundacao(dataFinal);

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String[] sql = new String[] { "fundacao>=mon jan 01 00:00:00 brst 2018", "fundacao<=wed jan 10 00:00:00 brst 2018" };

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorNumber() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setNumeroIntegrantes(4);

		BandaVO banda2 = new BandaVO();

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String[] sql = new String[] { "numerointegrantes=4" };

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorNumberBetween() throws Exception {
		BandaVO banda1 = new BandaVO();
		banda1.setNumeroIntegrantes(4);

		BandaVO banda2 = new BandaVO();
		banda2.setNumeroIntegrantes(5);

		Criteria criteria = listarBaseadoExemploHelper(banda1, banda2);

		String[] sql = new String[] { "numeroIntegrantes between 4 and 5" };

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorList() throws Exception {
		InstrumentoVO instrumento = new InstrumentoVO();
		instrumento.setDescricao("guitarra");

		IntegranteVO integrante = new IntegranteVO();
		if (integrante.getInstrumentos() == null)
			integrante.setInstrumentos(new ArrayList<InstrumentoVO>());

		integrante.getInstrumentos().add(instrumento);

		BandaVO banda = new BandaVO();
		if (banda.getIntegrantes() == null)
			banda.setIntegrantes(new ArrayList<IntegranteVO>());

		banda.getIntegrantes().add(integrante);

		Criteria criteria = listarBaseadoExemploHelper(banda, banda);

		String[] sql = new String[] { "integrantes.instrumentos.descricao=guitarra" };

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorSet() throws Exception {
		AlbumVO album = new AlbumVO();
		album.setAno(1984);

		BandaVO banda = new BandaVO();
		if (banda.getDiscografia() == null)
			banda.setDiscografia(new HashSet<AlbumVO>());

		banda.getDiscografia().add(album);

		Criteria criteria = listarBaseadoExemploHelper(banda, banda);

		String[] sql = new String[] { "discografia.ano=1984" };

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void montaRestricoes_ExemploComValorEmbeddable() throws Exception {
		PaisVO pais = new PaisVO();
		pais.setNome("EUA");

		BandaVO banda = new BandaVO();
		banda.setOrigem(pais);

		Criteria criteria = listarBaseadoExemploHelper(banda, banda);

		String[] sql = new String[] { "origem.nome=eua" };

		assertThat(checkIfContainsCriteria(criteria, sql), is(true));
	}

	@Test
	public void configuraProjecao_ProjecaoSimples() {
		ProjectionList projectionList = Projections.projectionList();

		String[] projecoes = new String[] { "id" };

		BandaVO exemplo = new BandaVO();

		EntityManager em = baseDAO.getEntityManager();

		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = null;
		criteria = hibernateSession.createCriteria(exemplo.getClass());

		Criterion exp = Restrictions.eq("nome", "metallica");
		criteria.add(exp);

		baseDAO.configuraProjecao(criteria, projectionList, exemplo, projecoes);

		assertThat(projectionList.getLength(), is(1));
	}

	@Test
	public void configuraProjecao_ProjecaoComposta() {
		ProjectionList projectionList = Projections.projectionList();

		String[] projecoes = new String[] { "id", "integrantes.nome" };

		BandaVO exemplo = new BandaVO();

		EntityManager em = baseDAO.getEntityManager();

		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = null;
		criteria = hibernateSession.createCriteria(exemplo.getClass());

		Criterion exp = Restrictions.eq("nome", "metallica");
		criteria.add(exp);

		baseDAO.configuraProjecao(criteria, projectionList, exemplo, projecoes);

		assertThat(projectionList.getLength(), is(2));
	}

	@Test
	public void criaCriteriaParaObjetoAgregadoRaiz_ForcarCriarSubcriteria() {
		EntityManager em = baseDAO.getEntityManager();

		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = spy(hibernateSession.createCriteria(BandaVO.class));

		Criterion exp = Restrictions.eq("nome", "metallica");
		criteria.add(exp);

		String objetoAgregadoRaiz = "integrantes";

		Criteria novaCriteria = baseDAO.criaCriteriaParaObjetoAgregadoRaiz(criteria, objetoAgregadoRaiz);

		verify(criteria, times(1)).createCriteria(Mockito.anyString(), Mockito.anyString(), Mockito.any());
	}

	@Test
	public void criaCriteriaParaObjetoAgregadoRaiz_RecuperarSubcriteriaExistente() {
		EntityManager em = baseDAO.getEntityManager();

		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = spy(hibernateSession.createCriteria(BandaVO.class));

		Criterion exp = Restrictions.eq("nome", "metallica");
		criteria.add(exp);

		String objetoAgregadoRaiz = "integrantes";

		Criteria criteria2 = criteria.createCriteria(objetoAgregadoRaiz, objetoAgregadoRaiz, JoinType.LEFT_OUTER_JOIN);

		Criteria novaCriteria = baseDAO.criaCriteriaParaObjetoAgregadoRaiz(criteria2, objetoAgregadoRaiz);

		verify(criteria, times(1)).createCriteria(Mockito.anyString(), Mockito.anyString(), Mockito.any());
	}

	@Test
	public void criaCriteriaParaObjetoAgregadoRaiz_RecuperarSubcriteriaMais2Niveis() {
		EntityManager em = baseDAO.getEntityManager();

		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = spy(hibernateSession.createCriteria(BandaVO.class));

		Criterion exp = Restrictions.eq("nome", "metallica");
		criteria.add(exp);

		String objetoAgregadoRaiz = "integrantes";

		Criteria criteria2 = criteria.createCriteria(objetoAgregadoRaiz, objetoAgregadoRaiz, JoinType.LEFT_OUTER_JOIN);

		objetoAgregadoRaiz = "instrumentos";

		Criteria criteria3 = criteria2.createCriteria(objetoAgregadoRaiz, objetoAgregadoRaiz, JoinType.LEFT_OUTER_JOIN);

		Criteria novaCriteria = baseDAO.criaCriteriaParaObjetoAgregadoRaiz(criteria3, objetoAgregadoRaiz);

		verify(criteria, times(1)).createCriteria(Mockito.anyString(), Mockito.anyString(), Mockito.any());
	}

//	@Test
//	public void configuraOrdenacao_PropriedadeSimplesASC() {
//
//		Session hibernateSession = (Session) em.getDelegate();
//		Criteria criteria = hibernateSession.createCriteria(BandaVO.class);
//
//		String[] ordenacao = new String[] { "nome" };
//
//		baseDAO.configuraOrdenacao(criteria, ordenacao);
//
//		System.out.println();
//	}
//
//	@Test
//	public void configuraOrdenacao_PropriedadeSimplesDESC() {
//
//		Session hibernateSession = (Session) em.getDelegate();
//		Criteria criteria = hibernateSession.createCriteria(BandaVO.class);
//
//		String[] ordenacao = new String[] { "integrantes.descricao asc", "nome DESC" };
//
//		baseDAO.configuraOrdenacao(criteria, ordenacao);
//
//		getOrdersFromCriteria(criteria);
//
//		System.out.println();
//	}
//
//	@Test
//	public void configuraOrdenacao_PropriedadeComposta() {
//
//		Session hibernateSession = (Session) em.getDelegate();
//		Criteria criteria = hibernateSession.createCriteria(BandaVO.class);
//
//		String[] ordenacao = new String[] { "integrantes.nome" };
//
//		baseDAO.configuraOrdenacao(criteria, ordenacao);
//
//		System.out.println();
//	}
//
//	@Test
//	public void teste() {
//		BandaVO bandaVO = new BandaVO();
//		bandaVO.setNome("Metallica");
//
//		Set<ProBaseVO> listarBaseadoNoExemplo = baseDAO.listarBaseadoNoExemplo(bandaVO, null, 0, 1, "nome", "integrantes.id", "integrantes.nome");
//
//		System.out.println();
//	}

	private Criteria listarBaseadoExemploHelper(Object exemplo, Object exemplo2) {
		EntityManager em = baseDAO.getEntityManager();
		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = null;
		criteria = hibernateSession.createCriteria(exemplo.getClass());
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		DadosDeSuporteAPesquisa dadosDeSuporteAPesquisa = baseDAO.new DadosDeSuporteAPesquisa();

		try {
			baseDAO.montaRestricoes(exemplo, exemplo2, criteria, dadosDeSuporteAPesquisa);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return criteria;
	}

	private boolean checkIfContainsCriteria(final Criteria criteria, final String... sql) {
		Iterator criterions = ((CriteriaImpl) criteria).iterateExpressionEntries();

		if (!criterions.hasNext())
			return false;

		Map<String, Boolean> sqls = Arrays.asList(sql).stream().map(el -> el.toLowerCase()).collect(Collectors.toMap(e -> e, e -> true));

		while (criterions.hasNext()) {
			CriterionEntry object = (CriterionEntry) criterions.next();
			String item = getSQLFromCriteria(object);
			if (sqls.get(item) == null)
				return false;
		}
		return true;
	}

	private void getOrdersFromCriteria(Criteria criteria) {
		Iterator<OrderEntry> iterateOrderings = ((CriteriaImpl) criteria).iterateOrderings();
		String alias = criteria.getAlias();
		while (iterateOrderings.hasNext()) {
			OrderEntry next = iterateOrderings.next();
		}
	}

	private String getSQLFromCriteria(CriterionEntry object) {
		try {
			Criterion criterion = object.getCriterion();
			Criteria criteria = object.getCriteria();

			String path = "";
			if (criteria instanceof Subcriteria)
				path = getPath(criteria);

			if (criterion instanceof BetweenExpression) {
				try {
					Field fLow = criterion.getClass().getDeclaredField("lo");
					fLow.setAccessible(true);
					Object low = fLow.get(criterion);

					Field fHigh = criterion.getClass().getDeclaredField("hi");
					fHigh.setAccessible(true);
					Object high = fHigh.get(criterion);

					Field fPropertyName = criterion.getClass().getDeclaredField("propertyName");
					fPropertyName.setAccessible(true);
					Object propertyName = fPropertyName.get(criterion);

					return String.format("%s%s between %s and %s", path, propertyName, low, high).toLowerCase();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Field fValue = criterion.getClass().getDeclaredField("value");
			fValue.setAccessible(true);
			Object value = fValue.get(criterion);

			Field fPropertyName = criterion.getClass().getDeclaredField("propertyName");
			fPropertyName.setAccessible(true);
			Object propertyName = fPropertyName.get(criterion);

			Field fOp = null;
			try {
				fOp = criterion.getClass().getDeclaredField("op");
				fOp.setAccessible(true);
			} catch (Exception e) {
			}

			Object op;
			if (fOp != null)
				return String.format("%s%s%s%s", path, propertyName, fOp.get(criterion), value).toLowerCase();
			else
				return String.format("%s%s=%s", path, propertyName, value).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private String getPath(Criteria criteria) {
		if (criteria instanceof CriteriaImpl) {
			return "";
		} else {
			Subcriteria subcriteria = (Subcriteria) criteria;
			return getPath(subcriteria.getParent()) + subcriteria.getPath() + ".";
		}
	}

}
