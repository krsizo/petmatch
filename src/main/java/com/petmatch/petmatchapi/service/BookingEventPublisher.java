package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.config.RabbitConfig;
import com.petmatch.petmatchapi.model.event.BookingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookingEventPublisher {

	private final RabbitTemplate rabbitTemplate;

	public BookingEventPublisher(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void publish(BookingEvent event) {

		rabbitTemplate.convertAndSend(
			RabbitConfig.BOOKING_EXCHANGE,
			event.getEventType().getRoutingKey(),
			event
		);

		log.info("Published event: type={}, routingKey={}, bookingId={}",
			event.getEventType(),
			event.getEventType().getRoutingKey(),
			event.getBookingId());
	}
}
