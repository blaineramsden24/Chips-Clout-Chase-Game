import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

import java.io.*;

import java.util.*;

/**
 * The main runtime class for the gameplay.
 *
 * @author Jozef Bohosian, Victor Cai
 * @version 3.0
 */
public class Engine {

    /*  GAME CONSTANTS:-
     ***************************************************************************************************************/

    /**
     * The width of each each in pixels.
     */
    private static int CELL_WIDTH = 150; // TODO: make this 'final' once we are finished debugging

    /**
     * The number of cells to be shown above around the player.
     */
    private static final int MAP_RADIUS = 2; // TODO: decide on our final value for this

    /**
     * The width of the canvas.
     */
    private static final int CANVAS_WIDTH = CELL_WIDTH * (MAP_RADIUS * 2 + 1);

    /**
     * The size of in-game text.
     */
    private static final int TEXT_SIZE = 22 * MAP_RADIUS;

    /**
     * The opacity value used when dimming the screen to show a message to the player.
     */
    private static final double PLAYER_NOTIFY_DIM_OPACITY = 0.15;

    /*
     ***************************************************************************************************************/



    /*  DEBUGGING OPTIONS:-
     ***************************************************************************************************************/

    /**
     * True: the player will walk through walls | False: the player will not move through boundaries.
     */
    private static final boolean DONT_RESPECT_BOUNDARIES = false;

    /**
     * True: display the whole map | False: display the section according to MAP_RADIUS.
     */
    private static final boolean SHOW_FULL_MAP = true;

    /**
     * True: the player is invincible to obstacles and enemies | False: the player can be killed as normal.
     */
    private static final boolean GOD_MODE = false;

    /*
     ***************************************************************************************************************/


    /**
     * All of the KeyCodes that correspond to moving the player
     */
    private static final KeyCode[] dirKeys = {KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT,
            KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D};

    /**
     * The directional key currently being pressed
     */
    private static KeyCode dirPressed;

    /**
     * All of the {@link Level}s.
     */
    private static LinkedList<Level> levels = new LinkedList<>();

    /**
     * The current {@link Player}'s Profile.
     */
    private static Profile playerProfile;

    /**
     * The current {@link Level} being played.
     */
    private static Level currentLevel;

    /**
     * The current {@link Player} object.
     */
    private static Player player;

    /**
     * The time the timer was started, used to calculate how long the {@link Level} took to complete.
     */
    private static long startTime;

    /**
     * The main menu's stage. TODO: implement this
     */
    private static Stage primaryStage;

    /**
     * The stage the {@link Level} is being played on.
     */
    private static Stage levelStage;

    /**
     * The canvas the {@link Level} is being played on.
     */
    private static Canvas canvas;

    /**
     * The file path to the current {@link Level} being played.
     */
    private static String currentLevelPath;


    public static void runLevel(Stage primaryStage, String levelPath, Profile playerProfile) {
        // temporary for testing
        Engine.primaryStage = primaryStage;
        setProfile(playerProfile);
        currentLevelPath = levelPath;

        initialise();
    }

