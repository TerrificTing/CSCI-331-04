package tiles.AI;

import java.io.FileWriter;
import java.io.IOException;
import tiles.model.Direction;
import tiles.model.TilesModel;

public class MiniMaxAI implements AI {
    public Direction chooseMove(TilesModel model, int depth) {
        // TODO Auto-generated method stub
        if(depth % 2 == 1) {
            // Maximizing player
            Direction[] dirs = Direction.values();
            Direction bestMove = null;
            int maxEval = Integer.MIN_VALUE;

            for(Direction dir : dirs) {
                TilesModel current = model.copy();
                current.move(dir);

                if(!current.equals(model)) {
                    int eval = evaluate(current, depth - 1);
                    if(eval > maxEval) {
                        maxEval = eval;
                        bestMove = dir;
                    }
                }
            }
            return bestMove;
        } else {
            // Minimizing player (simulating random tile addition)
            // This part can be expanded to simulate tile additions
            Direction[] dirs = Direction.values();
            Direction bestMove = null;
            int minEval = Integer.MAX_VALUE;

            for(Direction dir : dirs) {
                TilesModel current = model.copy();
                current.move(dir);

                if(!current.equals(model)) {
                    int eval = evaluate(current, depth - 1);
                    if(eval < minEval) {
                        minEval = eval;
                        bestMove = dir;
                    }
                }
            }
            return bestMove;
        }
    }

    int evaluate(TilesModel model, int depth) {
        if(depth == 0 || model.isGameOver()) {
            return model.getScore();
        }

        // Further evaluation logic can be added here
        return model.getScore();
    }

    @Override
    public Direction chooseMove(TilesModel model) {
        return chooseMove(model, 3); // Default depth of 3
    }
    @Override
    public void addData(TilesModel model) {
        try (FileWriter writer = new FileWriter("minimax.csv", true)) { // append mode
            writer.write(model.getScore() + "," + model.getMovesMade() + "\n");
        } catch (IOException e) {

        }
    }

    public static void main(String[] args) {
        TilesModel model = new TilesModel();
        MiniMaxAI minimaxAI = new MiniMaxAI();
        int depth = 3; // Example depth
        
        while (!model.isGameOver()) {
            Direction move = minimaxAI.chooseMove(model, depth);
            model.move(move);
        }

        minimaxAI.addData(model);                   
        System.out.println("Game Over! Final Score: " + model.getScore());
    }
}