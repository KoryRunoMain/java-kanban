package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Status;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    protected int id;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subTasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }
    // Увеличение ID задачи на 1
    public int getNextId() {
        return ++id;
    }

    /*
    TASKS
     */
    // TASKS.Добавление
    public Task addTask(Task task) {
        task.setTaskId(getNextId());
        tasks.put(task.getTaskId(), task);
        return task;
    }

    // TASKS.Получение
    public Task getTask(int id) {
        return tasks.get(id);
    }

    // TASKS.Получение всех задач
    public HashMap<Integer, Task> getAllTasks() {
        if (tasks == null) {
            return new HashMap<>();
        }
        return tasks;
    }

    // TASKS.Удаление по ID
    public void removeTask(int id) {
        if (tasks == null) {
            return;
        }
        tasks.remove(id);
    }

    // TASKS.Удаление всех задач
    public void removeAllTasks() {
        if (tasks == null) {
            return;
        }
        tasks.clear();
    }

    // TASKS.Обновление
    public void updateTask(Task updateTask) {
        if (updateTask == null) {
            return;
        }
        if (tasks.containsKey(updateTask.getTaskId())) {
            tasks.put(updateTask.getTaskId(), updateTask);
        }
    }

    /*
    EPICS
     */
    // EPICS.Добавление
    public Epic addEpic(Epic epic) {
        epic.setTaskId(getNextId());
        epics.put(epic.getTaskId(), epic);
        return epic;
    }

    // EPICS.Получение
    public Epic getEpic(int id) {
        return epics.get(id);
    }
    // EPICS.Получение списка всех подзадач определённого эпика
    public ArrayList<Integer> getSubtaskListByEpicId(Epic epic) {
        if (epics.containsKey(epic.getTaskId())) {
            return epic.getSubTask();

        } else {
            return null;
        }
    }
    // EPICS.Получение всех задач
    public HashMap<Integer, Epic> getAllEpics() {
        if (epics == null) {
            System.out.println("null");
        }
        return epics;
    }
    // EPICS.Удаление по ID
    public void removeEpic(int id) {
        Epic ep = epics.get(id);
        if (ep == null) {
            return;
        }
        for (Integer idToRemove : ep.getSubTask()) {
            subTasks.remove(idToRemove);
        }
        epics.remove(id);
    }
    // EPICS.Удаление всех задач
    public void removeAllEpics() {
        if (epics == null) {
            return;
        }
        subTasks.clear();
        epics.clear();
    }
    // EPICS.Обновление
    public void updateEpic(Epic updateEpic) {
        if (updateEpic == null) {
            return;
        }
        if (epics.containsKey(updateEpic.getTaskId())) {
            epics.put(updateEpic.getTaskId(), updateEpic);
            updateEpicStatus(updateEpic);
        }
    }
    // EPICS.Обновление статуса
    public void updateEpicStatus(Epic epic) {
        ArrayList<Integer> epicStatus = new ArrayList<>(epic.getSubTask());
        int countStatus = 0;
        for (Integer subTaskId : epicStatus) {
            Subtask subtask = subTasks.get(subTaskId);
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                countStatus += 1;
            } else if (subtask.getStatus() == Status.DONE) {
                countStatus += 2;
            }
        }
        if (countStatus == 0) {
            epic.setStatus(Status.NEW);
            return;
        }
        if (countStatus == epicStatus.size() * 2) {
            epic.setStatus(Status.DONE);
            return;
        }
        epic.setStatus(Status.IN_PROGRESS);
    }

    /*
    SUBTASKS
     */
    // SUBTASKS.Добавление
    public Subtask addSubTask(Subtask subtask) {
        subtask.setTaskId(getNextId());
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        } else {
            subTasks.put(subtask.getTaskId(), subtask);
            epic.setTaskId(subtask.getTaskId());
            return subtask;
        }
    }
    // SUBTASKS.Получение
    public Subtask getSubTask(int id) {
        return subTasks.get(id);
    }
    // SUBTASKS.Получение всех задач
    public HashMap<Integer, Subtask> getAllSubTasks() {
        if (subTasks == null) {
            System.out.println("null");
        }
        return subTasks;
    }
    // SUBTASKS.Удаление по ID
    public void removeSubTask(int id) {
        Subtask st = subTasks.get(id);
        if (st == null) {
            return;
        }
        Epic ep = epics.get(st.getEpicId());
        ep.getSubTask().remove(st.getTaskId());
        subTasks.remove(id);
    }
    // SUBTASKS.Удаление всех задач
    public void removeAllSubTasks() {
        if (subTasks == null) {
            return;
        }
        for (Epic ep : epics.values()) {
            for (Integer st : ep.getSubTask()) {
                subTasks.remove(st);
            }
            ep.getSubTask().clear();
        }
    }
    // SUBTASKS.Обновление
    public void updateSubTask(Subtask updateSubtask) {
        if (updateSubtask == null) {
            return;
        }
        Epic epic = epics.get(updateSubtask.getEpicId());
        if (epics.containsKey(epic.getTaskId())) {
            subTasks.put(updateSubtask.getTaskId(), updateSubtask);
            updateEpicStatus(epic);
        }
    }
}
