package rogue;

public class ImpossiblePositionException extends Exception {

    /** Constructor call. */
    public ImpossiblePositionException() {
        super("item is located at an impossible position");
    }
}
