package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final TaskManager manager;
    protected final Gson gson;

    public BaseHttpHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    protected void sendResponse(HttpExchange exchange, String response, int code) throws IOException {
        exchange.sendResponseHeaders(code, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
