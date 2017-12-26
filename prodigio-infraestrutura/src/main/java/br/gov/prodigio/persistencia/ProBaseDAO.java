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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.Transient;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.JDBCException;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.prodigio.comuns.IProBaseDAO;
import br.gov.prodigio.comuns.anotacoes.EntityBroker;
import br.gov.prodigio.comuns.anotacoes.ExclusaoLogica;
import br.gov.prodigio.comuns.constantes.Constantes;
import br.gov.prodigio.comuns.excecoes.RuntimeExceptionDao;
import br.gov.prodigio.comuns.exception.BrokerException;
import br.gov.prodigio.comuns.utils.Reflexao;
import br.gov.prodigio.entidades.ProBaseVO;
import br.gov.prodigio.entidades.RecordDataSet;
import br.gov.prodigio.entidades.StatusDoRegistro;
import br.gov.prodigio.persistencia.mainframe.IMainFrameDAOHelper;

public class ProBaseDAO implements IProBaseDAO {
	private static final Logger log = LoggerFactory.getLogger(ProBaseDAO.class);

	public ProBaseDAO() {
	}

	@Override
	public Object gravar(Object objeto) throws Exception {
		EntityManager em = getEntityManager();
		try {
			ProBaseVO proBase = (ProBaseVO) objeto;
			if (proBase.getId() == null) {
				proBase.setTsMovimentacao(new Date());
				proBase.setTpOperacao("I");
				persist(proBase);
			} else {
				boolean objetoAindaNaoPersistido = objetoAindaNaoPersistido(proBase);
				if (objetoAindaNaoPersistido) {
					proBase.setId(null);
					proBase.setTpOperacao("I");
					proBase.setTsMovimentacao(new Date());
					persist(proBase);
				} else {
					proBase.setTsMovimentacao(new Date());
					proBase.setTpOperacao("A");
					merge(proBase);
				}
			}
			em.flush();
			return proBase.getId();
		} catch (PersistenceException e) {
			em.clear();
			if (e.getCause() instanceof ConstraintViolationException || e.getCause() instanceof DataException || e.getCause() instanceof SQLGrammarException) {
				JDBCException e1 = (JDBCException) e.getCause();
				if (e1.getCause() instanceof BatchUpdateException) {
					SQLException e2 = ((BatchUpdateException) e1.getCause()).getNextException();
					if (e2.getMessage().contains("violates not-null constraint")) {
						throw new Exception("O campo " + e2.getMessage().replace("null value in column", "").replace("violates not-null constraint", "") + " não pode ser nulo");
					}
					throw e2;
				} else if (e1.getCause() instanceof SQLException) {
					SQLException e2 = (SQLException) e1.getCause();
					throw e2;
				}
				throw e1;
			}
			throw e;
		} catch (javax.validation.ConstraintViolationException e) {
			log.warn("Erro  de validacão: " + e.getConstraintViolations());
			throw e;
		} catch (Exception e) {
			log.error("Erro  ao gravar objeto", e);
			throw e;
		}
	}

	protected void persist(Object objeto) {
		EntityManager em = getEntityManager();
		try {
			((Session) em.getDelegate()).setDefaultReadOnly(false);
			em.detach(objeto);
			em.persist(objeto);
		} catch (PersistenceException e) {
			trataErrosDePersistencia(em, e);
		} catch (Exception e) {
			log.warn("Não conseguiu gravar a entidade", e);
			throw new RuntimeExceptionDao(e);
		} finally {
			((Session) em.getDelegate()).setDefaultReadOnly(true);
		}
	}

	protected void merge(Object objeto) {
		EntityManager em = getEntityManager();
		em.contains(objeto);
		try {
			((Session) em.getDelegate()).setDefaultReadOnly(false);
			em.detach(objeto);
			em.merge(objeto);
		} catch (PersistenceException e) {
			trataErrosDePersistencia(em, e);
		} catch (Exception e) {
			log.warn("Não conseguiu gravar a entidade", e);
			throw new RuntimeExceptionDao(e);
		} finally {
			((Session) em.getDelegate()).setDefaultReadOnly(true);
		}
	}

	@Override
	public void excluir(Object objeto) throws Exception {
		EntityManager em = getEntityManager();
		try {
			if (objeto.getClass().isAnnotationPresent(ExclusaoLogica.class)) {
				((ProBaseVO) objeto).setStatusDoRegistro(StatusDoRegistro.EXCLUIDO);
				merge(objeto);
			} else {
				if (((ProBaseVO) objeto).getId() != null) {
					objeto = em.merge(objeto);
					((Session) em.getDelegate()).setDefaultReadOnly(false);
					// em.detach(objeto); ocasionou erro ao excluir a demanda
					em.remove(objeto);
					em.flush();
				}
			}

		} catch (PersistenceException e) {
			trataErrosDePersistencia(em, e);
		} catch (Exception re) {
			throw new RuntimeExceptionDao("Não foi possível excluir objeto", re);
		} finally {
			((Session) em.getDelegate()).setDefaultReadOnly(true);
		}
	}

