package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Model class representing an Intake period.
 * Maps to: intakes table
 */
public class Intake implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum Semester {
        SEPTEMBER, JANUARY
    }
    
    private Long id;
    private Long programmeId;
    private String intakeYear;
    private Semester semester;
    private LocalDate openDate;
    private LocalDate closeDate;
    private boolean isOpen;
    
    // For display purposes
    private String programmeName;
    
    // Default constructor
    public Intake() {
        this.isOpen = true;
    }
    
    // Constructor with essential fields
    public Intake(Long programmeId, String intakeYear, Semester semester,
                  LocalDate openDate, LocalDate closeDate) {
        this.programmeId = programmeId;
        this.intakeYear = intakeYear;
        this.semester = semester;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.isOpen = true;
    }
    
    // Full constructor
    public Intake(Long id, Long programmeId, String intakeYear, Semester semester,
                  LocalDate openDate, LocalDate closeDate, boolean isOpen) {
        this.id = id;
        this.programmeId = programmeId;
        this.intakeYear = intakeYear;
        this.semester = semester;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.isOpen = isOpen;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getProgrammeId() {
        return programmeId;
    }
    
    public void setProgrammeId(Long programmeId) {
        this.programmeId = programmeId;
    }
    
    public String getIntakeYear() {
        return intakeYear;
    }
    
    public void setIntakeYear(String intakeYear) {
        this.intakeYear = intakeYear;
    }
    
    public Semester getSemester() {
        return semester;
    }
    
    public void setSemester(Semester semester) {
        this.semester = semester;
    }
    
    public void setSemester(String semester) {
        this.semester = Semester.valueOf(semester.toUpperCase());
    }
    
    public LocalDate getOpenDate() {
        return openDate;
    }
    
    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }
    
    public LocalDate getCloseDate() {
        return closeDate;
    }
    
    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    public void setOpen(boolean open) {
        isOpen = open;
    }
    
    public String getProgrammeName() {
        return programmeName;
    }
    
    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }
    
    // Display helper
    public String getDisplayName() {
        return intakeYear + " - " + 
               (semester != null ? semester.name().charAt(0) + 
                semester.name().substring(1).toLowerCase() : "");
    }
    
    @Override
    public String toString() {
        return "Intake{" +
                "id=" + id +
                ", intakeYear='" + intakeYear + '\'' +
                ", semester=" + semester +
                ", isOpen=" + isOpen +
                '}';
    }
}
