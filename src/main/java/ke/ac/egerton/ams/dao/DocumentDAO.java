package ke.ac.egerton.ams.dao;

import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Data Access Object for Document-related database operations.
 * Handles: applicant_documents table
 */
public class DocumentDAO {
    
    private final DBConnection dbConnection;
    
    public DocumentDAO() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    /**
     * Create document record
     */
    public ApplicantDocuments createDocuments(ApplicantDocuments docs) 
            throws SQLException {
        String sql = "INSERT INTO applicant_documents " +
                "(application_id, kcse_cert_path, national_id_path, birth_cert_path, created_at) " +
                "VALUES (?, ?, ?, ?, NOW())";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setLong(1, docs.getApplicationId());
            stmt.setString(2, docs.getKcseCertPath());
            stmt.setString(3, docs.getNationalIdPath());
            stmt.setString(4, docs.getBirthCertPath());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    docs.setId(rs.getLong(1));
                }
            }
            return docs;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Update document paths
     */
    public void updateDocuments(ApplicantDocuments docs) throws SQLException {
        String sql = "UPDATE applicant_documents SET " +
                "kcse_cert_path = ?, national_id_path = ?, birth_cert_path = ? " +
                "WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, docs.getKcseCertPath());
            stmt.setString(2, docs.getNationalIdPath());
            stmt.setString(3, docs.getBirthCertPath());
            stmt.setLong(4, docs.getApplicationId());
            
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Update KCSE certificate path
     */
    public void updateKcseCertPath(Long applicationId, String path) throws SQLException {
        String sql = "UPDATE applicant_documents SET kcse_cert_path = ? WHERE application_id = ?";
        executePathUpdate(sql, applicationId, path);
    }
    
    /**
     * Update National ID path
     */
    public void updateNationalIdPath(Long applicationId, String path) throws SQLException {
        String sql = "UPDATE applicant_documents SET national_id_path = ? WHERE application_id = ?";
        executePathUpdate(sql, applicationId, path);
    }
    
    /**
     * Update Birth Certificate path
     */
    public void updateBirthCertPath(Long applicationId, String path) throws SQLException {
        String sql = "UPDATE applicant_documents SET birth_cert_path = ? WHERE application_id = ?";
        executePathUpdate(sql, applicationId, path);
    }
    
    /**
     * Update summary PDF path
     */
    public void updateSummaryPdfPath(Long applicationId, String path) throws SQLException {
        String sql = "UPDATE applicant_documents SET summary_pdf_path = ?, pdf_generated_at = NOW() " +
                     "WHERE application_id = ?";
        executePathUpdate(sql, applicationId, path);
    }
    
    private void executePathUpdate(String sql, Long applicationId, String path) 
            throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, path);
            stmt.setLong(2, applicationId);
            stmt.executeUpdate();
        } finally {
            DBConnection.closeResources(stmt, conn);
        }
    }
    
    /**
     * Find documents by application ID
     */
    public ApplicantDocuments findByApplicationId(Long applicationId) 
            throws SQLException {
        String sql = "SELECT * FROM applicant_documents WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, applicationId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapDocuments(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Check if documents record exists
     */
    public boolean documentsExist(Long applicationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applicant_documents WHERE application_id = ?";
        
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
     * Create empty document record if not exists
     */
    public void ensureDocumentRecord(Long applicationId) throws SQLException {
        if (!documentsExist(applicationId)) {
            String sql = "INSERT INTO applicant_documents (application_id, kcse_cert_path, birth_cert_path, created_at) " +
                         "VALUES (?, '', '', NOW())";
            
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
    }
    
    private ApplicantDocuments mapDocuments(ResultSet rs) throws SQLException {
        ApplicantDocuments docs = new ApplicantDocuments();
        docs.setId(rs.getLong("id"));
        docs.setApplicationId(rs.getLong("application_id"));
        docs.setKcseCertPath(rs.getString("kcse_cert_path"));
        docs.setNationalIdPath(rs.getString("national_id_path"));
        docs.setBirthCertPath(rs.getString("birth_cert_path"));
        docs.setSummaryPdfPath(rs.getString("summary_pdf_path"));
        
        Timestamp pdfGenAt = rs.getTimestamp("pdf_generated_at");
        if (pdfGenAt != null) {
            docs.setPdfGeneratedAt(pdfGenAt.toLocalDateTime());
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            docs.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return docs;
    }
}
