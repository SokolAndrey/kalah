package asokol.kalah.game;

/**
 * Represents current state of the game.
 * FIRST_PLAYER_WON         - means that the step has been finished successfully and the first player won the game;
 * SECOND_PLAYER_WON        - means that the step has been finished successfully and the second player won the game;
 * DRAW                     - means draw;
 * ONGOING                  - means that the step hasn't been finished successfully;
 * SOMETHING_WENT_WRONG     - something went wrong, call the police.
 */
public enum GameStatus {
    FIRST_PLAYER_WON,
    SECOND_PLAYER_WON,
    DRAW,
    ONGOING,
    SOMETHING_WENT_WRONG
}
