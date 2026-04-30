package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.ApplicationDAO;
import ke.ac.egerton.ams.dao.DocumentDAO;
import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.PDFGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * Document Upload Servlet (Step 4)
 * Handles document uploads and PDF generation.
 * URL: /apply/documents
 */
public class DocumentServlet extends HttpServlet {
    
    private ApplicationDAO applicationDAO;
    private DocumentDAO documentDAO;
    
    @Override
    public void init() throws ServletException {
        applicationDAO = new ApplicationDAO();
        documentDAO = new DocumentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        ApplicationBean appBean = (ApplicationBean) session.getAttribute("applicationBean");
        
        if (appBean == null) {
            response.sendRedirect(request.getContextPath() + "/apply/personal");
            return;
        }
        
        request.setAttribute("documents", appBean.getDocuments());
        request.setAttribute("currentStep", 4);
        
        request.getRequestDispatcher("/WEB-INF/views/step4_documents.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        ApplicationBean appBean = (ApplicationBean) session.getAttribute("applicationBean");
        Long applicationId = (Long) session.getAttribute("applicationId");
        ApplicantAccount account = (ApplicantAccount) session.getAttribute("applicantAccount");
        
        if (appBean == null || applicationId == null) {
            response.sendRedirect(request.getContextPath() + "/apply/personal");
            return;
        }
        
        java.util.Map<String, String> errors = new java.util.HashMap<>();
        
        ApplicantDocuments docs = appBean.getDocuments();
        if (docs == null) {
            docs = new ApplicantDocuments();
            docs.setApplicationId(applicationId);
        }
        
        try {
            // Ensure document record exists
            documentDAO.ensureDocumentRecord(applicationId);
            
            // Handle KCSE Certificate upload
            Part kcsePart = request.getPart("kcseCert");
            if (kcsePart != null && kcsePart.getSize() > 0) {
                String error = validateDocument(kcsePart, 5 * 1024 * 1024);
                if (error != null) {
                    errors.put("kcseCert", error);
                } else {
                    String path = saveUploadedFile(kcsePart, "kcse", account.getId());
                    docs.setKcseCertPath(path);
                    documentDAO.updateKcseCertPath(applicationId, path);
                }
            } else if (docs.getKcseCertPath() == null || docs.getKcseCertPath().isEmpty()) {
                errors.put("kcseCert", "KCSE Certificate is required");
            }
            
            // Handle National ID upload (optional)
            Part nationalIdPart = request.getPart("nationalId");
            if (nationalIdPart != null && nationalIdPart.getSize() > 0) {
                String error = validateDocument(nationalIdPart, 2 * 1024 * 1024);
                if (error != null) {
                    errors.put("nationalId", error);
                } else {
                    String path = saveUploadedFile(nationalIdPart, "ids", account.getId());
                    docs.setNationalIdPath(path);
                    documentDAO.updateNationalIdPath(applicationId, path);
                }
            }
            
            // Handle Birth Certificate upload
            Part birthCertPart = request.getPart("birthCert");
            if (birthCertPart != null && birthCertPart.getSize() > 0) {
                String error = validateDocument(birthCertPart, 2 * 1024 * 1024);
                if (error != null) {
                    errors.put("birthCert", error);
                } else {
                    String path = saveUploadedFile(birthCertPart, "birth", account.getId());
                    docs.setBirthCertPath(path);
                    documentDAO.updateBirthCertPath(applicationId, path);
                }
            } else if (docs.getBirthCertPath() == null || docs.getBirthCertPath().isEmpty()) {
                errors.put("birthCert", "Birth Certificate is required");
            }
            
        } catch (Exception e) {
            errors.put("upload", "Failed to upload file: " + e.getMessage());
        }
        
        if (!errors.isEmpty()) {
            request.setAttribute("documents", docs);
            request.setAttribute("errors", errors);
            request.setAttribute("currentStep", 4);
            request.getRequestDispatcher("/WEB-INF/views/step4_documents.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            // Generate PDF summary
            if (docs.hasRequiredDocuments()) {
                String pdfPath = generatePdfSummary(appBean, applicationId, account.getId());
                docs.setSummaryPdfPath(pdfPath);
                documentDAO.updateSummaryPdfPath(applicationId, pdfPath);
            }
            
            // Update session
            appBean.setDocuments(docs);
            appBean.completeStep(4);
            session.setAttribute("applicationBean", appBean);
            session.setAttribute("currentStep", 5);
            
            // Update application current step
            applicationDAO.updateCurrentStep(applicationId, 5);
            
            // Redirect to step 5
            response.sendRedirect(request.getContextPath() + "/apply/guardian");
            
        } catch (Exception e) {
            request.setAttribute("error", "Failed to process documents: " + e.getMessage());
            request.setAttribute("documents", docs);
            request.setAttribute("currentStep", 4);
            request.getRequestDispatcher("/WEB-INF/views/step4_documents.jsp")
                   .forward(request, response);
        }
    }
    
    private String validateDocument(Part filePart, long maxSize) {
        String contentType = filePart.getContentType();
        
        // Check file type
        boolean validType = contentType.equals("application/pdf") ||
                           contentType.equals("image/jpeg") ||
                           contentType.equals("image/jpg") ||
                           contentType.equals("image/png");
        
        if (!validType) {
            return "File must be PDF, JPG, or PNG";
        }
        
        // Check file size
        if (filePart.getSize() > maxSize) {
            return "File size must be less than " + (maxSize / (1024 * 1024)) + " MB";
        }
        
        return null;
    }
    
    private String saveUploadedFile(Part filePart, String subDir, Long userId) 
            throws IOException {
        String uploadDir = getServletContext().getRealPath("/uploads/" + subDir);
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String originalName = Paths.get(filePart.getSubmittedFileName())
                                   .getFileName().toString();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String fileName = "user_" + userId + "_" + System.currentTimeMillis() + extension;
        
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(filePart.getInputStream(), filePath);
        
        return "/uploads/" + subDir + "/" + fileName;
    }
    
    private String generatePdfSummary(ApplicationBean appBean, Long applicationId, Long userId) 
            throws IOException, SQLException {
        
        String pdfDir = getServletContext().getRealPath("/pdf");
        File dir = new File(pdfDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String fileName = "application_" + applicationId + "_" + System.currentTimeMillis() + ".pdf";
        String filePath = pdfDir + File.separator + fileName;
        
        // Generate PDF
        PDFGenerator generator = new PDFGenerator();
        generator.generateApplicationSummary(appBean, filePath);
        
        return "/pdf/" + fileName;
    }
}
