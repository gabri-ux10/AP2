<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Application Submitted - Egerton University AMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <header class="header">
        <div class="container">
            <div class="header-content">
                <div class="header-logo">
                    <div>
                        <div class="header-title">EGERTON UNIVERSITY</div>
                        <div class="header-subtitle">Admission Management System</div>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <main class="container">
        <div class="status-container">
            <div class="status-card">
                <div class="status-header accepted">
                    <div class="congrats-icon">🎉</div>
                    <div class="status-title">Application Submitted Successfully!</div>
                </div>
                
                <div class="status-body" style="text-align: center;">
                    <h2 style="color: var(--primary-color); margin-bottom: 1rem;">
                        Congratulations, ${fullName}!
                    </h2>
                    
                    <p style="font-size: 1.1rem; margin-bottom: 1.5rem;">
                        Your application for admission to Egerton University has been received.
                    </p>
                    
                    <div style="margin-bottom: 2rem;">
                        <p class="text-muted">Your Application Reference Number:</p>
                        <div class="reference-number" onclick="copyReferenceNumber('${referenceNumber}')">
                            ${referenceNumber}
                        </div>
                        <p class="text-muted" style="font-size: 0.875rem;">
                            (Click to copy)
                        </p>
                    </div>
                    
                    <div class="card" style="text-align: left; margin-bottom: 1.5rem;">
                        <div class="card-body">
                            <p><strong>Programme Applied:</strong> ${programmeName}</p>
                        </div>
                    </div>
                    
                    <div class="alert alert-info" style="text-align: left;">
                        <strong>What happens next?</strong>
                        <ol style="margin: 0.5rem 0 0 1.5rem;">
                            <li>A confirmation email has been sent to your registered email address</li>
                            <li>Your application will be reviewed by the Admissions Office</li>
                            <li>You will be notified of the decision via email</li>
                            <li>You can check your application status anytime by logging in</li>
                        </ol>
                    </div>
                    
                    <div style="margin-top: 2rem;">
                        <a href="${pageContext.request.contextPath}/apply/status" class="btn btn-primary btn-lg">
                            Check Application Status
                        </a>
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-lg">
                            Logout
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
        <div class="container">
            <div class="footer-content">
                <p>&copy; 2025 Egerton University. All rights reserved.</p>
            </div>
        </div>
    </footer>

    <script>
        function copyReferenceNumber(ref) {
            navigator.clipboard.writeText(ref).then(function() {
                alert('Reference number copied: ' + ref);
            }).catch(function() {
                prompt('Copy reference number:', ref);
            });
        }
    </script>
</body>
</html>
