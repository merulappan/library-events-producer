package com.learn.kafka.libraryeventsproducer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.learn.kafka.libraryeventsproducer.domain.Book;
import com.learn.kafka.libraryeventsproducer.domain.LibraryEvent;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"},partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.admin.properties.bootstrap-servers=${spring.embedded.kafka.brokers}"})
class LibraryEventsControllerIT {
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;
	
	private Consumer<Integer,String> consumer;
	
	@BeforeEach
	void setUp() {
		Map<String,Object> config = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
		consumer = new DefaultKafkaConsumerFactory<>(config,new IntegerDeserializer(),new StringDeserializer()).createConsumer();
		embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
	}
	
	@AfterEach
	void tearDown() {
		consumer.close();
	}
	
	@Test
	void testPostLibraryEvent() {
		
		//given
		
		Book book = Book.builder()
						.bookId(123)
						.bookAuthor("KemK")
						.bookName("Kafka using spring boot")
						.build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder()
												.libraryEventId(null)
												.book(book)
												.build();
		
		
		//when
		
		HttpHeaders header = new HttpHeaders();
		header.set("content-type", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent,header);
		
		ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange("/v1/libraryevent", HttpMethod.POST,request,LibraryEvent.class);
		
		//then
		assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
		
		ConsumerRecord<Integer,String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "library-events");
		System.out.println(consumerRecord.value());
	}
	
	@Test
	@Timeout(5)
	void testPutLibraryEvent() {
		
		//given
		
		Book book = Book.builder()
						.bookId(123)
						.bookAuthor("KemK")
						.bookName("Kafka using spring boot")
						.build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder()
												.libraryEventId(345)
												.book(book)
												.build();
		
		
		//when
		
		HttpHeaders header = new HttpHeaders();
		header.set("content-type", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent,header);
		
		ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange("/v1/libraryevent", HttpMethod.PUT,request,LibraryEvent.class);
		
		//then
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
		
		ConsumerRecord<Integer,String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "library-events");
		System.out.println(consumerRecord.value());
	}

	
	
}
