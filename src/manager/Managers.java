package manager;

import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }
}