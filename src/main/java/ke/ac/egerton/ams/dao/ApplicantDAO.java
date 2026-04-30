package ke.ac.egerton.ams.dao;

import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Applicant-related database operations.
 * Handles: applicant_accounts, applicant_personal tables
 */
public class ApplicantDAO {
    
    private final DBConnection dbConnection;
    
    public ApplicantDAO() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    // ========== APPLICANT ACCOUNTS ==========
    
    /**
     * Create a new applicant account
     * @param email Applicant email
     * @param password Plain text password (will be hashed)
     * @return Created ApplicantAccount with ID, or null on failure
     */
    public ApplicantAccount createAccount(String email, String password) 
            throws SQLException {
        String sql = "INSERT INTO applicant_accounts (email, password_hash, is_active, created_at) " +
                     "VALUES (?, ?, 1, NOW())";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
            stmt.setString(1, email);
            stmt.setString(2, passwordHash);
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    ApplicantAccount account = new ApplicantAccount();
                    account.setId(rs.getLong(1));
                    account.setEmail(email);
                    account.setPasswordHash(passwordHash);
                    account.setActive(true);
                    account.setCreatedAt(LocalDateTime.now());
                    return account;
                }
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find applicant account by email
     * @param email Email to search
     * @return ApplicantAccount or null if not found
     */
    public ApplicantAccount findByEmail(String email) throws SQLException {
        String sql = "SELECT id, email, password_hash, is_active, created_at, last_login " +
                     "FROM applicant_accounts WHERE email = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapApplicantAccount(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find applicant account by ID
     * @param id Account ID
     * @return ApplicantAccount or null if not found
     */
    public ApplicantAccount findById(Long id) throws SQLException {
        String sql = "SELECT id, email, password_hash, is_active, created_at, last_login " +
                     "FROM applicant_accounts WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapApplicantAccount(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Authenticate applicant with email and password
     * @param email Applicant email
     * @param password Plain text password
     * @return ApplicantAccount if authenticated, null otherwise
     */
    public ApplicantAccount authenticate(String email, String password) 
            throws SQLException {
        ApplicantAccount account = findByEmail(email);
        if (account != null && account.isActive()) {
            if (BCrypt.checkpw(password, account.getPasswordHash())) {
                updateLastLogin(account.getId());
                return account;
            }
        }
        return null;
    }
    
    /**
     * Update last login timestamp
     * @param accountId Account ID
     */
    public void updateLastLogin(Long accountId) throws SQLException {
        String sql = "UPDATE applicant_accounts SET last_login = NOW() WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, accountId);
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Check if email already exists
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applicant_accounts WHERE email = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== APPLICANT PERSONAL ==========
    
    /**
     * Save applicant personal information (Step 1)
     * @param personal Personal info to save
     * @return Saved ApplicantPersonal with ID
     */
    public ApplicantPersonal savePersonal(ApplicantPersonal personal) 
            throws SQLException {
        String sql = "INSERT INTO applicant_personal " +
                "(application_id, first_name, middle_name, last_name, date_of_birth, " +
                "gender, nationality, national_id, birth_cert_number, phone_number, " +
                "email, county, town, photo_path, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setLong(1, personal.getApplicationId());
            stmt.setString(2, personal.getFirstName());
            stmt.setString(3, personal.getMiddleName());
            stmt.setString(4, personal.getLastName());
            stmt.setDate(5, Date.valueOf(personal.getDateOfBirth()));
            stmt.setString(6, personal.getGender().name());
            stmt.setString(7, personal.getNationality());
            stmt.setString(8, personal.getNationalId());
            stmt.setString(9, personal.getBirthCertNumber());
            stmt.setString(10, personal.getPhoneNumber());
            stmt.setString(11, personal.getEmail());
            stmt.setString(12, personal.getCounty());
            stmt.setString(13, personal.getTown());
            stmt.setString(14, personal.getPhotoPath());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    personal.setId(rs.getLong(1));
                }
            }
            return personal;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Update applicant personal information
     * @param personal Personal info to update
     */
    public void updatePersonal(ApplicantPersonal personal) throws SQLException {
        String sql = "UPDATE applicant_personal SET " +
                "first_name = ?, middle_name = ?, last_name = ?, date_of_birth = ?, " +
                "gender = ?, nationality = ?, national_id = ?, birth_cert_number = ?, " +
                "phone_number = ?, email = ?, county = ?, town = ?, photo_path = ? " +
                "WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, personal.getFirstName());
            stmt.setString(2, personal.getMiddleName());
            stmt.setString(3, personal.getLastName());
            stmt.setDate(4, Date.valueOf(personal.getDateOfBirth()));
            stmt.setString(5, personal.getGender().name());
            stmt.setString(6, personal.getNationality());
            stmt.setString(7, personal.getNationalId());
            stmt.setString(8, personal.getBirthCertNumber());
            stmt.setString(9, personal.getPhoneNumber());
            stmt.setString(10, personal.getEmail());
            stmt.setString(11, personal.getCounty());
            stmt.setString(12, personal.getTown());
            stmt.setString(13, personal.getPhotoPath());
            stmt.setLong(14, personal.getApplicationId());
            
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Find personal info by application ID
     * @param applicationId Application ID
     * @return ApplicantPersonal or null if not found
     */
    public ApplicantPersonal findPersonalByApplicationId(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM applicant_personal WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapApplicantPersonal(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Check if personal info exists for application
     * @param applicationId
     */
    public boolean personalExists(Long applicationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applicant_personal WHERE application_id = ?";
        
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
    
    private ApplicantAccount mapApplicantAccount(ResultSet rs) throws SQLException {
        ApplicantAccount account = new ApplicantAccount();
        account.setId(rs.getLong("id"));
        account.setEmail(rs.getString("email"));
        account.setPasswordHash(rs.getString("password_hash"));
        account.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            account.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            account.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        return account;
    }
    
    private ApplicantPersonal mapApplicantPersonal(ResultSet rs) throws SQLException {
        ApplicantPersonal personal = new ApplicantPersonal();
        personal.setId(rs.getLong("id"));
        personal.setApplicationId(rs.getLong("application_id"));
        personal.setFirstName(rs.getString("first_name"));
        personal.setMiddleName(rs.getString("middle_name"));
        personal.setLastName(rs.getString("last_name"));
        
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            personal.setDateOfBirth(dob.toLocalDate());
        }
        
        String gender = rs.getString("gender");
        if (gender != null) {
            personal.setGender(gender);
        }
        
        personal.setNationality(rs.getString("nationality"));
        personal.setNationalId(rs.getString("national_id"));
        personal.setBirthCertNumber(rs.getString("birth_cert_number"));
        personal.setPhoneNumber(rs.getString("phone_number"));
        personal.setEmail(rs.getString("email"));
        personal.setCounty(rs.getString("county"));
        personal.setTown(rs.getString("town"));
        personal.setPhotoPath(rs.getString("photo_path"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            personal.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return personal;
    }
}
