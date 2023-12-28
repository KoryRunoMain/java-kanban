package ru.yandex.practicum.kanban.services.historyManagers;

import ru.yandex.practicum.kanban.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    protected final Map<Integer, Node<Task>> historyTasks;
    protected Node<Task> head;
    protected Node<Task> tail;

    public InMemoryHistoryManager() {
        historyTasks = new HashMap<>();
    }


    /*Добавить задачу*/
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (historyTasks.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    /*Удалить задачу*/
    @Override
    public void remove(int id) {
        removeNode(historyTasks.get(id));
        historyTasks.remove(id);
    }

    /*Получить историю просмотров*/
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    /*Получить список задач*/
    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> taskNode = head;

        while (taskNode != null) {
            tasks.add(taskNode.data);
            taskNode = taskNode.next;
        }
        return tasks;
    }

    /*Добавить задачу в конец списка*/
    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(tail, task, null);
        historyTasks.put(task.getId(), newNode);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }

    /*Удалить узел из двухсвязного списка*/
    private void removeNode(Node<Task> taskNode) {
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

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "historyTasks=" + historyTasks +
                '}';
    }

}
