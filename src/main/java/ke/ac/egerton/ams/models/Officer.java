package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing an Admission Officer account.
 * Maps to: officers table
 */
public class Officer implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String fullName;
    private String email;
    private String passwordHash;
    private int loginAttempts;
    private LocalDateTime lockedUntil;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    // Default constructor
    public Officer() {
        this.isActive = true;
        this.loginAttempts = 0;
    }
    
    // Constructor with essential fields
    public Officer(String fullName, String email, String passwordHash) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isActive = true;
        this.loginAttempts = 0;
    }
    
    // Full constructor
    public Officer(Long id, String fullName, String email, String passwordHash,
                   int loginAttempts, LocalDateTime lockedUntil, 
                   boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.loginAttempts = loginAttempts;
        this.lockedUntil = lockedUntil;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    
    // Business methods
    
    /**
     * Check if account is currently locked
     * @return true if locked, false otherwise
     */
    public boolean isLocked() {
        if (lockedUntil == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(lockedUntil);
    }
    
    /**
     * Increment login attempts and lock if threshold reached
     * @return true if account is now locked
     */
    public boolean incrementLoginAttempts() {
        this.loginAttempts++;
        if (this.loginAttempts >= 3) {
            this.lockedUntil = LocalDateTime.now().plusMinutes(15);
            return true;
        }
        return false;
    }
    
    /**
     * Reset login attempts after successful login
     */
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.lockedUntil = null;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
    
    public int getLoginAttempts() {
        return loginAttempts;
    }
    
    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }
    
    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }
    
    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
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
    
    @Override
    public String toString() {
        return "Officer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", loginAttempts=" + loginAttempts +
                ", isLocked=" + isLocked() +
                ", isActive=" + isActive +
                '}';
    }
}
