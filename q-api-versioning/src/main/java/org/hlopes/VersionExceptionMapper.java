package org.hlopes;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hlopes.exceptions.UnsupportedVersionException;

import java.util.Map;

@Provider
public class VersionExceptionMapper implements ExceptionMapper<UnsupportedVersionException> {
    @Override
    public Response toResponse(final UnsupportedVersionException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                        "error", "Unsupported API version requested.",
                        "message", exception.getMessage(),
                        "supportedVersions", new String[] { "1", "2" }))
                .build();
    }
}
