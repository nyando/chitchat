package communication;

/**
 * Encapsulate output such that messages from server can be written to any output stream.
 */
public interface IMessageOutput {

    void printMessage(String msg);

}
