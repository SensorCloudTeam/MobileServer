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
import net.sf.json.JSONObject;
import net.sf.json.JSONString;

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
				
				System.out.println(type);
				if (version.equals("v1")) {
					if (domain.equals("user")) {
						//Runnable r = new UserThread(socket, version, domain, type,action, content);
						Runnable r = new UserThread(socket,msg);
						new Thread(r).start();
					} else if (domain.equals("sensor")) {
						
					} else if (domain.equals("sink")) {

					} else {
						log.debug("domain warning, has no defined " + domain);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
