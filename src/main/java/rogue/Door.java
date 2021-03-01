package rogue;

import java.util.ArrayList;

import java.io.Serializable;

public class Door implements Serializable {

    private int wallPos;
    private ArrayList<Room> rooms;
    public static final long serialVersionUID = 8950494212262494736L;

    /** Default Constructor. */
    public Door() {
        wallPos = 1;
        rooms = new ArrayList<>();
    }

    /** Constructor with given wallposition.
     *  @param wallpos wall offset of the door
    */
    public Door(int wallpos) {
        wallPos = wallpos;
        rooms = new ArrayList<>();
    }

    /** Constructor to create a new door.
     *  @param position offset from beginning of wall
     *  @param room room that the door is in
     *  @param conRoom room the door connects to
    */
    public Door(int position, Room room, Room conRoom) {
        wallPos = position;
        rooms = new ArrayList<>();
        rooms.add(room);
        connectRoom(conRoom);
    }

    /** connects a room to the current room.
     * @param room room to connect
     */
    public void connectRoom(Room room) {
        rooms.add(room);
    }

    /** get the arraylist of the connected rooms.
     * @return returns the arraylist of rooms the door is connected to
     */
    public ArrayList<Room> getConnectedRooms() {
        return rooms;
    }

    /** get the room that the door connects to.
     *  @param curRoom room the door is in
     *  @return returns the room the door connects to
     */
    public Room getOtherRoom(Room curRoom) {
        for (Room r : rooms) {
            if (curRoom != r) {
                return r;
            }
        }
        return null;
    }

    /** gets the wall position of the door.
     * @return returns the offset of the door from the beginning of the wall
     */
    public int getWallPos() {
        return wallPos;
    }

    /** setes the wall position of the door.
     * @param newWallPos sets the offset of the door from the beginning of the wall
     */
    public void setWallPos(int newWallPos) {
        wallPos = newWallPos;
    }
}
