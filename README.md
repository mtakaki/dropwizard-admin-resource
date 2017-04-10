# Status
![Build Status](https://codeship.com/projects/40f963a0-ffe5-0134-a631-32f3f6ee1f3c/status?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/mtakaki/dropwizard-admin-resource/badge.svg?branch=master)](https://coveralls.io/github/mtakaki/dropwizard-admin-resource?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/b6b6a9a48d334299ab49f012643bc046)](https://www.codacy.com/app/mitsuotakaki/dropwizard-hikaricp)
[![Download](https://maven-badges.herokuapp.com/maven-central/com.github.mtakaki/dropwizard-admin-resource/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mtakaki/dropwizard-admin-resource)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/com.github.mtakaki/dropwizard-admin-resource/badge.svg)](http://www.javadoc.io/doc/com.github.mtakaki/dropwizard-admin-resource)
[![Dependency Status](https://www.versioneye.com/user/projects/57e785c679806f002f4ac7e9/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57e785c679806f002f4ac7e9)

# dropwizard-admin-resource
This library provides the ability to register Jersey resources to the admin port.

Currently supporting dropwizard version `1.1.0`.

## Maven

The library is available at the maven central, so just add dependency to `pom.xml`:

```xml
<dependencies>
  <dependency>
    <groupId>com.github.mtakaki</groupId>
    <artifactId>dropwizard-admin-resource</artifactId>
    <version>1.1.0</version>
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
        this.adminResource.getJerseyEnvironment().register(new TestResource());
    }
}
```

The following will show on your logs when the admin resource is successfully registered:

```
INFO  [2017-04-10 04:39:22,177] io.dropwizard.jersey.DropwizardResourceConfig: Registering admin resources
The following paths were found for the configured resources:

    GET     /test (com.github.mtakaki.dropwizard.admin.AdminResourceBundleIntegrationTest.TestResource)

```
