package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Left.Call;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Phone extends ICommand {

    public Phone(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, sp)
                || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, sp)) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("call")) {
                    if (args.length != 1) {
                        if (args[1].equalsIgnoreCase("accept")) {
                            if (Call.isAccept(sp)) {
                                if (Call.soundMap.containsKey(sp)) {
                                    Call.soundMap.get(sp).cancel();
                                    Call.soundMap.remove(sp);
                                }
                                Player p = Call.acceptCall(sp);

                                TextComponent textComponent1 = new TextComponent();
                                textComponent1.setText(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "からの通話を開始しました。\n");

                                TextComponent textComponent2 = new TextComponent();
                                textComponent2.setText(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + sp.getName() + "との通話を開始しました。\n");

                                TextComponent textComponent3 = new TextComponent();
                                textComponent3.setText(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "通話を終了するには");
                                textComponent1.addExtra(textComponent3);
                                textComponent2.addExtra(textComponent3);
                                TextComponent textComponent4 = new TextComponent();
                                textComponent4.setText(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "ここ");
                                textComponent4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "通話を終了するにはここをクリックしてください。").create()));
                                textComponent4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/phone call end"));
                                textComponent1.addExtra(textComponent4);
                                textComponent2.addExtra(textComponent4);
                                TextComponent textComponent5 = new TextComponent();
                                textComponent5.setText(ChatColor.GRAY + "をクリックしてください。");
                                textComponent1.addExtra(textComponent5);
                                textComponent2.addExtra(textComponent5);

                                sp.spigot().sendMessage(textComponent1);
                                p.spigot().sendMessage(textComponent2);
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "着信がありません。");
                            return;
                        } else if (args[1].equalsIgnoreCase("reject")) {
                            if (Call.isAccept(sp)) {
                                if (Call.soundMap.containsKey(sp)) {
                                    Call.soundMap.get(sp).cancel();
                                    Call.soundMap.remove(sp);
                                }
                                Player p = Call.rejectCall(sp);
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "からの通話を拒否しました。");
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + sp.getName() + "が通話を拒否しました。");
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "着信がありません。");
                            return;
                        } else if (args[1].equalsIgnoreCase("end")) {
                            if (Call.isCall(sp)) {
                                if (Call.soundMap.containsKey(sp)) {
                                    Call.soundMap.get(sp).cancel();
                                    Call.soundMap.remove(sp);
                                }
                                Player p = Call.endCall(sp);
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "との通話を終了しました。");
                                p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + sp.getName() + "との通話を終了しました。");
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "すでに通話が終了しています。");
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            MissionManager.reloadBook(sp);
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "スマートフォンを配布しました。");
            return;
        }
        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンター・通報部隊のため実行できません。");
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
