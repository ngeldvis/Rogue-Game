## README For Rogue Game

> I affirm that all work done on this assignment is completely my own without copying from other people or from external sources without citations

#### Student Information:

Name: *Nigel Davis*  
Email: _ndavis10@uoguelph.ca_   
Student Number: *1105413*  
Course: _**CIS 2430** - Object Oriented Programming_ 

---

#### Running the Program:

to run the program, use the command: `java -jar build/libs/A3.jar` or `gradle run`

---

#### Playing the Game:

When the game opens, your player will start in a room. To move around you can use the arrow keys or the RDFG keys to move around. 

| Key | Direction |
|-----|-----------|
|  r  |   UP      |
|  g  |   RIGHT   |
|  f  |   DOWN    |
|  d  |   LEFT    | 

You can also pick up items in the game. Once you pick up an item, it will be adding to your inventory shown on the right side of the GUI.

Different Items have different capabilities and uses. For food items, if you press the **'e'** key, the game will prompt you to pick an item from your inventory to **eat**. If you press the **'w'** key, the game will prompt you to pick an item to **wear**. If you press the **'t'** key, you will be prompt to choose an item to **toss**. Not all items have all these properties so if you choose an item that doesn't have the property that you tried to use, the game will display a message which you can just dismiss.

---

#### Additional Information:

- I had to change the build.gradle file to run using Java 1.8 since the above command wouldn't work otherwise.

- I assumed that there won't be any rooms in the json file that have a door which specifies a room that does't exist or a room that does exist but doesn't have a door to match the other room.

- The only door error accounted for is if a room doesn't contain any doors. So in the json it would just have `"doors": [],`

---

#### Assignment 3 Assumptions:

- I assumed that when I was loading in a new rooms .json file (as on Teams it was said that it was a rooms .json file not a filelocations .json file), that we would be using the same symbols file ie. *"symbols-map.json"*.

- when an item is one of the subclasses of Item that implements more then one of the required interfaces, if the description doesnt contain the substring `": "`, then it will use the entire description for each interfaced metod.

- when tossing an item, I just placed the item in a random location in the room where the player can go to pick back up.

- When saving a file you can choose the name and the file extension .rog will automatically be added to the end. The extension .rog is what I used to save the rogue files as.

- Wasn't sure if it was required but since it was there in the example the professor gave, I kept the command line at the bottom of the GUI which there as another means of input. The exact same input can be put through the command line.

    - with this all I do is grab the first character in the textfield so I assume that the first character is the character that you wanted when typing the command. If the command is one that hasn't been accounted for I just ignore it.

- I assumed that when loading in a new rooms .json file that the players name would change to that specified in the default constructor for player in my case "John".

- On assignment 2 apparently failed 18 test cases. When I ran the tests given on teams myself, I appear to only fail 9. Because of this I was only able to fix the errors that tests were showing me. I'm assuming that the tests given on teams are the only tests that will be run on this program and so I shouldn't fail others. I had sent an email regarding this issue a few days before this assignment was due with no response so I was unable to get an answer regarding the other 9 tests that I apparently failed.

- Assumed since it never said in the description that an Item.md file was not necessary. The only ones we needed to make were RogueParser.md, Rogue.md, Player.md, Door.md and Inventory.md if we made an inventory class.

- I didn't make an Inventory class as the description for the assignment said that we could make it in whichever way we wanted and using an ArrayList in the Player class seems to be a better approach.