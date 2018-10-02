package com.datastax.user.interactions.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
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

		try {
			List<ListenableFuture<Void>> futures = new ArrayList(userInteractions.size());

			for (UserInteraction userInteraction : userInteractions) {

				futures.add(mapper.saveAsync(userInteraction));
			}
		} catch (Exception e) {

		}
	}

	public void insertSessionPath(SessionPath sessionPath) {
		try {
			pathMapper.save(sessionPath);
		} catch (Exception e) {

		}
	}

	public void updateSessionPathGlobal(SessionPathGlobal sessionPathGlobal) {
		try {

			globalMapper.save(sessionPathGlobal);
		} catch (Exception e) {

		}
	}

	public SessionPathGlobal getGlobalPath(String userId) {
		return globalMapper.get(userId);
	}

	public List<String> getAllSessionFromAToB(String from, String to) {

		String select = "select * from datastax.session_paths WHERE solr_query = '{\"q\":\"forward_path:\\\"%s %s\\\"~3\", \"fq\":\"date:[NOW-15MINUTE TO *]\"}';";
		String cql = String.format(select, from, to);
		logger.info(cql);

		ResultSet rs = session.execute(cql);
		List<String> paths = new ArrayList<String>();

		Iterator<Row> iter = rs.iterator();
		while (iter.hasNext()) {

			if (rs.getAvailableWithoutFetching() == 10 && !rs.isFullyFetched()) {
				rs.fetchMoreResults(); // this is asynchronous
			}

			Row row = iter.next();

			String path = row.getString("forward_path");
			if (path.indexOf(from) < path.indexOf(to)) {

				paths.add(getPathSubString(from, to, path));
			}
		}

		return paths;
	}

	private String getPathSubString(String from, String to, String path) {
		String pathSubString = path.substring(0, path.lastIndexOf(to) - 1);

		pathSubString = pathSubString.substring(pathSubString.lastIndexOf(from));
		return pathSubString + " " + to;
	}
	
	
}
