package ru.yandex.practicum.kanban.models;

import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    protected final List<Integer> subTasksIds;
    static long epochSecond = 3712455237000L;
    protected static Instant endTime = Instant.ofEpochSecond(epochSecond);

    public Epic(String name, String description) {
        super(name, description);
        this.type = Type.EPIC;
        this.subTasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, long duration, Instant startTime, Status status) {
        super(name, description, duration, startTime, status);
        this.type = Type.EPIC;
        this.subTasksIds = new ArrayList<>();
        endTime = super.getEndTime();
    }

    public Epic(int id, String name, String description, Status status, long duration,
                Instant startTime) {
        super(id, name, description, status, duration, startTime);
        this.type = Type.EPIC;
        this.subTasksIds = new ArrayList<>();
        endTime = super.getEndTime();
    }


    public ArrayList<Integer> getSubTaskIds() {
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

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        Epic.endTime = endTime;
    }

    @Override
    public String toString() {
        return type + " {" +
                "ID=" + id +
                ", taskName='" + name + '\'' +
                ", taskDescription='" + description + '\'' +
                ", status=" + status + ", start time=" + startTime.toEpochMilli() +
                ", duration=" + duration + ", end time=" + getEndTime().toEpochMilli() + '\'' + '}';
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
