package com.datastax.user.interactions.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.user.interactions.model.UserInteraction;
import com.google.common.util.concurrent.ListenableFuture;

public class UserInteractionDao {

	private static Logger logger = LoggerFactory.getLogger(UserInteractionDao.class);
	private Session session;

	private Mapper<UserInteraction> mapper;

	public UserInteractionDao(String[] contactPoints) {

		Cluster cluster = Cluster.builder().addContactPoints(contactPoints).build();
		this.session = cluster.connect();

		this.mapper = new MappingManager(this.session).mapper(UserInteraction.class);
	}

	public void insertUserInteraction(List<UserInteraction> userInteractions) {
		
		List<ListenableFuture<Void>> futures = new ArrayList(userInteractions.size());
		
		for (UserInteraction userInteraction : userInteractions) {
			
			futures.add(mapper.saveAsync(userInteraction));
		}
		
		
	}
}
