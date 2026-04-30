package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing uploaded documents (Step 4).
 * Maps to: applicant_documents table
 */
public class ApplicantDocuments implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long applicationId;
    private String kcseCertPath;       // Required
    private String nationalIdPath;      // Optional
    private String birthCertPath;       // Required
    private String summaryPdfPath;      // Auto-generated
    private LocalDateTime pdfGeneratedAt;
    private LocalDateTime createdAt;
    
    // Default constructor
    public ApplicantDocuments() {
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
    
    public String getKcseCertPath() {
        return kcseCertPath;
    }
    
    public void setKcseCertPath(String kcseCertPath) {
        this.kcseCertPath = kcseCertPath;
    }
    
    public String getNationalIdPath() {
        return nationalIdPath;
    }
    
    public void setNationalIdPath(String nationalIdPath) {
        this.nationalIdPath = nationalIdPath;
    }
    
    public String getBirthCertPath() {
        return birthCertPath;
    }
    
    public void setBirthCertPath(String birthCertPath) {
        this.birthCertPath = birthCertPath;
    }
    
    public String getSummaryPdfPath() {
        return summaryPdfPath;
    }
    
    public void setSummaryPdfPath(String summaryPdfPath) {
        this.summaryPdfPath = summaryPdfPath;
    }
    
    public LocalDateTime getPdfGeneratedAt() {
        return pdfGeneratedAt;
    }
    
    public void setPdfGeneratedAt(LocalDateTime pdfGeneratedAt) {
        this.pdfGeneratedAt = pdfGeneratedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Helper methods
    
    /**
     * Check if all required documents are uploaded
     */
    public boolean hasRequiredDocuments() {
        return kcseCertPath != null && !kcseCertPath.isEmpty() &&
               birthCertPath != null && !birthCertPath.isEmpty();
    }
    
    /**
     * Check if PDF has been generated
     */
    public boolean hasPdfGenerated() {
        return summaryPdfPath != null && !summaryPdfPath.isEmpty();
    }
    
    /**
     * Get filename from path
     */
    public String getKcseCertFilename() {
        return extractFilename(kcseCertPath);
    }
    
    public String getNationalIdFilename() {
        return extractFilename(nationalIdPath);
    }
    
    public String getBirthCertFilename() {
        return extractFilename(birthCertPath);
    }
    
    public String getSummaryPdfFilename() {
        return extractFilename(summaryPdfPath);
    }
    
    private String extractFilename(String path) {
        if (path == null || path.isEmpty()) return null;
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash == -1) lastSlash = path.lastIndexOf('\\');
        return lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
    }
    
    @Override
    public String toString() {
        return "ApplicantDocuments{" +
                "id=" + id +
                ", applicationId=" + applicationId +
                ", hasKcseCert=" + (kcseCertPath != null) +
                ", hasNationalId=" + (nationalIdPath != null) +
                ", hasBirthCert=" + (birthCertPath != null) +
                ", hasPdf=" + (summaryPdfPath != null) +
                '}';
    }
}
