package com.washingtonpost.dropwizard.liveness;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.timgroup.statsd.NonBlockingStatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Dropwizard-style configuration class for our Liveness Squawker</p>
 */
public class LivenessConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(LivenessReporter.class);

    @JsonProperty
    private String statsdHost;
    @JsonProperty
    private Integer statsdPort = 8125;
    @JsonProperty
    private String statsdPrefix;
    @JsonProperty
    private String livenessMetric;
    @JsonProperty
    private int livenessFrequencySec = 30;

    public String getStatsdHost() {
        return statsdHost;
    }

    public void setStatsdHost(String statsdHost) {
        this.statsdHost = statsdHost;
    }

    public int getStatsdPort() {
        return statsdPort;
    }

    public void setStatsdPort(int statsdPort) {
        this.statsdPort = statsdPort;
    }

    public String getStatsdPrefix() {
        return statsdPrefix;
    }

    public void setStatsdPrefix(String statsdPrefix) {
        this.statsdPrefix = statsdPrefix;
    }

    public String getLivenessMetric() {
        return livenessMetric;
    }

    public void setLivenessMetric(String livenessMetric) {
        this.livenessMetric = livenessMetric;
    }

    public Integer getLivenessFrequencySec() {
        return livenessFrequencySec;
    }

    public void setLivenessFrequencySec(Integer livenessFrequencySec) {
        if (livenessFrequencySec < 1) {
            throw new IllegalArgumentException("Liveness frequencies of less than 1 second is not supported");
        }
        this.livenessFrequencySec = livenessFrequencySec;
    }

    /**
     * @return A new LivenessReporter that has had its thread started
     */
    public LivenessReporter buildAndRun() {
        LivenessReporter reporter = build();
        if (reporter != null) {
            Thread reporterThread = new Thread(reporter);
            reporterThread.start();
        }
        return reporter;
    }

    /**
     * @return A new LivenessReporter (just a thread, really) that has not yet been started
     */
    public LivenessReporter build() {
        if (statsdHost == null || statsdPort == null) {
            logger.info("StatsHost or StatsdPort is null, skipping construction of the LivenessReporter");
            return null;
        }
        if (livenessMetric == null) {
            logger.info("livenessMetric, skipping construction of the LivenessReporter");
            return null;
        }
        logger.info("Constructing StatsDClient for '{}' on port '{}'", statsdHost, statsdPort);
        NonBlockingStatsDClient statsdClient = new NonBlockingStatsDClient(statsdPrefix, statsdHost, statsdPort);
        return new LivenessReporter(statsdClient, this.livenessMetric, this.livenessFrequencySec);
    }
}
