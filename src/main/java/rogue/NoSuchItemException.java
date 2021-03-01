package rogue;

public class NoSuchItemException extends Exception {

    /** Constructor call. */
    public NoSuchItemException() {
        super("No such item is found");
    }
}
