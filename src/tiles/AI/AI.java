package tiles.AI;

import tiles.model.Direction;
import tiles.model.TilesModel;

/**
 * AI interface to choose a move for 2048.
 */
public interface AI {
    Direction chooseMove(TilesModel model);

    void addData(TilesModel model);
}
