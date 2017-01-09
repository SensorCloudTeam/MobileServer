package com.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserDao {
	private static final Log log = LogFactory.getLog(UserDao.class);
	public boolean login(String id,String pwd){
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			Session s = sf.openSession();
			//Transaction tx = s.beginTransaction();
			List<User> list = s.createSQLQuery("select * from user where id='"+id+"' and password='"+pwd+"'").list();
			if(list.size()>0){
				return true;
			}
		}
		catch(RuntimeException re){
			log.error("vaidate User instatnce faild",re);
		}
		return false;
	}
	public void regist(User user){
		Session session = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
			
		}
		catch(RuntimeException re){
			log.error("vaidate User instatnce faild",re);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
	}
	
	public User getUserById(String id){
		Session session = null;
		User user = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			user = (User)session.get(User.class, id);
			session.getTransaction().commit();
		}
		catch(RuntimeException re){
			log.error("vaidate User instatnce faild",re);
		}finally{
			session.close();
		}
		return user;
	}
}
