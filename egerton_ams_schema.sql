-- =========================================
-- EGERTON UNIVERSITY ADMISSION MANAGEMENT SYSTEM
-- MySQL 8.x Database Schema
-- =========================================

-- Create database
CREATE DATABASE IF NOT EXISTS egerton_ams
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE egerton_ams;

-- =========================================
-- TABLE: applicant_accounts
-- Stores applicant login credentials
-- =========================================
CREATE TABLE applicant_accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_email (email),
    INDEX idx_active (is_active)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: officers
-- Stores admission officer accounts
-- =========================================
CREATE TABLE officers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    login_attempts INT DEFAULT 0,
    locked_until TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_active (is_active)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: schools
-- University schools/faculties
-- =========================================
CREATE TABLE schools (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_name VARCHAR(255) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_code (code)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: programmes
-- Academic programmes offered
-- =========================================
CREATE TABLE programmes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id BIGINT NOT NULL,
    programme_name VARCHAR(255) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    level ENUM('UNDERGRADUATE', 'POSTGRADUATE') DEFAULT 'UNDERGRADUATE',
    category ENUM('CERTIFICATE', 'DIPLOMA', 'DEGREE', 'MASTERS', 'DOCTORATE') DEFAULT 'DEGREE',
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (school_id) REFERENCES schools(id),
    INDEX idx_school (school_id),
    INDEX idx_level_category (level, category)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: intakes
-- Admission intake periods
-- =========================================
CREATE TABLE intakes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    programme_id BIGINT NOT NULL,
    intake_year VARCHAR(10) NOT NULL,
    semester ENUM('SEPTEMBER', 'JANUARY') NOT NULL,
    open_date DATE,
    close_date DATE,
    is_open BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (programme_id) REFERENCES programmes(id),
    INDEX idx_programme (programme_id),
    INDEX idx_open (is_open)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: applications
-- Master application records
-- =========================================
CREATE TABLE applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    applicant_account_id BIGINT NOT NULL,
    intake_id BIGINT NOT NULL,
    reviewed_by BIGINT NULL,
    reference_number VARCHAR(20) UNIQUE,
    status ENUM('DRAFT', 'SUBMITTED', 'UNDER_REVIEW', 'ACCEPTED', 'REJECTED') DEFAULT 'DRAFT',
    current_step INT DEFAULT 1 CHECK (current_step >= 1 AND current_step <= 7),
    officer_notes TEXT,
    decision_message TEXT,
    submitted_at TIMESTAMP NULL,
    decided_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (applicant_account_id) REFERENCES applicant_accounts(id),
    FOREIGN KEY (intake_id) REFERENCES intakes(id),
    FOREIGN KEY (reviewed_by) REFERENCES officers(id),
    INDEX idx_applicant (applicant_account_id),
    INDEX idx_status (status),
    INDEX idx_reference (reference_number)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: applicant_personal
-- Step 1: Personal Information
-- =========================================
CREATE TABLE applicant_personal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender ENUM('MALE', 'FEMALE') NOT NULL,
    nationality VARCHAR(100) DEFAULT 'Kenyan',
    national_id VARCHAR(9) CHECK (national_id IS NULL OR LENGTH(national_id) IN (8, 9)),
    birth_cert_number VARCHAR(7) NOT NULL CHECK (LENGTH(birth_cert_number) = 7),
    phone_number VARCHAR(13) NOT NULL CHECK (LENGTH(phone_number) = 13),
    email VARCHAR(255) NOT NULL,
    county VARCHAR(100) NOT NULL,
    town VARCHAR(100) NOT NULL,
    photo_path VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    INDEX idx_application (application_id)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: applicant_programme
-- Step 2: Programme Selection
-- =========================================
CREATE TABLE applicant_programme (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL UNIQUE,
    programme_id BIGINT NOT NULL,
    intake_id BIGINT NOT NULL,
    level VARCHAR(50) DEFAULT 'UNDERGRADUATE',
    category VARCHAR(50) DEFAULT 'DEGREE',
    study_mode ENUM('FULL_TIME', 'PART_TIME') NOT NULL,
    campus VARCHAR(100) DEFAULT 'Main Campus (Njoro)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    FOREIGN KEY (programme_id) REFERENCES programmes(id),
    FOREIGN KEY (intake_id) REFERENCES intakes(id),
    INDEX idx_application (application_id)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: applicant_academics
-- Step 3: KCSE Academic Background
-- =========================================
CREATE TABLE applicant_academics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL UNIQUE,
    kcse_index_number VARCHAR(11) NOT NULL CHECK (LENGTH(kcse_index_number) = 11),
    year_of_exam INT NOT NULL,
    school_name VARCHAR(255) NOT NULL,
    school_type ENUM('NATIONAL', 'EXTRA_COUNTY', 'COUNTY', 'PRIVATE', 'INTERNATIONAL') NOT NULL,
    overall_grade VARCHAR(2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    INDEX idx_application (application_id)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: subject_grades
-- Step 3: Individual subject grades
-- =========================================
CREATE TABLE subject_grades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL,
    academic_id BIGINT NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    grade VARCHAR(2) NOT NULL,
    subject_order INT NOT NULL,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    FOREIGN KEY (academic_id) REFERENCES applicant_academics(id) ON DELETE CASCADE,
    INDEX idx_application (application_id)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: applicant_documents
-- Step 4: Document Uploads
-- =========================================
CREATE TABLE applicant_documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL UNIQUE,
    kcse_cert_path VARCHAR(500),
    national_id_path VARCHAR(500),
    birth_cert_path VARCHAR(500),
    summary_pdf_path VARCHAR(500),
    pdf_generated_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    INDEX idx_application (application_id)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: applicant_guardians
-- Step 5: Guardian Information
-- =========================================
CREATE TABLE applicant_guardians (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    relationship ENUM('PARENT', 'GUARDIAN', 'SIBLING', 'SPOUSE') NOT NULL,
    phone_number VARCHAR(13) NOT NULL CHECK (LENGTH(phone_number) = 13),
    is_primary BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    INDEX idx_application (application_id)
) ENGINE=InnoDB;

-- =========================================
-- TABLE: applicant_extra
-- Step 6: Extra Information (Disability)
-- =========================================
CREATE TABLE applicant_extra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL UNIQUE,
    has_disability BOOLEAN DEFAULT FALSE,
    disability_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    INDEX idx_application (application_id)
) ENGINE=InnoDB;

-- =========================================
-- VIEW: vw_dashboard_counts
-- Dashboard statistics for officers
-- =========================================
CREATE OR REPLACE VIEW vw_dashboard_counts AS
SELECT 
    COUNT(*) AS total_applications,
    SUM(CASE WHEN status = 'SUBMITTED' THEN 1 ELSE 0 END) AS pending_review,
    SUM(CASE WHEN status = 'UNDER_REVIEW' THEN 1 ELSE 0 END) AS under_review,
    SUM(CASE WHEN status = 'ACCEPTED' THEN 1 ELSE 0 END) AS accepted,
    SUM(CASE WHEN status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected,
    SUM(CASE WHEN status = 'DRAFT' THEN 1 ELSE 0 END) AS drafts
FROM applications;

-- =========================================
-- VIEW: vw_application_summary
-- Comprehensive application view for officers
-- =========================================
CREATE OR REPLACE VIEW vw_application_summary AS
SELECT 
    a.id AS application_id,
    a.reference_number,
    a.status,
    a.current_step,
    a.submitted_at,
    a.decided_at,
    a.created_at AS started_at,
    CONCAT(p.first_name, ' ', COALESCE(p.middle_name, ''), ' ', p.last_name) AS full_name,
    p.email,
    p.phone_number,
    p.national_id,
    p.birth_cert_number,
    p.county,
    p.town,
    pr.programme_name,
    ap.study_mode,
    ap.campus,
    i.intake_year,
    i.semester,
    o.full_name AS reviewing_officer,
    a.officer_notes,
    a.decision_message
FROM applications a
LEFT JOIN applicant_personal p ON a.id = p.application_id
LEFT JOIN applicant_programme ap ON a.id = ap.application_id
LEFT JOIN programmes pr ON ap.programme_id = pr.id
LEFT JOIN intakes i ON ap.intake_id = i.id
LEFT JOIN officers o ON a.reviewed_by = o.id;

-- =========================================
-- VIEW: vw_applicant_status
-- Applicant's view of their application status
-- =========================================
CREATE OR REPLACE VIEW vw_applicant_status AS
SELECT 
    a.id AS application_id,
    a.reference_number,
    a.status,
    aa.email AS login_email,
    CONCAT(p.first_name, ' ', COALESCE(p.middle_name, ''), ' ', p.last_name) AS full_name,
    pr.programme_name,
    ap.study_mode,
    i.intake_year,
    a.submitted_at,
    a.decided_at,
    a.decision_message
FROM applications a
JOIN applicant_accounts aa ON a.applicant_account_id = aa.id
LEFT JOIN applicant_personal p ON a.id = p.application_id
LEFT JOIN applicant_programme ap ON a.id = ap.application_id
LEFT JOIN programmes pr ON ap.programme_id = pr.id
LEFT JOIN intakes i ON ap.intake_id = i.id;

-- =========================================
-- VIEW: vw_subject_grades
-- Subject grades for review
-- =========================================
CREATE OR REPLACE VIEW vw_subject_grades AS
SELECT 
    application_id,
    subject_order,
    subject_name,
    grade
FROM subject_grades
ORDER BY application_id, subject_order;

-- =========================================
-- STORED PROCEDURE: sp_generate_reference
-- Generates reference number and submits application
-- =========================================
DELIMITER //
CREATE PROCEDURE sp_generate_reference(IN p_application_id BIGINT)
BEGIN
    DECLARE v_year VARCHAR(4);
    DECLARE v_seq INT;
    DECLARE v_reference VARCHAR(20);
    
    SET v_year = YEAR(CURRENT_DATE());
    
    SELECT COALESCE(MAX(CAST(SUBSTRING(reference_number, 10) AS UNSIGNED)), 0) + 1 INTO v_seq
    FROM applications
    WHERE reference_number LIKE CONCAT('EGU-', v_year, '-%');
    
    SET v_reference = CONCAT('EGU-', v_year, '-', LPAD(v_seq, 6, '0'));
    
    UPDATE applications 
    SET reference_number = v_reference,
        status = 'SUBMITTED',
        submitted_at = CURRENT_TIMESTAMP,
        current_step = 7
    WHERE id = p_application_id;
    
    SELECT v_reference AS reference_number;
END //
DELIMITER ;

-- =========================================
-- STORED PROCEDURE: sp_open_review
-- Marks application as under review
-- =========================================
DELIMITER //
CREATE PROCEDURE sp_open_review(IN p_application_id BIGINT, IN p_officer_id BIGINT)
BEGIN
    UPDATE applications 
    SET status = 'UNDER_REVIEW',
        reviewed_by = p_officer_id
    WHERE id = p_application_id
    AND status IN ('SUBMITTED', 'UNDER_REVIEW');
END //
DELIMITER ;

-- =========================================
-- STORED PROCEDURE: sp_accept_application
-- Accepts an application
-- =========================================
DELIMITER //
CREATE PROCEDURE sp_accept_application(
    IN p_application_id BIGINT, 
    IN p_officer_id BIGINT,
    IN p_notes TEXT,
    IN p_message TEXT
)
BEGIN
    UPDATE applications 
    SET status = 'ACCEPTED',
        reviewed_by = p_officer_id,
        officer_notes = p_notes,
        decision_message = p_message,
        decided_at = CURRENT_TIMESTAMP
    WHERE id = p_application_id;
END //
DELIMITER ;

-- =========================================
-- STORED PROCEDURE: sp_reject_application
-- Rejects an application
-- =========================================
DELIMITER //
CREATE PROCEDURE sp_reject_application(
    IN p_application_id BIGINT, 
    IN p_officer_id BIGINT,
    IN p_notes TEXT,
    IN p_message TEXT
)
BEGIN
    UPDATE applications 
    SET status = 'REJECTED',
        reviewed_by = p_officer_id,
        officer_notes = p_notes,
        decision_message = p_message,
        decided_at = CURRENT_TIMESTAMP
    WHERE id = p_application_id;
END //
DELIMITER ;

-- =========================================
-- SEED DATA: Schools
-- =========================================
INSERT INTO schools (school_name, code) VALUES
('Faculty of Agriculture', 'FAG'),
('Faculty of Arts and Social Sciences', 'FASS'),
('Faculty of Commerce', 'FC'),
('Faculty of Education and Community Studies', 'FECS'),
('Faculty of Engineering and Technology', 'FET'),
('Faculty of Environment and Resources Development', 'FERD'),
('Faculty of Health Sciences', 'FHS'),
('Faculty of Law', 'FL'),
('Faculty of Science', 'FS'),
('Faculty of Veterinary Medicine and Surgery', 'FVMS');

-- =========================================
-- SEED DATA: Programmes (54 Undergraduate Degrees)
-- =========================================
INSERT INTO programmes (school_id, programme_name, code, level, category) VALUES
-- Faculty of Agriculture
(1, 'Bachelor of Science in Agriculture', 'BSC-AGR', 'UNDERGRADUATE', 'DEGREE'),
(1, 'Bachelor of Science in Agribusiness Management', 'BSC-ABM', 'UNDERGRADUATE', 'DEGREE'),
(1, 'Bachelor of Science in Animal Science', 'BSC-ANS', 'UNDERGRADUATE', 'DEGREE'),
(1, 'Bachelor of Science in Horticulture', 'BSC-HOR', 'UNDERGRADUATE', 'DEGREE'),
(1, 'Bachelor of Science in Food Science and Technology', 'BSC-FST', 'UNDERGRADUATE', 'DEGREE'),
(1, 'Bachelor of Science in Agricultural Education and Extension', 'BSC-AEE', 'UNDERGRADUATE', 'DEGREE'),
-- Faculty of Arts and Social Sciences
(2, 'Bachelor of Arts', 'BA', 'UNDERGRADUATE', 'DEGREE'),
(2, 'Bachelor of Arts in Economics', 'BA-ECO', 'UNDERGRADUATE', 'DEGREE'),
(2, 'Bachelor of Arts in Sociology', 'BA-SOC', 'UNDERGRADUATE', 'DEGREE'),
(2, 'Bachelor of Arts in Psychology', 'BA-PSY', 'UNDERGRADUATE', 'DEGREE'),
(2, 'Bachelor of Arts in Communication and Media Studies', 'BA-CMS', 'UNDERGRADUATE', 'DEGREE'),
(2, 'Bachelor of Arts in Gender, Women and Development Studies', 'BA-GWD', 'UNDERGRADUATE', 'DEGREE'),
-- Faculty of Commerce
(3, 'Bachelor of Commerce', 'BCOM', 'UNDERGRADUATE', 'DEGREE'),
(3, 'Bachelor of Science in Finance', 'BSC-FIN', 'UNDERGRADUATE', 'DEGREE'),
(3, 'Bachelor of Business Management', 'BBM', 'UNDERGRADUATE', 'DEGREE'),
(3, 'Bachelor of Science in Accounting', 'BSC-ACC', 'UNDERGRADUATE', 'DEGREE'),
(3, 'Bachelor of Science in Procurement and Supply Chain Management', 'BSC-PSC', 'UNDERGRADUATE', 'DEGREE'),
(3, 'Bachelor of Science in Human Resource Management', 'BSC-HRM', 'UNDERGRADUATE', 'DEGREE'),
-- Faculty of Education
(4, 'Bachelor of Education (Arts)', 'BED-ARTS', 'UNDERGRADUATE', 'DEGREE'),
(4, 'Bachelor of Education (Science)', 'BED-SCI', 'UNDERGRADUATE', 'DEGREE'),
(4, 'Bachelor of Education (Early Childhood Development)', 'BED-ECD', 'UNDERGRADUATE', 'DEGREE'),
(4, 'Bachelor of Science in Agricultural Education', 'BSC-AGED', 'UNDERGRADUATE', 'DEGREE'),
(4, 'Bachelor of Science in Technology Education', 'BSC-TED', 'UNDERGRADUATE', 'DEGREE'),
(4, 'Bachelor of Arts in Community Development', 'BA-CD', 'UNDERGRADUATE', 'DEGREE'),
-- Faculty of Engineering
(5, 'Bachelor of Science in Civil Engineering', 'BSC-CVE', 'UNDERGRADUATE', 'DEGREE'),
(5, 'Bachelor of Science in Electrical and Electronics Engineering', 'BSC-EEE', 'UNDERGRADUATE', 'DEGREE'),
(5, 'Bachelor of Science in Mechanical Engineering', 'BSC-MCE', 'UNDERGRADUATE', 'DEGREE'),
(5, 'Bachelor of Science in Agricultural Engineering', 'BSC-AGE', 'UNDERGRADUATE', 'DEGREE'),
(5, 'Bachelor of Science in Computer Science', 'BSC-CS', 'UNDERGRADUATE', 'DEGREE'),
(5, 'Bachelor of Science in Information Technology', 'BSC-IT', 'UNDERGRADUATE', 'DEGREE'),
-- Faculty of Environment
(6, 'Bachelor of Science in Environmental Science', 'BSC-ENV', 'UNDERGRADUATE', 'DEGREE'),
(6, 'Bachelor of Science in Geography', 'BSC-GEO', 'UNDERGRADUATE', 'DEGREE'),
(6, 'Bachelor of Science in Natural Resource Management', 'BSC-NRM', 'UNDERGRADUATE', 'DEGREE'),
(6, 'Bachelor of Science in Wildlife Management', 'BSC-WLM', 'UNDERGRADUATE', 'DEGREE'),
(6, 'Bachelor of Science in Tourism and Hospitality Management', 'BSC-THM', 'UNDERGRADUATE', 'DEGREE'),
(6, 'Bachelor of Arts in Urban and Regional Planning', 'BA-URP', 'UNDERGRADUATE', 'DEGREE'),
-- Faculty of Health Sciences
(7, 'Bachelor of Science in Nursing', 'BSC-NUR', 'UNDERGRADUATE', 'DEGREE'),
(7, 'Bachelor of Science in Community Health', 'BSC-CHE', 'UNDERGRADUATE', 'DEGREE'),
(7, 'Bachelor of Science in Public Health', 'BSC-PUH', 'UNDERGRADUATE', 'DEGREE'),
(7, 'Bachelor of Science in Health Records and Information Management', 'BSC-HRI', 'UNDERGRADUATE', 'DEGREE'),
-- Faculty of Law
(8, 'Bachelor of Laws (LLB)', 'LLB', 'UNDERGRADUATE', 'DEGREE'),
-- Faculty of Science
(9, 'Bachelor of Science in Mathematics', 'BSC-MAT', 'UNDERGRADUATE', 'DEGREE'),
(9, 'Bachelor of Science in Physics', 'BSC-PHY', 'UNDERGRADUATE', 'DEGREE'),
(9, 'Bachelor of Science in Chemistry', 'BSC-CHM', 'UNDERGRADUATE', 'DEGREE'),
(9, 'Bachelor of Science in Biology', 'BSC-BIO', 'UNDERGRADUATE', 'DEGREE'),
(9, 'Bachelor of Science in Biochemistry', 'BSC-BCH', 'UNDERGRADUATE', 'DEGREE'),
(9, 'Bachelor of Science in Botany', 'BSC-BOT', 'UNDERGRADUATE', 'DEGREE'),
(9, 'Bachelor of Science in Zoology', 'BSC-ZOO', 'UNDERGRADUATE', 'DEGREE'),
(9, 'Bachelor of Science in Statistics', 'BSC-STA', 'UNDERGRADUATE', 'DEGREE'),
(9, 'Bachelor of Science in Applied Statistics', 'BSC-AST', 'UNDERGRADUATE', 'DEGREE'),
-- Faculty of Veterinary Medicine
(10, 'Bachelor of Veterinary Medicine', 'BVM', 'UNDERGRADUATE', 'DEGREE'),
(10, 'Bachelor of Science in Animal Health Management', 'BSC-AHM', 'UNDERGRADUATE', 'DEGREE'),
(10, 'Bachelor of Science in Dairy Technology and Management', 'BSC-DTM', 'UNDERGRADUATE', 'DEGREE'),
(10, 'Bachelor of Science in Leather Science and Technology', 'BSC-LST', 'UNDERGRADUATE', 'DEGREE');

-- =========================================
-- SEED DATA: Intakes for all programmes
-- =========================================
INSERT INTO intakes (programme_id, intake_year, semester, open_date, close_date, is_open)
SELECT 
    id,
    '2025',
    'SEPTEMBER',
    '2025-01-01',
    '2025-08-31',
    TRUE
FROM programmes;

INSERT INTO intakes (programme_id, intake_year, semester, open_date, close_date, is_open)
SELECT 
    id,
    '2026',
    'JANUARY',
    '2025-06-01',
    '2025-12-31',
    TRUE
FROM programmes;

-- =========================================
-- SEED DATA: Default Officer Account
-- Password: officer123 (BCrypt hashed)
-- =========================================
INSERT INTO officers (full_name, email, password_hash) VALUES
('Admin Officer', 'admin@egerton.ac.ke', '$2a$12$VYVbNFCEPAcEaMSRo7tMeOfYhQnn.LOIQ5VHKN7fKYWZzylm2oof2');

-- =========================================
-- END OF SCHEMA
-- =========================================
