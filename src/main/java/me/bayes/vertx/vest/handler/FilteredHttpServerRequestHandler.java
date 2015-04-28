package me.bayes.vertx.vest.handler;

import io.vertx.ext.apex.RoutingContext;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;

import me.bayes.vertx.vest.binding.RouteBindingHolder.MethodBinding;
import me.bayes.vertx.vest.handler.context.VertxContainerRequestContext;
import me.bayes.vertx.vest.util.ParameterResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FilteredHttpServerRequestHandler extends HttpServerRequestHandler {

	private static final Logger LOG = LoggerFactory.getLogger(FilteredHttpServerRequestHandler.class);
	private List<ContainerRequestFilter> containerRequestFilters;
	private List<ContainerResponseFilter> containerResponseFilters;
	private ExceptionHandler exceptionHandler;

	public FilteredHttpServerRequestHandler(List<MethodBinding> bindings, ParameterResolver parameterResolver,
											ObjectMapper objectMapper, List<ContainerRequestFilter> containerRequestFilters,
											List<ContainerResponseFilter> containerResponseFilters,
											ExceptionHandler exceptionHandler) {
		super(bindings, parameterResolver, objectMapper, exceptionHandler);
		this.containerRequestFilters = containerRequestFilters;
		this.containerResponseFilters = containerResponseFilters;
		this.exceptionHandler = exceptionHandler;
	}
	
	@Override
	public void handle(RoutingContext routingContext) {
		ContainerRequestContext requestContext = new VertxContainerRequestContext(routingContext.request());
		for (ContainerRequestFilter containerRequestFilter : containerRequestFilters) {
			try {
				LOG.info("Run filted: " + containerRequestFilter);
				containerRequestFilter.filter(requestContext);
			} catch (IOException e) {
				LOG.error("Exception while applying filters", e);
				exceptionHandler.handle(e, routingContext.request());
			}
		}
		super.handle(routingContext);
		
		//TODO response filters
	}

}
