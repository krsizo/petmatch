package com.petmatch.petmatchapi.controller;

import com.petmatch.petmatchapi.dto.booking.BookingRequest;
import com.petmatch.petmatchapi.dto.booking.BookingResponse;
import com.petmatch.petmatchapi.dto.event.BookingEventLogResponse;
import com.petmatch.petmatchapi.mapper.BookingEventLogMapper;
import com.petmatch.petmatchapi.mapper.BookingMapper;
import com.petmatch.petmatchapi.model.Booking;
import com.petmatch.petmatchapi.service.BookingEventLogService;
import com.petmatch.petmatchapi.service.BookingService;
import com.petmatch.petmatchapi.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController
{
	private final BookingService bookingService;
	private final BookingMapper bookingMapper;
	private final BookingEventLogService bookingEventLogService;
	private final BookingEventLogMapper bookingEventLogMapper;

	public BookingController(BookingService bookingService, BookingMapper bookingMapper,
		BookingEventLogService bookingEventLogService, BookingEventLogMapper bookingEventLogMapper)
	{
		this.bookingService = bookingService;
		this.bookingMapper = bookingMapper;
		this.bookingEventLogService = bookingEventLogService;
		this.bookingEventLogMapper = bookingEventLogMapper;
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request)
	{
		Booking booking = bookingService.createBooking(request);
		return ResponseEntity.ok(bookingMapper.toResponse(booking));
	}

	@GetMapping("/my")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<BookingResponse>> getUserBookings()
	{
		List<Booking> bookings = bookingService.getUserBookings();
		return ResponseEntity.ok(bookings.stream().map(bookingMapper::toResponse).toList());
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<BookingResponse>> getAllBookings()
	{
		List<Booking> bookings = bookingService.getAllBookings();
		return ResponseEntity.ok(bookings.stream().map(bookingMapper::toResponse).toList());
	}

	@PutMapping("/{bookingId}/confirm")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BookingResponse> confirmBooking(@PathVariable Long bookingId)
	{
		Booking booking = bookingService.confirmBooking(bookingId);
		return ResponseEntity.ok(bookingMapper.toResponse(booking));
	}

	@PutMapping("/{bookingId}/cancel")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long bookingId)
	{
		Booking booking = bookingService.cancelBooking(bookingId);
		return ResponseEntity.ok(bookingMapper.toResponse(booking));
	}

	@PutMapping("/{bookingId}/adopt")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BookingResponse> adopt(@PathVariable Long bookingId)
	{
		Booking booking = bookingService.markAdopted(bookingId);
		return ResponseEntity.ok(bookingMapper.toResponse(booking));
	}

	@GetMapping("/{bookingId}/events")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<BookingEventLogResponse>> getBookingEvents(@PathVariable Long bookingId) {
		List<BookingEventLogResponse> events = bookingEventLogService.getEventsForBooking(bookingId)
			.stream()
			.map(bookingEventLogMapper::toResponse)
			.toList();

		return ResponseEntity.ok(events);
	}
}
