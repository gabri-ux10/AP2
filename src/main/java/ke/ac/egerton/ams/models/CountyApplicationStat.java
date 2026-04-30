package ke.ac.egerton.ams.models;

import java.io.Serializable;

/**
 * View model for dashboard county application counts.
 */
public class CountyApplicationStat implements Serializable {

    private static final long serialVersionUID = 1L;

    private String county;
    private int applicantCount;

    public CountyApplicationStat() {
    }

    public CountyApplicationStat(String county, int applicantCount) {
        this.county = county;
        this.applicantCount = applicantCount;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getApplicantCount() {
        return applicantCount;
    }

    public void setApplicantCount(int applicantCount) {
        this.applicantCount = applicantCount;
    }
}
