package rogue;

import java.util.Map;

public class Magic extends Item {

    /** Constructor creates a new Magic object.
     *  @param newMagicItem map that gives information about the item
     *  @param rogue the rogue class
     */
    public Magic(Map<String, String> newMagicItem, Rogue rogue) {
        super(newMagicItem, rogue);
    }
}
