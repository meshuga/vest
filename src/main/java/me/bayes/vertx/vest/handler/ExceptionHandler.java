package me.bayes.vertx.vest.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpServerRequest;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.lang.reflect.Type;
import java.util.List;

public class ExceptionHandler {
    private static final Response.Status DEFAULT_STATUS = Response.Status.BAD_REQUEST;
    private List<ExceptionMapper> providers;
    private ObjectMapper objectMapper;

    public ExceptionHandler(List<ExceptionMapper> providers, ObjectMapper objectMapper) {
        this.providers = providers;
        this.objectMapper = objectMapper;
    }

    public void handle(Throwable throwable, HttpServerRequest request) {
        for(ExceptionMapper exceptionMapper : providers) {
            if (isAcceptable(exceptionMapper, throwable)) {
                Response response = exceptionMapper.toResponse(throwable);
                handleResponse(response, request);
                return;
            }
        }
        handleDefaultResponse(request);
    }

    private void handleDefaultResponse(HttpServerRequest request) {
        request.response().setStatusCode(DEFAULT_STATUS.getStatusCode());
        request.response().setStatusMessage(DEFAULT_STATUS.getReasonPhrase());
        request.response().end();
    }

    /**
     * TODO add support for wildcard generics and non-Oracle JVM implementations
     */
    private boolean isAcceptable(ExceptionMapper exceptionMapper, Throwable throwable) {
        for(Type genericInterface : exceptionMapper.getClass().getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedTypeImpl &&
                    ExceptionMapper.class.isAssignableFrom(((ParameterizedTypeImpl) genericInterface).getRawType())) {
                Type type = ((ParameterizedTypeImpl) genericInterface).getActualTypeArguments()[0];
                if (type instanceof Class &&
                        throwable.getClass().isAssignableFrom((Class<?>)type)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void handleResponse(Response response, HttpServerRequest request) {
        request.response().setStatusCode(response.getStatus());
        request.response().setStatusMessage(response.getStatusInfo().getReasonPhrase());

        if (response.getEntity() != null) {
            String jsonSerializedValue;
            try {
                jsonSerializedValue = objectMapper.writeValueAsString(response.getEntity());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            request.response().end(jsonSerializedValue);
        } else {
            request.response().end();
        }
    }
}
