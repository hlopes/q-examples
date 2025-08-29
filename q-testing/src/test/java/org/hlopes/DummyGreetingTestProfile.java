package org.hlopes;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class DummyGreetingTestProfile implements QuarkusTestProfile {
  /**
   * Returns additional config to be applied to the test. This will override any existing config
   * (including in application.properties), however existing config will be merged with this (i.e.
   * application.properties config will still take effect, unless a specific config key has been
   * overridden).
   *
   * Here we are changing the Jakarta REST root path.
   */
  @Override
  public Map<String, String> getConfigOverrides() {
    return Collections.singletonMap("quarkus.resteasy.path", "/api");
  }

  /**
   * Returns enabled alternatives.
   *
   * This has the same effect as setting the 'quarkus.arc.selected-alternatives' config key, however
   * it may be more convenient.
   */
  //  @Override
  //  public Set<Class<?>> getEnabledAlternatives() {
  //    return Collections.singleton(MockGreetingService.class);
  //  }

  /**
   * Allows the default config profile to be overridden. This basically just sets the
   * quarkus.test.profile system property before the test is run.
   *
   * Here we are setting the profile to test-mocked
   */
  @Override
  public String getConfigProfile() {
    return "test-mocked";
  }

  /**
   * Additional {@link io.quarkus.test.common.QuarkusTestResourceLifecycleManager} classes (along with their init params)
   * to be used from this specific test profile.
   *
   * If this method is not overridden, then only the {@link io.quarkus.test.common.QuarkusTestResourceLifecycleManager}
   * classes enabled via the {@link io.quarkus.test.common.QuarkusTestResource} class annotation
   * will be used for the tests using this profile (which is the same behavior as tests that don't
   * use a profile at all).
   */
  //  @Override
  //  public List<TestResourceEntry> testResources() {
  //    return Collections.singletonList(new TestResourceEntry(CustomWireMockServerManager.class));
  //  }


  /**
   * If this returns true then only the test resources returned from {@link #testResources()} will
   * be started, global annotated test resources will be ignored.
   */
  @Override
  public boolean disableGlobalTestResources() {
    return false;
  }

  /**
   * The tags this profile is associated with. When the {@code quarkus.test.profile.tags} System
   * property is set (its value is a comma separated list of strings) then Quarkus will only execute
   * tests that are annotated with a {@code @TestProfile} that has at least one of the supplied (via
   * the aforementioned system property) tags.
   */
  @Override
  public Set<String> tags() {
    return Collections.emptySet();
  }

  /**
   * The command line parameters that are passed to the main method on startup.
   */
  @Override
  public String[] commandLineParameters() {
    return new String[0];
  }

  /**
   * If the main method should be run.
   */
  @Override
  public boolean runMainMethod() {
    return false;
  }

  /**
   * If this method returns true then all {@code StartupEvent} and {@code ShutdownEvent} observers
   * declared on application beans should be disabled.
   */
  @Override
  public boolean disableApplicationLifecycleObservers() {
    return false;
  }

  //  @Produces
  //  public ExternalService mockExternalService() {
  //    return new ExternalService("mock");
  //  }
}
