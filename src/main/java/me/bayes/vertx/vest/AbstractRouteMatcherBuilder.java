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
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.apex.Router;
import me.bayes.vertx.vest.binding.RouteBindingHolder;
import me.bayes.vertx.vest.binding.RouteBindingHolderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Bayes
 */
public abstract class AbstractRouteMatcherBuilder implements RouteMatcherBuilder {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractRouteMatcherBuilder.class);

	protected VestApplication application;
	protected Router router;
	protected RouteBindingHolderFactory bindingHolderFactory;
	protected RouteBindingHolder bindingHolder;

	public AbstractRouteMatcherBuilder(VestApplication application,
			RouteBindingHolderFactory bindingHolderFactory) {
		super();
		this.application = application;
		Vertx vertx = Vertx.factory.vertx();
		this.router = Router.router(vertx);
		this.bindingHolderFactory = bindingHolderFactory;
		this.application.addSingleton(this.router, this.bindingHolder);
	}

	public VestApplication getApplication() {
		return application;
	}

	@Override
	public void setApplication(VestApplication application) {
		this.application = application;
	}
	

	@Override
	public Router build() throws Exception {
		
		if(application == null) {
			LOG.error("No application was set.");
			throw new Exception("No application available.");
		}
		
		buildBindings();
		
		return buildInternal();
	}
	
	/**
	 * This builds a holder of the bindings that will be used to create the {@link RouteMatcher}.
	 * 
	 * @throws Exception
	 */
	protected void buildBindings() throws Exception {
		this.bindingHolder = this.bindingHolderFactory.build();
	}
	
	/**
	 * Implement this method to add your logic.
	 * 
	 * @return a {@link RouteMatcher}
	 * @throws Exception
	 */
	protected abstract Router buildInternal() throws Exception; 
	
	/*
	 *  TODO: Add the implemetation required by the specification.
	 *  
	 * (non-Javadoc)
	 * @see me.bayes.vertx.vest.RouteMatcherBuilder#setNoRouteHandler(org.vertx.java.core.Handler)
	 */
	public void setNoRouteHandler(Handler<HttpServerRequest> handler) {
//		router.noMatch(handler); //TODO
	}
	
}
