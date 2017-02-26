package com.hibernate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
		}catch(Exception re){
			//ex.printStackTrace();
			log.error("delete subscription error",re);
			session.getTransaction().rollback();
			//sub = null;
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
	
	public List<SubSinkSensor> getSubByUserId(String userId){
		Session session = null;
		List<SubSinkSensor> listSubSinkSensor = null;
		try{
			listSubSinkSensor = new ArrayList<SubSinkSensor>();
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			//session.beginTransaction();
			//Criteria cri = session.createCriteria(Subscription.class);
			//User user=(User)session.load(User.class, userId);
			//cri.add(Restrictions.eq("user",user));
			//list = cri.list();
			Query query = session.createSQLQuery("SELECT subscription.*,sensor.name as sensor_name,type.name as type_name,sink.name as sink_name,sink.latitude,sink.longitude FROM TYPE,subscription,sensor,sink WHERE sensor.id = subscription.sensor_id AND sink.id = sensor.sink_id AND sensor.type_id = type.id and subscription.user_id='" + userId + "'");
			List list = query.list();
			SubSinkSensor sss;
			for(int i = 0; i < list.size();i++){
				sss = new SubSinkSensor();
			    Object[] object = (Object[])list.get(i);// 每行记录不在是一个对象 而是一个数组

			    //System.out.println((String)object[0]);
			    Integer id = (Integer)(object[0]);
			    String sensorId = (String)object[1];
			    userId = (String)object[2];
			    //SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			    Date subTime = (Date)object[3];
			    Integer sendFrequency = (Integer)object[4];
			    String address = (String)object[5];
			    Integer filter = (Integer)object[6];
			    float thresholdValue =  (Float)object[7];
			    String sensorName =  (String)object[8];
			    String typeName =  (String)object[9];
			    String sinkName =  (String)object[10];
			    BigDecimal longitude = (BigDecimal)object[11];
			    BigDecimal latitude = (BigDecimal)object[12];
			    
			    // 重新封装在一个javabean里面
			    sss.setId(id);
			    sss.setSensorId(sensorId);
			    sss.setUserId(userId);
			    sss.setSubTime(subTime);
			    sss.setSendFrequency(sendFrequency);
			    sss.setAddress(address);
			    sss.setFilter(filter);
			    sss.setThresholdValue(thresholdValue);
			    sss.setSensorName(sensorName);
			    sss.setTypeName(typeName);
			    sss.setSinkName(sinkName);
			    sss.setLongitude(longitude);
			    sss.setLatitude(latitude);
			    listSubSinkSensor.add(sss); // 最终封装在list中 传到前台。
			} 
			
		}catch(Exception ex){
			//ex.printStackTrace();
			log.error("get subscriptions collection error", ex);
			//session.getTransaction().rollback();
		}finally{
			session.close();
		}
		return listSubSinkSensor;
	}
}
