package com.washingtonpost.dropwizard.liveness;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Creates a fake StatsD server on a localhost UDP port and makes sure the factory and resulting
 * liveness reporter push metrics to the port as expected.</p>
 */
public class IntTestLivenessReporter {

    private static final Logger logger = LoggerFactory.getLogger(LivenessReporter.class);


    @Test
    public void testThreadStartsAndWritesToGauge() {
        int port = 43627;
        DummyStatsDServer server = new DummyStatsDServer(port);

        LivenessConfiguration config = new LivenessConfiguration();
        config.setStatsdHost("localhost");
        config.setStatsdPort(port);
        config.setStatsdPrefix("foo");
        config.setLivenessMetric("alive");
        config.setLivenessFrequencySec(1);

        LivenessReporter reporter = null;
        try {
            reporter = config.buildAndRun();
            server.waitForMessage();
            Thread.sleep(3000);
            assertTrue("Expected message received to be 'foo.alive:1|g'", server.messagesReceived().contains("foo.alive:1|g"));
        } catch (InterruptedException ex) {
            logger.error("Exception waiting in test", ex);
        } finally {
            reporter.stop();
        }
    }


    /**
     * Copied from https://tinyurl.com/qjhydp4
     */
    private static final class DummyStatsDServer {
        private final List<String> messagesReceived = new ArrayList<>();
        private final DatagramSocket server;

        public DummyStatsDServer(int port) {
            try {
                logger.debug("Constructing DummyStatsDServer on port {}", port);
                this.server = new DatagramSocket(port);
            }
            catch (SocketException e) {
                throw new IllegalStateException(e);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final DatagramPacket packet = new DatagramPacket(new byte[256], 256);
                        server.receive(packet);
                        String message = new String(packet.getData(), Charset.forName("UTF-8")).trim();
                        messagesReceived.add(message);
                        logger.debug("DummyStatsDServer recieved message {}", message);
                    } catch (Exception e) { }
                }
            }).start();
        }

        public void stop() {
            server.close();
        }

        public void waitForMessage() {
            while (messagesReceived.isEmpty()) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {}
            }
        }

        public List<String> messagesReceived() {
            return new ArrayList<>(messagesReceived);
        }
    }
}
