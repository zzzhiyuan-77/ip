/**
 * Represents a task with a start and end time.
 */
public class Event extends Task {
    /** The start time entered by the user. */
    private final String from;

    /** The end time entered by the user. */
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description the text describing the event
     * @param from the event's start time
     * @param to the event's end time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this task in Moon's display format.
     *
     * @return the formatted event task
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description
                + " (from: " + from + " to: " + to + ")";
    }
}
