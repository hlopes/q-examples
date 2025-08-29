package org.hlopes.profiles;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class NoQuarkusProfileTest {

  @Test
  void test() {
    Log.info("### without test profile");
  }
}
