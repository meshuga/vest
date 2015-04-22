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
package me.bayes.vertx.vest.util;

import io.vertx.core.http.HttpServerRequest;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * The parameter handler resolves the value of the parameter.
 * 
 * Default implementation will be 
 * <ol>
 * <li>JaxrsAnnotatedParameterHandler</li>
 * <li>HttpServerResponseParameterHandler</li>
 * </ol>
 *  
 * @author Kevin Bayes
 */
public interface ParameterHandler<R> {

	/**
	 * <p>
	 * All parameter handlers implement this method to return a paramter resolved value.
	 * </p>
	 * 
	 * @param method
	 * @param parameterType
	 * @param annotations
	 * @param request
	 * @return
	 */
	R handle(final Method method, final Class<?> parameterType, final Annotation[] annotations, final HttpServerRequest request) throws IOException, ReflectiveOperationException;
}
