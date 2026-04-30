package ke.ac.egerton.ams.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ke.ac.egerton.ams.dao.ApplicantDAO;
import ke.ac.egerton.ams.dao.ApplicationDAO;
import ke.ac.egerton.ams.models.ApplicantAccount;
import ke.ac.egerton.ams.models.Application;
import ke.ac.egerton.ams.models.ApplicationBean;
import ke.ac.egerton.ams.util.Validator;

/**
 * Authentication Servlet
 * Handles applicant registration, login, and session creation.
 * URL: /auth
 */
public class AuthServlet extends HttpServlet {
    
    private ApplicantDAO applicantDAO;
    private ApplicationDAO applicationDAO;
    
    @Override
    public void init() throws ServletException {
        applicantDAO = new ApplicantDAO();
        applicationDAO = new ApplicationDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "login";
        }
        
        // Check for remember email cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberEmail".equals(cookie.getName())) {
                    request.setAttribute("rememberedEmail", cookie.getValue());
                    break;
                }
            }
        }
        
        if ("register".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("register".equals(action)) {
            handleRegistration(request, response);
        } else {
            handleLogin(request, response);
        }
    }
    
    private void handleRegistration(HttpServletRequest request, 
                                    HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        Map<String, String> errors = new HashMap<>();
        
        // Validate email
        String emailError = Validator.validateEmail(email);
        if (emailError != null) {
            errors.put("email", emailError);
        }
        
        // Validate password
        if (password == null || password.length() < 8) {
            errors.put("password", "Password must be at least 8 characters");
        }
        
        // Confirm password match
        if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Passwords do not match");
        }
        
        // Check if email exists
        if (errors.isEmpty()) {
            try {
                if (applicantDAO.emailExists(email)) {
                    errors.put("email", "An account with this email already exists");
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Database error. Please try again.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                       .forward(request, response);
                return;
            }
        }
        
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            // Create account
            ApplicantAccount account = applicantDAO.createAccount(email, password);
            
            if (account != null) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("applicantAccount", account);
                session.setAttribute("applicantId", account.getId());
                
                // Create application bean
                ApplicationBean appBean = new ApplicationBean(account.getId());
                appBean.getPersonal().setEmail(email);
                session.setAttribute("applicationBean", appBean);
                session.setAttribute("currentStep", 1);
                
                // Redirect to step 1
                response.sendRedirect(request.getContextPath() + "/apply/personal");
            } else {
                request.setAttribute("error", "Failed to create account. Please try again.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                       .forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
        }
    }
    
    private void handleLogin(HttpServletRequest request, 
                             HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        Map<String, String> errors = new HashMap<>();
        
        // Validate inputs
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email is required");
        }
        if (password == null || password.isEmpty()) {
            errors.put("password", "Password is required");
        }
        
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            // Authenticate
            ApplicantAccount account = applicantDAO.authenticate(email, password);
            
            if (account != null) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("applicantAccount", account);
                session.setAttribute("applicantId", account.getId());
                
                // Handle remember me cookie
                if ("on".equals(remember)) {
                    Cookie rememberCookie = new Cookie("rememberEmail", email);
                    rememberCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                    rememberCookie.setHttpOnly(true);
                    rememberCookie.setPath("/");
                    response.addCookie(rememberCookie);
                } else {
                    // Remove cookie if exists
                    Cookie rememberCookie = new Cookie("rememberEmail", "");
                    rememberCookie.setMaxAge(0);
                    rememberCookie.setPath("/");
                    response.addCookie(rememberCookie);
                }
                
                // Check for existing application
                Application existingApp = applicationDAO.findActiveByAccountId(account.getId());
                
                if (existingApp != null) {
                    // Resume existing application
                    session.setAttribute("applicationId", existingApp.getId());
                    session.setAttribute("currentStep", existingApp.getCurrentStep());
                    
                    // Load application bean
                    ApplicationBean appBean = loadApplicationBean(existingApp.getId(), account.getId());
                    session.setAttribute("applicationBean", appBean);
                    
                    // Set wizard step cookie
                    Cookie stepCookie = new Cookie("wizardStep", 
                            String.valueOf(existingApp.getCurrentStep()));
                    stepCookie.setHttpOnly(true);
                    stepCookie.setPath("/");
                    response.addCookie(stepCookie);
                    
                    // Redirect to current step
                    response.sendRedirect(getStepUrl(request, existingApp.getCurrentStep()));
                } else {
                    // Check if has submitted application
                    java.util.List<Application> apps = applicationDAO.findAllByAccountId(account.getId());
                    boolean hasSubmitted = apps.stream()
                            .anyMatch(a -> a.getStatus() != Application.Status.DRAFT);
                    
                    if (hasSubmitted) {
                        // Redirect to status page
                        response.sendRedirect(request.getContextPath() + "/apply/status");
                    } else {
                        // Start new application
                        ApplicationBean appBean = new ApplicationBean(account.getId());
                        appBean.getPersonal().setEmail(email);
                        session.setAttribute("applicationBean", appBean);
                        session.setAttribute("currentStep", 1);
                        
                        response.sendRedirect(request.getContextPath() + "/apply/personal");
                    }
                }
            } else {
                errors.put("login", "Invalid email or password");
                request.setAttribute("errors", errors);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                       .forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
        }
    }
    
    private ApplicationBean loadApplicationBean(Long applicationId, Long accountId) {
        ApplicationBean appBean = new ApplicationBean(accountId);
        appBean.setApplicationId(applicationId);
        
        try {
            ApplicantDAO applicantDAO = new ApplicantDAO();
            ApplicationDAO applicationDAO = new ApplicationDAO();
            
            // Load personal info
            appBean.setPersonal(applicantDAO.findPersonalByApplicationId(applicationId));
            
            // Load programme
            appBean.setProgramme(applicationDAO.findProgrammeByApplicationId(applicationId));
            
            // Load academics
            appBean.setAcademics(applicationDAO.findAcademicsByApplicationId(applicationId));
            appBean.setSubjectGrades(applicationDAO.findSubjectGradesByApplicationId(applicationId));
            
            // Load guardians
            ke.ac.egerton.ams.dao.GuardianDAO guardianDAO = new ke.ac.egerton.ams.dao.GuardianDAO();
            appBean.setGuardians(guardianDAO.findByApplicationId(applicationId));
            
            // Load extra
            appBean.setExtra(applicationDAO.findExtraByApplicationId(applicationId));
            
            // Load documents
            ke.ac.egerton.ams.dao.DocumentDAO documentDAO = new ke.ac.egerton.ams.dao.DocumentDAO();
            appBean.setDocuments(documentDAO.findByApplicationId(applicationId));
            
        } catch (SQLException e) {
            // Log error but continue with partial data
            System.err.println("Error loading application data: " + e.getMessage());
        }
        
        return appBean;
    }
    
    private String getStepUrl(HttpServletRequest request, int step) {
        String contextPath = request.getContextPath();
        switch (step) {
            case 1: return contextPath + "/apply/personal";
            case 2: return contextPath + "/apply/programme";
            case 3: return contextPath + "/apply/academics";
            case 4: return contextPath + "/apply/documents";
            case 5: return contextPath + "/apply/guardian";
            case 6: return contextPath + "/apply/extra";
            case 7: return contextPath + "/apply/submit";
            default: return contextPath + "/apply/personal";
        }
    }
}
