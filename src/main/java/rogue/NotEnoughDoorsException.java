package rogue;

public class NotEnoughDoorsException extends Exception {

    /** constructor call. */
    public NotEnoughDoorsException() {
        super("Not enough doors in room");
    }
}
