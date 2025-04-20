import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds = new ArrayList<>();


    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }


    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }


    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }


    public void clearSubtaskIds() {
        subtaskIds.clear();
    }


    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}