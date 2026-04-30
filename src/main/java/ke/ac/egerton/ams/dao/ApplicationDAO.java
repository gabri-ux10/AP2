package ke.ac.egerton.ams.dao;

import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Application-related database operations.
 * Handles: applications, applicant_programme, applicant_academics, 
 *          subject_grades, applicant_extra tables
 */
public class ApplicationDAO {
    
    private final DBConnection dbConnection;
    
    public ApplicationDAO() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    // ========== APPLICATIONS ==========
    
    /**
     * Create a new application record
     * @param accountId Applicant account ID
     * @param intakeId Intake ID
     * @return Created Application with ID
     */
    public Application createApplication(Long accountId, Long intakeId) 
            throws SQLException {
        String sql = "INSERT INTO applications " +
                "(applicant_account_id, intake_id, status, current_step, created_at, updated_at) " +
                "VALUES (?, ?, 'DRAFT', 1, NOW(), NOW())";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, accountId);
            stmt.setLong(2, intakeId);
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Application app = new Application();
                    app.setId(rs.getLong(1));
                    app.setApplicantAccountId(accountId);
                    app.setIntakeId(intakeId);
                    app.setStatus(Application.Status.DRAFT);
                    app.setCurrentStep(1);
                    return app;
                }
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find application by ID
     */
    public Application findById(Long id) throws SQLException {
        String sql = "SELECT * FROM applications WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapApplication(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find active (draft) application for an account
     * @param accountId Applicant account ID
     * @return Active draft application or null
     */
    public Application findActiveByAccountId(Long accountId) throws SQLException {
        String sql = "SELECT * FROM applications " +
                     "WHERE applicant_account_id = ? AND status = 'DRAFT' " +
                     "ORDER BY created_at DESC LIMIT 1";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, accountId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapApplication(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find all applications for an account
     */
    public List<Application> findAllByAccountId(Long accountId) throws SQLException {
        String sql = "SELECT * FROM applications WHERE applicant_account_id = ? " +
                     "ORDER BY created_at DESC";
        
        List<Application> applications = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, accountId);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                applications.add(mapApplication(rs));
            }
            return applications;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Update application current step
     */
    public void updateCurrentStep(Long applicationId, int step) throws SQLException {
        String sql = "UPDATE applications SET current_step = ?, updated_at = NOW() WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, step);
            stmt.setLong(2, applicationId);
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Update application intake
     */
    public void updateIntake(Long applicationId, Long intakeId) throws SQLException {
        String sql = "UPDATE applications SET intake_id = ?, updated_at = NOW() WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, intakeId);
            stmt.setLong(2, applicationId);
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Generate reference number and submit application using stored procedure
     * @return Generated reference number
     */
    public String submitApplication(Long applicationId) throws SQLException {
        String sql = "{CALL sp_generate_reference(?)}";
        
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareCall(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("reference_number");
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== APPLICANT PROGRAMME (Step 2) ==========
    
    /**
     * Save applicant programme selection
     */
    public ApplicantProgramme saveProgramme(ApplicantProgramme programme) 
            throws SQLException {
        String sql = "INSERT INTO applicant_programme " +
                "(application_id, programme_id, intake_id, level, category, study_mode, campus, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setLong(1, programme.getApplicationId());
            stmt.setLong(2, programme.getProgrammeId());
            stmt.setLong(3, programme.getIntakeId());
            stmt.setString(4, programme.getLevel());
            stmt.setString(5, programme.getCategory());
            stmt.setString(6, programme.getStudyMode().name());
            stmt.setString(7, programme.getCampus());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    programme.setId(rs.getLong(1));
                }
            }
            return programme;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Update applicant programme selection
     */
    public void updateProgramme(ApplicantProgramme programme) throws SQLException {
        String sql = "UPDATE applicant_programme SET " +
                "programme_id = ?, intake_id = ?, study_mode = ? " +
                "WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setLong(1, programme.getProgrammeId());
            stmt.setLong(2, programme.getIntakeId());
            stmt.setString(3, programme.getStudyMode().name());
            stmt.setLong(4, programme.getApplicationId());
            
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Find programme by application ID
     */
    public ApplicantProgramme findProgrammeByApplicationId(Long applicationId) 
            throws SQLException {
        String sql = "SELECT ap.*, p.programme_name, i.intake_year, i.semester " +
                     "FROM applicant_programme ap " +
                     "JOIN programmes p ON p.id = ap.programme_id " +
                     "JOIN intakes i ON i.id = ap.intake_id " +
                     "WHERE ap.application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapApplicantProgramme(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Check if programme exists for application
     */
    public boolean programmeExists(Long applicationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applicant_programme WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== APPLICANT ACADEMICS (Step 3) ==========
    
    /**
     * Save academic information
     */
    public ApplicantAcademics saveAcademics(ApplicantAcademics academics) 
            throws SQLException {
        String sql = "INSERT INTO applicant_academics " +
                "(application_id, kcse_index_number, year_of_exam, school_name, " +
                "school_type, overall_grade, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setLong(1, academics.getApplicationId());
            stmt.setString(2, academics.getKcseIndexNumber());
            stmt.setInt(3, academics.getYearOfExam());
            stmt.setString(4, academics.getSchoolName());
            stmt.setString(5, academics.getSchoolType().name());
            stmt.setString(6, academics.getOverallGrade().getDisplay());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    academics.setId(rs.getLong(1));
                }
            }
            return academics;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Update academic information
     */
    public void updateAcademics(ApplicantAcademics academics) throws SQLException {
        String sql = "UPDATE applicant_academics SET " +
                "kcse_index_number = ?, year_of_exam = ?, school_name = ?, " +
                "school_type = ?, overall_grade = ? " +
                "WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, academics.getKcseIndexNumber());
            stmt.setInt(2, academics.getYearOfExam());
            stmt.setString(3, academics.getSchoolName());
            stmt.setString(4, academics.getSchoolType().name());
            stmt.setString(5, academics.getOverallGrade().getDisplay());
            stmt.setLong(6, academics.getApplicationId());
            
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Find academics by application ID
     */
    public ApplicantAcademics findAcademicsByApplicationId(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM applicant_academics WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapAcademics(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Check if academics exists
     */
    public boolean academicsExists(Long applicationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applicant_academics WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== SUBJECT GRADES ==========
    
    /**
     * Save subject grades (delete existing first)
     */
    public void saveSubjectGrades(Long applicationId, Long academicId, 
                                  List<SubjectGrade> grades) throws SQLException {
        // First delete existing grades
        deleteSubjectGrades(applicationId);
        
        String sql = "INSERT INTO subject_grades " +
                "(application_id, academic_id, subject_name, grade, subject_order) " +
                "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            for (SubjectGrade grade : grades) {
                stmt.setLong(1, applicationId);
                stmt.setLong(2, academicId);
                stmt.setString(3, grade.getSubjectName());
                stmt.setString(4, grade.getGrade());
                stmt.setInt(5, grade.getSubjectOrder());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Delete subject grades for application
     */
    public void deleteSubjectGrades(Long applicationId) throws SQLException {
        String sql = "DELETE FROM subject_grades WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Find subject grades by application ID
     */
    public List<SubjectGrade> findSubjectGradesByApplicationId(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM subject_grades WHERE application_id = ? " +
                     "ORDER BY subject_order";
        
        List<SubjectGrade> grades = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                grades.add(mapSubjectGrade(rs));
            }
            return grades;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== APPLICANT EXTRA (Step 6) ==========
    
    /**
     * Save extra information
     */
    public ApplicantExtra saveExtra(ApplicantExtra extra) throws SQLException {
        String sql = "INSERT INTO applicant_extra " +
                "(application_id, has_disability, disability_description, created_at) " +
                "VALUES (?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setLong(1, extra.getApplicationId());
            stmt.setBoolean(2, extra.isHasDisability());
            stmt.setString(3, extra.getDisabilityDescription());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    extra.setId(rs.getLong(1));
                }
            }
            return extra;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Update extra information
     */
    public void updateExtra(ApplicantExtra extra) throws SQLException {
        String sql = "UPDATE applicant_extra SET " +
                "has_disability = ?, disability_description = ? " +
                "WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setBoolean(1, extra.isHasDisability());
            stmt.setString(2, extra.getDisabilityDescription());
            stmt.setLong(3, extra.getApplicationId());
            
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Find extra by application ID
     */
    public ApplicantExtra findExtraByApplicationId(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM applicant_extra WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapExtra(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Check if extra exists
     */
    public boolean extraExists(Long applicationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applicant_extra WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== MAPPING HELPERS ==========
    
    private Application mapApplication(ResultSet rs) throws SQLException {
        Application app = new Application();
        app.setId(rs.getLong("id"));
        app.setApplicantAccountId(rs.getLong("applicant_account_id"));
        app.setIntakeId(rs.getLong("intake_id"));
        
        long reviewedBy = rs.getLong("reviewed_by");
        if (!rs.wasNull()) {
            app.setReviewedBy(reviewedBy);
        }
        
        app.setReferenceNumber(rs.getString("reference_number"));
        app.setStatus(rs.getString("status"));
        app.setCurrentStep(rs.getInt("current_step"));
        app.setOfficerNotes(rs.getString("officer_notes"));
        app.setDecisionMessage(rs.getString("decision_message"));
        
        Timestamp submittedAt = rs.getTimestamp("submitted_at");
        if (submittedAt != null) {
            app.setSubmittedAt(submittedAt.toLocalDateTime());
        }
        
        Timestamp decidedAt = rs.getTimestamp("decided_at");
        if (decidedAt != null) {
            app.setDecidedAt(decidedAt.toLocalDateTime());
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            app.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            app.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return app;
    }
    
    private ApplicantProgramme mapApplicantProgramme(ResultSet rs) throws SQLException {
        ApplicantProgramme prog = new ApplicantProgramme();
        prog.setId(rs.getLong("id"));
        prog.setApplicationId(rs.getLong("application_id"));
        prog.setProgrammeId(rs.getLong("programme_id"));
        prog.setIntakeId(rs.getLong("intake_id"));
        prog.setLevel(rs.getString("level"));
        prog.setCategory(rs.getString("category"));
        prog.setStudyMode(rs.getString("study_mode"));
        prog.setCampus(rs.getString("campus"));
        
        try {
            prog.setProgrammeName(rs.getString("programme_name"));
            prog.setIntakeYear(rs.getString("intake_year"));
            prog.setIntakeSemester(rs.getString("semester"));
        } catch (SQLException e) {
            // Columns may not exist in all queries
        }
        
        return prog;
    }
    
    private ApplicantAcademics mapAcademics(ResultSet rs) throws SQLException {
        ApplicantAcademics acad = new ApplicantAcademics();
        acad.setId(rs.getLong("id"));
        acad.setApplicationId(rs.getLong("application_id"));
        acad.setKcseIndexNumber(rs.getString("kcse_index_number"));
        acad.setYearOfExam(rs.getInt("year_of_exam"));
        acad.setSchoolName(rs.getString("school_name"));
        acad.setSchoolType(rs.getString("school_type"));
        acad.setOverallGrade(rs.getString("overall_grade"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            acad.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return acad;
    }
    
    private SubjectGrade mapSubjectGrade(ResultSet rs) throws SQLException {
        SubjectGrade grade = new SubjectGrade();
        grade.setId(rs.getLong("id"));
        grade.setApplicationId(rs.getLong("application_id"));
        grade.setAcademicId(rs.getLong("academic_id"));
        grade.setSubjectName(rs.getString("subject_name"));
        grade.setGrade(rs.getString("grade"));
        grade.setSubjectOrder(rs.getInt("subject_order"));
        return grade;
    }
    
    private ApplicantExtra mapExtra(ResultSet rs) throws SQLException {
        ApplicantExtra extra = new ApplicantExtra();
        extra.setId(rs.getLong("id"));
        extra.setApplicationId(rs.getLong("application_id"));
        extra.setHasDisability(rs.getBoolean("has_disability"));
        extra.setDisabilityDescription(rs.getString("disability_description"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            extra.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return extra;
    }
}
