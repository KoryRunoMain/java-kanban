package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.models.Status;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.TaskManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Program is not ready yet! Please wait...");

        // Test
        TaskManager taskManager = new TaskManager();


        // TASK
        System.out.println("TASK");
        Task task1 = new Task("Task1", "Taskdes1");
        Task task2 = new Task("Task2", "Taskdes2");
        task1 = taskManager.addTask(task1);
        task2 = taskManager.addTask(task2);

        System.out.println("Получение по обьекту:");
        System.out.println(task1);
        System.out.println("Получение по ID:");
        System.out.println(taskManager.getTaskById(task1.getTaskId()));
        System.out.println("Получение всех тасков: ");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Изменение статуса задачи:");
        Task modifiedTask1 = taskManager.getTaskById(task1.getTaskId());
        modifiedTask1.setStatus(Status.IN_PROGRESS);
        System.out.println("Статус изменен");
        System.out.println("Обновление задачи:");
        taskManager.updateTask(modifiedTask1);
        System.out.println(task1);
        System.out.println("Удаление по ID:");
        taskManager.removeTask(task1.getTaskId());
        System.out.println(taskManager.getAllTasks());
        System.out.println("Удаление всех задач:");
        taskManager.removeAllTasks();
        System.out.println(taskManager.getAllTasks());


//        // EPIC
//        Epic epic1 = new Epic("Epic1", "Epicdes1");
//        taskManager.addEpic(epic1);
//
//        System.out.println("EPIC");
//
//        System.out.println("1. Получение по обьекту:");
//
//        System.out.println("2. Получение по ID:");
//
//        System.out.println("Получение всех тасков: ");
//
//        System.out.println("Обновление задачи:");
//
//        System.out.println("Изменение статуса задачи:");
//
//        System.out.println("Удаление по ID:");
//
//        System.out.println("Удаление всех задач:");
//
//
//        // SUBTASK
//        Subtask subtask1 = new Subtask(1,"Sub1","Subdes1");
//        Subtask subtask2 = new Subtask(1,"Sub2","Subdes2");
//        Subtask subtask3 = new Subtask(2,"Sub1","Subdes1");
//        subtask1 = taskManager.addSubTask(subtask1);
//        subtask2 = taskManager.addSubTask(subtask2);
//        subtask3 = taskManager.addSubTask(subtask3);
//
//        System.out.println("SUBTASK");
//
//        System.out.println("1. Получение по обьекту:");
//
//        System.out.println("2. Получение по ID:");
//
//        System.out.println("Получение всех тасков: ");
//
//        System.out.println("Обновление задачи:");
//
//        System.out.println("Изменение статуса задачи:");
//
//        System.out.println("Удаление по ID:");
//
//        System.out.println("Удаление всех задач:");

    }
}
