package com.interview.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserOpenSlotController {
	@Autowired
	private UserCalendarService userCalendarService;

	@PostMapping("/opens")
	public UserOpenSlotResponse getOpenSlot(@RequestBody UserOpenSlotRequest request) {
		ArrayList<Integer> idList = request.getUserId();

		ArrayList<UserCalendarResponse> responesList = new ArrayList<>();

		for (int userId : idList) {
			// call service
			responesList.add(userCalendarService.retrieveUserCalendar(userId));
		}

		// process response
		return processUserCalendarResponse(responesList);
	}

	private UserOpenSlotResponse processUserCalendarResponse(ArrayList<UserCalendarResponse> responesList) {
		UserOpenSlotResponse response = new UserOpenSlotResponse();
		
		Slot openSlot = new Slot(); 
		ArrayList<String> startEnd = new ArrayList<>();
		startEnd.add("8:00"); 
		startEnd.add("17:00"); 
		openSlot.setStartEnd(startEnd);
		  
		ArrayList<Slot> openSlotList = new ArrayList<>(); 
		openSlotList.add(openSlot);
		  
		// generate open slot 
		for (UserCalendarResponse responseItem : responesList) { 
			List<List<String>> slotList = responseItem.getData(); 
			
			// for both start and end are inside a open slot
			for (List<String> slot : slotList ) {
				List<String> startEndArrayList = slot;
				
				Iterator<Slot> it = openSlotList.iterator(); 
				while (it.hasNext()) { 
					Slot s = it.next(); 
					if (compareTimeString(startEndArrayList.get(0), s.getStartEnd().get(0)) >= 0 
							&& compareTimeString(startEndArrayList.get(0), s.getStartEnd().get(1)) <= 0 
							&& compareTimeString(startEndArrayList.get(1), s.getStartEnd().get(1)) <= 0) { 
						
						// break the exist open slot 
						String end = s.getStartEnd().get(1);
						s.getStartEnd().set(1, startEndArrayList.get(0));
		  
						// create a new open slot 
						Slot openSlot1 = new Slot(); 
						ArrayList<String> startEndList = new ArrayList<>();
						startEndList.add(startEndArrayList.get(1)); 
						startEndList.add(end);
						openSlot1.setStartEnd(startEndList); 
						openSlotList.add(openSlot1);
						break; 
					} 
				}
			}
			
			// for start is inside a open slot
			for (List<String> slot : slotList ) {
				List<String> startEndArrayList = slot;
				
				Iterator<Slot>it = openSlotList.iterator(); 
				while (it.hasNext()) { 
					Slot s = it.next(); 
					if (compareTimeString(startEndArrayList.get(0), s.getStartEnd().get(0)) >= 0 
							&& compareTimeString(startEndArrayList.get(0), s.getStartEnd().get(1)) <= 0) {
						s.getStartEnd().set(1, startEndArrayList.get(0)); 
						break; 
					} 
				}
			}
			
			// for end is inside a open slot
			for (List<String> slot : slotList ) {
				List<String> startEndArrayList = slot;
				
				Iterator<Slot>it = openSlotList.iterator(); 
				while (it.hasNext()) { 
					Slot s = it.next(); 
					if (compareTimeString(startEndArrayList.get(1), s.getStartEnd().get(0)) >= 0 
							&& compareTimeString(startEndArrayList.get(1), s.getStartEnd().get(1)) <= 0) {
						s.getStartEnd().set(0, startEndArrayList.get(1)); 
						break; 
					} 
				} 
			}
		}
		  
		response.setOpens(openSlotList);
		 
		return response;
	}

	private int compareTimeString(String string, String string2) {
		String[] stringArray = string.split(":");
		String[] stringArray2 = string2.split(":");
		
		if (Integer.parseInt(stringArray[0]) == Integer.parseInt(stringArray2[0])) {
			if (Integer.parseInt(stringArray[1]) >= Integer.parseInt(stringArray2[1]))
				return 1;
			else
				return -1;
		} else {
			if (Integer.parseInt(stringArray[0]) >= Integer.parseInt(stringArray2[0]))
				return 1;
			else
				return -1;
		}
	}

}
