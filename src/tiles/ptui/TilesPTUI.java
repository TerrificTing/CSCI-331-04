package tiles.ptui;

import tiles.model.Direction;
import tiles.model.Observer;
import tiles.model.TilesModel;

import java.util.Scanner;

/**
 * The plain text UI for the game 2048.  This class encapsulates both
 * the View and Controller portions of the MVC architecture.
 *
 * @author CS RIT
 */
public class TilesPTUI implements Observer<TilesModel, String> {
    /**
     * Error message for invalid user input
     */
    private static final String INVALID_INPUT_MSG = "Invalid user input!";
    /**
     * the model
     */
    private TilesModel model;

    /**
     * Create the PTUI.
     *
     * @param level the game's difficulty level
     */
    public TilesPTUI(String level) {
        this.model = new TilesModel();
        initializeView();
    }

    /*
     ******************* THE VIEW SECTION ***************************************
     */

    /**
     * The View initialization adds ourselves as an observer to the Model
     */
    private void initializeView() {
        this.model.addObserver(this);
        // this ready method call must be made at the bottom 
        // of the JavaFX start method
        this.model.ready(); 
        displayHelp();
    }

    /**
     * Display in the standard output the list of available commands
     */
    private void displayHelp() {
        System.out.println();
        System.out.println("Commands available:");
        System.out.println("\tm(ove) {N|S|E|W}    -- move the tile in the given direction");
        System.out.println("\tn(ew)               -- new game");
        System.out.println("\tq(uit)              -- quit the game");
    }

    /**
     * The update for the PTUI prints the board and some other state like current score and number of moves made
     *
     * @param board   the board
     * @param message the message sent from the model to the view to inform about what happened
     */
    @Override
    public void update(TilesModel board, String message) {
        if (message != null && !message.isEmpty()) {
            System.out.printf("Message: %s%n", message);
        }
        System.out.print(this.model);
        System.out.println();
        System.out.printf("Score: %6d, Best Score: %6d   Moves made: %s%n",
                this.model.getScore(), this.model.getBestScore(), this.model.getMovesMade());
    }

    /*
     ******************* THE CONTROLLER SECTION *********************************
     */

    /**
     * The run loop prompts for user input and makes calls into the Model.
     */
    public void run() {
        Scanner in = new Scanner(System.in);
        for (; ; ) {
            System.out.print("> ");
            String line = in.nextLine();
            String[] input = line.split("\\s+");
            if (input.length > 0) {
                if (input[0].startsWith("n")) {
                    this.model.newGame();
                } else if (input[0].startsWith("m")) {
                    if (!this.model.isGameOver()) {
                        if (input.length == 2) {
                            Direction dir = Direction.getDirectionByInitial(input[1].toUpperCase());
                            if (dir != null) {
                                this.model.move(dir);
                            } else {
                                System.out.println(INVALID_INPUT_MSG);
                                displayHelp();
                            }
                        } else {
                            System.out.println(INVALID_INPUT_MSG);
                            displayHelp();
                        }
                    } else {
                        System.out.println(TilesModel.GAME_OVER_MSG);
                    }
                } else if (input[0].startsWith("q")) {
                    this.model.shutdown();
                    break;
                } else {
                    displayHelp();
                }
            }
        }
    }

    /**
     * The main routine.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: TilesPTUI <TEST|EASY|NORMAL>");
            System.exit(0);
        }
        TilesPTUI ptui = new TilesPTUI(args[0].toUpperCase());
        ptui.run();
    }
}
