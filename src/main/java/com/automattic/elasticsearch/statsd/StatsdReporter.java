package com.automattic.elasticsearch.statsd;

import com.timgroup.statsd.StatsDClient;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ESLoggerFactory;

public abstract class StatsdReporter {

    private static final String DEFAULT_JOINER = ".";
    private static final Logger logger = ESLoggerFactory.getLogger(StatsdReporter.class.getName());
    private StatsDClient statsdClient;

    public StatsdReporter setStatsDClient(StatsDClient statsdClient) {
        this.statsdClient = statsdClient;
        return this;
    }

    public abstract void run();

    protected void sendGauge(String name, String valueName, long value) {
        this.statsdClient.gauge(this.join(name, valueName), value);
    }

    protected void sendGauge(String name, String valueName, double value) {
        this.statsdClient.gauge(this.join(name, valueName), value);
    }

    protected String sanitizeString(String s) {
        return s.replace(' ', '-');
    }

    protected String buildMetricName(String name) {
        return this.sanitizeString(name);
    }

    private String join(String... parts) {
        if (parts == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            builder.append(parts[i]);
            if (i < parts.length - 1) {
                builder.append(DEFAULT_JOINER);
            }
        }
        return builder.toString();
    }

    protected void logException(Exception e) {
        logger.warn("Error writing to StatsD", e);
    }
}
