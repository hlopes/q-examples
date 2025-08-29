package com.example.fullstack.project;

import com.example.fullstack.task.Task;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ProjectResourceTest {

    @Test
    @TestSecurity(user = "user", roles = "user")
    void delete() {
        var toDelete = RestAssured.given().body("{\"name\":\"to-delete\"}").
                                  contentType(ContentType.JSON)
                                  .post("/api/v1/projects").as(Project.class);

        var dependentTask = RestAssured.given()
                                       .body(
                                           "{\"title\":\"dependent-task\",\"project\":        {\"id\":" + toDelete.id + "}}")
                                       .contentType(ContentType.JSON)
                                       .post("/api/v1/tasks").as(Task.class);

        RestAssured.given()
                   .when().delete("/api/v1/projects/" + toDelete.id)
                   .then()
                   .statusCode(204);

        MatcherAssert.assertThat(Task.<Task>findById(dependentTask.id)
                                     .await().
                                     indefinitely().project, Matchers.nullValue());
    }

}
