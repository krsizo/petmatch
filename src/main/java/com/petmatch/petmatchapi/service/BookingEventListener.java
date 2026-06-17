package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.config.RabbitConfig;
import com.petmatch.petmatchapi.model.event.BookingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookingEventListener {

	private final BookingEventLogService bookingEventLogService;

	public BookingEventListener(BookingEventLogService bookingEventLogService) {
		this.bookingEventLogService = bookingEventLogService;
	}

	@RabbitListener(queues = RabbitConfig.BOOKING_QUEUE)
	public void handleBookingEvent(BookingEvent event) {
		bookingEventLogService.saveEvent(event);

		log.info("Received event: type={}, bookingId={}, petId={}, userId={}, createdAt={}",
			event.getEventType(),
			event.getBookingId(),
			event.getPetId(),
			event.getUserId(),
			event.getCreatedAt());
	}
}
