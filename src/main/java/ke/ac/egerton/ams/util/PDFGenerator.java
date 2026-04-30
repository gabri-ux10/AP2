package ke.ac.egerton.ams.util;

import ke.ac.egerton.ams.models.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PDF Generator Utility
 * Generates PDF summaries using Apache PDFBox 2.x.
 */
public class PDFGenerator {
    
    private static final float MARGIN = 50;
    private static final float LINE_HEIGHT = 14;
    private static final float SECTION_SPACING = 20;
    
    /**
     * Generate application summary PDF
     */
    public void generateApplicationSummary(ApplicationBean appBean, String outputPath) 
            throws IOException {
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            float yPosition = page.getMediaBox().getHeight() - MARGIN;
            float pageWidth = page.getMediaBox().getWidth();
            
            // Title
            yPosition = drawTitle(contentStream, pageWidth, yPosition, 
                    "EGERTON UNIVERSITY");
            yPosition = drawSubtitle(contentStream, pageWidth, yPosition, 
                    "APPLICATION SUMMARY");
            yPosition -= SECTION_SPACING;
            
            // Generated date
            yPosition = drawText(contentStream, MARGIN, yPosition,
                    "Generated: " + LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")));
            yPosition -= SECTION_SPACING;
            
            // Personal Information Section
            yPosition = drawSectionHeader(contentStream, MARGIN, yPosition, 
                    "1. PERSONAL INFORMATION");
            
            ApplicantPersonal personal = appBean.getPersonal();
            if (personal != null) {
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Full Name:", personal.getFullName());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Date of Birth:", personal.getDateOfBirth() != null ? 
                                personal.getDateOfBirth().toString() : "");
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Gender:", personal.getGenderDisplay());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Nationality:", personal.getNationality());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "National ID:", personal.getNationalId() != null ? 
                                personal.getNationalId() : "N/A");
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Birth Cert No:", personal.getBirthCertNumber());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Phone:", personal.getPhoneNumber());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Email:", personal.getEmail());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "County:", personal.getCounty());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Town:", personal.getTown());
            }
            yPosition -= SECTION_SPACING;
            
            // Programme Selection Section
            yPosition = drawSectionHeader(contentStream, MARGIN, yPosition, 
                    "2. PROGRAMME SELECTION");
            
            ApplicantProgramme programme = appBean.getProgramme();
            if (programme != null) {
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Programme:", programme.getProgrammeName());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Level:", programme.getLevelDisplay());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Category:", programme.getCategoryDisplay());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Study Mode:", programme.getStudyModeDisplay());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Campus:", programme.getCampus());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Intake:", programme.getIntakeYear() + " " + programme.getIntakeSemester());
            }
            yPosition -= SECTION_SPACING;
            
            // Academic Background Section
            yPosition = drawSectionHeader(contentStream, MARGIN, yPosition, 
                    "3. ACADEMIC BACKGROUND (KCSE)");
            
            ApplicantAcademics academics = appBean.getAcademics();
            if (academics != null) {
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Index Number:", academics.getKcseIndexNumber());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Year of Exam:", String.valueOf(academics.getYearOfExam()));
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "School:", academics.getSchoolName());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "School Type:", academics.getSchoolTypeDisplay());
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Overall Grade:", academics.getOverallGradeDisplay());
                yPosition -= LINE_HEIGHT;
                
                // Subject grades
                yPosition = drawText(contentStream, MARGIN, yPosition, "Subject Grades:");
                List<SubjectGrade> grades = appBean.getSubjectGrades();
                if (grades != null && !grades.isEmpty()) {
                    for (SubjectGrade grade : grades) {
                        yPosition = drawText(contentStream, MARGIN + 20, yPosition,
                                "- " + grade.getSubjectName() + ": " + grade.getGrade());
                    }
                }
            }
            yPosition -= SECTION_SPACING;
            
            // Check if need new page
            if (yPosition < 200) {
                contentStream.close();
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = page.getMediaBox().getHeight() - MARGIN;
            }
            
            // Guardian Information Section
            yPosition = drawSectionHeader(contentStream, MARGIN, yPosition, 
                    "4. GUARDIAN INFORMATION");
            
            List<ApplicantGuardian> guardians = appBean.getGuardians();
            if (guardians != null && !guardians.isEmpty()) {
                for (ApplicantGuardian guardian : guardians) {
                    yPosition = drawText(contentStream, MARGIN, yPosition,
                            (guardian.isPrimary() ? "Primary" : "Secondary") + " Guardian:");
                    yPosition = drawLabelValue(contentStream, MARGIN + 20, yPosition, 
                            "Name:", guardian.getFullName());
                    yPosition = drawLabelValue(contentStream, MARGIN + 20, yPosition, 
                            "Relationship:", guardian.getRelationshipDisplay());
                    yPosition = drawLabelValue(contentStream, MARGIN + 20, yPosition, 
                            "Phone:", guardian.getPhoneNumber());
                }
            }
            yPosition -= SECTION_SPACING;
            
            // Extra Information Section
            yPosition = drawSectionHeader(contentStream, MARGIN, yPosition, 
                    "5. ADDITIONAL INFORMATION");
            
            ApplicantExtra extra = appBean.getExtra();
            if (extra != null) {
                yPosition = drawLabelValue(contentStream, MARGIN, yPosition, 
                        "Special Needs/Disability:", extra.getHasDisabilityDisplay());
                if (extra.isHasDisability() && extra.getDisabilityDescription() != null) {
                    yPosition = drawText(contentStream, MARGIN + 20, yPosition,
                            extra.getDisabilityDescription());
                }
            }
            yPosition -= SECTION_SPACING * 2;
            
            // Footer
            yPosition = drawText(contentStream, MARGIN, yPosition,
                    "This document was automatically generated by the Egerton University AMS.");
            drawText(contentStream, MARGIN, yPosition,
                    "For official use only. Verify all information against original documents.");
            
            contentStream.close();
            
            // Save document
            document.save(outputPath);
        }
    }
    
    private float drawTitle(PDPageContentStream cs, float pageWidth, float y, String text) 
            throws IOException {
        PDType1Font font = PDType1Font.HELVETICA_BOLD;
        cs.setFont(font, 18);
        float textWidth = font.getStringWidth(text) / 1000 * 18;
        cs.beginText();
        cs.newLineAtOffset((pageWidth - textWidth) / 2, y);
        cs.showText(text);
        cs.endText();
        return y - 22;
    }
    
    private float drawSubtitle(PDPageContentStream cs, float pageWidth, float y, String text) 
            throws IOException {
        PDType1Font font = PDType1Font.HELVETICA_BOLD;
        cs.setFont(font, 14);
        float textWidth = font.getStringWidth(text) / 1000 * 14;
        cs.beginText();
        cs.newLineAtOffset((pageWidth - textWidth) / 2, y);
        cs.showText(text);
        cs.endText();
        return y - 18;
    }
    
    private float drawSectionHeader(PDPageContentStream cs, float x, float y, String text) 
            throws IOException {
        PDType1Font font = PDType1Font.HELVETICA_BOLD;
        cs.setFont(font, 12);
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
        
        // Draw line under header
        y -= 3;
        cs.moveTo(x, y);
        cs.lineTo(x + 200, y);
        cs.stroke();
        
        return y - LINE_HEIGHT;
    }
    
    private float drawText(PDPageContentStream cs, float x, float y, String text) 
            throws IOException {
        if (text == null) text = "";
        cs.setFont(PDType1Font.HELVETICA, 10);
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
        return y - LINE_HEIGHT;
    }
    
    private float drawLabelValue(PDPageContentStream cs, float x, float y, 
                                  String label, String value) 
            throws IOException {
        if (value == null) value = "";
        PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;
        PDType1Font normalFont = PDType1Font.HELVETICA;
        
        // Draw label
        cs.setFont(boldFont, 10);
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(label);
        cs.endText();
        
        // Draw value
        float labelWidth = boldFont.getStringWidth(label) / 1000 * 10;
        cs.setFont(normalFont, 10);
        cs.beginText();
        cs.newLineAtOffset(x + labelWidth + 5, y);
        cs.showText(value);
        cs.endText();
        
        return y - LINE_HEIGHT;
    }
}
