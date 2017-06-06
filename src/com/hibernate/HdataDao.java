package com.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
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

public class HdataDao {
	private static final Log log = LogFactory.getLog(HdataDao.class);
	
	public List<HdataSensors> getHdatasBySinkId(String sinkId){
		Session session = null;
		List<HdataSensors> listHdataSensors = null;
		try{
			listHdataSensors = new ArrayList<HdataSensors>();
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			Query query = session.createSQLQuery("SELECT hdata.*,sensor.name AS sensor_name,type.name AS type_name,sink.name AS sink_name FROM TYPE,hdata,sensor,sink WHERE sensor.sensor_id = hdata.sensor_id AND sensor.sink_id = hdata.sink_id AND sink.id = sensor.sink_id AND sensor.type_id = type.id AND hdata.sink_id='" + sinkId + "'");
			
			List list = query.list();
			HdataSensors sss;
			for(int i = 0;i<list.size();i++){
				sss = new HdataSensors();
				Object[] object = (Object[])list.get(i);// 每行记录不再是一个对象 而是一个数组
				
				Integer id = (Integer)(object[0]);
				Integer sensorId = (Integer)object[1];
			    //String sensorId = (String)object[1];
			    sinkId = (String)object[2];
			    //SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			    String name = (String)object[3];
			    Integer typeId = (Integer)(object[4]);
			    Date time = (Date)object[5];
			    String value = (String)object[6];
			    String sensorName =  (String)object[7];
			    String typeName =  (String)object[8];
			    String sinkName =  (String)object[9];
			    
			 // 重新封装在一个javabean里面
			    sss.setId(id);
			    sss.setSensorId(sensorId);
			    sss.setSinkId(sinkId);
			    sss.setName(name);
			    sss.setTypeId(typeId);
			    sss.setTime(time);
			    sss.setValue(value);
			    sss.setSensorName(sensorName);
			    sss.setTypeName(typeName);
			    sss.setSinkName(sinkName);
			    listHdataSensors.add(sss); // 最终封装在list中 传到前台。
			}
			
		}catch(Exception ex){
			log.error("get hdata collection error",ex);
		}finally{
			session.close();
		}
		return listHdataSensors;
	}
	
	public List<HdataSensors> getSensorHdata(String sinkId,int sensorId){
		Session session = null;
		List<HdataSensors> listHdataSensors = null;
		try{
			listHdataSensors = new ArrayList<HdataSensors>();
			Configuration cfg = new Configuration();
			SessionFactory sf = cfg.configure().buildSessionFactory();
			session = sf.openSession();
			Query query = session.createSQLQuery("SELECT hdata.*,sensor.name AS sensor_name,type.name AS type_name,sink.name AS sink_name FROM TYPE,hdata,sensor,sink WHERE sensor.sensor_id = hdata.sensor_id AND sensor.sink_id = hdata.sink_id AND sink.id = sensor.sink_id AND sensor.type_id = type.id AND hdata.sink_id='" + sinkId + "' AND hdata.sensor_id='" + sensorId +"'");
			
			List list = query.list();
			HdataSensors sss;
			for(int i = 0;i<list.size();i++){
				sss = new HdataSensors();
				Object[] object = (Object[])list.get(i);// 每行记录不再是一个对象 而是一个数组
				
				Integer id = (Integer)(object[0]);
				sensorId = (Integer)object[1];
			    sinkId = (String)object[2];
			    //SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			    String name = (String)object[3];
			    Integer typeId = (Integer)(object[4]);
			    Date time = (Date)object[5];
			    String value = (String)object[6];
			    String sensorName =  (String)object[7];
			    String typeName =  (String)object[8];
			    String sinkName =  (String)object[9];
			    
			 // 重新封装在一个javabean里面
			    sss.setId(id);
			    sss.setSensorId(sensorId);
			    sss.setSinkId(sinkId);
			    sss.setName(name);
			    sss.setTypeId(typeId);
			    sss.setTime(time);
			    sss.setValue(value);
			    sss.setSensorName(sensorName);
			    sss.setTypeName(typeName);
			    sss.setSinkName(sinkName);
			    listHdataSensors.add(sss); // 最终封装在list中 传到前台。
			}
			
		}catch(Exception ex){
			log.error("get hdata collection error",ex);
		}finally{
			session.close();
		}
		return listHdataSensors;
	}
}
