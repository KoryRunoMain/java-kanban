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
        Epic epic3 = new Epic("Epic3", "Epicdes3");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        Subtask subtask1 = new Subtask(1,"Sub1","Subdes1");
        Subtask subtask2 = new Subtask(1,"Sub2","Subdes2");
        Subtask subtask3 = new Subtask(2,"Sub1","Subdes1");
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);

        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));

//        System.out.println(taskManager.getEpic(1));
//        System.out.println(taskManager.getEpic(2));
//        System.out.println(taskManager.getEpic(5));

//        System.out.println(taskManager.getSubTask(1));
//        System.out.println(taskManager.getSubTask(2));
//        System.out.println(taskManager.getSubTask(3));

        System.out.println();
        //taskManager.removeTask(1);
        //taskManager.removeAllTasks();
        //taskManager.updateTask(task2);
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getAllTasks());
        Task task3 = new Task("Task3", "Taskdes3");
        task2 = task3;
        taskManager.updateTask(task2);
        System.out.println("Обновление успешно: \n" + taskManager.getTask(2));
    }
}
