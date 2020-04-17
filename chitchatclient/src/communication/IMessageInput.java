package communication;

/**
 * Encapsulate input such that messages can be taken from any source.
 */
public interface IMessageInput {

    /**
     * Check if input source is still producing messages.
     * @return True if input source still has messages to produce,
     * false if no more messages are available and user should be logged out.
     */
    boolean isActive();

    /**
     * Get next message.
     * @return Next message from input interface as String.
     */
    String readLine();

}
