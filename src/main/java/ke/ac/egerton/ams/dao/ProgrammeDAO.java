package ke.ac.egerton.ams.dao;

import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Programme-related database operations.
 * Handles: schools, programmes, intakes tables
 */
public class ProgrammeDAO {
    
    private final DBConnection dbConnection;
    
    public ProgrammeDAO() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    // ========== SCHOOLS ==========
    
    /**
     * Get all active schools
     * @return List of active schools
     */
    public List<School> getAllActiveSchools() throws SQLException {
        String sql = "SELECT id, school_name, code, is_active FROM schools " +
                     "WHERE is_active = 1 ORDER BY school_name";
        
        List<School> schools = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                schools.add(mapSchool(rs));
            }
            return schools;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find school by ID
     */
    public School findSchoolById(Long id) throws SQLException {
        String sql = "SELECT id, school_name, code, is_active FROM schools WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapSchool(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== PROGRAMMES ==========
    
    /**
     * Get all active undergraduate degree programmes
     * @return List of programmes
     */
    public List<Programme> getAllActiveProgrammes() throws SQLException {
        String sql = "SELECT p.id, p.school_id, p.programme_name, p.code, " +
                     "p.level, p.category, p.is_active, s.school_name " +
                     "FROM programmes p " +
                     "JOIN schools s ON s.id = p.school_id " +
                     "WHERE p.is_active = 1 AND p.level = 'UNDERGRADUATE' " +
                     "AND p.category = 'DEGREE' " +
                     "ORDER BY p.programme_name";
        
        List<Programme> programmes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                programmes.add(mapProgramme(rs));
            }
            return programmes;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Get programmes by school ID
     * @param schoolId School ID
     * @return List of programmes for that school
     */
    public List<Programme> getProgrammesBySchool(Long schoolId) throws SQLException {
        String sql = "SELECT p.id, p.school_id, p.programme_name, p.code, " +
                     "p.level, p.category, p.is_active, s.school_name " +
                     "FROM programmes p " +
                     "JOIN schools s ON s.id = p.school_id " +
                     "WHERE p.is_active = 1 AND p.school_id = ? " +
                     "AND p.level = 'UNDERGRADUATE' AND p.category = 'DEGREE' " +
                     "ORDER BY p.programme_name";
        
        List<Programme> programmes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, schoolId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                programmes.add(mapProgramme(rs));
            }
            return programmes;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find programme by ID
     */
    public Programme findProgrammeById(Long id) throws SQLException {
        String sql = "SELECT p.id, p.school_id, p.programme_name, p.code, " +
                     "p.level, p.category, p.is_active, s.school_name " +
                     "FROM programmes p " +
                     "JOIN schools s ON s.id = p.school_id " +
                     "WHERE p.id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapProgramme(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== INTAKES ==========
    
    /**
     * Get all open intakes
     * @return List of open intakes
     */
    public List<Intake> getAllOpenIntakes() throws SQLException {
        String sql = "SELECT DISTINCT i.id, i.programme_id, i.intake_year, " +
                     "i.semester, i.open_date, i.close_date, i.is_open " +
                     "FROM intakes i " +
                     "WHERE i.is_open = 1 " +
                     "ORDER BY i.intake_year DESC, i.semester";
        
        List<Intake> intakes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                intakes.add(mapIntake(rs));
            }
            return intakes;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Get unique intake years (for dropdown)
     */
    public List<String> getAvailableIntakeYears() throws SQLException {
        String sql = "SELECT DISTINCT intake_year FROM intakes " +
                     "WHERE is_open = 1 ORDER BY intake_year DESC";
        
        List<String> years = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                years.add(rs.getString("intake_year"));
            }
            return years;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Get open intakes for a specific programme
     * @param programmeId Programme ID
     * @return List of open intakes for that programme
     */
    public List<Intake> getOpenIntakesForProgramme(Long programmeId) throws SQLException {
        String sql = "SELECT i.id, i.programme_id, i.intake_year, " +
                     "i.semester, i.open_date, i.close_date, i.is_open, " +
                     "p.programme_name " +
                     "FROM intakes i " +
                     "JOIN programmes p ON p.id = i.programme_id " +
                     "WHERE i.programme_id = ? AND i.is_open = 1 " +
                     "ORDER BY i.intake_year DESC, i.semester";
        
        List<Intake> intakes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, programmeId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Intake intake = mapIntake(rs);
                intake.setProgrammeName(rs.getString("programme_name"));
                intakes.add(intake);
            }
            return intakes;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find intake by ID
     */
    public Intake findIntakeById(Long id) throws SQLException {
        String sql = "SELECT i.id, i.programme_id, i.intake_year, " +
                     "i.semester, i.open_date, i.close_date, i.is_open, " +
                     "p.programme_name " +
                     "FROM intakes i " +
                     "JOIN programmes p ON p.id = i.programme_id " +
                     "WHERE i.id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                Intake intake = mapIntake(rs);
                intake.setProgrammeName(rs.getString("programme_name"));
                return intake;
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Find intake by programme, year, and semester
     */
    public Intake findIntake(Long programmeId, String intakeYear, String semester) 
            throws SQLException {
        String sql = "SELECT i.id, i.programme_id, i.intake_year, " +
                     "i.semester, i.open_date, i.close_date, i.is_open " +
                     "FROM intakes i " +
                     "WHERE i.programme_id = ? AND i.intake_year = ? AND i.semester = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, programmeId);
            stmt.setString(2, intakeYear);
            stmt.setString(3, semester.toUpperCase());
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapIntake(rs);
            }
            return null;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    // ========== MAPPING HELPERS ==========
    
    private School mapSchool(ResultSet rs) throws SQLException {
        School school = new School();
        school.setId(rs.getLong("id"));
        school.setSchoolName(rs.getString("school_name"));
        school.setCode(rs.getString("code"));
        school.setActive(rs.getBoolean("is_active"));
        return school;
    }
    
    private Programme mapProgramme(ResultSet rs) throws SQLException {
        Programme programme = new Programme();
        programme.setId(rs.getLong("id"));
        programme.setSchoolId(rs.getLong("school_id"));
        programme.setProgrammeName(rs.getString("programme_name"));
        programme.setCode(rs.getString("code"));
        programme.setLevel(rs.getString("level"));
        programme.setCategory(rs.getString("category"));
        programme.setActive(rs.getBoolean("is_active"));
        
        try {
            programme.setSchoolName(rs.getString("school_name"));
        } catch (SQLException e) {
            // Column may not exist in all queries
        }
        
        return programme;
    }
    
    private Intake mapIntake(ResultSet rs) throws SQLException {
        Intake intake = new Intake();
        intake.setId(rs.getLong("id"));
        intake.setProgrammeId(rs.getLong("programme_id"));
        intake.setIntakeYear(rs.getString("intake_year"));
        intake.setSemester(rs.getString("semester"));
        
        Date openDate = rs.getDate("open_date");
        if (openDate != null) {
            intake.setOpenDate(openDate.toLocalDate());
        }
        
        Date closeDate = rs.getDate("close_date");
        if (closeDate != null) {
            intake.setCloseDate(closeDate.toLocalDate());
        }
        
        intake.setOpen(rs.getBoolean("is_open"));
        return intake;
    }
}
