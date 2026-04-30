package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing extra/disability information (Step 6).
 * Maps to: applicant_extra table
 */
public class ApplicantExtra implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long applicationId;
    private boolean hasDisability;
    private String disabilityDescription;
    private LocalDateTime createdAt;
    
    // Default constructor
    public ApplicantExtra() {
        this.hasDisability = false;
    }
    
    // Constructor with fields
    public ApplicantExtra(Long applicationId, boolean hasDisability, 
                          String disabilityDescription) {
        this.applicationId = applicationId;
        this.hasDisability = hasDisability;
        this.disabilityDescription = disabilityDescription;
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
    
    public boolean isHasDisability() {
        return hasDisability;
    }
    
    public void setHasDisability(boolean hasDisability) {
        this.hasDisability = hasDisability;
    }
    
    public String getDisabilityDescription() {
        return disabilityDescription;
    }
    
    public void setDisabilityDescription(String disabilityDescription) {
        this.disabilityDescription = disabilityDescription;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Display helpers
    public String getHasDisabilityDisplay() {
        return hasDisability ? "Yes" : "No";
    }
    
    @Override
    public String toString() {
        return "ApplicantExtra{" +
                "id=" + id +
                ", applicationId=" + applicationId +
                ", hasDisability=" + hasDisability +
                '}';
    }
}
