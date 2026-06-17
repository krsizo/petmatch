package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.model.event.BookingEvent;
import com.petmatch.petmatchapi.model.event.BookingEventLog;
import com.petmatch.petmatchapi.repository.BookingEventLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingEventLogService {

	private final BookingEventLogRepository bookingEventLogRepository;

	public BookingEventLogService(BookingEventLogRepository bookingEventLogRepository) {
		this.bookingEventLogRepository = bookingEventLogRepository;
	}

	@Transactional
	public void saveEvent(BookingEvent event) {
		BookingEventLog eventLog = BookingEventLog.builder()
			.bookingId(event.getBookingId())
			.petId(event.getPetId())
			.userId(event.getUserId())
			.eventType(event.getEventType())
			.eventCreatedAt(event.getCreatedAt())
			.build();

		bookingEventLogRepository.save(eventLog);
	}

	@Transactional(readOnly = true)
	public List<BookingEventLog> getEventsForBooking(Long bookingId) {
		return bookingEventLogRepository.findByBookingIdOrderByProcessedAtDesc(bookingId);
	}
}
