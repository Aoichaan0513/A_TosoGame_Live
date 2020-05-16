package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Code extends ICommand {

    public Code(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, sp)) {
            if (MissionManager.isMission() && MissionManager.getMission() == MissionManager.MissionType.MISSION_HUNTER_ZONE) {
                if (args.length != 0) {
                    if (!HunterZone.codeList.contains(sp)) {
                        if (HunterZone.code.equals(args[0])) {
                            HunterZone.codeList.add(sp);

                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "認証に成功しました。");
                            return;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コードが違います。");
                        return;
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "すでに認証しています。");
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。コードを入力してください。");
                return;
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンターゾーンミッションが実行されていないため実行できません。");
            return;
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
        return;
    }

    @Override
    public void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.NOT_PLAYER);
        return;
    }

    @Override
    public void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER);
        return;
    }

    @Override
    public List<String> onPlayerTabComplete(Player sp, Command cmd, String alias, String[] args) {
        return null;
    }

    @Override
    public List<String> onBlockTabComplete(BlockCommandSender bs, Command cmd, String alias, String[] args) {
        return null;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        return null;
    }
}
