import server.HttpTaskServer;

public class MainHttpServer {
    public static void main(String[] args) throws Exception {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }
}
