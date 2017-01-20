package com.publish;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.base.DefinedUtil;
import com.base.ProtoMessage;
import com.hibernate.Subscription;
import com.hibernate.SubscriptionDao;

public class SubscriptionThread implements Runnable{
	private static final Log log = LogFactory.getLog(SubscriptionThread.class); // 日志操作对象
	/**msg=
	 *{v:'v1', d:'subscription', t:'post', a:'one', u:{sensor_id:'',user_id:'lyz',send_frequency:2,address:'',filter:0,threshold_value:0}, l:{sid:'sessionID'}} ; 新增订阅信息
	 *{v:'v1', d:'subscription', t:'del', a:'one',u:{id:1},l:{sid:'sessionID'}} ； 删除订阅信息，应该限制只能删除自己的订阅信息，有待后续添加
	 *{v:'v1', d:'subscription', t:'put', a:'one',u:{id:1,address:'',filter:0,threshold_value:0},l:{sid:'sessionID'}} ; 修改订阅信息，address/filter/threshold_value可选
	 *{v:'v1', d:'subscription', t:'get', a:'user_sub_many', u:{user_id:'test'}, l:{sid:'sessionID'}} ; 获取用户订阅的信息
	 */
	private ProtoMessage msg;
	private Socket socket;
	public SubscriptionThread(Socket socket, ProtoMessage msg){
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
		if(msg.action.equals("user_sub_many")){
			getSubByUserId();
		}
	}
	public void post(){
		if(msg.action.equals("one")){
			addSubscription();
		}
	}
	public void put(){
		if(msg.action.equals("one")){
			updateSubscription();
		}
	}
	public void del(){
		if(msg.action.equals("one")){
			deleteSubscription();
		}
	}
	
	private void addSubscription(){
		String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}
		String sensorId = msg.definedContent.getString("sensor_id");
		String userId = msg.definedContent.getString("user_id");
		Date subTime = new Date();
		int sendFrequency = msg.definedContent.getInt("send_fre");
		String address = msg.definedContent.getString("adress");
		int filter = msg.definedContent.getInt("filter");
		float thresholdValue = (float)msg.definedContent.getDouble("threshold_value");
		SubscriptionDao subDao = new SubscriptionDao();
		Subscription sub = subDao.addSubscription(sensorId, userId, subTime, sendFrequency, address, filter, thresholdValue);
		String result = "";
		if(sub!=null){
			result = DefinedUtil.composeJSONString("201", "success");
		}
		else{
			result = DefinedUtil.composeJSONString("404", "failed");
		}
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("add subscription error",ex);
		}
	}
	
	private void updateSubscription(){
		String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}
		String result = "";
		
		if(msg.localContent.has("id")){
			String id = msg.localContent.getString("id");
			
			SubscriptionDao subDao = new SubscriptionDao();
			Subscription sub = subDao.getSubscriptionById(id);
			
			if(msg.localContent.has("address")){
				String address = msg.localContent.getString("adress");
				sub.setAddress(address);
			}
			if(msg.localContent.has("filter")){
				int filter = msg.localContent.getInt("filter");
				sub.setFilter(filter);
			}
			if(msg.localContent.has("threshold_value")){
				float thresholdValue = (float)msg.localContent.getDouble("threshold_value");
				sub.setThresholdValue(thresholdValue);
			}
			result = DefinedUtil.composeJSONString("201", "success");
		}
		else{
			result = DefinedUtil.composeJSONString("404", "failed");
		}
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("add subscription error",ex);
		}
	}
	
	private void deleteSubscription(){
		String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}
		String result = "";
		String id = msg.definedContent.getString("id");
		SubscriptionDao subDao = new SubscriptionDao();
		Subscription sub = subDao.deleteSubscription(id);
		if(sub!=null){
			result = DefinedUtil.composeJSONString("201", "success");
		}
		else{
			result = DefinedUtil.composeJSONString("404", "failed");
		}
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("add subscription error",ex);
		}
	}
	
	private void getSubByUserId(){
		String user_id = msg.definedContent.getString("user_id");
		SubscriptionDao subDao = new SubscriptionDao();
		List<Subscription> list = subDao.getSubByUserId(user_id);
		String result = DefinedUtil.composeJSONString("201", "success", (List)list);
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("delete subscription error",ex);
		}
	}
}
