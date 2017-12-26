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

import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LRUMap;
// FIXME: MIGRACAO -> Dependencia com log4j removida (revisar)
//import org.apache.log4j.Category;
//import org.apache.log4j.NDC;

/**
 * This class is <b>not</b> meant to be thread-safe. It does not appear to make sense. It is easy to do with
 * Collections.synchronizedMap(cacheMap) and not using the iterator (instead, copy keys to a different array and remove
 * in timerTask using that array). Making operations synchronized will slow down the operations.
 * 
 * WARNING: Not yet tested in production. Use at your own risk.
 */
public class ExpiringCache {

//	static Category logger = Category.getInstance(ExpiringCache.class);

	public static final long DEFAULT_TIME_TO_LIVE = 10 * 60 * 1000;
	public static final long DEFAULT_ACCESS_TIMEOUT = 5 * 60 * 1000;
	public static final long DEFAULT_TIMER_INTERVAL = 2 * 60 * 1000;

	long ttl = DEFAULT_TIME_TO_LIVE;
	long ato = DEFAULT_ACCESS_TIMEOUT;
	long tiv = DEFAULT_TIMER_INTERVAL;

	LRUMap cacheMap;
	Timer cacheManager;

	protected void finalize() throws Throwable {
		if (cacheManager != null)
			cacheManager.cancel();
	}

	public ExpiringCache() {
		cacheMap = new LRUMap();
		initialize();
	}

	// All times in millisecs
	public ExpiringCache(long timeToLive, long accessTimeout, int maximumCachedQuantity, long timerInterval) {
		ttl = timeToLive;
		ato = accessTimeout;
		cacheMap = new LRUMap(maximumCachedQuantity);
		tiv = timerInterval;
		initialize();
	}

	public void setTimeToLive(long milliSecs) {
		ttl = milliSecs;
		initialize();
	}

	public void setAccessTimeout(long milliSecs) {
		ato = milliSecs;
		initialize();
	}

	public void setCleaningInterval(long milliSecs) {
		tiv = milliSecs;
		initialize();
	}

	public void initialize() {
//		if (logger.isDebugEnabled())
//			logger.debug("initialize() started");
		if (cacheManager != null)
			cacheManager.cancel();
		cacheManager = new Timer(true);
		cacheManager.schedule(new TimerTask() {
			public void run() {
//				NDC.push("TimerTask");
				long now = System.currentTimeMillis();
				try {
					MapIterator itr = cacheMap.mapIterator();
					while (itr.hasNext()) {
						Object key = itr.next();
						CachedObject cobj = (CachedObject) itr.getValue();
						if (cobj == null || cobj.hasExpired(now)) {
//							if (logger.isDebugEnabled())
//								logger.debug("Removing " + key + ": Idle time=" + (now - cobj.timeAccessedLast)
//										+ "; Stale time:" + (now - cobj.timeCached));
							itr.remove();
							Thread.yield();
						}
					}
				} catch (ConcurrentModificationException cme) {
					/*
					 * Ignorable. This is just a timer cleaning up. It will catchup on cleaning next time it runs.
					 */
//					if (logger.isDebugEnabled())
//						logger.debug("Ignorable ConcurrentModificationException");
				}
//				NDC.remove();
			}
		}, 0, tiv);
	}

	public int howManyObjects() {
		return cacheMap.size();
	}

	public void clear() {
		cacheMap.clear();
	}

	/**
	 * If the given key already maps to an existing object and the new object is not equal to the existing object,
	 * existing object is overwritten and the existing object is returned; otherwise null is returned. You may want to
	 * check the return value for null-ness to make sure you are not overwriting a previously cached object. May be you
	 * can use a different key for your object if you do not intend to overwrite.
	 */
	public Object admit(Object key, Object dataToCache) {
		// cacheMap.put(key, new CachedObject(dataToCache));
		// return null;

		CachedObject cobj = (CachedObject) cacheMap.get(key);
		if (cobj == null) {
			cacheMap.put(key, new CachedObject(dataToCache));
			return null;
		} else {
			Object obj = cobj.getCachedData(key);
			if (obj == null) {
				if (dataToCache == null) {
					// Avoids creating unnecessary new cachedObject
					// Number of accesses is not reset because object is the same
					cobj.timeCached = cobj.timeAccessedLast = System.currentTimeMillis();
					return null;
				} else {
					cacheMap.put(key, new CachedObject(dataToCache));
					return null;
				}
			} else if (obj.equals(dataToCache)) {
				// Avoids creating unnecessary new cachedObject
				// Number of accesses is not reset because object is the same
				cobj.timeCached = cobj.timeAccessedLast = System.currentTimeMillis();
				return null;
			} else {
				cacheMap.put(key, new CachedObject(dataToCache));
				return obj;
			}
		}
	}

