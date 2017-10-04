package asokol.kalah.game.implementation;

import asokol.kalah.board.Board;
import asokol.kalah.dto.BoardDTO;
import asokol.kalah.game.GameStatus;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;

public class ClassicGameTest {
    @Test
    public void makeMove() throws Exception {
        ClassicGame classicGame = new ClassicGame(4, 4);
        BoardDTO beforeMove = classicGame.getCurrentState();
        int sumBeforeMove = beforeMove.getFirstHouse() +
                beforeMove.getSecondHouse() +
                beforeMove.getFirstPits().stream().mapToInt(Integer::intValue).sum() +
                beforeMove.getSecondPits().stream().mapToInt(Integer::intValue).sum();
        classicGame.makeMove(2);
        BoardDTO afterMove = classicGame.getCurrentState();
        int sumAfterMove = beforeMove.getFirstHouse() +
                beforeMove.getSecondHouse() +
                beforeMove.getFirstPits().stream().mapToInt(Integer::intValue).sum() +
                beforeMove.getSecondPits().stream().mapToInt(Integer::intValue).sum();
        Assert.assertThat("The house before move is not empty", beforeMove.getFirstHouse(), is(0));
        Assert.assertThat("The house after move should contain 1 seed", afterMove.getFirstHouse(), is(1));
        Assert.assertEquals("The sum of seeds before and after move should be the same", sumBeforeMove, sumAfterMove);
    }

    @Test
    public void makeMoveAndStopAtFirstPlayersOwnHouse() throws Exception {
        ClassicGame classicGame = new ClassicGame(4, 4);
        BoardDTO beforeMove = classicGame.getCurrentState();
        boolean firstPlayerMoveBeforeMove = classicGame.isFirstPlayerMove();
        classicGame.makeMove(1);
        boolean firstPlayerMoveAfterMove = classicGame.isFirstPlayerMove();
        BoardDTO afterMove = classicGame.getCurrentState();
        Assert.assertThat("The house before move is not empty", beforeMove.getFirstHouse(), is(0));
        Assert.assertThat("The house after move should contain 1 seed", afterMove.getFirstHouse(), is(1));
        Assert.assertEquals("When the move finished at player's own house, he should make additional move",
                firstPlayerMoveBeforeMove, firstPlayerMoveAfterMove);
    }

    @Test
    public void makeMoveAndStopAtSecondPlayersOwnHouse() throws Exception {
        ClassicGame classicGame = new ClassicGame(4, 2);
        BoardDTO beforeMove = classicGame.getCurrentState();
        classicGame.makeMove(1);
        boolean firstPlayerMoveBeforeMove = classicGame.isFirstPlayerMove();
        classicGame.makeMove(3);
        boolean firstPlayerMoveAfterMove = classicGame.isFirstPlayerMove();
        BoardDTO afterMove = classicGame.getCurrentState();
        Assert.assertThat("The house before move is not empty", beforeMove.getSecondHouse(), is(0));
        Assert.assertThat("The house after move should contain 1 seed", afterMove.getSecondHouse(), is(1));
        Assert.assertEquals("When the move finished at player's own house, he should make additional move",
                firstPlayerMoveBeforeMove, firstPlayerMoveAfterMove);
    }

    @Test
    public void makeMoveAndStopAtFirstPlayesOwnEmptyPit() throws Exception {
        ClassicGame classicGame = new ClassicGame(4, 2);
        Field board = classicGame.getClass().getSuperclass().getDeclaredField("board");
        board.setAccessible(true);
        Board boardObject = (Board) board.get(classicGame);
        boardObject.moveSeedsFromTo(3, 4);
        BoardDTO beforeMove = classicGame.getCurrentState();
        classicGame.makeMove(2);
        BoardDTO afterMove = classicGame.getCurrentState();
        Assert.assertThat("The house before move has 2 seeds", beforeMove.getFirstHouse(), is(2));
        Assert.assertThat("The house after move should contain 5 seed", afterMove.getFirstHouse(), is(5));
        Assert.assertThat("First player's 4th pit should be empty before move", beforeMove.getFirstPits().get(3), is(0));
        Assert.assertThat("Second player's 1nd pit should contain 2 seeds before move", beforeMove.getSecondPits().get(0), is(2));
        Assert.assertThat("First player's 4th pit should be empty after move", afterMove.getFirstPits().get(3), is(0));
        Assert.assertThat("Second player's 1nd pit should be empty after move", afterMove.getSecondPits().get(0), is(0));
    }

