package rogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java.awt.Point;

import java.io.Serializable;

public class Rogue implements Serializable {

    public static final char UP = 'r';
    public static final char DOWN = 'f';
    public static final char LEFT = 'd';
    public static final char RIGHT = 'g';
    public static final long serialVersionUID = 4324616277717421489L;

    private Player player;
    private Boolean madeRooms = false;
    private ArrayList<Room> rooms;
    private ArrayList<Item> items;
    private HashMap<String, Character> symbols;
    private ArrayList<Map<String, String>> doors;
    private transient RogueParser rogueParser;
    private String nextDisplay;

    // CONSTRUCTORS

    /** Default Constructor. */
    public Rogue() {
        player = new Player();
        rooms = new ArrayList<>();
        items = new ArrayList<>();
        symbols = new HashMap<>();
        doors = new ArrayList<>();
        rogueParser = new RogueParser();
        nextDisplay = "";
    }

    /** Constructor to create a new Rogue Object.
     *  @param rp rogueparser
     */
    public Rogue(RogueParser rp) {
        player = new Player();
        rooms = new ArrayList<>();
        items = new ArrayList<>();
        symbols = new HashMap<>();
        doors = new ArrayList<>();
        rogueParser = rp;
        createRooms();
        boolean startingRoomFound = false;
        for (Room room : rooms) {
            if (room.isPlayerInRoom()) {
                nextDisplay = room.displayRoom();
                startingRoomFound = true;
                break;
            }
        }
        if (!startingRoomFound) {
            nextDisplay = "";
        }
    }

    /** Constructor to create a new Rogue Object with player.
     * @param rp rogueparser
     * @param newPlayer Player to be added to Rogue
     */
    public Rogue(RogueParser rp, Player newPlayer) {
        player = newPlayer;
        rooms = new ArrayList<>();
        items = new ArrayList<>();
        symbols = new HashMap<>();
        doors = new ArrayList<>();
        rogueParser = rp;
        createRooms();
        boolean startingRoomFound = false;
        for (Room room : rooms) {
            if (room.isPlayerInRoom() && !startingRoomFound) {
                player.setCurrentRoom(room);
                nextDisplay = room.displayRoom();
                startingRoomFound = true;
            }
        }
        if (!startingRoomFound) {
            nextDisplay = "";
        }
    }

    // HELPER METHODS

    /** creates the rooms in the rogue object. */
    public void createRooms() {
        setSymbols();
        setItems();
        setRooms();
        setDoors();
        for (Room r1 : rooms) {
            try {
                r1.verifyRoom();
            } catch (NotEnoughDoorsException e) {
                // System.out.println("Throws NotEnoughDoorsException");
                fixDoors(r1);
            }
        }
        madeRooms = true;
    }

    /** sets all of the symbols required to print out the room. */
    public void setSymbols() {
        symbols = rogueParser.getSymbols();
    }

    /** sets all of the items    in the rogue. */
    public void setItems() {
        Map item = rogueParser.nextItem();
        while (item != null) {
            addItem(item);
            item = rogueParser.nextItem();
        }
    }

    /** adds an individual item to Rogue.
     * @param newItem item to be added to Rogue
     */
    public void addItem(Map<String, String> newItem) {
        Item i = makeItem(newItem);
        items.add(i);
        if (madeRooms) {
            for (Room r : rooms) {
                if (r.getId() == i.getRoomId()) {
                    ArrayList<Item> newItemArray = new ArrayList<>();
                    newItemArray.add(i);
                    r.setItems(newItemArray);
                }
            }
        }
    }

    private Item makeItem(Map<String, String> newItem) {
        String type = newItem.get("type").toLowerCase();
        Item i;
        if (type.equals("food")) {
            i = new Food(newItem, this);
        } else if (type.equals("smallfood")) {
            i = new SmallFood(newItem, this);
        } else if (type.equals("clothing")) {
            i = new Clothing(newItem, this);
        } else if (type.equals("magic")) {
            i = new Magic(newItem, this);
        } else if (type.equals("potion")) {
            i = new Potion(newItem, this);
        } else if (type.equals("ring")) {
            i = new Ring(newItem, this);
        } else {
            i = new Item(newItem, this);
        }
        return i;
    }

