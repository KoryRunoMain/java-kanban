package ru.yandex.practicum.kanban.models;

import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    protected final List<Integer> subTasksIds;
    protected LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = Type.EPIC;
        this.subTasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, long duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.type = Type.EPIC;
        this.subTasksIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status,long duration,
                LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, description, status, duration, startTime);
        this.type = Type.EPIC;
        this.subTasksIds = new ArrayList<>();
        this.endTime = endTime;
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

    public void setDuration() {
        long duration = 0;
        for (Integer subtask : subTasksIds) {
            duration = duration + subtask.compareTo((int) getDuration());
        }
        this.duration = duration;
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
