package com.datastax.user.interactions.model;

import java.util.Date;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="datastax", name="session_paths_global")
public class SessionPathGlobal {
	@PartitionKey
	private String userid;
	private Date updated;
	private String path;
	
	public SessionPathGlobal(){
		
	}
	
	public SessionPathGlobal(String userid, Date updated, String path) {
		super();
		this.userid = userid;
		this.updated = updated;
		this.path = path;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
