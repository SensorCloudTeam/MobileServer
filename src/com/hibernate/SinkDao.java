package com.hibernate;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SinkDao {
	private static final Log log = LogFactory.getLog(UserDao.class);
	public void addSink(Sink sink){
		Session session = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			session.save(sink);
			session.getTransaction().commit();
			
		}
		catch(RuntimeException re){
			log.error("vaidate User instatnce faild",re);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
	}
	public Sink getSinkById(String id){
		Session session = null;
		Sink sink = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			sink = (Sink)session.get(Sink.class, id);
			session.getTransaction().commit();
		}
		catch(RuntimeException re){
			log.error("vaidate User instatnce faild",re);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		return sink;
	}
	public Sink deleteSinkById(String id){
		Session session = null;
		Sink sink = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			sink = (Sink)session.load(Sink.class, id);
			session.delete(sink);
			session.getTransaction().commit();
		}
		catch(Exception re){
			log.error("delete sink error",re);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		return sink;
	}
	
	public void updateSink(Sink sink){
		Session session = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			session.update(sink);
			
		}catch(Exception ex){
			log.error("update sink error",ex);
		}finally{
			session.close();
		}
	}
	public boolean updateSinkPosition(String id, String longitude, String latitude){
		String hql = "UPDATE sink SET longitude=:longitude and latitude=:latitude where id=:id";
		Session session = null;
		int result=0;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			Query query = session.createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("longitude",new BigDecimal(longitude));
			query.setParameter("latitude", new BigDecimal(latitude));
			result = query.executeUpdate();
			
		}catch(Exception ex){
			log.error("update sink error",ex);
		}finally{
			session.close();
		}
		return result>0?true:false;
	}
}
