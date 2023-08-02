package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.services.interfaces.TaskManager;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());

        Task task1 = new Task("Task1", "Taskdes1");
        Task task2 = new Task("Task2", "Taskdes2");
        task1 = taskManager.createTask(task1);
        task2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Epicdes1");
        Epic epic2 = new Epic("Epic2", "Epicdes2");
        epic1 = taskManager.createEpic(epic1);
        epic2 = taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask(epic1.getTaskId(),"Sub1","Subdes1");
        Subtask subtask2 = new Subtask(epic1.getTaskId(),"Sub2","Subdes2");
        Subtask subtask3 = new Subtask(epic1.getTaskId(),"Sub1","Subdes1");


        System.out.println("Запись.История просмотров:");
        System.out.println(taskManager.getHistory());

        taskManager.getEpicById(epic1.getTaskId());
        taskManager.getSubTaskById(subtask1.getTaskId());
        taskManager.getSubTaskById(subtask2.getTaskId());
        taskManager.getSubTaskById(subtask3.getTaskId());

        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());
    }
}
