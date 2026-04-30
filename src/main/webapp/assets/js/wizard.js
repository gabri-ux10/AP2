/**
 * Egerton AMS - Wizard JavaScript
 * Handles dynamic form behavior in the application wizard.
 */

(function() {
    'use strict';

    /**
     * Initialize wizard functionality
     */
    function initWizard() {
        // Subject grades dynamic rows (Step 3)
        initSubjectGrades();
        
        // Disability toggle (Step 6)
        initDisabilityToggle();
        
        // File upload previews
        initFileUploadPreviews();
        
        // Form confirmation on submit (Step 7)
        initSubmitConfirmation();
        
        // Auto-save warning
        initAutoSaveWarning();
    }

    /**
     * Subject Grades - Dynamic Rows
     */
    function initSubjectGrades() {
        const subjectContainer = document.getElementById('subject-grades-container');
        const addButton = document.getElementById('add-subject-btn');
        
        if (!subjectContainer || !addButton) return;
        
        // Add subject row
        addButton.addEventListener('click', function() {
            const rows = subjectContainer.querySelectorAll('.subject-row');
            if (rows.length >= 8) {
                alert('Maximum 8 subjects allowed');
                return;
            }
            
            const rowNumber = rows.length + 1;
            const newRow = createSubjectRow(rowNumber);
            subjectContainer.appendChild(newRow);
            
            updateSubjectRemoveButtons();
        });
        
        // Delegate remove button clicks
        subjectContainer.addEventListener('click', function(e) {
            if (e.target.classList.contains('remove-subject-btn')) {
                const rows = subjectContainer.querySelectorAll('.subject-row');
                if (rows.length <= 7) {
                    alert('Minimum 7 subjects required');
                    return;
                }
                
                const row = e.target.closest('.subject-row');
                row.remove();
                
                // Renumber rows
                renumberSubjectRows();
                updateSubjectRemoveButtons();
            }
        });
        
        // Initial update
        updateSubjectRemoveButtons();
    }

    /**
     * Create a new subject row
     */
    function createSubjectRow(number) {
        const subjects = window.KCSE_SUBJECTS || [
            "Mathematics", "English", "Kiswahili", "Biology", "Chemistry", 
            "Physics", "History & Government", "Geography",
            "Christian Religious Education (CRE)", "Islamic Religious Education (IRE)",
            "Business Studies", "Computer Studies", "Agriculture", 
            "Home Science", "Art & Design", "Music", "French"
        ];
        
        const grades = ["A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "E"];
        
        const row = document.createElement('div');
        row.className = 'subject-row form-row';
        row.innerHTML = `
            <div class="form-group">
                <label>Subject ${number} <span class="required">*</span></label>
                <select name="subjectName" required>
                    <option value="">Select Subject</option>
                    ${subjects.map(s => `<option value="${s}">${s}</option>`).join('')}
                </select>
            </div>
            <div class="form-group">
                <label>Grade <span class="required">*</span></label>
                <select name="subjectGrade" required>
                    <option value="">Select Grade</option>
                    ${grades.map(g => `<option value="${g}">${g}</option>`).join('')}
                </select>
            </div>
            <div class="form-group" style="align-self: end;">
                <button type="button" class="btn btn-sm btn-danger remove-subject-btn">Remove</button>
            </div>
        `;
        
        return row;
    }

    /**
     * Renumber subject rows after deletion
     */
    function renumberSubjectRows() {
        const rows = document.querySelectorAll('.subject-row');
        rows.forEach((row, index) => {
            const label = row.querySelector('label');
            if (label) {
                label.innerHTML = `Subject ${index + 1} <span class="required">*</span>`;
            }
        });
    }

    /**
     * Update remove button visibility
     */
    function updateSubjectRemoveButtons() {
        const rows = document.querySelectorAll('.subject-row');
        const buttons = document.querySelectorAll('.remove-subject-btn');
        
        buttons.forEach(btn => {
            btn.style.display = rows.length <= 7 ? 'none' : 'inline-flex';
        });
        
        // Update add button
        const addBtn = document.getElementById('add-subject-btn');
        if (addBtn) {
            addBtn.disabled = rows.length >= 8;
        }
    }

    /**
     * Disability Toggle (Step 6)
     */
    function initDisabilityToggle() {
        const disabilityRadios = document.querySelectorAll('input[name="hasDisability"]');
        const descriptionDiv = document.getElementById('disability-description-div');
        
        if (!disabilityRadios.length || !descriptionDiv) return;
        
        function toggleDescription() {
            const yesRadio = document.querySelector('input[name="hasDisability"][value="yes"]') ||
                            document.querySelector('input[name="hasDisability"][value="1"]');
            
            if (yesRadio && yesRadio.checked) {
                descriptionDiv.style.display = 'block';
                descriptionDiv.querySelector('textarea').required = true;
            } else {
                descriptionDiv.style.display = 'none';
                descriptionDiv.querySelector('textarea').required = false;
            }
        }
        
        disabilityRadios.forEach(radio => {
            radio.addEventListener('change', toggleDescription);
        });
        
        // Initial state
        toggleDescription();
    }

    /**
     * File Upload Previews
     */
    function initFileUploadPreviews() {
        const fileInputs = document.querySelectorAll('input[type="file"]');
        
        fileInputs.forEach(input => {
            input.addEventListener('change', function() {
                const previewId = this.id + '-preview';
                let preview = document.getElementById(previewId);
                
                if (!preview) {
                    preview = document.createElement('div');
                    preview.id = previewId;
                    preview.className = 'file-preview';
                    this.parentNode.appendChild(preview);
                }
                
                if (this.files && this.files[0]) {
                    const file = this.files[0];
                    const size = formatFileSize(file.size);
                    
                    preview.innerHTML = `
                        <span style="color: var(--success-color);">✓</span>
                        <span>${file.name}</span>
                        <span class="text-muted">(${size})</span>
                    `;
                    preview.style.display = 'flex';
                } else {
                    preview.style.display = 'none';
                }
            });
        });
    }

    /**
     * Format file size
     */
    function formatFileSize(bytes) {
        if (bytes < 1024) return bytes + ' B';
        if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
        return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
    }

    /**
     * Submit Confirmation (Step 7)
     */
    function initSubmitConfirmation() {
        const submitForm = document.getElementById('submit-form');
        
        if (!submitForm) return;
        
        submitForm.addEventListener('submit', function(e) {
            const declaration = document.getElementById('declaration');
            
            if (!declaration || !declaration.checked) {
                e.preventDefault();
                alert('Please agree to the declaration before submitting');
                return;
            }
            
            const confirmed = confirm(
                'Are you sure you want to submit your application?\n\n' +
                'Once submitted, you will not be able to make changes.\n\n' +
                'Click OK to submit, or Cancel to review your application.'
            );
            
            if (!confirmed) {
                e.preventDefault();
            }
        });
    }

    /**
     * Auto-save warning
     */
    function initAutoSaveWarning() {
        const wizardForms = document.querySelectorAll('.wizard-form');
        let formChanged = false;
        
        wizardForms.forEach(form => {
            form.addEventListener('change', function() {
                formChanged = true;
            });
        });
        
        window.addEventListener('beforeunload', function(e) {
            if (formChanged) {
                e.preventDefault();
                e.returnValue = 'You have unsaved changes. Are you sure you want to leave?';
            }
        });
        
        // Clear flag on form submit
        wizardForms.forEach(form => {
            form.addEventListener('submit', function() {
                formChanged = false;
            });
        });
    }

    /**
     * Print functionality for review/status pages
     */
    window.printApplication = function() {
        window.print();
    };

    /**
     * Copy reference number
     */
    window.copyReferenceNumber = function(refNumber) {
        navigator.clipboard.writeText(refNumber).then(function() {
            alert('Reference number copied to clipboard: ' + refNumber);
        }).catch(function() {
            prompt('Copy reference number:', refNumber);
        });
    };

    // Initialize on DOM ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initWizard);
    } else {
        initWizard();
    }

})();
