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
                    System.out.println(" " + (i + 1) + ".[" + tasks[i].getStatusIcon()
                            + "] " + tasks[i].getDescription());
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   [X] " + tasks[taskIndex].getDescription());
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].unmarkAsDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   [ ] " + tasks[taskIndex].getDescription());
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println(" added: " + command);
            }
            System.out.println(divider);
        }
    }
}
