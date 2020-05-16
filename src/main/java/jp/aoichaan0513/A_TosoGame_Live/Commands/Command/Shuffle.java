package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.OPGameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.OPGame.Dice;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Shuffle extends ICommand {

    public Shuffle(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (OPGameManager.player != null) {
            if (OPGameManager.player.getUniqueId() == sp.getUniqueId()) {
                if (!OPGameManager.list.contains(sp.getUniqueId())) {
                    OPGameManager.list.add(sp.getUniqueId());

                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "サイコロを投げました。");

                    WorldConfig worldConfig = Main.getWorldConfig();
                    new BukkitRunnable() {

                        int count = 0;

                        @Override
                        public void run() {
                            if (count < 4) {
                                int result = (int) (Math.random() * 6 + 1);
                                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + result + "…");
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                }
                            } else if (count == 4) {
                                int result = (int) (Math.random() * 6 + 1);

                                Dice.setCount(Dice.getCount() + result);

                                if (Dice.getCount() > (worldConfig.getOPGameConfig().getDiceCount() - 1)) {
                                    if (result == 1) {
                                        Dice.end();
                                        OPGameManager.player = null;
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + result + "が出ました。(" + Dice.getCount() + "/" + worldConfig.getOPGameConfig().getDiceCount() + ")\n" +
                                                MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "1が出たためゲームが開始されました。");
                                        Timer.start(1, worldConfig.getGameConfig().getGame());

                                        for (Player player : Bukkit.getOnlinePlayers())
                                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                    } else {
                                        Dice.end();
                                        OPGameManager.player = null;
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + result + "が出ました。(" + Dice.getCount() + "/" + worldConfig.getOPGameConfig().getDiceCount() + ")\n" +
                                                MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "サイコロの目が合計" + worldConfig.getOPGameConfig().getDiceCount() + "カウント以上出たため、30秒の猶予のあとゲームが開始されます。");
                                        Timer.start(30, worldConfig.getGameConfig().getGame());

                                        for (UUID uuid : Dice.getSnowballPlayers(5)) {
                                            if (Bukkit.getPlayer(uuid) == null) continue;

                                            Player player = Bukkit.getPlayer(uuid);

                                            ItemStack itemStack = new ItemStack(Material.SNOWBALL);
                                            ItemMeta itemMeta = itemStack.getItemMeta();
                                            itemMeta.setDisplayName(ChatColor.GREEN + "雪玉 (移動禁止)");
                                            itemMeta.setLore(Arrays.asList(ChatColor.YELLOW + "ハンターに当てるとその周りに檻を貼り20秒間動けなくします。"));
                                            itemStack.setItemMeta(itemMeta);
                                            player.getInventory().addItem(new ItemStack(Material.SNOWBALL));
                                        }

                                        for (Player player : Bukkit.getOnlinePlayers())
                                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    }
                                } else {
                                    if (result == 1) {
                                        Dice.end();
                                        OPGameManager.player = null;
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + result + "が出ました。(" + Dice.getCount() + "/" + worldConfig.getOPGameConfig().getDiceCount() + ")\n" +
                                                MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "1が出たためゲームが開始されました。");
                                        Timer.start(1, worldConfig.getGameConfig().getGame());

                                        for (Player player : Bukkit.getOnlinePlayers())
                                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                    } else {
                                        if (Main.playerList.size() > 0) {
                                            TosoGameAPI.teleport(sp, worldConfig.getOPGameLocationConfig().getGOPLocations());

                                            Player p = Dice.getShufflePlayer();
                                            OPGameManager.player = p;
                                            p.teleport(worldConfig.getOPGameLocationConfig().getOPLocation());
                                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + result + "が出ました。(" + Dice.getCount() + "/" + worldConfig.getOPGameConfig().getDiceCount() + ")\n" +
                                                    MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "1以外が出たためハンターは放出されません。続行します。\n" +
                                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + p.getName() + "がサイコロを投げます。");
                                            p.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "\"/shuffle\"と入力してサイコロを投げてください。\n" +
                                                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "なお、15秒以内に実行しない場合は自動実行されます。");

                                            for (Player player : Bukkit.getOnlinePlayers())
                                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    if (OPGameManager.player == null || OPGameManager.player.getUniqueId() != p.getUniqueId() || !OPGameManager.list.contains(p.getUniqueId()))
                                                        return;
                                                    p.performCommand("shuffle");
                                                    return;
                                                }
                                            }.runTaskLater(Main.getInstance(), 20 * 15);
                                        } else {
                                            Dice.end();
                                            OPGameManager.player = null;
                                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + result + "が出ました。(" + Dice.getCount() + "/" + worldConfig.getOPGameConfig().getDiceCount() + ")\n" +
                                                    MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "オープニングゲームに参加できるプレイヤーがいないため、30秒の猶予のあとゲームが開始されます。");
                                            Timer.start(30, worldConfig.getGameConfig().getGame());

                                            for (Player player : Bukkit.getOnlinePlayers())
                                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                        }
                                    }
                                }

                                cancel();
                            }

                            count++;
                        }
                    }.runTaskTimer(Main.getInstance(), 20, 20);
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "すでにサイコロを振ったため実行できません。");
                return;
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "現在実行できません。");
            return;
        }
        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "現在実行できません。");
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
