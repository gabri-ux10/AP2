<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Applicant Login - Egerton University AMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-card">
            <div class="auth-logo">
                <h2 style="color: var(--primary-color);">EGERTON UNIVERSITY</h2>
                <p class="text-muted">Admission Management System</p>
            </div>
            
            <h3 class="auth-title">Applicant Login</h3>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            
            <c:if test="${not empty errors.login}">
                <div class="alert alert-danger">${errors.login}</div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/auth" method="post" class="needs-validation">
                <input type="hidden" name="action" value="login">
                
                <div class="form-group">
                    <label for="email">Email Address <span class="required">*</span></label>
                    <input type="email" id="email" name="email" 
                           value="${not empty email ? email : rememberedEmail}" 
                           required autofocus>
                    <c:if test="${not empty errors.email}">
                        <div class="field-error">${errors.email}</div>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label for="password">Password <span class="required">*</span></label>
                    <input type="password" id="password" name="password" required>
                    <c:if test="${not empty errors.password}">
                        <div class="field-error">${errors.password}</div>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label class="checkbox-option">
                        <input type="checkbox" name="remember" 
                               ${not empty rememberedEmail ? 'checked' : ''}>
                        Remember my email
                    </label>
                </div>
                
                <button type="submit" class="btn btn-primary btn-lg" style="width: 100%;">
                    Login
                </button>
            </form>
            
            <div style="margin-top: 1.5rem; text-align: center;">
                <p>Don't have an account? 
                    <a href="${pageContext.request.contextPath}/auth?action=register">
                        Register here
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
</body>
</html>
