package ke.ac.egerton.ams.models;

import java.io.Serializable;

/**
 * Model class representing individual KCSE subject grades (Step 3 detail).
 * Maps to: subject_grades table
 */
public class SubjectGrade implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Available KCSE subjects
    public static final String[] SUBJECTS = {
        "Mathematics",
        "English",
        "Kiswahili",
        "Biology",
        "Chemistry",
        "Physics",
        "History & Government",
        "Geography",
        "Christian Religious Education (CRE)",
        "Islamic Religious Education (IRE)",
        "Business Studies",
        "Computer Studies",
        "Agriculture",
        "Home Science",
        "Art & Design",
        "Music",
        "French"
    };
    
    // Available grades
    public static final String[] GRADES = {
        "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "E"
    };
    
    private Long id;
    private Long applicationId;
    private Long academicId;
    private String subjectName;
    private String grade;
    private int subjectOrder;
    
    // Default constructor
    public SubjectGrade() {
    }
    
    // Constructor with fields
    public SubjectGrade(Long applicationId, Long academicId, 
                        String subjectName, String grade, int subjectOrder) {
        this.applicationId = applicationId;
        this.academicId = academicId;
        this.subjectName = subjectName;
        this.grade = grade;
        this.subjectOrder = subjectOrder;
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
    
    public Long getAcademicId() {
        return academicId;
    }
    
    public void setAcademicId(Long academicId) {
        this.academicId = academicId;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public int getSubjectOrder() {
        return subjectOrder;
    }
    
    public void setSubjectOrder(int subjectOrder) {
        this.subjectOrder = subjectOrder;
    }
    
    /**
     * Get grade points for this subject
     */
    public int getGradePoints() {
        switch (grade) {
            case "A": return 12;
            case "A-": return 11;
            case "B+": return 10;
            case "B": return 9;
            case "B-": return 8;
            case "C+": return 7;
            case "C": return 6;
            case "C-": return 5;
            case "D+": return 4;
            case "D": return 3;
            case "D-": return 2;
            case "E": return 1;
            default: return 0;
        }
    }
    
    @Override
    public String toString() {
        return "SubjectGrade{" +
                "subjectName='" + subjectName + '\'' +
                ", grade='" + grade + '\'' +
                ", subjectOrder=" + subjectOrder +
                '}';
    }
}
