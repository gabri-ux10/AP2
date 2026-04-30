package ke.ac.egerton.ams.models;

import java.io.Serializable;

/**
 * Model class for dashboard statistics.
 * Maps to: vw_dashboard_counts view
 */
public class DashboardCounts implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int totalApplications;
    private int pendingReview;
    private int underReview;
    private int accepted;
    private int rejected;
    private int drafts;
    
    // Default constructor
    public DashboardCounts() {
    }
    
    // Full constructor
    public DashboardCounts(int totalApplications, int pendingReview,
                           int underReview, int accepted, int rejected, int drafts) {
        this.totalApplications = totalApplications;
        this.pendingReview = pendingReview;
        this.underReview = underReview;
        this.accepted = accepted;
        this.rejected = rejected;
        this.drafts = drafts;
    }
    
    // Getters and Setters
    public int getTotalApplications() {
        return totalApplications;
    }
    
    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }
    
    public int getPendingReview() {
        return pendingReview;
    }
    
    public void setPendingReview(int pendingReview) {
        this.pendingReview = pendingReview;
    }
    
    public int getUnderReview() {
        return underReview;
    }
    
    public void setUnderReview(int underReview) {
        this.underReview = underReview;
    }
    
    public int getAccepted() {
        return accepted;
    }
    
    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }
    
    public int getRejected() {
        return rejected;
    }
    
    public void setRejected(int rejected) {
        this.rejected = rejected;
    }
    
    public int getDrafts() {
        return drafts;
    }
    
    public void setDrafts(int drafts) {
        this.drafts = drafts;
    }
    
    // Calculated fields
    public int getSubmittedTotal() {
        return totalApplications - drafts;
    }
    
    public int getDecidedTotal() {
        return accepted + rejected;
    }

    public int getReviewQueueTotal() {
        return pendingReview + underReview;
    }

    public double getAcceptanceRate() {
        return getSubmittedTotal() == 0 ? 0 : (accepted * 100.0) / getSubmittedTotal();
    }

    public double getRejectionRate() {
        return getSubmittedTotal() == 0 ? 0 : (rejected * 100.0) / getSubmittedTotal();
    }

    public int getAcceptanceRateRounded() {
        return (int) Math.round(getAcceptanceRate());
    }

    public int getRejectionRateRounded() {
        return (int) Math.round(getRejectionRate());
    }
    
    @Override
    public String toString() {
        return "DashboardCounts{" +
                "total=" + totalApplications +
                ", pending=" + pendingReview +
                ", underReview=" + underReview +
                ", accepted=" + accepted +
                ", rejected=" + rejected +
                '}';
    }
}
