package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing applicant KCSE academic background (Step 3).
 * Maps to: applicant_academics table
 */
public class ApplicantAcademics implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum SchoolType {
        NATIONAL, EXTRA_COUNTY, COUNTY, PRIVATE, INTERNATIONAL
    }
    
    public enum Grade {
        A("A", 12), A_MINUS("A-", 11),
        B_PLUS("B+", 10), B("B", 9), B_MINUS("B-", 8),
        C_PLUS("C+", 7), C("C", 6), C_MINUS("C-", 5),
        D_PLUS("D+", 4), D("D", 3), D_MINUS("D-", 2),
        E("E", 1);
        
        private final String display;
        private final int points;
        
        Grade(String display, int points) {
            this.display = display;
            this.points = points;
        }
        
        public String getDisplay() {
            return display;
        }
        
        public int getPoints() {
            return points;
        }
        
        public static Grade fromString(String text) {
            for (Grade g : Grade.values()) {
                if (g.display.equalsIgnoreCase(text) || g.name().equalsIgnoreCase(text)) {
                    return g;
                }
            }
            // Handle database format
            String normalized = text.replace("-", "_MINUS").replace("+", "_PLUS");
            return Grade.valueOf(normalized.toUpperCase());
        }
    }
    
    private Long id;
    private Long applicationId;
    private String kcseIndexNumber;    // 11 digits, required
    private int yearOfExam;
    private String schoolName;
    private SchoolType schoolType;
    private Grade overallGrade;
    private LocalDateTime createdAt;
    
    // Default constructor
    public ApplicantAcademics() {
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
    
    public String getKcseIndexNumber() {
        return kcseIndexNumber;
    }
    
    public void setKcseIndexNumber(String kcseIndexNumber) {
        this.kcseIndexNumber = kcseIndexNumber;
    }
    
    public int getYearOfExam() {
        return yearOfExam;
    }
    
    public void setYearOfExam(int yearOfExam) {
        this.yearOfExam = yearOfExam;
    }
    
    public String getSchoolName() {
        return schoolName;
    }
    
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    
    public SchoolType getSchoolType() {
        return schoolType;
    }
    
    public void setSchoolType(SchoolType schoolType) {
        this.schoolType = schoolType;
    }
    
    public void setSchoolType(String schoolType) {
        if (schoolType != null && !schoolType.isEmpty()) {
            this.schoolType = SchoolType.valueOf(
                schoolType.toUpperCase().replace(" ", "_"));
        }
    }
    
    public Grade getOverallGrade() {
        return overallGrade;
    }
    
    public void setOverallGrade(Grade overallGrade) {
        this.overallGrade = overallGrade;
    }
    
    public void setOverallGrade(String grade) {
        if (grade != null && !grade.isEmpty()) {
            this.overallGrade = Grade.fromString(grade);
        }
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Display helpers
    public String getSchoolTypeDisplay() {
        if (schoolType == null) return "";
        switch (schoolType) {
            case NATIONAL: return "National";
            case EXTRA_COUNTY: return "Extra County";
            case COUNTY: return "County";
            case PRIVATE: return "Private";
            case INTERNATIONAL: return "International";
            default: return "";
        }
    }
    
    public String getOverallGradeDisplay() {
        return overallGrade != null ? overallGrade.getDisplay() : "";
    }
    
    @Override
    public String toString() {
        return "ApplicantAcademics{" +
                "id=" + id +
                ", applicationId=" + applicationId +
                ", kcseIndexNumber='" + kcseIndexNumber + '\'' +
                ", yearOfExam=" + yearOfExam +
                ", overallGrade=" + overallGrade +
                '}';
    }
}
