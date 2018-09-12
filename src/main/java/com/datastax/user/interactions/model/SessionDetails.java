package com.datastax.user.interactions.model;

import java.util.List;

public class SessionDetails {
	
	private SessionPath sessionPath;
	private List<UserInteraction> interactions;
	public SessionDetails(SessionPath sessionPath, List<UserInteraction> interactions) {
		super();
		this.sessionPath = sessionPath;
		this.interactions = interactions;
	}
	
	public SessionPath getSessionPath() {
		return sessionPath;
	}
	
	public List<UserInteraction> getInteractions() {
		return interactions;
	}
}
