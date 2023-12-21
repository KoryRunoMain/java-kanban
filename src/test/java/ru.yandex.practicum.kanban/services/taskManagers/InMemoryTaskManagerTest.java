package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.enums.Status;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    protected InMemoryTaskManager inMemoryTaskManager;
    protected Epic epic;

    @BeforeEach
    void initEpic() {
        inMemoryTaskManager.createEpic(epic = new Epic("Epic2", "Epic2 Description"));
    }

    // Создаем Подзадачи для теста на проверку NEW, DONE, IN_PROGRESS
    public void createSubtasksAndSetStatusForNewDoneIn_Progress(Status status) {
        Subtask subtask1 = new Subtask(1, "SubTask1", "SubTask1 Description");
        subtask1.setStatus(status);
        inMemoryTaskManager.createSubTask(subtask1);

        Subtask subtask2 = new Subtask(2, "SubTask2", "SubTask2 Description");
        subtask2.setStatus(status);
        inMemoryTaskManager.createSubTask(subtask2);
    }

    // Создаем Подзадачу для теста на проверку NEW и DONE
    public void createSubtasksAndSetStatusForNewAndDone(Status status) {
        Subtask subtask3 = new Subtask(3, "SubTask3", "SubTask3 Description");
        subtask3.setStatus(status);
        inMemoryTaskManager.createSubTask(subtask3);
    }

    //Пустой список подзадач
    @Test
    public void checkEpicStatusWithoutSubtasks() {
        assertEquals(0, epic.getSubTask().size(), "Список задач не пустой.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus(), "Статус задачи (Эпик) не NEW");
    }

    //Все подзадачи со статусом NEW
    @Test
    public void checkEpicStatusWithAllSubtasksStatusNew() {
        createSubtasksAndSetStatusForNewDoneIn_Progress(Status.NEW);
        assertEquals(2, epic.getSubTask().size(), "Список подзадач пуст.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus(), "Статус задачи (Эпик) не NEW");
    }

    //Все подзадачи со статусом DONE
    @Test
    public void checkEpicStatusWithAllSubtasksStatusDone() {
        createSubtasksAndSetStatusForNewDoneIn_Progress(Status.DONE);
        assertEquals(2, epic.getSubTask().size(), "Список подзадач пуст.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.DONE, epic.getStatus(), "Статус задачи (Эпик) не DONE");
    }

    //Подзадачи со статусами NEW и DONE
    @Test
    public void checkEpicStatusWithAllSubtasksStatusNewAndDone() {
        createSubtasksAndSetStatusForNewDoneIn_Progress(Status.NEW);
        createSubtasksAndSetStatusForNewAndDone(Status.DONE);
        assertEquals(3, epic.getSubTask().size(), "Список подзадач пуст.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус задачи (Эпик) не IN_PROGRESS");
    }


    //Подзадачи со статусом IN_PROGRESS
    @Test
    public void checkEpicStatusWithAllSubtasksStatusIN_PROGRESS() {
        createSubtasksAndSetStatusForNewDoneIn_Progress(Status.IN_PROGRESS);
        assertEquals(2, epic.getSubTask().size(), "Список подзадач пуст.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус задачи (Эпик) не IN_PROGRESS");
    }

}