package com.hibernate;
/*
 * 查询订阅数据时SQL语句得到的结果表对应的类，在SunscriptionDao中用到
 */
import java.math.BigDecimal;
import java.util.Date;

import com.base.DefinedJSONObject;

public class SubSinkSensor implements java.io.Serializable, DefinedJSONObject{

	private Integer id;
	private String sensorId;
	private String userId;
	private Date subTime;
	private Integer sendFrequency;
	private String address;
	private String phoneNum;
	private int filter;
	private Float thresholdValue;
	private String sensorName;
	private String typeName;
	private String sinkName;
	private BigDecimal longitude;
	private BigDecimal latitude;
	
    public SubSinkSensor() {
		
	}
	
	public SubSinkSensor(String sensorId,String userId,Date subTime,Integer sendFrequency,String address,String phoneNum,String sensorName,
			String typeName,BigDecimal longitude,BigDecimal latitude){
		
		this.sensorId = sensorId;
		this.userId = userId;
		this.subTime = subTime;
		this.sendFrequency = sendFrequency;
		this.address = address;
		this.phoneNum = phoneNum;
		this.sensorName = sensorName;
		this.typeName = typeName;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public String getSinkName() {
		return sinkName;
	}

	public void setSinkName(String sinkName) {
		this.sinkName = sinkName;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setThresholdValue(Float thresholdValue) {
		this.thresholdValue = thresholdValue;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUserId() {
		return this.userId;
	}
	public void setUserId(){
		this.userId = userId;
	}
	
	public String getSensorId() {
		return this.sensorId;
	}
	public void setSensorId(){
		this.sensorId = sensorId;
	}
	
	public Date getSubTime() {
		return this.subTime;
	}
	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}

	public Integer getSendFrequency() {
		return this.sendFrequency;
	}
	public void setSendFrequency(Integer sendFrequency) {
		this.sendFrequency = sendFrequency;
	}

	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	public int getFilter() {
		return this.filter;
	}
	public void setFilter(int filter) {
		this.filter = filter;
	}
	
	public float getThresholdValue() {
		return this.thresholdValue;
	}
	public void setThresholdValue() {
		this.thresholdValue = thresholdValue;
	}
	
	public String getSensorName() {
		return this.sensorName;
	}
	public void setsensorName(){
		this.sensorName = sensorName;
	}
	
	public String getTypeName() {
		return this.typeName;
	}
	public void setTypeName(){
		this.typeName = typeName;
	}
	
	public BigDecimal getLongitude() {
		return this.longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return this.latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	
	public String toJSONString() {
		String result = "{";
		result += "sensor_id:'"+sensorId+"',";
		result += "user_id:'"+userId+"',";
		result += "sub_time:'"+subTime+"',";
		result += "sendfrequency:'"+sendFrequency+"',";
		result += "sensorName:'"+sensorName+"',";
		result += "typeName:'"+typeName+"',";
		result += "sinkName:'"+sinkName+"',";
		result += "longitude:'"+longitude.toString()+"',";
		result += "latitude:'"+latitude.toString()+"'";
		result += "}";
		
		return result;
	}}
