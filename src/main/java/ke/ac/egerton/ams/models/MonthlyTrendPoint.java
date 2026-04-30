package ke.ac.egerton.ams.models;

import java.io.Serializable;

/**
 * View model for officer dashboard monthly submission trend.
 */
public class MonthlyTrendPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    private String monthLabel;
    private int totalSubmitted;

    public MonthlyTrendPoint() {
    }

    public MonthlyTrendPoint(String monthLabel, int totalSubmitted) {
        this.monthLabel = monthLabel;
        this.totalSubmitted = totalSubmitted;
    }

    public String getMonthLabel() {
        return monthLabel;
    }

    public void setMonthLabel(String monthLabel) {
        this.monthLabel = monthLabel;
    }

    public int getTotalSubmitted() {
        return totalSubmitted;
    }

    public void setTotalSubmitted(int totalSubmitted) {
        this.totalSubmitted = totalSubmitted;
    }
}
