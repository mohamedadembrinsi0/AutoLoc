package tn.esprit.autoloc.autolocapi;

public final class Utils {
    private Utils() { }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.isBlank();
    }
}
