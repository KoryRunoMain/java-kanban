package ru.yandex.practicum.kanban.services.taskManagers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.CSVFormatHandler.CSVFormatHandler;
import ru.yandex.practicum.kanban.services.taskManagers.exceptions.ManagerSaveException;

import static java.nio.charset.StandardCharsets.UTF_8;
//import static ru.yandex.practicum.kanban.services.Managers.getDefaultFileBackedTasksManager;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private static final Path PATH = Path.of("src/resources/tasks.csv");
    private static File file = new File(String.valueOf(PATH));
    private static final CSVFormatHandler handler = new CSVFormatHandler();

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }


    /* ТЕСТ */
    public static void main(String[] args) {
//        FileBackedTasksManager manager = Managers.getDefaultFileBackedTasksManager();
        FileBackedTasksManager manager = Managers.getDefaultFileBackedTasksManager(file);
        manager.loadFromFile(Paths.get("src/resources/tasks.csv").toFile());

//        Task task1 = new Task("Task1", "Taskdes1");
//        Task task2 = new Task("Task2", "Taskdes2");
//        task1 = manager.createTask(task1);
//        task2 = manager.createTask(task2);
//
//        Epic epic1 = new Epic("Epic1", "Epicdes1");
//        Epic epic2 = new Epic("Epic2", "Epicdes2");
//        epic1 = manager.createEpic(epic1);
//        epic2 = manager.createEpic(epic2);
//
//        Subtask subtask1 = new Subtask(epic1.getTaskId(),"Sub1","Subdes1");
//        Subtask subtask2 = new Subtask(epic2.getTaskId(),"Sub2","Subdes2");
//        Subtask subtask3 = new Subtask(epic2.getTaskId(),"Sub1","Subdes1");
//        subtask1 = manager.createSubTask(subtask1);
//        subtask2 = manager.createSubTask(subtask2);
//        subtask3 = manager.createSubTask(subtask3);
//
////        Subtask modified1 = manager1.getSubTaskById(subtask1.getTaskId());
////        modified1.setStatus(Status.DONE);
//
////        System.out.println("Запись.История просмотров:");
////        System.out.println(manager1.getHistory());
//
//        manager.getEpicById(epic1.getTaskId());
//        manager.getEpicById(epic2.getTaskId());
//        manager.getEpicById(task1.getTaskId());
//
//
//        System.out.println("История просмотров:");
//        System.out.println(manager.getHistory());

//        System.out.println(manager.getAllTasks());
    }


    /* Запись в файл */
    public static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, UTF_8));
             BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                writer.write(handler.getHeader());
            }
            /* Сохранение задач в файл */
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
            /* Сохранение истории в файл */
            writer.write(handler.generateHistoryToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл..");
        }
    }

    /* Загрузка данных из файла в менеджер */ // Дописать
    private void loadFromFile(File file){

        try (BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            String taskLine = reader.readLine();

            /* Проверка на пустой файл */
            if (taskLine.equals("")) {
                return;
            }

            while (reader.ready()) {
                /* Пропустить заголовок */
                if (taskLine.contains("id")) {
                    /*
                    ДОПИСАТЬ
                     */
                }

                /* Если встретили пустую строку следующие записи для менеджера историии */
                if (taskLine.isEmpty()) {
                    break;
                }

                /* Запись задач */
                Task task = handler.fromString(taskLine);
                if (task instanceof Epic epic) {
                    createEpic(epicStorage.put(epic.getTaskId(), epic));
                } else if (task instanceof Subtask subtask) {
                    createSubTask(subTaskStorage.put(subtask.getTaskId(), subtask));
                } else {
                    createTask(taskStorage.put(task.getTaskId(), task));
                }
            }

            /* Запись истории просмотренных задач */
            String historyRow = reader.readLine();
            if (historyRow.isEmpty()) {
                // Записываем просмотренные задачи в историю
                for (int id : handler.historyFromString(historyRow)) {
                    addToHistory(id);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла..");
        }
    }

    /* Добавление задач в историю. Для восстановления записей */
    public static void addToHistory(int id) {
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

}
