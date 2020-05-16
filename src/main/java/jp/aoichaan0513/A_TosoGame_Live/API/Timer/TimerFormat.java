package jp.aoichaan0513.A_TosoGame_Live.API.Timer;

public class TimerFormat {

    public static String format(int time) {
        int m = 0;
        int s = time;

        if (s >= 60) {
            m = s / 60;
            s = s - m * 60;
        }
        String sm = String.valueOf(m);
        String ss = String.valueOf(s);
        if (m <= 9) sm = "0" + sm;
        if (s <= 9) ss = "0" + ss;
        return sm + ":" + ss;
    }

    public static int formatMin(int time) {
        String t = TimerFormat.format(time);
        return Integer.parseInt(t.split(":")[0].startsWith("0") ? t.split(":")[0].substring(t.split(":")[0].length() - 1) : t.split(":")[0]);
    }

    public static int formatSec(int time) {
        String t = TimerFormat.format(time);
        return Integer.parseInt(t.split(":")[1].startsWith("0") ? t.split(":")[1].substring(t.split(":")[1].length() - 1) : t.split(":")[1]);
    }

    public static String formatJapan(int time) {
        String t = TimerFormat.format(time);
        return t.replace(":", "分") + "秒";
    }
}