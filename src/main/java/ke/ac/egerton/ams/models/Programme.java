package ke.ac.egerton.ams.models;

import java.io.Serializable;

/**
 * Model class representing an Academic Programme.
 * Maps to: programmes table
 */
public class Programme implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Enums matching database constraints
    public enum Level {
        UNDERGRADUATE, POSTGRADUATE
    }
    
    public enum Category {
        CERTIFICATE, DIPLOMA, DEGREE, MASTERS, DOCTORATE
    }
    
    private Long id;
    private Long schoolId;
    private String programmeName;
    private String code;
    private Level level;
    private Category category;
    private boolean isActive;
    
    // For display purposes - populated via JOIN
    private String schoolName;
    
    // Default constructor
    public Programme() {
        this.level = Level.UNDERGRADUATE;
        this.category = Category.DEGREE;
        this.isActive = true;
    }
    
    // Constructor with essential fields
    public Programme(Long schoolId, String programmeName, String code) {
        this.schoolId = schoolId;
        this.programmeName = programmeName;
        this.code = code;
        this.level = Level.UNDERGRADUATE;
        this.category = Category.DEGREE;
        this.isActive = true;
    }
    
    // Full constructor
    public Programme(Long id, Long schoolId, String programmeName, String code,
                     Level level, Category category, boolean isActive) {
        this.id = id;
        this.schoolId = schoolId;
        this.programmeName = programmeName;
        this.code = code;
        this.level = level;
        this.category = category;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSchoolId() {
        return schoolId;
    }
    
    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }
    
    public String getProgrammeName() {
        return programmeName;
    }
    
    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Level getLevel() {
        return level;
    }
    
    public void setLevel(Level level) {
        this.level = level;
    }
    
    public void setLevel(String level) {
        this.level = Level.valueOf(level.toUpperCase());
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public void setCategory(String category) {
        this.category = Category.valueOf(category.toUpperCase());
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getSchoolName() {
        return schoolName;
    }
    
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    
    // Display helper
    public String getLevelDisplay() {
        if (level == null) return "";
        return level.name().charAt(0) + 
               level.name().substring(1).toLowerCase();
    }
    
    public String getCategoryDisplay() {
        if (category == null) return "";
        return category.name().charAt(0) + 
               category.name().substring(1).toLowerCase();
    }
    
    @Override
    public String toString() {
        return "Programme{" +
                "id=" + id +
                ", programmeName='" + programmeName + '\'' +
                ", code='" + code + '\'' +
                ", level=" + level +
                ", category=" + category +
                '}';
    }
}
