package ru.yandex.practicum.kanban;


import com.google.gson.Gson;
import ru.yandex.practicum.kanban.httpServer.KVServer;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        TaskManager httpTaskManager;
        KVServer server;
        Gson gson;

        try {
            gson = Managers.getGson();
            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            httpTaskManager = Managers.getDefault(historyManager);

            // Таски
            Task task1 = new Task("T1", "D1", 5, Instant.ofEpochMilli(1703671200000L)); // 13:00
            httpTaskManager.createTask(task1);
            Epic epic1 = new Epic("E1", "E1", 10, Instant.ofEpochMilli(1703673000000L)); // 13:30
            httpTaskManager.createEpic(epic1);
            Subtask subtask1 = new Subtask(epic1.getId(), "S1", "S1", 5, Instant.ofEpochMilli(1703673900000L)); // 13:45
            httpTaskManager.createSubTask(subtask1);
            Subtask subtask2 = new Subtask(epic1.getId(), "S2", "S2", 5, Instant.ofEpochMilli(1703674500000L)); // 13:55
            httpTaskManager.createSubTask(subtask2);

            httpTaskManager.getTaskById(task1.getId());
            httpTaskManager.getEpicById(epic1.getId());
            httpTaskManager.getSubTaskById(subtask1.getId());

            System.out.println("Печать всех задач");
            System.out.println(gson.toJson(httpTaskManager.getAllTasks()));
            System.out.println("Печать всех эпиков");
            System.out.println(gson.toJson(httpTaskManager.getAllEpics()));
            System.out.println("Печать всех подзадач");
            System.out.println(gson.toJson(httpTaskManager.getAllSubTasks()));
            System.out.println("Загруженный менеджер");
            System.out.println(httpTaskManager);
            System.out.println("Subtasks OF Epic:");
            System.out.println(gson.toJson(httpTaskManager.getSubTasksOfEpic(epic1)));

            System.out.println("История просмотренных задач");
            System.out.println(gson.toJson(httpTaskManager.getHistory()));

            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
