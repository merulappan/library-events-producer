package com.learn.kafka.libraryeventsproducer.producer;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.libraryeventsproducer.domain.Book;
import com.learn.kafka.libraryeventsproducer.domain.LibraryEvent;

@ExtendWith(MockitoExtension.class)
class LibraryEventsProducerUnitTest {

	@Mock
	KafkaTemplate<Integer, String> kafkaTemplate;

	@Spy
	ObjectMapper objectMapper;

	@InjectMocks
	LibraryEventsProducer libraryEventProducer;

	@Test
	void testSendLibraryEvent() throws Exception {

		Book book = Book.builder().bookId(null).bookAuthor(null).bookName("Kafka using spring boot").build();

		LibraryEvent libraryEvent = LibraryEvent.builder().libraryEventId(null).book(book).build();
		
		
		libraryEventProducer.sendLibraryEvent(libraryEvent);

	}

}
