/**
 * Egerton AMS - Client-Side Validation
 * 
 * Critical fields with integer-only validation:
 *   - National ID: optional, exactly 8 digits
 *   - Birth Certificate: required, exactly 7 digits
 *   - Phone Number: required, exactly 9 digits (after +254)
 *   - KCSE Index Number: required, exactly 11 digits
 */

(function() {
    'use strict';

    // Validation rules
    const RULES = {
        nationalId: {
            pattern: /^[0-9]{8}$/,
            maxLength: 8,
            required: false,
            message: 'National ID must be exactly 8 digits'
        },
        birthCertNumber: {
            pattern: /^[0-9]{7}$/,
            maxLength: 7,
            required: true,
            message: 'Birth Certificate Number must be exactly 7 digits'
        },
        phoneNumber: {
            pattern: /^[0-9]{9}$/,
            maxLength: 9,
            required: true,
            message: 'Phone number must be exactly 9 digits after +254'
        },
        kcseIndexNumber: {
            pattern: /^[0-9]{11}$/,
            maxLength: 11,
            required: true,
            message: 'KCSE Index Number must be exactly 11 digits'
        },
        guardianPhone: {
            pattern: /^[0-9]{9}$/,
            maxLength: 9,
            required: true,
            message: 'Phone number must be exactly 9 digits after +254'
        }
    };

    /**
     * Initialize validation on page load
     */
    function init() {
        // Set up keypress handlers to reject non-numeric input
        setupNumericInputs();
        
        // Set up blur handlers for validation
        setupBlurValidation();
        
        // Set up form submission validation
        setupFormValidation();
    }

    /**
     * Set up numeric-only inputs
     */
    function setupNumericInputs() {
        const numericFields = [
            'nationalId',
            'birthCertNumber', 
            'phoneNumber',
            'kcseIndexNumber',
            'guardian1Phone',
            'guardian2Phone'
        ];

        numericFields.forEach(function(fieldId) {
            const field = document.getElementById(fieldId);
            if (field) {
                // Set maxlength from rules
                const ruleName = getRuleName(fieldId);
                if (RULES[ruleName]) {
                    field.maxLength = RULES[ruleName].maxLength;
                }

                // Prevent non-numeric input
                field.addEventListener('keypress', function(e) {
                    if (!/[0-9]/.test(e.key)) {
                        e.preventDefault();
                    }
                });

                // Also handle paste
                field.addEventListener('paste', function(e) {
                    const pastedText = (e.clipboardData || window.clipboardData).getData('text');
                    if (!/^[0-9]+$/.test(pastedText)) {
                        e.preventDefault();
                    }
                });

                // Filter on input (for mobile/IME)
                field.addEventListener('input', function(e) {
                    this.value = this.value.replace(/[^0-9]/g, '');
                    const ruleName = getRuleName(fieldId);
                    if (RULES[ruleName] && this.value.length > RULES[ruleName].maxLength) {
                        this.value = this.value.substring(0, RULES[ruleName].maxLength);
                    }
                });
            }
        });
    }

    /**
     * Map field ID to rule name
     */
    function getRuleName(fieldId) {
        if (fieldId === 'guardian1Phone' || fieldId === 'guardian2Phone') {
            return 'guardianPhone';
        }
        return fieldId;
    }

    /**
     * Set up blur validation (validate when field loses focus)
     */
    function setupBlurValidation() {
        const fieldsToValidate = Object.keys(RULES);

        fieldsToValidate.forEach(function(ruleName) {
            // Find fields that match this rule
            const fieldIds = getFieldIdsForRule(ruleName);
            
            fieldIds.forEach(function(fieldId) {
                const field = document.getElementById(fieldId);
                if (field) {
                    field.addEventListener('blur', function() {
                        validateField(this, ruleName);
                    });

                    // Clear error on focus
                    field.addEventListener('focus', function() {
                        clearFieldError(this);
                    });
                }
            });
        });
    }

    /**
     * Get field IDs for a rule name
     */
    function getFieldIdsForRule(ruleName) {
        if (ruleName === 'guardianPhone') {
            return ['guardian1Phone', 'guardian2Phone'];
        }
        return [ruleName];
    }

    /**
     * Validate a single field
     */
    function validateField(field, ruleName) {
        const rule = RULES[ruleName];
        if (!rule) return true;

        const value = field.value.trim();

        // Check if required
        if (rule.required && !value) {
            showFieldError(field, 'This field is required');
            return false;
        }

        // If not required and empty, it's valid
        if (!rule.required && !value) {
            clearFieldError(field);
            return true;
        }

        // Check pattern
        if (value && !rule.pattern.test(value)) {
            showFieldError(field, rule.message);
            return false;
        }

        clearFieldError(field);
        return true;
    }

    /**
     * Show error message for a field
     */
    function showFieldError(field, message) {
        clearFieldError(field);

        // Add error class to field
        field.classList.add('input-error');

        // Create error message element
        const errorDiv = document.createElement('div');
        errorDiv.className = 'field-error';
        errorDiv.textContent = message;

        // Insert after field (or after wrapper if exists)
        const wrapper = field.closest('.input-wrapper') || field.parentNode;
        if (wrapper) {
            wrapper.appendChild(errorDiv);
        }
    }

    /**
     * Clear error message for a field
     */
    function clearFieldError(field) {
        field.classList.remove('input-error');

        // Remove error message
        const wrapper = field.closest('.input-wrapper') || field.parentNode;
        if (wrapper) {
            const errorDiv = wrapper.querySelector('.field-error');
            if (errorDiv) {
                errorDiv.remove();
            }
        }
    }

    /**
     * Set up form submission validation
     */
    function setupFormValidation() {
        const forms = document.querySelectorAll('form.needs-validation');

        forms.forEach(function(form) {
            form.addEventListener('submit', function(e) {
                let isValid = true;

                // Validate all numeric fields in this form
                const numericFields = form.querySelectorAll(
                    '#nationalId, #birthCertNumber, #phoneNumber, #kcseIndexNumber, ' +
                    '#guardian1Phone, #guardian2Phone'
                );

                numericFields.forEach(function(field) {
                    const ruleName = getRuleName(field.id);
                    if (!validateField(field, ruleName)) {
                        isValid = false;
                    }
                });

                // Check required fields
                const requiredFields = form.querySelectorAll('[required]');
                requiredFields.forEach(function(field) {
                    if (!field.value.trim()) {
                        showFieldError(field, 'This field is required');
                        isValid = false;
                    }
                });

                if (!isValid) {
                    e.preventDefault();
                    
                    // Scroll to first error
                    const firstError = form.querySelector('.input-error');
                    if (firstError) {
                        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        firstError.focus();
                    }
                }
            });
        });
    }

    /**
     * Validate email format
     */
    function validateEmail(email) {
        const pattern = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
        return pattern.test(email);
    }

    /**
     * Validate date of birth (must be 16+)
     */
    function validateDateOfBirth(dobString) {
        const dob = new Date(dobString);
        const today = new Date();
        let age = today.getFullYear() - dob.getFullYear();
        const monthDiff = today.getMonth() - dob.getMonth();
        
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < dob.getDate())) {
            age--;
        }
        
        return age >= 16;
    }

    /**
     * Public API
     */
    window.AmsValidation = {
        init: init,
        validateField: validateField,
        showFieldError: showFieldError,
        clearFieldError: clearFieldError,
        validateEmail: validateEmail,
        validateDateOfBirth: validateDateOfBirth,
        RULES: RULES
    };

    // Initialize on DOM ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})();
