package com.learn.kafka.libraryeventsproducer.controller;

import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.libraryeventsproducer.domain.Book;
import com.learn.kafka.libraryeventsproducer.domain.LibraryEvent;
import com.learn.kafka.libraryeventsproducer.producer.LibraryEventsProducer;

@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
class LibraryEventsControllerUnitTest {
	
	@Autowired
	MockMvc mockMvc;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@MockBean
	LibraryEventsProducer libraryEventProducer;
	
	@Test
	void testPostLibraryEvent() throws Exception{
		
		//given
		
		Book book = Book.builder()
						.bookId(null)
						.bookAuthor(null)
						.bookName("Kafka using spring boot")
						.build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder()
												.libraryEventId(null)
												.book(book)
												.build();
		
		String json = objectMapper.writeValueAsString(libraryEvent);
		//doNothing().when(libraryEventProducer).sendLibraryEvent((LibraryEvent) isA(LibraryEvent.class));
		doNothing().when(libraryEventProducer).sendLibraryEvent(libraryEvent);
		mockMvc.perform(post("/v1/libraryevent")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isCreated());
			   
		
	}
	
	@Test
	void testPutLibraryEvent() throws Exception {

		// given

		Book book = Book.builder().bookId(123).bookAuthor("Kemk").bookName("Kafka using spring boot").build();

		LibraryEvent libraryEvent = LibraryEvent.builder().libraryEventId(123).book(book).build();

		String json = objectMapper.writeValueAsString(libraryEvent);
		// doNothing().when(libraryEventProducer).sendLibraryEvent((LibraryEvent)
		// isA(LibraryEvent.class));
		doNothing().when(libraryEventProducer).sendLibraryEvent(libraryEvent);
		mockMvc.perform(put("/v1/libraryevent").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		LibraryEvent libraryEvent1 = LibraryEvent.builder().libraryEventId(null).book(book).build();

		String json1 = objectMapper.writeValueAsString(libraryEvent1);
//doNothing().when(libraryEventProducer).sendLibraryEvent((LibraryEvent) isA(LibraryEvent.class));
		doNothing().when(libraryEventProducer).sendLibraryEvent(libraryEvent1);
		mockMvc.perform(put("/v1/libraryevent").content(json1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

}
