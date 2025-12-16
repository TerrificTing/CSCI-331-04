package tiles.model;

/**
 * An enumeration that names the four directions the tiles can be moved in the board.
 * Here is an example of how you can use this enum in another class:
 *
 * <pre>
 * Direction dir = Direction.NORTH;        // the direction is NORTH
 * if (dir == Direction.NORTH) {
 *     System.out.println(dir);              // prints: NORTH
 * }
 * dir = Direction.SOUTH;               // the direction is now SOUTH
 * </pre>
 *
 * @author CS RIT
 */
public enum Direction {
    NORTH("N"), SOUTH("S"), EAST("E"), WEST("W");

    /**
     * the initial of the direction
     */
    private final String initial;

    Direction(String initial) {
        this.initial = initial;
    }

    /**
     * Get the direction of the given initial
     * @param initial the initial
     * @return the direction associated with the given initial. If the initial doesn't exist, returns null.
     */
    public static Direction getDirectionByInitial(String initial) {
        for (Direction direction : Direction.values()) {
            if (direction.initial.equals(initial)) return direction;
        }
        return null;
    }
}