	public EntityManager getEntityManager() {
		try {
			EntityManager em = ProDAOHelper.entityManager();
			return em;
		} catch (PersistenceException p) {
			throw new RuntimeExceptionDao("Não foi possível recuperar o Entity Manager", p);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<ProBaseVO> listar(ProBaseVO vo) throws Exception {
		String nomeDaEntidade = vo.getClass().getSimpleName();
		try {
			String quaryNamed = "listaDe" + nomeDaEntidade.substring(0, nomeDaEntidade.length());
			EntityManager em = getEntityManager();
			String namedQuery = recuperarNamedQuery(vo, quaryNamed);
			namedQuery = aplicarFiltroExclusaoLogica(namedQuery);
			Query query = em.createQuery(namedQuery);
			List aux = query.getResultList();
			return new LinkedHashSet<ProBaseVO>(aux);
		} catch (PersistenceException e) {
			throw new Exception(e.getCause().getCause().getMessage());
		} catch (IllegalArgumentException e) {// Não achou namedquery
			try {
				EntityManager em = getEntityManager();
				Query query = em.createQuery("select  count(obj.id) from " + nomeDaEntidade + " obj where obj.statusDoRegistro <> 3");
				Long cont = (Long) query.getSingleResult();
				if (cont < 5000) {
					query = em.createQuery("from " + nomeDaEntidade + " obj where obj.statusDoRegistro <> 3");
					return new LinkedHashSet<ProBaseVO>(query.getResultList());
				}
			} catch (Exception e2) {
				log.warn("!!!!!!!!!!!!!!Não foi possivel listar a entidade:" + vo.getClass().getName() + ". Erro:" + e2.getMessage());
			}
			throw new Exception(e.getMessage());
		}
	}

	private String recuperarNamedQuery(ProBaseVO appBaseVO, String quaryNamed) {
		String queryString = "";
		NamedQuery namedQuery = appBaseVO.getClass().getAnnotation(NamedQuery.class);
		if (namedQuery != null) {
			if (namedQuery.name().equals(quaryNamed)) {
				queryString = namedQuery.query();
			}
		}
		if ("".endsWith(queryString)) {
			NamedQueries namedQueries = appBaseVO.getClass().getAnnotation(NamedQueries.class);
			if (namedQueries != null) {
				for (NamedQuery q : namedQueries.value()) {
					if (q.name().equals(quaryNamed)) {
						queryString = q.query();
						break;
					}
				}
			}
		}
		if (queryString.isEmpty()) {
			throw new IllegalArgumentException();
		}
		return queryString;
	}

	private String aplicarFiltroExclusaoLogica(String query) {
		StringBuffer buffer = new StringBuffer(query);

		Pattern pattern = Pattern.compile("(from) *(\\w*) *(\\w*)");
		Matcher matcher = pattern.matcher(query);

		String alias = null;
		if (matcher.find()) {
			alias = matcher.group(3);
		}
		String filtroExclusaoLogica = MessageFormat.format(" {0}.statusDoRegistro <> 3 ", alias);

		int contemWhere = query.toLowerCase().indexOf("where");
		int contemOrderBy = query.toLowerCase().indexOf("order");

		if (contemWhere == -1 && contemOrderBy == -1) { // não contem where nem order by
			buffer.append(" where" + filtroExclusaoLogica);
		} else if (contemWhere != -1 && contemOrderBy == -1) { // contem where mas nao tem order by
			buffer.append(" AND" + filtroExclusaoLogica);
		} else if (contemWhere != -1 && contemOrderBy != -1) { // contem where e contem order by
			buffer.insert(contemOrderBy - 1, " AND" + filtroExclusaoLogica);
		} else if (contemWhere == -1 && contemOrderBy != -1) { // nao contem where e contem order by
			buffer.insert(contemOrderBy - 1, " where" + filtroExclusaoLogica);
		}
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.sansys.persistencia.IAppBaseDAO#listar(br.com.sansys.entidades .AppBaseVO, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<ProBaseVO> listar(ProBaseVO appBaseVO, String quaryNamed) {
		EntityManager em = getEntityManager();
		try {
			String queryString = recuperarNamedQuery(appBaseVO, quaryNamed);
			queryString = aplicarFiltroExclusaoLogica(queryString);
			Query query = em.createQuery(queryString);

			List<String> properties = extraiParametro(queryString + "");
			for (String propriedade : properties) {
				try {
					Object valor = Reflexao.recuperaValorDeMetodoGet(propriedade, appBaseVO);
					if (valor != null) {
						if (valor instanceof Date) {
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
							valor = df.format(valor);
						}
						query.setParameter(propriedade, valor);
					} else {
						Object object = Reflexao.recuperaTipoDeRetornoDoMetodoGet(propriedade, appBaseVO);
						if (object.getClass().isInstance(Date.class)) {
							valor = new Date();
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
							valor = df.format(valor);
							query.setParameter(propriedade, valor);
						}
					}
				} catch (Exception e) {
					throw new RuntimeExceptionDao("Erro ao inserir parametros na Query", e);
				}
			}
			List aux = query.getResultList();
			return new LinkedHashSet<ProBaseVO>(aux);
		} catch (PersistenceException e) {
			trataErrosDePersistencia(em, e);
			throw e;
		} catch (IllegalArgumentException e) {
			throw new RuntimeExceptionDao(e);
		}
	}

	private void trataErrosDePersistencia(EntityManager em, PersistenceException e) {
		em.clear();
		if (e.getCause() instanceof ConstraintViolationException) {
			ConstraintViolationException e1 = (ConstraintViolationException) e.getCause();
			if (e1.getCause() instanceof BatchUpdateException) {
				SQLException e2 = ((BatchUpdateException) e1.getCause()).getNextException();
				throw new RuntimeExceptionDao(e2);
			}
			if (e1.getCause() instanceof SQLException) {
				SQLException e2 = (SQLException) e1.getCause();
				throw new RuntimeExceptionDao(e2);
			}
			throw new RuntimeExceptionDao(e1);
		} else if (e.getCause() instanceof SQLGrammarException) {
			SQLGrammarException e1 = (SQLGrammarException) e.getCause();
			if (e1.getCause() instanceof SQLException) {
				SQLException e2 = (SQLException) e1.getCause();
				throw new RuntimeExceptionDao(e2);
			}
		} else if (e.getCause() instanceof EntityNotFoundException) {
			EntityNotFoundException e1 = (EntityNotFoundException) e.getCause();
			throw new RuntimeExceptionDao(e1);

		} else {
			throw new RuntimeExceptionDao(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.sansys.persistencia.IAppBaseDAO#recuperaObjetoPorChaveNatural( br.com.sansys.entidades.AppBaseVO, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ProBaseVO recuperaObjetoPorChaveNatural(ProBaseVO vo, String nomeDaChave) throws Exception {
		EntityManager em = getEntityManager();
		try {
			Query query = em.createQuery("from " + vo.getClass().getName() + " where " + nomeDaChave + "= " + ":" + nomeDaChave);
			Object arg1 = Reflexao.recuperaValorDaPropriedade(nomeDaChave, vo);
			query.setParameter(nomeDaChave, arg1);
			List list = query.getResultList();
			if (list.isEmpty())
				return null;
			final ProBaseVO proBaseVO = (ProBaseVO) list.get(0);
			getEntityManager().clear();
			return proBaseVO;
		} catch (PersistenceException e) {
			trataErrosDePersistencia(em, e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.sansys.persistencia.IAppBaseDAO#recuperaObjeto(br.com.sansys.entidades .AppBaseVO)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ProBaseVO recuperaObjeto(ProBaseVO vo) throws Exception {
		EntityManager em = null;
		ProBaseVO voRecuperado = null;
		try {
			em = getEntityManager();
			antesRecuperaObjeto(vo);
			Class<? extends ProBaseVO> classASerCarregada = vo.getClass();
			while (classASerCarregada.getSimpleName().contains("$$")) {// Resolve questões relacionadas as classes de proxy hibernate
				classASerCarregada = (Class<? extends ProBaseVO>) classASerCarregada.getSuperclass();
			}
			voRecuperado = em.find(classASerCarregada, vo.getId());
			if (voRecuperado != null) {
				inicializaLazy(voRecuperado, getProfGrafoDePesquisa());
			}
			aposRecuperaObjeto(voRecuperado);
		} catch (PersistenceException e) {
			trataErrosDePersistencia(em, e);
			throw e;
		}
		return voRecuperado;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ProBaseVO recuperaObjetoLazy(ProBaseVO vo) throws Exception {
		EntityManager em = null;
		ProBaseVO voRecuperado = null;
		try {
			em = getEntityManager();
			antesRecuperaObjeto(vo);
			Class<? extends ProBaseVO> classASerCarregada = vo.getClass();
			while (classASerCarregada.getSimpleName().contains("$$")) {// Resolve questões relacionadas as classes de proxy hibernate
				classASerCarregada = (Class<? extends ProBaseVO>) classASerCarregada.getSuperclass();
			}
			voRecuperado = em.find(classASerCarregada, vo.getId());
			aposRecuperaObjeto(voRecuperado);
		} catch (PersistenceException e) {
			trataErrosDePersistencia(em, e);
			throw e;
		}
		return voRecuperado;
	}

	protected int getProfGrafoDePesquisa() {
		return Constantes.PROFUNDIDADE_GRAFO_ENTIDADE;
	}

	protected void inicializaLazy(ProBaseVO objetoPai, int nivel) throws Exception {
		try {
			if (nivel <= 0) {
				return;
			}
			--nivel;
			Method[] methods = objetoPai.getClass().getMethods();
			for (Method method : methods) {

				if (!method.getName().startsWith("get")) {
					continue;
				}
				final Transient transiente = Reflexao.findAnnotation(method, Transient.class);
				if (transiente != null) {
					continue;
				}
				final OneToMany oneToMany = Reflexao.findAnnotation(method, OneToMany.class);
				final ManyToOne manyToOne = Reflexao.findAnnotation(method, ManyToOne.class);
				final OneToOne oneToOne = Reflexao.findAnnotation(method, OneToOne.class);
				final ManyToMany manyToMany = Reflexao.findAnnotation(method, ManyToMany.class);
				if (oneToMany != null || manyToOne != null || oneToOne != null || manyToMany != null) {
					Object atributoAserInicilizado = method.invoke(objetoPai);
					Hibernate.initialize(atributoAserInicilizado);
					if (oneToMany != null) {
						CascadeType[] cascadeType = oneToMany.cascade();
						if (isCascade(cascadeType)) {
							inicalizaColecaoLazy(nivel, (Collection<?>) atributoAserInicilizado);
						}
					} else if (manyToMany != null) {
						CascadeType[] cascadeType = manyToMany.cascade();
						if (isCascade(cascadeType)) {
							inicalizaColecaoLazy(nivel, (Collection<?>) atributoAserInicilizado);
						}
					} else if (oneToOne != null) {
						CascadeType[] cascadeType = oneToOne.cascade();
						if (isCascade(cascadeType)) {
							inicializaLazy(objetoPai, nivel);
						}
					} else if (manyToOne != null) {
						CascadeType[] cascadeType = manyToOne.cascade();
						if (isCascade(cascadeType)) {
							inicializaLazy(objetoPai, nivel);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Erro ao recuperar objetos lazy ", e);
		}
	}

	private boolean isCascade(CascadeType[] cascadeTypes) {
		if (cascadeTypes == null) {
			return false;
		}

		for (CascadeType cascadeType : cascadeTypes) {
			if (cascadeType == CascadeType.ALL) {
				return true;
			}
			if (cascadeType == CascadeType.MERGE) {
				return true;
			}
			if (cascadeType == CascadeType.PERSIST) {
				return true;
			}
		}
		return false;
	}

	private void inicalizaColecaoLazy(int nivel, Collection c) throws Exception {
		if (c != null) {
			for (Object object : c) {
				if (object != null) {
					inicializaLazy((ProBaseVO) object, nivel);
				}
			}
		}
	}

	protected void removeTransientes(ProBaseVO objetoAtual, int nivel, ProBaseVO objetoDonoDaAgregacao) throws Exception {
		if (nivel == 0) {
			return;
		}
		nivel--;
		Method[] methods = objetoAtual.getClass().getMethods();
		for (Method method : methods) {
			OneToMany oneToMany = Reflexao.findAnnotation(method, OneToMany.class);
			if (oneToMany != null /* Collection.class.isAssignableFrom(method.getReturnType()) */) {
				if (oneToMany.cascade() != null && (Arrays.equals(oneToMany.cascade(), new CascadeType[] { CascadeType.ALL }))) {
					Collection c = (Collection) method.invoke(objetoAtual);
					if (c instanceof PersistentSet) {
						if (!((PersistentSet) c).wasInitialized()) {
							continue;
						}
					}
					if (c instanceof PersistentList) {
						if (!((PersistentList) c).wasInitialized()) {
							continue;
						}
					}
					if (c != null) {
						for (Object object : c) {
							if (object != null) {
								removeTransientes((ProBaseVO) object, nivel, objetoAtual);
							}
						}
					}
				}
			} else {
				final ManyToOne manyToOne = Reflexao.findAnnotation(method, ManyToOne.class);
				if (manyToOne != null/* ProBaseVO.class.isAssignableFrom(method.getReturnType()) */) {
					try {
						ProBaseVO baseVO = (ProBaseVO) method.invoke(objetoAtual, new Class[] {});
						if (baseVO != null && baseVO.getId() == null) {
							if (manyToOne.cascade() == null || Arrays.equals(manyToOne.cascade(), new CascadeType[] {})) {
								if (objetoDonoDaAgregacao == null || baseVO.hashCode() != objetoDonoDaAgregacao.hashCode()) {// Não pode eliminar o dono da agregação
									Reflexao.atualizarCampoDoObjeto(method, objetoAtual, null);
									baseVO = null;
								}
							}
						}
						if (baseVO != null) {
							removeTransientes(baseVO, nivel, objetoAtual);
						}
					} catch (InvocationTargetException e) {
						Throwable e2 = e.getTargetException();
						if (e2 instanceof LazyInitializationException) {
							log.warn("Erro controlado no metodo removeTransientes: Objeto não inicializado...");
						}
					}
				} else {
					final OneToOne oneToOne = Reflexao.findAnnotation(method, OneToOne.class);
					if (oneToOne != null/* ProBaseVO.class.isAssignableFrom(method.getReturnType()) */) {
						try {

							ProBaseVO baseVO = (ProBaseVO) method.invoke(objetoAtual, new Class[] {});
							if (baseVO != null && baseVO.getId() == null) {
								if (oneToOne.cascade() == null || Arrays.equals(oneToOne.cascade(), new CascadeType[] {})) {
									if (objetoDonoDaAgregacao == null || baseVO.hashCode() != objetoDonoDaAgregacao.hashCode()) {// Não pode eliminar o dono da agregação
										try {
											Reflexao.atualizarCampoDoObjeto(method, objetoAtual, null);
										} catch (Exception e) {
											log.error("Erro ao remover transientes", e);
										}
									}
								}
							}
						} catch (InvocationTargetException e) {
							Throwable e2 = e.getTargetException();
							if (e2 instanceof LazyInitializationException) {
								log.debug("Erro controlado no metodo removeTransientes: Objeto não inicializado...");
							}
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.sansys.persistencia.IAppBaseDAO#listarBaseadoNoExemplo(br.com. sansys.entidades.AppBaseVO, br.com.sansys.entidades.AppBaseVO, java.lang.String)
	 */
	@Override
	public Set<ProBaseVO> listarBaseadoNoExemplo(ProBaseVO exemplo, ProBaseVO exemplo2, int primeiroRegistro, int quantidadeDeRegistros, String... projecoes) {
		try {
			if (exemplo.getClass().isAnnotationPresent(EntityBroker.class)) {
				return listarBaseadoNoExemploMainFrame(exemplo);
			} else {
				return listarBaseadoNoExemploPadrao(exemplo, exemplo2, primeiroRegistro, quantidadeDeRegistros, projecoes);
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro ao listar baseado no exemplo", e);
		}
	}

	protected List<String> configuraListaOrdenacao(String... projecoes) throws Exception {
		List<String> ordenacaoFinal = new ArrayList<String>();
		if (projecoes != null) {
			List<String> projecoes_com_AscDesc = new ArrayList<String>();
			for (int i = 0; i < projecoes.length; i++) {
				String projecao_com_AscOuDesc = projecoes[i];
				if (projecao_com_AscOuDesc.toUpperCase().endsWith(" ASC") || projecao_com_AscOuDesc.toUpperCase().endsWith(" DESC")) {
					projecoes_com_AscDesc.add(projecao_com_AscOuDesc);
				}
			}
			// Coloca o campo pedido para ordenar como primeiro da lista
			// e depois coloca os outros campos. Caso nao ache o ASC ou DESC
			// segue a ordenacao das projeções
			for (String projecao_com_AscOuDesc : projecoes_com_AscDesc) {
				ordenacaoFinal.add(projecao_com_AscOuDesc);
			}
			for (String projecao : projecoes) {
				if (!ordenacaoFinal.contains(projecao)) {
					ordenacaoFinal.add(projecao);
				}
			}
		}
		return ordenacaoFinal;
	}

	protected String[] removeComandosSQLdasProjecoes(String... projecoes) throws Exception {
		String[] projecoesSemTransientes = new String[projecoes.length];
		if (projecoes != null) {
			for (int i = 0; i < projecoes.length; i++) {
				String projecaoPura = projecoes[i].replace(" ASC", "").replace(" asc", "").replace(" DESC", "").replace(" desc", "");
				projecoesSemTransientes[i] = projecaoPura;
			}
		}
		return projecoesSemTransientes;
	}

	protected List<String> retiraTransientesDaOrdenacao(List<String> ordenacao, String... projecoesAtualizada) {
		List<String> novaLista = new ArrayList<String>();
		for (String ordem : ordenacao) {
			for (String projecao : projecoesAtualizada) {
				if (ordem.equals(projecao) || ordem.toUpperCase().equals(projecao.toUpperCase() + " ASC") || ordem.toUpperCase().equals(projecao.toUpperCase() + " DESC")) {
					novaLista.add(ordem);
				}
			}
		}

		return novaLista;
	}

	private Set<ProBaseVO> listarBaseadoNoExemploPadrao(ProBaseVO exemplo, ProBaseVO exemplo2, int primeiroRegistro, int quantidadeDeRegistros, String... projecoes) throws Exception {
		List<String> ordenacao = configuraListaOrdenacao(projecoes);
		projecoes = removeComandosSQLdasProjecoes(projecoes);
		projecoes = retiraTransientesDaProjecao(exemplo, projecoes);
		ordenacao = retiraTransientesDaOrdenacao(ordenacao, projecoes);

		String[] projecoesDeColecoes = recuperaProjecaoDeColecoes(exemplo, projecoes);
		ordenacao = retiraProjecoesDeColecaoDaOrdenacaoPrincipal(projecoesDeColecoes, ordenacao);

		String[] projecoesDeByteArray = recuperaProjecaoDeBytes(exemplo, projecoes);
		ordenacao = retiraProjecoesDeColecaoDaOrdenacaoPrincipal(projecoesDeByteArray, ordenacao);

		projecoes = retiraProjecoesDeColecaoDaProjecaoPrincipal(projecoesDeColecoes, projecoes);
		Map<String, Set<String>> agregados = recuperaObjetosComSuasProjecoes(projecoes);
		projecoes = acrecentaIdNasProjecoes(projecoes, agregados);
		projecoes = acrecentaIdNasProjecoes(projecoes);
		projecoes = alteraProjecao(exemplo, projecoes);
		EntityManager em = getEntityManager();
		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = null;
		criteria = hibernateSession.createCriteria(exemplo.getClass());
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		DadosDeSuporteAPesquisa dadosDeSuporteAPesquisa = new DadosDeSuporteAPesquisa();
		montaRestricoes(exemplo, exemplo2, criteria, dadosDeSuporteAPesquisa);
		aplicarFiltroExclusaoLogica(exemplo, criteria, projecoes);
		criteria.setProjection(Projections.distinct(Projections.countDistinct("id")));
		Long total = recuperaQuantidadeDeRegistros(exemplo, criteria);
		if (total == null) {
			total = 0L;
		}
		if (!(total.equals(new Long(0L)))) {
			criteria.setFirstResult(primeiroRegistro);
			criteria.setMaxResults(quantidadeDeRegistros);
		}
		aplicarFiltroDeSeguranca(exemplo, criteria);
		ProjectionList projectionList = Projections.projectionList();
		configuraProjecao(criteria, projectionList, exemplo, projecoes);
		// Incluido acima da versão 5.1.21
		configuraOrdenacao(criteria, ordenacao.toArray(new String[ordenacao.size()]));

		if (dadosDeSuporteAPesquisa.temJoin) {
			criteria.setProjection(Projections.distinct(projectionList));
		} else {
			criteria.setProjection(projectionList);
		}
		List aux = null;
		antesRecuperaLista(aux);
		antesRecuperaLista(aux, criteria);
		aux = recuperaLista(criteria, exemplo);
		if (!possuiFuncaoDeAgregacao(projectionList, projecoes)) {
			aux = montarListaDeObjetosApartirDaProjecao(aux, exemplo, projecoes);
		} else {
			aux = montarListaDeObjetosApartirDaProjecao(aux, exemplo, projectionList.getAliases());
		}
		Map<String, String[]> mapaDeProjecoesDeUmaMesmaColecao = recuperaProjecoesDeUmaMesmaColecao(projecoesDeColecoes);
		atribuiColecoesAoObjetoMestre(aux, mapaDeProjecoesDeUmaMesmaColecao);
		aposRecuperaLista(aux);
		RecordDataSet<ProBaseVO> linkedHashSet = new RecordDataSet<ProBaseVO>(aux);
		linkedHashSet.setTotal(total);
		getEntityManager().clear();
		return linkedHashSet;
	}

	protected void aplicarFiltroExclusaoLogica(ProBaseVO exemplo, Criteria criteria, String[] projecoes) {
		List listaProjecoes = Arrays.asList(projecoes);
		if (!listaProjecoes.contains("statusDoRegistro")) {
			Criterion criterion = Restrictions.ne("statusDoRegistro", StatusDoRegistro.EXCLUIDO);
			criteria.add(criterion);
		}
	}

	protected List<String> retiraProjecoesDeColecaoDaOrdenacaoPrincipal(String[] projecoesDeColecoes, List<String> ordenacao) {
		if (!ordenacao.isEmpty()) {
			String[] array = new String[ordenacao.size()];
			for (int i = 0; i < array.length; i++)
				array[i] = ordenacao.get(i);
			String[] projecoesRestantes = retiraProjecoesDeColecaoDaProjecaoPrincipal(projecoesDeColecoes, array);
			if (projecoesRestantes != null && projecoesRestantes.length > 0) {
				ordenacao = Arrays.asList(projecoesRestantes);
			} else {
				ordenacao = new ArrayList<String>();
			}
		}
		return ordenacao;
	}

	protected void configuraOrdenacao(Criteria criteria, String... ordenacao) {
		for (String ordem : ordenacao) {
			if (ordem.contains(".")) {
				String[] agregados = ordem.split("\\.");
				String objetoAgregadoRaiz = agregados[0];
				Criteria subCriteria = criaCriteriaParaObjetoAgregadoRaiz(criteria, objetoAgregadoRaiz);
				String subProjecoes = ordem.replaceFirst(objetoAgregadoRaiz + ".", "");
				configuraOrdenacao(subCriteria, subProjecoes);
			} else {
				if (ordem.toUpperCase().endsWith("DESC")) {
					criteria.addOrder(Order.desc(ordem.substring(0, ordem.length() - 4).trim()));
				} else {
					ordem = ordem.toUpperCase().endsWith("ASC") ? ordem.substring(0, ordem.length() - 3).trim() : ordem;
					criteria.addOrder(Order.asc(ordem));
				}
			}
		}
	}

	protected String[] alteraProjecao(ProBaseVO exemplo, String[] projecoes) {
		return projecoes;
	}

	protected Long recuperaQuantidadeDeRegistros(ProBaseVO exemplo, Criteria criteria) {
		return (Long) criteria.uniqueResult();

	}

	protected void atribuiColecoesAoObjetoMestre(List aux, Map<String, String[]> mapaDeProjecoesDeUmaMesmaColecao) throws Exception, InstantiationException, IllegalAccessException {
		for (Object mestre : aux) {
			for (String key : mapaDeProjecoesDeUmaMesmaColecao.keySet()) {
				Method getter = Reflexao.recuperaMetodoGetDoObjeto(key, mestre);
				Class classe = Reflexao.recuperaTipoDeParametroGenericosEmRetornoDeMetodos(getter);

				OneToMany oneToMany = getter.getAnnotation(OneToMany.class);
				String nomeDoMestreNoDetalhe = oneToMany.mappedBy();

				Object detalhe = classe.newInstance();
				ProBaseVO baseVOArg = (ProBaseVO) mestre.getClass().newInstance();
				baseVOArg.setId(((ProBaseVO) mestre).getId());
				Reflexao.atualizarCampoDoObjeto(nomeDoMestreNoDetalhe, detalhe, mestre);
				String[] projecoesDeUmaMesmaColecao = mapaDeProjecoesDeUmaMesmaColecao.get(key);
				Set colecao = listarBaseadoNoExemploPadrao((ProBaseVO) detalhe, null, 0, 1000, projecoesDeUmaMesmaColecao);
				Reflexao.atualizarCampoDoObjeto(key, mestre, colecao);
			}
		}
	}

	protected Map<String, String[]> recuperaProjecoesDeUmaMesmaColecao(String[] projecoesDeColecoes) {
		Map<String, List> projecoesDeUmaMesmaColecao = new HashMap();
		Map<String, String[]> projecoesDeUmaMesmaColecaoRetorno = new HashMap();
		for (String proj : projecoesDeColecoes) {
			List listaProjecoesDeUmaMesmaColecao = null;
			String[] projecoesDoDetalhe = proj.split("\\.");
			String nomeDaColecao = projecoesDoDetalhe[0];
			if (projecoesDeUmaMesmaColecao.containsKey(nomeDaColecao)) {
				listaProjecoesDeUmaMesmaColecao = projecoesDeUmaMesmaColecao.get(nomeDaColecao);
			} else {
				listaProjecoesDeUmaMesmaColecao = new ArrayList<String>();
				projecoesDeUmaMesmaColecao.put(nomeDaColecao, listaProjecoesDeUmaMesmaColecao);
			}
			listaProjecoesDeUmaMesmaColecao.add(proj.replaceFirst(nomeDaColecao + ".", ""));
		}
		for (String key : projecoesDeUmaMesmaColecao.keySet()) {
			List<String> list = projecoesDeUmaMesmaColecao.get(key);
			String[] projecoesFinais = new String[list.size()];
			int i = 0;
			for (String projecoes : list) {
				projecoesFinais[i++] = projecoes;
			}
			projecoesDeUmaMesmaColecaoRetorno.put(key, projecoesFinais);
		}
		return projecoesDeUmaMesmaColecaoRetorno;
	}

	protected String[] retiraTransientesDaProjecao(ProBaseVO exemplo, String... projecoes) {
		String[] projecoesSemTransientes = projecoes;
		try {
			List<String> projListAux = new ArrayList<String>();
			int cont = 0;
			projecoes: for (String projAux : projecoes) {
				if ("nrVersao".equals(projAux)) {
					projListAux.add(projAux);
				} else if (projAux.contains(".")) {
					String[] subprojecoes = projAux.split("\\.");
					String aux = "";
					for (String subprojecao : subprojecoes) {
						aux = aux + "." + subprojecao;
						Method method = Reflexao.recuperaMetodoGetDoObjeto(aux.substring(1), exemplo);
						boolean isTransiente = method.isAnnotationPresent(Transient.class);
						if (isTransiente) {
							continue projecoes;
						}
					}
					projListAux.add(projAux);
				} else {
					Method method = Reflexao.recuperaMetodoGetDoObjeto(projAux, exemplo);
					boolean naoTransiente = !method.isAnnotationPresent(Transient.class);
					if (naoTransiente) {
						projListAux.add(projAux);
					}
				}
			}
			int i = 0;
			if (projListAux.size() > 0) {
				projecoesSemTransientes = new String[projListAux.size()];
				for (String projecao : projListAux) {
					projecoesSemTransientes[i++] = projecao;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeExceptionDao("Erro ao retirar projeção de coleção", e);
		}
		return projecoesSemTransientes;
	}

	protected String[] retiraProjecoesDeColecaoDaProjecaoPrincipal(String[] proJColecoes, String[] projecoes) {
		String[] projecoesSemColecoes = null;
		try {
			List<String> projListAux = new ArrayList<String>();
			volta: for (String projecao2 : projecoes) {
				for (String projecao : proJColecoes) {
					if (projecao.equals(projecao2)) {
						continue volta;
					}
				}
				projListAux.add(projecao2);
			}
			int i = 0;
			if (projListAux.size() > 0) {
				projecoesSemColecoes = new String[projListAux.size()];
				for (String projecao : projListAux) {
					projecoesSemColecoes[i++] = projecao;
				}
			} else {
				projecoesSemColecoes = new String[0];
			}
		} catch (Exception e) {
			throw new RuntimeExceptionDao("Erro ao retirar projeção de coleção", e);
		}
		return projecoesSemColecoes;
	}

	protected String[] recuperaProjecaoDeBytes(ProBaseVO exemplo, String... projecoes) {
		String[] projecoesByteArray = new String[0];
		try {
			List<String> projListAux = new ArrayList<String>();
			String nomeDoAtributo = null;
			projecoes: for (String projAux : projecoes) {
				if ("nrVersao".equals(projAux)) {
					continue;
				}
				if (projAux.contains(".")) {
					String[] subprojecoes = projAux.split("\\.");
					nomeDoAtributo = subprojecoes[0];
				} else {
					nomeDoAtributo = projAux;
				}
				Class classe = Reflexao.recuperaTipoDeRetornoDoMetodoGet(nomeDoAtributo, exemplo);
				if (byte[].class.isAssignableFrom(classe)) {
					projListAux.add(projAux);
				}
				Method method = Reflexao.recuperaMetodoGetDoObjeto(nomeDoAtributo, exemplo);
				if (method.isAnnotationPresent(Lob.class)) {
					projListAux.add(projAux);
				}
			}
			int i = 0;
			if (projListAux.size() > 0) {
				projecoesByteArray = new String[projListAux.size()];
				for (String projecao : projListAux) {
					projecoesByteArray[i++] = projecao;
				}
			}

		} catch (Exception e) {
			throw new RuntimeExceptionDao("Erro ao recuperar projeção de coleção", e);
		}
		return projecoesByteArray;
	}

	protected String[] recuperaProjecaoDeColecoes(ProBaseVO exemplo, String... projecoes) {
		String[] projecoesColecoes = new String[0];
		try {
			List<String> projListAux = new ArrayList<String>();
			String nomeDoAtributo = null;
			projecoes: for (String projAux : projecoes) {
				if ("nrVersao".equals(projAux)) {
					continue;
				}
				if (projAux.contains(".")) {
					String[] subprojecoes = projAux.split("\\.");
					nomeDoAtributo = subprojecoes[0];
				} else {
					nomeDoAtributo = projAux;
				}
				Class classe = Reflexao.recuperaTipoDeRetornoDoMetodoGet(nomeDoAtributo, exemplo);
				if (Collection.class.isAssignableFrom(classe)) {
					projListAux.add(projAux);
				}
			}
			int i = 0;
			if (projListAux.size() > 0) {
				projecoesColecoes = new String[projListAux.size()];
				for (String projecao : projListAux) {
					projecoesColecoes[i++] = projecao;
				}
			}

		} catch (Exception e) {
			throw new RuntimeExceptionDao("Erro ao recuperar projeção de coleção", e);
		}
		return projecoesColecoes;
	}

	private Set<ProBaseVO> listarBaseadoNoExemploMainFrame(ProBaseVO exemplo) throws Exception {
		LinkedHashSet<ProBaseVO> linkedHashSet = new LinkedHashSet<ProBaseVO>();
		IMainFrameDAOHelper instance = null;
		try {
			instance = (IMainFrameDAOHelper) Class.forName("br.gov.prodigio.persistencia.mainframe.MainFrameDAOHelper").newInstance();
		} catch (Exception e) {
			throw new BrokerException("Não encontrou a classe para usar o broker prodemge. Verifique se existe a dependência prodemge-mainframe no pom.xml do projeto.");
		}
		linkedHashSet.add((ProBaseVO) instance.sendReceive(exemplo));
		return linkedHashSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.sansys.persistencia.IAppBaseDAO#listarBaseadoNoExemplo(br.com. sansys.entidades.AppBaseVO, br.com.sansys.entidades.AppBaseVO, br.com.sansys.entidades.AppBaseVO, java.lang.String)
	 */
	@Override
	public Set<ProBaseVO> listarBaseadoNoExemplo(ProBaseVO exemplo, ProBaseVO exemplo2, ProBaseVO pojo, String... projecoes) throws Exception {
		projecoes = acrecentaIdNasProjecoes(projecoes);
		EntityManager em = getEntityManager();
		Session hibernateSession = (Session) em.getDelegate();
		Criteria criteria = null;
		criteria = hibernateSession.createCriteria(exemplo.getClass()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		DadosDeSuporteAPesquisa dadosDeSuporteAPesquisa = new DadosDeSuporteAPesquisa();
		montaRestricoes(exemplo, exemplo2, criteria, dadosDeSuporteAPesquisa);
		criteria.setProjection(Projections.count("id"));
		Long total = (Long) criteria.uniqueResult();
		aplicarFiltroDeSeguranca(exemplo, criteria);
		ProjectionList projectionList = Projections.projectionList();
		configuraProjecao(criteria, projectionList, exemplo, projecoes);
		criteria.setProjection(projectionList);
		List aux = null;
		antesRecuperaLista(aux);
		antesRecuperaLista(aux, criteria);
		criteria.setFirstResult(0);
		criteria.setMaxResults(10);
		aux = recuperaLista(criteria, exemplo);
		if (!possuiFuncaoDeAgregacao(projectionList, projecoes)) {
			aux = montarListaDeObjetosApartirDaProjecao(aux, pojo, projecoes);
		} else {
			aux = montarListaDeObjetosApartirDaProjecao(aux, pojo, projectionList.getAliases());
		}
		aposRecuperaLista(aux);
		RecordDataSet<ProBaseVO> recordDataSet = new RecordDataSet<ProBaseVO>(aux);
		return recordDataSet;
	}

	protected boolean possuiFuncaoDeAgregacao(ProjectionList projectionList, String[] projecoes) {
		if (projectionList.isGrouped()) {
			return true;
		}
		for (String projecao : projecoes) {
			if (projecao.contains("conta(")) {
				return true;
			}
			if (projecao.contains("agrupa(")) {
				return true;
			}
			if (projecao.contains("soma(")) {
				return true;
			}
		}
		return false;
	}

	protected String[] acrecentaIdNasProjecoes(String... projecoes) {
		if (projecoes == null || projecoes.length == 0) {
			projecoes = new String[] { "id" };
			return projecoes;
		} else {
			String[] novaLista = new String[projecoes.length + 1];
			for (int i = 0; i < projecoes.length; i++) {
				novaLista[i] = projecoes[i];
			}
			// Sempre adiciona o id
			novaLista[projecoes.length] = "id";
			return novaLista;
		}
	}

	protected List recuperaLista(Criteria criteria, ProBaseVO exemplo) {
		List aux;
		List<Object[]> aux2 = new ArrayList<Object[]>();
		aux = criteria.list();
		if (!aux.isEmpty()) {
			if (!(aux.get(0) instanceof Object[])) {
				for (Object object : aux) {
					Object[] tupla = new Object[] { object };
					aux2.add(tupla);
				}
				return aux2;
			}
		}
		return aux;
	}

	protected void montaRestricoes(Object exemplo, Object exemplo2, Criteria criteria, DadosDeSuporteAPesquisa dadosDeSuporteAPesquisa) throws Exception {
		List<String> properties = Reflexao.recuperaNomeDeAtributosBeansPorClasse(exemplo);
		Class tipo = null;
		if (exemplo instanceof ProBaseVO && ((ProBaseVO) exemplo).getId() != null && ((ProBaseVO) exemplo).getId() >= 1) {
			Example e = Example.create(exemplo).excludeZeroes();
			Criterion exp = Restrictions.eq("id", ((ProBaseVO) exemplo).getId());
			criteria.add(exp);
		} else {
			for (String propriedade : properties) {
				try {
					Object valor = Reflexao.recuperaValorDeMetodoGet(propriedade, exemplo);
					if (valor != null && !propriedade.equals("usuarioCriacao") && !propriedade.equals("unidadeCriacao")) {
						Class classe = Reflexao.recuperaTipoDeRetornoDoMetodoGet(propriedade, exemplo);
						if (classe.equals(String.class)) {
							if (!"".equals(valor)) {
								String value = ((String) valor);

								if (value.startsWith("%") && !value.substring(1).contains("%")) {
									Criterion ilike = Restrictions.ilike(propriedade, value, MatchMode.END);
									criteria.add(ilike);
								} else if (value.endsWith("%") && !value.substring(0, value.length() - 2).contains("%")) {
									Criterion ilike = Restrictions.ilike(propriedade, value, MatchMode.START);
									criteria.add(ilike);
								} else if (value.startsWith("%") && value.endsWith("%")) {
									Criterion ilike = Restrictions.ilike(propriedade, value, MatchMode.ANYWHERE);
									criteria.add(ilike);
								} else if (value.startsWith("\"") && value.endsWith("\"")) {
									Criterion ilike = Restrictions.ilike(propriedade, value.substring(1, value.length() - 1), MatchMode.EXACT);
									criteria.add(ilike);
								} else {
									Criterion exp = Restrictions.eq(propriedade, valor);
									criteria.add(exp);
								}
							}
						} else if (valor instanceof Date) {
							Object valor2 = null;
							if (exemplo2 != null)
								valor2 = Reflexao.recuperaValorDeMetodoGet(propriedade, exemplo2);
							Date data = (Date) valor;
							Date data2 = (Date) valor2;

							// Alterado pra usar >= e <= para datas e resolver problema do Timestamp
							if (data.equals(data2) || data2 == null) {
								Calendar c = Calendar.getInstance();
								c.setTime(data);
								c.add(Calendar.DATE, 1);
								data2 = c.getTime();
							}

							criteria.add(Restrictions.ge(propriedade, data));
							criteria.add(Restrictions.le(propriedade, data2));

						} else if (valor instanceof Number) {
							Object valor2 = null;
							if (exemplo2 != null) {
								valor2 = Reflexao.recuperaValorDeMetodoGet(propriedade, exemplo2);
							}
							Number primeiroNumero = (Number) valor;
							Number segundoNumero = (Number) valor2;
							Criterion exp = null;
							if (segundoNumero == null || primeiroNumero.equals(segundoNumero)) {
								exp = Restrictions.eq(propriedade, primeiroNumero);
							} else {
								exp = Restrictions.between(propriedade, primeiroNumero, segundoNumero);
							}
							criteria.add(exp);
						} else if (valor instanceof ProBaseVO || valor.getClass().isAnnotationPresent(Embeddable.class)) {
							Object valor2 = null;
							if (exemplo2 != null)
								valor2 = Reflexao.recuperaValorDeMetodoGet(propriedade, exemplo2);
							Criteria criteria2 = criteria.createCriteria(propriedade, propriedade, JoinType.LEFT_OUTER_JOIN);
							montaRestricoes(valor, valor2, criteria2, dadosDeSuporteAPesquisa);
						} else if (valor instanceof java.util.Set) {
							Set detalhe = (Set) valor;
							if (detalhe != null && !detalhe.isEmpty()) {
								Criteria criteria2 = criteria.createCriteria(propriedade, propriedade, JoinType.LEFT_OUTER_JOIN);
								ProBaseVO baseVO = (ProBaseVO) detalhe.toArray()[0];
								dadosDeSuporteAPesquisa.temJoin = true;
								montaRestricoes(baseVO, baseVO, criteria2, dadosDeSuporteAPesquisa);
							}
						} else if (valor instanceof java.util.List) {
							List detalhe = (List) valor;
							if (detalhe != null && !detalhe.isEmpty()) {
								Criteria criteria2 = criteria.createCriteria(propriedade, propriedade, JoinType.LEFT_OUTER_JOIN);
								ProBaseVO baseVO = (ProBaseVO) detalhe.toArray()[0];
								dadosDeSuporteAPesquisa.temJoin = true;
								montaRestricoes(baseVO, baseVO, criteria2, dadosDeSuporteAPesquisa);
							}
						} else if (valor instanceof String) {
							Criterion exp = Restrictions.eq(propriedade.toLowerCase(), ((String) valor).toLowerCase());
							criteria.add(exp);
						} else {
							Criterion exp = Restrictions.eq(propriedade, valor);
							criteria.add(exp);
						}
					}
				} catch (Exception e) {
					log.error("Erro ao montar restrições", e);
					throw e;
				}
			}
		}
	}

	protected void configuraProjecao(Criteria criteria, ProjectionList projectionList, ProBaseVO exemplo, String... projecoes) {
		String alias = criteria.getAlias();
		for (String projecao : projecoes) {
			if (projecao.contains(".")) {
				String[] agregados = projecao.split("\\.");
				String objetoAgregadoRaiz = agregados[0];
				Criteria subCriteria = criaCriteriaParaObjetoAgregadoRaiz(criteria, objetoAgregadoRaiz);
				String subProjecoes = projecao.replaceFirst(objetoAgregadoRaiz + ".", "");
				configuraProjecao(subCriteria, projectionList, exemplo, subProjecoes);
			} else {
				configuraAFormaDeProjecao(criteria, projectionList, alias, projecao);
			}
		}
	}

	private void configuraAFormaDeProjecao(Criteria criteria, ProjectionList projectionList, String alias, String projecao) {
		String propriedadeOrigem = null;
		String propriedadeResultante = null;
		if (projecao.contains("conta(")) {
			propriedadeOrigem = projecao.replace("conta(", "").replace(")", "");
			if (alias == "this") {
				propriedadeResultante = "qte" + propriedadeOrigem.substring(0, 1).toUpperCase() + propriedadeOrigem.substring(1);
			} else {
				propriedadeResultante = "qte" + alias.substring(0, 1).toUpperCase() + alias.substring(1) + propriedadeOrigem.substring(0, 1).toUpperCase() + propriedadeOrigem.substring(1);
			}
			projectionList.add(Property.forName(alias + "." + propriedadeOrigem).count().as(propriedadeResultante));
		} else if (projecao.contains("agrupa(")) {
			propriedadeOrigem = projecao.replace("agrupa(", "").replace(")", "");
			if (alias == "this") {
				propriedadeResultante = "grp" + propriedadeOrigem.substring(0, 1).toUpperCase() + propriedadeOrigem.substring(1);
			} else {
				propriedadeResultante = "grp" + alias.substring(0, 1).toUpperCase() + alias.substring(1) + propriedadeOrigem.substring(0, 1).toUpperCase() + propriedadeOrigem.substring(1);
			}
			projectionList.add(Property.forName(alias + "." + propriedadeOrigem).group().as(propriedadeResultante));
		} else if (projecao.contains("soma(")) {
			propriedadeOrigem = projecao.replace("soma(", "").replace(")", "");
			if (alias == "this") {
				propriedadeResultante = "sma" + propriedadeOrigem.substring(0, 1).toUpperCase() + propriedadeOrigem.substring(1);
			} else {
				propriedadeResultante = "sma" + alias.substring(0, 1).toUpperCase() + alias.substring(1) + propriedadeOrigem.substring(0, 1).toUpperCase() + propriedadeOrigem.substring(1);
			}
			projectionList.add(Projections.sum(alias + "." + propriedadeOrigem).as(propriedadeResultante));
		} else {
			propriedadeOrigem = alias + "." + projecao;
			propriedadeResultante = propriedadeOrigem;
			projectionList.add(Property.forName(propriedadeResultante));
		}
	}

	protected Criteria criaCriteriaParaObjetoAgregadoRaiz(Criteria criteria, String objetoAgregadoRaiz) {
		CriteriaImpl criteriaImpl;

		if ((criteria instanceof CriteriaImpl)) {
			criteriaImpl = (CriteriaImpl) criteria;
		} else {
			Subcriteria subcriteria = (Subcriteria) criteria;
			Criteria parent = subcriteria.getParent();
			while (!(parent instanceof CriteriaImpl)) {
				if ((parent instanceof Subcriteria)) {
					subcriteria = (Subcriteria) parent;
					parent = subcriteria.getParent();
				} else {
					throw new RuntimeException("Tipo Criteria inválido");
				}
			}
			criteriaImpl = (CriteriaImpl) parent;
		}
		Criteria novaCriteria = null;
		for (Iterator<Criteria> it = criteriaImpl.iterateSubcriteria(); it.hasNext();) {
			Criteria critAux = it.next();
			if (critAux.getAlias().equals(objetoAgregadoRaiz)) {// Evita que alias adicionados na montagem de restricoes sejam novamente inseridos aqui
				novaCriteria = critAux;
				break;
			}
		}
		if (novaCriteria == null) {
			novaCriteria = criteria.createCriteria(objetoAgregadoRaiz, objetoAgregadoRaiz, JoinType.LEFT_OUTER_JOIN);
		}
		return novaCriteria;
	}

	protected List montarListaDeObjetosApartirDaProjecao(List<Object[]> registros, ProBaseVO vo, String... projecoes) throws Exception {
		List<ProBaseVO> listaDeObjetos = new ArrayList<ProBaseVO>();
		for (Object[] objeto : registros) {
			try {
				ProBaseVO newVO = vo.getClass().newInstance();
				int indice = 0;
				final EntityManager entityManager = getEntityManager();
				if (recuperacaoCompleta(projecoes)) {// version e id
					newVO = entityManager.find(vo.getClass(), recuperaValorDoID(objeto, projecoes));
				} else {
					for (String propriedade : projecoes) {
						try {
							if (objeto[indice] instanceof Timestamp) {
								Reflexao.atualizarCampoDoObjeto(propriedade, newVO, new Date(((Timestamp) objeto[indice]).getTime()));
							} else if (objeto[indice] instanceof Time) {
								Reflexao.atualizarCampoDoObjeto(propriedade, newVO, new Date(((Time) objeto[indice]).getTime()));
							} else {
								try {
									Reflexao.atualizarCampoDoObjeto(propriedade, newVO, objeto[indice]);
								} catch (Exception e) {
									final String[] split = propriedade.split("\\.");
									String aux = split[split.length - 1];

									String propriedadeId = propriedade.replace("." + aux, ".id");

									int indiceDoId = 0;
									for (String propriedadeAux : projecoes) {
										if (propriedadeAux.equals(propriedadeId)) {
											break;
										}
										indiceDoId++;
									}
									Class c = Reflexao.recuperaTipoDeRetornoDoMetodoGet(split[0], newVO);
									if (c.isInterface()) {
										Method metodoGet = Reflexao.recuperaMetodoGetDoObjeto(split[0], newVO);
										ManyToOne annotation = metodoGet.getAnnotation(ManyToOne.class);
										if (annotation.targetEntity() != null) {
											c = annotation.targetEntity();
										}
									}

									Object o = entityManager.find(c, objeto[indiceDoId]);
									Reflexao.atualizarCampoDoObjeto(split[0], newVO, o);
									Reflexao.atualizarCampoDoObjeto(propriedade, newVO, objeto[indice]);
								}
							}
							indice++;
						} catch (NoSuchMethodException e) {
							throw new Exception("Erro no Metodo montarListaDeObjetosApartirDaProjecao.  Não foi possível encontra método para a propriedade " + propriedade + " do objeto " + newVO.getClass().getSimpleName());
						}
					}
				}
				listaDeObjetos.add(newVO);
			} catch (InstantiationException e) {
				throw new Exception("Erro no Metodo montarListaDeObjetosApartirDaProjecao. Não foi possível instaciar objetos para a classe " + vo.getClass().getSimpleName());
			} catch (IllegalAccessException e) {
				throw new Exception("Erro no Metodo montarListaDeObjetosApartirDaProjecao :" + e.getMessage());
			}
		}
		return listaDeObjetos;
	}

	protected Object recuperaValorDoID(Object[] objeto, String[] projecoes) {
		for (int i = 0; i < projecoes.length; i++) {
			if (projecoes[i].equals("id")) {
				return objeto[i];
			}
		}
		return null;
	}

	protected boolean recuperacaoCompleta(String... projecoes) {
		boolean retorno = false;
		for (String projecao : projecoes) {
			if (projecao.equals("nrVersao")) {
				retorno = true;
			}
		}
		return retorno;

	}

	protected void aplicarFiltroDeSeguranca(ProBaseVO vo, Criteria criteria) {
	}

	private List extraiParametro(String query) {
		List<String> parametros = new ArrayList<String>();
		String aux = "";
		Pattern pattern = Pattern.compile("\\:[a-zA-Z0-9]*");
		Matcher matcher = pattern.matcher(query);
		while (matcher.find()) {
			aux = matcher.group().substring(1);
			parametros.add(aux);
		}
		return parametros;
	}

	public void antesRecuperaObjeto(ProBaseVO vo) {

	}

	public void aposRecuperaObjeto(ProBaseVO vo) {

	}

	public void antesRecuperaLista(List<? extends ProBaseVO> lista) {

	}

	public void aposRecuperaLista(List<? extends ProBaseVO> lista) throws Exception {

	}

	public void newRevision(Object arg0) {
	}

	public void closeEntityManager() {
		ProDAOHelper.closeEntityManager();
	}

	@Override
	public void concluir(Object objeto, Map parametros) throws Exception {
		gravar(objeto);
	}

	public void antesRecuperaLista(List<? extends ProBaseVO> lista, Criteria criteria) {

	}

	@Override
	public void iniciarTransacao() throws Exception {
		EntityManager em = getEntityManager();
		if (em.getTransaction().isActive()) {
			em.getTransaction().rollback();
		}
		em.getTransaction().begin();
	}

	@Override
	public void confirmarTransacao() throws Exception {
		EntityManager em = getEntityManager();
		em.getTransaction().commit();
		closeEntityManager();
		log.debug(" Objeto gravado com sucesso!!!");
	}

	@Override
	public void limparTransacao() {
		EntityManager em = getEntityManager();
		if (em.getTransaction().isActive()) {
			em.getTransaction().rollback();
		}
		closeEntityManager();
	}

	@Override
	public void cancelarTransacao(Throwable e) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.clear();
			closeEntityManager();
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			if (e != null) {
				if (e instanceof RollbackException || e instanceof PersistenceException || e instanceof JDBCConnectionException || e instanceof JDBCConnectionException) {
					if (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause() instanceof BatchUpdateException) {
						BatchUpdateException exceptionBatchUpdateException = (BatchUpdateException) e.getCause().getCause();
						SQLException psqlException = exceptionBatchUpdateException.getNextException();
						throw new Exception(psqlException.getMessage());
					}
					if (e.getCause() != null && e.getCause().getCause() != null && e.getCause() instanceof JDBCConnectionException) {
						throw new Exception("Falha de conexão com o banco de dados.");
					}
					if (e instanceof JDBCConnectionException) {
						throw new Exception("Falha de conexão com o banco de dados.");
					}
				}
			}
		} catch (Throwable e2) {
			throw new Exception("Falha de conexão com o banco de dados.");
		}

	}

	public class DadosDeSuporteAPesquisa {
		public boolean temJoin = false;
	}

	public Map<String, Set<String>> recuperaObjetosComSuasProjecoes(String... projecoes) {
		Map<String, Set<String>> agregados = new LinkedHashMap<String, Set<String>>();
		for (String projecao : projecoes) {
			String propriedade = "";
			String nomeDoAgregado = "";
			String[] tokes = projecao.split("\\.");
			Set<String> projecoesPorAgregado = null;
			if (projecao.contains(".")) {
				for (int i = 0; i < tokes.length - 1; i++) {
					nomeDoAgregado = nomeDoAgregado + "." + tokes[i];
				}
				nomeDoAgregado = nomeDoAgregado.substring(1);
				propriedade = tokes[tokes.length - 1];
			} else {
				nomeDoAgregado = "";
				propriedade = tokes[0];
			}
			if (!agregados.containsKey(nomeDoAgregado)) {
				projecoesPorAgregado = new LinkedHashSet<String>();
				agregados.put(nomeDoAgregado, projecoesPorAgregado);
			}
			projecoesPorAgregado = agregados.get(nomeDoAgregado);
			projecoesPorAgregado.add(propriedade);
		}
		return agregados;
	}

	public boolean temIdNasProjecoes(Set<String> projecoes) {
		for (String string : projecoes) {
			if (string.equals("id")) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {

		ProBaseDAO dao = new ProBaseDAO();
		String[] projecoes = { "id", "pro2.id", "pro2.agregado.agregado2.descricao", "pro2.agregado.descricao", "pro2.descricao", "descricao" };
		Map<String, Set<String>> agregados = dao.recuperaObjetosComSuasProjecoes(projecoes);
		projecoes = dao.acrecentaIdNasProjecoes(projecoes, agregados);
		for (Object object : projecoes) {
			log.debug("objeto", object);
		}
	}

	public String[] acrecentaIdNasProjecoes(String[] projecoes, Map<String, Set<String>> agregados) {
		Set<String> ids = new LinkedHashSet<String>();
		Iterator<Entry<String, Set<String>>> iterator = agregados.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Set<String>> mapEntry = iterator.next();
			if (!temIdNasProjecoes(mapEntry.getValue())) {
				if (mapEntry.getKey().equals("")) {
					ids.add(mapEntry.getKey() + "id");
				} else {
					ids.add(mapEntry.getKey() + ".id");
				}
			}
		}
		String[] projecoesFinal = new String[projecoes.length + ids.size()];
		int i = 0;
		for (String string : projecoes) {
			projecoesFinal[i++] = string;
		}
		for (String string : ids) {
			projecoesFinal[i++] = string;
		}
		return projecoesFinal;
	}

	@Override
	public void updateEmLote(Class classe, Map<String, Object> valores, List<Long> ids) throws Exception {
		Set<String> atributos = valores.keySet();

		StringBuilder atributoValor = new StringBuilder();

		for (String atributo : atributos) {

			if ((valores.get(atributo).getClass().getName().indexOf("String") != -1) || (valores.get(atributo).getClass().isEnum())) {
				atributoValor.append(", " + atributo + " = '" + valores.get(atributo) + "'");
			} else if (valores.get(atributo).getClass().getName().indexOf("Date") != -1) {

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
				atributoValor.append(", " + atributo + " = '" + format.format(valores.get(atributo)) + "'");

			} else {
				atributoValor.append(", " + atributo + " = " + valores.get(atributo));
			}

		}

		String hql = "update " + classe.getSimpleName() + " set  nrVersao = nrVersao + 1, " + atributoValor.substring(1) + "  where id in  ( :ids )";

		Query query = getEntityManager().createQuery(hql);
		query.setParameter("ids", ids);

		query.executeUpdate();
		getEntityManager().flush();
	}

	@Override
	public <E extends ProBaseVO> String recuperaObjetoRest(E vo, String[] exclusaoJson) throws Exception {
		ProBaseVO voRecuperado = recuperaObjeto(vo);
		getEntityManager().clear();
		String aux = ProDAOHelper.jsonSerializer(voRecuperado, exclusaoJson);
		return aux;
	}

	private boolean objetoAindaNaoPersistido(ProBaseVO vo) {
		EntityManager entityManager = getEntityManager();
		if (entityManager.contains(vo)) {
			entityManager.detach(vo);
		}
		Query query = entityManager.createQuery("select id  from " + vo.getClass().getName() + " where id = " + vo.getId());
		List list = query.getResultList();
		boolean empty = list.isEmpty();
		return empty;
	}

}
