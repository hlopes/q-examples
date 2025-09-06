package org.hlopes;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.hlopes.dtos.ProductV2;

import java.util.List;

@Path("/v2/products")
public class ProductResourceV2 {

    @GET
    public Response getProducts() {
        List<ProductV2> products = List.of(new ProductV2("p1", "Laptop", "A powerful laptop for developers.", true));

        return Response.ok(products).build();
    }
}
