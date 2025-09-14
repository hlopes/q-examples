package org.hlopes;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.hlopes.service.PersonService;

@Path("/hello")
public class GreetingResource {

    @Inject
    EntityManager entityManager;

    @Inject
    PersonService personService;

    @GET
    public Response hello() {
        var all = Person.<Person> listAll();

        Log.info("### " + all.get(0).getJson());

        var el = new MyJson.Element();
        el.setCode("abc");

        // Native SQL query
        String sql = "SELECT * FROM person p, jsonb_array_elements(p.json->'elements') as elem " +
                "WHERE elem->>'code' = :fieldValue";

        return Response.ok().entity(entityManager.createNativeQuery(sql, Person.class)
                .setParameter("fieldValue", el.getCode())
                .getResultList()).build();
    }

    @GET
    @Path("/retry")
    public Response withRetry() {
        try {
            return Response.ok(personService.getAllPersonsWithRetry()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("Service failed after retries: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/timeout")
    public Response withTimeout() {
        try {
            return Response.ok(personService.getAllPersonsWithTimeout()).build();
        } catch (TimeoutException e) {
            return Response.status(Response.Status.GATEWAY_TIMEOUT)
                    .entity("Service timed out: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/circuit-breaker")
    public Response withCircuitBreaker() {
        try {
            return Response.ok(personService.getAllPersonsWithCircuitBreaker()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("Circuit open or service failed: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/fallback/{id}")
    public Response withFallback(@PathParam("id") Long id) {
        return Response.ok(personService.getPersonById(id)).build();
    }

    @GET
    @Path("/bulkhead")
    public Response withBulkhead() {
        try {
            return Response.ok(personService.getAllPersonsWithBulkhead()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .entity("Too many concurrent requests: " + e.getMessage())
                    .build();
        }
    }
}
