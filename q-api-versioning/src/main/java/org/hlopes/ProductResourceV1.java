package org.hlopes;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.hlopes.dtos.ProductV1;

import java.util.List;

@Path("/v1/products")
@Tag(name = "Products (V1)", description = "Legacy product operations")
public class ProductResourceV1 {

    @GET
    @Operation(summary = "Get all products", description = "Returns products in v1 format.")
    public Response getProducts() {
        List<ProductV1> products = List.of(new ProductV1("p1", "Laptop", "A powerful laptop for developers."));

        return Response.ok(products).build();
    }
}
