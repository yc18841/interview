package com.interview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserCalendarService {
	private static final String url = "http://demo8056139.mockable.io/calendars/";
	
	private RestTemplate restTemplate;
	
	@Autowired
	UserCalendarService(RestTemplateBuilder builder) {
		restTemplate = builder.build();
	}
	
	public UserCalendarResponse retrieveUserCalendar(int userId) {
		return restTemplate.getForObject(url + userId, UserCalendarResponse.class);
	}

}
