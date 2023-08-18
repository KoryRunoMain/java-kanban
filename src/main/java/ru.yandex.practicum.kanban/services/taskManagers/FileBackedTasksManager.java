package ru.yandex.practicum.kanban.services.taskManagers;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.exceptions.ManagerSaveException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private static final Path filePath = Path.of("src/resources/tasks.csv");
    private File file;

    public FileBackedTasksManager(HistoryManager historyManager, String file) {
        super(historyManager);
        this.file = new File(String.valueOf(filePath));
    }

    /* Запись в файл */
    protected void savetoFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, UTF_8));
             BufferedReader br = new BufferedReader(new FileReader(file, UTF_8))) {
            String line = br.readLine();
            if (line.isBlank()) {
                while (!line.isEmpty()) {
                    line = line.replaceAll("\\s+", " ")
                            .trim()
                            .concat("\n");
                }
                bw.write("id,type,name,status,description,epic" + "\n");
            }

            /* Сохранение задач в файл */
            for (Task task : getAllTasks()) {
                bw.write(toString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                bw.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubTasks()) {
                bw.write(toString(subtask) + "\n");
            }

            /* Сохранение истории в файл */
            bw.write("\n");
            for (Task task : getHistory()) {
                bw.write(task.getTaskId() + ",");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл..");
        }
    }

    /* Создание строки из задачи */
    private String toString(Task task) {
        String[] line = {
                Integer.toString(task.getTaskId()), // id
                getType(task).toString(),           // type
                task.getTaskName(),                 // name
                task.getStatus().toString(),        // status
                task.getTaskDescription(),          // description
                getSubtaskOfEpic(task)              // epic
        };
        return String.join(",", line);
    }

    /* Создание задачи из строки в менеджер */
    private void fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]); // id
        Type type = Type.valueOf(values[1]);
        String name = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        Integer epicId = Integer.parseInt(values[5]);

        if (type.equals(Type.TASK)) {
            Task task = new Task(name, description, type, status);
            taskStorage.put(id, task);
        }
        if (type.equals(Type.EPIC)) {
            Epic epic = new Epic(name, description, type, status);
            epicStorage.put(id, epic);
        }
        if (type.equals(Type.SUBTASK)) {
            Subtask subtask = new Subtask(epicId, name, description, type, status);
            subTaskStorage.put(id, subtask);
        }
    }

    /* Формирование задач из файла в менеджер истории */
    private List<Integer> historyFromString(String values) {
        if (values.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> historyTaskList = new ArrayList<>();
        String[] splitValue = values.split(",");
        for (String value : splitValue) {
            historyTaskList.add(Integer.valueOf(value));
        }
        return historyTaskList;
    }

    /* Получение типа задачи */
    private Type getType(Task task) {
        if (task.equals("EPIC")) {
            return Type.EPIC;
        }
        if (task.equals("SUBTASK")) {
            return Type.SUBTASK;
        }
        return Type.TASK;
    }

    /* Получение Subtask задачи */
    private String getSubtaskOfEpic(Task task) {
        if (task.equals("SUBTASK")) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }

    /* Загрузка данных из файла в менеджер */
    public void loadFromFile() {

    }


    /* TASKS */
    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        savetoFile();
        return task;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        savetoFile();
        return task;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        savetoFile();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        savetoFile();
    }

    @Override
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
        savetoFile();
    }

    /* EPICS */
    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        savetoFile();
        return epic;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        savetoFile();
        return epic;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        savetoFile();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        savetoFile();
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        savetoFile();
    }

    /* SUBTASKS */
    @Override
    public Subtask createSubTask(Subtask subtask) {
        super.createSubTask(subtask);
        savetoFile();
        return subtask;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = super.getSubTaskById(id);
        savetoFile();
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getAllSubTasks() {
        return super.getAllSubTasks();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        savetoFile();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        savetoFile();
    }

    @Override
    public void updateSubTask(Subtask updateSubtask) {
        super.updateSubTask(updateSubtask);
        savetoFile();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

}