    /** sets the rooms in Rogue. */
    public void setRooms() {
        Map<String, String> room = rogueParser.nextRoom();
        while (room != null) {
            addRoom(room);
            // Adding information about doors into an arraylist
            String[] directions = {"E", "N", "S", "W"};
            for (String dir : directions) {
                HashMap<String, String> doorInfo = new HashMap<>();
                if (room.get(dir) != "-1") {
                    doorInfo.put("dir", dir);
                    doorInfo.put("wall_pos", room.get(dir));
                    doorInfo.put("room", room.get("id"));
                    doorInfo.put("con_room", room.get(dir + "_Connection"));
                    doors.add(doorInfo);
                }
            }
            room = rogueParser.nextRoom();
        }
    }

    /** adds an individual room to Rogue.
     * @param newRoom room to be added to Rogue
     */
    public void addRoom(Map<String, String> newRoom) {
        rooms.add(new Room(newRoom, items, this));
    }

    private void setDoors() {
        for (Map<String, String> door : doors) {
            Room doorRoom = null;
            Room conRoom = null;
            for (Room room : rooms) {
                if (room.getId() == Integer.parseInt(door.get("room"))) {
                    doorRoom = room;
                } else if (room.getId() == Integer.parseInt(door.get("con_room"))) {
                    conRoom = room;
                }
            }
            doorRoom.addDoor(door.get("dir"), new Door(Integer.parseInt(door.get("wall_pos")), doorRoom, conRoom));
        }
    }

    private void fixDoors(Room r) {
        for (Room r2 : rooms) {
            if (r2.getId() != r.getId()) {
                if (!r2.getDoors().containsKey("W")) {
                    putDoorInRooms(r, r2, "W");
                    return;
                } else if (!r2.getDoors().containsKey("E")) {
                    putDoorInRooms(r, r2, "E");
                    return;
                } else if (!r2.getDoors().containsKey("N")) {
                    putDoorInRooms(r, r2, "N");
                    return;
                } else if (!r2.getDoors().containsKey("S")) {
                    putDoorInRooms(r, r2, "S");
                    return;
                }
            }
        }
        cantUseFileError();
    }

    private void cantUseFileError() {
        System.out.println("Sorry, dungeon file given can not be used");
        System.exit(1);
    }

    private void putDoorInRooms(Room r1, Room r2, String dir) {

        HashMap<String, String> opposite = new HashMap<>();
        opposite.put("W", "E");
        opposite.put("E", "W");
        opposite.put("S", "N");
        opposite.put("N", "S");

        Random rand = new Random();

        r2.addDoor(dir, new Door(rand.nextInt(r2.getHeight() - 2) + 1, r2, r1));
        r1.addDoor(opposite.get(dir), new Door(rand.nextInt(r1.getHeight() - 2) + 1, r1, r2));
    }

    // ADDITIONAL METHODS

    /** print out all of the rooms.
     *  @return string of all the rooms to be printed
     */
    public String displayAll() {
        String strRooms = "";
        for (Room room : rooms) {
            strRooms += "<---- [Room " + room.getId() + "] ---->\n";
            if (room.isPlayerInRoom()) {
                strRooms += "- Starting Room\n";
            }
            strRooms += room.displayRoom();
            strRooms += "\n\n";
        }
        return strRooms;
    }

    /** this method assesses a move to ensure it is valid.
     *  If the move is valid, then the display resulting from the move is calculated and set
     *  as the 'nextDisplay' (probably a private member variable) If the move is not valid, an
     *  InvalidMoveException is thrown and the nextDisplay is unchanged
     *  @param input character pressed by user
     *  @throws InvalidMoveException thrown if move is not valid
     *  @return returns the nextDisplay
     */
    public String makeMove(char input) throws InvalidMoveException {
        String message;
        Point newPlayerPosition = moveLocation(input);
        Room curRoom = player.getCurrentRoom();
        if (curRoom.isOnWall(newPlayerPosition)) { // player is on the border of the room
            message = handlePlayerOnWall(curRoom, newPlayerPosition, input);
        } else { // new position is somewhere in the middle of the room
            int itemId = playerPickedUpItem(newPlayerPosition, curRoom);
            if (itemId != -1) {
                message = "You found " + curRoom.getRoomItems().get(itemId).getName() + "!";
                curRoom.removeItem(curRoom.getRoomItems().get(itemId));
            } else {
                message = getDirectionMessage(input);
            }
            player.setXyLocation(newPlayerPosition);
            curRoom.setPlayer(player);
            setNextDisplay(curRoom.displayRoom());
        }
        return message;
    }

