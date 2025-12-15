package tiles.AI;

import java.io.FileWriter;
import java.io.IOException;
import src.tiles.model.Direction;
import src.tiles.model.TilesModel;

public class GreedyAI implements AI {
    @Override
    public Direction chooseMove(TilesModel model) {
        Direction[] dirs = Direction.values();
        return dirs[random.nextInt(dirs.length)];
    }

    @Override
    public void addData(TilesModel model) {
        try (FileWriter writer = new FileWriter("greedyai.csv", true)) { // append mode
            writer.write(model.getScore() + "," + model.getMovesMade() + "\n");
        } catch (IOException e) {

        }
    }

    public static void main(String[] args) {
        
    }
}