package com.devops.app;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/** Tiny HTTP app used to demonstrate build, CI/CD and configuration management. */
public class App {

    public static final String VERSION = "1.0.0";

    public static String greeting(String name) {
        if (name == null || name.isBlank()) {
            name = "DevOps";
        }
        return "Hello, " + name + "! Built with Maven, deployed by Jenkins/Azure Pipelines + Ansible.";
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("APP_PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", ex -> send(ex, 200, greeting(null)));
        server.createContext("/health", ex -> send(ex, 200, "{\"status\":\"UP\",\"version\":\"" + VERSION + "\"}"));

        server.start();
        System.out.println("App started on port " + port + " (try http://localhost:" + port + "/health)");
    }

    private static void send(com.sun.net.httpserver.HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}
