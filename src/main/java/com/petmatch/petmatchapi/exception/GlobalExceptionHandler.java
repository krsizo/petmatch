package com.petmatch.petmatchapi.exception;

import com.petmatch.petmatchapi.dto.error.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<ApiErrorResponse> build(
		HttpStatus status,
		String message,
		HttpServletRequest req,
		Object details
	) {
		ApiErrorResponse body = ApiErrorResponse.builder()
			.timestamp(LocalDateTime.now())
			.status(status.value())
			.error(status.getReasonPhrase())
			.message(message)
			.path(req.getRequestURI())
			.details(details)
			.build();

		return ResponseEntity.status(status).body(body);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationErrors(
		MethodArgumentNotValidException ex,
		HttpServletRequest req
	) {
		Map<String, String> fieldErrors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err ->
			fieldErrors.put(err.getField(), err.getDefaultMessage())
		);

		return build(
			HttpStatus.BAD_REQUEST,
			"Request validation failed",
			req,
			fieldErrors
		);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleNotFound(
		ResourceNotFoundException ex,
		HttpServletRequest req
	) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
	}

	@ExceptionHandler(BookingConflictException.class)
	public ResponseEntity<ApiErrorResponse> handleBookingConflict(
		BookingConflictException ex,
		HttpServletRequest req
	) {
		return build(HttpStatus.CONFLICT, ex.getMessage(), req, null);
	}

	@ExceptionHandler(PetUnavailableException.class)
	public ResponseEntity<ApiErrorResponse> handlePetUnavailable(
		PetUnavailableException ex,
		HttpServletRequest req
	) {
		return build(HttpStatus.CONFLICT, ex.getMessage(), req, null);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ApiErrorResponse> handleUserAlreadyExists(
		UserAlreadyExistsException ex,
		HttpServletRequest req
	) {
		return build(HttpStatus.CONFLICT, ex.getMessage(), req, null);
	}

	@ExceptionHandler({BadCredentialsException.class, InvalidCredentialsException.class})
	public ResponseEntity<ApiErrorResponse> handleBadCredentials(
		RuntimeException ex,
		HttpServletRequest req
	) {
		return build(HttpStatus.UNAUTHORIZED, "Invalid credentials", req, null);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiErrorResponse> handleAccessDenied(
		AccessDeniedException ex,
		HttpServletRequest req
	) {
		return build(HttpStatus.FORBIDDEN, "You do not have permission to perform this action", req, null);
	}

	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<ApiErrorResponse> handleOptimisticLock(
		ObjectOptimisticLockingFailureException ex,
		HttpServletRequest req
	) {
		return build(
			HttpStatus.CONFLICT,
			"Resource was updated by another request. Please retry.",
			req,
			null
		);
	}

	@ExceptionHandler(InvalidBookingStatusTransitionException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidBookingStatusTransition(
		InvalidBookingStatusTransitionException ex,
		HttpServletRequest req
	) {
		return build(HttpStatus.CONFLICT, ex.getMessage(), req, null);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleOtherErrors(
		Exception ex,
		HttpServletRequest req
	) {
		return build(
			HttpStatus.INTERNAL_SERVER_ERROR,
			"Unexpected error",
			req,
			null
		);
	}
}
