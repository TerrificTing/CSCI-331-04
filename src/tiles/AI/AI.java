package tiles.AI;

import tiles.model.TilesModel;
import tiles.model.Direction;

/**
 * AI interface to choose a move for 2048.
 */
public interface AI {
    Direction chooseMove(TilesModel model);
}
