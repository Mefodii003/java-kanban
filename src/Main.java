public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task1 = new Task(0, "Купить продукты", TaskStatus.NEW);
        Task task2 = new Task(0, "Помыть машину", TaskStatus.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic = new Epic(0, "Переезд");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Упаковать вещи", TaskStatus.NEW, epic.getId());
        Subtask subtask2 = new Subtask(0, "Заказать грузчиков", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        System.out.println("Все задачи: " + manager.getTasks());
        System.out.println("Все эпики: " + manager.getEpics());
        System.out.println("Все подзадачи: " + manager.getSubtasks());
    }
}