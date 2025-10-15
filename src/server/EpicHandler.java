package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET":
                    handleGet(exchange, path);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, path);
                    break;
                default:
                    sendResponse(exchange, "Unsupported method", 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, "Internal Server Error: " + e.getMessage(), 500);
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        if (parts.length == 2) {
            List<Epic> epics = manager.getAllEpics();
            sendResponse(exchange, gson.toJson(epics), 200);
        } else if (parts.length == 3) {
            int id = Integer.parseInt(parts[2]);
            Epic epic = manager.getEpic(id);
            if (epic != null) {
                sendResponse(exchange, gson.toJson(epic), 200);
            } else {
                sendResponse(exchange, "Epic not found", 404);
            }
        } else {
            sendResponse(exchange, "Invalid path", 400);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);

        if (epic.getId() == 0 || manager.getEpic(epic.getId()) == null) {
            manager.createEpic(epic);
            sendResponse(exchange, "Epic created", 201);
        } else {
            manager.updateEpic(epic);
            sendResponse(exchange, "Epic updated", 200);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        if (parts.length == 2) {
            manager.deleteAllEpics();
            sendResponse(exchange, "All epics deleted", 200);
        } else if (parts.length == 3) {
            int id = Integer.parseInt(parts[2]);
            manager.deleteEpic(id);
            sendResponse(exchange, "Epic deleted", 200);
        } else {
            sendResponse(exchange, "Invalid path", 400);
        }
    }
}