    @Test
    public void makeMoveAndStopAtSecondPlayesOwnEmptyPit() throws Exception {
        ClassicGame classicGame = new ClassicGame(4, 2);
        Field board = classicGame.getClass().getSuperclass().getDeclaredField("board");
        board.setAccessible(true);
        Board boardObject = (Board) board.get(classicGame);
        boardObject.moveSeedsFromTo(7, 8);
        classicGame.makeMove(1);
        BoardDTO beforeMove = classicGame.getCurrentState();
        classicGame.makeMove(1);
        BoardDTO afterMove = classicGame.getCurrentState();
        Assert.assertThat("The house before move has 2 seeds", beforeMove.getSecondHouse(), is(0));
        Assert.assertThat("The house after move should contain 4 seeds", afterMove.getSecondHouse(), is(4));
        Assert.assertThat("Second player's 3rd pit should be empty before move", beforeMove.getSecondPits().get(2), is(0));
        Assert.assertThat("First player's 2nd pit should contain 3 seeds before move", beforeMove.getFirstPits().get(1), is(3));
        Assert.assertThat("First player's 2nd pit should be empty after move", afterMove.getFirstPits().get(1), is(0));
        Assert.assertThat("Second player's 3rd pit should be empty after move", afterMove.getSecondPits().get(2), is(0));
    }

    @Test
    public void isFirstPlayerMoveShouldChangeValueAfterCommonMove() throws Exception {
        ClassicGame classicGame = new ClassicGame(4, 4);
        boolean firstPlayerMoveBeforeMove = classicGame.isFirstPlayerMove();
        classicGame.makeMove(2);
        boolean firstPlayerMoveAfterMove = classicGame.isFirstPlayerMove();
        Assert.assertNotEquals("When the move finished NOT at player's own house, he should finished his move",
                firstPlayerMoveBeforeMove, firstPlayerMoveAfterMove);
    }

    @Test
    public void getGameStatusDraw() throws Exception {
        ClassicGame classicGame = new ClassicGame(1, 1);
        GameStatus gameStatusBeforeMove = classicGame.getGameStatus();
        classicGame.makeMove(1);
        GameStatus gameStatusAfterMove = classicGame.getGameStatus();
        Assert.assertThat("When the game is not finished status should be ONGOING", gameStatusBeforeMove, is(GameStatus.ONGOING));
        Assert.assertThat("When the game finished as a draw, status should be DRAW", gameStatusAfterMove, is(GameStatus.DRAW));
    }

    @Test
    public void getGameStatusFirstPlayerWon() throws Exception {
        ClassicGame classicGame = new ClassicGame(2, 1);
        GameStatus gameStatusBeforeMove = classicGame.getGameStatus();
        classicGame.makeMove(2);
        classicGame.makeMove(1);
        GameStatus gameStatusAfterMove = classicGame.getGameStatus();
        Assert.assertThat("When the game is not finished status should be ONGOING", gameStatusBeforeMove, is(GameStatus.ONGOING));
        Assert.assertThat("When the first player won the game, status should be FIRST_PLAYER_WON", gameStatusAfterMove, is(GameStatus.FIRST_PLAYER_WON));
    }

    @Test
    public void getGameStatusSecondPlayerWon() throws Exception {
        ClassicGame classicGame = new ClassicGame(2, 2);
        GameStatus gameStatusBeforeMove = classicGame.getGameStatus();
        classicGame.makeMove(1);
        classicGame.makeMove(2);
        GameStatus gameStatusAfterMove = classicGame.getGameStatus();
        Assert.assertThat("When the game is not finished status should be ONGOING", gameStatusBeforeMove, is(GameStatus.ONGOING));
        Assert.assertThat("When the first player won the game, status should be SECOND_PLAYER_WON", gameStatusAfterMove, is(GameStatus.SECOND_PLAYER_WON));
    }

    @Test
    public void makeAMoveFromEmptyPitIsForbiden() throws Exception {
        ClassicGame classicGame = new ClassicGame(4, 2);
        Field board = classicGame.getClass().getSuperclass().getDeclaredField("board");
        board.setAccessible(true);
        Board boardObject = (Board) board.get(classicGame);
        boardObject.moveSeedsFromTo(3, 4);
        BoardDTO stateBeforeMove = classicGame.getCurrentState();
        boolean firstPlayerMoveBefore = classicGame.isFirstPlayerMove();
        boolean moveRes = classicGame.makeMove(4);
        boolean firstPlayerMoveAfter = classicGame.isFirstPlayerMove();
        BoardDTO stateAfterMove = classicGame.getCurrentState();
        Assert.assertFalse("Move result should false", moveRes);
        Assert.assertEquals("Player should continue the move after wrong try", firstPlayerMoveBefore, firstPlayerMoveAfter);
        Assert.assertEquals("State shouldn't change after wrong move", stateBeforeMove, stateAfterMove);
    }

    @Test
    public void whenThereAreNoSeedsAtSecondPlayersPits() throws Exception {
        ClassicGame classicGame = new ClassicGame(2, 1);
        Field board = classicGame.getClass().getSuperclass().getDeclaredField("board");
        board.setAccessible(true);
        Board boardObject = (Board) board.get(classicGame);
        boardObject.moveSeedsFromTo(3, 1);
        classicGame.makeMove(1);
        classicGame.makeMove(2);
        BoardDTO stateAfterMove = classicGame.getCurrentState();
        GameStatus gameStatus = classicGame.getGameStatus();
        Assert.assertThat(stateAfterMove.getFirstHouse(), is(3));
        Assert.assertThat(stateAfterMove.getSecondHouse(), is(1));
        Assert.assertThat("First player should won the game", gameStatus, is(GameStatus.FIRST_PLAYER_WON));
    }
}