package com.publish;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.base.DefinedUtil;
import com.base.ProtoMessage;
import com.base.Protocal;
import com.hibernate.Sensor;
import com.hibernate.SensorDao;
import com.hibernate.Sink;
import com.hibernate.SinkDao;

public class SensorThread implements Runnable,Protocal{
	private static final Log log = LogFactory.getLog(SensorThread.class); // 日志操作对象
	/**
	* msg = {v:'v1', d:'sensor', t:'post', a:'one', u:{sink_id:'ewfh1bQS',name:'温度传感器1',type_id:'',post:''}, l:{sid:'sessionID'}} ; 发布sensor节点
	*     = {v:'v1', d:'sensor', t:'put', a:'one', u:{id:'ewfh1bQS_1',name:'wxs',sample_rate:'',post:''}, l:{sid:'sessionID'}} ; 修改已发布的sensor节点，name/post/sample_rate都是可选属性
	*     = {v:'v1', d:'sensor', t:'del', a:'one', u:{id:'ewfh1bQS_1'}, l:{sid:'sessionID'}} ; 删除sensor节点
	*     = {v:'v1', d:'sensor', t:'get', a:'sink_many', u:{sink_id:'ewfh1bQS'}} ; 获取sink节点下的所有sensor
	*     = {v:'v1', d:'sensor', t:'get', a:'user_many', u:{user_id:'lyz'}, l:{sid:'sessionID'}} ; 获取用户订阅的所有sensor
	*/
	private ProtoMessage msg;
	private Socket socket;
	public SensorThread(Socket socket, ProtoMessage msg){
		this.socket = socket;
		this.msg = msg;
	}
	public void run(){
		String type = msg.type;
		if(type.equals("get")){
			get();
		}
		else if(type.equals("post")){
			post();
		}
		else if(type.equals("put")){
			put();
		}
		else if(type.equals("del")){
			del();
		}
		else{
			log.debug("has no type "+type);
		}
	}
	
	public void get(){
		if(msg.action.equals("one")){
			
		}else if(msg.action.equals("sink_many")){
			getSensorsBySink();
		}else if(msg.action.equals("user_many")){
			getSensorsByUser();
		}
	}
	public void post(){
		if(msg.action.equals("one")){
			addSensorNode();
		}
	}
	public void put(){
		if(msg.action.equals("one")){
			updateSensorInfo();
		}else if(msg.action.equals("name")){
			
		}else if(msg.action.equals("position")){
			
		}
	}
	public void del(){
		if(msg.action.equals("one")){
			deleteSensorNode();
		}
	}
	
	private void addSensorNode(){
		/*
		String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}*/
		String result = "";
		String sinkId = msg.definedContent.getString("sink_id");
		String name = msg.definedContent.getString("name");
		String typeId = msg.definedContent.getString("type_id");
		boolean isPosted = msg.definedContent.getBoolean("post");
		SensorDao sensorDao = new SensorDao();
		boolean success = sensorDao.addSensor(sinkId, name, typeId, isPosted);
		if(success){
			result = DefinedUtil.composeJSONString("201","success");
		}
		else{
			result = DefinedUtil.composeJSONString("404","failed");
		}
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("add sink error",ex);
		}
	}
	
	private void updateSensorInfo(){
		String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}
		String result = "";
		SensorDao sensorDao = new SensorDao();
		String id = msg.definedContent.getString("id");
		Sensor sensor = sensorDao.getSensorById(id);	
		if(sensor!=null){
			if(msg.definedContent.has("name")){
				sensor.setName(msg.definedContent.getString("name"));
			}
			if(msg.definedContent.has("sample_rate")){
				int sampleRate = msg.definedContent.getInt("sample_rate");
				sensor.setSampleRate(sampleRate);
			}
			if(msg.definedContent.has("post")){
				sensor.setPost(msg.definedContent.getBoolean("post"));
			}
			result = DefinedUtil.composeJSONString("201","success");
		}
		else{
			result = DefinedUtil.composeJSONString("404","failed");
		}
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("updaste sensor error",ex);
		}
	}
	
	private void deleteSensorNode(){
		/*String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}*/
		SensorDao sensorDao = new SensorDao();
		String id = msg.definedContent.getString("id");
		Sensor sensor = sensorDao.deleteSensorById(id);
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			String result = "";
			if(sensor!=null){
				result = DefinedUtil.composeJSONString("201","success");
			}
			else{
				result = DefinedUtil.composeJSONString("404","failed");
			}
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("updaste sensor error",ex);
		}
	}
	private void getSensorsBySink(){
		String sinkId = msg.definedContent.getString("sink_id");
		SensorDao  sensorDao = new SensorDao();
		List<Sensor> list = sensorDao.getSensorsBySinkId(sinkId);
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			String result;
			result = DefinedUtil.composeJSONString("201", "success",(List)list);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("get sensors error",ex);
		}
		
	}
	
	private void getSensorsByUser2(){
		System.out.println("Sensor-getSensorsByUser");
		String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}
		String userId = msg.definedContent.getString("user_id");
		SensorDao sensorDao = new SensorDao();
		List<Sensor> list =  sensorDao.getSensorsByUserId(userId);
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			String result;
			result = DefinedUtil.composeJSONString("201", "success",(List)list);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("get sensors error",ex);
		}
	}
	private void getSensorsByUser() {
		String user_id = msg.definedContent.getString("user_id");
		SensorDao sensorDao = new SensorDao();
		List<Sensor> list = sensorDao.getSensorsByUserId(user_id);
		String result = DefinedUtil.composeJSONString("201", "success", (List)list);
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("delete sink error",ex);
		}
	}
}
