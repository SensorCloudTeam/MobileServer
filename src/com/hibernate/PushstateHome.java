// Generated 2016-12-30 13:34:40 by Hibernate Tools 4.3.1.Final
package com.hibernate;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
/**
 * Home object for domain model class Pushstate.
 * @see .Pushstate
 * @author Hibernate Tools
 */
public class PushstateHome {

	private static final Log log = LogFactory.getLog(PushstateHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Pushstate transientInstance) {
		log.debug("persisting Pushstate instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Pushstate instance) {
		log.debug("attaching dirty Pushstate instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Pushstate instance) {
		log.debug("attaching clean Pushstate instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Pushstate persistentInstance) {
		log.debug("deleting Pushstate instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Pushstate merge(Pushstate detachedInstance) {
		log.debug("merging Pushstate instance");
		try {
			Pushstate result = (Pushstate) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Pushstate findById(java.lang.String id) {
		log.debug("getting Pushstate instance with id: " + id);
		try {
			Pushstate instance = (Pushstate) sessionFactory.getCurrentSession().get("Pushstate", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Pushstate instance) {
		log.debug("finding Pushstate instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("Pushstate").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
