package org.hlopes;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.callback.QuarkusTestBeforeEachCallback;
import io.quarkus.test.junit.callback.QuarkusTestMethodContext;

public class MyQuarkusTestBeforeEachCallback implements QuarkusTestBeforeEachCallback {
  @Override
  public void beforeEach(QuarkusTestMethodContext context) {
    Log.info("### Executing " + context.getTestMethod());
  }
}
