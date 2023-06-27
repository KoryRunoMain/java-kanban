package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.TaskManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Program is not ready yet! Please wait...");

        // Test
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Task1", "Taskdes1");
        Task task2 = new Task("Task2", "Taskdes2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Epic1", "Epicdes1");
        Epic epic2 = new Epic("Epic2", "Epicdes2");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask(1,"Sub1","Subdes1");
        Subtask subtask2 = new Subtask(1,"Sub2","Subdes2");
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);

        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getSubtaskListByEpicId(epic2));

    }
}
