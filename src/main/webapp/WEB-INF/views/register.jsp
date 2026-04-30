<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Egerton University AMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-card">
            <div class="auth-logo">
                <h2 style="color: var(--primary-color);">EGERTON UNIVERSITY</h2>
                <p class="text-muted">Admission Management System</p>
            </div>
            
            <h3 class="auth-title">Create Account</h3>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/auth" method="post" class="needs-validation">
                <input type="hidden" name="action" value="register">
                
                <div class="form-group">
                    <label for="email">Email Address <span class="required">*</span></label>
                    <input type="email" id="email" name="email" 
                           value="${email}" required autofocus>
                    <p class="help-text">Use an email you have access to. Important notifications will be sent here.</p>
                    <c:if test="${not empty errors.email}">
                        <div class="field-error">${errors.email}</div>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label for="password">Password <span class="required">*</span></label>
                    <input type="password" id="password" name="password" 
                           minlength="8" required>
                    <p class="help-text">Minimum 8 characters</p>
                    <c:if test="${not empty errors.password}">
                        <div class="field-error">${errors.password}</div>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password <span class="required">*</span></label>
                    <input type="password" id="confirmPassword" name="confirmPassword" 
                           minlength="8" required>
                    <c:if test="${not empty errors.confirmPassword}">
                        <div class="field-error">${errors.confirmPassword}</div>
                    </c:if>
                </div>
                
                <button type="submit" class="btn btn-primary btn-lg" style="width: 100%;">
                    Create Account
                </button>
            </form>
            
            <div style="margin-top: 1.5rem; text-align: center;">
                <p>Already have an account? 
                    <a href="${pageContext.request.contextPath}/auth?action=login">
                        Login here
                    </a>
                </p>
                <p style="margin-top: 1rem;">
                    <a href="${pageContext.request.contextPath}/" class="text-muted">
                        Back to Home
                    </a>
                </p>
            </div>
        </div>
    </div>
    
    <script>
        // Password match validation
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const password = document.getElementById('password').value;
            if (this.value !== password) {
                this.setCustomValidity('Passwords do not match');
            } else {
                this.setCustomValidity('');
            }
        });
    </script>
</body>
</html>
