package com.datastax.user.message.model;

import java.util.Date;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="datastax_demo", name="messages")
public class Message{
	
	private String msg;
	
	@ClusteringColumn
	private Date date;
	
	@PartitionKey(1)
	private String sendee;
	
	@PartitionKey(0)
	private String sender;

	public Message(String sender, String sendee, Date date, String msg){
		
		this.sender = sender;
		this.sendee = sendee;
		this.date = date;
		this.msg = msg;
	}
	public Message(){
		
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSendee() {
		return sendee;
	}

	public void setSendee(String sendee) {
		this.sendee = sendee;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	@Override
	public String toString() {
		return "Message [msg=" + msg + ", date=" + date + ", sendee=" + sendee + ", sender=" + sender + "]";
	}	
}