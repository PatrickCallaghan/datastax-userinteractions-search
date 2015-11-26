package com.datastax.user.interactions.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Timer;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metrics;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.user.interactions.model.UserInteraction;

public class UserInteractionDao {

	private static Logger logger = LoggerFactory.getLogger(UserInteractionDao.class);
	private Session session;

	private DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
	private static String keyspaceName = "datastax_userinteractions2_demo";
	private static String userInteractionsTable = keyspaceName + ".user_interactions";

	private static final String INSERT_INTO_USER_INTERACTION = "Insert into " + userInteractionsTable
			+ " (user_id, app, time, action) values (?,?,?,?);";
	
	private PreparedStatement insertUserInteractionStmt;

	public UserInteractionDao(String[] contactPoints) {

		Cluster cluster = Cluster.builder().addContactPoints(contactPoints).build();
		this.session = cluster.connect();
		
		KeyspaceMetadata keyspaceMetadata = this.session.getCluster().getMetadata().getKeyspace(keyspaceName);

		this.insertUserInteractionStmt  = session.prepare(INSERT_INTO_USER_INTERACTION);
	}

	public void insertUserInteraction(List<UserInteraction> userInteractions) {

	}


	public void printMetrics() {
		logger.info("Metrics");
		Metrics metrics = session.getCluster().getMetrics();
		Gauge<Integer> gauge = metrics.getConnectedToHosts();
		Integer numberOfHosts = gauge.getValue();
		logger.info("Number of hosts: "+ numberOfHosts);
		Metrics.Errors errors = metrics.getErrorMetrics();
		Counter counter = errors.getReadTimeouts();
		logger.info("Number of read timeouts:"+  counter.getCount());
		com.codahale.metrics.Timer timer = metrics.getRequestsTimer();
		Timer.Context context = timer.time();
		try {
			long numberUserRequests = timer.getCount();
			logger.info("Number of user requests:"+ numberUserRequests);
		} finally {
			context.stop();
		}
	}
}
