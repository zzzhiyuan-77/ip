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
        String[] tasks = new String[100];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println(" added: " + command);
            }
            System.out.println(divider);
        }
    }
}
