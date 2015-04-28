package me.bayes.vertx.vest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import me.bayes.vertx.vest.sample.SampleExceptionMapper;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class ExceptionMapperTest {

    @Test
    public void onCorrectExceptionShouldReturnCustomResponse() {
        // Given
        ExceptionHandler sut = new ExceptionHandler(Collections.singletonList(new SampleExceptionMapper()), new ObjectMapper());
        HttpServerRequest request = mock(HttpServerRequest.class);
        HttpServerResponse response = mock(HttpServerResponse.class);

        // When
        when(request.response()).thenReturn(response);


        sut.handle(new RuntimeException(), request);

        // Then
        verify(response, times(1)).setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        verify(response, times(1)).setStatusMessage(Response.Status.BAD_REQUEST.getReasonPhrase());
        verify(response, times(1)).end("{}");
    }
}
