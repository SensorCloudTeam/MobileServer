// default package
// Generated 2016-12-30 13:34:40 by Hibernate Tools 4.3.1.Final
package com.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Example;


/**
 * Home object for domain model class User.
 * @see .User
 * @author Hibernate Tools
 */
public class UserHome {

	private static final Log log = LogFactory.getLog(UserHome.class);
	
	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		/*
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
		*/
		
		return (SessionFactory) new AnnotationConfiguration().configure("hibernate.cfg.xml").buildSessionFactory();
	}

	public void persist(User transientInstance) {
		log.debug("persisting User instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(User instance) {
		log.debug("attaching dirty User instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public void attachClean(User instance) {
		log.debug("attaching clean User instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(User persistentInstance) {
		log.debug("deleting User instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public User merge(User detachedInstance) {
		log.debug("merging User instance");
		try {
			User result = (User) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public User findById(String id) {
		log.debug("getting User instance with id: " + id);
		try {
			User instance = (User) sessionFactory.getCurrentSession().get("User", id);
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

	public List findByExample(User instance) {
		log.debug("finding User instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("User").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	/*
	 * �������Զ���ķ���*/
	public boolean login(String id,String pwd){
		log.debug("validate login information");
		try{
			
			List<User> list = sessionFactory.getCurrentSession().createQuery("select * from user where id='"+id+"' and password='"+pwd+"'").list();
			if(list.size()>0){
				return true;
			}
		}
		catch(RuntimeException re){
			log.error("vaidate User instatnce faild",re);
		}
		return false;
	}
}