package asokol.kalah.UI.console;

public enum GameType {
    CLASSIC,
    EXIT,
    SETTINGS,
    NOT_DEFINED;

    public static GameType getEnum(String s) {
        switch (s) {
            case "1":
                return CLASSIC;
            case "42":
                return SETTINGS;
            case "0":
                return EXIT;
            default:
                return NOT_DEFINED;
        }
    }
}
