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

    // Обновление статуса EPIC
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


    // Вывод всех задач: TASK, EPIC, SUBTASK
    public HashMap<Integer, Task> getAllTasks() {
        if (tasks == null) {
            System.out.println("null");
        }
        return tasks;
    }
    public HashMap<Integer, Epic> getAllEpics() {
        if (epics == null) {
            System.out.println("null");
        }
        return epics;
    }
    public HashMap<Integer, Subtask> getAllSubTasks() {
        if (subTasks == null) {
            System.out.println("null");
        }
        return subTasks;
    }


    // Добавление задачи TASK
    public Task addTask(Task task) {
        task.setTaskId(getNextId());
        tasks.put(task.getTaskId(), task);
        return task;
    }
    // Добавление задачи EPIC
    public Epic addEpic(Epic epic) {
        epic.setTaskId(getNextId());
        epics.put(epic.getTaskId(), epic);
        return epic;
    }
    // Добавление подзадач SUBTASK
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


    // Удаление всех задач: TASK, EPIC, SUBTASK
    public void removeAllTasks() {
        if (tasks == null) {
            return;
        }
        tasks.clear();
    }

    public void removeAllEpics() {
        if (epics == null) {
            return;
        }
        subTasks.clear();
        epics.clear();
    }

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


    // Получение задачи по ID: TASK, EPIC, SUBTASK
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubTask(int id) {
        return subTasks.get(id);
    }


    // Удаление задачи по ID: TASK
    public void removeTask(int id) {
        if (tasks == null) {
            return;
        }
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }
    // Удаление задачи по ID: EPIC
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
    // Удаление задачи по ID: SUBTASK
    public void removeSubTask(int id) {
        Subtask st = subTasks.get(id);
        if (st == null) {
            return;
        }
        Epic ep = epics.get(st.getEpicId());
        ep.getSubTask().remove(st.getTaskId());
        subTasks.remove(id);
    }

    // Обновление задачи EPIC
    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        if (tasks.containsKey(task.getTaskId())) {
            tasks.put(task.getTaskId(), task);
        }
    }

    // Обновление задачи EPIC
    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        if (epics.containsKey(epic.getTaskId())) {
            epics.put(epic.getTaskId(), epic);
            // update
        }
    }

    // Обновление задачи SUBTASK

    // Получение списка всех подзадач определённого эпика



}
