CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL
);

CREATE TABLE pets (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL,
    name VARCHAR(80) NOT NULL,
    age INTEGER NOT NULL,
    breed VARCHAR(80) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    size VARCHAR(10) NOT NULL,
    temperament VARCHAR(60),
    description VARCHAR(1000),
    image_url VARCHAR(500),
    status VARCHAR(12) NOT NULL,
    pet_type VARCHAR(10) NOT NULL,
    location VARCHAR(80) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    adoption_fee NUMERIC(10,2)
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    appointment_time TIMESTAMP NOT NULL,
    status VARCHAR(12) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_booking_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_booking_pet
        FOREIGN KEY (pet_id)
        REFERENCES pets(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_booking_pet_time
        UNIQUE (pet_id, appointment_time)
);

CREATE INDEX idx_booking_user ON bookings(user_id);
CREATE INDEX idx_booking_pet ON bookings(pet_id);

CREATE TABLE user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    preferred_pet_type VARCHAR(20),
    preferred_gender VARCHAR(10),
    preferred_size VARCHAR(10),
    min_age INTEGER,
    max_age INTEGER,
    max_adoption_fee NUMERIC(10,2),
    preferred_location VARCHAR(80),
    preferred_latitude DOUBLE PRECISION,
    preferred_longitude DOUBLE PRECISION,
    search_radius_km DOUBLE PRECISION,
    preferred_temperament VARCHAR(60),

    CONSTRAINT fk_user_preferences_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    added_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_favorite_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_favorite_pet
        FOREIGN KEY (pet_id)
        REFERENCES pets(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_favorite_user_pet
        UNIQUE (user_id, pet_id)
);

CREATE INDEX idx_fav_user ON favorites(user_id);
CREATE INDEX idx_fav_pet ON favorites(pet_id);

CREATE TABLE booking_event_logs (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    event_type VARCHAR(40) NOT NULL,
    event_created_at TIMESTAMP NOT NULL,
    processed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_event_log_booking
    ON booking_event_logs(booking_id);

CREATE INDEX idx_event_log_type
    ON booking_event_logs(event_type);
