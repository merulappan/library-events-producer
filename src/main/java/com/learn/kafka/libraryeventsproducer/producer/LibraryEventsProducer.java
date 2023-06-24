package com.learn.kafka.libraryeventsproducer.producer;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.libraryeventsproducer.domain.LibraryEvent;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventsProducer {
	
	@Autowired
	KafkaTemplate<Integer,String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	public void sendLibraryEvent(@Valid LibraryEvent libraryEvent) throws JsonProcessingException {
		
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		CompletableFuture<SendResult<Integer,String>> completableFuture = kafkaTemplate.sendDefault(key, value);
		completableFuture.thenAccept((result) -> {
			log.info("Message Sent Successfull");
			log.info(result.getRecordMetadata().toString());
		});
	}

}
