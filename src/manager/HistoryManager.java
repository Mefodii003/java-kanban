package manager;

import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}