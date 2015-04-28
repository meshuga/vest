package me.bayes.vertx.vest.sample;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;

@Provider
public class SampleExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new HashMap())
                .build();
    }
}
