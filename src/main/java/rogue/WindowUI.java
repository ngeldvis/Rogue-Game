package rogue;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
// import javax.xml.crypto.Data;

public class WindowUI extends JFrame {

 // Constants

    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;
    public static final int DEFAULT_TEXTFIELD_SIZE = 30;
    public static final int COLS = 100;
    public static final int ROWS = 24;
    public static final int PADDING = 5;
    public static final Color DARK_PANEL_C = new Color(30, 30, 30);
    private final char startCol = 0;
    private final char roomRow = 0;

 // Swing Components

    private Container contentPane;
    private JLabel messagefield;
    private JLabel nameLabel;
    private SwingTerminal terminal;
    private TerminalScreen screen;
    private JTextArea inventory;
    private JTextArea equippedItems;

 // The Game Objects

    private static Rogue theGame;
    private static WindowUI theGameUI;

 // Constructor Methods

    /** Constructor. */
    public WindowUI() {
        super();
        setWindowDefaults();
        addContents();
        pack();
        start();
        setSize(WIDTH, HEIGHT);
    }

    private void setWindowDefaults() {
        setTitle("Rogue Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
    }

    private void addContents() {
        addMenu();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);

        setMessagePanel();
        setTerminalPanel(mainPanel);
        setInfoPanel(mainPanel);
        setCommandPanel();
    }

 // Setup Swing GUI and Panels

