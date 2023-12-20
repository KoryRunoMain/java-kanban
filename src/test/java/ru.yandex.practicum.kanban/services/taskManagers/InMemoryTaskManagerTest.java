package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.kanban.models.Epic;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    protected InMemoryTaskManager inMemoryTaskManager;
    protected Epic epic;

    @BeforeEach
    void initEpic() {
        inMemoryTaskManager.createEpic(epic = new Epic("Epic2", "Epic2 Description"));
    }



}