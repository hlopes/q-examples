package org.hlopes;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/hello")
public class GreetingResource {

    public static String createSlugProperly(String input) {
        StringBuilder slug = new StringBuilder();
        input.codePoints().forEach(codePoint -> {
            if (Character.isLetterOrDigit(codePoint)) {
                slug.append(Character.toLowerCase(Character.toChars(codePoint)));
            }
        });
        return slug.toString();
    }

    @GET
    public List<Greeting> getAll() {
        return Greeting.listAll();
    }

    @POST
    @Transactional
    public Response add(Greeting greeting) {
        if (greeting.name != null && greeting.name.codePointCount(0, greeting.name.length()) > 6) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Name cannot exceed 6 characters\"}")
                    .build();
        }

        greeting.persist();

        return Response.status(Response.Status.CREATED).entity(greeting).build();
    }
}
