package com.hibernate;
// Generated 2016-12-30 13:34:40 by Hibernate Tools 4.3.1.Final

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.base.DefinedJSONObject;

/**
 * Sensor generated by hbm2java
 */
public class Sensor implements java.io.Serializable, DefinedJSONObject {

	private String id;
	private Sink sink;
	private Type type;
	private int sensorId;
	private String name;
	private Date dataTime;
	private Integer sampleRate;
	private String value;
	private Boolean post;
	private Set subscriptions = new HashSet(0);

	public Sensor() {
	}

	public Sensor(String id, Sink sink, int sensorId, String name, Date dataTime) {
		this.id = id;
		this.sink = sink;
		this.sensorId = sensorId;
		this.name = name;
		this.dataTime = dataTime;
	}

	public Sensor(String id, Sink sink, Type type, int sensorId, String name, Date dataTime, Integer sampleRate,
			String value, Boolean post, Set subscriptions) {
		this.id = id;
		this.sink = sink;
		this.type = type;
		this.sensorId = sensorId;
		this.name = name;
		this.dataTime = dataTime;
		this.sampleRate = sampleRate;
		this.value = value;
		this.post = post;
		this.subscriptions = subscriptions;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Sink getSink() {
		return this.sink;
	}

	public void setSink(Sink sink) {
		this.sink = sink;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getSensorId() {
		return this.sensorId;
	}

	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDataTime() {
		return this.dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public Integer getSampleRate() {
		return this.sampleRate;
	}

	public void setSampleRate(Integer sampleRate) {
		this.sampleRate = sampleRate;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getPost() {
		return this.post;
	}

	public void setPost(Boolean post) {
		this.post = post;
	}

	public Set getSubscriptions() {
		return this.subscriptions;
	}

	public void setSubscriptions(Set subscriptions) {
		this.subscriptions = subscriptions;
	}

	public String toJSONString() {
		String result = "{";
		result +="id:'"+id+"',";
		result +="sensor_id:"+sensorId+",";
		//result +="sink:"+sink.toJSONString()+",";
		result +="name:'"+name+"',";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		result +="date_time:'"+sdf.format(dataTime)+"',";
		result +="sample_rate:"+(sampleRate==null?0:sampleRate)+",";
		result +="value:"+value+",";
		result +="post:"+post+"}";
		return result;
	}

}
