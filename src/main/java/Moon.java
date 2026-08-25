import java.util.Scanner;

/**
 * Runs Moon, a chatbot that manages a list of tasks.
 */
public class Moon {
    /** The maximum number of tasks Moon stores while it is running. */
    private static final int MAX_TASKS = 100;

    /** The line used to separate Moon's messages. */
    private static final String DIVIDER = "____________________________________________________________";

    /**
     * Starts the chatbot and reads commands from the user.
     *
     * @param args command-line arguments, which are not used by this program
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        System.out.println(DIVIDER);
        System.out.println("Hello! I'm Moon, your personal chatbot.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            System.out.println(DIVIDER);

            try {
                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(DIVIDER);
                    return;
                }
                taskCount = processCommand(command, tasks, taskCount);
            } catch (MoonException exception) {
                System.out.println(" Oof! " + exception.getMessage());
            }

            System.out.println(DIVIDER);
        }
    }

    /**
     * Processes one command and returns the resulting number of stored tasks.
     *
     * @param command the command entered by the user
     * @param tasks the array that stores tasks
     * @param taskCount the number of tasks currently stored
     * @return the number of tasks after processing the command
     * @throws MoonException if the command is invalid
     */
    private static int processCommand(String command, Task[] tasks, int taskCount) throws MoonException {
        if (command.equals("list")) {
            printTaskList(tasks, taskCount);
            return taskCount;
        }
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new MoonException("your todo needs a description.");
            }
            return addTask(tasks, taskCount, new ToDo(description));
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return addTask(tasks, taskCount, parseDeadline(command));
        }
        if (command.equals("event") || command.startsWith("event ")) {
            return addTask(tasks, taskCount, parseEvent(command));
        }
        if (command.equals("mark") || command.startsWith("mark ")) {
            Task task = findTask(command, "mark", tasks, taskCount);
            task.markAsDone();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + task);
            return taskCount;
        }
        if (command.equals("unmark") || command.startsWith("unmark ")) {
            Task task = findTask(command, "unmark", tasks, taskCount);
            task.unmarkAsDone();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + task);
            return taskCount;
        }
        throw new MoonException("I don't recognise that command. Try todo, deadline, event, list, mark, unmark, or bye.");
    }

    /**
     * Creates a deadline from a validated deadline command.
     *
     * @param command the deadline command
     * @return the deadline described by the command
     * @throws MoonException if its description or deadline is missing
     */
    private static Deadline parseDeadline(String command) throws MoonException {
        String details = command.substring("deadline".length()).trim();
        int byIndex = details.indexOf(" /by ");
        if (byIndex < 0) {
            throw new MoonException("a deadline needs /by followed by its due date or time.");
        }
        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty()) {
            throw new MoonException("your deadline needs a description before /by.");
        }
        if (by.isEmpty()) {
            throw new MoonException("your deadline needs a date or time after /by.");
        }
        return new Deadline(description, by);
    }

    /**
     * Creates an event from a validated event command.
     *
     * @param command the event command
     * @return the event described by the command
     * @throws MoonException if its description, start time, or end time is missing
     */
    private static Event parseEvent(String command) throws MoonException {
        String details = command.substring("event".length()).trim();
        int fromIndex = details.indexOf(" /from ");
        int toIndex = details.indexOf(" /to ");
        if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
            throw new MoonException("an event needs /from and /to times.");
        }
        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = details.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty()) {
            throw new MoonException("your event needs a description before /from.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new MoonException("your event needs both a start time and an end time.");
        }
        return new Event(description, from, to);
    }

    /**
     * Finds the task identified by a mark or unmark command.
     *
     * @param command the command entered by the user
     * @param action the command word, either {@code mark} or {@code unmark}
     * @param tasks the array that stores tasks
     * @param taskCount the number of tasks currently stored
     * @return the requested task
     * @throws MoonException if no valid task number was provided
     */
    private static Task findTask(String command, String action, Task[] tasks, int taskCount)
            throws MoonException {
        String numberText = command.substring(action.length()).trim();
        if (numberText.isEmpty()) {
            throw new MoonException("tell me which task number to " + action + ".");
        }
        try {
            int taskNumber = Integer.parseInt(numberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new MoonException("task " + taskNumber + " is not in your list yet.");
            }
            return tasks[taskNumber - 1];
        } catch (NumberFormatException exception) {
            throw new MoonException("use a task number after " + action + ", for example: " + action + " 1.");
        }
    }

    /**
     * Prints every task currently stored by Moon.
     *
     * @param tasks the array that stores tasks
     * @param taskCount the number of tasks currently stored
     */
    private static void printTaskList(Task[] tasks, int taskCount) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i]);
        }
    }

    /**
     * Adds a task to the list and displays Moon's confirmation message.
     *
     * @param tasks the array that stores tasks
     * @param taskCount the number of tasks currently stored
     * @param task the task to add
     * @return the number of tasks after adding the task
     * @throws MoonException if the task list has reached its maximum capacity
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) throws MoonException {
        if (taskCount == MAX_TASKS) {
            throw new MoonException("your task list is full — 100 tasks is the max.");
        }
        tasks[taskCount] = task;
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        taskCount++;
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        return taskCount;
    }
}
