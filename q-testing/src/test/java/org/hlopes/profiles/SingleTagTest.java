package org.hlopes.profiles;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.hlopes.Profiles;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestProfile(Profiles.SingleTag.class)
public class SingleTagTest {

  @Test
  public void test() {
    // test something
  }
}
