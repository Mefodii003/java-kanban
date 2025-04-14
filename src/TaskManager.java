import java.util.List;

public interface TaskManager {
    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);

    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);

    List<Task> getHistory();

    void removeTask(int id);
    void removeEpic(int id);
    void removeSubtask(int id);
}