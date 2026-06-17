package com.petmatch.petmatchapi.model;

public enum BookingStatus {
	PENDING,    // создано, ждёт подтверждения админа
	CONFIRMED,  // админ подтвердил встречу
	CANCELED    // отменено
}
