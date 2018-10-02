package com.datastax.messages;

import static org.junit.Assert.*;

import org.junit.Test;

import com.datastax.messages.service.MessageService;

public class TestService {

	@Test
	public void testServiceAnalysis(){
		
		
		MessageService service = MessageService.getInstance();
		
		service.getForwardAnalysisWithinSession("F001", "F023");
		
	}
	
	@Test
	public void testPathSubString(){
		
		String pathSubString = this.getPathSubString("A001", "A005", "A003 A009 A001 A007 A006 A005 A013");
		
		assertEquals("A001 A007 A006 A005", pathSubString);
		
		pathSubString = this.getPathSubString("A001", "A005", "A003 A009 A001 A007 A006 A005 A013");
		assertEquals("A001 A007 A006 A005", pathSubString);
	}
	
	private String getPathSubString(String from, String to, String path) {
		String pathSubString = path.substring(0, path.lastIndexOf(to) - 1);

		pathSubString = pathSubString.substring(pathSubString.lastIndexOf(from));
		return pathSubString.trim() + " " + to;
	}
}

