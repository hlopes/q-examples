package org.hlopes.problems;

import io.quarkiverse.resteasy.problem.HttpProblem;
import jakarta.ws.rs.core.Response;

public class OutOfStockProblem extends HttpProblem {
    public OutOfStockProblem(String message) {
        super(builder().withTitle("Bad hello request")
                .withStatus(Response.Status.BAD_REQUEST)
                .withDetail(message)
                .withHeader("X-RFC7807-Message", message)
                .with("hello", "world"));
    }
}
