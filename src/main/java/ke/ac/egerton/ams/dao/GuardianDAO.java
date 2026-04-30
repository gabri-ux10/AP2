package ke.ac.egerton.ams.dao;

import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Guardian-related database operations.
 * Handles: applicant_guardians table
 */
public class GuardianDAO {
    
    private final DBConnection dbConnection;
    
    public GuardianDAO() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    /**
     * Save a guardian record
     */
    public ApplicantGuardian saveGuardian(ApplicantGuardian guardian) 
            throws SQLException {
        String sql = "INSERT INTO applicant_guardians " +
                "(application_id, full_name, relationship, phone_number, is_primary, created_at) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setLong(1, guardian.getApplicationId());
            stmt.setString(2, guardian.getFullName());
            stmt.setString(3, guardian.getRelationship().name());
            stmt.setString(4, guardian.getPhoneNumber());
            stmt.setBoolean(5, guardian.isPrimary());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    guardian.setId(rs.getLong(1));
                }
            }
            return guardian;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Save all guardians (delete existing first)
     */
    public void saveGuardians(Long applicationId, List<ApplicantGuardian> guardians) 
            throws SQLException {
        // Delete existing guardians
        deleteGuardians(applicationId);
        
        // Insert new ones
        for (ApplicantGuardian guardian : guardians) {
            guardian.setApplicationId(applicationId);
            saveGuardian(guardian);
        }
    }
    
    /**
     * Update guardian
     */
    public void updateGuardian(ApplicantGuardian guardian) throws SQLException {
        String sql = "UPDATE applicant_guardians SET " +
                "full_name = ?, relationship = ?, phone_number = ? " +
                "WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, guardian.getFullName());
            stmt.setString(2, guardian.getRelationship().name());
            stmt.setString(3, guardian.getPhoneNumber());
            stmt.setLong(4, guardian.getId());
            
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Delete all guardians for application
     */
    public void deleteGuardians(Long applicationId) throws SQLException {
        String sql = "DELETE FROM applicant_guardians WHERE application_id = ?";
        
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
     * Find all guardians by application ID
     */
    public List<ApplicantGuardian> findByApplicationId(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM applicant_guardians WHERE application_id = ? " +
                     "ORDER BY is_primary DESC";
        
        List<ApplicantGuardian> guardians = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                guardians.add(mapGuardian(rs));
            }
            return guardians;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find primary guardian
     */
    public ApplicantGuardian findPrimaryGuardian(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM applicant_guardians " +
                     "WHERE application_id = ? AND is_primary = 1";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapGuardian(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find secondary guardian
     */
    public ApplicantGuardian findSecondaryGuardian(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM applicant_guardians " +
                     "WHERE application_id = ? AND is_primary = 0";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapGuardian(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Check if guardians exist
     */
    public boolean guardiansExist(Long applicationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applicant_guardians WHERE application_id = ?";
        
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
    
    /**
     * Get guardian count
     */
    public int getGuardianCount(Long applicationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applicant_guardians WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    private ApplicantGuardian mapGuardian(ResultSet rs) throws SQLException {
        ApplicantGuardian guardian = new ApplicantGuardian();
        guardian.setId(rs.getLong("id"));
        guardian.setApplicationId(rs.getLong("application_id"));
        guardian.setFullName(rs.getString("full_name"));
        guardian.setRelationship(rs.getString("relationship"));
        guardian.setPhoneNumber(rs.getString("phone_number"));
        guardian.setPrimary(rs.getBoolean("is_primary"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            guardian.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return guardian;
    }
}
