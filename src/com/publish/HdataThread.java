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
import com.hibernate.Hdata;
import com.hibernate.HdataDao;
import com.hibernate.HdataSensors;

public class HdataThread implements Runnable,Protocal{
	private static final Log log = LogFactory.getLog(HdataThread.class); // 日志操作对象
	/**
	* msg = {v:'v1', d:'sensor', t:'post', a:'one', u:{sink_id:'ewfh1bQS',name:'温度传感器1',type_id:'',post:''}, l:{sid:'sessionID'}} ; 发布sensor节点
	*     = {v:'v1', d:'sensor', t:'put', a:'one', u:{id:'ewfh1bQS_1',name:'wxs',sample_rate:'',post:''}, l:{sid:'sessionID'}} ; 修改已发布的sensor节点，name/post/sample_rate都是可选属性
	*     = {v:'v1', d:'sensor', t:'del', a:'one', u:{id:'ewfh1bQS_1'}, l:{sid:'sessionID'}} ; 删除sensor节点
	*     = {v:'v1', d:'sensor', t:'get', a:'sink_many', u:{sink_id:'ewfh1bQS'}} ; 获取sink节点下的所有sensor
	*     = {v:'v1', d:'sensor', t:'get', a:'user_many', u:{user_id:'lyz'}, l:{sid:'sessionID'}} ; 获取用户订阅的所有sensor
	*/
	private ProtoMessage msg;
	private Socket socket;
	public HdataThread(Socket socket, ProtoMessage msg){
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
			
		}else if(msg.action.equals("hdata_many")){
			getDatasBySink();
		}
	}
	public void post(){
		
	}
	public void put(){
		
	}
	public void del(){
		
	}
	
	private void getDatasBySink(){
		String sinkId = msg.definedContent.getString("sink_id");
		HdataDao hdataDao = new HdataDao();
		List<HdataSensors> list = hdataDao.getHdatasBySinkId(sinkId);
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			String result;
			result = DefinedUtil.composeJSONString("201", "success",(List)list);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("get hdatas error",ex);
		}
	}
}
