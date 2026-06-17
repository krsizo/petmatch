package com.petmatch.petmatchapi.repository;
import com.petmatch.petmatchapi.model.Booking;
import com.petmatch.petmatchapi.model.BookingStatus;
import com.petmatch.petmatchapi.model.Pet;
import com.petmatch.petmatchapi.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	@EntityGraph(attributePaths = {"pet", "user"})
	List<Booking> findAll();
	@EntityGraph(attributePaths = {"pet", "user"})
	List<Booking> findByUser(User user);
	void deleteAllByPetId(Long petId);
	void deleteAllByUserId(Long userId);
	List<Booking> findByPet(Pet pet);
	// Проверка: есть ли у питомца бронирование на это время
	Optional<Booking> findByPetAndAppointmentTimeAndStatusNot(Pet pet, LocalDateTime appointmentTime, BookingStatus bookingStatus);
	boolean existsByPetAndStatusInAndIdNot(Pet pet, List<BookingStatus> statuses, Long id);
	@Modifying
	@Query("""
	update Booking b
	set b.status = com.petmatch.petmatchapi.model.BookingStatus.CANCELED
	where b.pet = :pet
	 and b.id <> :keepId
	 and b.status in :active
	""")
	int cancelOtherActiveBookings(@Param("pet") Pet pet,
		@Param("keepId") Long keepId,
		@Param("active") List<BookingStatus> active);
}
