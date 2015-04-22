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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Verticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;

/**
 * @author kevinbayes
 *
 */
public abstract class AbstractVestVerticle extends AbstractVerticle implements VestService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractVestVerticle.class);
	
	/* (non-Javadoc)
	 * @see org.vertx.java.deploy.Verticle#start()
	 */
	@Override
	public void start() {
		
		final JsonObject config = config();
		final HttpServer server = getVertx().createHttpServer();
		final String listenHost = config.getString(LISTEN_HOST);
		final int listenPort = 
				(config.getInteger(LISTEN_PORT) == null) ? 
						DEFAULT_PORT : config.getInteger(LISTEN_PORT);
		
		try {
		
		VestApplication application = createApplication(config);
		application.addSingleton(vertx);
		
		//A hook to add to the VestApplication once it has been created.
		postApplicationCreationProcess(application);
		
		final RouteMatcherBuilder routeBuilder = createBuilder(
				application);
		
		postRouteMatcherBuilderCreationProcess(routeBuilder);
		
		server.requestHandler(routeBuilder.build()::accept);
		
		//Set listen information
		if(listenHost == null) {
			server.listen(listenPort);
		} else {
			server.listen(listenPort, listenHost);
		}
		
		} catch (Exception ex) {
			LOG.error("Unable to start verticle due to exception.", ex);
		}
	}
	
	public void postRouteMatcherBuilderCreationProcess(RouteMatcherBuilder routeBuilder) { }

	/**
	 * Override this method to add post processing once the {@link VestApplication} has been created.
	 * 
	 * @param application that has just been created.
	 */
	public void postApplicationCreationProcess(final VestApplication application) { }
	
}
