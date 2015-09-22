package com.washingtonpost.dropwizard.liveness;

import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>A {@code Runnable} that will emit a "1" to the gauge {@code livenessMetric} every {@code livenessFrequencySec}s</p>
 */
public class LivenessReporter implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LivenessReporter.class);
    private final StatsDClient statsdClient;
    private final String livenessMetric;
    private final int livenessFrequencySec;
    private boolean stop = false;

    LivenessReporter(StatsDClient statsdClient, String livenessMetric, int livenessFrequencySec) {
        this.statsdClient = statsdClient;
        this.livenessMetric = livenessMetric;
        this.livenessFrequencySec = livenessFrequencySec;
    }

    @Override
    public void run() {
        while (!stop) {
            logger.trace("Squawking a 1 against livenessMetric gauge {}", livenessMetric);
            this.statsdClient.gauge(livenessMetric, 1, (String[]) null);
            try {
                Thread.sleep(livenessFrequencySec * 1000);
            }
            catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Sets the run() loop control to false; this thread should stop after the current gauge writing/waiting loop completes.
     */
    public void stop() {
        this.stop = true;
    }

}
