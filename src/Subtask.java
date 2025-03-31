public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String name, TaskStatus status, int epicId) {
        super(id, name, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{id=" + id + ", name='" + name + "', status=" + status + ", epicId=" + epicId + "}";
    }
}