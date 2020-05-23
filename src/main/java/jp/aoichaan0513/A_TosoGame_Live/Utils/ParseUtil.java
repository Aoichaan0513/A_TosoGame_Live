package jp.aoichaan0513.A_TosoGame_Live.Utils;

import java.net.MalformedURLException;
import java.net.URL;

public class ParseUtil {


    public static boolean isURL(String str) {
        try {
            new URL(str);
            return true;
        } catch (MalformedURLException err) {
            return false;
        }
    }

    public static URL toURL(String str) {
        try {
            return new URL(str);
        } catch (MalformedURLException err) {
            return null;
        }
    }

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException err) {
            return false;
        }
    }

    public static int toInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException err) {
            return 0;
        }
    }

    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException err) {
            return false;
        }
    }

    public static long toLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException err) {
            return 0L;
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException err) {
            return false;
        }
    }

    public static double toDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException err) {
            return 0D;
        }
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException err) {
            return false;
        }
    }

    public static float toFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException err) {
            return 0f;
        }
    }
}
