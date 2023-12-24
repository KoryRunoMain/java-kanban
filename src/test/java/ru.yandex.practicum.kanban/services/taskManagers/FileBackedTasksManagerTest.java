package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    protected File file;

    // Отчистка коллекций задач для тестов
    protected void deleteAllTasksForTest() {
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubTasks();
    }

    // Загрузка истории задач для тестов
    protected void createTasksHistory() {
        taskManager.historyManager.add(task);
        taskManager.historyManager.add(epic);
        taskManager.historyManager.add(subtask);
    }

    // Удаление истории задач для тестов
    protected void removeTasksHistory() {
        taskManager.historyManager.remove(task.getId());
        taskManager.historyManager.remove(epic.getId());
        taskManager.historyManager.remove(subtask.getId());
    }


    @BeforeEach
    public void initFile() {
        file = new File("src/resources/" + "tasksTest" + ".csv");
    }

    @Test
    public void checkLoadFromFile() {
        FileBackedTasksManager taskMan = FileBackedTasksManager.loadFromFile(file);
        final List<Task> tasks = taskMan.getAllTasks();
        assertNotNull(tasks, "Список задач пуст.");
        assertEquals(1, tasks.size(), "Список задач пуст.");
    }

    // Пустой список задач
    @Test
    public void checkLoadFromFileWithNoTasks() {
        deleteAllTasksForTest();
        FileBackedTasksManager taskMan = FileBackedTasksManager.loadFromFile(file);
        final List<Task> tasks = taskMan.getAllTasks();
        final List<Epic> epics = taskMan.getAllEpics();
        final List<Subtask> subtasks = taskMan.getAllSubTasks();
        assertNotNull(tasks, "Список задач не пустой.");
        assertEquals(0, tasks.size(), "Список задач не пустой.");
        assertNotNull(epics, "Список задач пуст.");
        assertEquals(0, epics.size(), "Список задач (Epic) не пустой.");
        assertNotNull(subtasks, "Список задач пуст.");
        assertEquals(0, subtasks.size(), "Список подзадач не пустой.");
    }

    //Эпик без подзадач
    @Test
    public void checkLoadFromEpicFileWithoutSubtasks() {
        deleteAllTasksForTest();
        assertEquals(0, taskManager.getAllSubTasks().size(), "Список подзадач не пустой.");
        FileBackedTasksManager taskMan = FileBackedTasksManager.loadFromFile(file);
        assertEquals(1, taskMan.getAllEpics().size(), "Список задач (Epic) не пустой.");
        assertEquals(0, taskMan.getAllSubTasks().size(), "Список подзадач не пустой.");
    }

    //Пустой список истории
    @Test
    public void checkLoadFromEmptyFileOfHistory() {
        createTasksHistory();
        FileBackedTasksManager tasksManagerFromFile = FileBackedTasksManager.loadFromFile(file);
        assertEquals(3, taskManager.historyManager.getHistory().size(), "Файл с историрей пустой.");
        removeTasksHistory();
        tasksManagerFromFile = FileBackedTasksManager.loadFromFile(file);
        assertEquals(0, taskManager.historyManager.getHistory().size(), "Файл с историрей пустой.");
    }

}