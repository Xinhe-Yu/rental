-- Create the database
CREATE DATABASE IF NOT EXISTS chatop;

-- Use the new database
USE chatop;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS `MESSAGES`;
DROP TABLE IF EXISTS `RENTALS`;
DROP TABLE IF EXISTS `USERS`;

-- SQL Script provided by front-end
CREATE TABLE `USERS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255),
  `name` varchar(255),
  `password` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `RENTALS` (
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

CREATE TABLE `MESSAGES` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `rental_id` integer,
  `user_id` integer,
  `message` varchar(2000),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);

-- Create necessary indexes for foreign keys
CREATE INDEX `RENTALS_owner_index` ON `RENTALS` (`owner_id`);
CREATE INDEX `MESSAGES_user_index` ON `MESSAGES` (`user_id`);
CREATE INDEX `MESSAGES_rental_index` ON `MESSAGES` (`rental_id`);

-- Add foreign key constraints
ALTER TABLE `RENTALS` ADD FOREIGN KEY (`owner_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `MESSAGES` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `MESSAGES` ADD FOREIGN KEY (`rental_id`) REFERENCES `RENTALS` (`id`);