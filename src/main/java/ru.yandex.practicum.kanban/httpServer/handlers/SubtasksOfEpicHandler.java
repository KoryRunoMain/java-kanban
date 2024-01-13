package ru.yandex.practicum.kanban.httpServer.handlers;

import ru.yandex.practicum.kanban.adapter.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class SubtasksOfEpicHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int ID_SYMBOL = 3;

    public SubtasksOfEpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                String valueId = query.substring(ID_SYMBOL);
                int id = Integer.parseInt(valueId);
                Epic epic = taskManager.getEpicById(id);
                String response = gson.toJson(taskManager.getSubTasksOfEpic(epic));
                writeResponse(exchange, response);
            }
            default -> exchange.sendResponseHeaders(400, 0);
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(200, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
