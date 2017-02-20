package com.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

public class SensorDao {
	
	private static final Log log = LogFactory.getLog(SensorDao.class);
	
	public Sensor getSensorById(String id){
		Session session = null;
		Sensor sensor = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			sensor = (Sensor)session.get(Sensor.class, id);
			session.getTransaction().commit();
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("get sensor error",ex);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		return sensor;
	}
	
	public boolean addSensor(Sensor sensor){
		Session session = null;
		boolean result = false;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			session.save(sensor);
			session.getTransaction().commit();
			result = true;
		}
		catch(RuntimeException re){
			log.error("vaidate Sensor instatnce faild",re);
			session.getTransaction().rollback();
			result = false;
		}finally{
			session.close();
		}
		return result;
	}
	public boolean addSensor(String sinkId,String name,String typeId,boolean isPosted){
		int sensor_id = generatorSensorId(sinkId);
		String id = sinkId+"_"+sensor_id;
		Date date = new Date();
		SinkDao sinkDao = new SinkDao();
		Sink sink = sinkDao.getSinkById(sinkId);
		TypeDao typeDao = new TypeDao();
		Type type = typeDao.getTypeById(typeId);
		Sensor sensor = new Sensor(id,sink,sensor_id,name,date);
		sensor.setType(type);
		sensor.setPost(isPosted);
		return addSensor(sensor);
	}
	private int generatorSensorId(String sinkId){
		Session session = null;
		int sensorId = 0;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
		    Query query = session.createSQLQuery("select max(sensor_id) from sensor where sink_id='"+sinkId+"'");
		    List<Integer> list = query.list();
		    if(list.size()>0){
		    	sensorId = list.get(0);
		    }
		    sensorId++;
		}
		catch(RuntimeException re){
			log.error("vaidate Sensor instance faild",re);
		}finally{
			session.close();
		}
		return sensorId;
	}
	public Sensor deleteSensorById(String id){
		Session session = null;
		Sensor sensor = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			sensor = (Sensor)session.load(Sensor.class, id);
			session.delete(sensor);
			session.getTransaction().commit();
		}
		catch(Exception re){
			log.error("delete sensor error",re);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		return sensor;
	}
	
	public void updateSensor(Sensor sensor){
		Session session = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			session.update(sensor);
			
		}catch(Exception ex){
			log.error("update sensor error",ex);
		}finally{
			session.close();
		}
	}
	
	public List<Sensor> getSensorsBySinkId(String sinkId){
		Session session = null;
		List<Sensor> list = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			Criteria cri = session.createCriteria(Sensor.class);
			
			Sink sink =  (Sink)session.load(Sink.class, sinkId);
			cri.add(Restrictions.eq("sink",sink));
			list = cri.list();
			
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("get sensor collection error",ex);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		return list;
	}
	
	public List<Sensor> getSensorsByUserId(String userId){
		Session session = null;
		List<Sensor> list = null;
		try{
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			session.beginTransaction();
			Criteria cri = session.createCriteria(Sensor.class);

			User user=(User)session.load(User.class, userId);
			cri.add(Restrictions.eq("user",user));
			list = cri.list();
			
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("get sensors collection error",ex);
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		return list;
	}
}
