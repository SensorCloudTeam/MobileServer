package com.base;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mine.UserThread;
import com.publish.HdataThread;
import com.publish.SensorThread;
import com.publish.SinkThread;
import com.publish.SubscriptionThread;

import net.sf.json.JSONObject;

public class DispatchServer {
	private static final Log log = LogFactory.getLog(DispatchServer.class);
	public static HashSet<String> sessionList = new HashSet<String>(); //用于记录当前已登录的客户端，已登录用户需要将sessionID传给服务器以免重复登录
	private static ServerSocket server;

	public static void main(String args[]) {
		start();
	}

	public static void start() {
		try {
			System.out.println("Server重新启动");
			server = new ServerSocket(3205);// SensorCloudPlatformMobileServer的unicode码之和
			while (true) {
				Socket socket = server.accept();
				System.out.println("建立连接!");
				InputStream is = socket.getInputStream();
				DataInputStream dis = new DataInputStream(is);
				JSONObject jsonObj = JSONObject.fromObject(dis.readUTF());
			    System.out.println(jsonObj.toString());
				/*
				 * v：version, 协议版本 
				 * d：domain, 作用的数据范围
				 * t：type, 请求类型（get,post,put,del）
				 * a: action, 动作编码
				 * c: content, 参数内容
				 */
				String version = jsonObj.getString("v");
				String type = jsonObj.getString("t");
				String domain = jsonObj.getString("d");
				String action = jsonObj.getString("a");
				JSONObject content = jsonObj.getJSONObject("u");
				JSONObject localContent = jsonObj.getJSONObject("l");
				ProtoMessage msg = new ProtoMessage(version,domain,type,action,content,localContent);
		
				if (version.equals("v1")) {
					if (domain.equals("user")) {
						//Runnable r = new UserThread(socket, version, domain, type,action, content);
						Runnable r = new UserThread(socket,msg);
						new Thread(r).start();
					} else if (domain.equals("sensor")) {
						Runnable r = new SensorThread(socket,msg);
						new Thread(r).start();
					} else if (domain.equals("sink")) {
						Runnable r = new SinkThread(socket,msg);
						new Thread(r).start();
					}else if(domain.equals("subscription")){
						Runnable r = new SubscriptionThread(socket,msg);
						new Thread(r).start();
					}else if(domain.equals("hdata")){
						Runnable r = new HdataThread(socket,msg);
						new Thread(r).start();
					}else {
						log.debug("domain warning, has no defined " + domain);
					}
				}
			}
		} catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
