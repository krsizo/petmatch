package com.petmatch.petmatchapi.model.event;

public enum BookingEventType {

	BOOKING_CREATED("booking.created"),
	BOOKING_CONFIRMED("booking.confirmed"),
	BOOKING_CANCELED("booking.canceled"),
	PET_ADOPTED("pet.adopted");

	private final String routingKey;

	BookingEventType(String routingKey) {
		this.routingKey = routingKey;
	}

	public String getRoutingKey() {
		return routingKey;
	}
}
