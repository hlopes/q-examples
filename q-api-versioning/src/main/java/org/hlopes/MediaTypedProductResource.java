package org.hlopes;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.hlopes.dtos.ProductV1;
import org.hlopes.dtos.ProductV2;

import java.util.List;

@Path("/products-media")
public class MediaTypedProductResource {

    @GET
    @Produces("application/vnd.myapi.v1+json")
    public Response getProductsV1() {
        List<ProductV1> products = List.of(new ProductV1("p1", "Laptop", "A powerful laptop for developers."));

        return Response.ok(products).build();
    }

    @GET
    @Produces("application/vnd.myapi.v2+json")
    public Response getProductsV2() {
        List<ProductV2> products = List.of(new ProductV2("p1", "Laptop", "A powerful laptop for developers.", true));

        return Response.ok(products).build();
    }
}
