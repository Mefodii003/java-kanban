import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }

    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtask(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }

    public void clearSubtasks() {
        subtaskIds.clear();
    }

    public void updateEpicData(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            setStatus(TaskStatus.NEW);
            this.startTime = null;
            this.duration = Duration.ZERO;
            this.endTime = null;
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        LocalDateTime earliest = null;
        LocalDateTime latest = null;
        Duration totalDuration = Duration.ZERO;

        for (Subtask sub : subtasks) {
            TaskStatus status = sub.getStatus();
            if (status != TaskStatus.NEW) allNew = false;
            if (status != TaskStatus.DONE) allDone = false;

            if (sub.getStartTime() != null) {
                if (earliest == null || sub.getStartTime().isBefore(earliest)) earliest = sub.getStartTime();
                LocalDateTime subEnd = sub.getEndTime();
                if (latest == null || (subEnd != null && subEnd.isAfter(latest))) latest = subEnd;
            }

            if (sub.getDuration() != null) totalDuration = totalDuration.plus(sub.getDuration());
        }

        if (allDone) setStatus(TaskStatus.DONE);
        else if (allNew) setStatus(TaskStatus.NEW);
        else setStatus(TaskStatus.IN_PROGRESS);

        this.startTime = earliest;
        this.duration = totalDuration;
        this.endTime = latest;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}