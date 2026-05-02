/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  GABRIEL
 * Created: Apr 3, 2026
 *
 * Migration helper:
 * Run this on existing databases if you want National ID to accept 8 or 9 digits.
 *
 * ALTER TABLE applicant_personal
 *     MODIFY national_id VARCHAR(9)
 *     CHECK (national_id IS NULL OR LENGTH(national_id) IN (8, 9));
 */
