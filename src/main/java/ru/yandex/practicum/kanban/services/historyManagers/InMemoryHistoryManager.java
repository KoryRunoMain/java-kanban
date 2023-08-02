package ru.yandex.practicum.kanban.services.historyManagers;

import ru.yandex.practicum.kanban.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasks;
    private final Map<Integer, Node<Task>> historyTasks;
    public Node<Task> head;
    public Node<Task> tail;

    public InMemoryHistoryManager() {
        tasks = new ArrayList<>();
        historyTasks = new HashMap<>();
    }

    /* Добавление задачи в конец списка */
    public void linkLast(Task task) {
        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(oldTail, task, null);
        historyTasks.put(task.getTaskId(), newNode);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    /* Сбор всех задач в список выдачи истории просмотров */
    public List<Task> getTasks() {
        Node<Task> taskNode = head;

        while (taskNode != null) {
            tasks.add(taskNode.data);
            taskNode = taskNode.next;
        }
        return new ArrayList<>(tasks);
    }

    /* Удаление узла задачи из двухсвязного списка */
    public void removeNode(Node<Task> taskNode) {
        if (taskNode == null) {
            return;
        }
        taskNode.data = null;

        if (head == taskNode && tail == taskNode) {
            head = null;
            tail = null;
            return;
        }
        if (head == taskNode) {
            head = taskNode.next;
            head.prev = null;
            return;
        }
        if (tail == taskNode) {
            tail = taskNode.prev;
            tail.next = null;
            return;
        }
        taskNode.prev.next = taskNode.next;
        taskNode.next.prev = taskNode.prev;
    }

    /* Добавить задачу */
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getTaskId());
        linkLast(task);
    }

    /* Удалить задачу */
    @Override
    public void remove(int id) {
        removeNode(historyTasks.get(id));
    }

    /* Получить историю просмотров */
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "historyTasks=" + historyTasks +
                '}';
    }

}
