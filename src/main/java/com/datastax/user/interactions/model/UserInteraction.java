package com.datastax.user.interactions.model;

import java.util.Date;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;

@Table(keyspace="datastax", name="user_interactions")
public class UserInteraction {

	@PartitionKey
	private String correlationid;
	@ClusteringColumn(0)
	private String userid;
	
	@ClusteringColumn(1)
	@Column(name="date")
	private Date dateTime;

	private String clientid;
	private String details;
	private String event_type;
	private String user_agent;;
	private String user_agent_filterd;
	private UUID id;
	private String reference;
	private String forward_path;
	private String reverse_path;
	
	public UserInteraction(){
		
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	public String getUser_agent_filterd() {
		return user_agent_filterd;
	}

	public void setUser_agent_filterd(String user_agent_filterd) {
		this.user_agent_filterd = user_agent_filterd;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReverse_path() {
		return reverse_path;
	}

	public void setReverse_path(String reverse_path) {
		this.reverse_path = reverse_path;
	}

	public String getForward_path() {
		return forward_path;
	}

	public void setForward_path(String forward_path) {
		this.forward_path = forward_path;
	}	
}
