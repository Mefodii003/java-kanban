import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;

    public void addTask(Task task) {
        task.id = nextId++;
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.id = nextId++;
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.id = nextId++;
        subtasks.put(subtask.getId(), subtask);

        if (epics.containsKey(subtask.getEpicId())) {
            epics.get(subtask.getEpicId()).addSubtask(subtask);
        }
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}