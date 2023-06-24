package com.learn.kafka.libraryeventsproducer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learn.kafka.libraryeventsproducer.domain.LibraryEvent;
import com.learn.kafka.libraryeventsproducer.domain.LibraryEventType;
import com.learn.kafka.libraryeventsproducer.producer.LibraryEventsProducer;

import jakarta.validation.Valid;

@RestController
public class LibraryEventsController {
	
	@Autowired
	LibraryEventsProducer libraryEventsProducer;
	
	@PostMapping("/v1/libraryevent")
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
		libraryEvent.setLibraryEventType(LibraryEventType.NEW);
		libraryEventsProducer.sendLibraryEvent(libraryEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
	
	@PutMapping("/v1/libraryevent")
	public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
		if(libraryEvent.getLibraryEventId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(libraryEvent);
		}
		libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
		libraryEventsProducer.sendLibraryEvent(libraryEvent);
		return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
	}

}
