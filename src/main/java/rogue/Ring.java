package rogue;

import java.util.Map;

public class Ring extends Magic implements Wearable {

    /** Constructor creates a new Ring object.
     *  @param newRing map that gives information about the item
     *  @param rogue the rogue class
     */
    public Ring(Map<String, String> newRing, Rogue rogue) {
        super(newRing, rogue);
    }

    /** called when the player wears an item.
     *  @return returns the description given about the item (String)
     */
    @Override
    public String wear() {
        return getDescription();
    }
}
