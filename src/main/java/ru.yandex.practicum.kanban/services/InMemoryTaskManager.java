package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.services.interfaces.HistoryManager;
import ru.yandex.practicum.kanban.services.interfaces.TaskManager;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Status;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int generatorId = 0;
    private final HashMap<Integer, Task> taskStorage;
    private final HashMap<Integer, Epic> epicStorage;
    private final HashMap<Integer, Subtask> subTaskStorage;
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        taskStorage = new HashMap<>();
        epicStorage = new HashMap<>();
        subTaskStorage = new HashMap<>();
        this.historyManager = historyManager;
    }


    // Увеличение ID задачи на 1
    public int getNextId() {
        return ++generatorId;
    }

    // Просмотр истории
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /* TASKS */
    // TASKS.Добавление
    @Override
    public Task createTask(Task task) {
        if (task == null) {
            return null;
        }
        taskStorage.put(getNextId(), task);
        task.setTaskId(generatorId);
        return task;
    }

    // TASKS.Получение по ID
    @Override
    public Task getTaskById(int id) {
        if (!taskStorage.containsKey(id)) {
            return null;
        }
        historyManager.add(taskStorage.get(id));
        return taskStorage.get(id);
    }

    // TASKS.Получение всех задач
    @Override
    public ArrayList<Task> getAllTasks() {
        if (taskStorage.isEmpty()) {
            return null;
        }
        return new ArrayList<>(taskStorage.values());
    }

    // TASKS.Удаление по ID
    @Override
    public void removeTaskById(int id) {
        if (!taskStorage.containsKey(id)) {
            return;
        }
        taskStorage.remove(id);
        historyManager.remove(id);
    }

    // TASKS.Удаление всех задач
    @Override
    public void removeAllTasks() {
        if (taskStorage.isEmpty()) {
            return;
        }
        taskStorage.clear();
    }

    // TASKS.Обновление
    @Override
    public void updateTask(Task updateTask) {
        if (updateTask == null) {
            return;
        }
        Task taskToUpdate = taskStorage.get(updateTask.getTaskId());
        if (taskToUpdate == null) {
            return;
        }
        taskStorage.put(updateTask.getTaskId(), updateTask);
    }


    /* EPICS */
    // EPICS.Добавление
    @Override
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            return null;
        }
        epicStorage.put(getNextId(), epic);
        epic.setTaskId(generatorId);
        return epic;
    }

    // EPICS.Получение по ID
    @Override
    public Epic getEpicById(int id) {
        if (!epicStorage.containsKey(id)) {
            return null;
        }
        historyManager.add(epicStorage.get(id));
        return epicStorage.get(id);
    }

    // EPICS.Получение всех задач
    @Override
    public ArrayList<Epic> getAllEpics() {
        if (epicStorage.isEmpty()) {
            return null;
        }
        return new ArrayList<>(epicStorage.values());
    }

    // EPICS.Удаление по ID
    @Override
    public void removeEpicById(int id) {
        Epic epic = epicStorage.get(id);
        if (epic == null) {
            return;
        }
        epic.getSubTask().forEach(subTaskStorage::remove);
        epicStorage.remove(id);
        historyManager.remove(id);
    }

    // EPICS.Удаление всех задач
    @Override
    public void removeAllEpics() {
        if (epicStorage.isEmpty()) {
            return;
        }
        epicStorage.clear();
        subTaskStorage.clear();
    }

    // EPICS.Обновление
    @Override
    public void updateEpic(Epic updateEpic) {
        if (updateEpic == null) {
            return;
        }
        Epic epicToUpdate = epicStorage.get(updateEpic.getTaskId());
        if (epicToUpdate == null) {
            return;
        }
        epicStorage.put(updateEpic.getTaskId(), updateEpic);
        updateEpicStatus(updateEpic);
    }

    // EPICS.Обновление статуса
    protected void updateEpicStatus(Epic epic) {
        int newCount = 0;
        int doneCount = 0;

        for (Integer id : epic.getSubTask()) {
            Subtask subtask = subTaskStorage.get(id);
            switch (subtask.getStatus()) {
                case NEW:
                    newCount += 1;
                    break;
                case DONE:
                    doneCount += 1;
                    break;
                case IN_PROGRESS:
                    break;
            }
        }
        if (newCount == epic.getSubTask().size()) {
            epic.setStatus(Status.NEW);
        } else if (doneCount == epic.getSubTask().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    /* SUBTASKS */
    // SUBTASKS.Добавление
    @Override
    public Subtask createSubTask(Subtask subtask) {
        if (subtask == null) {
            return null;
        }
        Epic epic = epicStorage.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subTaskStorage.put(getNextId(), subtask);
        epic.getSubTask().add(generatorId);
        subtask.setTaskId(generatorId);
        epic.addSubtask(generatorId);
        updateEpicStatus(epic);
        return subtask;
    }

    // SUBTASKS.Получение по ID
    @Override
    public Subtask getSubTaskById(int id) {
        if (!subTaskStorage.containsKey(id)) {
            return null;
        }
        historyManager.add(subTaskStorage.get(id));
        return subTaskStorage.get(id);
    }

    // SUBTASKS.Получение всех подзадач
    @Override
    public ArrayList<Subtask> getAllSubTasks() {
        if (subTaskStorage.isEmpty()) {
            return null;
        }
        return new ArrayList<>(subTaskStorage.values());
    }

    // Получение списка всех подзадач определенного EPIC
    public ArrayList<Subtask> getSubTasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer subTaskNum : epic.getSubTask()) {
            subtasksOfEpic.add(subTaskStorage.get(subTaskNum));
        }
        return subtasksOfEpic;
    }

    // SUBTASKS.Удаление по ID
    @Override
    public void removeSubTaskById(int id) {
        if (!subTaskStorage.containsKey(id)) {
            return;
        }
        Subtask subtask = subTaskStorage.get(id);
        Epic epic = epicStorage.get(subTaskStorage.get(id).getEpicId());
        epic.removeSubtask(id);
        subTaskStorage.remove(id);
        historyManager.remove(id);
        updateEpicStatus(epic);
    }

    // SUBTASKS.Удаление всех задач
    @Override
    public void removeAllSubTasks() {
        if (subTaskStorage.isEmpty()) {
            return;
        }
        for (Epic epic : epicStorage.values()) {
            for (Integer st : epic.getSubTask()) {
                subTaskStorage.remove(st);
            }
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
        }
    }

    // SUBTASKS.Обновление
    @Override
    public void updateSubTask(Subtask updateSubtask) {
        if (updateSubtask == null) {
            return;
        }
        Subtask subTaskToUpdate = subTaskStorage.get(updateSubtask.getTaskId());
        if (subTaskToUpdate == null) {
            return;
        }
        Epic epicToUpdate = epicStorage.get(updateSubtask.getEpicId());
        if (epicStorage.containsKey(epicToUpdate.getTaskId())) {
            subTaskStorage.put(updateSubtask.getTaskId(), updateSubtask);
            updateEpicStatus(epicToUpdate);
        }
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "id=" + generatorId +
                ", tasks=" + taskStorage +
                ", epics=" + epicStorage +
                ", subTasks=" + subTaskStorage +
                '}';
    }

}
