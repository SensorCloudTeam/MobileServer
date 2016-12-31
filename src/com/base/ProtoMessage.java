package com.base;

import net.sf.json.JSONObject;

public class ProtoMessage {
	public String version,domain,type,action,sessionId; //基本参数
	public JSONObject definedContent; //用户自定义的参数部分
	public JSONObject localContent; //可扩展部分
	public ProtoMessage(String version,String domain,String type,String action){
		this.version = version;
		this.domain = domain;
		this.type = type;
		this.action = action;
	}
	public ProtoMessage(String version,String domain,String type,String action,JSONObject definedContent){
		this(version,domain,type,action);
		this.definedContent = definedContent;
	}
	public ProtoMessage(String version,String domain,String type,String action,JSONObject definedContent, JSONObject localContent){
		this(version,domain,type,action,definedContent);
		this.localContent = localContent;
	}
}
