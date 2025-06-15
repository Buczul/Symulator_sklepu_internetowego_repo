-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Cze 15, 2025 at 10:35 PM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ebookshopdb`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `audiobooks`
--

CREATE TABLE `audiobooks` (
  `product_id` varchar(36) NOT NULL,
  `duration_minutes` int(11) NOT NULL,
  `narrator` varchar(255) NOT NULL,
  `audio_format` enum('MP3','AAC','WAV','FLAC') NOT NULL,
  `studio_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `audiobooks`
--

INSERT INTO `audiobooks` (`product_id`, `duration_minutes`, `narrator`, `audio_format`, `studio_name`) VALUES
('91070e7d-f179-4855-a70d-755a52f62268', 3080, 'Jan Kowalski', 'MP3', 'AudioBk'),
('fa4e751f-27ea-4347-86bb-12775a804861', 837, 'Krzysztof Gosztyła', 'WAV', 'superNOWA');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(1, 'BIOGRAPHY'),
(6, 'CHILDREN'),
(10, 'CONTEMPORARY'),
(5, 'CRIME'),
(2, 'FANTASY'),
(4, 'HISTORICAL'),
(7, 'LANGUAGE_LEARNING'),
(11, 'LITERATURE'),
(12, 'OTHER'),
(9, 'POPULAR_SCIENCE'),
(3, 'SCIENCE_FICTION'),
(8, 'SCIENTIFIC');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ebooks`
--

CREATE TABLE `ebooks` (
  `product_id` varchar(36) NOT NULL,
  `file_format` enum('PDF','EPUB','MOBI','AZW') NOT NULL,
  `file_size_mb` decimal(5,2) NOT NULL,
  `download_link` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ebooks`
--

