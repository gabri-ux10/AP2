package ke.ac.egerton.ams.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Utility Class
 * Provides singleton-pattern database connectivity using JDBC.
 * Reads configuration from db.properties file.
 */
public class DBConnection {
    
    private static DBConnection instance;
    private static Properties properties;
    private static boolean driverLoaded = false;
    
    // Configuration keys
    private static final String PROP_DRIVER = "db.driver";
    private static final String PROP_URL = "db.url";
    private static final String PROP_USERNAME = "db.username";
    private static final String PROP_PASSWORD = "db.password";
    
    /**
     * Private constructor - loads properties on first instantiation
     */
    private DBConnection() {
        loadProperties();
        loadDriver();
    }
    
    /**
     * Get singleton instance of DBConnection
     * @return DBConnection instance
     */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    /**
     * Load database properties from db.properties file
     */
    private void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("db.properties")) {
                if (input == null) {
                    throw new RuntimeException(
                        "Unable to find db.properties in classpath");
                }
                properties.load(input);
            } catch (IOException e) {
                throw new RuntimeException(
                    "Error loading db.properties: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Load JDBC driver class
     */
    private void loadDriver() {
        if (!driverLoaded) {
            try {
                String driver = properties.getProperty(PROP_DRIVER);
                if (driver == null || driver.trim().isEmpty()) {
                    throw new RuntimeException(
                        "Database driver not specified in db.properties");
                }
                Class.forName(driver);
                driverLoaded = true;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(
                    "MySQL JDBC Driver not found: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Get a new database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        String url = properties.getProperty(PROP_URL);
        String username = properties.getProperty(PROP_USERNAME);
        String password = properties.getProperty(PROP_PASSWORD);
        
        if (url == null || url.trim().isEmpty()) {
            throw new SQLException("Database URL not configured in db.properties");
        }
        
        return DriverManager.getConnection(url, username, password);
    }
    
    /**
     * Get a property value from db.properties
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key);
    }
    
    /**
     * Get a property value with default
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     */
    public String getProperty(String key, String defaultValue) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Close a database connection safely
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close multiple AutoCloseable resources safely
     * @param resources Resources to close (Connection, Statement, ResultSet, etc.)
     */
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    System.err.println("Error closing resource: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get database URL (for logging/debugging)
     * @return Database URL with password masked
     */
    public String getDatabaseInfo() {
        String url = properties.getProperty(PROP_URL);
        String username = properties.getProperty(PROP_USERNAME);
        return String.format("URL: %s, User: %s", url, username);
    }
}
