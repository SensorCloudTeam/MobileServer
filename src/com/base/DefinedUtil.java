package com.base;

import com.hibernate.User;

import net.sf.json.JSONObject;

public class DefinedUtil {
	/**
	 * ¼ì²âÊÇ·ñÒÑµÇÂ¼
	 */
	public static boolean checkLogin(String sid) {
		return DispatchServer.sessionList.contains(sid);
	}
	/**
	 * {"c":"code","m":"msg","d":"data"}
	 */
	public static String composeJSONString(String code,String msg,DefinedJSONObject object){
		 JSONObject jo = new JSONObject();
		 jo.element("c", code);
		 jo.element("m", msg);
		 jo.element("d", object.toJSONString());
		 //jo.element("d", object);
		 return jo.toString();
	}
	public static String composeJSONString(String code,String msg,String data){
		 JSONObject jo = new JSONObject();
		 jo.element("c", code);
		 jo.element("m", msg);
		 jo.element("d", data);
		 return jo.toString();
	}
	public static String composeJSONString(String code,String msg){
		JSONObject jo = new JSONObject();
		 jo.element("c", code);
		 jo.element("m", msg);
		 return jo.toString();
	}
	public static void main(String args[]){
		User user = new User();
		user.setId("test");
		user.setPassword("123456");
		System.out.println(composeJSONString("201","success",(String)null));
	}
}
