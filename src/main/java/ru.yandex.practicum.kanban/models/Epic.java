package ru.yandex.practicum.kanban.models;

import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    protected final List<Integer> subTasksIds;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        this.type = Type.EPIC;
        this.subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTask() {
        return new ArrayList<>(subTasksIds);
    }

    public void addSubtaskId(int id) {
        subTasksIds.add(id);
    }

    public void removeSubtask(int id) {
        subTasksIds.remove((Integer) id);
    }

    public void clearSubtaskIds() {
        subTasksIds.clear();
    }

    public List<Integer> getAllSubtaskIds() {
        return new ArrayList<>(subTasksIds);
    }

    @Override
    public String toString() {
        return type + " {" +
                "ID=" + id +
                ", taskName='" + name + '\'' +
                ", taskDescription='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksIds, epic.subTasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksIds);
    }
}
