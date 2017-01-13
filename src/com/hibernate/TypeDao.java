package com.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TypeDao {
	private static final Log log = LogFactory.getLog(TypeDao.class);
	public Type getTypeById(String id){
		Configuration cfg = new Configuration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		Type type = null;
		try{
			type = (Type)session.get(Type.class, id);
			session.getTransaction().commit();
		}
		catch(Exception re){
			log.error("delete sensor error",re);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		return type;
	}
}
