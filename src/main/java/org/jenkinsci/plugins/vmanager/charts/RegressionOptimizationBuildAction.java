package org.jenkinsci.plugins.vmanager.charts;

import hudson.model.Action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Per-build action that records the data for the
 * <strong>Runs Duration Chart</strong>: every run that belongs to the
 * build's vManager session(s), bucketed into Small / Medium / Large
 * thirds by duration. Two views are stored:
 *
 * <ul>
 *   <li>Start-time view: each point is {@code [startTimeMinutes, durationMinutes]}.</li>
 *   <li>End-time view:   each point is {@code [endTimeMinutes,   durationMinutes]}.</li>
 * </ul>
 *
 * <p>Hidden from the sidebar (null icon/display/url); the data is consumed
 * by {@link BuildChartAction#getRegressionOptimizationData()}.</p>
 */
public class RegressionOptimizationBuildAction implements Action, Serializable {

    private static final long serialVersionUID = 1L;

    // Start-time view (also the legacy fields kept for backward compat with
    // already-saved builds where these held the only stored series).
    private final List<double[]> small;
    private final List<double[]> medium;
    private final List<double[]> large;

    // End-time view (added later; may be null when reading legacy saved builds).
    private List<double[]> smallEnd;
    private List<double[]> mediumEnd;
    private List<double[]> largeEnd;

    // vManager session names that produced these points (added later;
    // may be null on legacy saved builds).
    private List<String> sessions;

    // Per-session warning (added later); keyed by session name; may be null
    // or absent for sessions that have no warning, and on legacy saved builds.
    private Map<String, String> sessionWarnings;

    public RegressionOptimizationBuildAction(List<double[]> small,
                                             List<double[]> medium,
                                             List<double[]> large,
                                             List<double[]> smallEnd,
                                             List<double[]> mediumEnd,
                                             List<double[]> largeEnd) {
        this(small, medium, large, smallEnd, mediumEnd, largeEnd, null, null);
    }

    public RegressionOptimizationBuildAction(List<double[]> small,
                                             List<double[]> medium,
                                             List<double[]> large,
                                             List<double[]> smallEnd,
                                             List<double[]> mediumEnd,
                                             List<double[]> largeEnd,
                                             List<String> sessions) {
        this(small, medium, large, smallEnd, mediumEnd, largeEnd, sessions, null);
    }

    public RegressionOptimizationBuildAction(List<double[]> small,
                                             List<double[]> medium,
                                             List<double[]> large,
                                             List<double[]> smallEnd,
                                             List<double[]> mediumEnd,
                                             List<double[]> largeEnd,
                                             List<String> sessions,
                                             Map<String, String> sessionWarnings) {
        this.small           = small     == null ? new ArrayList<>() : new ArrayList<>(small);
        this.medium          = medium    == null ? new ArrayList<>() : new ArrayList<>(medium);
        this.large           = large     == null ? new ArrayList<>() : new ArrayList<>(large);
        this.smallEnd        = smallEnd  == null ? new ArrayList<>() : new ArrayList<>(smallEnd);
        this.mediumEnd       = mediumEnd == null ? new ArrayList<>() : new ArrayList<>(mediumEnd);
        this.largeEnd        = largeEnd  == null ? new ArrayList<>() : new ArrayList<>(largeEnd);
        this.sessions        = sessions  == null ? new ArrayList<>() : new ArrayList<>(sessions);
        this.sessionWarnings = sessionWarnings == null
                ? new LinkedHashMap<>() : new LinkedHashMap<>(sessionWarnings);
    }

    public List<double[]> getSmall()  { return Collections.unmodifiableList(small);  }
    public List<double[]> getMedium() { return Collections.unmodifiableList(medium); }
    public List<double[]> getLarge()  { return Collections.unmodifiableList(large);  }

    public List<double[]> getSmallEnd()  {
        return Collections.unmodifiableList(smallEnd  == null ? Collections.<double[]>emptyList() : smallEnd);
    }
    public List<double[]> getMediumEnd() {
        return Collections.unmodifiableList(mediumEnd == null ? Collections.<double[]>emptyList() : mediumEnd);
    }
    public List<double[]> getLargeEnd()  {
        return Collections.unmodifiableList(largeEnd  == null ? Collections.<double[]>emptyList() : largeEnd);
    }

    public List<String> getSessions() {
        return Collections.unmodifiableList(sessions == null ? Collections.<String>emptyList() : sessions);
    }

    public Map<String, String> getSessionWarnings() {
        return Collections.unmodifiableMap(
                sessionWarnings == null ? Collections.<String, String>emptyMap() : sessionWarnings);
    }

    @Override public String getIconFileName() { return null; }
    @Override public String getDisplayName()  { return null; }
    @Override public String getUrlName()      { return null; }
}
