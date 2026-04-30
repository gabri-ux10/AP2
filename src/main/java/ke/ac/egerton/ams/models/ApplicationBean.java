package ke.ac.egerton.ams.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Session Bean that holds all application data across wizard steps.
 * This bean is stored in HttpSession and accumulates data from Steps 1-7.
 */
public class ApplicationBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Application identifiers
    private Long applicationId;
    private Long applicantAccountId;
    private String referenceNumber;
    
    // Step 1: Personal Information
    private ApplicantPersonal personal;
    
    // Step 2: Programme Selection
    private ApplicantProgramme programme;
    
    // Step 3: Academic Background
    private ApplicantAcademics academics;
    private List<SubjectGrade> subjectGrades;
    
    // Step 4: Documents
    private ApplicantDocuments documents;
    
    // Step 5: Guardians
    private List<ApplicantGuardian> guardians;
    
    // Step 6: Extra Information
    private ApplicantExtra extra;
    
    // Wizard state
    private int currentStep;
    private int highestCompletedStep;
    
    // Default constructor
    public ApplicationBean() {
        this.currentStep = 1;
        this.highestCompletedStep = 0;
        this.personal = new ApplicantPersonal();
        this.programme = new ApplicantProgramme();
        this.academics = new ApplicantAcademics();
        this.subjectGrades = new ArrayList<>();
        this.documents = new ApplicantDocuments();
        this.guardians = new ArrayList<>();
        this.extra = new ApplicantExtra();
    }
    
    // Constructor with account ID
    public ApplicationBean(Long applicantAccountId) {
        this();
        this.applicantAccountId = applicantAccountId;
    }
    
    // Getters and Setters
    public Long getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
    
    public Long getApplicantAccountId() {
        return applicantAccountId;
    }
    
    public void setApplicantAccountId(Long applicantAccountId) {
        this.applicantAccountId = applicantAccountId;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public ApplicantPersonal getPersonal() {
        return personal;
    }
    
    public void setPersonal(ApplicantPersonal personal) {
        this.personal = personal;
    }
    
    public ApplicantProgramme getProgramme() {
        return programme;
    }
    
    public void setProgramme(ApplicantProgramme programme) {
        this.programme = programme;
    }
    
    public ApplicantAcademics getAcademics() {
        return academics;
    }
    
    public void setAcademics(ApplicantAcademics academics) {
        this.academics = academics;
    }
    
    public List<SubjectGrade> getSubjectGrades() {
        return subjectGrades;
    }
    
    public void setSubjectGrades(List<SubjectGrade> subjectGrades) {
        this.subjectGrades = subjectGrades;
    }
    
    public void addSubjectGrade(SubjectGrade grade) {
        if (this.subjectGrades == null) {
            this.subjectGrades = new ArrayList<>();
        }
        this.subjectGrades.add(grade);
    }
    
    public void clearSubjectGrades() {
        if (this.subjectGrades != null) {
            this.subjectGrades.clear();
        }
    }
    
    public ApplicantDocuments getDocuments() {
        return documents;
    }
    
    public void setDocuments(ApplicantDocuments documents) {
        this.documents = documents;
    }
    
    public List<ApplicantGuardian> getGuardians() {
        return guardians;
    }
    
    public void setGuardians(List<ApplicantGuardian> guardians) {
        this.guardians = guardians;
    }
    
    public void addGuardian(ApplicantGuardian guardian) {
        if (this.guardians == null) {
            this.guardians = new ArrayList<>();
        }
        this.guardians.add(guardian);
    }
    
    public void clearGuardians() {
        if (this.guardians != null) {
            this.guardians.clear();
        }
    }
    
    public ApplicantGuardian getPrimaryGuardian() {
        if (guardians != null) {
            for (ApplicantGuardian g : guardians) {
                if (g.isPrimary()) return g;
            }
        }
        return null;
    }
    
    public ApplicantGuardian getSecondaryGuardian() {
        if (guardians != null) {
            for (ApplicantGuardian g : guardians) {
                if (!g.isPrimary()) return g;
            }
        }
        return null;
    }
    
    public ApplicantExtra getExtra() {
        return extra;
    }
    
    public void setExtra(ApplicantExtra extra) {
        this.extra = extra;
    }
    
    public int getCurrentStep() {
        return currentStep;
    }
    
    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
        if (currentStep > highestCompletedStep + 1) {
            this.highestCompletedStep = currentStep - 1;
        }
    }
    
    public int getHighestCompletedStep() {
        return highestCompletedStep;
    }
    
    public void setHighestCompletedStep(int highestCompletedStep) {
        this.highestCompletedStep = highestCompletedStep;
    }
    
    public void completeStep(int step) {
        if (step > highestCompletedStep) {
            highestCompletedStep = step;
        }
    }
    
    // Validation helpers
    
    /**
     * Check if Step 1 (Personal) is complete
     */
    public boolean isStep1Complete() {
        return personal != null &&
               personal.getFirstName() != null && !personal.getFirstName().isEmpty() &&
               personal.getLastName() != null && !personal.getLastName().isEmpty() &&
               personal.getDateOfBirth() != null &&
               personal.getGender() != null &&
               personal.getBirthCertNumber() != null &&
               personal.getPhoneNumber() != null &&
               personal.getEmail() != null &&
               personal.getCounty() != null &&
               personal.getTown() != null;
    }
    
    /**
     * Check if Step 2 (Programme) is complete
     */
    public boolean isStep2Complete() {
        return programme != null &&
               programme.getProgrammeId() != null &&
               programme.getIntakeId() != null &&
               programme.getStudyMode() != null;
    }
    
    /**
     * Check if Step 3 (Academics) is complete
     */
    public boolean isStep3Complete() {
        return academics != null &&
               academics.getKcseIndexNumber() != null &&
               academics.getYearOfExam() > 0 &&
               academics.getSchoolName() != null &&
               academics.getSchoolType() != null &&
               academics.getOverallGrade() != null &&
               subjectGrades != null &&
               subjectGrades.size() >= 7 && subjectGrades.size() <= 8;
    }
    
    /**
     * Check if Step 4 (Documents) is complete
     */
    public boolean isStep4Complete() {
        return documents != null &&
               documents.hasRequiredDocuments();
    }
    
    /**
     * Check if Step 5 (Guardians) is complete
     */
    public boolean isStep5Complete() {
        return guardians != null &&
               !guardians.isEmpty() &&
               getPrimaryGuardian() != null;
    }
    
    /**
     * Check if Step 6 (Extra) is complete
     */
    public boolean isStep6Complete() {
        if (extra == null) return false;
        if (extra.isHasDisability()) {
            return extra.getDisabilityDescription() != null && 
                   !extra.getDisabilityDescription().isEmpty();
        }
        return true;
    }
    
    /**
     * Check if all steps are complete (ready for submission)
     */
    public boolean isReadyForSubmission() {
        return isStep1Complete() && isStep2Complete() && isStep3Complete() &&
               isStep4Complete() && isStep5Complete() && isStep6Complete();
    }
    
    /**
     * Get completion percentage
     */
    public int getCompletionPercentage() {
        int completed = 0;
        if (isStep1Complete()) completed++;
        if (isStep2Complete()) completed++;
        if (isStep3Complete()) completed++;
        if (isStep4Complete()) completed++;
        if (isStep5Complete()) completed++;
        if (isStep6Complete()) completed++;
        return (completed * 100) / 6;
    }
    
    /**
     * Get applicant full name
     */
    public String getFullName() {
        return personal != null ? personal.getFullName() : "";
    }
    
    /**
     * Get programme name
     */
    public String getProgrammeName() {
        return programme != null ? programme.getProgrammeName() : "";
    }
    
    @Override
    public String toString() {
        return "ApplicationBean{" +
                "applicationId=" + applicationId +
                ", currentStep=" + currentStep +
                ", highestCompletedStep=" + highestCompletedStep +
                ", isReady=" + isReadyForSubmission() +
                '}';
    }
}