    private String getDirectionMessage(char c) {
        if (c == DOWN) {
            return "moved South";
        } else if (c == UP) {
            return "moved North";
        } else if (c == LEFT) {
            return "moved West";
        } else if (c == RIGHT) {
            return "moved East";
        }
        return null;
    }

    private Point moveLocation(char input) {
        int x = (int) player.getXyLocation().getX();
        int y = (int) player.getXyLocation().getY();
        if (input == DOWN) {
            y++;
        } else if (input == UP) {
            y--;
        } else if (input == LEFT) {
            x--;
        } else if (input == RIGHT) {
            x++;
        }
        return new Point(x, y);
    }

    private String handlePlayerOnWall(Room cRoom, Point pos, char i) throws InvalidMoveException {
        if (cRoom.isOnDoor(pos)) { // player is in a doorway
            Room newRoom = putPlayerInNewRoom(cRoom, i);
            setNextDisplay(newRoom.displayRoom());
            return "You entered room " + newRoom.getId();
        } else {
            throw new InvalidMoveException();
        }
    }

    private Room putPlayerInNewRoom(Room curRoom, char input) {
        Room newRoom = null;
        if (input == DOWN) {
            newRoom = switchRooms("S", curRoom);
            player.setXyLocation(new Point(newRoom.getDoor("N").getWallPos(), 1));
        } else if (input == UP) {
            newRoom = switchRooms("N", curRoom);
            player.setXyLocation(new Point(newRoom.getDoor("S").getWallPos(), newRoom.getHeight() - 2));
        } else if (input == LEFT) {
            newRoom = switchRooms("W", curRoom);
            player.setXyLocation(new Point(newRoom.getWidth() - 2, newRoom.getDoor("E").getWallPos()));
        } else if (input == RIGHT) {
            newRoom = switchRooms("E", curRoom);
            player.setXyLocation(new Point(1, newRoom.getDoor("W").getWallPos()));
        }
        return newRoom;
    }

    private Room switchRooms(String direction, Room curRoom) {

        HashMap<String, String> opposite = new HashMap<>();
        opposite.put("W", "E");
        opposite.put("E", "W");
        opposite.put("S", "N");
        opposite.put("N", "S");

        Room newRoom = curRoom.getDoor(direction).getOtherRoom(curRoom);
        curRoom.setPlayerInRoom(false);
        newRoom.setPlayerInRoom(true);
        player.setCurrentRoom(newRoom);
        newRoom.setPlayer(player);
        return newRoom;
    }

    private int playerPickedUpItem(Point playerPosition, Room curRoom) {
        int itemId = -1;
        for (Item i : curRoom.getRoomItems()) {
            if (playerPosition.equals(i.getXyLocation())) {
                player.pickUp(i);
                itemId = curRoom.getRoomItems().indexOf(i);
            }
        }
        return itemId;
    }

    // GETTERS AND SETTERS

    /** gets all of the items in Rogue.
     *  @return List of items
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /** gets the symbols from rogue.
     *  @return hashmap of all of the symbols
     */
    public HashMap<String, Character> getSymbols() {
        return symbols;
    }

    /** gets a symbol from the list of symbols.
     *  @param symbolName name of symobl
     *  @return returns the symbol character
     */
    public Character getSymbol(String symbolName) {
        if (symbols.containsKey(symbolName)) {
            return symbols.get(symbolName);
        }
        // Does not contain the key
        return null;
    }

    /** gets the items from rogue.
     *  @return list of items
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /** sets player.
     *  @param thePlayer player object
     */
    public void setPlayer(Player thePlayer) {
        player = thePlayer;
        for (Room room : rooms) {
            if (room.isPlayerInRoom()) {
                player.setCurrentRoom(room);
            }
        }
    }

    /** gets the player from rogue.
     *  @return player object
     */
    public Player getPlayer() {
        return player;
    }

    /** changes the message in nextDisplay.
     *  @param newNextDisplay message to replace
     */
    public void setNextDisplay(String newNextDisplay) {
        nextDisplay = newNextDisplay;
    }

    /** gets the nextDisplay message.
     *  @return returns display message
     */
    public String getNextDisplay() {
        return nextDisplay;
    }
}
