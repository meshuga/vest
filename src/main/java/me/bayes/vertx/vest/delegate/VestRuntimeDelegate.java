package me.bayes.vertx.vest.delegate;

import me.bayes.vertx.vest.response.OutboundJaxrsResponse;
import me.bayes.vertx.vest.response.OutboundMessageContext;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.RuntimeDelegate;

public class VestRuntimeDelegate extends RuntimeDelegate {
    @Override
    public UriBuilder createUriBuilder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Response.ResponseBuilder createResponseBuilder() {
        return new OutboundJaxrsResponse.Builder(new OutboundMessageContext());
    }

    @Override
    public Variant.VariantListBuilder createVariantListBuilder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Link.Builder createLinkBuilder() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
