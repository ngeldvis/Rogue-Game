package rogue;

import java.util.Map;

public class Potion extends Magic implements Edible, Tossable {

    /** Constructor creates a new Potion object.
     *  @param newPotion map that gives information about the item
     *  @param rogue the rogue class
     */
    public Potion(Map<String, String> newPotion, Rogue rogue) {
        super(newPotion, rogue);
    }

    /** called when the player eats an item.
     *  @return returns the description given about the item (String)
     */
    @Override
    public String eat() {
        String[] str = getDescription().split(": ");
        return str[0];
    }

    /** called when the player tosses an item.
     *  @return returns the description given about the item (String)
     */
    @Override
    public String toss() {
        String[] str = getDescription().split(": ");
        if (str.length < 2) {
            return str[0];
        }
        return str[1];
    }
}
