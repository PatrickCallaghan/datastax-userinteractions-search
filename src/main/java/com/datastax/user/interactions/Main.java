package com.datastax.user.interactions;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.PropertyHelper;
import com.datastax.user.message.model.Message;
import com.datastax.user.messages.dao.MessageDao;

public class Main {

	private static final String USER_MESSAGES = "1000000";
	private static final String NO_OF_USERS = "5000";

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	private DateTime dateTime = new DateTime().minusDays(365);
	private Map<String, Date> userDateMap = new HashMap<String, Date>();
	private long secondsToAdd;
	
	public static void main(String args[]) {
		new Main();
    }
    
    public Main(){
		String contactPointsStr = PropertyHelper.getProperty("contactPoints", "localhost");
    	int noOfUsers = Integer.parseInt(PropertyHelper.getProperty("noOfUsers", NO_OF_USERS));
    	int noOfMessages = Integer.parseInt(PropertyHelper.getProperty("noOfMessages", USER_MESSAGES));
    	
    	MessageDao dao = new MessageDao(contactPointsStr.split(","));
    	
    	this.secondsToAdd = 365 * 24 * 60 * 60 /noOfMessages; 
    			
    	logger.info ("Starting to process "+noOfMessages+" visits.");
    	
    	for (int i = 0; i < noOfMessages; i++){
    		
    		dao.insertMessage(createRandomMessage(noOfUsers));
    		
    		if ((i+1)%10000 ==0){
    			logger.info("Processed " + (i+1) + " messages.");
    			
    			sleep(100);
    		}
    	}
    	
    	System.exit(0);
	}

	private void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private Message createRandomMessage(int noOfUsers) {

		Message msg = new Message();
		
		int senderNo = (new Double(Math.random() * noOfUsers).intValue() + 1);
		String sender = "U" + senderNo;
		int sendeeNo = senderNo +  (new Double(Math.random() * 20).intValue() + 1);
		
		String sendee = "U" + sendeeNo;
		
		DateTime messageTime = dateTime.plus(secondsToAdd);
		
		dateTime= messageTime;
		
		msg.setSender(sender);
		msg.setSendee(sendee);
		msg.setDate(messageTime.toDate());
		msg.setMsg(createRandomString());

		return msg;
	}

	private String createRandomString() {
		String alphabet = 
		        new String("         0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); 
		int n = alphabet.length() + new Double(Math.random() * 20).intValue(); 

		String result = new String(); 
		Random r = new Random(); //11

		for (int i=0; i< n; i++) //12
		    result = result + alphabet.charAt((r.nextInt(n)%alphabet.length())); //13

		if (Math.random() < .2){
			String app = apps.get(new Double(Math.random() * apps.size()).intValue());
			result = result + app + " blah";
		}
		

		return result;
	}
	
	private List<String> apps = Arrays.asList("mobilefx", "mobilefx", "mobilefx", "desktop ui", "tablet research",
			"tablet research", "iphone banking", "android banking", "mobile trading");
}
