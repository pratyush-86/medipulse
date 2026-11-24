-- MediPulse database schema (MySQL 8)
CREATE DATABASE IF NOT EXISTS medipulse_db;
USE medipulse_db;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  password VARCHAR(64) NOT NULL,
  phone VARCHAR(20),
  specialization VARCHAR(80),
  role ENUM('ADMIN','DOCTOR','PATIENT') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE appointments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  doctor_id INT NOT NULL,
  appt_date DATE NOT NULL,
  appt_time TIME NOT NULL,
  status ENUM('PENDING','CONFIRMED','CANCELLED','COMPLETED') DEFAULT 'PENDING',
  notes VARCHAR(255),
  FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE medical_records (
  id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  doctor_id INT NOT NULL,
  diagnosis VARCHAR(255) NOT NULL,
  treatment VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE feedback (
  id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  doctor_id INT NOT NULL,
  rating INT NOT NULL,
  comments VARCHAR(255),
  FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE hospitals (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  city VARCHAR(60) NOT NULL,
  address VARCHAR(200),
  phone VARCHAR(20),
  type VARCHAR(40)
);

CREATE TABLE settings (
  setting_key VARCHAR(50) PRIMARY KEY,
  setting_value VARCHAR(255)
);

-- Demo accounts, all with password: careflow123
INSERT INTO users(name,email,password,phone,specialization,role) VALUES
 ('Alex Morgan','alex@careflow.health',SHA2('careflow123',256),'9000000001',NULL,'ADMIN'),
 ('Amelia Carter','amelia@careflow.health',SHA2('careflow123',256),'9000000002','General Physician','DOCTOR'),
 ('Olivia Brown','olivia@email.com',SHA2('careflow123',256),'9000000003',NULL,'PATIENT');

INSERT INTO hospitals(name,city,address,phone,type) VALUES
 ('City Care Hospital','Greater Noida','Sector 62','0120-1111111','Multi-speciality'),
 ('Lifeline Clinic','Noida','Sector 18','0120-2222222','Clinic'),
 ('Sunrise Medical Centre','Ghaziabad','Indirapuram','0120-3333333','Multi-speciality'),
 ('Green Cross Hospital','Delhi','Saket','011-44444444','Emergency & Trauma'),
 ('Wellness Diagnostics','Dadri','Main Market','0120-5555555','Diagnostics');

INSERT INTO settings VALUES
 ('clinic_name','MediPulse Clinic'),('slot_minutes','30'),('working_hours','09:00 - 18:00');
