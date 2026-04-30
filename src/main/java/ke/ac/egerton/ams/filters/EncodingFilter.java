package ke.ac.egerton.ams.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Character Encoding Filter to ensure UTF-8 encoding for all requests/responses.
 * Mapped to /* in web.xml
 */
public class EncodingFilter implements Filter {
    
    private String encoding = "UTF-8";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String configEncoding = filterConfig.getInitParameter("encoding");
        if (configEncoding != null && !configEncoding.isEmpty()) {
            this.encoding = configEncoding;
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        
        // Set character encoding for request
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        
        // Set character encoding for response
        response.setCharacterEncoding(encoding);
        
        // Do NOT set Content-Type here - let Tomcat handle MIME types for static files
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // No cleanup needed
    }
}
