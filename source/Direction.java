/**
 * Enum for  Directions
 *
 * @author Nick Robertson, Victor Cai
 * @version 2.0
 */
public enum Direction {
    UP, RIGHT, DOWN, LEFT, NONE;

    private String string;
    private Direction oppositeDirection;
    private Direction clockwiseDirection;
    private Direction antiClockwiseDirection;

    static {
        UP.string = "up";
        DOWN.string = "down";
        LEFT.string = "left";
        RIGHT.string = "right";

        UP.oppositeDirection = DOWN;
        DOWN.oppositeDirection = UP;
        LEFT.oppositeDirection = RIGHT;
        RIGHT.oppositeDirection = LEFT;

        UP.clockwiseDirection = RIGHT;
        DOWN.clockwiseDirection = LEFT;
        LEFT.clockwiseDirection = UP;
        RIGHT.clockwiseDirection = DOWN;

        UP.antiClockwiseDirection = LEFT;
        DOWN.antiClockwiseDirection = RIGHT;
        LEFT.antiClockwiseDirection = DOWN;
        RIGHT.antiClockwiseDirection = UP;
    }

    public String toString() {
        return string;
    }

    public Direction getOppositeDirection() {
        return oppositeDirection;
    }

    public Direction getClockwiseDirection() {
        return clockwiseDirection;
    }

    public Direction getAntiClockwiseDirection() {
        return antiClockwiseDirection;
    }
}
