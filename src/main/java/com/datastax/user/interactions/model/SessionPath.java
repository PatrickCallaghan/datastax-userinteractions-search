package com.datastax.user.interactions.model;

import java.util.Date;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="datastax", name="session_paths")
public class SessionPath {

	@PartitionKey
	private String correlationid;
	
	@ClusteringColumn
	@Column(name="date")
	private Date dateTime;

	private String userid;
	private String forward_path;
	private String reverse_path;
	
	public String getCorrelationid() {
		return correlationid;
	}
	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getForward_path() {
		return forward_path;
	}
	public void setForward_path(String forward_path) {
		this.forward_path = forward_path;
	}
	public String getReverse_path() {
		return reverse_path;
	}
	public void setReverse_path(String reverse_path) {
		this.reverse_path = reverse_path;
	}	
	
	
}
