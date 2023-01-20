import java.io.*;
import java.time.*;
import java.time.format.*;

/**
 * A class for saving a level to a level file.
 *
 * @author Victor Cai
 * @version 1.0
 */
public class FileWriter {
    /**
     * Saves a level to a level file.
     *
     * @param level The level to save
     * @param profile The user profile playing this level
     */
    public static void saveLevel(Level level, Profile profile) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ssX").withZone(ZoneId.of("Z"));
        Instant saveTime = Instant.now();
        String timestamp = dtf.format(saveTime);
        System.out.println(timestamp);
        new File("savedGames").mkdir();
        String filename = String.format("savedGames/%s-level%d-saved-%s.txt", profile.getName(), level.getLevelNumber(),
                                        timestamp);
        File savedLevelFile = new File(filename);
        try {
            savedLevelFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        } PrintWriter out = null;

        try {
            out = new PrintWriter(savedLevelFile);
        } catch (FileNotFoundException e) {
            System.out.printf("FileWriter.saveLevel: Cannot open file \"%s\" for writing\n", filename);
            System.exit(-2);
        }

        writeLevelFile(level, out);

        out.close();
    }

    /**
     * Writes each section of the level file using one of the other private methods.
     *
     * @param level The level to be saved.
     * @param out The {@link PrintWriter} used to write to the level file.
     */
    private static void writeLevelFile(Level level, PrintWriter out) {
        // Level number, time and dimensions
        Cell[][] map = level.getMap();
        out.println(level.getLevelNumber());
        out.println(level.getElapsedTime());
        out.printf("%d %d\n", map.length, map[0].length);

        // Cell grid
        writeCellGrid(map, out);

        // Complex cells
        writeComplexCells(map, out);

        // Player
        writePlayer(level.getPlayer(), out);

        // Enemies
        for (Enemy enemy : level.getEnemies()) {
            writeEnemy(enemy, out);
        }

        // Inventory - this will ALWAYS be at the end of the file
        out.println("===INVENTORY===");
        Inventory inventory = level.getPlayer().getInventory();
        writeInventory(inventory, out);

    }

    /**
     * Writes a representation of the cell grid to the level file.
     *
     * @param map The cell grid to be saved
     * @param out The {@link PrintWriter} used to write to the level file.
     */
    private static void writeCellGrid(Cell[][] map, PrintWriter out) {
        for (Cell[] row : map) {
            for (Cell cell : row) {
                out.print(getSymbolForCell(cell));
            }
            out.println();
        }
    }

    /**
     * Converts a cell into its symbol
     *
     * @param cell The cell to be converted
     *
     * @return The symbol for the cell
     */
    private static String getSymbolForCell(Cell cell) {
        switch (cell.getClass().getName()) {
            case "Wall":
                return "L";
            case "Ground":
                return "#";
            case "Water":
                return "W";
            case "Fire":
                return "F";
            case "CollectableCell":
                // When the item has been collected, the cell becomes ground
                if (((CollectableCell) cell).getItem() == null) {
                    return "#";
                } else {
                    ItemType itemType = ((CollectableCell) cell).getItem().getType();
                    if (itemType == ItemType.FIRE_BOOTS) {
                        return "B";
                    } else if (itemType == ItemType.FLIPPERS) {
                        return "S";
                    } else if (itemType == ItemType.TOKEN) {
                        return "Q";
                    } else {
                        return "_";
                    }
                }
            case "TeleporterCell":
                return "T";
            default:
                return "_";
        }
    }

    /**
     * Writes the lines that represent complex cells. The types of complex cells
     * are: {@link TokenDoor}, {@link ColourDoor}, {@link CollectableCell}
     * containing a key, {@link Goal} and {@link TeleporterCell}.
     *
     * @param map The cell grid to be saved
     * @param out The {@link PrintWriter} used to write the level file
     */
    private static void writeComplexCells(Cell[][] map, PrintWriter out) {
        for (Cell[] row : map) {
            for (Cell cell : row) {
                if (isComplexCell(cell)) {
                    out.println(complexCellToStr(cell));
                }
            }
        }
    }

    /**
     * Gives the representation of a complex cell in a level file.
     *
     * @param cell a complex cell.
     *
     * @return The line of the level file that represents the complex cell.
     */
    private static String complexCellToStr(Cell cell) {
        String cellType = cell.getClass().getName();
        int y = (int) cell.getLocation().getY();
        int x = (int) cell.getLocation().getX();


        if (cellType.equals("TokenDoor")) {
            int numTokens = ((TokenDoor) cell).getNumTokens();
            boolean unlocked = ((TokenDoor) cell).isUnlocked();
            return String.format("TokenDoor %d %d %d %b", y, x, numTokens, unlocked);

        } else if (cellType.equals("ColourDoor")) {
            String colour = ((ColourDoor) cell).getColour().toString();
            boolean unlocked = ((ColourDoor) cell).isUnlocked();
            return String.format("ColourDoor %d %d %s %b", y, x, colour, unlocked);

        } else if (cellType.equals("TeleporterCell")) {
            int otherY = (int) ((TeleporterCell) cell).getDestination().getLocation().getY();
            int otherX = (int) ((TeleporterCell) cell).getDestination().getLocation().getX();
            return String.format("Teleporter %d %d %d %d", y, x, otherY, otherX);

        } else if (cellType.equals("CollectableCell")) {
            CollectableItem item = ((CollectableCell) cell).getItem();

            if (item != null && item.getType() == ItemType.KEY) {
                String colour = ((Key) item).getColour().toString();
                return String.format("Key %d %d %s", y, x, colour);
            }
        } else if (cellType.equals("Goal")) {
            int variant = ((Goal) cell).getVariant();
            return String.format("Goal %d %d %d", y, x, variant);
        }

        // This should never happen!
        throw new IllegalArgumentException("Not a valid complex cell type: " + cellType);
    }

    /**
     * Writes the line that specifies the player.
     *
     * @param player the player in the current game
     * @param out The {@link PrintWriter} used to write the level file
     */
    private static void writePlayer(Player player, PrintWriter out) {
        int y = (int) player.getLocation().getY();
        int x = (int) player.getLocation().getX();

        out.printf("Player %d %d\n", y, x);
    }

    /**
     * Writes a line that specifies an enemy.
     *
     * @param enemy The enemy to be written
     * @param out The {@link PrintWriter} used to write the level file
     */
    private static void writeEnemy(Enemy enemy, PrintWriter out) {
        int y = (int) enemy.getLocation().getY();
        int x = (int) enemy.getLocation().getX();

        switch (enemy.getClass().getName()) {
            case "WallFollowingEnemy":
                out.printf("Enemy wall %d %d %s\n", y, x,
                           ((WallFollowingEnemy) enemy).getDirection().toString());
                break;
            case "DumbFollowingEnemy":
                out.printf("Enemy dumb %d %d\n", y, x);
                break;
            case "StraightLineEnemy":
                out.printf("Enemy line %d %d %s\n",
                           y, x, ((StraightLineEnemy) enemy).getDirection().toString());
                break;
            case "SmartFollowingEnemy":
                out.printf("Enemy smart %d %d\n", y, x);
                break;
        }
    }

    /**
     * Writes the state of the player's inventory.
     *
     * @param inventory The inventory to save
     * @param out The {@link PrintWriter} used to write the level file
     */
    private static void writeInventory(Inventory inventory, PrintWriter out) {
        out.println(inventory.hasFireBoots());
        out.println(inventory.hasFlippers());

        if (inventory.getKeyList().isEmpty()) {
            out.print("none");
        } else {
            for (Key key : inventory.getKeyList()) {
                out.print(key.getColour() + " ");
            }
        }
        out.println();

        out.println(inventory.getTokenCount());
    }

    /**
     * Determines if a cell is complex
     *
     * @param cell The cell to test
     *
     * @return true if the cell is a complex cell
     */
    private static boolean isComplexCell(Cell cell) {
        String cellType = cell.getClass().getName();

        boolean complexCollectable = false;
        if (cell instanceof CollectableCell) {
            if (((CollectableCell) cell).getItem() != null) {
                ItemType itemType = ((CollectableCell) cell).getItem().getType();
                complexCollectable = itemType == ItemType.KEY;
            }
        }

        return (cellType.equals("TokenDoor")
                || cellType.equals("ColourDoor")
                || complexCollectable
                || cellType.equals("TeleporterCell")
                || cellType.equals("Goal"));
    }

}
