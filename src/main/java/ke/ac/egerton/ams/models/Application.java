package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing the master Application record.
 * Maps to: applications table
 */
public class Application implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum Status {
        DRAFT, SUBMITTED, UNDER_REVIEW, ACCEPTED, REJECTED
    }
    
    private Long id;
    private Long applicantAccountId;
    private Long intakeId;
    private Long reviewedBy;
    private String referenceNumber;
    private Status status;
    private int currentStep;
    private String officerNotes;
    private String decisionMessage;
    private LocalDateTime submittedAt;
    private LocalDateTime decidedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // For display purposes
    private String reviewingOfficerName;
    
    // Default constructor
    public Application() {
        this.status = Status.DRAFT;
        this.currentStep = 1;
    }
    
    // Constructor with essential fields
    public Application(Long applicantAccountId, Long intakeId) {
        this.applicantAccountId = applicantAccountId;
        this.intakeId = intakeId;
        this.status = Status.DRAFT;
        this.currentStep = 1;
    }
    
    // Full constructor
    public Application(Long id, Long applicantAccountId, Long intakeId,
                       Long reviewedBy, String referenceNumber, Status status,
                       int currentStep, String officerNotes, String decisionMessage,
                       LocalDateTime submittedAt, LocalDateTime decidedAt,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.applicantAccountId = applicantAccountId;
        this.intakeId = intakeId;
        this.reviewedBy = reviewedBy;
        this.referenceNumber = referenceNumber;
        this.status = status;
        this.currentStep = currentStep;
        this.officerNotes = officerNotes;
        this.decisionMessage = decisionMessage;
        this.submittedAt = submittedAt;
        this.decidedAt = decidedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    
    /**
     * Check if application has been submitted
     */
    public boolean isSubmitted() {
        return status != Status.DRAFT;
    }
    
    /**
     * Check if application can be edited
     */
    public boolean isEditable() {
        return status == Status.DRAFT;
    }
    
    /**
     * Check if decision has been made
     */
    public boolean hasDecision() {
        return status == Status.ACCEPTED || status == Status.REJECTED;
    }
    
    /**
     * Get status badge color for display
     */
    public String getStatusColor() {
        switch (status) {
            case SUBMITTED: return "blue";
            case UNDER_REVIEW: return "yellow";
            case ACCEPTED: return "green";
            case REJECTED: return "red";
            default: return "gray";
        }
    }
    
    /**
     * Get status display text
     */
    public String getStatusDisplay() {
        switch (status) {
            case SUBMITTED: return "Application Received";
            case UNDER_REVIEW: return "Under Review";
            case ACCEPTED: return "Congratulations! Accepted";
            case REJECTED: return "Not Selected";
            default: return "Draft";
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getApplicantAccountId() {
        return applicantAccountId;
    }
    
    public void setApplicantAccountId(Long applicantAccountId) {
        this.applicantAccountId = applicantAccountId;
    }
    
    public Long getIntakeId() {
        return intakeId;
    }
    
    public void setIntakeId(Long intakeId) {
        this.intakeId = intakeId;
    }
    
    public Long getReviewedBy() {
        return reviewedBy;
    }
    
    public void setReviewedBy(Long reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public void setStatus(String status) {
        this.status = Status.valueOf(status.toUpperCase());
    }
    
    public int getCurrentStep() {
        return currentStep;
    }
    
    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getReviewingOfficerName() {
        return reviewingOfficerName;
    }
    
    public void setReviewingOfficerName(String reviewingOfficerName) {
        this.reviewingOfficerName = reviewingOfficerName;
    }
    
    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", status=" + status +
                ", currentStep=" + currentStep +
                '}';
    }
}
