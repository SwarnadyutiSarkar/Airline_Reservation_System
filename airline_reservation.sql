CREATE DATABASE airline_reservation;

USE airline_reservation;

CREATE TABLE flights (
    id INT PRIMARY KEY AUTO_INCREMENT,
    flight_number VARCHAR(50) NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_date DATE NOT NULL,
    available_seats INT NOT NULL
);

CREATE TABLE customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE reservations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    flight_id INT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (flight_id) REFERENCES flights(id)
);
