package asokol.kalah.game.implementation;

import asokol.kalah.board.Board;
import asokol.kalah.dto.BoardDTO;
import asokol.kalah.game.Game;
import asokol.kalah.game.GameStatus;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Classic implementation of Kalah game.
 */
public class ClassicGame extends Game {
    private boolean firstPlayerMove;

    public ClassicGame(int pitsNumber, int seedsPerPit) {
        this.board = new Board(pitsNumber, seedsPerPit);
        this.firstPlayerMove = true;
    }

    @Override
    public boolean makeMove(int houseNumber) {
        int cellNumber = calculateCellNumber(houseNumber);
        int seedsNumber = board.getSeedsNumberInCell(cellNumber);
        if (seedsNumber == 0) {
            return false;
        } else {
            int cellWhereStopped = board.sowSeedsCounterClockwise(cellNumber, firstPlayerMove);
            BoardDTO currentState = board.getCurrentState();
            if (firstPlayerMove && cellWhereStopped < board.getFirstKalahIndex()) {
                List<Integer> firstPits = currentState.getFirstPits();
                Integer seedsInLastCell = firstPits.get(cellWhereStopped);
                if (seedsInLastCell == 1) {
                    board.moveSeedsFromTo(cellWhereStopped, board.getFirstKalahIndex());
                    board.moveSeedsFromTo(board.getSecondKalahIndex() - (cellWhereStopped + 1), board.getFirstKalahIndex());
                }
            }
            if (!firstPlayerMove && cellWhereStopped > board.getFirstKalahIndex() && cellWhereStopped < board.getSecondKalahIndex()) {
                List<Integer> secondPits = currentState.getSecondPits();
                Integer seedsInLastCell = secondPits.get(cellWhereStopped - board.getFirstKalahIndex() - 1);
                if (seedsInLastCell == 1) {
                    board.moveSeedsFromTo(cellWhereStopped, board.getSecondKalahIndex());
                    board.moveSeedsFromTo(board.getSecondKalahIndex() - (cellWhereStopped + 1), board.getSecondKalahIndex());
                }
            }
            long firstNonNullPits = currentState.getFirstPits()
                    .stream()
                    .filter(seeds -> seeds != 0)
                    .count();
            long secondNonNullPits = currentState.getSecondPits()
                    .stream()
                    .filter(seeds -> seeds != 0)
                    .count();
            if (firstNonNullPits == 0) {
                IntStream.range(board.getFirstKalahIndex() + 1, board.getSecondKalahIndex())
                        .boxed()
                        .forEach(pit -> board.moveSeedsFromTo(pit, board.getSecondKalahIndex()));
            }
            if (secondNonNullPits == 0) {
                IntStream.range(0, board.getFirstKalahIndex())
                        .boxed()
                        .forEach(pit -> board.moveSeedsFromTo(pit, board.getFirstKalahIndex()));
            }
            if (!isItOwnHouse(cellWhereStopped, firstPlayerMove)) {
                firstPlayerMove ^= true;
            }
        }
        return true;
    }

    /**
     * Calculate the number of cell for board representation.
     *
     * @param houseNumber user input.
     * @return board representation.
     */
    private int calculateCellNumber(int houseNumber) {
        return firstPlayerMove ? houseNumber - 1 : board.getFirstKalahIndex() + houseNumber;
    }

    /**
     * Check if it is player's own house.
     *
     * @param cellNumber      the number of a cell.
     * @param firstPlayerMove flag to check is it first player.
     * @return whether it is player's own house or not.
     */
    private boolean isItOwnHouse(int cellNumber, boolean firstPlayerMove) {
        return firstPlayerMove ? cellNumber == board.getFirstKalahIndex()
                : cellNumber == board.getSecondKalahIndex();
    }

    @Override
    public boolean isFirstPlayerMove() {
        return firstPlayerMove;
    }

    @Override
    public GameStatus getGameStatus() {
        GameStatus gameStatus;
        int numberOfSeeds = board.getNumberOfSeeds();
        int[] seedsInKalahs = board.getSeedsInKalahs();
        if (seedsInKalahs[0] > numberOfSeeds / 2) {
            gameStatus = GameStatus.FIRST_PLAYER_WON;
        } else if (seedsInKalahs[1] > numberOfSeeds / 2) {
            gameStatus = GameStatus.SECOND_PLAYER_WON;
        } else if (seedsInKalahs[0] == numberOfSeeds / 2 && seedsInKalahs[1] == numberOfSeeds / 2) {
            gameStatus = GameStatus.DRAW;
        } else {
            gameStatus = GameStatus.ONGOING;
        }
        return gameStatus;
    }
}
