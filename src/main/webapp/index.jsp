<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Egerton University - Admission Management System</title>
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
                <nav class="header-nav">
                    <a href="${pageContext.request.contextPath}/auth?action=login">Applicant Login</a>
                    <a href="${pageContext.request.contextPath}/auth?action=register">Register</a>
                    <a href="${pageContext.request.contextPath}/officer/login">Officer Portal</a>
                </nav>
            </div>
        </div>
    </header>

    <main class="container" style="margin-top: 50px;">
        <div style="text-align: center; max-width: 800px; margin: 0 auto;">
            <h1 style="color: var(--primary-color); font-size: 2.5rem; margin-bottom: 1rem;">
                Welcome to the Egerton University<br>Admission Management System
            </h1>
            
            <p style="font-size: 1.25rem; color: var(--text-light); margin-bottom: 2rem;">
                Apply online for undergraduate degree programmes at Egerton University. 
                Complete your application in 7 simple steps.
            </p>
            
            <div style="display: flex; gap: 1rem; justify-content: center; margin-bottom: 3rem;">
                <a href="${pageContext.request.contextPath}/auth?action=register" 
                   class="btn btn-primary btn-lg">
                    Start Application
                </a>
                <a href="${pageContext.request.contextPath}/auth?action=login" 
                   class="btn btn-secondary btn-lg">
                    Continue Application
                </a>
            </div>
            
            <div class="card" style="margin-top: 2rem;">
                <div class="card-header">Application Process</div>
                <div class="card-body">
                    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; text-align: left;">
                        <div>
                            <h4 style="color: var(--primary-color);">Step 1</h4>
                            <p>Personal Information</p>
                        </div>
                        <div>
                            <h4 style="color: var(--primary-color);">Step 2</h4>
                            <p>Programme Selection</p>
                        </div>
                        <div>
                            <h4 style="color: var(--primary-color);">Step 3</h4>
                            <p>Academic Background</p>
                        </div>
                        <div>
                            <h4 style="color: var(--primary-color);">Step 4</h4>
                            <p>Document Upload</p>
                        </div>
                        <div>
                            <h4 style="color: var(--primary-color);">Step 5</h4>
                            <p>Guardian Information</p>
                        </div>
                        <div>
                            <h4 style="color: var(--primary-color);">Step 6</h4>
                            <p>Extra Information</p>
                        </div>
                        <div>
                            <h4 style="color: var(--primary-color);">Step 7</h4>
                            <p>Review & Submit</p>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="alert alert-info" style="margin-top: 2rem; text-align: left;">
                <strong>Important:</strong> Make sure you have the following documents ready before starting:
                <ul style="margin-top: 0.5rem; margin-left: 1.5rem;">
                    <li>KCSE Certificate or Result Slip (PDF or image)</li>
                    <li>Birth Certificate (PDF or image)</li>
                    <li>National ID (optional, PDF or image)</li>
                    <li>Passport-size photo</li>
                </ul>
            </div>
        </div>
    </main>

    <footer class="footer" style="margin-top: 50px;">
        <div class="container">
            <div class="footer-content">
                <p>&copy; 2025 Egerton University. All rights reserved.</p>
            </div>
        </div>
    </footer>
</body>
</html>
