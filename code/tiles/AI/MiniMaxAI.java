package tiles.AI;

import java.io.FileWriter;
import java.io.IOException;
import tiles.model.Direction;
import tiles.model.TilesModel;

public class MiniMaxAI implements AI {
    public int minimax(TilesModel model, int depth, boolean isMaximizing) {
        if (depth == 0 || model.isGameOver()) {
            return model.getScore();
        }

        Direction[] dirs = Direction.values();
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Direction dir : dirs) {
                TilesModel current = model.copy();
                current.move(dir);
                if (!current.equals(model)) {
                    int eval = minimax(current, depth - 1, false);
                    maxEval = Math.max(maxEval, eval);
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Direction dir : dirs) {
                TilesModel current = model.copy();
                current.move(dir);
                if (!current.equals(model)) {
                    int eval = minimax(current, depth - 1, true);
                    minEval = Math.min(minEval, eval);
                }
            }
            return minEval;
        }
    }

    @Override
    public Direction chooseMove(TilesModel model) {
        int depth = 3;
        int bestScore = Integer.MIN_VALUE;
        Direction bestMove = null;

        for (Direction dir : Direction.values()) {
            TilesModel current = model.copy();
            current.move(dir);

            if (!current.equals(model)) {
                int score = minimax(current, depth - 1, false);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = dir;
                }
            }
        }

        return bestMove;
    }

    @Override
    public void addData(TilesModel model) {
        try (FileWriter writer = new FileWriter("data/minimax.csv", true)) { // append mode
            writer.write(model.getScore() + "," + model.getMovesMade() + "," + model.maxScore() + "\n");
        } catch (IOException e) {

        }
    }

    public static void play() {
        TilesModel model = new TilesModel();
        MiniMaxAI minimaxAI = new MiniMaxAI();

        while (!model.isGameOver()) {
            Direction move = minimaxAI.chooseMove(model);

            if (move == null) {
                break;
            }

            model.move(move);
        }

        minimaxAI.addData(model);
        System.out.println("Game Over! Final Score: " + model.getScore());
    }
    public static void main(String[] args) {
        play();
    }
}