/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    /** The deadline entered by the user. */
    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description the text describing the task
     * @param by the deadline for completing the task
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this task in Moon's display format.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
    }

    /**
     * Returns this deadline in the format used by the data file.
     *
     * @return a line that can be saved to Moon's data file
     */
    @Override
    public String toSaveFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}
