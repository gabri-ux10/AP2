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
import ke.ac.egerton.ams.dao.OfficerDAO;
import ke.ac.egerton.ams.models.Officer;

/**
 * Officer Login Servlet
 * Handles admission officer authentication.
 * URL: /officer/login
 */
public class OfficerLoginServlet extends HttpServlet {
    
    private OfficerDAO officerDAO;
    
    @Override
    public void init() throws ServletException {
        officerDAO = new OfficerDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if already logged in
        HttpSession session = request.getSession(false);
        if (session != null) {
            Officer officer = (Officer) session.getAttribute("loggedInOfficer");
            if (officer != null) {
                response.sendRedirect(request.getContextPath() + "/officer/dashboard");
                return;
            }
        }
        
        request.getRequestDispatcher("/WEB-INF/views/officer_login.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
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
            request.getRequestDispatcher("/WEB-INF/views/officer_login.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            // Check if account is locked
            Officer officer = officerDAO.findByEmail(email);
            
            if (officer != null && officer.isLocked()) {
                errors.put("login", "Account is locked. Please try again in 15 minutes.");
                request.setAttribute("errors", errors);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/views/officer_login.jsp")
                       .forward(request, response);
                return;
            }
            
            // Authenticate
            officer = officerDAO.authenticate(email, password);
            
            if (officer != null) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("loggedInOfficer", officer);
                session.setAttribute("officerId", officer.getId());
                
                // Set officer session cookie
                Cookie officerCookie = new Cookie("officerSession", 
                        String.valueOf(System.currentTimeMillis()));
                officerCookie.setHttpOnly(true);
                officerCookie.setPath("/");
                response.addCookie(officerCookie);
                
                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/officer/dashboard");
            } else {
                // Get updated officer to check attempts
                officer = officerDAO.findByEmail(email);
                
                if (officer != null && officer.isLocked()) {
                    errors.put("login", "Account locked after 3 failed attempts. Please try again in 15 minutes.");
                } else {
                    errors.put("login", "Invalid email or password");
                }
                
                request.setAttribute("errors", errors);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/views/officer_login.jsp")
                       .forward(request, response);
            }
            
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/officer_login.jsp")
                   .forward(request, response);
        }
    }
}