    private void setMessagePanel() {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout());
        messagePanel.setBackground(DARK_PANEL_C);
        messagefield = new JLabel("Welcome to My Rogue Game");
        messagefield.setForeground(Color.WHITE);
        messagePanel.add(messagefield);
        contentPane.add(messagePanel, BorderLayout.NORTH);
    }

    private void setTerminalPanel(JPanel mainPanel) {
        JPanel terminalPanel = new JPanel();
        terminalPanel.setBackground(DARK_PANEL_C);
        terminal = new SwingTerminal();
        terminal.setCursorVisible(false);
        terminalPanel.add(terminal);
        mainPanel.add(terminalPanel, BorderLayout.WEST);
    }

    private void setInfoPanel(JPanel mainPanel) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setBackground(DARK_PANEL_C);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        createNamePanel(infoPanel);
        createItemsPanel(infoPanel);
    }

    private void createNamePanel(JPanel infoPanel) {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.setBackground(DARK_PANEL_C);
        nameLabel = new JLabel("Player's Name");
        nameLabel.setForeground(Color.WHITE);
        namePanel.add(nameLabel);
        infoPanel.add(namePanel, BorderLayout.NORTH);
    }

    private void createItemsPanel(JPanel infoPanel) {
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(2, 1));
        addInventoryPanel(itemsPanel);
        addEquippedPanel(itemsPanel);
        infoPanel.add(itemsPanel);
    }

    private void addInventoryPanel(JPanel itemsPanel) {
        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BorderLayout());
        backPanel.setBackground(DARK_PANEL_C);
        JLabel inventoryLabel = new JLabel("Items in Inventory");
        inventoryLabel.setForeground(Color.WHITE);
        backPanel.add(inventoryLabel, BorderLayout.NORTH);
        inventory = new JTextArea();
        inventory.setEditable(false);
        inventory.setLineWrap(false);
        inventory.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        JScrollPane inventoryPanel = new JScrollPane(inventory,
          JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        backPanel.add(inventoryPanel, BorderLayout.CENTER);
        itemsPanel.add(backPanel);
    }

    private void addEquippedPanel(JPanel itemsPanel) {
        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BorderLayout());
        backPanel.setBackground(DARK_PANEL_C);
        JLabel equippedLabel = new JLabel("Items Equipped");
        equippedLabel.setForeground(Color.WHITE);
        backPanel.add(equippedLabel, BorderLayout.NORTH);
        equippedItems = new JTextArea();
        equippedItems.setEditable(false);
        equippedItems.setLineWrap(false);
        equippedItems.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        JScrollPane equippedPanel = new JScrollPane(equippedItems,
          JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        backPanel.add(equippedPanel, BorderLayout.CENTER);
        itemsPanel.add(backPanel);
    }

    private void setCommandPanel() {
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new FlowLayout());
        commandPanel.setBackground(DARK_PANEL_C);
        JTextField commandfield = addCommandTextField(commandPanel);
        addCommandButton(commandPanel, commandfield);
        contentPane.add(commandPanel, BorderLayout.SOUTH);
    }

    private JTextField addCommandTextField(JPanel commandPanel) {
        JTextField commandfield = new JTextField(DEFAULT_TEXTFIELD_SIZE);
        commandfield.addActionListener((ActionEvent e) -> commandAction(commandfield));
        commandPanel.add(commandfield);
        return commandfield;
    }

    private void addCommandButton(JPanel commandPanel, JTextField commandfield) {
        JButton commandButton = new JButton("Execute");
        commandButton.addActionListener((ActionEvent e) -> commandAction(commandfield));
        commandPanel.add(commandButton);
    }

    private void commandAction(JTextField commandfield) {
        String input = commandfield.getText();
        if (!input.equals("")) {
            handleInput(input.charAt(0));
        }
        commandfield.setText("");
    }

    private void start() {
        try {
            screen = new TerminalScreen(terminal);
         // screen = new VirtualScreen(baseScreen);
            screen.setCursorPosition(TerminalPosition.TOP_LEFT_CORNER);
            screen.startScreen();
            screen.refresh();
            terminal.setForegroundColor(TextColor.ANSI.CYAN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 // Create Menu and Menu Items

    private void addMenu() {
     // new menu bar
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

     // new menu
        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);

     // new menu items
        addMenuItems(fileMenu);
    }

    private void addMenuItems(JMenu fileMenu) {
        addChangeNameItem(fileMenu);
        fileMenu.add(new JSeparator());
        addOpenRogItem(fileMenu);
        addOpenJsonItem(fileMenu);
        fileMenu.add(new JSeparator());
        addSaveItem(fileMenu);
        addSaveQuitItem(fileMenu);
        fileMenu.add(new JSeparator());
        addQuitItem(fileMenu);
    }

    private void addChangeNameItem(JMenu fileMenu) {
        JMenuItem changeNameItem = new JMenuItem("Change Name");
        changeNameItem.addActionListener((ActionEvent e) -> menuChangeNameAction());
        fileMenu.add(changeNameItem);
    }

    private void addOpenRogItem(JMenu fileMenu) {
        JMenuItem openRogItem = new JMenuItem("Open Rogue Save");
        openRogItem.addActionListener((ActionEvent e) -> menuOpenRogAction());
        fileMenu.add(openRogItem);
    }

    private void addOpenJsonItem(JMenu fileMenu) {
        JMenuItem openJsonItem = new JMenuItem("Open JSON File");
        openJsonItem.addActionListener((ActionEvent e) -> menuOpenJsonAction());
        fileMenu.add(openJsonItem);
    }

    private void addSaveItem(JMenu fileMenu) {
        JMenuItem saveItem = new JMenuItem("Save Game");
        saveItem.addActionListener((ActionEvent e) -> menuSaveAction());
        fileMenu.add(saveItem);
    }

    private void addSaveQuitItem(JMenu fileMenu) {
        JMenuItem savequitItem = new JMenuItem("Save and Quit");
        savequitItem.addActionListener((ActionEvent e) -> menuSaveQuitAction());
        fileMenu.add(savequitItem);
    }

    private void addQuitItem(JMenu fileMenu) {
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener((ActionEvent ev) -> menuQuitAction());
        fileMenu.add(quitItem);
    }

 // Menu Actions

    private void menuChangeNameAction() {
        JOptionPane optionPane = new JOptionPane();
        String msg = "Enter new name:";
        String title = "Change Player Name";
        String name = optionPane.showInputDialog(this, msg, title, JOptionPane.QUESTION_MESSAGE);
        if (name != null) {
            theGame.getPlayer().setName(name);
            changeNameText(theGame.getPlayer().getName());
        }
    }

    private void menuOpenRogAction() {
        boolean error = false;
        do {
            try {
                JFileChooser fChooser = new JFileChooser(".");
                fChooser.setDialogTitle("Open Rogue File");
                fChooser.setFileFilter(new FileNameExtensionFilter("rogue (*.rog)", "rog"));
                if (fChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File chosenFile = fChooser.getSelectedFile();
                    error = loadRogGame(chosenFile.getAbsolutePath());
                } else {
                    error = false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "There was something wrong with that file, try a different one.",
                  "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);
    }

    private void menuOpenJsonAction() {
        boolean error = false;
        do {
            try {
                JFileChooser fChooser = new JFileChooser(".");
                fChooser.setDialogTitle("Open JSON File");
                fChooser.setFileFilter(new FileNameExtensionFilter("json (*.json)", "json"));
                if (fChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File chosenFile = fChooser.getSelectedFile();
                    loadJsonGame(chosenFile.getAbsolutePath());
                }
                error = false;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "There was something wrong with that file, try a different one.",
                  "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);
    }

    private int menuSaveAction() {
        String directory = ".";
        JFileChooser fChooser = new JFileChooser(directory);
        fChooser.setDialogTitle("Save As");
        fChooser.setFileFilter(new FileNameExtensionFilter("rogue (*.rog)", "rog"));
        int result = fChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File toSave = fChooser.getSelectedFile();
            save(toSave.getName() + ".rog");
        }
        return result;
    }

    private void menuSaveQuitAction() {
        int result = menuSaveAction();
        if (result == JFileChooser.APPROVE_OPTION) {
            menuQuitAction();
        }
    }

    private void menuQuitAction() {
        System.exit(0);
    }

 // Save and Load file using JFileChooser

     private void save(String filename) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filename);
            ObjectOutputStream output = new ObjectOutputStream(outputStream);
            output.writeObject(theGame);
            output.close();
            outputStream.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /** loads chosen .rog file.
     *  @param filename filename to load save from
     *  @return true if any errors, false if success
     */
    private boolean loadRogGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));) {
            Rogue loadedGame = (Rogue) in.readObject();
            theGame = loadedGame;
            theGameUI.draw("Loaded new game", theGame.getNextDisplay());
            updateInventory();
            changeNameText(theGame.getPlayer().getName());
        } catch (IOException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "There was something wrong with that file, try a different one.",
              "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "There was something wrong with that file, try a different one.",
              "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private void loadJsonGame(String filename) {
        RogueParser newParser = new RogueParser(filename, "symbols-map.json");
        Rogue loadedGame = new Rogue(newParser);
        theGame = loadedGame;
        theGameUI.draw("Loaded new game", theGame.getNextDisplay());
        updateInventory();
        changeNameText(theGame.getPlayer().getName());
    }

 // Item Actions

    private void updateInventory() {
        inventory.setText(theGame.getPlayer().getInventoryAsString());
        equippedItems.setText(theGame.getPlayer().getEquippedAsString());
    }

    private Item selectItem() {
        JComboBox items = new JComboBox(theGame.getPlayer().getInventory().toArray());
        if (theGame.getPlayer().getInventory().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You don't have any items", "Message", JOptionPane.DEFAULT_OPTION);
            return null;
        }
        items.setSelectedIndex(0);
        JOptionPane optionpane = new JOptionPane();
        Object[] options = {"OK", "CANCEL"};
        int result = optionpane.showOptionDialog(this, items, "Choose an Item",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (result == JOptionPane.OK_OPTION) {
            Item item = (Item) items.getSelectedItem();
            return item;
        }
        return null;
    }

    private String eatAction() {
        Item item = selectItem();
        if (item == null) {
            return null;
        }
        if (item instanceof Food) {
            Food edible = (Food) item;
            theGame.getPlayer().removeFromInventory(edible);
            theGameUI.messagefield.setForeground(Color.YELLOW);
            return edible.eat();
        } else if (item instanceof Potion) {
            Potion edible = (Potion) item;
            theGame.getPlayer().removeFromInventory(edible);
            theGameUI.messagefield.setForeground(Color.YELLOW);
            return edible.eat();
        } else {
            JOptionPane.showMessageDialog(this, "You can not eat that");
            return "try picking a different item";
        }
    }

    private String wearAction() {
        Item item = selectItem();
        if (item == null) {
            return null;
        }
        if (item instanceof Clothing) {
            Clothing wearable = (Clothing) item;
            theGame.getPlayer().equip(wearable);
            theGameUI.messagefield.setForeground(Color.YELLOW);
            return wearable.wear();
        } else if (item instanceof Ring) {
            Ring wearable = (Ring) item;
            theGame.getPlayer().equip(wearable);
            theGameUI.messagefield.setForeground(Color.YELLOW);
            return wearable.wear();
        } else {
            JOptionPane.showMessageDialog(this, "You can not wear that");
            return "try picking a different item";
        }
    }

    private String tossAction() {
        Item item = selectItem();
        if (item == null) {
            return null;
        }
        if (item instanceof Potion) {
            Potion tossable = (Potion) item;
            theGame.getPlayer().throwItem(tossable);
            theGameUI.messagefield.setForeground(Color.YELLOW);
            return tossable.toss();
        } else if (item instanceof SmallFood) {
            SmallFood tossable = (SmallFood) item;
            theGame.getPlayer().throwItem(tossable);
            theGameUI.messagefield.setForeground(Color.YELLOW);
            return tossable.toss();
        } else {
            JOptionPane.showMessageDialog(this, "You can not toss that");
            return "try picking a different item";
        }
    }

 // Terminal Methods

    /** Prints a string to the screen starting at the indicated column and row.
     *  @param toDisplay the string to be printed
     *  @param column the column in which to start the display
     *  @param row the row in which to start the display
     */
    public void putString(String toDisplay, int column, int row) {
        Terminal t = screen.getTerminal();
        try {
            t.setCursorPosition(column, row);
            for (char ch: toDisplay.toCharArray()) {
                t.putCharacter(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Redraws the whole screen including the room and the message.
     *  @param message the message to be displayed at the top of the room
     *  @param room the room map to be drawn
     */
    public void draw(String message, String room) {
        try {
            screen.getTerminal().clearScreen();
            setMessage(message);
            // terminal.setForegroundColor(getRandomColor());
            putString(room, startCol, roomRow);
            screen.refresh();
            terminal.setForegroundColor(TextColor.ANSI.CYAN);
        } catch (IOException e) {
        }
    }

    private void setTerminalColor(TextColor color) {
        terminal.setForegroundColor(color);
    }

 // GUI Methods

    /** Changes the message at the top of the screen for the user.
     *  @param msg the message to be displayed
     */
    public void setMessage(String msg) {
        messagefield.setText(msg);
    }

    private void changeNameText(String name) {
        nameLabel.setText("Player Name: " + name);
    }

 // Input Methods

    /** Obtains input from the user and returns it as a char.
     *  Converts arrow keys to the equivalent movement keys in rogue.
     *  @return the ascii value of the key pressed by the user
     */
    public char getInput() {
        KeyStroke keyStroke = null;
        while (keyStroke == null) {
            try {
                keyStroke = screen.pollInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            return Rogue.DOWN;  //constant defined in rogue
        } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            return Rogue.UP;
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return Rogue.LEFT;
        } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            return Rogue.RIGHT;
        }
        return keyStroke.getCharacter();
    }

    private String doAction(char input) {
        if (input == 'e') {
            return eatAction();
        } else if (input == 'w') {
            return wearAction();
        } else if (input == 't') {
            return tossAction();
        }
        return null;
    }

    private static void runGame() {
        char input = 'h';
        while (input != 'q') {
            input = theGameUI.getInput();
            handleInput(input);
        }
    }

    private static void handleInput(char input) {
        String message = null;
        try {
            if (theGameUI.isMove(input)) {
                message = theGame.makeMove(input);
                theGameUI.messagefield.setForeground(Color.WHITE);
            } else if (theGameUI.isAction(input)) {
                message = theGameUI.doAction(input);
                theGame.setNextDisplay(theGame.getPlayer().getCurrentRoom().displayRoom());
            }
            theGameUI.updateInventory();
            if (message != null) {
                theGameUI.draw(message, theGame.getNextDisplay());
            }
        } catch (InvalidMoveException badMove) {
            handleInvalidMove(message);
        }
    }

    private boolean isMove(char c) {
        if (c == theGame.DOWN || c == theGame.UP || c == theGame.LEFT || c == theGame.RIGHT) {
            return true;
        }
        return false;
    }

    private boolean isAction(char c) {
        if (c == 'e' || c == 'w' || c == 't') {
            return true;
        }
        return false;
    }

    private static void handleInvalidMove(String message) {
        message = "you can't walk through walls, try again!";
        theGameUI.setMessage(message);
        theGameUI.setTerminalColor(TextColor.ANSI.RED);
        theGameUI.draw(message, theGame.getNextDisplay());
        theGameUI.messagefield.setForeground(Color.RED);
    }

 // Main Method

    /** The controller method for making the game logic work.
     *  @param args command line parameters
     */
    public static void main(String[] args) {
        String configurationFileLocation = "fileLocations.json";
        RogueParser parser = new RogueParser(configurationFileLocation); // Parse the json files
        theGameUI = new WindowUI(); // allocate memory for the GUI
        theGame = new Rogue(parser); // allocate memory for the game and set it up
        Player thePlayer = new Player("Nigel"); // set up the initial game display
        theGameUI.changeNameText(thePlayer.getName());
        theGame.setPlayer(thePlayer);
        String message = "Welcome to my Rogue game!";
        theGameUI.draw(message, theGame.getNextDisplay());
        theGameUI.setVisible(true);
        runGame();
        System.exit(0);
    }
}
