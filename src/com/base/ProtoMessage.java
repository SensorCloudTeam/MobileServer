package com.base;

import net.sf.json.JSONObject;

public class ProtoMessage {
	public String version,domain,type,action,sessionId; //��������
	public JSONObject definedContent; //�û��Զ���Ĳ�������
	public JSONObject localContent; //����չ����
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
