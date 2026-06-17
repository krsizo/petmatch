package com.petmatch.petmatchapi.repository;

import com.petmatch.petmatchapi.model.event.BookingEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingEventLogRepository extends JpaRepository<BookingEventLog, Long> {

	List<BookingEventLog> findByBookingIdOrderByProcessedAtDesc(Long bookingId);
}
