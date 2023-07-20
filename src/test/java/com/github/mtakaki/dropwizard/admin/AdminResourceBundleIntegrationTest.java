package com.github.mtakaki.dropwizard.admin;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ExtendWith(DropwizardExtensionsSupport.class)
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
        private final AdminResourceBundle<TestConfiguration> adminResource = new AdminResourceBundle<>();

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

    private static final DropwizardTestSupport<TestConfiguration> TEST_SUPPORT = new DropwizardTestSupport<>(
            TestApplication.class, new TestConfiguration());
    private static final DropwizardAppExtension<TestConfiguration> DROPWIZARD = new DropwizardAppExtension<>(
            TEST_SUPPORT);

    @Test
    public void testGetAdmin() {
        final Model model = DROPWIZARD.client()
                .target(String.format("http://localhost:%d/admin/test", DROPWIZARD.getAdminPort()))
                .request().get(Model.class);
        assertThat(model).isEqualTo(new Model(1, "me"));
    }
}
