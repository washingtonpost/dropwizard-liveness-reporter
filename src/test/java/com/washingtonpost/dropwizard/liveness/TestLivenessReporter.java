package com.washingtonpost.dropwizard.liveness;

import com.timgroup.statsd.StatsDClient;
import static org.easymock.EasyMock.*;
import org.junit.Test;

/**
 * <p>Tests the liveness reporter</p>
 */
public class TestLivenessReporter {

    @Test
    public void testRun() throws InterruptedException {
        StatsDClient mockStatsdClient = createNiceMock(StatsDClient.class);
        mockStatsdClient.gauge("foo", 1);
        mockStatsdClient.gauge("foo", 1);
        mockStatsdClient.gauge("foo", 1);
        replay(mockStatsdClient);

        LivenessReporter reporter = new LivenessReporter(mockStatsdClient, "foo", 1);
        Thread reporterThread = new Thread(reporter);
        reporterThread.start();

        Thread.sleep(3000);

        reporter.stop();

        verify(mockStatsdClient);

    }

}
