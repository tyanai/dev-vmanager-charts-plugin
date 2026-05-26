package org.jenkinsci.plugins.vmanager.charts;

import hudson.model.Action;

import java.io.Serializable;

/**
 * Per-build action that records the data for the build-level
 * <strong>Run Anomalies</strong> chart: four stacked bars (Duration,
 * CPU Time, Max Memory Usage, Average Memory Usage), each split into
 * None / Unknown / Anomaly (critical) counts.
 *
 * <p>Computed at build completion by {@code CustomMetricsRunListener}
 * by chaining {@code /rest/sessions/list} (to translate session names
 * to ids and sum {@code total_runs_in_session}) and
 * {@code /rest/data-mining/get-sessions-exceptions-aggregated-counts}.</p>
 *
 * <p>Hidden from the sidebar (null icon/display/url); the data is
 * consumed by {@link BuildChartAction#getRunAnomaliesData()}.</p>
 */
public class RunAnomaliesBuildAction implements Action, Serializable {

    private static final long serialVersionUID = 1L;

    private final int totalRuns;
    private final int durationCritical;
    private final int durationUnknown;
    private final int cpuTimeCritical;
    private final int cpuTimeUnknown;
    private final int maxMemCritical;
    private final int maxMemUnknown;
    private final int avgMemCritical;
    private final int avgMemUnknown;

    public RunAnomaliesBuildAction(int totalRuns,
                                   int durationCritical, int durationUnknown,
                                   int cpuTimeCritical,  int cpuTimeUnknown,
                                   int maxMemCritical,   int maxMemUnknown,
                                   int avgMemCritical,   int avgMemUnknown) {
        this.totalRuns        = totalRuns;
        this.durationCritical = durationCritical;
        this.durationUnknown  = durationUnknown;
        this.cpuTimeCritical  = cpuTimeCritical;
        this.cpuTimeUnknown   = cpuTimeUnknown;
        this.maxMemCritical   = maxMemCritical;
        this.maxMemUnknown    = maxMemUnknown;
        this.avgMemCritical   = avgMemCritical;
        this.avgMemUnknown    = avgMemUnknown;
    }

    public int getTotalRuns()        { return totalRuns;        }
    public int getDurationCritical() { return durationCritical; }
    public int getDurationUnknown()  { return durationUnknown;  }
    public int getCpuTimeCritical()  { return cpuTimeCritical;  }
    public int getCpuTimeUnknown()   { return cpuTimeUnknown;   }
    public int getMaxMemCritical()   { return maxMemCritical;   }
    public int getMaxMemUnknown()    { return maxMemUnknown;    }
    public int getAvgMemCritical()   { return avgMemCritical;   }
    public int getAvgMemUnknown()    { return avgMemUnknown;    }

    /** Clamp to zero so a misreported total can't yield a negative "None" bar. */
    private static int noneFor(int total, int critical, int unknown) {
        int n = total - critical - unknown;
        return n < 0 ? 0 : n;
    }

    public int getDurationNone() { return noneFor(totalRuns, durationCritical, durationUnknown); }
    public int getCpuTimeNone()  { return noneFor(totalRuns, cpuTimeCritical,  cpuTimeUnknown);  }
    public int getMaxMemNone()   { return noneFor(totalRuns, maxMemCritical,   maxMemUnknown);   }
    public int getAvgMemNone()   { return noneFor(totalRuns, avgMemCritical,   avgMemUnknown);   }

    @Override public String getIconFileName() { return null; }
    @Override public String getDisplayName()  { return null; }
    @Override public String getUrlName()      { return null; }
}
