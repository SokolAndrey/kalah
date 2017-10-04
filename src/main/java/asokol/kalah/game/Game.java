package asokol.kalah.game;

import asokol.kalah.board.Board;
import asokol.kalah.dto.BoardDTO;

/**
 * Kalah game.
 * See https://en.wikipedia.org/wiki/Kalah.
 */
public abstract class Game {
    protected Board board;

    /**
     * Get current state in {@link BoardDTO} representation.
     *
     * @return current status.
     */
    public BoardDTO getCurrentState() {
        return board.getCurrentState();
    }

    /**
     * Make a move accordingly to the implementation.
     *
     * @param houseNumber the number of house to make a move from.
     * @return true is it is possible to make a move, otherwise - false.
     */
    public abstract boolean makeMove(int houseNumber);

    /**
     * Check is first player move or not.
     *
     * @return true if it is first player move, otherwise - false.
     */
    public abstract boolean isFirstPlayerMove();

    /**
     * Current game status.
     *
     * @return game status.
     */
    public abstract GameStatus getGameStatus();

}
