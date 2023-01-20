import javafx.geometry.*;

import java.io.*;
import java.util.*;

/**
 * Class for reading level files
 *
 * @author Victor Cai
 * @version 3.0
 */
public class FileReader {
    /**
     * Constructs a level by reading a level file.
     *
     * @param filename Name of the level file.
     * @param playerVariant The variant of the player in this level
     *
     * @return The level as specified in the file.
     */
    public static Level readLevel(String filename, int playerVariant) {
        Scanner in = null;
        try {
            File levelFile = new File(filename);
            String absolutePath = levelFile.getAbsolutePath();
            File absLevelFile = new File(absolutePath);
            in = new Scanner(absLevelFile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            System.exit(1);
        }

        return buildLevel(in, playerVariant);
    }

    /**
     * Creates a {@link Level} by reading a level file.
     *
     * @param in The {@link Scanner} used to read the file.
     *
     * @return A level of the game.
     */
    private static Level buildLevel(Scanner in, int playerVariant) {
        int levelNumber = in.nextInt();
        in.nextLine();
        double timeSpent = in.nextDouble();

        // First read in the size of the map
        int height = in.nextInt();
        int width = in.nextInt();
        Cell[][] map = new Cell[height][width];
        in.nextLine();

        // Then read the simple cells row by row
        int rowNum = 0;
        while (in.hasNextLine() && rowNum < height) {
            String line = in.nextLine();
            readRow(map, rowNum, line);
            rowNum++;
        }

        // Then read the complex cells, player and enemies
        Player player = null;
        ArrayList<Enemy> enemies = new ArrayList<>();
        boolean stop = false;
        while (in.hasNextLine() && (!stop)) {
            String line = in.nextLine();

            // Stop when the inventory part is reached
            if (line.equals("===INVENTORY===")) {
                stop = true;
            } else {
                if (line.startsWith("Player")) {
                    player = readPlayer(map, line, playerVariant);
                } else if (line.startsWith("Enemy")) {
                    enemies.add(readEnemy(map, line));
                } else {
                    readComplexCell(map, line);
                }
            }
        }

        // The inventory will ALWAYS be at the end of the file
        if (in.hasNextLine()) {
            if (player == null) {
                throw new IllegalStateException("The player has not been created");
            } else {
                readInventory(player, in);
            }
        }

        in.close();

        Level level = new Level(levelNumber, map, player, enemies);
        level.setElapsedTime(timeSpent);
        return level;
    }

    /**
     * Creates a single row of cells from one line in the level file.
     *
     * @param map The cell grid being constructed.
     * @param rowNum The y-position of the row, counting from 0 for the top row.
     * @param line The line from the level file specifying this row,
     */
    private static void readRow(Cell[][] map, int rowNum, String line) {
        for (int i = 0; i < line.length(); i++) {
            Point2D location = new Point2D(i, rowNum);
            switch (line.charAt(i)) {
                case 'L':
                    map[rowNum][i] = new Wall(location);
                    break;
                case '#':
                    map[rowNum][i] = new Ground(location);
                    break;
                case 'W':
                    map[rowNum][i] = new Water(location);
                    break;
                case 'F':
                    map[rowNum][i] = new Fire(location);
                    break;
                case 'T':
                    map[rowNum][i] = new TeleporterCell(location);
                    break;
                case 'B':
                    FireBoots fireBoots = new FireBoots();
                    map[rowNum][i] = new CollectableCell(location, fireBoots);
                    break;
                case 'S':
                    Flippers flippers = new Flippers();
                    map[rowNum][i] = new CollectableCell(location, flippers);
                    break;
                case 'Q':
                    Token token = new Token();
                    map[rowNum][i] = new CollectableCell(location, token);
                    break;
                case '_':
                    map[rowNum][i] = null;
                    System.out.printf("WARNING: Cell initially null at (%d, %d)\n", rowNum, i);
                    break;
                default:
                    String errorMsg = String.format("Invalid cell type '%s' at (%d, %d)\n", line.charAt(i), rowNum, i);
                    throw new IllegalArgumentException(errorMsg);
            }
        }
    }

    /**
     * Sets up a complex cell. A complex cell is a cell that needs more than
     * just its location to be constructed. For teleporter cells, this method
     * simply connects up the two ends of a teleport.
     *
     * @param map The cell grid being constructed.
     * @param line The line from the level file describing this complex cell.
     */
    private static void readComplexCell(Cell[][] map, String line) {
        Scanner lineReader = new Scanner(line);
        String type = lineReader.next();
        int y = lineReader.nextInt(); // 0-based, starting from top
        int x = lineReader.nextInt(); // 0-based, starting from left
        Point2D location = new Point2D(x, y);

        switch (type) {
            case "TokenDoor":
                int numTokens = lineReader.nextInt();
                boolean tUnlocked = lineReader.nextBoolean();
                map[y][x] = new TokenDoor(location, numTokens, tUnlocked);
                break;
            case "ColourDoor":
                String doorColour = lineReader.next();
                boolean cUnlocked = lineReader.nextBoolean();
                map[y][x] = new ColourDoor(location, doorColour, cUnlocked);
                break;
            case "Key":
                String keyColour = lineReader.next();
                Key key = new Key(keyColour);
                map[y][x] = new CollectableCell(location, key);
                break;
            case "Teleporter":
                /* The teleporters have already been created, so we are only
                 connecting them up here */
                int otherY = lineReader.nextInt();
                int otherX = lineReader.nextInt();
                TeleporterCell teleporter1 = (TeleporterCell) map[y][x];
                TeleporterCell teleporter2 = (TeleporterCell) map[otherY][otherX];
                teleporter1.setDestination(teleporter2);
                teleporter2.setDestination(teleporter1);
                break;
            case "Goal":
                int variant = lineReader.nextInt();
                map[y][x] = new Goal(location, variant);
                break;
            default:
                throw new IllegalArgumentException("Invalid complex cell type: " + type);
        }

        lineReader.close();
    }

    /**
     * Sets up the player as specified by a line in the file
     *
     * @param map The cell grid in which the player should be placed.
     * @param line The line of the file that specifies the player's type and its location.
     *
     * @return The player as specified in the file.
     */
    private static Player readPlayer(Cell[][] map, String line, int variant) {
        Scanner lineReader = new Scanner(line);
        lineReader.next(); // throw away the "Player" at the start
        int y = lineReader.nextInt();
        int x = lineReader.nextInt();

        Point2D location = new Point2D(x, y);
        Player player = new Player(location, variant);
        map[y][x].playerEnter();

        lineReader.close();
        return player;
    }

    /**
     * Sets up an enemy as specified by a line in the file.
     *
     * @param map The cell grid in which the enemy should be placed.
     * @param line The line of the file that specifies the enemy.
     *
     * @return The enemy as specified in the file
     */
    private static Enemy readEnemy(Cell[][] map, String line) {
        Scanner lineReader = new Scanner(line);
        lineReader.next();
        String enemyType = lineReader.next();
        int y = lineReader.nextInt();
        int x = lineReader.nextInt();
        Point2D location = new Point2D(x, y);

        Enemy enemy;
        Direction initDirection;
        switch (enemyType) {
            case "wall":
                initDirection = readDirection(lineReader.next());
                enemy = new WallFollowingEnemy(location, initDirection);
                break;
            case "line":
                initDirection = readDirection(lineReader.next());
                enemy = new StraightLineEnemy(location, initDirection);
                break;
            case "dumb":
                enemy = new DumbFollowingEnemy(location);
                break;
            case "smart":
                enemy = new SmartFollowingEnemy(location);
                break;
            default:
                throw new IllegalArgumentException("Not a valid enemy type: " + enemyType);
        }

        map[y][x].enemyEnter(enemy);
        lineReader.close();
        return enemy;
    }

    /**
     * Converts a string representing a direction to a {@link Direction} enum.
     *
     * @param direction The string "left", "right", "up" or "down".
     *
     * @return The direction.
     */
    private static Direction readDirection(String direction) {
        switch (direction) {
            case "left":
                return Direction.LEFT;
            case "right":
                return Direction.RIGHT;
            case "up":
                return Direction.UP;
            case "down":
                return Direction.DOWN;
            default:
                throw new IllegalArgumentException("Not a valid direction: " + direction);
        }
    }

    /**
     * Adds items to the player's inventory based on the last 4 lines of the level file
     *
     * @param player The player who owns the inventory
     * @param in The {@link Scanner} used to read the file
     */
    private static void readInventory(Player player, Scanner in) {
        Inventory inventory = player.getInventory();

        // Fire boots
        boolean hasFireBoots = in.nextBoolean();
        if (hasFireBoots) {
            inventory.setFireBoots();
        }
        in.nextLine();

        // Flippers
        boolean hasFlippers = in.nextBoolean();
        if (hasFlippers) {
            inventory.setFlippers();
        }
        in.nextLine();

        // Keys
        String keys = in.nextLine();
        if (!keys.equals("none")) {
            Scanner keyReader = new Scanner(keys);

            while (keyReader.hasNext()) {
                String keyColour = keyReader.next();
                Key key = new Key(keyColour);
                inventory.addKey(key);
            }

            keyReader.close();
        }

        // Tokens
        int numTokens = in.nextInt();
        for (int i = 0; i < numTokens; i++) {
            inventory.addToken();
        }
    }

}