INSERT INTO `ebooks` (`product_id`, `file_format`, `file_size_mb`, `download_link`) VALUES
('e4152396-34bf-4275-a29d-55a0f4dbe443', 'MOBI', 4.96, 'www.bookhaven.pl/ebook/Kroniki_Diuny_tom_01');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `order_date` datetime NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` enum('NEW','SHIPPED','CANCELLED') DEFAULT 'NEW'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `order_date`, `total_amount`, `status`) VALUES
(1, 6, '2025-06-13 15:10:31', 140.85, 'NEW'),
(2, 6, '2025-06-13 15:26:48', 40.69, 'NEW'),
(3, 6, '2025-06-13 15:27:10', 39.99, 'CANCELLED'),
(4, 6, '2025-06-13 15:27:17', 31.29, 'SHIPPED'),
(5, 8, '2025-06-15 18:47:25', 99.97, 'NEW');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` varchar(36) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  `price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `quantity`, `price`) VALUES
(1, 1, '91070e7d-f179-4855-a70d-755a52f62268', 1, 18.99),
(2, 1, '986c14e4-7787-426d-b88d-0723ddc54119', 1, 27.99),
(3, 1, 'e4152396-34bf-4275-a29d-55a0f4dbe443', 1, 31.29),
(4, 1, 'e4152396-34bf-4275-a29d-55a0f4dbe443', 1, 31.29),
(5, 1, 'e4152396-34bf-4275-a29d-55a0f4dbe443', 1, 31.29),
(6, 2, '7265a475-4ca0-49f3-a370-bb8f7e1346fa', 1, 40.69),
(7, 3, 'fa4e751f-27ea-4347-86bb-12775a804861', 1, 39.99),
(8, 4, 'e4152396-34bf-4275-a29d-55a0f4dbe443', 1, 31.29),
(9, 5, '986c14e4-7787-426d-b88d-0723ddc54119', 1, 27.99),
(10, 5, 'e4152396-34bf-4275-a29d-55a0f4dbe443', 1, 31.29),
(11, 5, '7265a475-4ca0-49f3-a370-bb8f7e1346fa', 1, 40.69);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `physical_books`
--

CREATE TABLE `physical_books` (
  `product_id` varchar(36) NOT NULL,
  `page_count` int(11) NOT NULL,
  `cover_type` enum('Twarda','Miękka','Inna') NOT NULL,
  `isbn` varchar(20) NOT NULL,
  `publication_year` int(11) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `available_in_stock` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `physical_books`
--

INSERT INTO `physical_books` (`product_id`, `page_count`, `cover_type`, `isbn`, `publication_year`, `publisher`, `available_in_stock`) VALUES
('457841e3-4068-425e-bc9b-47beaeaee003', 380, 'Twarda', '978-83-8222-226-5', 2020, 'SBM', 1),
('7265a475-4ca0-49f3-a370-bb8f7e1346fa', 530, 'Twarda', '114-64-8346-465-4', 2018, 'insignis', 1),
('986c14e4-7787-426d-b88d-0723ddc54119', 490, 'Miękka', '227-32-7588-455-7', 2016, 'Rebis', 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `products`
--

CREATE TABLE `products` (
  `id` varchar(36) NOT NULL DEFAULT uuid(),
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `category_id` int(11) NOT NULL,
  `product_type` enum('PHYSICAL','EBOOK','AUDIOBOOK') NOT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT NULL ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `title`, `author`, `price`, `category_id`, `product_type`, `created_at`, `updated_at`) VALUES
('457841e3-4068-425e-bc9b-47beaeaee003', 'Zbrodnia i Kara', 'Fiodor Dostojewski', 32.99, 11, 'PHYSICAL', '2025-06-12 19:31:01', NULL),
('7265a475-4ca0-49f3-a370-bb8f7e1346fa', 'Miasto na Marsie', 'Kelly Weinersmith', 40.69, 9, 'PHYSICAL', '2025-06-12 19:42:15', NULL),
('91070e7d-f179-4855-a70d-755a52f62268', 'Potop', 'Henryk Sienkiewicz', 18.99, 11, 'AUDIOBOOK', '2025-06-12 19:37:23', NULL),
('986c14e4-7787-426d-b88d-0723ddc54119', 'Sztuka zwycięstwa. Wspomnienia twórcy NIKE', 'Phil Knight', 27.99, 1, 'PHYSICAL', '2025-06-12 19:45:47', NULL),
('e4152396-34bf-4275-a29d-55a0f4dbe443', 'Kroniki Diuny, Tom 1', 'Frank Herbert', 31.29, 3, 'EBOOK', '2025-06-12 19:55:16', NULL),
('fa4e751f-27ea-4347-86bb-12775a804861', 'Wiedźmin, Czas pogardy', 'Andrzej Sapkowski', 39.99, 2, 'AUDIOBOOK', '2025-06-12 19:58:27', '2025-06-13 16:30:14');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(2, 'admin'),
(1, 'user');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` int(11) DEFAULT 1,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `apartment_number` varchar(10) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT NULL ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `role_id`, `first_name`, `last_name`, `city`, `postal_code`, `street`, `apartment_number`, `phone`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin', 'admin', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-06-11 20:35:57', NULL),
(4, 'Bartosz111', 'Bartosz@gmail.com', 'Bartosz123', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-06-12 15:40:24', NULL),
(6, 'JanK123', 'JKowalski@onet.pl', 'Janek123', 1, 'Jan', 'Kowalski', 'Rzeszów', '11-111', 'Rejtana 13', '41', '987654321', '2025-06-13 11:18:41', '2025-06-13 13:33:21'),
(8, 'Piotr123', 'PNowak@wp.pl', 'Piotrek123', 1, 'Piotr', 'Nowak', 'Warszawa', '22-222', 'Nowogrodzka 21', '9', '999888777', '2025-06-15 18:46:11', '2025-06-15 18:47:25');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `audiobooks`
--
ALTER TABLE `audiobooks`
  ADD PRIMARY KEY (`product_id`);

--
-- Indeksy dla tabeli `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indeksy dla tabeli `ebooks`
--
ALTER TABLE `ebooks`
  ADD PRIMARY KEY (`product_id`);

--
-- Indeksy dla tabeli `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeksy dla tabeli `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indeksy dla tabeli `physical_books`
--
ALTER TABLE `physical_books`
  ADD PRIMARY KEY (`product_id`),
  ADD UNIQUE KEY `isbn` (`isbn`);

--
-- Indeksy dla tabeli `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indeksy dla tabeli `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `audiobooks`
--
ALTER TABLE `audiobooks`
  ADD CONSTRAINT `audiobooks_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `ebooks`
--
ALTER TABLE `ebooks`
  ADD CONSTRAINT `ebooks_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `physical_books`
--
ALTER TABLE `physical_books`
  ADD CONSTRAINT `physical_books_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
