package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager manager;

    public HttpTaskServer() throws IOException {
        this.manager = Managers.getDefault();
        this.gson = new Gson();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Регистрируем обработчики
        server.createContext("/tasks", new TaskHandler(manager, gson));
        server.createContext("/epics", new EpicHandler(manager, gson));
        server.createContext("/subtasks", new SubtaskHandler(manager, gson));
        server.createContext("/history", new HistoryHandler(manager, gson));
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на порту " + PORT);
    }

    public void stop() {
        server.stop(1);
        System.out.println("HTTP-сервер остановлен.");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
    }
}
