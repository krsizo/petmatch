package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.dto.booking.BookingRequest;
import com.petmatch.petmatchapi.exception.BookingConflictException;
import com.petmatch.petmatchapi.exception.PetUnavailableException;
import com.petmatch.petmatchapi.exception.ResourceNotFoundException;
import com.petmatch.petmatchapi.model.*;
import com.petmatch.petmatchapi.model.event.BookingEvent;
import com.petmatch.petmatchapi.model.event.BookingEventType;
import com.petmatch.petmatchapi.repository.BookingRepository;
import com.petmatch.petmatchapi.repository.PetRepository;
import com.petmatch.petmatchapi.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService
{

	private final BookingRepository bookingRepository;
	private final PetRepository petRepository;
	private final UserService userService;
	private final BookingStatusTransitionService transitionService;
	private final BookingEventPublisher bookingEventPublisher;

	public BookingService(BookingRepository bookingRepository, PetRepository petRepository,
		UserService userService, BookingStatusTransitionService transitionService,
		BookingEventPublisher bookingEventPublisher)
	{
		this.bookingRepository = bookingRepository;
		this.petRepository = petRepository;
		this.userService = userService;
		this.transitionService = transitionService;
		this.bookingEventPublisher = bookingEventPublisher;
	}

	@Transactional
	public Booking createBooking(BookingRequest request)
	{
		User user = userService.getCurrentUser();
		Pet pet = petRepository.findById(request.getPetId())
			.orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

		validatePetCanBeBooked(pet);

		bookingRepository.findByPetAndAppointmentTimeAndStatusNot(pet, request.getAppointmentTime(),
			BookingStatus.CANCELED).ifPresent(b -> {
			throw new BookingConflictException("This time slot is already taken");
		});

		Booking booking = Booking.builder()
			.user(user)
			.pet(pet)
			.appointmentTime(request.getAppointmentTime())
			.status(BookingStatus.PENDING)
			.build();

		try
		{
			Booking savedBooking = bookingRepository.save(booking);
			publishBookingEvent(savedBooking, BookingEventType.BOOKING_CREATED);
			return savedBooking;
		}
		catch(DataIntegrityViolationException e)
		{
			throw new BookingConflictException("This time slot is already taken");
		}
	}

	private void validatePetCanBeBooked(Pet pet)
	{
		if(pet.getStatus() == PetStatus.ADOPTED)
		{
			throw new PetUnavailableException("Pet already adopted");
		}

		if(pet.getStatus() == PetStatus.RESERVED)
		{
			throw new PetUnavailableException("Pet is already reserved");
		}
	}

	@Transactional(readOnly = true)
	public List<Booking> getUserBookings()
	{
		User user = userService.getCurrentUser();
		return bookingRepository.findByUser(user);
	}

	@Transactional(readOnly = true)
	public List<Booking> getAllBookings()
	{
		return bookingRepository.findAll();
	}

	@Transactional
	public Booking confirmBooking(Long bookingId)
	{
		Booking booking = getBooking(bookingId);

		transitionService.validateTransition(booking.getStatus(), BookingStatus.CONFIRMED);

		Pet pet = booking.getPet();
		validatePetCanBeBooked(pet);

		try
		{
			booking.setStatus(BookingStatus.CONFIRMED);
			pet.setStatus(PetStatus.RESERVED);
			petRepository.save(pet);
			Booking savedBooking = bookingRepository.save(booking);
			publishBookingEvent(savedBooking, BookingEventType.BOOKING_CONFIRMED);
			return savedBooking;
		}
		catch(ObjectOptimisticLockingFailureException e)
		{
			throw new BookingConflictException("Pet was just reserved by another user");
		}

	}

	@Transactional
	public Booking cancelBooking(Long bookingId)
	{
		User currentUser = userService.getCurrentUser();
		Booking booking = getBooking(bookingId);

		validateCanManageBooking(currentUser, booking);

		if(booking.getStatus() == BookingStatus.CANCELED)
		{
			return booking;
		}

		transitionService.validateTransition(booking.getStatus(), BookingStatus.CANCELED);

		Pet pet = booking.getPet();
		booking.setStatus(BookingStatus.CANCELED);

		if(pet.getStatus() == PetStatus.RESERVED)
		{
			pet.setStatus(PetStatus.AVAILABLE);
			petRepository.save(pet);
		}

		Booking savedBooking = bookingRepository.save(booking);
		publishBookingEvent(savedBooking, BookingEventType.BOOKING_CANCELED);
		return savedBooking;
	}

	@Transactional
	public Booking markAdopted(Long bookingId)
	{
		Booking booking = getBooking(bookingId);

		if(booking.getStatus() != BookingStatus.CONFIRMED)
		{
			throw new BookingConflictException("Only CONFIRMED booking can be adopted");
		}

		Pet pet = booking.getPet();

		if(pet.getStatus() == PetStatus.ADOPTED)
		{
			throw new PetUnavailableException("Pet already adopted");
		}

		if(pet.getStatus() != PetStatus.RESERVED)
		{
			throw new BookingConflictException("Only RESERVED pet can be adopted");
		}

		bookingRepository.cancelOtherActiveBookings(pet, booking.getId(),
			List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED));

		try
		{
			pet.setStatus(PetStatus.ADOPTED);
			petRepository.save(pet);
			Booking savedBooking = bookingRepository.save(booking);
			publishBookingEvent(savedBooking, BookingEventType.PET_ADOPTED);
			return savedBooking;
		}
		catch(ObjectOptimisticLockingFailureException e)
		{
			throw new BookingConflictException("Pet was just adopted by another user");
		}
	}

	private Booking getBooking(Long bookingId)
	{
		return bookingRepository.findById(bookingId)
			.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
	}

	private void validateCanManageBooking(User user, Booking booking)
	{
		boolean isAdmin = user.getRole() == Role.ADMIN;
		boolean isOwner = booking.getUser().getId().equals(user.getId());

		if(!isAdmin && !isOwner)
		{
			throw new AccessDeniedException("You are not allowed to perform this action");
		}
	}

	private void publishBookingEvent(Booking booking, BookingEventType eventType) {
		BookingEvent event = BookingEvent.builder()
			.bookingId(booking.getId())
			.petId(booking.getPet().getId())
			.userId(booking.getUser().getId())
			.eventType(eventType)
			.createdAt(LocalDateTime.now())
			.build();

		bookingEventPublisher.publish(event);
	}
}
