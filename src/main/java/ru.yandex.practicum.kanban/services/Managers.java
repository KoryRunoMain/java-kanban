package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.adapter.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.kanban.httpServer.KVServer;
import ru.yandex.practicum.kanban.services.historyManagers.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.HttpTaskManager;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;
import ru.yandex.practicum.kanban.services.taskManagers.InMemoryTaskManager;

import java.io.IOException;
import java.time.Instant;

public class Managers {

//    public static TaskManager getDefault() {
//        return new InMemoryTaskManager(getDefaultHistory());
//    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HttpTaskManager getDefault() {
        return new HttpTaskManager(getDefaultHistory(), "http://localhost:" + KVServer.PORT);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantAdapter());
        return gsonBuilder.create();
    }

}
