package ru.yandex.practicum.kanban.services.taskManagers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.enums.Type;
import ru.yandex.practicum.kanban.httpServer.KVTaskClient;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private Gson gson = Managers.getGson();

    public HttpTaskManager(HistoryManager historyManager, String urlPath) {
        super(historyManager);
        client = new KVTaskClient(urlPath);
        save();
        loadFromServer();
    }


    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(taskStorage.values()));
        client.saveTasks(jsonTasks);
        String jsonEpics = gson.toJson(new ArrayList<>(taskStorage.values()));
        client.saveEpics(jsonEpics);
        String jsonSubtasks = gson.toJson(new ArrayList<>(taskStorage.values()));
        client.saveSubTasks(jsonSubtasks);
        String jsonHistory = gson.toJson(this.getHistory().stream()
                .map(Task::getId).
                collect(Collectors.toList()));
        client.saveHistory(jsonHistory);
    }

    public void loadFromServer() {
        List<Task> tasksList = gson.fromJson(client.load(Type.TASK), new TypeToken<ArrayList<Task>>() {
        }.getType());
        for (Task taskValue : tasksList) {
            taskStorage.put(taskValue.getId(), taskValue);
            addTaskToPrioritizedList(taskValue);
        }
        List<Epic> epicsList = gson.fromJson(client.load(Type.EPIC), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        for (Epic epicValue : epicsList) {
            epicStorage.put(epicValue.getId(), epicValue);
        }
        List<Subtask> subtasksList = gson.fromJson(client.load(Type.SUBTASK), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        for (Subtask subtaskValue : subtasksList) {
            subTaskStorage.put(subtaskValue.getId(), subtaskValue);
            addTaskToPrioritizedList(subtaskValue);
        }
        List<Integer> historyList = gson.fromJson(client.load(Type.HISTORY), new TypeToken<ArrayList<Integer>>() {
        }.getType());
        for (Integer historyId : historyList) {
            if (taskStorage.containsKey(historyId)) {
                historyManager.add(taskStorage.get(historyId));
            }
            if (epicStorage.containsKey(historyId)) {
                historyManager.add(epicStorage.get(historyId));
            }
            if (subTaskStorage.containsKey(historyId)) {
                historyManager.add(subTaskStorage.get(historyId));
            }
        }
    }

}
