package ru.yandex.practicum.kanban.models;

import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;


import java.time.Instant;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected Type type;
    protected long duration;
    protected Instant startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.type = Type.TASK;
        this.status = Status.NEW;
    }

    public Task(int id, String name, String description, Status status, Type type) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.id = id;
    }

    public Task(int id, String name, String description, Status status, long duration, Instant startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, long duration, Instant startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.type = Type.TASK;
        this.duration = duration;
        this.startTime = startTime;

    }

    public String getName() {
        return name;
    }

    public void setName(String taskName) {
        this.name = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String taskDescription) {
        this.description = taskDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public Type getType() {
        return type;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        long seconds = 60L;
        return startTime.plusSeconds(duration * seconds);
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
        Task task = (Task) o;
        return id == task.id
                && duration == task.duration
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status
                && type == task.type
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, type, duration, startTime);
    }

}
