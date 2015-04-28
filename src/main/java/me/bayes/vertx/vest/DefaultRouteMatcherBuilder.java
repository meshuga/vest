/**
 * Copyright 2013 Bayes Technologies
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.bayes.vertx.vest;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.apex.Route;
import io.vertx.ext.apex.Router;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ExceptionMapper;

import me.bayes.vertx.vest.binding.DefaultRouteBindingHolderFactory;
import me.bayes.vertx.vest.binding.Function;
import me.bayes.vertx.vest.binding.RouteBindingHolder.MethodBinding;
import me.bayes.vertx.vest.handler.ExceptionHandler;
import me.bayes.vertx.vest.handler.FilteredHttpServerRequestHandler;
import me.bayes.vertx.vest.handler.HttpServerRequestHandler;
import me.bayes.vertx.vest.util.DefaultParameterResolver;
import me.bayes.vertx.vest.util.ParameterResolver;
import me.bayes.vertx.vest.util.UriPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <pre>
 * The {@link DefaultRouteMatcherBuilder} is a basic {@link RouteMatcherBuilder} that
 * uses the {@link Application} to get classes that are candidates for adding to
 * the {@link RouteMatcher}.
 * 
 * This simple logic searches the given classes for the {@link Path} annotation
 * and uses that with the combination of the {@link Path} anotation on method level
 * to create the route that the method will handle. Once the path is established
 * a {@link Handler} is added to the route matcher for the specific path. The method
 * associated with the {@link Handler} is derived from either one of the {@link GET},
 * {@link POST}, {@link PUT}, {@link DELETE}, {@link OPTIONS} xor {@link HEAD} annotations
 * on the method.
 * 
 * Spec: 3.3.5 HEAD and OPTIONS is only supported for explicit request
 *
 * TODO: {@link Context} annotation support.
 * TODO: {@link Consumes}
 * TODO: {@link Produces}
 * TODO: Add verticle reference injection through {@link Context} annotation.
 * TODO: Add default behaviour for HEAD and OPTIONS according to 3.3.5.
 * TODO: Handle exceptions better. 3.3.4 Exceptions
 * TODO: 3.8 Determining the MediaType of Responses
 * TODO: Allow multiple endpoint implementation to have the same uri but different consumes and produces parameters.
 * </pre>
 * 
 * @author Kevin Bayes
 * @since 1.0
 * @version 1.0
 *
 */
public class DefaultRouteMatcherBuilder extends AbstractRouteMatcherBuilder {
	
	private static final Logger LOG = LoggerFactory.getLogger(DefaultRouteMatcherBuilder.class);
	
	private final ParameterResolver parameterResolver;

	public DefaultRouteMatcherBuilder(final VestApplication application) {
		super(application, new DefaultRouteBindingHolderFactory(application));
		this.parameterResolver = new DefaultParameterResolver(application);
	}

	/*
	 * (non-Javadoc)
	 * @see me.bayes.vertx.extension.RouteMatcherBuilder#build(me.bayes.vertx.extension.BuilderContext)
	 */
	protected Router buildInternal() throws Exception {

		addRoutes(router);
		
		return router;
	}
	/**
	 * The simplest case adds routes and delegates the execution to the method annotated with the {@link Path} annotation. 
	 * 
	 * @param router
	 * @throws Exception
	 */
	private void addRoutes(final Router router) throws Exception {
		final ObjectMapper objectMapper = application.getSingleton(ObjectMapper.class);

		this.bindingHolder.foreach(new Function() {
			public void apply(final HttpMethod method, final String key, final List<MethodBinding> bindings) throws Exception {
				
				Route route = null;
				final String finalPath = UriPathUtil.convertPath(key);
				
				//3.7 Matching Requests to Resource Methods is delegated to vertx router.
				switch (method) {
					case GET:
						route = router.get(finalPath);
						break;
					case DELETE:
						route = router.delete(finalPath);
						break;
					case HEAD:
						route = router.head(finalPath);
						break;
					case OPTIONS:
						route = router.options(finalPath);
						break;
					case POST:
						route = router.post(finalPath);
						break;
					case PUT:
						route = router.put(finalPath);
						break;
					case CONNECT:
					case PATCH:
					case TRACE:
						throw new IllegalArgumentException("Unsupported http method: " + method);
					default:
						throw new IllegalArgumentException("Unknown http method: " + method);
				}

				ExceptionHandler exceptionHandler = new ExceptionHandler(
						application.getProviders(ExceptionMapper.class), objectMapper);
				
				HttpServerRequestHandler requestHandler = new FilteredHttpServerRequestHandler(bindings, parameterResolver, objectMapper,
						application.getProviders(ContainerRequestFilter.class),
						application.getProviders(ContainerResponseFilter.class),
						exceptionHandler);
				route.handler(requestHandler);
			}
		});
	}


}