    /**
     * Gets the current {@link Level}.
     *
     * @return The current {@link Level} being played.
     */
    public static Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Initialises everything ready to play a {@link Level}.
     */
    private static void initialise() {

        currentLevel = FileReader.readLevel(currentLevelPath, playerProfile.getVariant());
        player = currentLevel.getPlayer();

        // if a levelStage hasn't been created then create and set one up
        if(levelStage == null) {
            levelStage = new Stage();
            levelStage.initOwner(primaryStage);
            levelStage.initModality(Modality.WINDOW_MODAL);
            levelStage.setOnShown((e) -> onLevelShown());

            // multiply 50 * ((2 * MAP_RADIUS) + 1) for perfect fit if not showing full map
            // set the size of the canvas appropriately using the CANVAS_WIDTH or a default large value if the SHOW_FULL_MAP debug option is enabled
            int sceneLengthPx;
            Scene gameScene;
            Group controls = new Group();

            if (SHOW_FULL_MAP) {
                CELL_WIDTH = 50;
                sceneLengthPx = 900;
                canvas = new Canvas(CELL_WIDTH * currentLevel.getMapLimits().getX(),CELL_WIDTH * currentLevel.getMapLimits().getY());
                controls.getChildren().add(canvas);
                gameScene = new Scene(controls, sceneLengthPx , sceneLengthPx + 50);
                levelStage.setMaximized(true);
            } else {
                sceneLengthPx = CELL_WIDTH * ((2 * MAP_RADIUS) + 1);
                canvas = new Canvas(CANVAS_WIDTH, CANVAS_WIDTH + 50);
                controls.getChildren().add(canvas);
                gameScene = new Scene(controls, sceneLengthPx , sceneLengthPx + 50);
            }

            if(SHOW_FULL_MAP) {
                System.out.println("DEBUG OPTION ENABLED: SHOWING FULL MAP");
            }
            if(GOD_MODE) {
                System.out.println("DEBUG OPTION ENABLED: GODMODE");
            }
            if(DONT_RESPECT_BOUNDARIES) {
                System.out.println("DEBUG OPTION ENABLED: PLAYER NOT RESPECTING BOUNDARIES");
            }

            gameScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> handleKeyPressEvent(event));
            gameScene.addEventFilter(KeyEvent.KEY_RELEASED, event -> handleKeyUpEvent(event));
            levelStage.setScene(gameScene);
        }
        //nextLevel();
        //getLevel();
        //currentLevel = new Level(); // temp