	public Object admit(Object key, Object dataToCache, long objectTimeToLive, long objectIdleTimeout) {
		// cacheMap.put(key, new CachedObject(dataToCache));
		// return null;

		CachedObject cobj = (CachedObject) cacheMap.get(key);
		if (cobj == null) {
			cacheMap.put(key, new CachedObject(dataToCache, objectTimeToLive, objectIdleTimeout));
			return null;
		} else {
			Object obj = cobj.getCachedData(key);
			if (obj == null) {
				if (dataToCache == null) {
					// Avoids creating unnecessary new cachedObject
					// Number of accesses is not reset because object is the same
					cobj.timeCached = cobj.timeAccessedLast = System.currentTimeMillis();
					cobj.objectTTL = objectTimeToLive;
					cobj.objectIdleTimeout = objectIdleTimeout;
					cobj.userTimeouts = true;
					return null;
				} else {
					cacheMap.put(key, new CachedObject(dataToCache, objectTimeToLive, objectIdleTimeout));
					return null;
				}
			} else if (obj.equals(dataToCache)) {
				// Avoids creating unnecessary new cachedObject
				// Number of accesses is not reset because object is the same
				cobj.timeCached = cobj.timeAccessedLast = System.currentTimeMillis();
				cobj.objectTTL = objectTimeToLive;
				cobj.objectIdleTimeout = objectIdleTimeout;
				cobj.userTimeouts = true;
				return null;
			} else {
				cacheMap.put(key, new CachedObject(dataToCache, objectTimeToLive, objectIdleTimeout));
				return obj;
			}
		}
	}

	public Object recover(Object key) {
		CachedObject cobj = (CachedObject) cacheMap.get(key);
		if (cobj == null)
			return null;
		else
			return cobj.getCachedData(key);
	}

	public void discard(Object key) {
		cacheMap.remove(key);
	}

	public long whenCached(Object key) {
		CachedObject cobj = (CachedObject) cacheMap.get(key);
		if (cobj == null)
			return 0;
		return cobj.timeCached;
	}

	public long whenLastAccessed(Object key) {
		CachedObject cobj = (CachedObject) cacheMap.get(key);
		if (cobj == null)
			return 0;
		return cobj.timeAccessedLast;
	}

	public int howManyTimesAccessed(Object key) {
		CachedObject cobj = (CachedObject) cacheMap.get(key);
		if (cobj == null)
			return 0;
		return cobj.numberOfAccesses;
	}

	/**
	 * A cached object, needed to store attributes such as the last time it was accessed.
	 */
	protected class CachedObject {
		Object cachedData;
		long timeCached;
		long timeAccessedLast;
		int numberOfAccesses;
		long objectTTL;
		long objectIdleTimeout;
		boolean userTimeouts;

		CachedObject(Object cachedData) {
			long now = System.currentTimeMillis();
			this.cachedData = cachedData;
			timeCached = now;
			timeAccessedLast = now;
			++numberOfAccesses;
		}

		CachedObject(Object cachedData, long timeToLive, long idleTimeout) {
			long now = System.currentTimeMillis();
			this.cachedData = cachedData;
			objectTTL = timeToLive;
			objectIdleTimeout = idleTimeout;
			userTimeouts = true;
			timeCached = now;
			timeAccessedLast = now;
			++numberOfAccesses;
		}

		Object getCachedData(Object key) {
			long now = System.currentTimeMillis();
			if (hasExpired(now)) {
				cachedData = null;
				cacheMap.remove(key);
				return null;
			}
			timeAccessedLast = now;
			++numberOfAccesses;
			return cachedData;
		}

		boolean hasExpired(long now) {
			long usedTTL = userTimeouts ? objectTTL : ttl;
			long usedATO = userTimeouts ? objectIdleTimeout : ato;

			if (now > timeAccessedLast + usedATO || now > timeCached + usedTTL) {
				return true;
			} else
				return false;
		}

	}

} // END OF CLASS

