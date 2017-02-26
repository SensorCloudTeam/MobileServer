package com.publish;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.base.DefinedUtil;
import com.base.DispatchServer;
import com.base.ProtoMessage;
import com.base.Protocal;
import com.hibernate.Sink;
import com.hibernate.SinkDao;
import com.hibernate.UserDao;

public class SinkThread implements Runnable,Protocal{
	private static final Log log = LogFactory.getLog(SinkThread.class); // 日志操作对象
	/**
	 * msg = {v:'v1', d:'sink', t:'post', a:'one', u:{user_id:'lyz',name:'wxs',longitude:'',latitude:''}, l:{sid:'sessionID'}} ; 发布sink节点
	 * 	   = {v:'v1', d:'sink', t:'put', a:'one', u:{id:'ewfh1bQS',name:'wxs',longitude:'',latitude:''}, l:{sid:'sessionID'}} ; 修改sink节点
	 * 	   = {v:'v1', d:'sink', t:'del', a:'one', u:{id:'ewfh1bQS'}, l:{sid:'sessionID'}} ; 删除sink节点
	 *     = {v:'v1', d:'sink', t:'get', a:'one', u:{id:'ewfh1bQS'}} ; 获取sink节点
	 *     = {v:'v1', d:'sink', t:'get', a:'user_pub_many', u:{user_id:'lyz'}, l:{sid:'sessionID'}} ; 获取用户发布的sink节点
	 *     = {v:'v1', d:'sink', t:'get', a:'all', u:{}, l:{sid:'sessionID'}} ; 获取所有sink节点信息
	 *     
	 */
	private ProtoMessage msg;
	private Socket socket;
	public SinkThread(Socket socket, ProtoMessage msg){
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
			getSinkById();
		}else if(msg.action.equals("user_pub_many")){
			getSinksByUserId();
		}else if(msg.action.equals("all")){
			getAllSinks();
		}
	}
	public void post(){
		if(msg.action.equals("one")){
			addSinkNode();
		}
	}
	public void put(){
		if(msg.action.equals("one")){
			updateSinkInfo();
		}else if(msg.action.equals("name")){
			
		}else if(msg.action.equals("position")){
			
		}
	}
	public void del(){
		if(msg.action.equals("one")){
			deleteSinkNode();
		}
	}
	
	private void addSinkNode(){
		/*
		String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}*/
		UserDao userDao = new UserDao();
		SinkDao sinkDao = new SinkDao();
		Sink sink = new Sink();
		String id = getAuthorizedKey();
		while(sinkDao.getSinkById(id)!=null){
			id = getAuthorizedKey();//缺陷是当表中的数据非常多的时候，这个步骤耗时长
		}
		sink.setId(id);
		sink.setUser(userDao.getUserById(msg.definedContent.getString("user_id")));
		sink.setName(msg.definedContent.getString("name"));
		sink.setLatitude(new BigDecimal(msg.definedContent.getString("latitude")));
		sink.setLongitude(new BigDecimal(msg.definedContent.getString("longitude")));
		sinkDao.addSink(sink);
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			String result = DefinedUtil.composeJSONString("201","success",sink);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("add sink error",ex);
		}
	}
	/**
	 * 随机组合个数为1.36e14,取每次随机字符串的前8位
	 * @return
	 */
	private String getAuthorizedKey(){
		int length = 8;
		StringBuffer list = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
		for(int i=0;i<list.length();i++){
			int index = (int)Math.floor(Math.random()*list.length());
			char temp = list.charAt(i);
			list.setCharAt(i, list.charAt(index));
			list.setCharAt(index, temp);
		}
		return list.substring(0, length);
	}
	
	private void deleteSinkNode(){
		/*String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}*/
		String id = msg.definedContent.getString("id");
		String result = "";
		if(id!=null){
			SinkDao sinkDao = new SinkDao();
			if(sinkDao.deleteSinkById(msg.definedContent.getString("id"))!=null){
				result = DefinedUtil.composeJSONString("201","success");
			}
			else{
				result = DefinedUtil.composeJSONString("404","has no such id"); 
			}
		}
		else{
			result = DefinedUtil.composeJSONString("404","id is null");
		}
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
	
	private void getSinkById(){
		String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}
		String id = msg.definedContent.getString("id");
		String result = "";
		if(id!=null){
			SinkDao sinkDao = new SinkDao();
			Sink sink = sinkDao.getSinkById(id);
			if(sink!=null)
				result = DefinedUtil.composeJSONString("201", "success", sink); 
			else
				result = DefinedUtil.composeJSONString("404", "has no such sink", sink);
		}else{
			result = DefinedUtil.composeJSONString("404", "id is null");
		}
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
	
	private void updateSinkInfo(){
		String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}
		String result = "";
		String id = msg.definedContent.getString("id");
		SinkDao sinkDao = new SinkDao();
		Sink sink = sinkDao.getSinkById(id);
		if(sink!=null){
			String name = msg.definedContent.getString("name");
			if(name!=null){
				sink.setName(msg.definedContent.getString("name"));
			}
			String longitude = msg.definedContent.getString("longitude");
			if(longitude!=null){
				sink.setLongitude(new BigDecimal(longitude));
			}
			String latitude = msg.definedContent.getString("latitude");
			if(latitude!=null){
				sink.setLatitude(new BigDecimal(latitude));
			}
		    sinkDao.updateSink(sink);
		    result = DefinedUtil.composeJSONString("201", "success");
		}
		else{
			result = DefinedUtil.composeJSONString("404", "has no such sink");
		}
		try{
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(result);
			dos.close();
			socket.close();
		}catch(Exception ex){
			log.error("update sink error",ex);
		}
	    
	}
	
	private void getSinksByUserId(){
		String user_id = msg.definedContent.getString("user_id");
		SinkDao sinkDao = new SinkDao();
		List<Sink> list = sinkDao.getSinksByUserId(user_id);
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
	
	private void getAllSinks(){
		SinkDao sinkDao = new SinkDao();
		List<Sink> list = sinkDao.getAllSinksInfo();
		String result = DefinedUtil.composeJSONString("201", "sucsess",(List)list);
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
