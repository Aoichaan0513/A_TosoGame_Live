package jp.aoichaan0513.A_TosoGame_Live.API.Manager;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OPGameManager {

    private static boolean isOPGame = false;
    public static boolean isDice = false;

    public static Player player = null;
    public static List<UUID> list = new ArrayList<>();

    public static boolean getOPGame() {
        return isOPGame;
    }

    public static void setOPGame(boolean isOPGame) {
        OPGameManager.isOPGame = isOPGame;
    }

    public static boolean getDice() {
        return isDice;
    }

    public static void setDice(boolean isDice) {
        OPGameManager.isDice = isDice;
    }
}
