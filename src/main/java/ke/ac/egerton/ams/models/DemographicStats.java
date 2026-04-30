package ke.ac.egerton.ams.models;

import java.io.Serializable;

/**
 * View model for dashboard demographic breakdowns.
 */
public class DemographicStats implements Serializable {

    private static final long serialVersionUID = 1L;

    private int maleCount;
    private int femaleCount;
    private int disabledCount;

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public int getDisabledCount() {
        return disabledCount;
    }

    public void setDisabledCount(int disabledCount) {
        this.disabledCount = disabledCount;
    }

    public int getTotalApplicants() {
        return maleCount + femaleCount;
    }

    public double getMalePercentage() {
        return getTotalApplicants() == 0 ? 0 : (maleCount * 100.0) / getTotalApplicants();
    }

    public double getFemalePercentage() {
        return getTotalApplicants() == 0 ? 0 : (femaleCount * 100.0) / getTotalApplicants();
    }

    public double getDisabledPercentage() {
        return getTotalApplicants() == 0 ? 0 : (disabledCount * 100.0) / getTotalApplicants();
    }

    public int getMalePercentageRounded() {
        return (int) Math.round(getMalePercentage());
    }

    public int getFemalePercentageRounded() {
        return (int) Math.round(getFemalePercentage());
    }

    public int getDisabledPercentageRounded() {
        return (int) Math.round(getDisabledPercentage());
    }
}
