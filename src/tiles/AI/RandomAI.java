package tiles.AI;

import tiles.model.Direction;
import tiles.model.TilesModel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

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

        public static void main(String[] args) {
        TilesModel model = new TilesModel(); // create a new game
        RandomAI ai = new RandomAI();         // create AI instance

        while (!model.isGameOver()) {
            Direction move = ai.chooseMove(model);
            model.move(move);
            System.out.println(model);                   // print the board
            System.out.println("Score: " + model.getScore() + "\n");
        }
        
        try (FileWriter writer = new FileWriter("randomai.csv", true)) { // append mode
            writer.write(model.getScore() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Game Over! Final Score: " + model.getScore());
    }
}