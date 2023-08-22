package ru.yandex.practicum.kanban.services.taskManagers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.CSVFormatHandler.CSVFormatHandler;
import ru.yandex.practicum.kanban.services.taskManagers.exceptions.ManagerSaveException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    /* Класс работает с форматом .CSV */
    private static final CSVFormatHandler handler = new CSVFormatHandler();

    private static final Path PATH = Path.of("src/resources/tasks.csv");
    private File file = new File(String.valueOf(PATH));

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    /* Сохранение задач и истории в файл */
    protected void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, UTF_8));
             BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                writer.write(handler.getHeader());
            }

            /* Сохранение задач */
            for (Task task: taskStorage.values()) {
                writer.write(handler.generateToString(task));
                writer.newLine();
            }
            for (Epic epic: epicStorage.values()) {
                writer.write(handler.generateToString(epic));
                writer.newLine();
            }
            for (Subtask subtask: subTaskStorage.values()) {
                writer.write(handler.generateToString(subtask));
                writer.newLine();
            }
            writer.newLine();

            /* Сохранение истории */
            writer.write(handler.generateHistoryToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл..");
        }
    }

    /* Восстановление данных из файла */
    public static FileBackedTasksManager loadFromFile(File file){
        FileBackedTasksManager fileBackedTasksManager = Managers.getFileBackedTasksManager();
        int initialID = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            /* Проверка на пустой файл */
            if (file.length() == 0) {
                return fileBackedTasksManager;
            }

            String tasksLine = "";
            boolean firstLine = reader.readLine().trim().equals(handler.getHeader()); // Переменная для пропуска заголовка

            /* Проверка первой строки файла на заголовок */
            while ((tasksLine = reader.readLine()) != null) {

                /* Пропустить заголовок */
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                /* Если пустая строка -> следующие записи для менеджера историии */
                if (tasksLine.isEmpty()) {
                    break;
                }

                /* Запись задач */
                Task task = handler.fromString(tasksLine);
                if (task == null) {
                    return fileBackedTasksManager;
                }

                /* Запись ID */
                if (task.getTaskId() > initialID) {
                    initialID = task.getTaskId();
                }

                if (task instanceof Epic epic) {
                    fileBackedTasksManager.createEpic(epicStorage.put(epic.getTaskId(), epic));
                } else if (task instanceof Subtask subtask) {
                    fileBackedTasksManager.createSubTask(subTaskStorage.put(subtask.getTaskId(), subtask));
                } else {
                    fileBackedTasksManager.createTask(taskStorage.put(task.getTaskId(), task));
                }
            }

            /* Запись истории просмотренных задач */
            String historyRow = reader.readLine();
            if (!(historyRow == null)) {
                for (int id : handler.historyFromString(historyRow)) {
                    if (taskStorage.containsKey(id)) {
                        historyManager.add(taskStorage.get(id));
                    }
                    if (epicStorage.containsKey(id)) {
                        historyManager.add(epicStorage.get(id));
                    }
                    if (subTaskStorage.containsKey(id)) {
                        historyManager.add(subTaskStorage.get(id));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла..");
        }
        return fileBackedTasksManager;
    }

    /* TASKS */
    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        saveToFile();
        return task;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        saveToFile();
        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        saveToFile();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        saveToFile();
    }

    @Override
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
        saveToFile();
    }

    /* EPICS */
    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        saveToFile();
        return epic;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        saveToFile();
        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        saveToFile();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        saveToFile();
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        saveToFile();
    }

    /* SUBTASKS */
    @Override
    public Subtask createSubTask(Subtask subtask) {
        super.createSubTask(subtask);
        saveToFile();
        return subtask;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = super.getSubTaskById(id);
        saveToFile();
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getAllSubTasks() {
        return super.getAllSubTasks();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        saveToFile();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        saveToFile();
    }

    @Override
    public void updateSubTask(Subtask updateSubtask) {
        super.updateSubTask(updateSubtask);
        saveToFile();
    }

    /* ТЕСТ */
    public static void main(String[] args) {
        FileBackedTasksManager manager = Managers.getFileBackedTasksManager();
//        loadFromFile(Paths.get("src/resources/tasks.csv").toFile());

//        Task task4 = new Task("Task0", "TaskDes22");
//        Task task5 = new Task("Task1", "TaskDes22");
//        Task task6 = new Task("Task3", "TaskDes22");
//
//        manager.createTask(task4);
//        manager.createTask(task5);
//        manager.createTask(task6);

        System.out.println(manager.getAllTasks());

        Task task1 = new Task("Task4", "TaskDes22");
        Task task2 = new Task("Task5", "TaskDes22");
        Task task3 = new Task("Task6", "TaskDes22");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        System.out.println(manager.getAllTasks());

        //loadFromFile(Paths.get("src/resources/tasks.csv").toFile());

        System.out.println(manager.getAllTasks());

    }

}
