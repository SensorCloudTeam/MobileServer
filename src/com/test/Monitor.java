package com.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import net.sf.json.JSONObject;

public class Monitor {
	private static String sessionId;
	public static void main(String args[]){
		login();
		//regist();
		//logout();
		//updateUserInfo();
		//getUserInfo();
		//getSinkInfo();
		//getPubInfo();
		//getSensorInfo();
		//getHdataInfo();
		//getSubscriptionInfo();
		//getAllSinksInfo();
		//addSinkNode();
		//addSensorNode();
		//addSub();
		//delSensorNode();
		//delSub();
		sendEmail();
		//sendSms();
	}
	
	public static void login(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
            DataOutputStream dos=new DataOutputStream(os); 
            String output = "{v:'v1',d:'user',t:'get',a:'login', u:{id:'test',pwd:'123456'}, l:{}}";
            dos.writeUTF(output);
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
            JSONObject obj = JSONObject.fromObject(result);
            JSONObject localContent =  obj.getJSONObject("d");
            Monitor.sessionId = localContent.getString("sid");  
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void regist(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
            DataOutputStream dos=new DataOutputStream(os); 
            String output = "{v:'v1',d:'user',t:'post',a:'regist',u:{id:'fcytest',pwd:'123456',email:'fangchengying@126.com',},1:{}}";
            dos.writeUTF(output);
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void logout(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'user', t:'get', a:'logout', u:{id:'3awNbZqr_1'}, l:{sid:'"+Monitor.sessionId+"'} }";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void updateUserInfo(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'user', t:'put', a:'key', u:{id:'test',key:'654321'}, l:{} }";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void getUserInfo(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output2 = "{v:'v1',d:'user',t:'get',a:'one', u:{id:'test'}, l:{sid:'"+Monitor.sessionId+"'} }";
            dos.writeUTF(output2);
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void getSinkInfo(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1',d:'sink',t:'get',a:'one', u:{id:'APkl9qzQ'}, l:{sid:'"+Monitor.sessionId+"'} }";
            dos.writeUTF(output);
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void getPubInfo(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1',d:'sink',t:'get',a:'user_pub_many', u:{user_id:'lyz'}, l:{} }";
            dos.writeUTF(output);
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void getSensorInfo(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1',d:'sensor',t:'get',a:'sink_many', u:{sink_id:'ewfh1bQS'}, l:{sid:'"+Monitor.sessionId+"'} }";
            dos.writeUTF(output);
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void getHdataInfo(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1',d:'hdata',t:'get',a:'hdata_many', u:{sink_id:'ewfh1bQS'}, l:{} }";
            dos.writeUTF(output);
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void getSubscriptionInfo(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1',d:'subscription',t:'get',a:'user_sub_many', u:{user_id:'fcy'}, l:{} }";
            dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void getAllSinksInfo(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'sink', t:'get', a:'all', u:{}, l:{sid:'sessionID'}}";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void addSinkNode(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'sink', t:'post', a:'one', u:{user_id:'test',name:'fcytest1',latitude:'31.132636000',longitude:'121.010937000'}, l:{} }";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void addSensorNode(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'sensor', t:'post', a:'one', u:{sink_id:'3awNbZqr',name:'test2',type_id:'1',post:'true'}, l:{} }";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void addSub(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'subscription', t:'post', a:'one', u:{sensor_id:'ewfh1bQS_2',user_id:'test',send_fre:'2',adress:'fangchengying93@126.com',phoneNum:'15201703786',filter:'0',threshold_value:'0'}, l:{} }";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void delSensorNode(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'sensor', t:'del', a:'one', u:{id:'3awNbZqr_1'}, l:{} }";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void delSub(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'subscription', t:'del', a:'one', u:{id:'2'}, l:{} }";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void sendEmail(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'subscription', t:'post', a:'email', u:{adress:'fangchengying93@126.com',sensor_id:'ewfh1bQS_3'}, l:{} }";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void sendSms(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1', d:'subscription', t:'post', a:'phone', u:{number:'15201703786',sensor_id:'ewfh1bQS_3'}, l:{} }";
	        dos.writeUTF(output);
            InputStream is = socket.getInputStream(); 
            DataInputStream dis = new DataInputStream(is);
            String result = dis.readUTF();
            System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
