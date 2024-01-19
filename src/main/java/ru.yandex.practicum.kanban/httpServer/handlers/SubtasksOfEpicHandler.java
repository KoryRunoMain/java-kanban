package ru.yandex.practicum.kanban.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SubtasksOfEpicHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int ID_SYMBOL = 3;
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksOfEpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException,
            NumberFormatException, StringIndexOutOfBoundsException, NullPointerException {

        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        switch (method) {
            case "GET" -> {
                String s = query.substring(query.indexOf("id=") + ID_SYMBOL);
                int id = Integer.parseInt(s);
                String jsonString = gson.toJson(taskManager.getSubTaskById(id));
                writeResponse(exchange, jsonString, 200);
            }

            default -> writeResponse(exchange, "Запрос не может быть обработан", 400);
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
