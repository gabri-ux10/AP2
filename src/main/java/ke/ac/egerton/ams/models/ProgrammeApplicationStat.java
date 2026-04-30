package ke.ac.egerton.ams.models;

import java.io.Serializable;

/**
 * View model for programme application analytics.
 */
public class ProgrammeApplicationStat implements Serializable {

    private static final long serialVersionUID = 1L;

    private String programmeName;
    private String schoolName;
    private int totalApplied;
    private int accepted;
    private int rejected;
    private int pending;
    private int rank;
    private int maxTotalApplied;

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getTotalApplied() {
        return totalApplied;
    }

    public void setTotalApplied(int totalApplied) {
        this.totalApplied = totalApplied;
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

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMaxTotalApplied() {
        return maxTotalApplied;
    }

    public void setMaxTotalApplied(int maxTotalApplied) {
        this.maxTotalApplied = maxTotalApplied;
    }

    public double getVolumePercentage() {
        return maxTotalApplied == 0 ? 0 : (totalApplied * 100.0) / maxTotalApplied;
    }

    public String getRankBadgeClass() {
        if (rank == 1) {
            return "rank-1";
        }
        if (rank == 2) {
            return "rank-2";
        }
        if (rank == 3) {
            return "rank-3";
        }
        return "rank-other";
    }
}
