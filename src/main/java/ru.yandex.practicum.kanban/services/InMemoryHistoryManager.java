package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.services.interfaces.HistoryManager;
import ru.yandex.practicum.kanban.models.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasks = new ArrayList<>();
    private final Map<Integer, Node<Task>> historyTasks;
    public Node<Task> first;
    public Node<Task> last;

    public InMemoryHistoryManager() {
        historyTasks = new HashMap<>();
    }

    public void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task, last, null);
        historyTasks.put(task.getTaskId(), newNode);

        if (last == null) {
            first = newNode;
        } else {
            last.setNext(newNode);
        }
        last = newNode;
    }

    public List<Task> getTasks() {
        Node<Task> valueTask = first;

        while (valueTask != null) {
            tasks.add(valueTask.getValue());
            valueTask = first.getNext();
        }
        return new ArrayList<>(tasks);
    }

    public void removeNode(Node<Task> taskNode) {
        if (taskNode == null) {
            return;
        }
        taskNode.setValue(null);
        if (taskNode.equals(first) && taskNode.equals(last)) {
            first = null;
            last = null;
            return;
        }
        if (!(taskNode.equals(first)) && taskNode.equals(last)) {
            last = taskNode.getPrev();
            last.setNext(null);
            return;
        }
        if (taskNode.equals(first) && !(taskNode.equals(last))) {
            first = taskNode.getNext();
            last.setPrev(null);
        } else {
            Node<Task> next = taskNode.getNext();
            Node<Task> prev = taskNode.getPrev();
            prev.getNext() = next; // Доработать
            next.getPrev() = prev; // Доработать
        }

    }

    // Добавить
    @Override
    public void add(Task task) {

    }

    @Override
    public void remove(int id) {
        historyTasks.remove(id);
    }

    // Получить
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
