<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Step 4: Document Upload - Egerton University AMS</title>
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
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </nav>
            </div>
        </div>
    </header>

    <main class="container">
        <div class="wizard-container">
            <div class="wizard-header">
                <h2 class="wizard-title">Application Form</h2>
                <div class="wizard-steps">
                    <div class="wizard-step completed"><span class="step-number">1</span><span class="step-label">Personal</span></div>
                    <div class="wizard-step completed"><span class="step-number">2</span><span class="step-label">Programme</span></div>
                    <div class="wizard-step completed"><span class="step-number">3</span><span class="step-label">Academics</span></div>
                    <div class="wizard-step active"><span class="step-number">4</span><span class="step-label">Documents</span></div>
                    <div class="wizard-step"><span class="step-number">5</span><span class="step-label">Guardian</span></div>
                    <div class="wizard-step"><span class="step-number">6</span><span class="step-label">Extra Info</span></div>
                    <div class="wizard-step"><span class="step-number">7</span><span class="step-label">Submit</span></div>
                </div>
            </div>
            
            <div class="wizard-body">
                <h3 style="margin-bottom: 1.5rem; color: var(--primary-color);">
                    Step 4: Document Upload
                </h3>
                
                <div class="alert alert-info">
                    <strong>Upload Requirements:</strong>
                    <ul style="margin: 0.5rem 0 0 1.5rem;">
                        <li>Accepted formats: PDF, JPG, PNG</li>
                        <li>Maximum file size: 5MB per document</li>
                        <li>Ensure documents are clear and readable</li>
                    </ul>
                </div>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <c:if test="${not empty errors.upload}">
                    <div class="alert alert-danger">${errors.upload}</div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/apply/documents" method="post" 
                      enctype="multipart/form-data" class="wizard-form needs-validation">
                    
                    <!-- KCSE Certificate -->
                    <div class="form-group">
                        <label for="kcseCert">KCSE Certificate/Result Slip <span class="required">*</span></label>
                        <div class="file-input-wrapper">
                            <input type="file" id="kcseCert" name="kcseCert" 
                                   accept=".pdf,.jpg,.jpeg,.png,application/pdf,image/jpeg,image/png">
                            <div class="file-input-button">
                                <span>Choose File</span>
                            </div>
                        </div>
                        <p class="help-text">Upload your KCSE certificate or result slip</p>
                        <c:if test="${not empty documents.kcseCertPath}">
                            <div class="file-preview">
                                <span style="color: var(--success-color);">✓</span>
                                <span>KCSE Certificate uploaded</span>
                            </div>
                        </c:if>
                        <c:if test="${not empty errors.kcseCert}">
                            <div class="field-error">${errors.kcseCert}</div>
                        </c:if>
                    </div>
                    
                    <!-- National ID -->
                    <div class="form-group">
                        <label for="nationalId">National ID Copy</label>
                        <div class="file-input-wrapper">
                            <input type="file" id="nationalId" name="nationalId" 
                                   accept=".pdf,.jpg,.jpeg,.png,application/pdf,image/jpeg,image/png">
                            <div class="file-input-button">
                                <span>Choose File</span>
                            </div>
                        </div>
                        <p class="help-text">Optional - both sides of your National ID</p>
                        <c:if test="${not empty documents.nationalIdPath}">
                            <div class="file-preview">
                                <span style="color: var(--success-color);">✓</span>
                                <span>National ID uploaded</span>
                            </div>
                        </c:if>
                        <c:if test="${not empty errors.nationalId}">
                            <div class="field-error">${errors.nationalId}</div>
                        </c:if>
                    </div>
                    
                    <!-- Birth Certificate -->
                    <div class="form-group">
                        <label for="birthCert">Birth Certificate <span class="required">*</span></label>
                        <div class="file-input-wrapper">
                            <input type="file" id="birthCert" name="birthCert" 
                                   accept=".pdf,.jpg,.jpeg,.png,application/pdf,image/jpeg,image/png">
                            <div class="file-input-button">
                                <span>Choose File</span>
                            </div>
                        </div>
                        <p class="help-text">Upload your birth certificate</p>
                        <c:if test="${not empty documents.birthCertPath}">
                            <div class="file-preview">
                                <span style="color: var(--success-color);">✓</span>
                                <span>Birth Certificate uploaded</span>
                            </div>
                        </c:if>
                        <c:if test="${not empty errors.birthCert}">
                            <div class="field-error">${errors.birthCert}</div>
                        </c:if>
                    </div>
                    
                    <!-- PDF Summary Note -->
                    <c:if test="${documents.hasPdfGenerated()}">
                        <div class="alert alert-success" style="margin-top: 1.5rem;">
                            <strong>PDF Summary Generated</strong>
                            <p>Your application summary PDF has been automatically generated.</p>
                        </div>
                    </c:if>
                    
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/apply/academics" class="btn btn-secondary btn-lg">
                            ← Back
                        </a>
                        <button type="submit" class="btn btn-primary btn-lg">
                            Save & Continue →
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/assets/js/validate.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/wizard.js"></script>
</body>
</html>
