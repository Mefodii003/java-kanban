public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task1 = new Task(manager.generateId(), "Купить продукты", "Сходить в магазин", TaskStatus.NEW);
        Task task2 = new Task(manager.generateId(), "Помыть машину", "Очистить снаружи и внутри", TaskStatus.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic(manager.generateId(), "Переезд", "Организовать переезд в новую квартиру");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask(manager.generateId(), "Упаковать вещи", "Собрать вещи в коробки", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask(manager.generateId(), "Заказать грузчиков", "Найти грузчиков и договориться о дате", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        System.out.println("Все задачи: " + manager.tasks);
        System.out.println("Все эпики: " + manager.epics);
        System.out.println("Все подзадачи: " + manager.subtasks);
    }
}