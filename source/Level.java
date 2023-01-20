import javafx.geometry.Point2D;
import java.util.ArrayList;

/**
 * A Level which can be played by the {@link Engine}.
 * @author Jozef Bohosian
 */
public class Level {

    /**
     * The index of the level.
     */
    private final int levelNumber;

    /**
     * Whether the level has been completed.
     */
    private boolean finished;

    /**
     * The map containing a 2D array of {@link Cell}s.
     */
    private Cell[][] map;

    /**
     * The {@link Player} of the level.
     */
    private Player player;

    /**
     * All of the {@link Enemy} instances in the level.
     */
    private ArrayList<Enemy> enemies;

    /**
     * The total time elapsed since the level was started.
     */
    private double elapsedTime;


    /**
     * Constructs a level.
     * @param levelNumber The index of the level.
     * @param map The level's {@link #map}.
     * @param player The {@link Player} of the level.
     * @param enemies All of the {@link Enemy} instances in the level.
     */
    public Level(int levelNumber, Cell[][] map, Player player, ArrayList<Enemy> enemies) {
        this.levelNumber = levelNumber;
        this.map = map;
        this.player = player;
        this.enemies = enemies;
    }

    /**
     * Gets the level's {@link #map}.
     * @return The level's {@link #map}.
     */
    public Cell[][] getMap() {
        return map;
    }

    /**
     * Sets the level's {@link #map}.
     * @param map The {@link #map} to be set to.
     */
    public void setMap(Cell[][] map) {
        this.map = map;
    }

    /**
     * Gets the level's index.
     * @return The level's index.
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Gets the level's {@link Player} instance.
     * @return The level's {@link Player} instance.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets all of the {@link Enemy} instances in the level.
     * @return All of the {@link Enemy} instances in the level.
     */
    public ArrayList<Enemy> getEnemies() {
        return this.enemies;
    }

    /**
     * Retrieves a cell at a specified coordinate.
     * @param location The location of the {@link Cell} to be retrieved.
     * @return The {@link Cell} at the location specified.
     */
    public Cell getCell(Point2D location) {
        return map[(int)location.getY()][(int)location.getX()];
    }

    /**
     * Gets the {@link Cell} the {@link Player} is currently on.
     * @return The {@link Cell} the {@link Player} is currently on.
     */
    public Cell getCurrentCell() {
        Point2D loc = player.getLocation();
        return map[(int)loc.getY()][(int)loc.getX()];
    }

    /**
     * Sets whether the level is complete.
     * @param state Whether or not the level is complete.
     */
    public void setFinished(boolean state) {
        this.finished = state;
    }

    /**
     * Gets whether the level is complete.
     * @return Whether the level is complete.
     */
    public boolean isFinished() {
        return this.finished;
    }

