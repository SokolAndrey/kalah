package asokol.kalah;

import asokol.kalah.UI.KalahUI;
import asokol.kalah.UI.console.KalahUIConsoleImpl;

public class MainClass {
    public static void main(final String... args) {
        // TODO(asokol): 10/3/17 Add different implementation and possibility to start it in different mode.
        KalahUI UI = new KalahUIConsoleImpl();
        UI.start();
    }
}
