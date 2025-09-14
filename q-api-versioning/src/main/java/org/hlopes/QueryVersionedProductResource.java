package org.hlopes;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.hlopes.dtos.ProductV1;
import org.hlopes.dtos.ProductV2;

import java.util.List;

@Path("/products-query")
public class QueryVersionedProductResource {

    @GET
    public Response getProducts(@QueryParam("v") @DefaultValue("1") String version) {
        return switch (version) {
            case "1" -> Response.ok(List.of(new ProductV1("p1", "Laptop", "A powerful laptop for developers."))).build();
            case "2" -> Response.ok(List.of(new ProductV2("p1", "Laptop", "A powerful laptop for developers.", true)))
                    .build();
            default -> Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unsupported version. Please use '1' or '2'.")
                    .build();
        };
    }
}
