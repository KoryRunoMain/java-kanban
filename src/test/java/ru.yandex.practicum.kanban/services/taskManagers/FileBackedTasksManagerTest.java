package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.services.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    protected final Path path = Path.of("src/resources/tasks.csv");
    protected final File file = new File(String.valueOf(path));
    private final Task task = new Task("Task", "Task Description", 5,
            Instant.ofEpochMilli(1703275200000L), Status.NEW);
    private final Epic epic = new Epic("Epic", "Epic Description", 15,
            Instant.ofEpochMilli(1703275500000L), Status.NEW);
    private final Subtask subtask = new Subtask(epic.getId(), "SubTask", "Subtask Description", 5,
            Instant.ofEpochMilli(1703276400000L), Status.NEW);


    @BeforeEach
    public void init() {
        taskManager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
    }

    @AfterEach
    void afterEach() {
        try {
            Files.delete(path);

        } catch (IOException exception) {
            exception.getStackTrace();
        }
    }

    /* Загрузка из файла */
    @Test
    public void checkLoadFromFile() {
        FileBackedTasksManager taskMan = FileBackedTasksManager.loadFromFile(file);
        final List<Task> tasks = taskMan.getAllTasks();
        assertNotNull(tasks, "Список задач пуст.");
        assertEquals(1, tasks.size(), "Список задач пуст.");
    }

    /* Пустой список задач */
    @Test
    public void checkLoadFromFileWithNoTasks() {
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubTasks();
        FileBackedTasksManager taskMan = FileBackedTasksManager.loadFromFile(file);
        final List<Task> tasks = taskMan.getAllTasks();
        final List<Epic> epics = taskMan.getAllEpics();
        final List<Subtask> subtasks = taskMan.getAllSubTasks();
        assertNotNull(tasks, "Список задач не пустой.");
        assertEquals(Collections.EMPTY_LIST, tasks, "Список задач не пустой.");
        assertNotNull(epics, "Список задач пуст.");
        assertEquals(Collections.EMPTY_LIST, epics, "Список задач (Epic) не пустой.");
        assertNotNull(subtasks, "Список задач пуст.");
        assertEquals(Collections.EMPTY_LIST, subtasks, "Список подзадач не пустой.");
    }

    /* Эпик без подзадач */
    @Test
    public void checkLoadFromEpicFileWithoutSubtasks() {
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubTasks();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllSubTasks(), "Список подзадач не пустой.");
        FileBackedTasksManager taskMan = FileBackedTasksManager.loadFromFile(file);
        assertEquals(1, taskMan.getAllEpics().size(), "Список задач (Epic) не пустой.");
        assertEquals(Collections.EMPTY_LIST, taskMan.getAllSubTasks(), "Список подзадач не пустой.");
    }

    /* Пустой список истории */
    @Test
    public void checkLoadFromEmptyFileOfHistory() {
        taskManager.historyManager.add(task);
        taskManager.historyManager.add(epic);
        taskManager.historyManager.add(subtask);
        assertEquals(3, FileBackedTasksManager.loadFromFile(file).getHistory().size(), "Файл с историрей пустой.");
        taskManager.historyManager.remove(task.getId());
        taskManager.historyManager.remove(epic.getId());
        taskManager.historyManager.remove(subtask.getId());
        assertEquals(Collections.EMPTY_LIST, FileBackedTasksManager.loadFromFile(file).getHistory(), "Файл с историрей пустой.");
    }

}