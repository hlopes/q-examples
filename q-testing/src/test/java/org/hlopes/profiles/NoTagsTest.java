package org.hlopes.profiles;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.hlopes.Profiles;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestProfile(Profiles.NoTags.class)
public class NoTagsTest {

  @Test
  void test() {
    Log.info("### Test Profile noTags");
  }
}
