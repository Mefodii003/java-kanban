package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager manager, Gson gson) {
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
            List<Subtask> subtasks = manager.getAllSubtasks();
            sendResponse(exchange, gson.toJson(subtasks), 200);
        } else if (parts.length == 3) {
            int id = Integer.parseInt(parts[2]);
            Subtask subtask = manager.getSubtask(id);
            if (subtask != null) {
                sendResponse(exchange, gson.toJson(subtask), 200);
            } else {
                sendResponse(exchange, "Subtask not found", 404);
            }
        } else {
            sendResponse(exchange, "Invalid path", 400);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);

        if (subtask.getId() == 0 || manager.getSubtask(subtask.getId()) == null) {
            manager.createSubtask(subtask);
            sendResponse(exchange, "Subtask created", 201);
        } else {
            manager.updateSubtask(subtask);
            sendResponse(exchange, "Subtask updated", 200);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        if (parts.length == 2) {
            manager.deleteAllSubtasks();
            sendResponse(exchange, "All subtasks deleted", 200);
        } else if (parts.length == 3) {
            int id = Integer.parseInt(parts[2]);
            manager.deleteSubtask(id);
            sendResponse(exchange, "Subtask deleted", 200);
        } else {
            sendResponse(exchange, "Invalid path", 400);
        }
    }
}
