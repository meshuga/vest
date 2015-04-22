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
package me.bayes.vertx.vest.deploy;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.VertxFactoryImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.VertxFactory;
import me.bayes.vertx.vest.DefaultRouteMatcherBuilder;
import me.bayes.vertx.vest.RouteMatcherBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Bayes
 *
 */
public class Embedded {
	
	private static final String LISTEN_HOST = "host";
	private static final String LISTEN_PORT = "port";
	
	//Default values
	private static final int DEFAULT_PORT = 8080;
	
	private static final Logger LOG = LoggerFactory.getLogger(Embedded.class);
	
	public Vertx start(final JsonObject config) throws Exception {
		
		final Vertx vertx = new VertxFactoryImpl().vertx();
		final JsonArray vestPackagesToScan = config.getJsonArray("vestPackagesToScan");
		final JsonArray vestClasses = config.getJsonArray("vestClasses");
		final RootContextVestApplication application = new RootContextVestApplication();
		final HttpServer server = vertx.createHttpServer();
		final RouteMatcherBuilder builder = new DefaultRouteMatcherBuilder(application);
		final String listenHost = config.getString(LISTEN_HOST);
		final int listenPort = 
				(config.getInteger(LISTEN_PORT) == null) ? 
						DEFAULT_PORT : config.getInteger(LISTEN_PORT);

		//Add packages to scan
		if(vestPackagesToScan != null) {
			for(Object obj : vestPackagesToScan) {
				application.addPackagesToScan(String.valueOf(obj));
			}
		}
				
		//Add classes
		if(vestClasses != null) {
			for(Object obj : vestClasses) {
				try {
					Class<?> clazz = Class.forName((String)obj);
					application.addEndpointClasses(clazz);
				} catch (ClassNotFoundException e) {
					LOG.error("Could not load class " + obj + ".", e);
				}
			}
		}
		
		application.addSingleton(config);
		application.addSingleton(vertx);
		
		server.requestHandler(builder.build()::accept);
		
		//Set listen information
		if(listenHost == null) {
			server.listen(listenPort);
		} else {
			server.listen(listenPort, listenHost);
		}
		
		return vertx;
	}
	
	

}
