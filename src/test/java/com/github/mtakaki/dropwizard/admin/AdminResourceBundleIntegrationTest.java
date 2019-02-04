package com.github.mtakaki.dropwizard.admin;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AdminResourceBundleIntegrationTest {
    public static class TestConfiguration extends Configuration {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Model {
        private int id;
        private String name;
    }

    @Path("/test")
    public static class TestResource {
        @GET
        @Consumes
        @Produces(MediaType.APPLICATION_JSON)
        public Model get() {
            return new Model(1, "me");
        }
    }

    public static class TestApplication extends Application<TestConfiguration> {
        private final AdminResourceBundle adminResource = new AdminResourceBundle();

        @Override
        public void initialize(final Bootstrap<TestConfiguration> bootstrap) {
            bootstrap.addBundle(this.adminResource);
        }

        @Override
        public void run(final TestConfiguration configuration, final Environment environment)
                throws Exception {
            this.adminResource.getJerseyEnvironment().register(new TestResource());
        }
    }

    @Rule
    public final DropwizardAppRule<TestConfiguration> RULE = new DropwizardAppRule<>(
            TestApplication.class,
            ResourceHelpers.resourceFilePath("config.yml"));

    private Client client;

    @Before
    public void createClient() {
        final JerseyClientConfiguration configuration = new JerseyClientConfiguration();
        configuration.setTimeout(Duration.minutes(1L));
        configuration.setConnectionTimeout(Duration.minutes(1L));
        configuration.setConnectionRequestTimeout(Duration.minutes(1L));
        this.client = new JerseyClientBuilder(this.RULE.getEnvironment()).using(configuration)
                .build("test client");
    }

    @Test
    public void testGetAdmin() {
        final Model model = this.client
                .target(String.format("http://localhost:%d/admin/test", this.RULE.getAdminPort()))
                .request().get(Model.class);
        assertThat(model).isEqualTo(new Model(1, "me"));
    }
}
