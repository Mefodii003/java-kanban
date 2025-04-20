public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создаем задачи с статусом NEW
        Task task = new Task("Task 1", "Description of task", TaskStatus.NEW);
        taskManager.createTask(task);

        Epic epic = new Epic("Epic 1", "Description of epic");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description of subtask", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        // Получаем задачи
        Task retrievedTask = taskManager.getTask(task.getId());
        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());

        // Выводим на экран
        System.out.println("Retrieved Task: " + retrievedTask);
        System.out.println("Retrieved Epic: " + retrievedEpic);
        System.out.println("Retrieved Subtask: " + retrievedSubtask);
    }
}