package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Status;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // TASK
        Task task1 = new Task("Task1", "Taskdes1");
        Task task2 = new Task("Task2", "Taskdes2");
        task1 = taskManager.createTask(task1);
        task2 = taskManager.createTask(task2);

        // EPIC
        Epic epic1 = new Epic("Epic1", "Epicdes1");
        epic1 = taskManager.createEpic(epic1);
        Epic epic2 = new Epic("Epic2", "Epicdes2");
        epic2 = taskManager.createEpic(epic2);

        // SUBTASK
        Subtask subtask1 = new Subtask(epic1.getTaskId(),"Sub1","Subdes1");
        Subtask subtask2 = new Subtask(epic1.getTaskId(),"Sub2","Subdes2");
        Subtask subtask3 = new Subtask(epic2.getTaskId(),"Sub1","Subdes1");
        subtask1 = taskManager.createSubTask(subtask1);
        subtask2 = taskManager.createSubTask(subtask2);
        subtask3 = taskManager.createSubTask(subtask3);


        //|1| Получение по ID//________________________+
//        System.out.println("_____Получение по ID_____");
//        // Task +
//        System.out.println("TASK:");
//        System.out.println(taskManager.getTaskById(task1.getTaskId()));
//        System.out.println(taskManager.getTaskById(task2.getTaskId()));
//        // Epic +
//        System.out.println("EPIC:");
//        System.out.println(taskManager.getEpicById(epic1.getTaskId()));
//        System.out.println(taskManager.getEpicById(epic2.getTaskId()));
//        // Subtask +
//        System.out.println("SUBTASK:");
//        System.out.println(taskManager.getSubTaskById(subtask1.getTaskId()));
//        System.out.println(taskManager.getSubTaskById(subtask2.getTaskId()));
//        System.out.println(taskManager.getSubTaskById(subtask3.getTaskId()));

        //|2| Получение всех задач//____________________+
//        System.out.println("_____Получение всех задач_____");
//        //Task +
//        System.out.println("TASK:");
//        System.out.println(taskManager.getAllTasks());
//        //Epic +
//        System.out.println("EPIC:");
//        System.out.println(taskManager.getAllEpics());
//        //Subtask +
//        System.out.println("SUBTASK:");
//        System.out.println(taskManager.getAllSubTasks());
//        //Получение списка всех задач определенного EPIC
//        System.out.println("Получение списка всех задач определенного EPIC:");
//        System.out.println(taskManager.getSubTasksOfEpic(epic1));
//        System.out.println(taskManager.getSubTasksOfEpic(epic2));

        //|3| Удаление по ID//__________________________ +
//        System.out.println("_____Удаление по ID_____");
//        //Task +
//        System.out.println("TASK:");
//        taskManager.removeTaskById(task1.getTaskId());
//        taskManager.removeTaskById(task2.getTaskId());
//        System.out.println(taskManager.getAllTasks());
//        //Epic +
//        System.out.println("EPIC:");
//        taskManager.removeEpicById(epic1.getTaskId());
//        taskManager.removeEpicById(epic2.getTaskId());
//        System.out.println(taskManager.getAllEpics());
//        //Subtask +
//        System.out.println("SUBTASK:");
//        taskManager.removeSubTaskById(subtask1.getTaskId());
//        taskManager.removeSubTaskById(subtask2.getTaskId());
//        taskManager.removeSubTaskById(subtask3.getTaskId());
//        System.out.println(taskManager.getAllSubTasks());
//        System.out.println(taskManager.getSubTasksOfEpic(epic1));
//        System.out.println(taskManager.getSubTasksOfEpic(epic2));
//        System.out.println(taskManager.getAllEpics());
//        System.out.println(taskManager.getAllSubTasks());

        //|4| Удаление всех задач//_____________________ +
//        System.out.println("_____Удаление всех задач_____");
//        //Task +
//        System.out.println("TASK:");
//        taskManager.removeAllTasks();
//        System.out.println(taskManager.getAllTasks());
//        //Epic +
//        System.out.println("EPIC:");
//        taskManager.removeAllEpics();
//        System.out.println(taskManager.getAllEpics());
//        //Subtask +
//        System.out.println("SUBTASK:");
//        taskManager.removeAllSubTasks();
//        System.out.println(taskManager.getAllSubTasks());


        //|5| Обновление//______________________________
//        System.out.println("_____Обновление задачи_____");
//        //Task +
//        System.out.println("TASK:");
//        Task modifiedTask1 = taskManager.getTaskById(task1.getTaskId());
//        modifiedTask1.setStatus(Status.IN_PROGRESS);
//        taskManager.updateTask(modifiedTask1);
//        System.out.println(task1);
//        //Epic +
//        System.out.println("EPIC:");
//        Epic modifiedEpic1 = taskManager.getEpicById(epic1.getTaskId());
//        modifiedEpic1.setStatus(Status.DONE);
//        taskManager.updateTask(modifiedEpic1);
//        System.out.println(epic1);
//        Epic modifiedEpic2 = taskManager.getEpicById(epic2.getTaskId());
//        modifiedEpic2.setStatus(Status.DONE);
//        taskManager.updateTask(modifiedEpic2);
//        System.out.println(epic2);
//        //Subtask +
//        System.out.println("SUBTASK:");
//        Subtask modifiedSubTask1 = taskManager.getSubTaskById(subtask1.getTaskId());
//        modifiedSubTask1.setStatus(Status.DONE);
//        Subtask modifiedSubTask2 = taskManager.getSubTaskById(subtask2.getTaskId());
//        modifiedSubTask2.setStatus(Status.DONE);
//        Subtask modifiedSubTask3 = taskManager.getSubTaskById(subtask3.getTaskId());
//        modifiedSubTask3.setStatus(Status.IN_PROGRESS);
//        taskManager.updateSubTask(modifiedSubTask1);
//        taskManager.updateSubTask(modifiedSubTask2);
//        taskManager.updateSubTask(modifiedSubTask3);
//        //taskManager.updateEpicStatus(epic1); // Модификация запрещена
//        System.out.println(taskManager.getSubTasksOfEpic(epic1));
//        System.out.println(taskManager.getAllEpics());
    }
}
