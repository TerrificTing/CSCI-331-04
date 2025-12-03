package tiles.model;

/**
 * An enumeration that represents the three different levels of difficulty of the game.
 * Every level has a different goal tile number.
 * 
 * @author CS RIT
 */
public enum Level {
    TEST(32),
    EASY(1024),
    NORMAL(2048);
    /**
     * the goal number for the level
     */
    private final int goalNumber;
    
    Level(int goalNumber){
        this.goalNumber = goalNumber;
    }

    /**
     * Get the goal number for this level
     * @return the goal number associated with this level
     */
    public int getGoalNumber(){
        return this.goalNumber;
    }
}