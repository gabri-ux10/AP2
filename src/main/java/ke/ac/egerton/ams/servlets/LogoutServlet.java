package ke.ac.egerton.ams.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Logout Servlet
 * Invalidates session and clears cookies.
 * URL: /logout
 */
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        // Clear officer session cookie
        Cookie officerCookie = new Cookie("officerSession", "");
        officerCookie.setMaxAge(0);
        officerCookie.setPath("/");
        response.addCookie(officerCookie);
        
        // Clear wizard step cookie
        Cookie stepCookie = new Cookie("wizardStep", "");
        stepCookie.setMaxAge(0);
        stepCookie.setPath("/");
        response.addCookie(stepCookie);
        
        // Check if this is an officer logout
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/officer/")) {
            response.sendRedirect(request.getContextPath() + "/officer/login");
        } else {
            response.sendRedirect(request.getContextPath() + "/auth");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
