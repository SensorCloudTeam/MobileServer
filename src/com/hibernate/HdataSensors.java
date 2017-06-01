package com.hibernate;
/*
 * 用于查询hdata数据表时得到的数据列表对应的类，在HdataDao中使用
 */
import java.math.BigDecimal;
import java.util.Date;

import com.base.DefinedJSONObject;

public class HdataSensors implements java.io.Serializable, DefinedJSONObject {

	private Integer id; 
	private Integer sensorId;
	private String sinkId;
	private String name;
	private Integer typeId;
	private Date time;
	private String value;
	private String sensorName;
	private String typeName;
	private String sinkName;
	
	public HdataSensors() {
		
	}
	
	public HdataSensors(Integer sensorId,String sinkId,Date time,String value,String sensorName,
			String typeName,String sinkName){
		
		this.sensorId = sensorId;
		this.sinkId = sinkId;
		this.time = time;
		this.value = value;
		this.sensorName = sensorName;
		this.sensorName = sensorName;
		this.typeName = typeName;
		this.sinkName = sinkName;
	}
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public String getSinkId() {
		return sinkId;
	}

	public void setSinkId(String sinkId) {
		this.sinkId = sinkId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Integer getTypeId() {
		return typeId;
	}



	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}



	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}



	public String getSensorName() {
		return sensorName;
	}



	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}



	public String getTypeName() {
		return typeName;
	}



	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}



	public String getSinkName() {
		return sinkName;
	}



	public void setSinkName(String sinkName) {
		this.sinkName = sinkName;
	}

	public String toJSONString() {
		String result = "{";
		result += "sensor_id:'"+sensorId+"',";
		result += "sink_id:'"+sinkId+"',";
		result += "time:'"+time+"',";
		result += "value:'"+value+"',";
		result += "sensorName:'"+sensorName+"',";
		result += "typeName:'"+typeName+"',";
		result += "sinkName:'"+sinkName+"',";
		result += "}";
		
		return result;
	}
}
