package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import ke.ac.egerton.ams.dao.ApplicantDAO;
import ke.ac.egerton.ams.dao.ApplicationDAO;
import ke.ac.egerton.ams.dao.ProgrammeDAO;
import ke.ac.egerton.ams.models.*;
import ke.ac.egerton.ams.util.Validator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Personal Information Servlet (Step 1)
 * Handles applicant personal details collection.
 * URL: /apply/personal
 */
public class PersonalInfoServlet extends HttpServlet {
    
    private ApplicantDAO applicantDAO;
    private ApplicationDAO applicationDAO;
    private ProgrammeDAO programmeDAO;
    
    // Kenya counties list
    private static final String[] COUNTIES = {
        "Baringo", "Bomet", "Bungoma", "Busia", "Elgeyo-Marakwet",
        "Embu", "Garissa", "Homa Bay", "Isiolo", "Kajiado",
        "Kakamega", "Kericho", "Kiambu", "Kilifi", "Kirinyaga",
        "Kisii", "Kisumu", "Kitui", "Kwale", "Laikipia",
        "Lamu", "Machakos", "Makueni", "Mandera", "Marsabit",
        "Meru", "Migori", "Mombasa", "Murang'a", "Nairobi",
        "Nakuru", "Nandi", "Narok", "Nyamira", "Nyandarua",
        "Nyeri", "Samburu", "Siaya", "Taita-Taveta", "Tana River",
        "Tharaka-Nithi", "Trans-Nzoia", "Turkana", "Uasin Gishu",
        "Vihiga", "Wajir", "West Pokot"
    };
    
