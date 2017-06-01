package com.publish;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.base.DefinedUtil;
import com.base.ProtoMessage;
import com.hibernate.Sensor;
import com.hibernate.SensorDao;
import com.hibernate.SubSinkSensor;
import com.hibernate.Subscription;
import com.hibernate.SubscriptionDao;
import com.hibernate.UserDao;

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
	
	 // 发送邮件的服务器的IP和端口    
    private String mailServerHost;    
    private String mailServerPort = "25";   

    // 邮件发送者的地址    
    private String fromAddress;    
    // 邮件接收者的地址    
    private String toAddress;    
    // 登陆邮件发送服务器的用户名和密码    
    private String userName;    
    private String password;    
    // 是否需要身份验证    
    private boolean validate = true;    
    // 邮件主题    
    private String subject;    
    // 邮件的文本内容    
    private String content;    
    // 邮件附件的文件名    
    private String[] attachFileNames; 
	
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
		}else if(msg.action.equals("email")){
			sendEmail();
		}else if(msg.action.equals("phone")){
			sendSms();
		}else if(msg.action.equals("both")){
		
		}else{
			log.debug("has no type ");
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
		/*String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}*/
		SensorDao sensorDao = new SensorDao();
		UserDao userDao = new UserDao();
		SubscriptionDao subDao = new SubscriptionDao();
		Subscription sub = new Subscription();
		
		sub.setSensor(sensorDao.getSensorById(msg.definedContent.getString("sensor_id")));
		sub.setUser(userDao.getUserById(msg.definedContent.getString("user_id")));
		Date subTime = new Date();
		sub.setSubTime(subTime);
		sub.setSendFrequency(msg.definedContent.getInt("send_fre"));
		sub.setAddress(msg.definedContent.getString("adress"));
		sub.setPhoneNum(msg.definedContent.getString("phoneNum"));
		sub.setFilter(msg.definedContent.getInt("filter"));
		sub.setThresholdValue((float)msg.definedContent.getDouble("threshold_value"));
		subDao.addSubscription(sub);
		/*
		String sensorId = msg.definedContent.getString("sensor_id");
		String userId = msg.definedContent.getString("user_id");
		int sendFrequency = msg.definedContent.getInt("send_fre");
		String address = msg.definedContent.getString("adress");
		String phoneNum = msg.definedContent.getString("phoneNum");
		int filter = msg.definedContent.getInt("filter");
		float thresholdValue = (float)msg.definedContent.getDouble("threshold_value");
		//Subscription sub = subDao.addSubscription(sensorId, userId, subTime, sendFrequency, address, phoneNum, filter, thresholdValue);
		*/
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
	//发送邮件
	public void sendEmail(){
		final Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		props.put("mail.smtp.starttls.enable", "true");// 使用 STARTTLS安全连接
		props.put("mail.smtp.auth", "true"); // 使用验证
		 // 发件人的账号
        props.put("mail.user", "ECNU_Sei_Lab301@163.com");
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", "seilab301");
		// 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        String sensorId = msg.definedContent.getString("sensor_id");
        SensorDao sensorDao = new SensorDao();
        Sensor sensor = sensorDao.getSensorById(sensorId);
		try {
			// 设置发件人
			InternetAddress form;
			form = new InternetAddress(
			        props.getProperty("mail.user"));
			message.setFrom(form);
			// 设置收件人
	        InternetAddress to = new InternetAddress(msg.definedContent.getString("adress"));
	        message.setRecipient(RecipientType.TO, to);
	        // 设置邮件标题
	        message.setSubject("SensorCloud服务订阅");
	        // 设置邮件的内容体
	        String text = "SensorCloud订阅数据\n";
	        text += sensor.getName()+":"+sensor.getValue();
	        text += "\n";
	        text += "数据最近更新时间："+sensor.getDataTime();
	        text += "\n";
	        text += "华东师范大学   软件学院";
	        message.setContent(text, "text/html;charset=UTF-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String result = "";
        try {
        	// 发送邮件
			Transport.send(message);
			result = DefinedUtil.composeJSONString("201", "success",(Sensor)sensor);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	//发送短信
	public void sendSms(){
		String sensorId = msg.definedContent.getString("sensor_id");
        SensorDao sensorDao = new SensorDao();
        Sensor sensor = sensorDao.getSensorById(sensorId);
        
        //通用发送接口的http地址
        final String URI_SEND_SMS = "http://yunpian.com/v1/sms/send.json";
        //编码格式。发送编码格式统一用UTF-8
        final String ENCODING = "UTF-8";
		String apikey = "6e660d3105db8f4c95c55d07b5e9fdb9";
		//要发送的手机号
		String mobile = msg.definedContent.getString("number");
		String text = "【传感云平台】  订阅的数据为";
        text += sensor.getName()+":"+sensor.getValue();
        text += "\n";
        text += "数据最近更新时间："+sensor.getDataTime();
        text += "\n";
        text += "华东师范大学   软件学院";
		
		Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("text", text);
        params.put("mobile", mobile);
        
        String result;
        HttpClient client = new HttpClient();
        try {
            PostMethod method = new PostMethod(URI_SEND_SMS);
            if (params != null) {
                NameValuePair[] namePairs = new NameValuePair[params.size()];
                int i = 0;
                for (Map.Entry<String, String> param : params.entrySet()) {
                    NameValuePair pair = new NameValuePair(param.getKey(), param.getValue());
                    namePairs[i++] = pair;
                }
                method.setRequestBody(namePairs);
                HttpMethodParams param = method.getParams();
                param.setContentCharset(ENCODING);
            }
            client.executeMethod(method);
            result = DefinedUtil.composeJSONString("201", "success",method.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
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
		/*String sid = msg.localContent.getString("sid");
		if(!DefinedUtil.checkLogin(sid)){
			log.debug("has no login!");
			return;
		}*/
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
		List<SubSinkSensor> list = subDao.getSubByUserId(user_id);
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
