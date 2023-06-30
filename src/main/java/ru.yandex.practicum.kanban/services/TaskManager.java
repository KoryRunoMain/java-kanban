package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Status;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int generatorId = 0;
    private final HashMap<Integer, Task> taskStorage;
    private final HashMap<Integer, Epic> epicStorage;
    private final HashMap<Integer, Subtask> subTaskStorage;

    public TaskManager() {
        taskStorage = new HashMap<>();
        epicStorage = new HashMap<>();
        subTaskStorage = new HashMap<>();
    }
    // Увеличение ID задачи на 1
    public int getNextId() {
        return ++generatorId;
    }


    //_____TASKS_____//
    // TASKS.Добавление
    public Task createTask(Task task) {
        if (task == null) {
            return null;
        }
        taskStorage.put(getNextId(), task);
        task.setTaskId(generatorId);
        return task;
    }

    // TASKS.Получение по ID
    public Task getTaskById(int id) {
        if (!taskStorage.containsKey(id)) {
            return null;
        }
        return taskStorage.get(id);
    }

    // TASKS.Получение всех задач
    public ArrayList<Task> getAllTasks() {
        if (taskStorage.isEmpty()) {
            return null;
        }
        return new ArrayList<>(taskStorage.values());
    }

    // TASKS.Удаление по ID
    public Task removeTaskById(int id) {
        if (!taskStorage.containsKey(id)) {
            return null;
        }
        return taskStorage.remove(id);
    }

    // TASKS.Удаление всех задач
    public void removeAllTasks() {
        if (taskStorage.isEmpty()) {
            return;
        }
        taskStorage.clear();
    }

    // TASKS.Обновление
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


    //_____EPICS_____//
    // EPICS.Добавление
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            return null;
        }
        epicStorage.put(getNextId(), epic);
        epic.setTaskId(generatorId);
        return epic;
    }

    // EPICS.Получение по ID
    public Epic getEpicById(int id) {
        if (!epicStorage.containsKey(id)) {
            return null;
        }
        return epicStorage.get(id);
    }

    // EPICS.Получение всех задач
    public ArrayList<Epic> getAllEpics() {
        if (epicStorage.isEmpty()) {
            return null;
        }
        return new ArrayList<>(epicStorage.values());
    }

    // EPICS.Удаление по ID
    public void removeEpicById(int id) {
        Epic epic = epicStorage.get(id);
        if (epic == null) {
            return;
        }
        for (Integer epicToRemove : epic.getSubTask()) {
            subTaskStorage.remove(epicToRemove);
        }
        epicStorage.remove(id);
    }

    // EPICS.Удаление всех задач
    public void removeAllEpics() {
        if (epicStorage.isEmpty()) {
            return;
        }
        epicStorage.clear();
        subTaskStorage.clear();
    }

    // EPICS.Обновление
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

        for (Integer subtaskStatus : epic.subTaskList) {
            Subtask subtask = subTaskStorage.get(subtaskStatus);
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
        if (newCount == epic.subTaskList.size()) {
            epic.setStatus(Status.NEW);
        } else if (doneCount == epic.subTaskList.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    //_____SUBTASKS_____//
    // SUBTASKS.Добавление
    public Subtask createSubTask(Subtask subtask) {
        if (subtask == null) {
            return null;
        }
        Epic epic = epicStorage.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subTaskStorage.put(getNextId(), subtask);
        subtask.setTaskId(generatorId);
        epic.addSubtask(generatorId);
        updateEpicStatus(epic);
        return subtask;
    }

    // SUBTASKS.Получение по ID
    public Subtask getSubTaskById(int id) {
        if (!subTaskStorage.containsKey(id)) {
            return null;
        }
        return subTaskStorage.get(id);
    }

    // SUBTASKS.Получение всех подзадач
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
    public void removeSubTaskById(int id) {
        Subtask subtask = subTaskStorage.get(id);
        if (!subTaskStorage.containsKey(id)) {
            return;
        }
        Epic epic = epicStorage.get(subtask.getEpicId());
        Integer subId = subtask.getTaskId();
        epic.getSubTask().remove(subId);
        subTaskStorage.remove(id);
        updateEpicStatus(epic);
    }

    // SUBTASKS.Удаление всех задач
    public void removeAllSubTasks() {
        if (subTaskStorage.isEmpty()) {
            return;
        }
        for (Epic epic : epicStorage.values()) {
            for (Integer st : epic.getSubTask()) {
                subTaskStorage.remove(st);
            }
            epic.getSubTask().clear();
            updateEpicStatus(epic);
        }
    }

    // SUBTASKS.Обновление
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
