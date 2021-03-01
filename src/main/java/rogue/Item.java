package rogue;

import java.util.Map;

import java.awt.Point;

import java.io.Serializable;

public class Item implements Serializable {

    private int id;
    private int roomId;
    private Point xyLocation;
    private String name;
    private String type;
    private String description;
    private Character displayChar;
    private Room curRoom;
    private Rogue theGame;
    public static final long serialVersionUID = 2451778682887595033L;

    // CONSTSRUCTORS

    /** Default Constructor. */
    public Item() {
        id = 0;
        roomId = 0;
        xyLocation = new Point(1, 1);
        name = "";
        type = "";
        description = "";
        displayChar = ' ';
    }

    /** Constructor to create a new Item.
     *  @param newItem item to be added
     *  @param rogue rogue object
     */
    public Item(Map<String, String> newItem, Rogue rogue) {
        theGame = rogue;
        id = Integer.parseInt(newItem.get("id"));
        if (newItem.containsKey("room")) {
            roomId = Integer.parseInt(newItem.get("room"));
            xyLocation = new Point(Integer.parseInt(newItem.get("x")), Integer.parseInt(newItem.get("y")));
        }
        name = newItem.get("name");
        type = newItem.get("type");
        description = newItem.get("description");
        displayChar = theGame.getSymbol(type.toUpperCase());
    }

    // GETTERS AND SETTERS

    /** Gets the ID of an item.
     *  @return returns id of item
     */
    public int getId() {
        return id;
    }

    /** Sets the ID of an item.
     *  @param newId id of item
     */
    public void setId(int newId) {
        id = newId;
    }

    /** Gets the name of an item.
     *  @return returns name of item
     */
    public String getName() {
        return name;
    }

    /** Sets the name of an item.
     *  @param newName name of item
     */
    public void setName(String newName) {
        name = newName;
    }

    /** Gets the type of an item.
     *  @return returns type of item
     */
    public String getType() {
        return type;
    }

    /** Sets the type of an item.
     *  @param newType type of item
     */
    public void setType(String newType) {
        type = newType;
    }

    /** Gets the display character of an item.
     *  @return returns display character of item
     */
    public Character getDisplayCharacter() {
        return displayChar;
    }

    /** Sets the display character of an item.
     *  @param newDisplayCharacter display character of item
     */
    public void setDisplayCharacter(Character newDisplayCharacter) {
        displayChar = newDisplayCharacter;
    }

    /** Gets the description of an item.
     *  @return returns description of item
     */
    public String getDescription() {
        return description;
    }

    /** Sets the description of an item.
     *  @param newDescription description of item
     */
    public void setDescription(String newDescription) {
        description = newDescription;
    }

    /** Gets the location of an item.
     *  @return returns location of item
     */
    public Point getXyLocation() {
        return xyLocation;
    }

    /** Sets the location of an item.
     *  @param newXyLocation location of item
     */
    public void setXyLocation(Point newXyLocation) {
        xyLocation = newXyLocation;
    }

    /** Gets the current roomId of an item.
     *  @return returns current roomId of item
     */
    public int getRoomId() {
        return roomId;
    }

    /** Sets the current roomId of an item.
     *  @param newCurrentRoom current roomId of item
     */
    public void setRoomId(int newCurrentRoom) {
        roomId = newCurrentRoom;
    }

    /** gets the current room of an item.
     *  @return current room of item
     */
    public Room getCurrentRoom() {
        return curRoom;
    }

    /** Sets the current room of an item.
     *  @param newRoom new current room of item
     */
    public void setCurrentRoom(Room newRoom) {
        curRoom = newRoom;
    }

    /** toString to be the name of the item.
     *  @return the name of the item
     */
    @Override
    public String toString() {
        return name;
    }
}
