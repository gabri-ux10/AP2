package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing an applicant's login account.
 * Maps to: applicant_accounts table
 */
public class ApplicantAccount implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String email;
    private String passwordHash;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    
    // Default constructor
    public ApplicantAccount() {
        this.isActive = true;
    }
    
    // Constructor with essential fields
    public ApplicantAccount(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.isActive = true;
    }
    
    // Full constructor
    public ApplicantAccount(Long id, String email, String passwordHash, 
                           boolean isActive, LocalDateTime createdAt, 
                           LocalDateTime lastLogin) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    @Override
    public String toString() {
        return "ApplicantAccount{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
