package rogue;
public class InvalidMoveException extends Exception {

    /** Constructor call. */
    public InvalidMoveException() {
        super();
    }

    /** Constructor call with message.
     *  @param message given message
     */
    public InvalidMoveException(String message) {
        super(message);
    }

}
