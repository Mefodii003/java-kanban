import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Task> history = new LinkedHashMap<>();

    @Override
    public void add(Task task) {
        history.remove(task.getId());
        history.put(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history.values());
    }
}