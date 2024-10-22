DROP DATABASE IF EXISTS chatop;
CREATE DATABASE IF NOT EXISTS chatop;
USE chatop;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS `messages`;
DROP TABLE IF EXISTS `rentals`;
DROP TABLE IF EXISTS `users`;

-- content in script.sql provided by front-end
CREATE TABLE `users` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

INSERT INTO users (email, name, password, role) VALUES ('user@test.fr', 'user', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.', 'USER'),
('admin@test.fr', 'admin', '$2y$10$kp1V7UYDEWn17WSK16UcmOnFd1mPFVF6UkLrOOCGtf24HOYt8p1iC', 'ADMIN');

CREATE TABLE `rentals` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `surface` numeric,
  `price` numeric,
  `picture` varchar(255),
  `description` varchar(2000),
  `owner_id` integer NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `messages` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `rental_id` integer,
  `user_id` integer,
  `message` varchar(2000),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE UNIQUE INDEX `users_index` ON `users` (`email`);

-- Create necessary indexes for foreign keys
CREATE INDEX `rentals_owner_index` ON `rentals` (`owner_id`);
CREATE INDEX `messages_user_index` ON `messages` (`user_id`);
CREATE INDEX `messages_rental_index` ON `messages` (`rental_id`);

-- Add foreign key constraints
ALTER TABLE `rentals` ADD FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);
ALTER TABLE `messages` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `messages` ADD FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`id`);