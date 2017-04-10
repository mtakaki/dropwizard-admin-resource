package com.github.mtakaki.dropwizard.admin;

import com.codahale.metrics.MetricRegistry;

import io.dropwizard.jersey.DropwizardResourceConfig;

public class DropwizardAdminResourceConfig extends DropwizardResourceConfig {
    private static final String NEWLINE = String.format("%n");

    public DropwizardAdminResourceConfig(final MetricRegistry metricRegistry) {
        super(false, metricRegistry);
    }

    @Override
    public String getEndpointsInfo() {
        final StringBuilder message = new StringBuilder("Registering admin resources");
        return message.append(NEWLINE).append(super.getEndpointsInfo()).toString();
    }
}