    /**
     * Checks whether the specified {@link Cell} touches an {@link Enemy} boundary.
     * @param cell The {@link Cell} to check.
     * @return  whether the specified {@link Cell} touches an {@link Enemy} boundary.
     */
    public boolean doesCellTouchEnemyBoundary(Cell cell) {
        Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        Cell cellInDir;
        for(Direction dir : dirs) {
            cellInDir = getRelativeCell(cell, dir);
            if(!cellInDir.isEnemyPassable()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether the {@link Cell} at the specified location touches an {@link Enemy} boundary.
     * @param location The coordinates of the {@link Cell} to check.
     * @return Whether the {@link Cell} at the specified location touches an {@link Enemy} boundary.
     */
    public boolean doesCellTouchEnemyBoundary(Point2D location) {
        return doesCellTouchEnemyBoundary(getCell(location));
    }

    /**
     * @param cell The {@link Cell} whose adjacent {@link Cell} we're interested in.
     * @param direction The direction from the specified {@link Cell} our {@link Cell} of interest is in.
     * @return The {@link Cell} adjacent to the one specified in the direction specified.
     */
    public Cell getRelativeCell(Cell cell, Direction direction) {
        Cell result;
        Point2D cellLoc = cell.getLocation();
        switch(direction) {
            case UP:
                result = map[(int) cellLoc.getY() - 1][(int) cellLoc.getX()];
                break;
            case DOWN:
                result = map[(int) cellLoc.getY() + 1][(int) cellLoc.getX()];
                break;
            case LEFT:
                result = map[(int) cellLoc.getY()][(int) cellLoc.getX() - 1];
                break;
            case RIGHT:
                result = map[(int) cellLoc.getY()][(int) cellLoc.getX() + 1];
                break;
            default:
                result = map[(int) cellLoc.getY()][(int) cellLoc.getX()];
                break;
        }
        return result;
    }

    /**
     * Gets a {@link Cell} adjacent to one a {@link Character} is currently on.
     * @param character The {@link Character} whose adjacent {@link Cell} we're interested in.
     * @param dir The {@link Direction} from the {@link Character} the {@link Cell} of interest is in.
     * @return The {@link Cell} adjacent to that of the specified {@link Character} in the {@link Direction} specified.
     */
    public Cell getCellRelativeToCharacter(Character character, Direction dir) {
        Cell characterCell = getCell(character.getLocation());

        return getRelativeCell(characterCell, dir);
    }

    /**
     * Gets the {@link Cell} a {@link Character} is currently on.
     * @param character The {@link Character} whose current {@link Cell} we're interested in.
     * @return The {@link Cell} the specified {@link Character} is currently on.
     */
    public Cell getCharacterCell(Character character) {
        Point2D loc = character.getLocation();
        return map[(int) loc.getY()][(int) loc.getX()];
    }

    /**
     * Retrieves a subsection of the {@link #map} based on start and end coordinates.
     * @param start The coordinate of the top left {@link Cell} of the subsection of the {@link #map} to retrieve.
     * @param end The coordinate of the bottom right {@link Cell} of the subsection of the {@link #map} to retrieve.
     * @return The subsection of the {@link #map} specified.
     */
    public Cell[][] getMapSection(Point2D start, Point2D end) {
        // create a new 2d array of the required size
        Cell[][] section = new Cell[(int) (end.getY() - start.getY())][(int) (end.getX() - start.getX())];

        // the coordinates of the next cell to be added to the section
        int secY = 0;
        int secX = 0;

        // ensure we aren't trying to display negative cell indexes
        if(start.getX() < 0) {
            Point2D start2 = new Point2D(0, start.getY());
            start = start2;
        }
        if(start.getY() < 0) {
            Point2D start2 = new Point2D(start.getX(), 0);
            start = start2;
        }

        // traverse the desired cell grid adding the appropriate cells to 'section' as we go
        for (int y = (int) start.getY(); y < end.getY(); y++) {
            for (int x = (int) start.getX(); x < end.getX(); x++) {
                section[secY][secX] = map[y][x];
                secX++;
            }
            secY++;
            secX = 0;
        }

        return section;
    }

    /**
     * Gets the location of the bottom right cell of the {@link #map} denoting its limits.
     * @return The location of the bottom right cell of the {@link #map} denoting its limits.
     */
    public Point2D getMapLimits() {
        int limitX = map[0].length;
        int limitY = map.length;
        return new Point2D(limitX, limitY);
    }

    /**
     * Jumps the {@link #player} to the {@link Cell} at the specified coordinates.
     * @param location The coordinates of the {@link Cell} the {@link #player} should jump to.
     */
    public void jumpPlayerTo(Point2D location) {
        getCurrentCell().playerLeave();
        getCell(location).playerEnter();
        player.setLocation(location);
    }

    /**
     * Jumps the {@link #player} to the {@link Cell} specified.
     * @param cell The {@link Cell} the {@link #player} should jump to.
     */
    public void jumpPlayerTo(Cell cell) {
        getCurrentCell().playerLeave();
        cell.playerEnter();
        player.setLocation(cell.location);
    }

    /**
     * Triggers each of the {@link #enemies} to perform their moves based on the {@link #player}'s position.
     */
    public void triggerEnemyMoves() {
        for(Enemy enemy : enemies) {
            enemy.nextMove();
        }
    }

    /**
     * Gets the distance in {@link Cell}s from a given cell to an {@link Enemy} boundary.
     * @param cell The {@link Cell} to check from.
     * @param dir The {@link Direction} to check in.
     * @return The distance in {@link Cell}s from a given cell to an {@link Enemy} boundary.
     */
    public int getCellDistanceToEnemyBoundary(Cell cell, Direction dir) {
        if(doesCellTouchEnemyBoundary(cell)) {
            return 0;
        } else {
            return 1 + getCellDistanceToEnemyBoundary(getRelativeCell(cell, dir), dir);
        }
    }

    /**
     * Gets the distance in {@link Cell}s from the {@link Cell} at the given coordinates to an {@link Enemy} boundary.
     * @param location The coordinates of the {@link Cell} to check from.
     * @param dir The {@link Direction} to check in.
     * @return The distance in {@link Cell}s from the {@link Cell} at the given coordinates to an {@link Enemy} boundary.
     */
    public int getCellDistanceToEnemyBoundary(Point2D location, Direction dir) {
        return getCellDistanceToEnemyBoundary(getCell(location), dir);
    }

    /**
     * Gets the {@link Direction} that the closest {@link Enemy} boundary is in from a given {@link Cell}.
     * @param cell The {@link Cell} from which to find the {@link Direction} to the closest {@link Enemy} boundary.
     * @return The {@link Direction} that the closest {@link Enemy} boundary is in.
     */
    public Direction getClosestEnemyBoundary(Cell cell) {
        int left = getCellDistanceToEnemyBoundary(cell, Direction.LEFT),
            right = getCellDistanceToEnemyBoundary(cell, Direction.RIGHT),
            up = getCellDistanceToEnemyBoundary(cell, Direction.UP),
            down = getCellDistanceToEnemyBoundary(cell, Direction.DOWN);
        Direction closest = Direction.LEFT;
        int currentLeast = left;

        if(right < currentLeast) {
            closest = Direction.RIGHT;
            currentLeast = right;
        }
        if(up < currentLeast) {
            closest = Direction.UP;
            currentLeast = up;
        }
        if(down < currentLeast) {
            closest = Direction.DOWN;
        }

        return closest;
    }

    /**
     * Gets the {@link Direction} that the closest {@link Enemy} boundary is in from the {@link Cell} at the given coordinates.
     * @param location The coordinates of the {@link Cell} to check from.
     * @return The {@link Direction} that the closest {@link Enemy} boundary is in.
     */
    public Direction getClosestEnemyBoundary(Point2D location) {
        return getClosestEnemyBoundary(getCell(location));
    }

    /**
     * Moves a {@link Character} to its adjacent {@link Cell} in the specified {@link Direction}.
     * @param character The {@link Character} to be moved.
     * @param dir The {@link Direction} the {@link Character} should move.
     */
    public void characterMove(Character character, Direction dir) {
        characterMove(character, getCellRelativeToCharacter(character, dir).getLocation());
    }

    /**
     * Moves a {@link Character} to a {@link Cell} at specified coordinates.
     * @param character The {@link Character} to be moved.
     * @param newLocation The coordinates the {@link Character} should move to.
     */
    public void characterMove(Character character, Point2D newLocation) {
        Cell current = getCharacterCell(character);
        Cell newCell = getCell(newLocation);
        if(character instanceof Enemy) {
            Enemy e = (Enemy) character;
            current.enemyLeave(e);
            newCell.enemyEnter(e);
        } else if (character instanceof Player) {
            current.playerLeave();
            newCell.playerEnter();
        }

        character.setLocation(newCell.getLocation());
    }

    /**
     * Gets the {@link Enemy} currently on the {@link Cell} at the specified coordinates.
     * @param cellLocation The coordinates of the {@link Cell} in question.
     * @return The {@link Enemy} currently on the {@link Cell} at the specified coordinates.
     */
    public Enemy getEnemyAt(Point2D cellLocation) {
        Enemy enemy = getEnemies().stream().filter(
                x -> x.getLocation().equals(cellLocation)).findFirst().get();

        return enemy;
    }

    /**
     * Checks whether a {@link Character} can move to its adjacent {@link Cell} in the specified {@link Direction}.
     * @param character The {@link Character} whose move is to be checked.
     * @param direction The {@link Direction} in which we check whether the {@link Character} can move.
     * @return Whether the {@link Character} can move to its adjacent {@link Cell} in the {@link Direction} specified.
     */
    public boolean checkMove(Character character, Direction direction) {
        Cell current = getCharacterCell(character);
        Cell newCell = getRelativeCell(current, direction);

        if (character instanceof Player) {
            return newCell.isPlayerPassable();
        } else {
            return (newCell.isEnemyPassable() && !newCell.isEnemyOnCell());
        }
    }

    /**
     * Checks whether an {@link Enemy} can move to its adjacent {@link Cell} in the {@link Direction} specified.
     * @param currentLoc The location of the {@link Enemy}.
     * @param direction The {@link Direction} to move in.
     * @return whether an {@link Enemy} can move to its adjacent {@link Cell} in the specified {@link Direction}.
     */
    public boolean checkEnemyMove(Point2D currentLoc, Direction direction) {
        Cell newCell = getRelativeCell(getCell(currentLoc), direction);

        return newCell.isEnemyPassable();
    }

    /**
     * Checks whether the {@link #player} should be killed in their current position.
     * @return Whether the {@link #player} should be killed in their current position.
     */
    public boolean isPlayerDead() {
        Cell currentCell = getCurrentCell();

        if (currentCell.isDeadly()) {
            return !((Obstacle) currentCell).playerIsSafe(player);
        }

        return currentCell.isEnemyOnCell();
    }

    /**
     * Checks whether the {@link #player} has reached the level's {@link Goal}.
     * @return Whether the {@link #player} has reached the level's {@link Goal}.
     */
    public boolean isPlayerAtGoal() {
        Cell currentCell = getCurrentCell();

        return currentCell instanceof Goal;
    }

    /**
     * Gets the total time elapsed since the level was started in seconds.
     * @return The total time elapsed since the level was started in seconds.
     */
    public double getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Sets the total time elapsed since the level was started in seconds.
     * @param elapsedTime The total time elapsed since the level was started in seconds.
     */
    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * Adds to the total time elapsed since the level was started.
     * @param continuedElapsedTime The amount of time to be added to the total time elapsed since the level was started.
     */
    public void addToElapsedTime(double continuedElapsedTime) {
        this.elapsedTime += continuedElapsedTime;
    }
}
