package jp.aoichaan0513.A_TosoGame_Live.API.OPGame;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.OPGameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Dice {

    private static boolean isStart = false;
    private static int count = 0;

    private static final int maxCount = 30;

    private static ArrayList<Player> list = new ArrayList<>();

    public static void start() {
        if (!OPGameManager.getOPGame() && !OPGameManager.getDice()) {
            count = 0;
            isStart = true;
            list = new ArrayList<>();

            OPGameManager.setOPGame(true);
            OPGameManager.setDice(true);
        }
    }

    public static void end() {
        if (OPGameManager.getOPGame() && OPGameManager.getDice()) {
            isStart = false;
            OPGameManager.setOPGame(false);
            OPGameManager.setDice(false);
        }
    }

    public static boolean isStart() {
        return isStart;
    }

    public static void setCount(int c) {
        count = c;
    }

    public static int getCount() {
        return count;
    }

    public static Player getShufflePlayer() {
        Player p = null;

        for (int c = 0; c < 3; c++)
            Collections.shuffle(Main.playerList);

        for (Player player : Main.playerList) {
            if (!list.contains(player)) {
                p = player;
                Main.playerList.remove(player);
                list.add(player);
                break;
            } else {
                for (int c = 0; c < 3; c++)
                    Collections.shuffle(Main.playerList);
                continue;
            }
        }
        return p;
    }

    public static ArrayList<UUID> getSnowballPlayers(int i) {
        ArrayList<UUID> rList = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player))
                rList.add(player.getUniqueId());

        for (int c = 0; c < 3; c++)
            Collections.shuffle(rList);

        ArrayList<UUID> list = new ArrayList<>();
        for (int c = 0; c < i; c++)
            list.add(rList.get(c));

        return list;
    }
}
