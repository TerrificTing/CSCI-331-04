package tiles.AI;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import tiles.model.Direction;
import tiles.model.TilesModel;

/**
 * Simple AI that picks a random valid move.
 */
public class RandomAI implements AI {
    private final Random random = new Random();

    @Override
    public Direction chooseMove(TilesModel model) {
        Direction[] dirs = Direction.values();
        return dirs[random.nextInt(dirs.length)];
    }

    @Override
    public void addData(TilesModel model) {
        try (FileWriter writer = new FileWriter("data/randomai.csv", true)) { // append mode
            writer.write(model.getScore() + "," + model.getMovesMade() + "," + model.maxScore() + "\n");
        } catch (IOException e) {

        }
    }

    public static void play() {
        TilesModel model = new TilesModel(); // create a new game
        RandomAI ai = new RandomAI();         // create AI instance

        while (!model.isGameOver()) {
            Direction move = ai.chooseMove(model);
            model.move(move);
        }
        
        ai.addData(model);

        System.out.println("Game Over! Final Score: " + model.getScore());
    }
    public static void main(String[] args) {
        play();
    }
}