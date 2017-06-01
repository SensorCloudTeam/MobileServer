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
	private static final Log log = LogFactory.getLog(SubscriptionThread.class); // ��־��������
	/**msg=
	 *{v:'v1', d:'subscription', t:'post', a:'one', u:{sensor_id:'',user_id:'lyz',send_frequency:2,address:'',filter:0,threshold_value:0}, l:{sid:'sessionID'}} ; ����������Ϣ
	 *{v:'v1', d:'subscription', t:'del', a:'one',u:{id:1},l:{sid:'sessionID'}} �� ɾ��������Ϣ��Ӧ������ֻ��ɾ���Լ��Ķ�����Ϣ���д��������
	 *{v:'v1', d:'subscription', t:'put', a:'one',u:{id:1,address:'',filter:0,threshold_value:0},l:{sid:'sessionID'}} ; �޸Ķ�����Ϣ��address/filter/threshold_value��ѡ
	 *{v:'v1', d:'subscription', t:'get', a:'user_sub_many', u:{user_id:'test'}, l:{sid:'sessionID'}} ; ��ȡ�û����ĵ���Ϣ
	 */
	private ProtoMessage msg;
	private Socket socket;
	
	 // �����ʼ��ķ�������IP�Ͷ˿�    
    private String mailServerHost;    
    private String mailServerPort = "25";   

    // �ʼ������ߵĵ�ַ    
    private String fromAddress;    
    // �ʼ������ߵĵ�ַ    
    private String toAddress;    
    // ��½�ʼ����ͷ��������û���������    
    private String userName;    
    private String password;    
    // �Ƿ���Ҫ�����֤    
    private boolean validate = true;    
    // �ʼ�����    
    private String subject;    
    // �ʼ����ı�����    
    private String content;    
    // �ʼ��������ļ���    
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
	//�����ʼ�
	public void sendEmail(){
		final Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		props.put("mail.smtp.starttls.enable", "true");// ʹ�� STARTTLS��ȫ����
		props.put("mail.smtp.auth", "true"); // ʹ����֤
		 // �����˵��˺�
        props.put("mail.user", "ECNU_Sei_Lab301@163.com");
        // ����SMTP����ʱ��Ҫ�ṩ������
        props.put("mail.password", "seilab301");
		// ������Ȩ��Ϣ�����ڽ���SMTP���������֤
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // �û���������
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // ʹ�û������Ժ���Ȩ��Ϣ�������ʼ��Ự
        Session mailSession = Session.getInstance(props, authenticator);
        // �����ʼ���Ϣ
        MimeMessage message = new MimeMessage(mailSession);
        String sensorId = msg.definedContent.getString("sensor_id");
        SensorDao sensorDao = new SensorDao();
        Sensor sensor = sensorDao.getSensorById(sensorId);
		try {
			// ���÷�����
			InternetAddress form;
			form = new InternetAddress(
			        props.getProperty("mail.user"));
			message.setFrom(form);
			// �����ռ���
	        InternetAddress to = new InternetAddress(msg.definedContent.getString("adress"));
	        message.setRecipient(RecipientType.TO, to);
	        // �����ʼ�����
	        message.setSubject("SensorCloud������");
	        // �����ʼ���������
	        String text = "SensorCloud��������\n";
	        text += sensor.getName()+":"+sensor.getValue();
	        text += "\n";
	        text += "�����������ʱ�䣺"+sensor.getDataTime();
	        text += "\n";
	        text += "����ʦ����ѧ   ���ѧԺ";
	        message.setContent(text, "text/html;charset=UTF-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String result = "";
        try {
        	// �����ʼ�
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
	
	//���Ͷ���
	public void sendSms(){
		String sensorId = msg.definedContent.getString("sensor_id");
        SensorDao sensorDao = new SensorDao();
        Sensor sensor = sensorDao.getSensorById(sensorId);
        
        //ͨ�÷��ͽӿڵ�http��ַ
        final String URI_SEND_SMS = "http://yunpian.com/v1/sms/send.json";
        //�����ʽ�����ͱ����ʽͳһ��UTF-8
        final String ENCODING = "UTF-8";
		String apikey = "6e660d3105db8f4c95c55d07b5e9fdb9";
		//Ҫ���͵��ֻ���
		String mobile = msg.definedContent.getString("number");
		String text = "��������ƽ̨��  ���ĵ�����Ϊ";
        text += sensor.getName()+":"+sensor.getValue();
        text += "\n";
        text += "�����������ʱ�䣺"+sensor.getDataTime();
        text += "\n";
        text += "����ʦ����ѧ   ���ѧԺ";
		
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
