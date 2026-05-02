package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model class representing applicant personal information (Step 1).
 * Maps to: applicant_personal table
 */
public class ApplicantPersonal implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum Gender {
        MALE, FEMALE
    }
    
    private Long id;
    private Long applicationId;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationality;
    private String nationalId;         // 8 or 9 digits, optional
    private String birthCertNumber;    // 7 digits, required
    private String phoneNumber;        // +254XXXXXXXXX, 13 chars
    private String email;
    private String county;
    private String town;
    private String photoPath;
    private LocalDateTime createdAt;
    
    // Default constructor
    public ApplicantPersonal() {
        this.nationality = "Kenyan";
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public void setGender(String gender) {
        this.gender = Gender.valueOf(gender.toUpperCase());
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public String getNationalId() {
        return nationalId;
    }
    
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
    
    public String getBirthCertNumber() {
        return birthCertNumber;
    }
    
    public void setBirthCertNumber(String birthCertNumber) {
        this.birthCertNumber = birthCertNumber;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Get phone number without +254 prefix (for form display)
     */
    public String getPhoneNumberShort() {
        if (phoneNumber != null && phoneNumber.startsWith("+254")) {
            return phoneNumber.substring(4);
        }
        return phoneNumber;
    }
    
    /**
     * Set phone number from short form (adds +254 prefix)
     */
    public void setPhoneNumberShort(String shortNumber) {
        if (shortNumber != null && !shortNumber.isEmpty()) {
            this.phoneNumber = "+254" + shortNumber;
        }
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCounty() {
        return county;
    }
    
    public void setCounty(String county) {
        this.county = county;
    }
    
    public String getTown() {
        return town;
    }
    
    public void setTown(String town) {
        this.town = town;
    }
    
    public String getPhotoPath() {
        return photoPath;
    }
    
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Display helpers
    
    /**
     * Get full name (first + middle + last)
     */
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        if (middleName != null && !middleName.isEmpty()) {
            sb.append(" ").append(middleName);
        }
        sb.append(" ").append(lastName);
        return sb.toString();
    }
    
    /**
     * Get gender display text
     */
    public String getGenderDisplay() {
        if (gender == null) return "";
        return gender.name().charAt(0) + gender.name().substring(1).toLowerCase();
    }
    
    @Override
    public String toString() {
        return "ApplicantPersonal{" +
                "id=" + id +
                ", applicationId=" + applicationId +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
