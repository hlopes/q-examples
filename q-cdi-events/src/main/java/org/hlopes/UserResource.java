package org.hlopes;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.hlopes.service.UserService;

@Path("/users")
public class UserResource {

    @Inject
    UserService userService;

    @POST
    public Response register(final String username) {
        userService.registerUser(username);

        return Response.ok("User registered").build();
    }
}
