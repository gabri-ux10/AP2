package ke.ac.egerton.ams.models;

import java.io.Serializable;

/**
 * Model class representing a School/Faculty.
 * Maps to: schools table
 */
public class School implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String schoolName;
    private String code;
    private boolean isActive;
    
    // Default constructor
    public School() {
        this.isActive = true;
    }
    
    // Constructor with essential fields
    public School(String schoolName, String code) {
        this.schoolName = schoolName;
        this.code = code;
        this.isActive = true;
    }
    
    // Full constructor
    public School(Long id, String schoolName, String code, boolean isActive) {
        this.id = id;
        this.schoolName = schoolName;
        this.code = code;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSchoolName() {
        return schoolName;
    }
    
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", schoolName='" + schoolName + '\'' +
                ", code='" + code + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
