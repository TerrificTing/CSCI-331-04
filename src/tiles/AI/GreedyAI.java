package tiles.AI;

import java.io.FileWriter;
import java.io.IOException;
import tiles.model.Direction;
import tiles.model.TilesModel;
import java.util.Random;

public class GreedyAI implements AI {

    @Override
    public Direction chooseMove(TilesModel model) {
        Direction[] dirs = Direction.values();
        Direction move = null;
        int max = -1;
        int score = 0;

        for(Direction dir : dirs) {
            TilesModel current = model.copy();

            current.move(dir);

            if(!current.equals(model)) {
                System.out.println(current);
                System.out.println(current.getScore());
                System.out.println(model);
                System.out.println(model.getScore());
                if(current.getScore() > max) {
                    move = dir;
                    max = current.getScore();
                    score = current.getScore();
                }
            }
        }
        if(move == null || model.getScore() == score){
            Random random = new Random();
            move = dirs[random.nextInt(dirs.length)];
        }
        System.out.println(move);
        return move;
    }

    @Override
    public void addData(TilesModel model) {
        try (FileWriter writer = new FileWriter("greedyai.csv", true)) { // append mode
            writer.write(model.getScore() + "," + model.getMovesMade() + "\n");
        } catch (IOException e) {

        }
    }

    public static void main(String[] args) {
        TilesModel model = new TilesModel();
        GreedyAI greedyAI = new GreedyAI();
        
        while (!model.isGameOver()) {
            Direction move = greedyAI.chooseMove(model);
            model.move(move);
        }
        
        greedyAI.addData(model);

        System.out.println("Game Over! Final Score: " + model.getScore());
    }
}