/**
 * 
 */
package me.bayes.vertx.vest.binding;

import io.vertx.core.http.HttpMethod;

import java.util.List;

import me.bayes.vertx.vest.binding.RouteBindingHolder.MethodBinding;

/**
 * @author Kevin Bayes
 *
 */
public interface Function {

	void apply(HttpMethod method, String key, List<MethodBinding> value) throws Exception;

}
