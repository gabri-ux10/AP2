package ke.ac.egerton.ams.util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * File Upload Handler Utility
 * Handles file upload validation and storage.
 */
public class FileUploadHandler {
    
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_EXTENSIONS = 
            Arrays.asList("pdf", "jpg", "jpeg", "png");
    private static final List<String> ALLOWED_CONTENT_TYPES = 
            Arrays.asList("application/pdf", "image/jpeg", "image/jpg", "image/png");
    
    /**
     * Validate uploaded file
     * @param part File part from request
     * @param maxSize Maximum allowed size in bytes
     * @return Error message or null if valid
     */
    public static String validateFile(Part part, long maxSize) {
        if (part == null || part.getSize() == 0) {
            return "No file uploaded";
        }
        
        // Check size
        if (part.getSize() > maxSize) {
            return "File size exceeds maximum allowed size of " + 
                   (maxSize / (1024 * 1024)) + " MB";
        }
        
        // Check content type
        String contentType = part.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            return "Invalid file type. Allowed: PDF, JPG, PNG";
        }
        
        // Check extension
        String fileName = getFileName(part);
        String extension = getFileExtension(fileName).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return "Invalid file extension. Allowed: pdf, jpg, jpeg, png";
        }
        
        return null;
    }
    
    /**
     * Save uploaded file to specified directory
     * @param part File part from request
     * @param uploadDir Upload directory path
     * @param userId User ID for file naming
     * @param prefix File name prefix
     * @return Relative path to saved file
     */
    public static String saveFile(Part part, String uploadDir, Long userId, String prefix) 
            throws IOException {
        
        // Create directory if not exists
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // Get original filename and extension
        String originalFileName = getFileName(part);
        String extension = getFileExtension(originalFileName);
        
        // Generate unique filename
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String newFileName = prefix + "_" + userId + "_" + 
                             System.currentTimeMillis() + "_" + uniqueId + "." + extension;
        
        // Save file
        Path filePath = Paths.get(uploadDir, newFileName);
        Files.copy(part.getInputStream(), filePath);
        
        return newFileName;
    }
    
    /**
     * Delete file from storage
     * @param filePath Path to file
     * @return true if deleted successfully
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get file name from Part
     */
    public static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim()
                           .replace("\"", "");
            }
        }
        return "";
    }
    
    /**
     * Get file extension
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1 || lastDot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDot + 1);
    }
    
    /**
     * Get content type for extension
     */
    public static String getContentType(String extension) {
        switch (extension.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                return "application/octet-stream";
        }
    }
    
    /**
     * Check if file exists
     */
    public static boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Get file size in human readable format
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        }
    }
}
