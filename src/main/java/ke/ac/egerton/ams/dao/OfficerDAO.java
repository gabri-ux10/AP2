package ke.ac.egerton.ams.dao;

import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Officer-related database operations.
 * Handles: officers table, application review stored procedures,
 *          vw_application_summary, vw_applicant_status, vw_dashboard_counts views
 */
public class OfficerDAO {
    
    private final DBConnection dbConnection;
    
    public OfficerDAO() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    // ========== OFFICER AUTHENTICATION ==========
    
    /**
     * Find officer by email
     */
    public Officer findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM officers WHERE email = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapOfficer(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find officer by ID
     */
    public Officer findById(Long id) throws SQLException {
        String sql = "SELECT * FROM officers WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapOfficer(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Authenticate officer with email and password
     * @return Officer if authenticated, null otherwise
     */
    public Officer authenticate(String email, String password) throws SQLException {
        Officer officer = findByEmail(email);
        if (officer == null) {
            return null;
        }
        
        // Check if locked
        if (officer.isLocked()) {
            return null;
        }
        
        // Check if active
        if (!officer.isActive()) {
            return null;
        }
        
        // Verify password
        if (BCrypt.checkpw(password, officer.getPasswordHash())) {
            // Reset login attempts on success
            resetLoginAttempts(officer.getId());
            return officer;
        } else {
            // Increment login attempts
            incrementLoginAttempts(officer.getId());
            return null;
        }
    }
    
    /**
     * Increment login attempts (called on failed login)
     */
    public void incrementLoginAttempts(Long officerId) throws SQLException {
        String sql = "UPDATE officers SET login_attempts = login_attempts + 1, " +
                     "locked_until = CASE WHEN login_attempts >= 2 THEN DATE_ADD(NOW(), INTERVAL 15 MINUTE) ELSE locked_until END " +
                     "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, officerId);
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Reset login attempts (called on successful login)
     */
    public void resetLoginAttempts(Long officerId) throws SQLException {
        String sql = "UPDATE officers SET login_attempts = 0, locked_until = NULL WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, officerId);
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    // ========== DASHBOARD ==========
    
    /**
     * Get dashboard counts from view
     */
    public DashboardCounts getDashboardCounts() throws SQLException {
        String sql = "SELECT * FROM vw_dashboard_counts";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                DashboardCounts counts = new DashboardCounts();
                counts.setTotalApplications(rs.getInt("total_applications"));
                counts.setPendingReview(rs.getInt("pending_review"));
                counts.setUnderReview(rs.getInt("under_review"));
                counts.setAccepted(rs.getInt("accepted"));
                counts.setRejected(rs.getInt("rejected"));
                counts.setDrafts(rs.getInt("drafts"));
                return counts;
            }
            return new DashboardCounts();
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Get all application summaries for dashboard
     * @param status Filter by status (null for all)
     * @param search Search term for name or reference
     * @param offset Pagination offset
     * @param limit Pagination limit
     */
    public List<ApplicationSummary> getApplicationSummaries(String status, 
            String search, int offset, int limit) throws SQLException {
        
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM vw_application_summary WHERE status != 'DRAFT' ");
        
        if (status != null && !status.isEmpty() && !status.equals("ALL")) {
            sql.append("AND status = ? ");
        }
        if (search != null && !search.isEmpty()) {
            sql.append("AND (full_name LIKE ? OR reference_number LIKE ?) ");
        }
        sql.append("ORDER BY submitted_at DESC LIMIT ? OFFSET ?");
        
        List<ApplicationSummary> summaries = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            int paramIndex = 1;
            if (status != null && !status.isEmpty() && !status.equals("ALL")) {
                stmt.setString(paramIndex++, status);
            }
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }
            stmt.setInt(paramIndex++, limit);
            stmt.setInt(paramIndex, offset);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                summaries.add(mapApplicationSummary(rs));
            }
            return summaries;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Count applications for pagination
     */
    public int countApplications(String status, String search) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM vw_application_summary WHERE status != 'DRAFT' ");
        
        if (status != null && !status.isEmpty() && !status.equals("ALL")) {
            sql.append("AND status = ? ");
        }
        if (search != null && !search.isEmpty()) {
            sql.append("AND (full_name LIKE ? OR reference_number LIKE ?) ");
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            int paramIndex = 1;
            if (status != null && !status.isEmpty() && !status.equals("ALL")) {
                stmt.setString(paramIndex++, status);
            }
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex, searchPattern);
            }
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }

    /**
     * Get dashboard submission trend grouped by month.
     */
    public List<MonthlyTrendPoint> getMonthlySubmissionTrend(int limit) throws SQLException {
        String sql =
            "SELECT month_label, total_submitted " +
            "FROM (" +
            "   SELECT DATE_FORMAT(submitted_at, '%b %Y') AS month_label, " +
            "          DATE_FORMAT(submitted_at, '%Y-%m') AS month_key, " +
            "          COUNT(*) AS total_submitted " +
            "   FROM applications " +
            "   WHERE status != 'DRAFT' AND submitted_at IS NOT NULL " +
            "   GROUP BY DATE_FORMAT(submitted_at, '%Y-%m'), DATE_FORMAT(submitted_at, '%b %Y') " +
            "   ORDER BY month_key DESC " +
            "   LIMIT ? " +
            ") trend " +
            "ORDER BY month_key ASC";

        List<MonthlyTrendPoint> trendPoints = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            rs = stmt.executeQuery();

            while (rs.next()) {
                MonthlyTrendPoint point = new MonthlyTrendPoint();
                point.setMonthLabel(rs.getString("month_label"));
                point.setTotalSubmitted(rs.getInt("total_submitted"));
                trendPoints.add(point);
            }
            return trendPoints;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }

    /**
     * Get dashboard demographic totals.
     */
    public DemographicStats getDemographicStats() throws SQLException {
        String sql =
            "SELECT " +
            "   COALESCE(SUM(CASE WHEN p.gender = 'MALE' THEN 1 ELSE 0 END), 0) AS male_count, " +
            "   COALESCE(SUM(CASE WHEN p.gender = 'FEMALE' THEN 1 ELSE 0 END), 0) AS female_count, " +
            "   COALESCE(SUM(CASE WHEN COALESCE(e.has_disability, FALSE) = TRUE THEN 1 ELSE 0 END), 0) AS disabled_count " +
            "FROM applications a " +
            "JOIN applicant_personal p ON a.id = p.application_id " +
            "LEFT JOIN applicant_extra e ON a.id = e.application_id " +
            "WHERE a.status != 'DRAFT'";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            DemographicStats stats = new DemographicStats();
            if (rs.next()) {
                stats.setMaleCount(rs.getInt("male_count"));
                stats.setFemaleCount(rs.getInt("female_count"));
                stats.setDisabledCount(rs.getInt("disabled_count"));
            }
            return stats;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }

    /**
     * Get top applicant origin counties.
     */
    public List<CountyApplicationStat> getTopCountyStats(int limit) throws SQLException {
        String sql =
            "SELECT p.county, COUNT(*) AS applicant_count " +
            "FROM applications a " +
            "JOIN applicant_personal p ON a.id = p.application_id " +
            "WHERE a.status != 'DRAFT' " +
            "GROUP BY p.county " +
            "ORDER BY applicant_count DESC, p.county ASC " +
            "LIMIT ?";

        List<CountyApplicationStat> countyStats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            rs = stmt.executeQuery();

            while (rs.next()) {
                CountyApplicationStat stat = new CountyApplicationStat();
                stat.setCounty(rs.getString("county"));
                stat.setApplicantCount(rs.getInt("applicant_count"));
                countyStats.add(stat);
            }
            return countyStats;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }

    /**
     * Get programme application totals including decision split.
     */
    public List<ProgrammeApplicationStat> getProgrammeApplicationStats() throws SQLException {
        String sql =
            "SELECT pr.programme_name, s.school_name, " +
            "       COUNT(a.id) AS total_applied, " +
            "       COALESCE(SUM(CASE WHEN a.status = 'ACCEPTED' THEN 1 ELSE 0 END), 0) AS accepted, " +
            "       COALESCE(SUM(CASE WHEN a.status = 'REJECTED' THEN 1 ELSE 0 END), 0) AS rejected, " +
            "       COALESCE(SUM(CASE WHEN a.status IN ('SUBMITTED', 'UNDER_REVIEW') THEN 1 ELSE 0 END), 0) AS pending " +
            "FROM programmes pr " +
            "JOIN schools s ON pr.school_id = s.id " +
            "LEFT JOIN applicant_programme ap ON ap.programme_id = pr.id " +
            "LEFT JOIN applications a ON a.id = ap.application_id AND a.status != 'DRAFT' " +
            "GROUP BY pr.id, pr.programme_name, s.school_name " +
            "ORDER BY total_applied DESC, pr.programme_name ASC";

        List<ProgrammeApplicationStat> programmeStats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ProgrammeApplicationStat stat = new ProgrammeApplicationStat();
                stat.setProgrammeName(rs.getString("programme_name"));
                stat.setSchoolName(rs.getString("school_name"));
                stat.setTotalApplied(rs.getInt("total_applied"));
                stat.setAccepted(rs.getInt("accepted"));
                stat.setRejected(rs.getInt("rejected"));
                stat.setPending(rs.getInt("pending"));
                programmeStats.add(stat);
            }
            return programmeStats;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== APPLICATION REVIEW ==========
    
    /**
     * Get application summary by ID
     */
    public ApplicationSummary getApplicationSummaryById(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM vw_application_summary WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapApplicationSummary(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Open application for review (calls sp_open_review)
     */
    public void openReview(Long applicationId, Long officerId) throws SQLException {
        String sql = "{CALL sp_open_review(?, ?)}";
        
        Connection conn = null;
        CallableStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareCall(sql);
            stmt.setLong(1, applicationId);
            stmt.setLong(2, officerId);
            stmt.execute();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Accept application (calls sp_accept_application)
     */
    public void acceptApplication(Long applicationId, Long officerId, 
                                  String notes, String message) throws SQLException {
        String sql = "{CALL sp_accept_application(?, ?, ?, ?)}";
        
        Connection conn = null;
        CallableStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareCall(sql);
            stmt.setLong(1, applicationId);
            stmt.setLong(2, officerId);
            stmt.setString(3, notes);
            stmt.setString(4, message);
            stmt.execute();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Reject application (calls sp_reject_application)
     */
    public void rejectApplication(Long applicationId, Long officerId,
                                  String notes, String message) throws SQLException {
        String sql = "{CALL sp_reject_application(?, ?, ?, ?)}";
        
        Connection conn = null;
        CallableStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareCall(sql);
            stmt.setLong(1, applicationId);
            stmt.setLong(2, officerId);
            stmt.setString(3, notes);
            stmt.setString(4, message);
            stmt.execute();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    // ========== APPLICANT STATUS ==========
    
    /**
     * Get applicant status by account ID (for applicant status page)
     */
    public ApplicationSummary getApplicantStatus(Long accountId) throws SQLException {
        String sql = "SELECT * FROM vw_applicant_status " +
                     "WHERE login_email = (SELECT email FROM applicant_accounts WHERE id = ?) " +
                     "ORDER BY submitted_at DESC LIMIT 1";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, accountId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                ApplicationSummary summary = new ApplicationSummary();
                summary.setApplicationId(rs.getLong("application_id"));
                summary.setReferenceNumber(rs.getString("reference_number"));
                summary.setStatus(rs.getString("status"));
                summary.setFullName(rs.getString("full_name"));
                summary.setProgrammeName(rs.getString("programme_name"));
                summary.setStudyMode(rs.getString("study_mode"));
                summary.setIntakeYear(rs.getString("intake_year"));
                summary.setDecisionMessage(rs.getString("decision_message"));
                
                Timestamp submittedAt = rs.getTimestamp("submitted_at");
                if (submittedAt != null) {
                    summary.setSubmittedAt(submittedAt.toLocalDateTime());
                }
                
                Timestamp decidedAt = rs.getTimestamp("decided_at");
                if (decidedAt != null) {
                    summary.setDecidedAt(decidedAt.toLocalDateTime());
                }
                
                return summary;
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Get subject grades for review (from vw_subject_grades)
     */
    public List<SubjectGrade> getSubjectGradesForReview(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM vw_subject_grades WHERE application_id = ?";
        
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
                SubjectGrade grade = new SubjectGrade();
                grade.setApplicationId(rs.getLong("application_id"));
                grade.setSubjectOrder(rs.getInt("subject_order"));
                grade.setSubjectName(rs.getString("subject_name"));
                grade.setGrade(rs.getString("grade"));
                grades.add(grade);
            }
            return grades;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== MAPPING HELPERS ==========
    
    private Officer mapOfficer(ResultSet rs) throws SQLException {
        Officer officer = new Officer();
        officer.setId(rs.getLong("id"));
        officer.setFullName(rs.getString("full_name"));
        officer.setEmail(rs.getString("email"));
        officer.setPasswordHash(rs.getString("password_hash"));
        officer.setLoginAttempts(rs.getInt("login_attempts"));
        
        Timestamp lockedUntil = rs.getTimestamp("locked_until");
        if (lockedUntil != null) {
            officer.setLockedUntil(lockedUntil.toLocalDateTime());
        }
        
        officer.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            officer.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return officer;
    }
    
    private ApplicationSummary mapApplicationSummary(ResultSet rs) throws SQLException {
        ApplicationSummary summary = new ApplicationSummary();
        summary.setApplicationId(rs.getLong("application_id"));
        summary.setReferenceNumber(rs.getString("reference_number"));
        summary.setStatus(rs.getString("status"));
        summary.setCurrentStep(rs.getInt("current_step"));
        
        Timestamp submittedAt = rs.getTimestamp("submitted_at");
        if (submittedAt != null) {
            summary.setSubmittedAt(submittedAt.toLocalDateTime());
        }
        
        Timestamp decidedAt = rs.getTimestamp("decided_at");
        if (decidedAt != null) {
            summary.setDecidedAt(decidedAt.toLocalDateTime());
        }
        
        Timestamp startedAt = rs.getTimestamp("started_at");
        if (startedAt != null) {
            summary.setStartedAt(startedAt.toLocalDateTime());
        }
        
        summary.setFullName(rs.getString("full_name"));
        summary.setEmail(rs.getString("email"));
        summary.setPhoneNumber(rs.getString("phone_number"));
        summary.setNationalId(rs.getString("national_id"));
        summary.setBirthCertNumber(rs.getString("birth_cert_number"));
        summary.setCounty(rs.getString("county"));
        summary.setTown(rs.getString("town"));
        summary.setProgrammeName(rs.getString("programme_name"));
        summary.setStudyMode(rs.getString("study_mode"));
        summary.setCampus(rs.getString("campus"));
        summary.setIntakeYear(rs.getString("intake_year"));
        summary.setSemester(rs.getString("semester"));
        summary.setReviewingOfficer(rs.getString("reviewing_officer"));
        summary.setOfficerNotes(rs.getString("officer_notes"));
        summary.setDecisionMessage(rs.getString("decision_message"));
        
        return summary;
    }
}
