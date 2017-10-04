package asokol.kalah.UI.console;

import asokol.kalah.UI.KalahUI;
import asokol.kalah.dto.BoardDTO;
import asokol.kalah.game.GameStatus;
import asokol.kalah.game.implementation.ClassicGame;
import lombok.Setter;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Console implementation of {@link KalahUI}.
 */
public class KalahUIConsoleImpl extends KalahUI {
    private final static int DEFAULT_PITS_NUMBER = 6;
    private final static int DEFAULT_SEEDS_NUMBER = 4;
    private final static int DELAY_IN_SEC = 2;
    private Scanner sc;
    @Setter
    private int pitsNumber;
    @Setter
    private int seedsPerPit;

    public KalahUIConsoleImpl() {
        sc = new Scanner(System.in);
        pitsNumber = DEFAULT_PITS_NUMBER;
        seedsPerPit = DEFAULT_SEEDS_NUMBER;
    }

    public void start() {
        while (true) {
            clearScreen();
            GameType gameType = menu();
            switch (gameType) {
                case CLASSIC:
                    game = new ClassicGame(pitsNumber, seedsPerPit);
                    playGame();
                    clearScreen();
                    break;
                case SETTINGS:
                    settings();
                    break;
                case EXIT:
                    System.exit(0);
                case NOT_DEFINED:
                default:
                    printWrongInput();
            }
        }
    }

    private void printWrongInput() {
        clearScreen();
        System.out.println("Wrong input\n\n\n");
        try {
            TimeUnit.SECONDS.sleep(DELAY_IN_SEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearScreen();
    }

    private void playGame() {
        GameStatus gameStatus = game.getGameStatus();
        while (gameStatus == GameStatus.ONGOING) {
            int move = -1;
            while (move == -1) {
                clearScreen();
                drawBoard(game.getCurrentState());
                move = getUserMove(move);
            }
            if (!game.makeMove(move)) {
                System.out.println("Incorrect move. Try another one.");
                try {
                    TimeUnit.SECONDS.sleep(DELAY_IN_SEC);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameStatus = game.getGameStatus();
        }
        switch (gameStatus) {
            case FIRST_PLAYER_WON:
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("=================================");
                System.out.println("     PLAYER 1 WON THE GAME       ");
                System.out.println("=================================");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                break;
            case SECOND_PLAYER_WON:
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("=================================");
                System.out.println("     PLAYER 2 WON THE GAME       ");
                System.out.println("=================================");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                break;
            case DRAW:
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("=================================");
                System.out.println("             DRAW                ");
                System.out.println("=================================");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                break;
        }

        try {
            TimeUnit.SECONDS.sleep(DELAY_IN_SEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private int getUserMove(int move) {
        try {
            String userAction = sc.next();
            move = Integer.parseInt(userAction);
            if (move < 1 || move > pitsNumber) {
                move = -1;
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            clearScreen();
            System.out.println("Insert the number of pit in range from 1 to " + pitsNumber + "\n\n\n");
            try {
                TimeUnit.SECONDS.sleep(DELAY_IN_SEC);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        return move;
    }

    private GameType menu() {
        System.out.println("ΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩ\n");
        System.out.println("            Pits #          " + pitsNumber + " ");
        System.out.println("            Seeds/pit #     " + seedsPerPit + " ");
        System.out.println();
        System.out.println("Chose game type:");
        System.out.println("Insert 1 to chose Classic Game");
        System.out.println("Insert 42 to open Settings");
        System.out.println("Insert 0 to exit\n\n\n");
        System.out.println("ΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩ\n\n\n\n\n\n");
        String response = sc.next();
        return GameType.getEnum(response);
    }

    private void settings() {
        clearScreen();
        System.out.println("ΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩ\n\n\n");
        System.out.println("Chose game type:");
        System.out.println("Insert 1 to change pits  number");
        System.out.println("Insert 2 to change seeds number");
        System.out.println("Insert 0 to exit\n\n\n");
        System.out.println("ΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩΩ\n\n\n\n\n\n");
        Settings response = Settings.getEnum(sc.next());
        switch (response) {
            case PITS_SETTINGS:
                changePits();
                break;
            case SEEDS_SETTINGS:
                changeSeeds();
                break;
            case NOT_DEFINED:

            default:
                break;
        }
    }

    private void changePits() {
        System.out.println("Insert the number of pits:");
        String userInput = sc.next();
        try {
            setPitsNumber(Integer.parseInt(userInput));
        } catch (NumberFormatException ex) {
            printWrongInput();
        }
    }

    private void changeSeeds() {
        System.out.println("Insert the number of seeds:");
        String userInput = sc.next();
        try {
            setSeedsPerPit(Integer.parseInt(userInput));
        } catch (NumberFormatException ex) {
            printWrongInput();
        }
    }

    private void drawBoard(BoardDTO currentState) {
        if (game.isFirstPlayerMove()) {
            System.out.println("∞∞∞ FIRST PLAYER MOVE ∞∞∞");
        } else {
            System.out.println("∞∞∞ SECOND PLAYER MOVE ∞∞∞");
        }
        System.out.println("+------------------+");
        drawSecondKalah(currentState);
        List<Integer> firstPits = currentState.getFirstPits();
        List<Integer> secondPits = currentState.getSecondPits();
        for (int i = 0; i < pitsNumber; i++) {
            Integer firstSeeds = firstPits.get(i);
            Integer secondSeeds = secondPits.get(pitsNumber - i - 1);

            System.out.println("|   +--+    +--+   |");
            if (firstSeeds / 10 > 0 && secondSeeds / 10 > 0) {
                System.out.println("| " + (i + 1) + " |" + firstSeeds + "|    |" + secondSeeds + "| " + (pitsNumber - i) + " |");
            } else if (firstSeeds / 10 > 0 && secondSeeds / 10 == 0) {
                System.out.println("| " + (i + 1) + " |" + firstSeeds + "|    | " + secondSeeds + "| " + (pitsNumber - i) + " |");
            } else if (firstSeeds / 10 == 0 && secondSeeds / 10 > 0) {
                System.out.println("| " + (i + 1) + " | " + firstSeeds + "|    |" + secondSeeds + "| " + (pitsNumber - i) + " |");
            } else {
                System.out.println("| " + (i + 1) + " | " + firstSeeds + "|    | " + secondSeeds + "| " + (pitsNumber - i) + " |");
            }
            System.out.println("|   +--+    +--+   |");
            if (i != pitsNumber - 1)
                System.out.println("|                  |");
        }
        drawFirstKalah(currentState);
        System.out.println("+------------------+");
        System.out.println("Insert the pit number: ");
    }

    private void drawFirstKalah(BoardDTO currentState) {
        System.out.println("|  Ω--------Ω      |");
        if (currentState.getFirstHouse() / 10 > 0) {
            System.out.println("|  |   " + currentState.getFirstHouse() + "   |      |");
        } else {
            System.out.println("|  |    " + currentState.getFirstHouse() + "   |      |");
        }
        System.out.println("|  Ω--------Ω      |");
    }

    private void drawSecondKalah(BoardDTO currentState) {
        System.out.println("|      Ω--------Ω  |");
        if (currentState.getSecondHouse() / 10 > 0) {
            System.out.println("|      |   " + currentState.getSecondHouse() + "   |  |");
        } else {
            System.out.println("|      |    " + currentState.getSecondHouse() + "   |  |");
        }
        System.out.println("|      Ω--------Ω  |");
    }

    private void clearScreen() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
}
