package com.datastax.user.interactions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import com.datastax.demo.utils.PropertyHelper;
import com.datastax.user.interactions.dao.UserRepository;
import com.datastax.user.interactions.model.UserInteraction;

public class Main {

	private static final String USER_VISITS = "10000";
	private static final String NO_OF_USERS = "100000";

	private UserRepository repository;
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	private DateTime dateTime = new DateTime().minusDays(20);
	private Map<String, Date> userDateMap = new HashMap<String, Date>();
	
	public static void main(String args[]) {
		new Main();
    }
    
    public Main(){
    	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new ClassPathResource("spring-context.xml").getPath());
    	repository = context.getBean(UserRepository.class);
    	
    	int noOfUsers = Integer.parseInt(PropertyHelper.getProperty("noOfUsers", NO_OF_USERS));
    	int noOfVisits = Integer.parseInt(PropertyHelper.getProperty("noOfVisits", USER_VISITS));
    	
    	for (int i = 0; i < noOfVisits; i++){
    		
    		repository.save(createRandomUserInteraction(noOfUsers));
    		
    		if (i%10000 ==0){
    			logger.info("Processed " + i + " users visits.");
    		}
    	}
	}

	private List<UserInteraction> createRandomUserInteraction(int noOfUsers) {

		List<UserInteraction> interactions = new ArrayList<UserInteraction>();

		String user = "U" + (new Double(Math.random() * noOfUsers).intValue() + 1);
		String app = apps.get(new Double(Math.random() * apps.size()).intValue());
		String userAgent = userAgents.get(new Double(Math.random() * userAgents.size()).intValue());

		// Need to establish a good date. now or after the user logged out of
		// the last app they were using
		DateTime dateTime = new DateTime();

		if (userDateMap.containsKey(user)) {
			dateTime = new DateTime(userDateMap.get(user));
			dateTime = dateTime.plusSeconds(new Double(Math.random() * 60).intValue());
		} else {
			dateTime = new DateTime();
		}

		UUID correlationId = UUID.randomUUID();

		UserInteraction interaction = new UserInteraction();
		interaction.setId(UUID.randomUUID());
		interaction.setClientid(app);
		interaction.setCorrelationid(correlationId.toString());
		interaction.setDateTime(dateTime.toDate());
		interaction.setDetails("Login");
		interaction.setEvent_type("Login");		
		interaction.setReference("");
		interaction.setUser_agent(userAgent);
		interaction.setUserid(user);

		interactions.add(interaction);

		int items = new Double(Math.random() * 50).intValue() + 1;

		for (int i = 0; i < items; i++) {

			dateTime = dateTime.plusSeconds(new Double(Math.random() * 60).intValue());

			String action = actions.get(new Double(Math.random() * actions.size()).intValue());

			interaction = new UserInteraction();
			interaction.setClientid(app);
			interaction.setCorrelationid(correlationId.toString());
			interaction.setDateTime(dateTime.toDate());
			interaction.setDetails(action);
			interaction.setEvent_type(action);
			interaction.setId(UUID.randomUUID());
			interaction.setReference("");
			interaction.setUser_agent(userAgent);
			interaction.setUserid(user);

			interactions.add(interaction);
		}

		interaction = new UserInteraction();
		interaction.setClientid(app);
		interaction.setCorrelationid(correlationId.toString());
		interaction.setDateTime(dateTime.toDate());
		interaction.setDetails("Logout");
		interaction.setEvent_type("Logout");
		interaction.setId(UUID.randomUUID());
		interaction.setReference("");
		interaction.setUser_agent(userAgent);
		interaction.setUserid(user);

		interactions.add(interaction);
		userDateMap.put(user, dateTime.toDate());

		return interactions;
	}

	private List<String> userAgents = Arrays.asList(
			"Some User Agents for OS - WindowsInternet Explorer 7 (Windows Vista)", "Safari 125.8 (MacintoshOSX)",
			"Mozilla 1.6 (Debian)", "Googlebot 2.1 (New version)", "FireFox 2.0.0.19 (Ubuntu)",
			"Google Chrome 0.2.149.29 (Windows XP)", "Firefox 0.9 (MacintoshOSX )");

	private List<String> apps = Arrays.asList("mobilefx", "mobilefx", "mobilefx", "desktop ui", "tablet research",
			"tablet research", "iphone banking", "android banking", "mobile trading");

	private List<String> actions = Arrays.asList("tradeview", "news", "portfolio", "p and l", "charting",
			"advanced charting", "advanced charting", "advanced charting", "news", "news", "landing page",
			"preferences", "tradeview", "tradeview", "tradeview", "tradeview", "tradeview");
}
