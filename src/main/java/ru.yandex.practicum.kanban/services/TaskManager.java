package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Status;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private int id;
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
        return ++id;
    }

    /*
    TASKS
     */
    // TASKS.Добавление +
    public Task addTask(Task task) {
        task.setTaskId(getNextId());
        taskStorage.put(task.getTaskId(), task);
        return task;
    }

    // TASKS.Получение +
    public Task getTask(int id) {
        return taskStorage.get(id);
    }

    // TASKS.Получение всех задач +
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    // TASKS.Удаление по ID +
    public void removeTask(int id) {
        taskStorage.remove(id);
    }

    // TASKS.Удаление всех задач +
    public void removeAllTasks() {
        taskStorage.clear();
    }

    // TASKS.Обновление
    public void updateTask(Task updateTask) {
        Task saved = taskStorage.get(updateTask.getTaskId());
        if (saved == null) {
            return;
        }
        taskStorage.put(updateTask.getTaskId(), updateTask);
    }

    /*
    EPICS
     */
    // EPICS.Добавление
    public Epic addEpic(Epic epic) {
        epic.setTaskId(getNextId());
        epicStorage.put(epic.getTaskId(), epic);
        return epic;
    }

    // EPICS.Получение
    public Epic getEpic(int id) {
        return epicStorage.get(id);
    }
    // EPICS.Получение списка всех подзадач определённого эпика // ПРОТЕСТИРОВАТЬ!
    public ArrayList<Integer> getSubtaskListByEpicId(Epic epic) {
        Epic list = epicStorage.get(epic.getTaskId());
        if (list == null) {
            return new ArrayList<>();
        }
        ArrayList<> subTaskList = new ArrayList<>();
        for (Integer ep : epicStorage.containsKey()) {

        }
        return ArrayList<>(epic.getSubTask());

//        if (epicStorage.containsKey(epic.getTaskId())) {
//            return epic.getSubTask();
//
//        } else {
//            return null;
//        }
    }
    // EPICS.Получение всех задач
    public HashMap<Integer, Epic> getAllEpics() {
        return epicStorage;
    }
    // EPICS.Удаление по ID
    public void removeEpic(int id) {
        Epic ep = epicStorage.get(id);
        if (ep == null) {
            return;
        }
        for (Integer idToRemove : ep.getSubTask()) {
            epicStorage.remove(idToRemove);
        }
        epicStorage.remove(id);
    }
    // EPICS.Удаление всех задач
    public void removeAllEpics() {
        if (epicStorage == null) {
            return;
        }
        subTaskStorage.clear();
        epicStorage.clear();
    }
    // EPICS.Обновление
    public void updateEpic(Epic updateEpic) {
        Epic saved = epicStorage.get(updateEpic.getTaskId());
        if (saved == null) {
            return;
        }
        epicStorage.put(updateEpic.getTaskId(), updateEpic);
        updateEpicStatus(updateEpic);
    }
    // EPICS.Обновление статуса
    public void updateEpicStatus(Epic epic) {
        ArrayList<Integer> epicStatus = new ArrayList<>(epic.getSubTask());
        int countStatus = 0;
        for (Integer subTaskId : epicStatus) {
            Subtask subtask = subTaskStorage.get(subTaskId);
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
        Epic epic = epicStorage.get(subtask.getEpicId());
        if (epic != null) {
            subTaskStorage.put(subtask.getTaskId(), subtask);
            epic.setTaskId(subtask.getTaskId());
        }
        return subtask;
    }
    // SUBTASKS.Получение по ID
    public Subtask getSubTask(int id) {
        return subTaskStorage.get(id);
    }
    // SUBTASKS.Получение всех задач
    public HashMap<Integer, Subtask> getAllSubTasks() {
        return subTaskStorage;
    }
    // SUBTASKS.Удаление по ID
    public void removeSubTask(int id) {
        Subtask st = subTaskStorage.get(id);
        if (st == null) {
            return;
        }
        Epic ep = epicStorage.get(st.getEpicId());
        ep.getSubTask().remove(st.getTaskId());
        updateEpicStatus(ep);
        subTaskStorage.remove(id);

    }
    // SUBTASKS.Удаление всех задач
    public void removeAllSubTasks() {
        for (Epic ep : epicStorage.values()) {
            for (Integer st : ep.getSubTask()) {
                subTaskStorage.remove(st);
            }
            ep.getSubTask().clear();
        }
    }
    // SUBTASKS.Обновление
    public void updateSubTask(Subtask updateSubtask) {
        Subtask saved = subTaskStorage.get(updateSubtask.getTaskId());
        if (saved == null) {
            return;
        }
        Epic epicSaved = epicStorage.get(updateSubtask.getEpicId());
        if (epicStorage.containsKey(epicSaved.getTaskId())) {
            subTaskStorage.put(updateSubtask.getTaskId(), updateSubtask);
            updateEpicStatus(epicSaved);
        }
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "id=" + id +
                ", tasks=" + taskStorage +
                ", epics=" + epicStorage +
                ", subTasks=" + subTaskStorage +
                '}';
    }

}
