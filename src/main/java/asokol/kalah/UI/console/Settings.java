package asokol.kalah.UI.console;

public enum Settings {
    PITS_SETTINGS,
    SEEDS_SETTINGS,
    NOT_DEFINED;

    public static Settings getEnum(String s) {
        switch (s) {
            case "1":
                return PITS_SETTINGS;
            case "2":
                return SEEDS_SETTINGS;
            default:
                return NOT_DEFINED;
        }
    }
}
