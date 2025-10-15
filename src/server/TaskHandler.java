package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager manager, Gson gson) {
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
        // /tasks или /tasks/{id}
        String[] parts = path.split("/");
        if (parts.length == 2) {
            List<Task> tasks = manager.getAllTasks();
            sendResponse(exchange, gson.toJson(tasks), 200);
        } else if (parts.length == 3) {
            int id = Integer.parseInt(parts[2]);
            Task task = manager.getTask(id);
            if (task != null) {
                sendResponse(exchange, gson.toJson(task), 200);
            } else {
                sendResponse(exchange, "Task not found", 404);
            }
        } else {
            sendResponse(exchange, "Invalid path", 400);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Task task = gson.fromJson(body, Task.class);

        if (task.getId() == 0 || manager.getTask(task.getId()) == null) {
            manager.createTask(task);
            sendResponse(exchange, "Task created", 201);
        } else {
            manager.updateTask(task);
            sendResponse(exchange, "Task updated", 200);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        if (parts.length == 2) {
            manager.deleteAllTasks();
            sendResponse(exchange, "All tasks deleted", 200);
        } else if (parts.length == 3) {
            int id = Integer.parseInt(parts[2]);
            manager.deleteTask(id);
            sendResponse(exchange, "Task deleted", 200);
        } else {
            sendResponse(exchange, "Invalid path", 400);
        }
    }

    // для чтения запроса
    protected String readRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}
