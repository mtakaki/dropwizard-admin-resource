# Status
[![CircleCI](https://circleci.com/gh/mtakaki/dropwizard-admin-resource/tree/master.svg?style=svg)](https://circleci.com/gh/mtakaki/dropwizard-admin-resource/tree/master)
[![Coverage Status](https://coveralls.io/repos/github/mtakaki/dropwizard-admin-resource/badge.svg?branch=master)](https://coveralls.io/github/mtakaki/dropwizard-admin-resource?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/09d257c610bc4aff8022e86e7ab02f90)](https://www.codacy.com/app/mitsuotakaki/dropwizard-admin-resource?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mtakaki/dropwizard-admin-resource&amp;utm_campaign=Badge_Grade)
[![Download](https://maven-badges.herokuapp.com/maven-central/com.github.mtakaki/dropwizard-admin-resource/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mtakaki/dropwizard-admin-resource)
[![Javadoc](http://javadoc.io/badge/com.github.mtakaki/dropwizard-admin-resource.svg)](http://www.javadoc.io/doc/com.github.mtakaki/dropwizard-admin-resource)

# dropwizard-admin-resource
This library provides the ability to register Jersey resources to the admin port.

Supported versions:

| Dropwizard  |  Admin resource |
|---|---|
| 1.1.0  | 1.1.0  |
| 1.1.4  | 1.1.4  |
| 1.2.2  | 1.2.2  |
| 1.3.8  | 1.3.8  |
| 2.0.0  | 2.0.0  |
| 2.0.9  | 2.0.9  |
| 4.0.1  | 4.0.1  |

## Maven

The library is available at the maven central, so just add dependency to `pom.xml`:

```xml
<dependencies>
  <dependency>
    <groupId>com.github.mtakaki</groupId>
    <artifactId>dropwizard-admin-resource</artifactId>
    <version>4.0.1</version>
  </dependency>
</dependencies>
```

## Adding admin resources

First you add the bundle to your application:
```java
public class TestApplication extends Application<TestConfiguration> {
    private final AdminResourceBundle adminResource = new AdminResourceBundle();

    @Override
    public void initialize(final Bootstrap<TestConfiguration> bootstrap) {
        bootstrap.addBundle(this.adminResource);
    }

    @Override
    public void run(final TestConfiguration configuration, final Environment environment)
            throws Exception {
        final JerseyEnvironment adminJerseyEnvironment = this.adminResourceBundle
                .getJerseyEnvironment();
        // Not necessary, but with this you can make sure you use the same settings
        // of your jackson mapper settings for both jersey environments.
        adminJerseyEnvironment.register(new JacksonBinder(environment.getObjectMapper()));
        adminJerseyEnvironment.register(new TestResource());
    }
}
```

The following will show on your logs when the admin resource is successfully registered:

```
INFO  [2017-04-10 04:39:22,177] io.dropwizard.jersey.DropwizardResourceConfig: Registering admin resources
The following paths were found for the configured resources:

    GET     /test (com.github.mtakaki.dropwizard.admin.AdminResourceBundleIntegrationTest.TestResource)

```
