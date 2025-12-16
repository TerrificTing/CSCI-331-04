package tiles.model;

import java.util.*;

/**
 * The model for the Tiles 2048 game.
 *
 * @author CS RIT
 */
public class TilesModel {

    /**
     * the square dimension of the board
     */
    public static final int DIM = 4;

    /**
     * Default tile value
     */
    public static final int EMPTY = 0;

    /**
     * String representation of an empty tile
     */
    public static final String WHITESPACE = " ";

    /**
     * Error message sent when the game is over and the player tries to make a move.
     */
    public static final String GAME_OVER_MSG = "The game is already over!";

    /**
     * Enumeration for representing the different states of the game
     */
    private enum GameStatus {
        READY, // initial state
        ONGOING, // game in progress
        LOST,
        WON
    }

    /**
     * The messages this model send to the observers to notify them what happened
     */
    private static final EnumMap<GameStatus, String> STATE_MSGS =
            new EnumMap<>(Map.of(
                    GameStatus.WON, "You won!",
                    GameStatus.LOST, "Game Over 😥.",
                    GameStatus.ONGOING, "+%d points",
                    GameStatus.READY, "Good luck!"
            ));

    /**
     * The list of possible initial numbers a tile can hold when created
     */
    private static final List<Integer> INITIAL_TILE_VALUES = List.of(2, 4);

    /**
     * The probability of generating a tile with the number 2
     */
    private static final int CHANCE_TO_GENERATE_SMALLEST_NUMBER = 75;

    /**
     * represent a column delta and row delta to move around the board
     */
    private record Coordinates(int row, int col) {
    }

    /**
     * All four directions in which the tiles can be moved in the board and their delta
     * to visit each tile in the board in that direction.
     */
    private static final EnumMap<Direction, Coordinates> DELTA_MAP = new EnumMap<>(Map.of(
            Direction.NORTH, new Coordinates(1, 0),
            Direction.SOUTH, new Coordinates(-1, 0),
            Direction.EAST, new Coordinates(0, -1),
            Direction.WEST, new Coordinates(0, 1)));

    /**
     * the game board
     */
    private int[][] board;
    /**
     * the status of the game
     */
    private GameStatus status;
    /**
     * the number of moves made by the player on this game
     */
    private int movesMade;
    /**
     * the current player's score of this game
     */
    private int score;
    /**
     * the observers of this model
     */
    private final List<Observer<TilesModel, String>> observers;
    /**
     * the random number generator to create the random tile numbers
     */
    private final Random random;

    /**
     * Has the board changed after a move?
     */
    private boolean hasChanged;

    /**
     * Create a new game
     * @param levelName the difficulty level of the game
     */
    public TilesModel() {
        this.random = new Random();
        // getting the goal number according to the game's level selected
        this.observers = new ArrayList<>();
        init();
    }

    /**
     * Initialize the board and create two random tiles
     */
    private void init() {
        this.movesMade = 0;
        this.status = GameStatus.READY;
        this.score = 0;
        // the board is initialized with all the tiles EMPTY (value 0)
        this.board = new int[DIM][DIM];
        // the game starts generating two random tiles
        addNewTile();
        addNewTile();
    }

