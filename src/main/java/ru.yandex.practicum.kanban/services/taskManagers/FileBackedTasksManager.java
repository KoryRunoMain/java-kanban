package ru.yandex.practicum.kanban.services.taskManagers;

import java.io.*;
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

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    /* Класс работает с форматом .CSV */
    private static final CSVFormatHandler handler = new CSVFormatHandler();

    protected Path path = Path.of("src/resources/tasks.csv");
    protected File file = new File(String.valueOf(path));

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    /* Сохранение задач и истории в файл */
    protected void saveToFile() {
        if (subTaskStorage == null && taskStorage == null && epicStorage == null) {
            return;
        }
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
        int initialID = 0; // Счетчик id восстановленных задач

        try (BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            /* Проверка на пустой файл */
            if (file.length() == 0) {
                return fileBackedTasksManager;
            }

            String tasksLine = "";
            boolean firstLine = reader.readLine()  // Переменная для пропуска заголовка
                                      .trim()
                                      .equals(handler.getHeader());

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

                /* Обновление ID счетчика восстановленных задач */
                if (task.getId() > initialID) {
                    initialID = task.getId();
                    fileBackedTasksManager.updateGeneratorID(initialID);
                }

                if (task instanceof Epic epic) {
                    fileBackedTasksManager.epicStorage // Заполнение задачами Epic
                                          .put(epic.getId(), epic);

                } else if (task instanceof Subtask subtask) {
                    fileBackedTasksManager.subTaskStorage // Заполнение задачами SubTask
                                          .put(subtask.getId(), subtask);
                    fileBackedTasksManager.epicStorage // Заполнение Epic задачи своими SubTask задачами
                                          .get(subtask.getEpicId())
                                          .addSubtaskId(subtask.getId());
                } else {
                    fileBackedTasksManager.taskStorage // Заполнение задачами Task
                                          .put(task.getId(), task);
                }
            }

            /* Запись истории просмотренных задач */
            String historyRow = reader.readLine();
            if (historyRow == null) {
                return fileBackedTasksManager;
            }
            for (int id : handler.historyFromString(historyRow)) {
                if (fileBackedTasksManager.taskStorage.containsKey(id)) {
                    fileBackedTasksManager.historyManager
                                          .add(fileBackedTasksManager.taskStorage.get(id));
                }
                if (fileBackedTasksManager.epicStorage.containsKey(id)) {
                    fileBackedTasksManager.historyManager
                                          .add(fileBackedTasksManager.epicStorage.get(id));
                }
                if (fileBackedTasksManager.subTaskStorage.containsKey(id)) {
                    fileBackedTasksManager.historyManager
                                          .add(fileBackedTasksManager.subTaskStorage.get(id));
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
        manager = loadFromFile(Paths.get("src/resources/tasks.csv").toFile());

    }

}
