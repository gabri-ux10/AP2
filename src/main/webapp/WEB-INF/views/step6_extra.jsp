<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Step 6: Extra Information - Egerton University AMS</title>
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
                    <div class="wizard-step completed"><span class="step-number">4</span><span class="step-label">Documents</span></div>
                    <div class="wizard-step completed"><span class="step-number">5</span><span class="step-label">Guardian</span></div>
                    <div class="wizard-step active"><span class="step-number">6</span><span class="step-label">Extra Info</span></div>
                    <div class="wizard-step"><span class="step-number">7</span><span class="step-label">Submit</span></div>
                </div>
            </div>
            
            <div class="wizard-body">
                <h3 style="margin-bottom: 1.5rem; color: var(--primary-color);">
                    Step 6: Additional Information
                </h3>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/apply/extra" method="post" 
                      class="wizard-form needs-validation">
                    
                    <div class="card">
                        <div class="card-header">Special Needs / Disability</div>
                        <div class="card-body">
                            <div class="form-group">
                                <label>Do you have any special needs or disability? <span class="required">*</span></label>
                                <div class="radio-group" style="margin-top: 0.5rem;">
                                    <label class="radio-option">
                                        <input type="radio" name="hasDisability" value="no" 
                                               ${not extra.hasDisability ? 'checked' : ''} required>
                                        No
                                    </label>
                                    <label class="radio-option">
                                        <input type="radio" name="hasDisability" value="yes"
                                               ${extra.hasDisability ? 'checked' : ''}>
                                        Yes
                                    </label>
                                </div>
                                <c:if test="${not empty errors.hasDisability}">
                                    <div class="field-error">${errors.hasDisability}</div>
                                </c:if>
                            </div>
                            
                            <div id="disability-description-div" 
                                 style="${extra.hasDisability ? '' : 'display: none;'}">
                                <div class="form-group">
                                    <label for="disabilityDescription">
                                        Please describe your special needs or disability
                                    </label>
                                    <textarea id="disabilityDescription" name="disabilityDescription" 
                                              rows="4" maxlength="1000"
                                              placeholder="Provide details about your special needs or disability to help us make appropriate arrangements...">${extra.disabilityDescription}</textarea>
                                    <p class="help-text">This information helps us provide appropriate support and accommodations.</p>
                                    <c:if test="${not empty errors.disabilityDescription}">
                                        <div class="field-error">${errors.disabilityDescription}</div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="alert alert-info" style="margin-top: 1.5rem;">
                        <strong>Note:</strong> All information provided is confidential and will only be used 
                        for the purpose of supporting your application and academic experience at Egerton University.
                    </div>
                    
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/apply/guardian" class="btn btn-secondary btn-lg">
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
