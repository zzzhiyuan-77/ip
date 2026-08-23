import java.util.Scanner;

public class Moon {
    /**
     * Starts the chatbot and reads commands from the user.
     *
     * @param args command-line arguments, which are not used by this program
     */
    public static void main(String[] args) {
        String divider = "____________________________________________________________";
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println(divider);
        System.out.println("Hello! I'm Moon, your personal chatbot.");
        System.out.println("What can I do for you?");
        System.out.println(divider);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            System.out.println(divider);
            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                return;
            }

            if (command.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].unmarkAsDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);
            } else if (command.startsWith("todo ")) {
                taskCount = addTask(tasks, taskCount, new ToDo(command.substring(5)));
            } else if (command.startsWith("deadline ")) {
                int byIndex = command.indexOf(" /by ");
                String description = command.substring(9, byIndex);
                String by = command.substring(byIndex + 5);
                taskCount = addTask(tasks, taskCount, new Deadline(description, by));
            } else if (command.startsWith("event ")) {
                int fromIndex = command.indexOf(" /from ");
                int toIndex = command.indexOf(" /to ");
                String description = command.substring(6, fromIndex);
                String from = command.substring(fromIndex + 7, toIndex);
                String to = command.substring(toIndex + 5);
                taskCount = addTask(tasks, taskCount, new Event(description, from, to));
            } else {
                taskCount = addTask(tasks, taskCount, new ToDo(command));
            }
            System.out.println(divider);
        }
    }

    /**
     * Adds a task to the list and displays Moon's confirmation message.
     *
     * @param tasks the array that stores tasks
     * @param taskCount the number of tasks currently stored
     * @param task the task to add
     * @return the number of tasks after adding the task
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) {
        tasks[taskCount] = task;
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        taskCount++;
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        return taskCount;
    }
}
