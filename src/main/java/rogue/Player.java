package rogue;

import java.util.ArrayList;
import java.awt.Point;

import java.util.Random;

import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private Room room;
    private Point xyLocation;
    private ArrayList<Item> inventory;
    private ArrayList<Item> equippedItems;
    public static final long serialVersionUID = 761597850956258323L;

    // CONSTRUCTORS

    /** Default constructor. */
    public Player() {
        name = "John";
        room = null;
        xyLocation = new Point(1, 1);
        inventory = new ArrayList<>();
        equippedItems = new ArrayList<>();
    }

    /** Constructor to create new Player object with a player name.
     *  @param newName item object
     */
    public Player(String newName) {
        name = newName;
        xyLocation = new Point(1, 1);
        inventory = new ArrayList<>();
        equippedItems = new ArrayList<>();
    }

    // GETTERS AND SETTERS

    /** Gets the name of the player.
     *  @return returns name of the player
     */
    public String getName() {
        return name;
    }

    /** Sets the name of the player.
     *  @param newName name of the player
     */
    public void setName(String newName) {
        name = newName;
    }

    /** Gets the location of the player.
     *  @return returns location of the player
     */
    public Point getXyLocation() {
        return xyLocation;
    }

    /** Sets the location of the player.
     *  @param newXyLocation location of the player
     */
    public void setXyLocation(Point newXyLocation) {
        xyLocation = newXyLocation;
    }

    /** Gets the current room of the player.
     *  @return returns current room of the player
     */
    public Room getCurrentRoom() {
        return room;
    }

    /** Sets the current room of the player.
     *  @param newRoom current room of the player
     */
    public void setCurrentRoom(Room newRoom) {
        room = newRoom;
        newRoom.setPlayer(this);
        newRoom.setPlayerInRoom(true);
    }

    /** Gets the inventory of the player.
     *  @return returns the inventory of the player
     */
    public ArrayList<Item> getInventory() {
        return inventory;
    }

    /** Sets the inventroy of the player.
     *  @param newInventory new list of items the player has
     */
    public void setInventory(ArrayList<Item> newInventory) {
        inventory = newInventory;
    }

    /** adds an item to inventory.
     *  @param newItem item to add to inventory
     */
    public void pickUp(Item newItem) {
        inventory.add(newItem);
    }

    /** takes an item from inventory and equips it.
     *  @param toEquip item to equip
     */
    public void equip(Item toEquip) {
        if (removeFromInventory(toEquip) == 1) {
            equippedItems.add(toEquip);
        }
    }

    /** remove and item from the player's inventory.
     *  @param item item to be removed
     *  @return 1 on success and -1 on failure ie. item is not in the inventory
     */
    public int removeFromInventory(Item item) {
        int index = inventory.indexOf(item);
        if (index != -1) {
            inventory.remove(index);
            return 1; // SUCCESS
        }
        return -1; // FAIL
    }

    /** makes a string to display all items in the players inventory.
     *  @return the string of all items
     */
    public String getInventoryAsString() {
        String s = "";
        for (Item i : inventory) {
            s += i.getName() + "\n";
        }
        return s;
    }

    /** makes a string to display all the items the player has equipped.
     *  @return the string of the items
     */
    public String getEquippedAsString() {
        String s = "";
        for (Item i : equippedItems) {
            s += i.getName() + "\n";
        }
        return s;
    }

    /** throw an item from inventory into a random place in the room.
     *  @param item item to be thrown
     */
    public void throwItem(Item item) {
        item.setCurrentRoom(room);
        item.setRoomId(room.getId());
        Random rand = new Random();
        int x = rand.nextInt(room.getWidth() - 2) + 1;
        int y = rand.nextInt(room.getHeight() - 2) + 1;
        item.setXyLocation(new Point(x, y));
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        room.setItems(itemList);
        removeFromInventory(item);
    }
}
