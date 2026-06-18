package com.petmatch.petmatchapi.integration;

import com.petmatch.petmatchapi.dto.booking.BookingRequest;
import com.petmatch.petmatchapi.exception.BookingConflictException;
import com.petmatch.petmatchapi.exception.InvalidBookingStatusTransitionException;
import com.petmatch.petmatchapi.model.*;
import com.petmatch.petmatchapi.model.event.BookingEventType;
import com.petmatch.petmatchapi.repository.BookingRepository;
import com.petmatch.petmatchapi.repository.PetRepository;
import com.petmatch.petmatchapi.repository.UserRepository;
import com.petmatch.petmatchapi.service.BookingEventPublisher;
import com.petmatch.petmatchapi.service.BookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

class BookingServiceIntegrationTest extends IntegrationTestBase {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private UserRepository userRepository;

	@MockitoBean
	private BookingEventPublisher bookingEventPublisher;

	private User user;
	private Pet pet;

	@BeforeEach
	void setUp() {
		bookingRepository.deleteAll();
		petRepository.deleteAll();
		userRepository.deleteAll();
		SecurityContextHolder.clearContext();

		user = userRepository.save(User.builder()
			.name("Jane Doe")
			.email("jane@petmatch.com")
			.password("encoded-password")
			.role(Role.USER)
			.build());

		pet = petRepository.save(Pet.builder()
			.name("Buddy")
			.age(3)
			.breed("Labrador Retriever")
			.gender(Gender.MALE)
			.size(Size.MEDIUM)
			.temperament("Friendly")
			.description("Very loyal and playful dog")
			.imageUrl("/images/buddy.jpg")
			.status(PetStatus.AVAILABLE)
			.petType(PetType.DOG)
			.location("Tallinn")
			.adoptionFee(BigDecimal.valueOf(50))
			.build());

		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(user.getEmail(), null, List.of())
		);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void shouldCreateBooking() {
		BookingRequest request = new BookingRequest();
		request.setPetId(pet.getId());
		request.setAppointmentTime(LocalDateTime.now().plusDays(1));

		Booking booking = bookingService.createBooking(request);

		assertThat(booking.getId()).isNotNull();
		assertThat(booking.getStatus()).isEqualTo(BookingStatus.PENDING);
		assertThat(booking.getPet().getId()).isEqualTo(pet.getId());
		assertThat(booking.getUser().getId()).isEqualTo(user.getId());

		assertThat(bookingRepository.findById(booking.getId())).isPresent();

		verify(bookingEventPublisher).publish(argThat(event ->
			event.getEventType() == BookingEventType.BOOKING_CREATED
				&& event.getBookingId().equals(booking.getId())
				&& event.getPetId().equals(pet.getId())
				&& event.getUserId().equals(user.getId())
		));
	}

	@Test
	void shouldNotCreateSecondBookingForSamePetAndSameTime() {
		LocalDateTime appointmentTime = LocalDateTime.now().plusDays(1);

		BookingRequest firstRequest = new BookingRequest();
		firstRequest.setPetId(pet.getId());
		firstRequest.setAppointmentTime(appointmentTime);

		bookingService.createBooking(firstRequest);

		BookingRequest secondRequest = new BookingRequest();
		secondRequest.setPetId(pet.getId());
		secondRequest.setAppointmentTime(appointmentTime);

		assertThatThrownBy(() -> bookingService.createBooking(secondRequest))
			.isInstanceOf(BookingConflictException.class)
			.hasMessage("This time slot is already taken");
	}

	@Test
	void shouldConfirmBookingAndReservePet() {
		BookingRequest request = new BookingRequest();
		request.setPetId(pet.getId());
		request.setAppointmentTime(LocalDateTime.now().plusDays(1));

		Booking createdBooking = bookingService.createBooking(request);

		Booking confirmedBooking = bookingService.confirmBooking(createdBooking.getId());

		assertThat(confirmedBooking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);

		Pet updatedPet = petRepository.findById(pet.getId()).orElseThrow();
		assertThat(updatedPet.getStatus()).isEqualTo(PetStatus.RESERVED);

		verify(bookingEventPublisher).publish(argThat(event ->
			event.getEventType() == BookingEventType.BOOKING_CONFIRMED
				&& event.getBookingId().equals(confirmedBooking.getId())
				&& event.getPetId().equals(pet.getId())
				&& event.getUserId().equals(user.getId())
		));
	}

	@Test
	void shouldCancelConfirmedBookingAndMakePetAvailableAgain() {
		BookingRequest request = new BookingRequest();
		request.setPetId(pet.getId());
		request.setAppointmentTime(LocalDateTime.now().plusDays(1));

		Booking createdBooking = bookingService.createBooking(request);
		Booking confirmedBooking = bookingService.confirmBooking(createdBooking.getId());

		Booking canceledBooking = bookingService.cancelBooking(confirmedBooking.getId());

		assertThat(canceledBooking.getStatus()).isEqualTo(BookingStatus.CANCELED);

		Pet updatedPet = petRepository.findById(pet.getId()).orElseThrow();
		assertThat(updatedPet.getStatus()).isEqualTo(PetStatus.AVAILABLE);

		verify(bookingEventPublisher).publish(argThat(event ->
			event.getEventType() == BookingEventType.BOOKING_CANCELED
				&& event.getBookingId().equals(canceledBooking.getId())
				&& event.getPetId().equals(pet.getId())
				&& event.getUserId().equals(user.getId())
		));
	}

	@Test
	void shouldMarkConfirmedBookingAsAdopted() {
		BookingRequest request = new BookingRequest();
		request.setPetId(pet.getId());
		request.setAppointmentTime(LocalDateTime.now().plusDays(1));

		Booking createdBooking = bookingService.createBooking(request);
		Booking confirmedBooking = bookingService.confirmBooking(createdBooking.getId());

		Booking adoptedBooking = bookingService.markAdopted(confirmedBooking.getId());

		assertThat(adoptedBooking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);

		Pet updatedPet = petRepository.findById(pet.getId()).orElseThrow();
		assertThat(updatedPet.getStatus()).isEqualTo(PetStatus.ADOPTED);

		verify(bookingEventPublisher).publish(argThat(event ->
			event.getEventType() == BookingEventType.PET_ADOPTED
				&& event.getBookingId().equals(adoptedBooking.getId())
				&& event.getPetId().equals(pet.getId())
				&& event.getUserId().equals(user.getId())
		));
	}

	@Test
	void shouldNotConfirmCanceledBooking() {
		BookingRequest request = new BookingRequest();
		request.setPetId(pet.getId());
		request.setAppointmentTime(LocalDateTime.now().plusDays(1));

		Booking createdBooking = bookingService.createBooking(request);
		Booking canceledBooking = bookingService.cancelBooking(createdBooking.getId());

		assertThatThrownBy(() -> bookingService.confirmBooking(canceledBooking.getId()))
			.isInstanceOf(InvalidBookingStatusTransitionException.class)
			.hasMessage("Canceled booking cannot be changed");
	}
}
