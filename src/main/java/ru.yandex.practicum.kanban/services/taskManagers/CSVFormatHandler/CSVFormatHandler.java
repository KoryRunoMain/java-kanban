package ru.yandex.practicum.kanban.services.taskManagers.CSVFormatHandler;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.exceptions.ManagerSaveException;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static final String DELIMITER = ",";
    private static final Integer TASK_LENGTH = 7;
    private static final Integer TASK_LENGTH_WITH_EPICID = 8;

    /*Получить заголовок*/
    public String getHeader() {
        return "id,type,name,status,description,epic,duration,startTime" + "\n";
    }

    /*Получить String из задач*/
    public String generateToString(Task task) {
        return  task.getId() + DELIMITER +              // id
                task.getType() + DELIMITER +            // type
                task.getName() + DELIMITER +            // name
                task.getStatus() + DELIMITER +          // status
                task.getDescription() + DELIMITER +     // description
                getEpicIdOfSubTask(task) + DELIMITER +  // epic id
                task.getDuration() + DELIMITER +        // duration
                getTaskStartTime(task);                 // startTime
    }

    /*Получить задачи из String*/
    public Task fromString(String value) {
        String[] values = value.split(DELIMITER);
        int valuesLength = values.length;

        if (valuesLength != TASK_LENGTH &&
            valuesLength != TASK_LENGTH_WITH_EPICID) {
            return null;
        }

        int epicId = 0;
        int id = Integer.parseInt(values[0]);              // id
        Type type = Type.valueOf(values[1]);               // type
        String name = values[2];                           // name
        Status status = Status.valueOf(values[3]);         // status
        String description = values[4];                    // description
        if (type.equals(Type.SUBTASK)) {
            epicId = Integer.parseInt(values[5]);          // epic id
        }
        long duration = Long.parseLong(values[6]);         // duration
        Instant startTime = Instant.parse(values[7]);      // startTime

        switch (type) {
            case EPIC -> {return new Epic(id, name, description, status, duration, startTime);}
            case SUBTASK -> {return new Subtask(epicId, name, description, duration, startTime, status);}
            default -> {return new Task(id, name, description, status, duration, startTime);}
        }
    }

    /*Получить String из истории*/
    public String generateHistoryToString(HistoryManager historyManager) {
        List<String> result = new ArrayList<>();
        for (Task task: historyManager.getHistory()) {
            result.add(String.valueOf(task.getId()));
        }
        return String.join(DELIMITER, result);
    }

    /*Получить историю из String*/
    public List<Integer> historyFromString(String values) {
        List<Integer> taskIDs = new ArrayList<>();
        if (values == null) {
            return taskIDs;
        }
        String[] splitValue = values.split(DELIMITER);
        for (String value : splitValue) {
            taskIDs.add(Integer.parseInt(value));
        }
        return taskIDs;
    }

    /*Получить начальное время*/
    private static Instant getTaskStartTime(Task task) {
        return task.getStartTime();
    }

    /*Получить ID задачи Epic для подзадачи SubTask*/
    private static String getEpicIdOfSubTask(Task task) {
        String epicId = "";
        if (task.getType() == Type.SUBTASK) {
            Subtask subtask = (Subtask) task;
            epicId = String.valueOf(subtask.getEpicId());
            return epicId;
        }
        return epicId;
    }

}
