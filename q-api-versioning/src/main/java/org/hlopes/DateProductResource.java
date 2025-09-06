package org.hlopes;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.hlopes.dtos.ProductV1;
import org.hlopes.dtos.ProductV2;

import java.util.List;

@Path("/products-date")
public class DateProductResource {

    @GET
    public Response getProducts(@HeaderParam("X-API-Version") String date) {
        if ("2024-05.01".equals(date)) {
            List<ProductV1> products = List.of(new ProductV1("p1", "Laptop", "A powerful laptop for developers."));

            return Response.ok(products).build();
        } else if ("2024-07-01".equals(date)) {
            List<ProductV2> products = List.of(new ProductV2("p1",
                    "Laptop",
                    "A powerful laptop for developers.",
                    true));

            return Response.ok(products).build();
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Unsupported version date. Please use '2024-05.01' or '2024-06.01'.")
                .build();

    }
}
