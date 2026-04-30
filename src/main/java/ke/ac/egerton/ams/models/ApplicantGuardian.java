package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing guardian information (Step 5).
 * Maps to: applicant_guardians table
 */
public class ApplicantGuardian implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum Relationship {
        PARENT, GUARDIAN, SIBLING, SPOUSE
    }
    
    private Long id;
    private Long applicationId;
    private String fullName;
    private Relationship relationship;
    private String phoneNumber;        // +254XXXXXXXXX, 13 chars
    private boolean isPrimary;         // true = Guardian 1, false = Guardian 2
    private LocalDateTime createdAt;
    
    // Default constructor
    public ApplicantGuardian() {
    }
    
    // Constructor with fields
    public ApplicantGuardian(Long applicationId, String fullName, 
                             Relationship relationship, String phoneNumber,
                             boolean isPrimary) {
        this.applicationId = applicationId;
        this.fullName = fullName;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
        this.isPrimary = isPrimary;
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
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public Relationship getRelationship() {
        return relationship;
    }
    
    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
    
    public void setRelationship(String relationship) {
        if (relationship != null && !relationship.isEmpty()) {
            this.relationship = Relationship.valueOf(relationship.toUpperCase());
        }
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Get phone number without +254 prefix
     */
    public String getPhoneNumberShort() {
        if (phoneNumber != null && phoneNumber.startsWith("+254")) {
            return phoneNumber.substring(4);
        }
        return phoneNumber;
    }
    
    /**
     * Set phone number from short form
     */
    public void setPhoneNumberShort(String shortNumber) {
        if (shortNumber != null && !shortNumber.isEmpty()) {
            this.phoneNumber = "+254" + shortNumber;
        }
    }
    
    public boolean isPrimary() {
        return isPrimary;
    }
    
    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Display helpers
    public String getRelationshipDisplay() {
        if (relationship == null) return "";
        return relationship.name().charAt(0) + 
               relationship.name().substring(1).toLowerCase();
    }
    
    public String getGuardianLabel() {
        return isPrimary ? "Primary Guardian" : "Secondary Guardian";
    }
    
    @Override
    public String toString() {
        return "ApplicantGuardian{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", relationship=" + relationship +
                ", isPrimary=" + isPrimary +
                '}';
    }
}
