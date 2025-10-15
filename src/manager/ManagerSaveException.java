package manager;

import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }

    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}