package org.hlopes.rest;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.hlopes.dtos.request.CarFilter;
import org.hlopes.dtos.response.FilterOptions;
import org.hlopes.repository.CarRepository;

@Path("api/cars")
public class CarResource {

    private final CarRepository carRepository;

    public CarResource(final CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GET
    @Path("/filter-options")
    public FilterOptions getFilterOptions() {
        return carRepository.getFilterOptions();
    }

    @POST
    @Path("/search")
    public Response searchCars(
            final CarFilter filter,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("10") int pageSize) {
        var result = carRepository.search(filter, pageIndex, pageSize);

        return Response.ok(result.list())
                .header("X-Total-Count", result.totalCount())
                .build();
    }
}
