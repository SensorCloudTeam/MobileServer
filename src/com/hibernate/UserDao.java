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
	public User getUserById(String id){
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			Session s = sf.openSession();
			s.beginTransaction();
			/*
			//Transaction tx = s.beginTransaction();
			List<User> list = s.createSQLQuery("select * from user where id='"+id+"'").list();
			if(list==null||list.size()==0){
				log.debug("has no User "+id);
				return null;
			}
			else{
				return list.get(0);
			}
			*/
			User user = (User)s.get(User.class, id);
			//System.out.println(user.getEmail());
			s.getTransaction().commit();
			//s.close();
			return user;
		}
		catch(RuntimeException re){
			log.error("vaidate User instatnce faild",re);
		}
		return null;
	}
}
