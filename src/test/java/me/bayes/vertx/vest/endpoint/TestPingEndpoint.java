package me.bayes.vertx.vest.endpoint;

import static org.junit.Assert.assertTrue;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class TestPingEndpoint {

//    private static final Logger LOG = LoggerFactory.getLogger(TestPingEndpoint.class);
//
//    private static final String JSON_CONFIG_CLASSES_TO_ADD = "{\"vestClasses\":[\"me.bayes.vertx.vest.endpoint.PingEndpoint\"]}";
//
//    Vertx vertx;
//    HttpServer server;
//
//    @Before
//    public void before(TestContext context) {
//        vertx = Vertx.vertx();
//        server =
//                vertx.createHttpServer().requestHandler(req -> req.response().end("foo")).
//                        listen(8080, context.async().handler());
//    }
//
//    @After
//    public void after(TestContext context) {
//        vertx.close(context.async().handler());
//    }
//
//    @Test
//    public void testPing() {
//
//        JsonObject config = new JsonObject(JSON_CONFIG_CLASSES_TO_ADD);
//
//        container.deployVerticle("me.bayes.vertx.vest.deploy.VestVerticle", config);
//
//        HttpClient client = vertx.createHttpClient();
//        client.setHost("localhost");
//        client.setPort(8080);
//
//        HttpClientRequest request = client.get("/ping", new Handler<HttpClientResponse>() {
//            public void handle(HttpClientResponse resp) {
//
//                resp.bodyHandler(new Handler<Buffer>() {
//                    public void handle(Buffer body) {
//
//                        final String payload = body.toString();
//
//                        VertxAssert.testComplete();
//
//                        assertTrue(payload.equals("ping"));
//                    }
//                });
//
//                String contentType = resp.headers().get(HttpHeaders.CONTENT_TYPE);
//                assertTrue(MediaType.TEXT_PLAIN.equals(contentType));
//            }
//        }).putHeader(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);
//
//        request.end();
//
//    }
//
}