    @Override
    public void init() throws ServletException {
        applicantDAO = new ApplicantDAO();
        applicationDAO = new ApplicationDAO();
        programmeDAO = new ProgrammeDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        ApplicationBean appBean = (ApplicationBean) session.getAttribute("applicationBean");
        
        if (appBean == null) {
            appBean = new ApplicationBean();
            session.setAttribute("applicationBean", appBean);
        }
        
        // Set data for form
        request.setAttribute("personal", appBean.getPersonal());
        request.setAttribute("counties", COUNTIES);
        request.setAttribute("currentStep", 1);
        
        request.getRequestDispatcher("/WEB-INF/views/step1_personal.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        ApplicationBean appBean = (ApplicationBean) session.getAttribute("applicationBean");
        ApplicantAccount account = (ApplicantAccount) session.getAttribute("applicantAccount");
        
        if (appBean == null || account == null) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }
        
        // Collect form parameters
        Map<String, String> params = new HashMap<>();
        params.put("firstName", request.getParameter("firstName"));
        params.put("middleName", request.getParameter("middleName"));
        params.put("lastName", request.getParameter("lastName"));
        params.put("dateOfBirth", request.getParameter("dateOfBirth"));
        params.put("gender", request.getParameter("gender"));
        params.put("nationality", request.getParameter("nationality"));
        params.put("nationalId", request.getParameter("nationalId"));
        params.put("birthCertNumber", request.getParameter("birthCertNumber"));
        params.put("phoneNumber", request.getParameter("phoneNumber"));
        params.put("email", request.getParameter("email"));
        params.put("county", request.getParameter("county"));
        params.put("town", request.getParameter("town"));
        
        // Validate
        Map<String, String> errors = Validator.validateStep1(params);
        
        // Handle photo upload
        Part photoPart = request.getPart("photo");
        String photoPath = null;
        
        if (photoPart != null && photoPart.getSize() > 0) {
            // Validate file type
            String contentType = photoPart.getContentType();
            if (!contentType.startsWith("image/")) {
                errors.put("photo", "Please upload an image file (JPG or PNG)");
            } else if (photoPart.getSize() > 2 * 1024 * 1024) {
                errors.put("photo", "Photo must be less than 2 MB");
            } else {
                try {
                    photoPath = saveUploadedFile(photoPart, "photos", account.getId());
                } catch (Exception e) {
                    errors.put("photo", "Failed to upload photo: " + e.getMessage());
                }
            }
        } else if (appBean.getPersonal().getPhotoPath() == null) {
            errors.put("photo", "Passport photo is required");
        } else {
            // Keep existing photo
            photoPath = appBean.getPersonal().getPhotoPath();
        }
        
        if (!errors.isEmpty()) {
            // Populate personal object for form re-display
            ApplicantPersonal personal = new ApplicantPersonal();
            populatePersonal(personal, params);
            if (photoPath != null) {
                personal.setPhotoPath(photoPath);
            }
            
            request.setAttribute("personal", personal);
            request.setAttribute("errors", errors);
            request.setAttribute("counties", COUNTIES);
            request.setAttribute("currentStep", 1);
            
            request.getRequestDispatcher("/WEB-INF/views/step1_personal.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            // Create or get application record
            Long applicationId = (Long) session.getAttribute("applicationId");
            
            if (applicationId == null) {
                // Get first open intake for default
                List<Intake> intakes = programmeDAO.getAllOpenIntakes();
                if (intakes.isEmpty()) {
                    throw new ServletException("No open intakes available");
                }
                
                Application app = applicationDAO.createApplication(
                        account.getId(), intakes.get(0).getId());
                applicationId = app.getId();
                session.setAttribute("applicationId", applicationId);
                appBean.setApplicationId(applicationId);
            }
            
            // Populate personal object
            ApplicantPersonal personal = appBean.getPersonal();
            if (personal == null) {
                personal = new ApplicantPersonal();
            }
            personal.setApplicationId(applicationId);
            populatePersonal(personal, params);
            personal.setPhotoPath(photoPath);
            
            // Save to database
            if (applicantDAO.personalExists(applicationId)) {
                applicantDAO.updatePersonal(personal);
            } else {
                personal = applicantDAO.savePersonal(personal);
            }
            
            // Update session
            appBean.setPersonal(personal);
            appBean.completeStep(1);
            session.setAttribute("applicationBean", appBean);
            session.setAttribute("currentStep", 2);
            
            // Update application current step
            applicationDAO.updateCurrentStep(applicationId, 2);
            
            // Redirect to step 2 (PRG pattern)
            response.sendRedirect(request.getContextPath() + "/apply/programme");
            
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.setAttribute("personal", appBean.getPersonal());
            request.setAttribute("counties", COUNTIES);
            request.setAttribute("currentStep", 1);
            request.getRequestDispatcher("/WEB-INF/views/step1_personal.jsp")
                   .forward(request, response);
        }
    }
    
    private void populatePersonal(ApplicantPersonal personal, Map<String, String> params) {
        personal.setFirstName(Validator.sanitize(params.get("firstName"), 100));
        personal.setMiddleName(Validator.sanitize(params.get("middleName"), 100));
        personal.setLastName(Validator.sanitize(params.get("lastName"), 100));
        
        String dobStr = params.get("dateOfBirth");
        if (dobStr != null && !dobStr.isEmpty()) {
            personal.setDateOfBirth(LocalDate.parse(dobStr));
        }
        
        String gender = params.get("gender");
        if (gender != null && !gender.isEmpty()) {
            personal.setGender(gender);
        }
        
        personal.setNationality(Validator.sanitize(params.get("nationality"), 100));
        
        String nationalId = params.get("nationalId");
        if (nationalId != null && !nationalId.trim().isEmpty()) {
            personal.setNationalId(Validator.sanitize(nationalId, 8));
        }
        
        personal.setBirthCertNumber(Validator.sanitize(params.get("birthCertNumber"), 7));
        personal.setPhoneNumber(Validator.formatPhoneNumber(params.get("phoneNumber")));
        personal.setEmail(Validator.sanitize(params.get("email"), 255));
        personal.setCounty(Validator.sanitize(params.get("county"), 100));
        personal.setTown(Validator.sanitize(params.get("town"), 100));
    }
    
    private String saveUploadedFile(Part filePart, String subDir, Long userId) 
            throws IOException {
        String uploadDir = getServletContext().getRealPath("/uploads/" + subDir);
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String originalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String fileName = "user_" + userId + "_" + System.currentTimeMillis() + extension;
        
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(filePart.getInputStream(), filePath);
        
        return "/uploads/" + subDir + "/" + fileName;
    }
}
