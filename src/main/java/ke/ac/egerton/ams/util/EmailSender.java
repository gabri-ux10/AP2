package ke.ac.egerton.ams.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.InputStream;
import java.util.Properties;

/**
 * Email Sender Utility
 * Sends notification emails using Jakarta Mail API.
 */
public class EmailSender {
    
    private Properties mailProperties;
    private String username;
    private String password;
    private String fromEmail;
    private String fromName;
    
    public EmailSender() {
        loadProperties();
    }
    
    private void loadProperties() {
        mailProperties = new Properties();
        
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Could not load mail properties");
                return;
            }
            Properties allProps = new Properties();
            allProps.load(input);
            
            // Set mail properties
            mailProperties.put("mail.smtp.host", 
                    allProps.getProperty("mail.smtp.host", "smtp.gmail.com"));
            mailProperties.put("mail.smtp.port", 
                    allProps.getProperty("mail.smtp.port", "587"));
            mailProperties.put("mail.smtp.auth", 
                    allProps.getProperty("mail.smtp.auth", "true"));
            mailProperties.put("mail.smtp.starttls.enable", 
                    allProps.getProperty("mail.smtp.starttls.enable", "true"));
            
            username = allProps.getProperty("mail.username");
            password = allProps.getProperty("mail.password");
            fromEmail = allProps.getProperty("mail.from", "admissions@egerton.ac.ke");
            fromName = allProps.getProperty("mail.from.name", "Egerton University Admissions");
            
        } catch (Exception e) {
            System.err.println("Error loading mail properties: " + e.getMessage());
        }
    }
    
    /**
     * Send application submission confirmation email
     */
    public void sendSubmissionConfirmation(String toEmail, String applicantName, 
                                           String referenceNumber, String programmeName) 
            throws MessagingException {
        
        String subject = "Application Received - " + referenceNumber;
        
        String body = String.format("""
            Dear %s,
            
            Thank you for submitting your application to Egerton University.
            
            Your application details:
            - Reference Number: %s
            - Programme: %s
            - Status: Application Received
            
            You can track your application status by logging into the application portal.
            
            What happens next:
            1. Your application will be reviewed by our Admissions Office
            2. You will receive an email notification when a decision is made
            3. If accepted, you will receive further instructions for registration
            
            If you have any questions, please contact our Admissions Office.
            
            Best regards,
            Egerton University Admissions Office
            """, applicantName, referenceNumber, programmeName);
        
        sendEmail(toEmail, subject, body);
    }
    
    /**
     * Send acceptance notification email
     */
    public void sendAcceptanceNotification(String toEmail, String applicantName,
                                           String referenceNumber, String programmeName) 
            throws MessagingException {
        
        String subject = "Congratulations! Application Accepted - " + referenceNumber;
        
        String body = String.format("""
            Dear %s,
            
            CONGRATULATIONS!
            
            We are pleased to inform you that your application to Egerton University has been ACCEPTED.
            
            Application Details:
            - Reference Number: %s
            - Programme: %s
            - Status: ACCEPTED
            
            Next Steps:
            1. Log into the application portal to view your acceptance letter
            2. Pay the required fees as outlined in the acceptance letter
            3. Report to the university on the specified date with all required documents
            
            Required documents for registration:
            - Original and copy of KCSE certificate
            - Original and copy of National ID
            - Original and copy of Birth Certificate
            - Passport photos (4)
            - Fee payment receipt
            
            Welcome to the Egerton University family!
            
            Best regards,
            Egerton University Admissions Office
            """, applicantName, referenceNumber, programmeName);
        
        sendEmail(toEmail, subject, body);
    }
    
    /**
     * Send rejection notification email
     */
    public void sendRejectionNotification(String toEmail, String applicantName,
                                          String referenceNumber) 
            throws MessagingException {
        
        String subject = "Application Status Update - " + referenceNumber;
        
        String body = String.format("""
            Dear %s,
            
            Thank you for your interest in Egerton University.
            
            After careful consideration of your application (Reference: %s), we regret to inform you 
            that we are unable to offer you admission for the current intake period.
            
            This decision was based on the high number of qualified applicants and 
            limited available spaces in the programme.
            
            We encourage you to:
            1. Consider applying for other programmes that may have available spaces
            2. Apply again in future intake periods
            3. Contact our Admissions Office for guidance on improving your application
            
            We appreciate your interest in Egerton University and wish you success in your 
            academic pursuits.
            
            Best regards,
            Egerton University Admissions Office
            """, applicantName, referenceNumber);
        
        sendEmail(toEmail, subject, body);
    }
    
    /**
     * Send a general email
     */
    public void sendEmail(String toEmail, String subject, String body) 
            throws MessagingException {
        
        if (username == null || password == null) {
            System.err.println("Email not configured - skipping email send");
            return;
        }
        
        Session session = Session.getInstance(mailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, fromName));
            message.setRecipients(Message.RecipientType.TO, 
                    InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            
            Transport.send(message);
            
            System.out.println("Email sent successfully to: " + toEmail);
            
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new MessagingException("Failed to send email", e);
        }
    }
}
