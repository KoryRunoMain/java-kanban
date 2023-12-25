package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    protected File file;

    @BeforeEach
    public void setUp() {
        file = new File(String.valueOf(Path.of("src/resources/tasksTest1.csv")));
        taskManager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
        initTasks();
    }

    @AfterEach
    public void tearDown() {
        assertTrue(file.delete());
    }

    /* Загрузка из файла */
    @Test
    public void checkLoadFromFile() {
        Task taskList = taskManager.createTask(task);
        Epic epicList = taskManager.createEpic(epic);
        FileBackedTasksManager.loadFromFile(file);
        ArrayList<Task> tasksList = taskManager.getAllTasks();
        ArrayList<Epic> epicsList = taskManager.getAllEpics();
        assertEquals(List.of(taskList), tasksList);
        assertEquals(List.of(epicList), epicsList);
    }

    /* Пустой список задач */
    @Test
    public void checkLoadFromFileWithNoTasks() {
        taskManager.saveToFile();
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllTasks());
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllEpics());
    }

    /* Пустой список истории */
    @Test
    public void checkLoadFromEmptyFileOfHistory() {
        taskManager.saveToFile();
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, taskManager.getHistory());
    }

}