package com.datastax.user.interactions.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

	public SessionPathGlobal getGlobalPath(String userId) {
		return globalMapper.get(userId);
	}

	public List<String> getAllSessionFromAToB(String from, String to) {

		String select = "select * from datastax.session_paths WHERE solr_query = '{\"q\":\"forward_path:\\\"%s %s\\\"~5\", \"fq\":\"date:[NOW-5MINUTE TO *]\"}';";
		String cql = String.format(select,from, to);
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

				String pathSubString = path.substring(path.indexOf(from), path.indexOf(to) - 1);
				paths.add(from + " " + pathSubString + " " + to);
			}
		}

		return paths;
	}
}
