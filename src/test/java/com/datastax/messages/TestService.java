package com.datastax.messages;

import org.junit.Test;

import com.datastax.messages.service.MessageService;

public class TestService {

	@Test
	public void testServiceAnalysis(){
		
		
		MessageService service = MessageService.getInstance();
		
		service.getForwardAnalysis("A2", "A40");
		
	}
}
