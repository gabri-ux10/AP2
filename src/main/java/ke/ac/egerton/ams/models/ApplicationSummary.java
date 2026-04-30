package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for application summary display.
 * Maps to: vw_application_summary view
 */
public class ApplicationSummary implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long applicationId;
    private String referenceNumber;
    private String status;
    private int currentStep;
    private LocalDateTime submittedAt;
    private LocalDateTime decidedAt;
    private LocalDateTime startedAt;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String nationalId;
    private String birthCertNumber;
    private String county;
    private String town;
    private String programmeName;
    private String studyMode;
    private String campus;
    private String intakeYear;
    private String semester;
    private String reviewingOfficer;
    private String officerNotes;
    private String decisionMessage;
    
    // Default constructor
    public ApplicationSummary() {
    }
    
    // Getters and Setters
    public Long getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getCurrentStep() {
        return currentStep;
    }
    
    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public LocalDateTime getDecidedAt() {
        return decidedAt;
    }
    
    public void setDecidedAt(LocalDateTime decidedAt) {
        this.decidedAt = decidedAt;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
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
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getNationalId() {
        return nationalId;
    }
    
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
    
    public String getBirthCertNumber() {
        return birthCertNumber;
    }
    
    public void setBirthCertNumber(String birthCertNumber) {
        this.birthCertNumber = birthCertNumber;
    }
    
    public String getCounty() {
        return county;
    }
    
    public void setCounty(String county) {
        this.county = county;
    }
    
    public String getTown() {
        return town;
    }
    
    public void setTown(String town) {
        this.town = town;
    }
    
    public String getProgrammeName() {
        return programmeName;
    }
    
    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }
    
    public String getStudyMode() {
        return studyMode;
    }
    
    public void setStudyMode(String studyMode) {
        this.studyMode = studyMode;
    }
    
    public String getCampus() {
        return campus;
    }
    
    public void setCampus(String campus) {
        this.campus = campus;
    }
    
    public String getIntakeYear() {
        return intakeYear;
    }
    
    public void setIntakeYear(String intakeYear) {
        this.intakeYear = intakeYear;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getReviewingOfficer() {
        return reviewingOfficer;
    }
    
    public void setReviewingOfficer(String reviewingOfficer) {
        this.reviewingOfficer = reviewingOfficer;
    }
    
    public String getOfficerNotes() {
        return officerNotes;
    }
    
    public void setOfficerNotes(String officerNotes) {
        this.officerNotes = officerNotes;
    }
    
    public String getDecisionMessage() {
        return decisionMessage;
    }
    
    public void setDecisionMessage(String decisionMessage) {
        this.decisionMessage = decisionMessage;
    }
    
    // Display helpers
    public String getStatusColor() {
        if (status == null) return "gray";
        switch (status.toUpperCase()) {
            case "SUBMITTED": return "blue";
            case "UNDER_REVIEW": return "yellow";
            case "ACCEPTED": return "green";
            case "REJECTED": return "red";
            default: return "gray";
        }
    }
    
    public String getStatusDisplay() {
        if (status == null) return "Draft";
        switch (status.toUpperCase()) {
            case "SUBMITTED": return "Application Received";
            case "UNDER_REVIEW": return "Under Review";
            case "ACCEPTED": return "Accepted";
            case "REJECTED": return "Rejected";
            default: return "Draft";
        }
    }
    
    public String getSubmittedAtFormatted() {
        if (submittedAt == null) return "-";
        return submittedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
    }
    
    public String getDecidedAtFormatted() {
        if (decidedAt == null) return "-";
        return decidedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
    }
    
    public String getStudyModeDisplay() {
        if (studyMode == null) return "";
        return studyMode.replace("_", "-").toLowerCase();
    }
    
    public String getSemesterDisplay() {
        if (semester == null) return "";
        return semester.charAt(0) + semester.substring(1).toLowerCase();
    }
    
    public String getIntakeDisplay() {
        return intakeYear + " " + getSemesterDisplay();
    }
    
    @Override
    public String toString() {
        return "ApplicationSummary{" +
                "applicationId=" + applicationId +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
