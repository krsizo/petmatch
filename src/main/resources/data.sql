ALTER TABLE pets
ALTER COLUMN version SET DEFAULT 0;

UPDATE pets
SET version = 0
WHERE version IS NULL;

ALTER TABLE pets
ALTER COLUMN version SET NOT NULL;


INSERT INTO pets
(name, age, pet_type, breed, gender, size, temperament, description, image_url, status, adoption_fee, location)
VALUES
('Buddy', 3, 'DOG', 'Labrador Retriever', 'MALE', 'MEDIUM', 'Friendly',
'Very loyal and playful dog',
'/images/YellowLabradorLooking_new.jpg',
'AVAILABLE', 50, 'Tallinn'),
('Mittens', 2, 'CAT', 'Siamese', 'FEMALE', 'SMALL', 'Calm',
'Loves to cuddle and nap',
'/images/Siam_lilacpoint.jpg',
'AVAILABLE', 80, 'Helsinki'),
('Charlie', 5, 'DOG', 'Beagle', 'MALE', 'SMALL', 'Curious',
'Loves to explore and sniff around',
'/images/Beagle_600.jpg',
'ADOPTED', 30, 'Stockholm'),
('Luna', 1, 'CAT', 'British Shorthair', 'FEMALE', 'MEDIUM', 'Gentle',
'Very calm and affectionate temperament',
'/images/British_shorthair_blue_Irina_AEA.jpg',
'AVAILABLE', 60, 'Riga'),
('Coco', 4, 'OTHER', 'Parrot', 'FEMALE', 'SMALL', 'Talkative',
'Colorful, social, and playful',
'/images/Parrot_4.jpg',
'AVAILABLE', 40, 'Vilnius');

-- INSERT INTO users (name, email, password, role)
-- VALUES
-- ('Admin User', 'admin@petmatch.com', 'pass', 'ADMIN'),
-- ('Jane Doe', 'jane@petmatch.com', '$2a$10$MUHA767y1hSF4EScW.tUdOfkRT1aT3BFdUC/.GtOaZ07XSlkrDqwm', 'USER');


-- UPDATE users set role='ADMIN' where name='Admin'
-- INSERT INTO favorites (user_id, pet_id, added_at)
-- VALUES
-- (2, 1, '2025-10-06T12:00:00'),
-- (2, 2, '2025-10-06T12:00:00');
--
--
-- INSERT INTO bookings (user_id, pet_id, appointment_time, status)
-- VALUES
-- (2, 1, '2025-10-10T14:00:00', 'PENDING');

