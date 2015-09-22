package com.washingtonpost.dropwizard.liveness;

import com.timgroup.statsd.NoOpStatsDClient;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Tests our Configuration and builder does what is expected</p>
 */
public class TestLivenessConfiguration {

    private  LivenessConfiguration config;

    @Before
    public void setUp() {
        config = new LivenessConfiguration();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testTooFrequentChecksAreInvalid() {
        config.setLivenessFrequencySec(0);
    }

    @Test
    public void buildingWithNullHostReturnsNull() {
        config.setStatsdHost(null);
        config.setStatsdPort(8125);
        assertNull(config.build());
    }

    @Test
    public void buildingWithNullLivenessMetricReturnsNull() {
        config.setStatsdHost("statsd");
        config.setLivenessMetric(null);
        assertNull(config.build());
    }

    @Test
    public void buildingWithStatsDClientProvidedIsOkay() {
        config.setLivenessMetric("whatever");
        assertNotNull(config.build(new NoOpStatsDClient()));
    }
}
