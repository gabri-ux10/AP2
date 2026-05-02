package ke.ac.egerton.ams.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Validation Utility Class
 * Provides server-side validation for all form fields.
 * Critical fields with integer-only validation:
 *   - National ID: optional, 8 or 9 digits
 *   - Birth Certificate: required, exactly 7 digits
 *   - Phone Number: required, +254 + 9 digits
 *   - KCSE Index Number: required, exactly 11 digits
 */
public class Validator {
    
    // Validation patterns
    private static final Pattern NATIONAL_ID_PATTERN = Pattern.compile("^[0-9]{8,9}$");
    private static final Pattern BIRTH_CERT_PATTERN = Pattern.compile("^[0-9]{7}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Pattern KCSE_INDEX_PATTERN = Pattern.compile("^[0-9]{11}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern DIGITS_ONLY = Pattern.compile("^[0-9]+$");
    
    // Error messages
    public static final String ERROR_NATIONAL_ID = "National ID must be 8 or 9 digits";
    public static final String ERROR_BIRTH_CERT = "Birth Certificate Number must be exactly 7 digits";
    public static final String ERROR_PHONE = "Phone number must be exactly 9 digits after +254";
    public static final String ERROR_KCSE_INDEX = "KCSE Index Number must be exactly 11 digits";
    public static final String ERROR_EMAIL = "Please enter a valid email address";
    public static final String ERROR_REQUIRED = "This field is required";
    public static final String ERROR_AGE = "Applicant must be at least 16 years old";
    
    /**
     * Validate National ID (optional, but if provided must be 8 or 9 digits)
     * @param nationalId National ID value
     * @return Error message or null if valid
     */
    public static String validateNationalId(String nationalId) {
        if (nationalId == null || nationalId.trim().isEmpty()) {
            return null; // Optional field
        }
        String trimmed = nationalId.trim();
        if (!NATIONAL_ID_PATTERN.matcher(trimmed).matches()) {
            return ERROR_NATIONAL_ID;
        }
        return null;
    }
    
    /**
     * Validate Birth Certificate Number (required, exactly 7 digits)
     * @param birthCert Birth certificate number
     * @return Error message or null if valid
     */
    public static String validateBirthCertNumber(String birthCert) {
        if (birthCert == null || birthCert.trim().isEmpty()) {
            return ERROR_REQUIRED;
        }
        String trimmed = birthCert.trim();
        if (!BIRTH_CERT_PATTERN.matcher(trimmed).matches()) {
            return ERROR_BIRTH_CERT;
        }
        return null;
    }
    
    /**
     * Validate Phone Number (required, exactly 9 digits after +254)
     * @param phoneNumber Phone number (without +254 prefix)
     * @return Error message or null if valid
     */
    public static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return ERROR_REQUIRED;
        }
        String trimmed = phoneNumber.trim();
        // Remove +254 prefix if present
        if (trimmed.startsWith("+254")) {
            trimmed = trimmed.substring(4);
        }
        if (!PHONE_PATTERN.matcher(trimmed).matches()) {
            return ERROR_PHONE;
        }
        return null;
    }
    
    /**
     * Validate KCSE Index Number (required, exactly 11 digits)
     * @param indexNumber KCSE index number
     * @return Error message or null if valid
     */
    public static String validateKcseIndexNumber(String indexNumber) {
        if (indexNumber == null || indexNumber.trim().isEmpty()) {
            return ERROR_REQUIRED;
        }
        String trimmed = indexNumber.trim();
        if (!KCSE_INDEX_PATTERN.matcher(trimmed).matches()) {
            return ERROR_KCSE_INDEX;
        }
        return null;
    }
    
    /**
     * Validate Email
     * @param email Email address
     * @return Error message or null if valid
     */
    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return ERROR_REQUIRED;
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return ERROR_EMAIL;
        }
        return null;
    }
    
    /**
     * Validate required string field
     * @param value Field value
     * @return Error message or null if valid
     */
    public static String validateRequired(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ERROR_REQUIRED;
        }
        return null;
    }
    
    /**
     * Validate required string field with custom message
     * @param value Field value
     * @param fieldName Field name for error message
     * @return Error message or null if valid
     */
    public static String validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return fieldName + " is required";
        }
        return null;
    }
    
    /**
     * Validate date of birth (must be 16+ years old)
     * @param dob Date of birth
     * @return Error message or null if valid
     */
    public static String validateDateOfBirth(LocalDate dob) {
        if (dob == null) {
            return ERROR_REQUIRED;
        }
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 16) {
            return ERROR_AGE;
        }
        return null;
    }
    
    /**
     * Validate Step 1 (Personal Information)
     * @param params Map of form parameters
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateStep1(Map<String, String> params) {
        Map<String, String> errors = new HashMap<>();
        
        // Required fields
        String error;
        
        error = validateRequired(params.get("firstName"), "First Name");
        if (error != null) errors.put("firstName", error);
        
        error = validateRequired(params.get("lastName"), "Last Name");
        if (error != null) errors.put("lastName", error);
        
        error = validateRequired(params.get("dateOfBirth"), "Date of Birth");
        if (error != null) {
            errors.put("dateOfBirth", error);
        } else {
            try {
                LocalDate dob = LocalDate.parse(params.get("dateOfBirth"));
                error = validateDateOfBirth(dob);
                if (error != null) errors.put("dateOfBirth", error);
            } catch (Exception e) {
                errors.put("dateOfBirth", "Invalid date format");
            }
        }
        
        error = validateRequired(params.get("gender"), "Gender");
        if (error != null) errors.put("gender", error);
        
        // National ID (optional but if provided must be 8 or 9 digits)
        error = validateNationalId(params.get("nationalId"));
        if (error != null) errors.put("nationalId", error);
        
        // Birth Certificate (required, 7 digits)
        error = validateBirthCertNumber(params.get("birthCertNumber"));
        if (error != null) errors.put("birthCertNumber", error);
        
        // Phone Number (required, 9 digits after +254)
        error = validatePhoneNumber(params.get("phoneNumber"));
        if (error != null) errors.put("phoneNumber", error);
        
        // Email
        error = validateEmail(params.get("email"));
        if (error != null) errors.put("email", error);
        
        // County
        error = validateRequired(params.get("county"), "County");
        if (error != null) errors.put("county", error);
        
        // Town
        error = validateRequired(params.get("town"), "Town");
        if (error != null) errors.put("town", error);
        
        return errors;
    }
    
    /**
     * Validate Step 2 (Programme Selection)
     */
    public static Map<String, String> validateStep2(Map<String, String> params) {
        Map<String, String> errors = new HashMap<>();
        
        String error;
        
        error = validateRequired(params.get("programmeId"), "Programme");
        if (error != null) errors.put("programmeId", error);
        
        error = validateRequired(params.get("intakeYear"), "Intake Year");
        if (error != null) errors.put("intakeYear", error);
        
        error = validateRequired(params.get("intakeSemester"), "Intake Semester");
        if (error != null) errors.put("intakeSemester", error);
        
        error = validateRequired(params.get("studyMode"), "Study Mode");
        if (error != null) errors.put("studyMode", error);
        
        return errors;
    }
    
    /**
     * Validate Step 3 (Academic Background)
     */
    public static Map<String, String> validateStep3(Map<String, String> params, 
                                                     int subjectCount) {
        Map<String, String> errors = new HashMap<>();
        
        String error;
        
        // KCSE Index Number (required, 11 digits)
        error = validateKcseIndexNumber(params.get("kcseIndexNumber"));
        if (error != null) errors.put("kcseIndexNumber", error);
        
        error = validateRequired(params.get("yearOfExam"), "Year of Examination");
        if (error != null) errors.put("yearOfExam", error);
        
        error = validateRequired(params.get("schoolName"), "School Name");
        if (error != null) errors.put("schoolName", error);
        
        error = validateRequired(params.get("schoolType"), "School Type");
        if (error != null) errors.put("schoolType", error);
        
        error = validateRequired(params.get("overallGrade"), "Overall Grade");
        if (error != null) errors.put("overallGrade", error);
        
        // Subject count validation
        if (subjectCount < 7) {
            errors.put("subjects", "You must enter at least 7 subjects");
        } else if (subjectCount > 8) {
            errors.put("subjects", "You can enter a maximum of 8 subjects");
        }
        
        return errors;
    }
    
    /**
     * Validate Step 5 (Guardian Information)
     */
    public static Map<String, String> validateStep5(Map<String, String> params) {
        Map<String, String> errors = new HashMap<>();
        
        String error;
        
        // Primary guardian is required
        error = validateRequired(params.get("guardian1Name"), "Guardian Name");
        if (error != null) errors.put("guardian1Name", error);
        
        error = validateRequired(params.get("guardian1Relationship"), "Relationship");
        if (error != null) errors.put("guardian1Relationship", error);
        
        error = validatePhoneNumber(params.get("guardian1Phone"));
        if (error != null) errors.put("guardian1Phone", error);
        
        // Secondary guardian (optional, but if any field is filled, all are required)
        String guardian2Name = params.get("guardian2Name");
        String guardian2Relationship = params.get("guardian2Relationship");
        String guardian2Phone = params.get("guardian2Phone");
        boolean hasSecondaryGuardianInput =
                (guardian2Name != null && !guardian2Name.trim().isEmpty()) ||
                (guardian2Relationship != null && !guardian2Relationship.trim().isEmpty()) ||
                (guardian2Phone != null && !guardian2Phone.trim().isEmpty());

        if (hasSecondaryGuardianInput) {
            error = validateRequired(guardian2Name, "Secondary Guardian Name");
            if (error != null) errors.put("guardian2Name", error);

            error = validateRequired(guardian2Relationship, "Relationship");
            if (error != null) errors.put("guardian2Relationship", error);
            
            error = validatePhoneNumber(guardian2Phone);
            if (error != null) errors.put("guardian2Phone", error);
        }
        
        return errors;
    }
    
    /**
     * Validate Step 6 (Extra Information)
     */
    public static Map<String, String> validateStep6(Map<String, String> params) {
        Map<String, String> errors = new HashMap<>();
        
        String hasDisability = params.get("hasDisability");
        if (hasDisability == null || hasDisability.trim().isEmpty()) {
            errors.put("hasDisability", "Please select Yes or No");
        } else if ("yes".equalsIgnoreCase(hasDisability) || "1".equals(hasDisability)) {
            String description = params.get("disabilityDescription");
            if (description == null || description.trim().isEmpty()) {
                errors.put("disabilityDescription", "Please describe your special needs");
            }
        }
        
        return errors;
    }
    
    /**
     * Check if string contains only digits
     */
    public static boolean isDigitsOnly(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return DIGITS_ONLY.matcher(value).matches();
    }
    
    /**
     * Sanitize input (remove leading/trailing whitespace, limit length)
     */
    public static String sanitize(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.length() > maxLength) {
            return trimmed.substring(0, maxLength);
        }
        return trimmed;
    }
    
    /**
     * Format phone number with +254 prefix
     */
    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        String digits = phoneNumber.replaceAll("[^0-9]", "");
        if (digits.startsWith("254") && digits.length() == 12) {
            return "+" + digits;
        }
        if (digits.length() == 9) {
            return "+254" + digits;
        }
        if (digits.startsWith("0") && digits.length() == 10) {
            return "+254" + digits.substring(1);
        }
        return "+254" + digits;
    }
}
