package com.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

public class SubscriptionDao {
	private static final Log log = LogFactory.getLog(SubscriptionDao.class);
	
	public Subscription addSubscription(String sensorId,String userId,Date subTime,int sendFrequency,String address,int filter,float thresholdValue){
		Session session = null;
		Subscription sub = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.buildSessionFactory();
			sf.openSession();
			session.beginTransaction();
			Sensor sensor = (Sensor)session.load(Sensor.class, sensorId);
			User user = (User)session.load(User.class, userId);
			sub = new Subscription();
			sub.setSensor(sensor);
			sub.setUser(user);
			sub.setSubTime(subTime);
			sub.setSendFrequency(sendFrequency);
			sub.setAddress(address);
			sub.setFilter(filter);
			sub.setThresholdValue(thresholdValue);
			session.getTransaction().commit();
		}catch(Exception ex){
			log.error("add subscription error",ex);
			session.getTransaction().rollback();
			sub = null;
		}finally{
			session.close();
		}
		return sub;
	}
	
	public boolean addSubscription(Subscription sub){
		Session session = null;
		boolean result = false;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			session.save(sub);
			session.getTransaction().commit();
			result = true;
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("add subscription error",ex);
			session.getTransaction().rollback();
			result = false;
		}finally{
			session.close();
			
		}
		return result;
	}
	
	public Subscription deleteSubscription(String id){
		Session session = null;
		Subscription sub = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			sub = (Subscription)session.load(Subscription.class,id);
			session.delete(sub);
			session.getTransaction().commit();
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("delete subscription error",ex);
			session.getTransaction().rollback();
			sub = null;
		}finally{
			session.close();
		}
		return sub;
	}
	
	public void editSubscription(Subscription sub){
		Session session = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			session.update(sub);
			session.getTransaction().commit();
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("edit subscription error",ex);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
	}
	
	public Subscription getSubscriptionById(String id){
		Session session = null;
		Subscription sub = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			sub = (Subscription)session.load(Subscription.class, id);
			session.getTransaction().commit();
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("get subscription error",ex);
			session.getTransaction().rollback();
			sub = null;
		}finally{
			session.close();
		}
		return sub;
	}
	
	public List<Subscription> getSubByUserId(String userId){
		Session session = null;
		List<Subscription> list = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			Criteria cri = session.createCriteria(Subscription.class);
			
			User user=(User)session.load(User.class, userId);
			cri.add(Restrictions.eq("user",user));
			list = cri.list();
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("get subscriptions collection error", ex);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		return list;
	}
}
