import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW);
        this.subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    @Override
    public String toString() {
        return "Epic{id=" + getId() + ", name='" + getName() + "', description='" + getDescription() + "', status=" + getStatus() + ", subtasks=" + subtasks + "}";
    }
}