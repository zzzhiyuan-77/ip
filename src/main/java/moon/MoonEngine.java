package moon;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Provides Moon's task-management commands independently of a user interface. */
public class MoonEngine {
    private final Storage storage;
    private final List<Task> tasks;
    private final boolean loadingError;

    /** Creates an engine and loads Moon's saved tasks. */
    public MoonEngine() {
        storage = new Storage();
        List<Task> loadedTasks;
        boolean failedToLoad;
        try {
            loadedTasks = storage.load();
            failedToLoad = false;
        } catch (IOException exception) {
            loadedTasks = new ArrayList<>();
            failedToLoad = true;
        }
        tasks = loadedTasks;
        loadingError = failedToLoad;
    }

    /**
     * Returns whether Moon had to start with an empty list because saved data could not be read.
     *
     * @return whether loading saved tasks failed
     */
    public boolean hasLoadingError() {
        return loadingError;
    }

    /**
     * Processes one command and returns the response for a user interface to display.
     *
     * @param command the command entered by the user
     * @return Moon's response text
     */
    public String getResponse(String command) {
        if (command.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        }
        try {
            return processCommand(command);
        } catch (MoonException exception) {
            return " Oof! " + exception.getMessage() + System.lineSeparator();
        } catch (IOException exception) {
            return " Oof! I couldn't save your task list." + System.lineSeparator();
        }
    }

    private String processCommand(String command) throws MoonException, IOException {
        StringBuilder response = new StringBuilder();
        if (command.equals("list")) {
            printTaskList(response);
            return response.toString();
        }
        if (command.equals("find") || command.startsWith("find ")) {
            printMatchingTasks(command, response);
            return response.toString();
        }
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new MoonException("your todo needs a description.");
            }
            addTask(new ToDo(description), response);
            return response.toString();
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            addTask(parseDeadline(command), response);
            return response.toString();
        }
        if (command.equals("event") || command.startsWith("event ")) {
            addTask(parseEvent(command), response);
            return response.toString();
        }
        if (command.equals("mark") || command.startsWith("mark ")) {
            Task task = tasks.get(findTaskIndex(command, "mark"));
            task.markAsDone();
            storage.save(tasks);
            appendLine(response, " Nice! I've marked this task as done:");
            appendLine(response, "   " + task);
            return response.toString();
        }
        if (command.equals("unmark") || command.startsWith("unmark ")) {
            Task task = tasks.get(findTaskIndex(command, "unmark"));
            task.unmarkAsDone();
            storage.save(tasks);
            appendLine(response, " OK, I've marked this task as not done yet:");
            appendLine(response, "   " + task);
            return response.toString();
        }
        if (command.equals("delete") || command.startsWith("delete ")) {
            Task removedTask = tasks.remove(findTaskIndex(command, "delete"));
            storage.save(tasks);
            appendLine(response, " Noted. I've removed this task:");
            appendLine(response, "   " + removedTask);
            appendLine(response, " Now you have " + tasks.size() + " tasks in the list.");
            return response.toString();
        }
        throw new MoonException("I don't recognise that command. Try todo, deadline, event, "
                + "find, list, mark, unmark, delete, or bye.");
    }

    private Deadline parseDeadline(String command) throws MoonException {
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
        return new Deadline(description, parseDate(by));
    }

    private Event parseEvent(String command) throws MoonException {
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
        return new Event(description, parseDate(from), parseDate(to));
    }

    private LocalDate parseDate(String dateText) throws MoonException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException exception) {
            throw new MoonException("use a date in yyyy-MM-dd format, for example: 2019-12-02.");
        }
    }

    private int findTaskIndex(String command, String action) throws MoonException {
        String numberText = command.substring(action.length()).trim();
        if (numberText.isEmpty()) {
            throw new MoonException("tell me which task number to " + action + ".");
        }
        try {
            int taskNumber = Integer.parseInt(numberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new MoonException("task " + taskNumber + " is not in your list yet.");
            }
            return taskNumber - 1;
        } catch (NumberFormatException exception) {
            throw new MoonException("use a task number after " + action + ", for example: "
                    + action + " 1.");
        }
    }

    private void printTaskList(StringBuilder response) {
        appendLine(response, " Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            appendLine(response, " " + (i + 1) + "." + tasks.get(i));
        }
    }

    private void printMatchingTasks(String command, StringBuilder response) throws MoonException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new MoonException("your find needs a keyword.");
        }

        appendLine(response, " Here are the matching tasks in your list:");
        int matchingTaskNumber = 1;
        for (Task task : tasks) {
            if (task.matchesKeyword(keyword)) {
                appendLine(response, " " + matchingTaskNumber + "." + task);
                matchingTaskNumber++;
            }
        }
    }

    private void addTask(Task task, StringBuilder response) throws IOException {
        tasks.add(task);
        storage.save(tasks);
        appendLine(response, " Got it. I've added this task:");
        appendLine(response, "   " + task);
        appendLine(response, " Now you have " + tasks.size() + " tasks in the list.");
    }

    private void appendLine(StringBuilder response, String line) {
        response.append(line).append(System.lineSeparator());
    }
}
