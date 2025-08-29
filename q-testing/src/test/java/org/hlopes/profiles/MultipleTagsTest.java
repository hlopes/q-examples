package org.hlopes.profiles;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.hlopes.Profiles;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestProfile(Profiles.MultipleTags.class)
public class MultipleTagsTest {

  @Test
  public void test() {
    // test something
  }
}
