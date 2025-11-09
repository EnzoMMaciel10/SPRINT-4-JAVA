package br.com.fiap.apoia;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import br.com.fiap.apoia.infrastructure.Database;
import br.com.fiap.apoia.application.controller.ApiController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        int port = 7070;
        String envPort = System.getenv("PORT");
        if (envPort != null) {
            try { port = Integer.parseInt(envPort); } catch (Exception ignored) {}
        }

        Database.init();

        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.plugins.enableDevLogging();
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
        });

// registra as rotas fora do config
        app.routes(ApiController::routes);

        app.get("/healthz", ctx -> ctx.result("ok"));
        app.get("/", ctx -> ctx.json(java.util.Map.of("name","Apoia+ API","hint","use /api")));



        app.exception(Exception.class, (Exception e, Context ctx) -> {
            log.error("Unhandled error", e);
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .json(new ErrorMessage(e.getMessage()));
        });

        app.start(port);
        log.info("Apoia+ API is running on port {}", port);
    }

    public record ErrorMessage(String error) {}
}
