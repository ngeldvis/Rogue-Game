package rogue;

import java.awt.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.Serializable;

public class Room implements Serializable {

    private int id;
    private int width;
    private int height;
    private boolean playerInRoom;
    private Player player;
    private ArrayList<Item> items;
    private HashMap<String, Door> doors;
    private Rogue theGame;
    public static final long serialVersionUID = 1762534533859595533L;

    // CONSTRUCTORS

    /** Default constrcutor. */
    public Room() {
        id = 0;
        final int DEFAULT_SIZE = 10;
        width = DEFAULT_SIZE;
        height = DEFAULT_SIZE;
        playerInRoom = false;
        player = null;
        items = new ArrayList<>();
        doors = new HashMap<>();
        theGame = new Rogue();
    }

    /** Constructor to create room from information from RogueParser.
     *  @param newRoom room map parsed by theGame
     *  @param newItems items from Rogue
     *  @param rogue the Game
     */
    public Room(Map<String, String> newRoom, ArrayList<Item> newItems, Rogue rogue) {
        id = Integer.parseInt(newRoom.get("id"));
        width = Integer.parseInt(newRoom.get("width"));
        height = Integer.parseInt(newRoom.get("height"));
        playerInRoom = Boolean.parseBoolean(newRoom.get("start"));
        if (playerInRoom) {
            player = rogue.getPlayer();
            player.setCurrentRoom(this);
        }
        items = new ArrayList<>();
        doors = new HashMap<>();
        theGame = rogue;
        setItems(newItems);
    }

    /** adds all the items that should be added to the room.
     * @param newItems list of items from Rogue
     */
    public void setItems(ArrayList<Item> newItems) {
        for (Item item : newItems) {
            if (item.getRoomId() == id) { // only add items that are in the specific room
                boolean toBeAdded = true;
                while (toBeAdded) {
                    try {
                        addItem(item);
                        toBeAdded = false;
                    } catch (ImpossiblePositionException e) {
                        setValidItemLocation(item);
                        toBeAdded = true;
                    } catch (NoSuchItemException e) {
                        toBeAdded = false;
                    }
                }
            }
        }
    }

    /** adds an individual item to the room and checks for any errors.
     *  @param toAdd item to be added to the room
     *  @throws ImpossiblePositionException thrown if item is in an invalid position
     *  @throws NoSuchItemException thrown if item does not exist in list of items in room
     */
    public void addItem(Item toAdd) throws ImpossiblePositionException, NoSuchItemException {
        if (!validItemPosition(toAdd)) {
            throw new ImpossiblePositionException();
        } else if (!itemInList(toAdd)) {
            throw new NoSuchItemException();
        } else {
            toAdd.setCurrentRoom(this);
            items.add(toAdd);
        }
    }

    private boolean validItemPosition(Item checkItem) {
        if (itemOutOfBounds(checkItem)) { // includes walls and doors
            return false;
        } else if (itemOnItem(checkItem)) {
            return false;
        } else if (itemOnPlayer(checkItem)) {
            return false;
        }
        return true;
    }

    private boolean itemOutOfBounds(Item checkItem) {
        Point itemXY = checkItem.getXyLocation();
        if (itemXY.getX() <= 0 || itemXY.getX() >= width - 1 || itemXY.getY() <= 0 || itemXY.getY() >= height - 1) {
            return true;
        }
        return false;
    }

