package rogue;

import java.util.Map;

public class Clothing extends Item implements Wearable {

    /** Constructor creates a new Clothing object.
     *  @param newClothing map that gives information about the item
     *  @param rogue the rogue class
    */
    public Clothing(Map<String, String> newClothing, Rogue rogue) {
        super(newClothing, rogue);
    }

    /** called when the player wears an item.
     *  @return returns the description given about the item (String)
     */
    @Override
    public String wear() {
        return getDescription();
    }
}
