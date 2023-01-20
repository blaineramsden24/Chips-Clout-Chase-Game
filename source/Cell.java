import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.ArrayList;


/**
 * Represents a cell on the grid.
 * @author Victor Cai
 * @version 1.0
 */
public abstract class Cell {
    protected boolean playerOnCell;
    protected Enemy enemy;
    protected boolean playerPassable;
    protected boolean enemyPassable;
    protected Image image;
    protected Point2D location;


    /**
     * Creates a cell.
     *
     * @param playerPassable Whether or not the player can enter the cell.
     * @param enemyPassable Whether or not an enemy can enter the cell.
     * @param image Image shown in the game to represent the cell.
     * @param location The coordinates of the cell on the grid, with (0,0) being
     * the top-left cell.
     */
    public Cell(boolean playerPassable, boolean enemyPassable, Image image, Point2D location) {
        this.playerPassable = playerPassable;
        this.enemyPassable = enemyPassable;
        this.image = image;
        this.location = location;
    }

    /**
     * Indicates if the player is currently on the cell.
     *
     * @return true if the player is on the cell.
     */
    public boolean isPlayerOnCell() {
        return this.playerOnCell;
    }

    /**
     * Indicates if an enemy is currently on the cell
     *
     * @return true if an enemy is on the cell
     */
    public boolean isEnemyOnCell() {
        return enemy != null;
    }

    /**
     * Indicates if an enemy is currently on the cell
     *
     * @return true if an enemy is on the cell
     */
    public Enemy getEnemy() {
        return this.enemy;
    }

    /**
     * Indicates if the player can enter the cell.
     * @return true if the player can enter
     */
    public boolean isPlayerPassable() {
        return this.playerPassable;
    }

    /**
     * Indicates if an enemy can enter the cell.
     * @return true if enemies can enter
     */
    public boolean isEnemyPassable() {
        return this.enemyPassable;
    }

    /**
     * Indicates if the cell can kill the player
     * @return true if the cell can kill the player
     */
    public boolean isDeadly() { return false; } // override from implementation if deadly

    /**
     * Gets the image of the cell.
     * @return The image of the cell
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Gets the location of the cell on the grid.
     *
     * @return The location of the cell
     */
    public Point2D getLocation() {
        return location;
    }

    /**
     * Changes the state of the cell to represent that the player has entered.
     */
    public void playerEnter() {
        this.playerOnCell = true;
    }

    /**
     * Changes the state of the cell to represent that the player has left.
     */
    public void playerLeave() {
        this.playerOnCell = false;
    }

    /**
     * Represents an enemy entering the cell.
     */
    public void enemyEnter(Enemy enemy) {
        if (this.enemy == null) {
            this.enemy = enemy;
        }
    }

    /**
     * Represents an enemy leaving the cell.
     */
    public void enemyLeave(Enemy enemy) {
        this.enemy = null;
    }

    /**
     * Gives a string representation of the cell for debugging purposes
     *
     * @return A string consisting of the cell's type and its coordinates
     */
    public String toString() {
        return this.getClass().getName() + " " + this.location.toString();
    }

}
