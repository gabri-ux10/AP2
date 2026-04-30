package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing applicant programme selection (Step 2).
 * Maps to: applicant_programme table
 */
public class ApplicantProgramme implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum StudyMode {
        FULL_TIME, PART_TIME
    }
    
    private Long id;
    private Long applicationId;
    private Long programmeId;
    private Long intakeId;
    private String level;           // Fixed: UNDERGRADUATE
    private String category;        // Fixed: DEGREE
    private StudyMode studyMode;
    private String campus;          // Fixed: Main Campus (Njoro)
    private LocalDateTime createdAt;
    
    // For display purposes
    private String programmeName;
    private String intakeYear;
    private String intakeSemester;
    
    // Default constructor
    public ApplicantProgramme() {
        this.level = "UNDERGRADUATE";
        this.category = "DEGREE";
        this.campus = "Main Campus (Njoro)";
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
    
    public Long getProgrammeId() {
        return programmeId;
    }
    
    public void setProgrammeId(Long programmeId) {
        this.programmeId = programmeId;
    }
    
    public Long getIntakeId() {
        return intakeId;
    }
    
    public void setIntakeId(Long intakeId) {
        this.intakeId = intakeId;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public StudyMode getStudyMode() {
        return studyMode;
    }
    
    public void setStudyMode(StudyMode studyMode) {
        this.studyMode = studyMode;
    }
    
    public void setStudyMode(String studyMode) {
        if (studyMode != null && !studyMode.isEmpty()) {
            this.studyMode = StudyMode.valueOf(studyMode.toUpperCase().replace("-", "_"));
        }
    }
    
    public String getCampus() {
        return campus;
    }
    
    public void setCampus(String campus) {
        this.campus = campus;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getProgrammeName() {
        return programmeName;
    }
    
    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }
    
    public String getIntakeYear() {
        return intakeYear;
    }
    
    public void setIntakeYear(String intakeYear) {
        this.intakeYear = intakeYear;
    }
    
    public String getIntakeSemester() {
        return intakeSemester;
    }
    
    public void setIntakeSemester(String intakeSemester) {
        this.intakeSemester = intakeSemester;
    }
    
    // Display helpers
    public String getStudyModeDisplay() {
        if (studyMode == null) return "";
        return studyMode == StudyMode.FULL_TIME ? "Full-time" : "Part-time";
    }
    
    public String getLevelDisplay() {
        return "Undergraduate";
    }
    
    public String getCategoryDisplay() {
        return "Degree";
    }
    
    @Override
    public String toString() {
        return "ApplicantProgramme{" +
                "id=" + id +
                ", applicationId=" + applicationId +
                ", programmeId=" + programmeId +
                ", studyMode=" + studyMode +
                '}';
    }
}
