package com.petmatch.petmatchapi.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitConfig {

	public static final String BOOKING_EXCHANGE = "booking.exchange";
	public static final String BOOKING_QUEUE = "booking.queue";
	public static final String BOOKING_ROUTING_PATTERN  = "booking.*";
	public static final String PET_ROUTING_PATTERN  = "pet.*";


	@Bean
	public TopicExchange bookingExchange() {
		return new TopicExchange(BOOKING_EXCHANGE);
	}

	@Bean
	public Queue bookingQueue() {
		return QueueBuilder
			.durable(BOOKING_QUEUE)
			.build();
	}

	@Bean
	public Binding bookingBinding() {
		return BindingBuilder
			.bind(bookingQueue())
			.to(bookingExchange())
			.with(BOOKING_ROUTING_PATTERN);
	}

	@Bean
	public Binding petBinding() {
		return BindingBuilder
			.bind(bookingQueue())
			.to(bookingExchange())
			.with(PET_ROUTING_PATTERN);
	}

	@Bean
	public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(
		ConnectionFactory connectionFactory,
		MessageConverter jsonMessageConverter
	) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter);
		return rabbitTemplate;
	}
}
