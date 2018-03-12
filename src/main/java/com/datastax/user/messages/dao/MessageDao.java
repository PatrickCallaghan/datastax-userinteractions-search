package com.datastax.user.messages.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.user.message.model.Message;
import com.google.common.util.concurrent.ListenableFuture;

public class MessageDao {

	private static Logger logger = LoggerFactory.getLogger(MessageDao.class);
	private Session session;

	private Mapper<Message> mapper;

	public MessageDao(String[] contactPoints) {

		Cluster cluster = Cluster.builder().addContactPoints(contactPoints).build();
		this.session = cluster.connect();

		this.mapper = new MappingManager(this.session).mapper(Message.class);
	}

	public void insertMessage(Message message) {
		
		mapper.saveAsync(message);
	}
}
