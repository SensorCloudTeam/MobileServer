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
		//getUserInfo();
		//getSinkInfo();
		getPubInfo();
		getSensorInfo();
		getSubscriptionInfo();
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
	
	public static void getSubscriptionInfo(){
		try{
			Socket socket = new Socket("127.0.0.1",3205);
			OutputStream os=socket.getOutputStream();  
	        DataOutputStream dos=new DataOutputStream(os);
	        String output = "{v:'v1',d:'subscription',t:'get',a:'user_sub_many', u:{user_id:'test'}, l:{sid:'"+Monitor.sessionId+"'} }";
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
