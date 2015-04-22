package me.bayes.vertx.vest.endpoint;

import static org.junit.Assert.assertTrue;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(VertxUnitRunner.class)
public class JsonObjectParameterTest {

    private static final Logger LOG = LoggerFactory.getLogger(JsonObjectParameterTest.class);

    private static final String JSON_CONFIG_CLASSES_TO_ADD = "{\"vestClasses\":[\"me.bayes.vertx.vest.endpoint.EchoEndpoint\"]}";

    Vertx vertx;
    HttpServer server;

    @Before
    public void before(TestContext context) {
        vertx = Vertx.vertx();
        server =
                vertx.createHttpServer().requestHandler(req -> req.response().end("foo")).
                        listen(8080, context.async().handler());
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.async().handler());
    }

    @Test
    public void testHandle(TestContext context) {

        final String echoJson = "{\"test\":\"test\"}";

        JsonObject config = new JsonObject(JSON_CONFIG_CLASSES_TO_ADD);

        vertx.deployVerticle("me.bayes.vertx.vest.deploy.VestVerticle", new DeploymentOptions(config));

        HttpClientOptions options = new HttpClientOptions();
        options.setDefaultPort(8080);
        HttpClient client = vertx.createHttpClient(options);

        client.get("/echo", new Handler<HttpClientResponse>() {
            public void handle(HttpClientResponse resp) {

                resp.bodyHandler(new Handler<Buffer>() {
                    public void handle(Buffer body) {

                        final String payload = body.toString();

                        context.async().complete();

                        assertTrue(payload.equals(echoJson));
                    }
                });

                String contentType = resp.headers().get(HttpHeaders.CONTENT_TYPE);
                assertTrue(MediaType.APPLICATION_JSON.equals(contentType));
            }
        }).putHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setChunked(true)
                .write(echoJson)
                .end();

    }

}
