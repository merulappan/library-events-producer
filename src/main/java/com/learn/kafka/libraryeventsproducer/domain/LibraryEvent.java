package com.learn.kafka.libraryeventsproducer.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LibraryEvent {
	
	private Integer libraryEventId;
	@NotNull
	@Valid
	private Book book;
	
	private LibraryEventType libraryEventType;

}
