
package poc.cvgaviao.backend.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class HttpVerticle extends AbstractVerticle {

    /**
     * The class Logger
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(HttpVerticle.class);

    @Override
    public void start() {
        String id = this.context.deploymentID();
        LOG.info("Starting verticle instance: {}", id);

        vertx.createHttpServer().requestHandler(setupRouter()).listen(8085,
                listen -> {
                    if (listen.succeeded()) {
                        LOG.info("Server listening on {}:{}",
                                "http://localhost",
                                listen.result().actualPort());
                    } else {
                        LOG.error("Failure while starting a verticle",
                                listen.cause());
                        System.exit(1);
                    }
                });
    }

    private Router setupRouter() {
        Router router = Router.router(vertx);
        
        router.get("/").handler(rountingContext-> rountingContext.response().end("Hello "));
        
        return router;
    }
}
