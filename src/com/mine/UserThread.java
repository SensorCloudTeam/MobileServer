package com.mine;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.base.DispatchServer;
import com.base.ProtoMessage;
import com.base.Protocal;
import com.base.DefinedUtil;
import com.hibernate.User;
import com.hibernate.UserDao;

public class UserThread implements Runnable, Protocal {
	private static final Log log = LogFactory.getLog(DispatchServer.class); // 日志操作对象
	private ProtoMessage msg;
	private Socket socket;

	/*
	 * 暂时不需要这么写 private static HashMap<String,String>
	 * actionGet,actionPost,actionPut,actionDel; static{ actionGet.put("login",
	 * "login");
	 * 
	 * }
	 */
	public UserThread() {

	}

	public UserThread(Socket socket, ProtoMessage msg) {
		this.socket = socket;
		this.msg = msg;
	}

	public void run() {
		try {
			if (msg.type.equals("get")) {
				get();
			} else if (msg.type.equals("post")) {
				post();
			} else if (msg.type.equals("put")) {
				put();
			} else if (msg.type.equals("del")) {
				del();
			} else {
				log.debug("type warning, has no defined " + msg.type);
			}
		} catch (Exception ex) {

		}
	}

	public void get() {
		if (msg.action.equals("login")) {
			login();
		} else if (msg.action.equals("logout")) {
			logout();
		} else if (msg.action.equals("one")) {
			getUserInfo();
		}
	}

	public void post() {

	}

	public void put() {

	}

	public void del() {

	}

	/**
	 * 登陆检验
	 */
	public void login() {
		try {
			String id = msg.definedContent.getString("id");
			String pwd = msg.definedContent.getString("pwd");
			if (id != null && pwd != null) {
				UserDao dao = new UserDao();
				boolean result = dao.login(id, pwd);
				Date now = new Date();

				OutputStream os = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);

				if (result) {
					// 保存sesssionID
					String ip = socket.getInetAddress().getHostAddress();
					String sessionId = ip + "_" + now.getTime();
					DispatchServer.sessionList.add(sessionId);
					/**
					 * c：code,表示服务器处理客户端请求的状态
					 * m: message,表示和code对应的信息，可选
					 * d: data,表示服务器返回的结果，可选
					 */
					//String output = "{c:'201', d:{sid:'" + sessionId + "'}}";
					String output = DefinedUtil.composeJSONString("201", "success","{sid:'" + sessionId + "'}");
					dos.writeUTF(output);
				} else {
					//String output = "{c:'404', m:'failed'}";
					String output = DefinedUtil.composeJSONString("404", "failed");
					dos.writeUTF(output);
				}
				dos.close();
			} else {
				log.debug("content warning, parameters are not right");
			}
		} catch (Exception ex) {
			log.error("check error", ex);
		}
	}

	/**
	 * 登出
	 */
	public void logout() {
		String sid = msg.definedContent.getString("sid");
		if (sid != null) {
			DispatchServer.sessionList.remove(sid);
		}
	}

	/**
	 * 获取用户信息
	 */
	public void getUserInfo() {
		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			String sid = msg.localContent.getString("sid");
			if (!DefinedUtil.checkLogin(sid)) {
				//dos.writeUTF("{c:'404', m:'no login', d:{}}");
				String output = DefinedUtil.composeJSONString("404", "no login");
				dos.writeUTF(output);
				return;
			}
			String id = msg.definedContent.getString("id");

			if (id != null) {

				UserDao dao = new UserDao();
				User user = dao.getUserById(id);

				if (user != null) {
					//String result = JSONObject.fromObject(user).toString(); //此处使用该方法会出现org.hibernate.LazyInitializationException
					//String result = user.toJSONString(); //toJSONString时自定义方法
					//dos.writeUTF("{c:'201',m:'success',d:'"+result+"'}");
					String output = DefinedUtil.composeJSONString("201", "success",user);
					dos.writeUTF(output);
				} else {
					//dos.writeUTF("{c:'404', m:'no user', d:{}}");
					String output = DefinedUtil.composeJSONString("404", "has no such user");
					dos.writeUTF(output);
				}
				dos.close();
				socket.close();
			}

		} catch (Exception ex) {
			log.error("get user info error", ex);
		}
	}

	
}
