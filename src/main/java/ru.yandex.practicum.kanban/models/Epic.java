package ru.yandex.practicum.kanban.models;

import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subTasksIds;

    public Epic(String taskName, String taskDescription, Type type) {
        super(taskName, taskDescription);
        this.subTasksIds = new ArrayList<>();
        this.type = Type.EPIC;
    }

    public Epic(String taskName, String taskDescription, Type type, Status status) {
        super(taskName, taskDescription, type, status);
        this.subTasksIds = new ArrayList<>();
        this.type = type;
        this.status = status;
    }


    public ArrayList<Integer> getSubTask() {
        return new ArrayList<>(subTasksIds);
    }

    public void addSubtask(int id) {
        subTasksIds.add(id);
    }

    public void removeSubtask(int id) {
        subTasksIds.remove((Integer) id);
    }

    public void clearSubtaskIds() {
        subTasksIds.clear();
    }

    @Override
    public String toString() {
        return type + " {" +
                "ID= " + id +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
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
