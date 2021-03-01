package rogue;

import java.util.Map;

public class Food extends Item implements Edible {

    /** Constructor creates a new Food object.
     *  @param newFood map that gives information about the item
     *  @param rogue the rogue class
     */
    public Food(Map<String, String> newFood, Rogue rogue) {
        super(newFood, rogue);
    }

    /** called when the player eats an item.
     *  @return returns the description given about the item (String)
     */
    @Override
    public String eat() {
        return getDescription();
    }
}