        levelStage.show();
        drawMap();
        startFreshTimer();
    }


    /**
     * @param level The {@link Level} to be played.
     */
    public static void setLevel(Level level) { // TODO: implement this

    }

    /**
     * Resets everything ready to play a different {@link Level}.
     */
    private static void reset() {
        //levelStage.close();
        //levelStage = null;

        // set appropriate variables back to their default values ready to start a level from the beginning
        startTime = 0;
        currentLevel = null;
        player = null;

        initialise();
    }

    /**
     * Switches to the next {@link Level}.
     */
    private static void nextLevel() {
        // retrieve the next level from the linked list and set the current level and player appropriately
        Level nextLevel = levels.pollFirst();
        if (nextLevel != null) {
            currentLevel = nextLevel;
            player = currentLevel.getPlayer();
        }
    }

    /**
     * Handles KeyPress events.
     *
     * @param event The {@link KeyEvent to handle}
     */
    private static void handleKeyPressEvent(KeyEvent event) {
        // for easier access and readability
        KeyCode code = event.getCode();

        // if a directional key has been performed but is still being held down then do nothing
        if(code == dirPressed) {
            return;
        }
        // otherwise if the key being pressed is a direction key set dirPressed to that key
        else if(isDirectionKeyPressed(code)) {
            dirPressed = code;
        }

        // handle the input treating WASD as direction keys also and also handle valid no-directional presses
        switch (event.getCode()) {
            case A:
            case LEFT:
                handlePlayerInput(Direction.LEFT);
                break;
            case D:
            case RIGHT:
                handlePlayerInput(Direction.RIGHT);
                break;
            case W:
            case UP:
                handlePlayerInput(Direction.UP);
                break;
            case S:
            case DOWN:
                handlePlayerInput(Direction.DOWN);
                break;
            case Q:
                saveAndQuit();
                break;
            case ESCAPE:
                Platform.exit();
                break;
            case R: // restart the level at any time (for debugging)
                reset();
                break;
            case SPACE: // restart the level if the player is dead
                if(player.isDead()) {
                    reset();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Handles KeyUp events.
     *
     * @param event The {@link KeyEvent} to handle.
     */
    private static void handleKeyUpEvent(KeyEvent event) {
        // if the pressed direction key was released then set dirPressed to null
        if(dirPressed == event.getCode()) {
            dirPressed = null;
        }
    }

    /**
     * Checks whether a directional key is currently being pressed.
     *
     * @param code The KeyCode to check.
     * @return whether a directional key is currently being pressed.
     */
    private static boolean isDirectionKeyPressed(KeyCode code) {
        for(KeyCode kc : dirKeys) {
            if(kc == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * The logic of the game - if a move is performed, update each {@link Enemy} and check safety etc.
     * @param dir The direction the {@link #player} is trying to move to.
     */
    private static void handlePlayerInput(Direction dir) {
        // if the attempted move can't be performed then return as nothing has changed
        if (!attemptMove(dir)) {
            return;
        }

        // the attempted move was performed successfully

        // trigger the moves of all enemies now that the player is in a new location
        currentLevel.triggerEnemyMoves();

        Cell currentCell = currentLevel.getCurrentCell();

        // if the player is on a CollectableCell then collect its item
        if (currentCell instanceof CollectableCell) {
            player.pickUpItem(((CollectableCell) currentCell).collectItem());
        }
        // otherwise if the player is on a TeleporterCell then teleport the player to its destination
        else if (currentCell instanceof TeleporterCell) {
            TeleporterCell origin = (TeleporterCell) currentCell;
            TeleporterCell destination = origin.getDestination();
            Cell playerLands = currentLevel.getRelativeCell(destination, dir);

            currentLevel.jumpPlayerTo(playerLands);
        }

        // re-draw the map at this point
        drawMap();

        // check if the player should be killed where they currently are and if so handle it with an appropriate message
        if (currentLevel.isPlayerDead() && !GOD_MODE) {
            killPlayer();
        }

        // check if the player has reached the Goal cell and if so trigger level completion
        if (currentLevel.isPlayerAtGoal()) {
            endLevel();
        }
    }

    private static void saveAndQuit() {
        addTimerValueToLevelElapsed();
        FileWriter.saveLevel(currentLevel, playerProfile);
        levelStage.close();
        primaryStage.show();
    }

    /**
     * Triggers the {@link #player}'s death and shows the death screen.
     */
    private static void killPlayer() {
        Cell currentCell = currentLevel.getCurrentCell();
        player.setDead(true);
        String deathMessage = "You died ";
        String cellName = currentCell.getClass().getTypeName();
        if(currentCell.isDeadly()) {
            if(cellName.equals("Fire")) {
                deathMessage = "You BURNED in " + cellName.toUpperCase() + "!";
            } else if (cellName.equals("Water")) {
                deathMessage = "You DROWNED in " + cellName.toUpperCase() + "!";
            } else {
                deathMessage += "to " + cellName + "!";
            }
        } else if (currentCell.isEnemyOnCell()) {
            String enemyName = currentCell.getEnemy().getSimpleName().toUpperCase();
            deathMessage = "You were killed by a " + enemyName + "!";
        }

        // darken the game
        dimBackground(0.35);

        // print the death message
        showMessage(deathMessage, Color.ORANGE);
        showSubMessage("Press Space to try again.", Color.MAGENTA);
        showMotd();
    }

    /**
     * Attempts to perform a move and returns whether it was successful or not.
     * @param dir The {@link Direction} to try to move in.
     * @return Whether the move was performed successfully.
     */
    private static boolean attemptMove(Direction dir) {
        // check whether the move is possible. if not then return without doing anything
        if ((!DONT_RESPECT_BOUNDARIES && !Engine.getCurrentLevel().checkMove(player, dir)) || currentLevel.isFinished() || player.isDead()) { // TODO: Add player death here also when it would be less inconvenient for testing
            return false;
        }

        // get a copy of the map and player position for easier readability
        Cell[][] map = currentLevel.getMap();
        Point2D currentPos = player.getLocation();
        int currentX = (int) currentPos.getX();
        int currentY = (int) currentPos.getY();

        // the cell the player is attempting to move onto
        Cell desiredCell = currentLevel.getCellRelativeToCharacter(player, dir);
        // whether the player can pass onto the cell they want to move to
        boolean canPass = true;

        // check if the player is trying to pas through a door and act accordingly depending on if they have the required items
        if (desiredCell instanceof TokenDoor) {
            TokenDoor door = (TokenDoor) desiredCell;
            int tokensRequired = door.getNumTokens();
            boolean hasEnoughTokens = player.getInventory().hasTokens(tokensRequired);

            // unlock the door if the player has enough tokens, otherwise notify the player
            if (hasEnoughTokens) {
                player.useTokens(tokensRequired);
                door.unlock();
            } else {
                int currentTokens = player.getInventory().getTokenCount();
                dimBackground(PLAYER_NOTIFY_DIM_OPACITY);
                showMessage("You don't have enough tokens!", Color.YELLOW);
                showSubMessage("You need to find " + (tokensRequired - currentTokens) + " more.", Color.GOLD);
            }
            canPass = door.isUnlocked();

        } else if (desiredCell instanceof ColourDoor) {
            ColourDoor door = (ColourDoor) desiredCell;
            KeyType requiredKey = door.getColour();
            boolean hasCorrectKey = player.getInventory().hasKey(requiredKey);

            // unlock the door if the player has the correct key, otherwise notify the player
            if (hasCorrectKey) {
                player.useKey(requiredKey);
                door.unlock();
            } else {
                dimBackground(PLAYER_NOTIFY_DIM_OPACITY);
                showMessage("You don't have the right key!", Color.ORANGE);
                showSubMessage("Go and find a " + requiredKey.name() + " key.", requiredKey.getColour());
            }
            canPass = door.isUnlocked();
        }

        // if the player can't pass return false
        if (!canPass && !DONT_RESPECT_BOUNDARIES) {
            return false;
        }

        // the player can pass onto the cell

        // set the direction the player is currently moving.
        player.setMovingDirection(dir);

        // leave the current cell
        currentLevel.getCurrentCell().playerLeave();


        // enter the new cell according to the direction passed
        switch (dir) {
            case LEFT:
                map[currentY][currentX - 1].playerEnter();
                player.setLocation(currentX - 1, currentY);
                break;
            case RIGHT:
                map[currentY][currentX + 1].playerEnter();
                player.setLocation(currentX + 1, currentY);
                break;
            case UP:
                map[currentY - 1][currentX].playerEnter();
                player.setLocation(currentX, currentY - 1);
                break;
            case DOWN:
                map[currentY + 1][currentX].playerEnter();
                player.setLocation(currentX, currentY + 1);
                break;
            default:
                throw new IllegalArgumentException("An invalid direction was passed.\n" + dir.toString());
        }

        return true; // the move was performed
    }

    /**
     * Displays the map when the {@link Level} is first loaded.
     */
    private static void onLevelShown() {
        // draw the initial map
        drawMap();
    }

    /**
     * Sets the {@link #startTime} to the current time ready to be used to calculate the time taken to complete the {@link #currentLevel}.
     */
    private static void startFreshTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Calculates the time since the timer was started in seconds and adds it to the {@link #currentLevel}'s elapsed time.
     */
    private static void addTimerValueToLevelElapsed() {
        // calculate the time taken in seconds since the timer was started and set the level's elapsed time to it
        double totalTime = (System.currentTimeMillis() - startTime) / 1e3;
        currentLevel.addToElapsedTime(totalTime);
    }

    /**
     * Starts a {@link Level}.
     *
     * @param level The {@link Level} to start running.
     */
    private static void startLevel(Level level) {

    }

    /**
     * Ends a {@link Level} when the {@link Goal} is reached.
     */
    private static void endLevel() {
        new AnimationTimer() { // TODO: tweak this
            long lastUpdate = 0;
            int i = 0;
            double opacity = 0.2;
            GraphicsContext gc = canvas.getGraphicsContext2D();

            @Override
            public void handle(long l) {
                if(lastUpdate == 0) {
                    gc.setFill(Color.rgb(255,255,255, opacity));
                    //gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    lastUpdate = l;
                    return;
                }
                if (((l - lastUpdate) / 1e6) < 270) {
                    return;
                }
                if (++i > 4) {
                    showMessage("Level complete!", Color.GREEN);
                    showSubMessage("Press N to continue or R to replay...", Color.MAGENTA);
                    stop();
                } else {
                    System.out.println(((l - lastUpdate) / 1e6));
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    switch(i) {
                        case 1:
                            gc.setFill(Color.rgb(0,0,255, opacity));
                            break;
                        case 2:
                            gc.setFill(Color.rgb(255,255,0, opacity));
                            gc.setFill(Color.rgb(255,0,0, opacity));
                            break;
                        case 3:
                            gc.setFill(Color.rgb(0,255,255, opacity));
                            gc.setFill(Color.rgb(0,255,0, opacity));
                            break;
                        case 4:
                            gc.setFill(Color.rgb(0,100,130, opacity));
                    }
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    lastUpdate = l;
                }
            }
        }.start();

        currentLevel.setFinished(true);
        addTimerValueToLevelElapsed();
    }

    /**
     * Gets the width of the map's area on the canvas in pixels.
     * @return The width of the map's area on the canvas in pixels.
     */
    private static double getMapAreaWidthPx() {
        return canvas.getWidth();
    }

    /**
     * Gets the height of the map's area on the canvas in pixels.
     * @return The height of the map's area on the canvas in pixels.
     */
    private static double getMapAreaHeightPx() {
        return canvas.getHeight() - 50;
    }

    /**
     * Draws the {@link Level}'s map in its current state to the canvas.
     */
    private static void drawMap() {
        // store the graphics context of the game for easier readability
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // clear the canvas each time we draw the map
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // the coordinates of the next tile to be drawn
        int currentX = 0;
        int currentY = 0;

        // Get the area of the map to be displayed
        Cell[][] section;
        if (SHOW_FULL_MAP) { // show the full map for debugging purposes
            section = currentLevel.getMap();
        } else { // show the section of the map according to the MAP_RADIUS
            // get the section of the map to be drawn
            Point2D[] frameCoords = getFramePosition();
            section = currentLevel.getMapSection(frameCoords[0], frameCoords[1]);
        }

        Point2D playerLoc = player.getLocation();

        // traverse each cell in each row, fetching the appropriate image for each type and drawing it in the correct position
        for (Cell[] row : section) {
            for (Cell c : row) {

                // draw the cell's image first so players / enemies are drawn on top
                gc.drawImage(c.getImage(), currentX * CELL_WIDTH, currentY * CELL_WIDTH, CELL_WIDTH, CELL_WIDTH); // draw the cell

                // draw the Player on the cell if appropriate
                if (c.isPlayerOnCell()) {
                    gc.drawImage(currentLevel.getPlayer().getImage(), currentX * CELL_WIDTH, currentY * CELL_WIDTH,
                            CELL_WIDTH, CELL_WIDTH);
                }

                // draw an Enemy on the cell if appropriate
                if (c.isEnemyOnCell()) {
                    gc.drawImage(c.getEnemy().getImage(), currentX * CELL_WIDTH, currentY * CELL_WIDTH, CELL_WIDTH, CELL_WIDTH);
                }
                currentX++;
            }
            currentY++;
            currentX = 0;
        }

        showInventory();

    }

    /**
     * Gets the position the frame should be depending on the {@link Player}'s location.
     * @return position the frame should be depending on the {@link Player}'s location given as the top left {@link Cell}'s coordinate and bottom right {@link Cell}'s coordinate.
     */
    private static Point2D[] getFramePosition() {
        // store the player's location for easier access / readability
        Point2D playerLoc = player.getLocation();
        // calculate the coordinates of the top left cell to be displayed, based on the player's location and the map radius
        Point2D startCoords = new Point2D(playerLoc.getX() - MAP_RADIUS, playerLoc.getY() - MAP_RADIUS);
        // calculate the coordinates of the bottom right cell to be displayed, based on the player's location and the map radius
        Point2D endCoords = new Point2D(playerLoc.getX() + MAP_RADIUS + 1, playerLoc.getY() + MAP_RADIUS + 1);

        // ensure the map cannot scroll out of bounds in any direction:

        // ensure the map cannot scroll out of bounds to the left
        if (playerLoc.getX() - MAP_RADIUS < 0) {
            // the difference between where the frame is trying to be and where the limit is
            int difference = (int) (playerLoc.getX() - MAP_RADIUS);
            // add the difference on to the start and end coordinates so the map is displayed at the limit
            Point2D cameraStartNew = startCoords.add(-difference, 0);
            startCoords = cameraStartNew;
            Point2D cameraEndNew = endCoords.add(-difference, 0);
            endCoords = cameraEndNew;
        }
        // ensure the map cannot scroll past the top cells
        if (playerLoc.getY() - MAP_RADIUS < 0) {
            int difference = (int) (playerLoc.getY() - MAP_RADIUS);
            Point2D cameraStartNew = startCoords.add(0, -difference);
            startCoords = cameraStartNew;
            Point2D cameraEndNew = endCoords.add(0, -difference);
            endCoords = cameraEndNew;
        }

        // store the bottom right limit of the map for easier access
        int limitX = (int) currentLevel.getMapLimits().getX();
        int limitY = (int) currentLevel.getMapLimits().getY();

        // ensure the map cannot scroll out of bounds to the right
        if ((playerLoc.getX() + MAP_RADIUS + 1) > limitX) {
            int difference = (int) (limitX - (playerLoc.getX() + MAP_RADIUS + 1));
            Point2D cameraStartNew = startCoords.add(difference, 0);
            startCoords = cameraStartNew;
            Point2D cameraEndNew = endCoords.add(difference, 0);
            endCoords = cameraEndNew;
        }
        // ensure the map cannot scroll past the bottom cells
        if ((playerLoc.getY() + MAP_RADIUS + 1) > limitY) {
            int difference = (int) (limitY - (playerLoc.getY() + MAP_RADIUS + 1));
            Point2D cameraStartNew = startCoords.add(0, difference);
            startCoords = cameraStartNew;
            Point2D cameraEndNew = endCoords.add(0, difference);
            endCoords = cameraEndNew;
        }

        // return the start and end coordinates to be displayed
        Point2D[] coords = {startCoords, endCoords};
        return coords;
    }

    /**
     * Prints a message to the screen to notify the player.
     * @param message The message to be displayed.
     * @param colour The colour of the message to be displayed.
     */
    private static void showMessage(String message, Color colour) {
        // assign the graphics context to a variable for easier readability
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // set text options
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font(TEXT_SIZE));
        gc.setLineWidth(1);
        gc.setFill(colour);
        gc.setStroke(Color.LIGHTYELLOW);

        // print the message to the screen
        gc.fillText(message, Math.round(canvas.getWidth()  / 2 ), Math.round(canvas.getHeight()  / 20));
        gc.strokeText(message, Math.round(canvas.getWidth()  / 2), Math.round(canvas.getHeight()  / 20));
    }

    /**
     * Prints a sub-message to the screen to notify the player with secondary information.
     * @param message The sub-message to be displayed.
     * @param colour The colour of the sub-message to be displayed.
     */
    private static void showSubMessage(String message, Color colour) {
        // assign the graphics context to a variable for easier readability
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // set text options
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font(TEXT_SIZE));
        gc.setLineWidth(1);
        gc.setFill(colour);
        gc.setStroke(Color.LIGHTYELLOW);

        // print the sub message to the screen
        gc.fillText(message, Math.round(canvas.getWidth()  / 2), Math.round(canvas.getHeight()  / 8));
        gc.strokeText(message, Math.round(canvas.getWidth()  / 2), Math.round(canvas.getHeight()  / 8));
    }


    /**
     * Prints the inventory of the player, uses the icons of the items to show if it is in the inventory or not.
     */
    private static void showInventory() {
        // assign the graphics context to a variable for easier readability
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // print the message to the screen
        final long INVENTORY_WIDTH = Math.round(canvas.getWidth());
        final long INVENTORY_HEIGHT = Math.round(getMapAreaHeightPx() + 20);

        gc.setFill(Color.rgb(195,195,195));
        gc.fillRect( 0, getMapAreaHeightPx(), getMapAreaWidthPx(), 50);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, getMapAreaHeightPx(), getMapAreaWidthPx(), 2);

        // set text options
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font(30));
        gc.setLineWidth(4);
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.LIGHTYELLOW);

        Inventory inv = Engine.getCurrentLevel().getPlayer().getInventory();


        gc.fillText("Inventory: ", INVENTORY_WIDTH * 0.1 , INVENTORY_HEIGHT);

        final double INVENTORY_ICON_SIZE = 30;

        if (inv.hasKey(KeyType.GREEN)) {
            gc.drawImage(new Image("Resources/Images/key_green.png"), INVENTORY_WIDTH * 0.2, INVENTORY_HEIGHT * 0.99, INVENTORY_ICON_SIZE, INVENTORY_ICON_SIZE);
        }

        if (inv.hasKey(KeyType.BLUE)) {
            gc.drawImage(new Image("Resources/Images/key_blue.png"), INVENTORY_WIDTH * 0.25, INVENTORY_HEIGHT * 0.99, INVENTORY_ICON_SIZE, INVENTORY_ICON_SIZE);
        }

        if (inv.hasKey(KeyType.RED)) {
            gc.drawImage(new Image("Resources/Images/key_red.png"), INVENTORY_WIDTH * 0.3, INVENTORY_HEIGHT * 0.99, INVENTORY_ICON_SIZE, INVENTORY_ICON_SIZE);
        }

        if (inv.hasTokens(1)) {
            gc.drawImage(new Image("Resources/Images/token.png"), INVENTORY_WIDTH * 0.35, INVENTORY_HEIGHT * 0.99, INVENTORY_ICON_SIZE, INVENTORY_ICON_SIZE);
            gc.fillText("x" + inv.getTokenCount(), INVENTORY_WIDTH * 0.41, INVENTORY_HEIGHT *1.005 );
        }

        if (inv.hasFireBoots()) {
            gc.drawImage(new Image("Resources/Images/fire_boots.png"), INVENTORY_WIDTH * 0.45, INVENTORY_HEIGHT * 0.99, INVENTORY_ICON_SIZE, INVENTORY_ICON_SIZE);
        }

        if (inv.hasFlippers()) {
            gc.drawImage(new Image("Resources/Images/flippers.png"), INVENTORY_WIDTH * 0.5, INVENTORY_HEIGHT * 0.99, INVENTORY_ICON_SIZE, INVENTORY_ICON_SIZE);
        }
    }

    private static void showMotd() {
        // try to update the message of the day
        try {
            Motd.updateMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // store the width and height of the map in pixels for easier readability
        double mapWidth = getMapAreaWidthPx(),
                mapHeight = getMapAreaHeightPx();

        String message = Motd.getMessage();

        // assign the graphics context to a variable for easier readability
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // set text options
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font(TEXT_SIZE / 1.2));
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.LIGHTYELLOW);

        // Write the pretext of the MOTD
        gc.fillText("Some wise words:", Math.round(mapWidth  / 2), mapHeight - Math.round(mapHeight  / 5));
        gc.strokeText("Some wise words:", Math.round(mapWidth  / 2), mapHeight - Math.round(mapHeight  / 5));

        // change the font for the message itself
        gc.setFill(Color.YELLOW);
        gc.setFont(new Font(TEXT_SIZE / 1.8));

        // print the MOTD on 1, 2 or 3 separate lines depending on its length

        if(message.length() < 68) { // we can fit around 74 characters on a line maximum
            gc.fillText(message, Math.round(mapWidth  / 2), mapHeight - Math.round(mapHeight  / 8));

        } else if(message.length() < 136) {
            // split the message into 2 and display each one underneath the other
            String messageP1 = message.substring(0, message.length() / 2) + "-",
                    messageP2 = message.substring(message.length() / 2);

            // print the parts separately at different heights
            gc.fillText(messageP1, Math.round(mapWidth  / 2), mapHeight - Math.round(mapHeight  / 8));

            gc.fillText(messageP2, Math.round(mapWidth  / 2), mapHeight - Math.round(mapHeight  / 11.5));
        } else {
            // split the message into 3 and display each one underneath the other
            String messageP1 = message.substring(0, message.length() / 3) + "-",
                    messageP2 = message.substring(message.length() / 3, 2 * message.length() / 3) + "-",
                    messageP3 = message.substring(2 * message.length() / 3);

            // print the parts separately at different heights
            gc.fillText(messageP1, Math.round(mapWidth  / 2), mapHeight - Math.round(mapHeight  / 7));

            gc.fillText(messageP2, Math.round(mapWidth  / 2), getMapAreaHeightPx() - Math.round(mapHeight  / 9.5));

            gc.fillText(messageP3, Math.round(mapWidth  / 2), getMapAreaHeightPx() - Math.round(mapHeight  / 15));
        }


    }

    /**
     * Sets the {@link #playerProfile} to the one specified.
     * @param playerProfile The {@link Profile} to use.
     */
    private static void setProfile(Profile playerProfile) {
        Engine.playerProfile = playerProfile;
    }

    /**
     * Gets the current {@link #playerProfile}.
     * @return The current {@link #playerProfile}.
     */
    private static Profile getProfile() {
        return Engine.playerProfile;
    }

    /**
     * Temporarily dims the game to emphasise a message to the player.
     * @param opacity The opacity value to use when dimming the background.
     */
    private static void dimBackground(double opacity) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(0,0,0, opacity));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}