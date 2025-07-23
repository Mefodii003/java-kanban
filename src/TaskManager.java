import java.util.List;

public interface TaskManager {
    // TASK
    void createTask(Task task);
    List<Task> getAllTasks();
    void deleteAllTasks();
    Task getTask(int id);
    void updateTask(Task task);
    void deleteTask(int id);

    // EPIC
    void createEpic(Epic epic);
    List<Epic> getAllEpics();
    void deleteAllEpics();
    Epic getEpic(int id);
    void updateEpic(Epic epic);
    void deleteEpic(int id);

    // SUBTASK
    void createSubtask(Subtask subtask);
    List<Subtask> getAllSubtasks();
    void deleteAllSubtasks();
    Subtask getSubtask(int id);
    void updateSubtask(Subtask subtask);
    void deleteSubtask(int id);

    // SUBTASKS of specific epic
    List<Subtask> getSubtasksOfEpic(int epicId);

    // HISTORY
    List<Task> getHistory();
}