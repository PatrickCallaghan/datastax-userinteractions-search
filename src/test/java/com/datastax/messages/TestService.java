package com.datastax.messages;

import static org.junit.Assert.*;

import org.junit.Test;

import com.datastax.messages.service.MessageService;

public class TestService {

	@Test
	public void testServiceAnalysis(){
		
		
		MessageService service = MessageService.getInstance();
		
		service.getForwardAnalysis("A2", "A40");
		
	}
	
	@Test
	public void testPathSubString(){
		
		String pathSubString = this.getPathSubString("A1", "A5", "A3 A9 A1 A7 A6 A5 A13");
		
		assertEquals("A1 A7 A6 A5", pathSubString);
		
		pathSubString = this.getPathSubString("A1", "A5", "A3 A9 A1 A1 A1 A7 A6 A5 A13");
		assertEquals("A1 A7 A6 A5", pathSubString);
	}
	
	private String getPathSubString(String from, String to, String path) {
		String pathSubString = path.substring(0, path.lastIndexOf(to) - 1);

		pathSubString = pathSubString.substring(pathSubString.lastIndexOf(from));
		return pathSubString + " " + to;
	}
}

