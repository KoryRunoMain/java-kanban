package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Status;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.TaskManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Program is not ready yet! Please wait...");

        // Test
        TaskManager taskManager = new TaskManager();


        // TASK
        System.out.println("TASK:");
        Task task1 = new Task("Task1", "Taskdes1");
        Task task2 = new Task("Task2", "Taskdes2");
        task1 = taskManager.addTask(task1);
        task2 = taskManager.addTask(task2);

//        System.out.println("Получение по обьекту:");
//        System.out.println(task1);
//
//        System.out.println("Получение по ID:");
//        System.out.println(taskManager.getTaskById(task1.getTaskId()));
//
//        System.out.println("Получение всех тасков: ");
//        System.out.println(taskManager.getAllTasks());
//
//        System.out.println("Изменение статуса задачи:");
//        Task modifiedTask1 = taskManager.getTaskById(task1.getTaskId());
//        modifiedTask1.setStatus(Status.IN_PROGRESS);
//        System.out.println("Статус изменен");
//
//        System.out.println("Обновление задачи:");
//        taskManager.updateTask(modifiedTask1);
//        System.out.println(task1);
//
//        System.out.println("Удаление по ID:");
//        taskManager.removeTask(task1.getTaskId());
//        System.out.println(taskManager.getAllTasks());
//
//        System.out.println("Удаление всех задач:");
//        taskManager.removeAllTasks();
//        System.out.println(taskManager.getAllTasks());


        // EPIC
        Epic epic1 = new Epic("Epic1", "Epicdes1");
        epic1 = taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Epic2", "Epicdes2");
        epic2 = taskManager.addEpic(epic2);

//        System.out.println("EPIC:");
//        System.out.println("1. Получение по обьекту:");
//        System.out.println(epic1);
//
//        System.out.println("2. Получение по ID:");
//        System.out.println(taskManager.getEpicById(epic1.getTaskId()));
//
//        System.out.println("Получение всех эпиков: ");
//        System.out.println(taskManager.getAllEpics());
//
//        System.out.println("Изменение статуса задачи:");
//        Epic modified2 = taskManager.getEpicById(epic2.getTaskId());
//        modified2.setStatus(Status.DONE);
//        System.out.println("Статус изменить нельзя.");
//        System.out.println(epic2);
//
//        System.out.println("Обновление задачи:");
//        taskManager.updateTask(modified2);
//        System.out.println(epic2);
//
//        System.out.println("Удаление по ID:");
//        taskManager.removeEpic(epic2.getTaskId());
//        System.out.println(taskManager.getAllEpics());
//
//        System.out.println("Удаление всех задач:");
//        taskManager.removeAllEpics();
//        System.out.println(taskManager.getAllEpics());

        // SUBTASK
        Subtask subtask1 = new Subtask(epic1.getTaskId(),"Sub1","Subdes1");
        Subtask subtask2 = new Subtask(epic1.getTaskId(),"Sub2","Subdes2");
        Subtask subtask3 = new Subtask(epic2.getTaskId(),"Sub1","Subdes1");
        subtask1 = taskManager.addSubTask(subtask1);
        subtask2 = taskManager.addSubTask(subtask2);
        subtask3 = taskManager.addSubTask(subtask3);

        System.out.println("SUBTASK:");
        System.out.println("1. Получение по обьекту:");
        System.out.println(subtask1);

        System.out.println("2. Получение по ID:");
        System.out.println(taskManager.getSubTaskById(subtask1.getTaskId()));

        System.out.println("Получение всех тасков: ");
        System.out.println(taskManager.getAllSubTasks());

        System.out.println("Изменение статуса задачи:");
        Subtask modified1 = taskManager.getSubTaskById(subtask1.getTaskId());
        modified1.setStatus(Status.IN_PROGRESS);

        System.out.println("Обновление задачи:");
        taskManager.updateTask(modified1);
        System.out.println(subtask1);

        System.out.println("Удаление по ID:");
        taskManager.removeSubTask(subtask2.getTaskId());
        taskManager.removeSubTask(subtask1.getTaskId());
        System.out.println(taskManager.getAllSubTasks());

        System.out.println("Удаление всех задач:");
//        taskManager.removeAllSubTasks();
//        System.out.println(taskManager.getAllSubTasks());

        // Получение списка всех задач определенного EPIC
        System.out.println("Получение списка всех задач определенного EPIC:");
        System.out.println(taskManager.getSubTasksOfEpic(epic1));

    }
}
