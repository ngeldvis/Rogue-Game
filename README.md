## README For Rogue Game

> 2430 Project at the University of Guelph

#### Student Information:

Name: *Nigel Davis*  
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
