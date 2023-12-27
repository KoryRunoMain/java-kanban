package ru.yandex.practicum.kanban.services.taskManagers;

import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.taskManagers.exceptions.TaskConflictException;

import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int generatorId = 0;
    protected HashMap<Integer, Task> taskStorage = new HashMap<>();
    protected HashMap<Integer, Epic> epicStorage = new HashMap<>();
    protected HashMap<Integer, Subtask> subTaskStorage = new HashMap<>();
    protected final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);
    protected final Set<Task> prioritizedTasks = new TreeSet<>(comparator);


    protected HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {
    }

    /* Увеличение ID задачи на 1 */
    public int getNextId() {
        return ++generatorId;
    }

    protected void updateGeneratorID(int initialID) {
        generatorId = initialID;
    }


    /* Обновление времени задач EPIC */
    private void updateEpicTime(Epic epic) {
        List<Subtask> subtasks = getSubTasksOfEpic(epic);
        Instant startTime = subtasks.get(0).getStartTime();
        Instant endTime = subtasks.get(0).getEndTime();
        long duration = 0L;

        for (Subtask subtaskNum : subtasks) {
            Subtask subtask = subTaskStorage.get(subtaskNum.getId());
            if (subtaskNum.getStartTime().isAfter(endTime)) {
                startTime = subtaskNum.getStartTime();
            } else if (subtaskNum.getEndTime().isAfter(endTime)) {
                endTime = subtaskNum.getEndTime();
            }
            duration += subtask.getDuration();
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    /* Проверка на пересечение задач  */
    private boolean verifyTasks(Task task) {
        Instant startTimeOfTask = task.getStartTime();
        Instant endTimeOfTask = task.getEndTime();

        for (Task taskNum : prioritizedTasks) {
            if (taskNum.getStartTime() == null) {
                continue;
            }
            Instant startTime = taskNum.getStartTime();
            Instant endTime = taskNum.getEndTime();

            if(endTimeOfTask.isAfter(startTime) && startTimeOfTask.isBefore(endTime)) {
                return false;
            }
        }
        return true;
    }

    /* Добавление задачи в список prioritizedTasks  */
    public void addTaskToPrioritizedList(Task task) {
        boolean isVeried = verifyTasks(task);
        if (isVeried) {
            prioritizedTasks.add(task);
        } else {
            throw new TaskConflictException("Ошибка. Задачи пересекаются.");
        }
    }

    /* Получить копию задач по приоритету */
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    /* Просмотр истории */
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /* TASKS.Добавление */
    @Override
    public Task createTask(Task task) {
        if (task == null) {
            return null;
        }
        taskStorage.put(getNextId(), task);
        task.setId(generatorId);
        addTaskToPrioritizedList(task);
        return task;
    }

    /* TASKS.Получение по ID */
    @Override
    public Task getTaskById(int id) {
        if (!taskStorage.containsKey(id)) {
            return null;
        }
        historyManager.add(taskStorage.get(id));
        return taskStorage.get(id);
    }

    /* TASKS.Получение всех задач */
    @Override
    public ArrayList<Task> getAllTasks() {
        if (taskStorage.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(taskStorage.values());
    }

    /* TASKS.Удаление по ID */
    @Override
    public void removeTaskById(int id) {
        if (!taskStorage.containsKey(id)) {
            return;
        }
        taskStorage.remove(id);
        historyManager.remove(id);
        prioritizedTasks.removeIf(task -> task.getId() == id);
    }

    /* TASKS.Удаление всех задач */
    @Override
    public void removeAllTasks() {
        if (taskStorage.isEmpty()) {
            return;
        }
        taskStorage.clear();
        prioritizedTasks.clear();
    }

    /* TASKS.Обновление */
    @Override
    public void updateTask(Task updateTask) {
        if (updateTask == null) {
            return;
        }
        Task taskToUpdate = taskStorage.get(updateTask.getId());
        if (taskToUpdate == null) {
            return;
        }
        taskStorage.put(updateTask.getId(), updateTask);
        addTaskToPrioritizedList(updateTask);
    }


    /* EPICS.Добавление */
    @Override
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            return null;
        }
        epicStorage.put(getNextId(), epic);
        epic.setId(generatorId);
        return epic;
    }

    /* EPICS.Получение по ID */
    @Override
    public Epic getEpicById(int id) {
        if (!epicStorage.containsKey(id)) {
            return null;
        }
        historyManager.add(epicStorage.get(id));
        return epicStorage.get(id);
    }

    /* EPICS.Получение всех задач */
    @Override
    public ArrayList<Epic> getAllEpics() {
        if (epicStorage.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(epicStorage.values());
    }

    /* EPICS.Удаление по ID */
    @Override
    public void removeEpicById(int id) {
        Epic epic = epicStorage.get(id);
        if (epic == null) {
            return;
        }
        for (Integer subTasksId : epic.getSubTaskIds()) {
            subTaskStorage.remove(subTasksId);
            historyManager.remove(subTasksId);
            prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), subTasksId));
        }
        epicStorage.remove(id);
        historyManager.remove(id);
    }

    /* EPICS.Удаление всех задач */
    @Override
    public void removeAllEpics() {
        if (epicStorage.isEmpty()) {
            return;
        }
        epicStorage.clear();
        subTaskStorage.clear();
    }

    /* EPICS.Обновление */
    @Override
    public void updateEpic(Epic updateEpic) {
        if (updateEpic == null) {
            return;
        }
        Epic epicToUpdate = epicStorage.get(updateEpic.getId());
        if (epicToUpdate == null) {
            return;
        }
        epicStorage.put(updateEpic.getId(), updateEpic);
        updateEpicStatus(updateEpic);
        updateEpicTime(updateEpic);
    }

    /* EPICS.Обновление статуса */
    protected void updateEpicStatus(Epic epic) {
        int newCount = 0;
        int doneCount = 0;

        for (Integer id : epic.getSubTaskIds()) {
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
        if (newCount == epic.getSubTaskIds().size()) {
            epic.setStatus(Status.NEW);
        } else if (doneCount == epic.getSubTaskIds().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    /* SUBTASKS.Добавление */
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
        epic.getSubTaskIds().add(generatorId);
        subtask.setId(generatorId);
        epic.addSubtaskId(generatorId);
        updateEpicStatus(epic);
        updateEpicTime(epic);
        addTaskToPrioritizedList(subtask);
        return subtask;
    }

    /* SUBTASKS.Получение по ID */
    @Override
    public Subtask getSubTaskById(int id) {
        if (!subTaskStorage.containsKey(id)) {
            return null;
        }
        historyManager.add(subTaskStorage.get(id));
        return subTaskStorage.get(id);
    }

    /* SUBTASKS.Получение всех подзадач */
    @Override
    public ArrayList<Subtask> getAllSubTasks() {
        if (subTaskStorage.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(subTaskStorage.values());
    }

    /* Получение списка всех подзадач определенного EPIC */
    @Override
    public List<Subtask> getSubTasksOfEpic(Epic epic) {
        if (epicStorage.containsKey(epic.getId())) {
            List<Subtask> subtasksOfEpic = new ArrayList<>();
            for (Integer subTaskNum : epic.getSubTaskIds()) {
                subtasksOfEpic.add(subTaskStorage.get(subTaskNum));
            }
            return subtasksOfEpic;
        } else {
            return new ArrayList<>();
        }
    }

    /* SUBTASKS.Удаление по ID */
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
        updateEpicTime(epic);
        prioritizedTasks.remove(subtask);
    }

    /* SUBTASKS.Удаление всех задач */
    @Override
    public void removeAllSubTasks() {
        if (subTaskStorage.isEmpty()) {
            return;
        }
        for (Epic epic : epicStorage.values()) {
            for (Integer subTasksId : epic.getSubTaskIds()) {
                subTaskStorage.remove(subTasksId);
                historyManager.remove(subTasksId);
                prioritizedTasks.remove(subTaskStorage.get(subTasksId));
            }
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
        }
    }

    /* SUBTASKS.Обновление */
    @Override
    public void updateSubTask(Subtask updateSubtask) {
        if (updateSubtask == null) {
            return;
        }
        Subtask subTaskToUpdate = subTaskStorage.get(updateSubtask.getId());
        if (subTaskToUpdate == null) {
            return;
        }
        Epic epicToUpdate = epicStorage.get(updateSubtask.getEpicId());
        if (epicStorage.containsKey(epicToUpdate.getId())) {
            subTaskStorage.put(updateSubtask.getId(), updateSubtask);
            updateEpicStatus(epicToUpdate);
            addTaskToPrioritizedList(updateSubtask);
            updateEpicTime(epicToUpdate);
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
