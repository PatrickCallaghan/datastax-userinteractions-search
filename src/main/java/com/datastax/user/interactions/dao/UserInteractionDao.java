package com.datastax.user.interactions.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.user.interactions.model.SessionPath;
import com.datastax.user.interactions.model.SessionPathGlobal;
import com.datastax.user.interactions.model.UserInteraction;
import com.google.common.util.concurrent.ListenableFuture;

public class UserInteractionDao {

	private static Logger logger = LoggerFactory.getLogger(UserInteractionDao.class);
	private Session session;

	private Mapper<UserInteraction> mapper;
	private Mapper<SessionPath> pathMapper;
	private Mapper<SessionPathGlobal> globalMapper;

	public UserInteractionDao(String[] contactPoints) {

		Cluster cluster = Cluster.builder().addContactPoints(contactPoints).build();
		this.session = cluster.connect();

		this.mapper = new MappingManager(this.session).mapper(UserInteraction.class);
		this.pathMapper = new MappingManager(this.session).mapper(SessionPath.class);
		this.globalMapper = new MappingManager(this.session).mapper(SessionPathGlobal.class);
	}

	public void insertUserInteraction(List<UserInteraction> userInteractions) {
		
		List<ListenableFuture<Void>> futures = new ArrayList(userInteractions.size());
		
		for (UserInteraction userInteraction : userInteractions) {
			
			futures.add(mapper.saveAsync(userInteraction));
		}	
	}

	public void insertSessionPath(SessionPath sessionPath) {
		
		pathMapper.save(sessionPath);
	}
	
	public void updateSessionPathGlobal(SessionPathGlobal sessionPathGlobal) {
		
		globalMapper.save(sessionPathGlobal);
	}

	public SessionPathGlobal getGlobalPath (String userId){
		return globalMapper.get(userId);
	}
}