    private boolean itemOnItem(Item checkItem) {
        Point itemXY = checkItem.getXyLocation();
        for (Item i : items) {
            if (itemXY.equals(i.getXyLocation()) && checkItem.getId() != i.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean itemOnPlayer(Item checkItem) {
        Point itemXY = checkItem.getXyLocation();
        if (playerInRoom && itemXY.equals(player.getXyLocation())) {
            return true;
        }
        return false;
    }

    /** checks to see if an item's room id matched the id of the room its in.
     * @param checkItem item to check
     * @return returns true if the id's match
     */
    public boolean itemInList(Item checkItem) {
        ArrayList<Item> theGameItems = theGame.getItems();
        for (Item i : theGameItems) {
            if (checkItem.getId() == i.getId()) {
                return true;
            }
        }
        return false;
    }

    private void setValidItemLocation(Item checkItem) {
        boolean validLocation = false;
        while (!validLocation) {
            moveItem(checkItem);
            if (validItemPosition(checkItem)) {
                validLocation = true;
            }
        }
    }

    private void moveItem(Item item) {
        Point pt = item.getXyLocation();
        if (pt.getX() > 0 && pt.getX() < width - 2) {
            item.setXyLocation(new Point((int) pt.getX() + 1, (int) pt.getY()));
        } else if (pt.getX() == width - 2 && pt.getY() > 0 && pt.getY() < height - 2) {
            item.setXyLocation(new Point(1, (int) pt.getY() + 1));
        } else {
            item.setXyLocation(new Point(1, 1));
        }
    }

    /** verifies the information in a room.
     *  @return returns true if the room is valid
     *  @throws NotEnoughDoorsException thrown if number of doors is less then one
     */
    public boolean verifyRoom() throws NotEnoughDoorsException {
        for (Item item : items) {
            if (!validItemPosition(item)) {
                return false;
            }
        }
        if (playerInRoom) {
            if (isOnWall(player.getXyLocation())) {
                return false;
            }
        }
        if (doors.size() < 1) {
            throw new NotEnoughDoorsException();
        }
        return true;
    }

    /** removes an item from the list of items.
     *  @param toRemove item to be removed from list
     */
    public void removeItem(Item toRemove) {
        items.remove(toRemove);
    }

    /** adds a door to the room.
     *  @param direction direction the door is facing
     *  @param newDoor door object to be added
     */
    public void addDoor(String direction, Door newDoor) {
        doors.put(direction, newDoor);
    }

    // GETTERS AND SETTERS

    /** gets widhth of the room.
     *  @return width of the room
     */
    public int getWidth() {
        return width;
    }

    /** set width of room.
     *  @param newWidth width of room
     */
    public void setWidth(int newWidth) {
        width = newWidth;
    }

    /** gets height of the room.
     *  @return height of the room
     */
    public int getHeight() {
        return height;
    }

    /** set height of room.
     *  @param newHeight height of room
     */
    public void setHeight(int newHeight) {
        height = newHeight;
    }

    /** gets id of the room.
     *  @return id of the room
     */
    public int getId() {
        return id;
    }

    /** set id of room.
     *  @param newId id of room
     */
    public void setId(int newId) {
        id = newId;
    }

    /** gets items in the room.
     *  @return list of items in the room
     */
    public ArrayList<Item> getRoomItems() {
        return items;
    }

    /** set items in room.
     *  @param newRoomItems items in new room
     */
    public void setRoomItems(ArrayList<Item> newRoomItems) {
        items = newRoomItems;
    }

    /** gets player in the room.
     *  @return player in the room
     */
    public Player getPlayer() {
        return player;
    }

    /** set player in room.
     *  @param newPlayer player to be set
     */
    public void setPlayer(Player newPlayer) {
        player = newPlayer;
    }

    /** gets the hashmap of doors from room.
     *  @return returns the HashMap of doors
     */
    public HashMap<String, Door> getDoors() {
        return doors;
    }

    /** gets door facing a specific direction in room.
     *  @param direction direction of the door wanted
     *  @return specified door
     */
    public Door getDoor(String direction) {
        return doors.get(direction);
    }

    /** set doors of room.
     *  @param direction direction of room the door is on
     *  @param wallpos position offset of the door
     */
    public void setDoor(String direction, int wallpos) {
        Door newDoor = new Door(wallpos);
        doors.put(direction, newDoor);
    }

    // HELPER FUNCTIONS

    /** is player in room.
     *  @return true if player is in room, false if not
     */
    public boolean isPlayerInRoom() {
        return playerInRoom;
    }

    /** sets whether or not the player is in the room.
     *  @param inRoom is player in room now?
     */
    public void setPlayerInRoom(boolean inRoom) {
        playerInRoom = inRoom;
    }

    /** Produces a string that can be printed to produce an ascii rendering of the room and all of its contents.
     *  @return returns string representation of how the room looks
     */
    public String displayRoom() {
        String strRoom = "";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                strRoom += getNextCharacter(new Point(j, i));
            }
            strRoom = strRoom + "\n";
        }
        return strRoom;
    }

    private Character getNextCharacter(Point xy) {
        if (playerInRoom && player.getXyLocation().equals(xy)) {
            return theGame.getSymbol("PLAYER");
        } else if (isOnWall(xy)) { // point (x,y) is on the edge of the room
            if (isOnDoor(xy)) {
                return theGame.getSymbol("DOOR");
            } else if (xy.getY() == 0 || xy.getY() == height - 1) {
                return theGame.getSymbol("NS_WALL");
            } else {
                return theGame.getSymbol("EW_WALL");
            }
        } else { // point (x,y) is somewhere in the middle of the room
            for (Item item : items) {
                if (item.getXyLocation().equals(xy)) {
                    return item.getDisplayCharacter();
                }
            }
            return theGame.getSymbol("FLOOR");
        }
    }

    /** looks for a door at a given point location in a room.
     *  @param xy point to look for doors at
     *  @return true if a door is found, false if door is not found
     */
    public boolean isOnDoor(Point xy) {
        for (String key : doors.keySet()) {
            if (key.equals("N")
              && xy.getY() == 0 && doors.get("N").getWallPos() == xy.getX()) {
                return true;
            } else if (key.equals("S")
              && xy.getY() == height - 1 && doors.get("S").getWallPos() == xy.getX()) {
                return true;
            } else if (key.equals("W")
              && xy.getX() == 0 && doors.get("W").getWallPos() == xy.getY()) {
                return true;
            } else if (key.equals("E")
              && xy.getX() == width - 1 && doors.get("E").getWallPos() == xy.getY()) {
                return true;
            }
        }
        return false;
    }

    /** checks to see if a point (x,y) is on the edge of a room.
     *  @param xy Point to check
     *  @return returns true if point is on a wall location
     */
    public boolean isOnWall(Point xy) {
        if (xy.getY() == 0 || xy.getY() == height - 1 || xy.getX() == 0 || xy.getX() == width - 1) {
            return true;
        }
        return false;
    }
}