    /**
     * The view calls this method to add themselves as an observer of the model.
     *
     * @param observer the observer
     */
    public void addObserver(Observer<TilesModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * Get the number of moves that have been made.
     *
     * @return moves made
     */
    public int getMovesMade() {
        return this.movesMade;
    }

    /**
     * Get the status of the game.
     *
     * @return game status
     */
    public GameStatus getGameStatus() {
        return this.status;
    }

    /**
     * Get the value stored in a given row and column
     *
     * @param row the row
     * @param col the col
     * @return the value stored in that cell, or EMPTY if there
     * is no value.
     */
    public int getContent(int row, int col) {
        return this.board[row][col];
    }

    /**
     * Get the player's current score.
     *
     * @return current score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Get the best score achieved in a game
     *
     * @return the best score
     */

    /**
     * Notify the observers that the game is ready.
     * This method is called only once from the View
     * (last step in the JavaFX start method), once the View is ready
     * to display all the model's information.
     */
    public void ready() {
        notifyObservers(STATE_MSGS.get(GameStatus.READY));
    }

    /**
     * Is the game over? The user will lose the game
     * if the board is full and none of the tiles can be joined together.
     *
     * @return whether the board is full and none of the tiles can be joined together.
     */
    public boolean hasLost() {
        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                // the game continues if there are empty cells in the board
                if (board[row][col] == EMPTY) {
                    return false;
                } else {
                    // or there are tiles that can be joined together
                    for (Coordinates delta : DELTA_MAP.values()) {
                        int nrow = delta.row() + row;
                        int ncol = delta.col() + col;
                        if (inBounds(nrow, ncol) && this.board[row][col] == this.board[nrow][ncol]) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Move all the tiles in the board in the given direction and update the game's statistics.
     *
     * @param direction the direction
     */
    public void move(Direction direction) {
        // not allowing further moves if the game is over
        if (isGameOver()){
            notifyObservers(GAME_OVER_MSG);
            return;
        }
        // resetting the flag to false before moving the tiles
        // the moveTiles method will set the flag to true if any tile has been moved or joined
        // with another tile
        hasChanged = false; // DO NOT MODIFY THIS
        
        // move the tiles in the given direction (moveTiles(direction)) and store the total
        // number of points of the tiles joined
        // TODO
        int points = moveTiles(direction);
        
        // if the board has changed, update the game status and notify the observers
        // otherwise, do nothing
        if (hasChanged) {
            // TODO
            // increment the number of moves made
            movesMade++;
            
            // update the score (updateScore(amount)) with the points returned by moveTiles
            updateScore(points);
            
            // add a random tile (addNewTile())
            addNewTile();

            // check if the game has been won (hasWon()), or lost (hasLost()), and set the status accordingly 
            // (see GameStatus enum at the top). If the game is still not over, the status should be ONGOING
            if (hasLost()) {
                status = GameStatus.LOST;
            } else {
                status = GameStatus.ONGOING;
            }
            // get the message associated with the game status (see STATES_MSGS) and send it to the view by notifying 
            // the observers. This message will let the view know what have happened.
            String msg = STATE_MSGS.get(status);
            if (status == GameStatus.ONGOING){
                msg = String.format(msg, points);
            }
            notifyObservers(msg);
        }
    }

    /**
     * Helper function that moves all tiles in the board in the given direction.
     * Tiles with the same number merge into one when they touch.
     *
     * @param direction The direction
     * @return points the sum of the numbers of the tiles merged during this movement
     */
    private int moveTiles(Direction direction) {
        // get the delta associated to this direction
        Coordinates delta = DELTA_MAP.get(direction);
        int points = 0;
        if (delta.col() == 0) { // moving north or south
            for (int col = 0; col < DIM; col++) { // look at each column
                // assigning the starting row according to the direction
                // north -> joining tiles from top to bottom, south -> joining tiles from bottom to top
                int row = (direction == Direction.NORTH ? 0 : DIM - 1);
                points += moveTiles(row, col, delta);
            }
        } else { // moving east or west
            for (int row = 0; row < DIM; row++) { // look at each row
                // assigning the starting col according to the direction
                // east -> joining tiles from right to left, west -> joining tiles from left to right
                int col = (direction == Direction.WEST ? 0 : DIM - 1);
                points += moveTiles(row, col, delta);
            }
        }
        return points;
    }

    /**
     * Move the tiles of a given row or column according to the delta coordinates.
     * The delta coordinates specify whether the tiles will be moved horizontally (column-wise)
     * or vertically (row-wise).
     *
     * @param row   the row
     * @param col   the column
     * @param delta the delta coordinates
     * @return the sum of all the tiles' number joined together
     */
    private int moveTiles(int row, int col, Coordinates delta) {
        int points = 0;
        // for every column or row
        for (int steps = 0; steps < DIM; steps++) {
            // get the index of the next non-empty tile to move
            Coordinates tile = getNextTile(row, col, delta);
            // there are no tiles left to move, we are done
            if (tile == null) {
                break;
            } else if (this.board[row][col] == EMPTY || (tile.row() == row && tile.col() == col)) {
                if (this.board[row][col] == EMPTY) {
                    // moving the tile to this empty spot
                    this.board[row][col] = this.board[tile.row()][tile.col()];
                    // clearing that previous location of the tile in the board
                    this.board[tile.row()][tile.col()] = EMPTY;
                    this.hasChanged = true;
                }
                // check whether we can merge this tile with the next one
                int fromRow = tile.row() + delta.row();
                int fromCol = tile.col() + delta.col();
                Coordinates secondTile = getNextTile(fromRow, fromCol, delta);
                if (secondTile != null && this.board[row][col] == this.board[secondTile.row()][secondTile.col()]) {
                    // joining the tiles
                    points += joinTiles(row, col, secondTile.row(), secondTile.col());
                    this.hasChanged = true;
                }
            } else if (this.board[row][col] == this.board[tile.col()][tile.col()]) {
                // if the current position has a tile with the same value, join them
                points += joinTiles(row, col, tile.row(), tile.col());
                this.hasChanged = true;
            } // else, do nothing

            // moving to the next cell
            if (delta.col() == 0) {
                row += delta.row();
            } else {
                col += delta.col();
            }
        }
        return points;
    }

    /**
     * Join the tiles from the given coordinates. The new number of the join operation will be stored in the
     * destination coordinates (destRow, destCol). The other tile (fromRow, fromCol) will be cleared (EMPTY value) from the board.
     *
     * @param destRow the row of the tile which contains the new number after joining both tiles
     * @param destCol the column of the tile which contains the new number after joining both tiles
     * @param fromRow the row of the tile to join with the destination tile.
     * @param fromCol the column of the tile to join with the destination tile.
     * @return the new tile's number after the join operation.
     */
    private int joinTiles(int destRow, int destCol, int fromRow, int fromCol) {
        this.board[destRow][destCol] += this.board[destRow][destCol];
        this.board[fromRow][fromCol] = EMPTY;
        return this.board[destRow][destCol];
    }

    /**
     * Returns the position of the next non-empty tile
     *
     * @param row   the starting row position to look for the non-empty tile
     * @param col   the starting column position to look for the non-empty tile
     * @param delta the delta coordinates to move to the next cell in the board
     * @return the index of the next non-empty tile. If there is non-empty tile, returns null.
     */
    private Coordinates getNextTile(int row, int col, Coordinates delta) {
        Coordinates coor = null;
        while (inBounds(row, col)) {
            if (this.board[row][col] != EMPTY) {
                coor = new Coordinates(row, col);
                break;
            }
            row += delta.row();
            col += delta.col();
        }
        return coor;
    }

    /**
     * Same some model's statistics before shutting down the game.
     */
    public void shutdown() {
    }


    /**
     * Reset the game
     */
    public void newGame() {
        init();
        ready();
    }

    /**
     * Is the game over?
     *
     * @return whether the player has won or lost the game
     */
    public boolean isGameOver() {
        return status.equals(GameStatus.WON) || status.equals(GameStatus.LOST);
    }

    /**
     * A human-readable representation of the model.
     * Displays the entire board plus some game's statistics (e.g. number of moves, score, best score)
     *
     * @return the string representation of the model.
     */
    public String toString() {    
        int TILES_MAX_NUMBER_DIGITS = getMaxNumberDigits();   // <-- dynamic value

        StringBuilder result = new StringBuilder(" ");
        result.append(System.lineSeparator());
        // displaying columns numbers
        result.append("    ");
        for (int col = 0; col < DIM; col++) {
            result.append(String.format("%" + (TILES_MAX_NUMBER_DIGITS + 1) + "d", col));
        }
        result.append(System.lineSeparator());
        result.append("    ");
        for (int col = 0; col < DIM; col++) {
            result.append("_".repeat(TILES_MAX_NUMBER_DIGITS + 1));
        }
        result.append(System.lineSeparator());

        for (int row = 0; row < DIM; ++row) {
            result.append(String.format("%2d |", row));
            for (int col = 0; col < DIM; ++col) {
                int value = this.board[row][col];
                if (value == EMPTY) {
                    result.append(WHITESPACE.repeat(TILES_MAX_NUMBER_DIGITS + 1));
                } else {
                    result.append(String.format("%" + (TILES_MAX_NUMBER_DIGITS + 1) + "d", value));
                }
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    private int getMaxNumberDigits() {
    int max = 0;
    for (int[] row : board) {
        for (int val : row) {
            if (val > max) {
                max = val;
            }
        }
    }
    // At least 1 digit (for the empty tile placeholder)
    return Math.max(1, String.valueOf(max).length());
}

    /**
     * When the model changes, the observers are notified via their update method
     */
    private void notifyObservers(String message) {
        for (Observer<TilesModel, String> obs : this.observers) {
            obs.update(this, message);
        }
    }

    /**
     * Update the score with the given amount. If the score is greater than the best score, then, it updates
     * also the best score.
     *
     * @param amount the amount
     */
    private void updateScore(int amount) {
        this.score += amount;
    }

    /**
     * Generate a new tile in a random empty position at the board.
     * The tile will have a random value from the list of possible initial values (INITIAL_TILE_VALUES)
     */
    private void addNewTile() {
        // choosing randomly a value for the tile from the list of possible values
        int index = this.random.nextInt(1, 101) <= CHANCE_TO_GENERATE_SMALLEST_NUMBER ? 0 : 1;
        int value = INITIAL_TILE_VALUES.get(index);
        // generate random position in the board, the position must be empty
        int row;
        int col;
        do {
            // generating a random number between 0 and 16 (exclusive)
            int position = this.random.nextInt(DIM * DIM);
            // converting the position into a 2d array index
            row = position / DIM;
            col = position % DIM;
        } while (this.board[row][col] != EMPTY);
        // adding the new tile into the board    
        this.board[row][col] = value;
    }

    /**
     * Are these coordinates in bounds?
     *
     * @param row the row coordinate
     * @param col the column coordinate
     * @return whether the coordinates are within the boundaries of the board.
     */
    private boolean inBounds(int row, int col) {
        return 0 <= row && row < DIM && 0 <= col && col < DIM;
    }

    public TilesModel copy() {
        TilesModel newModel = new TilesModel();
        for(int i = 0; i < 4; i++){ 
            for(int j = 0; j < 4; j++) {
                newModel.board[i][j] = board[i][j];
            }
        }
        newModel.score = this.score;
        return newModel;
    }
}
