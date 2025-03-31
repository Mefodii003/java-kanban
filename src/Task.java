public class Task {
    protected int id;
    protected String name;
    protected TaskStatus status;

    public Task(int id, String name, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", name='" + name + "', status=" + status + "}";
    }
}