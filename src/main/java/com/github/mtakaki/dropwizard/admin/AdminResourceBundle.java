package com.github.mtakaki.dropwizard.admin;

import io.dropwizard.core.ConfiguredBundle;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.servlet.ServletContainer;

import com.codahale.metrics.jersey2.InstrumentedResourceMethodApplicationListener;

import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Bundle that gives the ability to register resources under the admin port.
 *
 * @author mtakaki
 */
@RequiredArgsConstructor
public class AdminResourceBundle<T> implements ConfiguredBundle<T> {
    private final String basePath;
    @Getter
    private JerseyEnvironment jerseyEnvironment;

    /**
     * Default base path, having the resources under "/admin".
     */
    public AdminResourceBundle() {
        this("/admin");
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        // There's nothing really to do here.
    }

    @Override
    public void run(final T config, final Environment environment) {
        this.jerseyEnvironment = this.setupAdminEnvironment(environment);
    }

    /**
     * Enables registering resource to the admin environment. The resources
     * registered under the returned {@link JerseyEnvironment} are only
     * accessible from the admin port.
     *
     * @param environment
     *            The application environment.
     * @return The admin environment.
     */
    private JerseyEnvironment setupAdminEnvironment(final Environment environment) {
        final DropwizardResourceConfig jerseyConfig = new DropwizardAdminResourceConfig(
                environment.metrics());
        final JerseyContainerHolder servletContainer = new JerseyContainerHolder(
                new ServletContainer(jerseyConfig));
        final JerseyEnvironment tempJerseyEnvironment = new JerseyEnvironment(servletContainer,
                jerseyConfig);

        // Our resources will be under the base path.
        environment.admin().addServlet("admin resources", servletContainer.getContainer())
                .addMapping(String.format("%s/*", this.basePath));

        // These are needed to hook up Timed, CircuitBreaker, etc.
        tempJerseyEnvironment.register(
                new InstrumentedResourceMethodApplicationListener(environment.metrics()));
        tempJerseyEnvironment.register(new RolesAllowedDynamicFeature());

        return tempJerseyEnvironment;
    }
}