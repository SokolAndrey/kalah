package asokol.kalah.board;

import asokol.kalah.dto.BoardDTO;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Board representation of Kalah game board.
 */
public class Board {

    private int[] cells;
    @Getter
    private final int firstKalahIndex;
    @Getter
    private final int secondKalahIndex;
    @Getter
    private final int numberOfSeeds;

    /**
     * Init board with {@code storeNumber} and {@code seedsPerPit} parameters.
     * NOTE: The initial number of seeds in Kalahs is always 0.
     *
     * @param pitsNumber  the number of pits for each player.
     * @param seedsPerPit the number of initial seeds in each pit.
     */
    public Board(int pitsNumber, int seedsPerPit) {
        if (pitsNumber < 0) {
            throw new IllegalArgumentException("Pits number should be greater than 0");
        }
        if (seedsPerPit < 0) {
            throw new IllegalArgumentException("Seeds number per pit should be greater than 0");
        }

        numberOfSeeds = seedsPerPit * pitsNumber * 2;
        cells = new int[pitsNumber * 2 + 2];
        firstKalahIndex = pitsNumber;
        secondKalahIndex = cells.length - 1;
        for (int i = 0; i < cells.length; i++) {
            if (i != firstKalahIndex && i != secondKalahIndex) {
                cells[i] = seedsPerPit;
            }
        }
    }

    /**
     * Get the number of seeds in particular cell.
     * The Kalahs or 'the-end--zone's are considered as cells too.
     *
     * @param cellNumber the number of the particular cell.
     *                   In case the {@code cellNumber} is greater than number of cells or less than 0
     *                   throws an {@link IllegalArgumentException}.
     * @return the number of seeds in particular cell.
     */
    public int getSeedsNumberInCell(int cellNumber) {
        if (cellNumber > cells.length || cellNumber < 0) {
            throw new IllegalArgumentException("There is no cells with number " + cellNumber);
        }
        return cells[cellNumber];
    }

    /**
     * Get current state of the board.
     *
     * @return {@link BoardDTO}, which represents current state of the board.
     */
    public BoardDTO getCurrentState() {
        List<Integer> firstPits = Arrays.stream(cells, 0, firstKalahIndex)
                .boxed()
                .collect(toList());
        List<Integer> secondPits = Arrays.stream(cells, firstKalahIndex + 1, secondKalahIndex)
                .boxed()
                .collect(toList());
        return BoardDTO.builder()
                .firstHouse(cells[firstKalahIndex])
                .secondHouse(cells[secondKalahIndex])
                .firstPits(firstPits)
                .secondPits(secondPits)
                .build();
    }

    /**
     * Moving counter-clockwise, one seed is dropped in each house in turn, including the player's own store
     * but not their opponent's.
     *
     * @param cellNumber the number of the cell from which seeds should be removed.
     * @return the number of the last cell.
     */
    public int sowSeedsCounterClockwise(int cellNumber, boolean firstPlayerMove) {
        if (cellNumber == firstKalahIndex || cellNumber == secondKalahIndex) {
            throw new UnsupportedOperationException("Seeds cannot be removed from the Kalahs");
        }
        int seedsNumber = getSeedsNumberInCell(cellNumber);
        int iter = cellNumber;
        cells[cellNumber] = 0;
        while (seedsNumber > 0) {
            iter = (++iter) % (secondKalahIndex + 1);
            if (iter == secondKalahIndex && firstPlayerMove) {
                // skip opponent's Kalah.
                continue;
            }
            if (iter == firstKalahIndex && !firstPlayerMove) {
                // skip opponent's Kalah.
                continue;
            }
            cells[iter] += 1;
            seedsNumber--;
        }
        return iter;
    }

    /**
     * Get the number of seeds in Kalahs.
     *
     * @return an array of int of size 2, where the 0th element represents the number of seeds in the first
     * player's Kalah nad the 1st element represents the number of seeds in the second player's Kalah.
     */
    public int[] getSeedsInKalahs() {
        int[] seedsInKalahs = new int[2];
        seedsInKalahs[0] = cells[firstKalahIndex];
        seedsInKalahs[1] = cells[secondKalahIndex];
        return seedsInKalahs;
    }

    /**
     * Move seeds from on cell to another.
     *
     * @param from cell number to move from.
     * @param to   cell number to move to.
     */
    public void moveSeedsFromTo(int from, int to) {
        cells[to] += cells[from];
        cells[from] = 0;
    }

}
