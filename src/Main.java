public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task1 = new Task(0, "Купить продукты", "Купить овощи и фрукты", TaskStatus.NEW);
        Task task2 = new Task(0, "Помыть машину", "Помыть машину после поездки", TaskStatus.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        // Передаем описание для Epic
        Epic epic = new Epic(0, "Переезд", "Переезд на новое место жительства");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(0, "Упаковать вещи", "Упаковать все вещи в коробки", TaskStatus.NEW, epic.getId());
        Subtask subtask2 = new Subtask(0, "Заказать грузчиков", "Найти и заказать грузчиков для переезда", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        System.out.println("Все задачи: " + manager.getTasks());
        System.out.println("Все эпики: " + manager.getEpics());
        System.out.println("Все подзадачи: " + manager.getSubtasks());
    }
}