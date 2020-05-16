package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.SkinManager;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class Nick extends ICommand {

    private static HashMap<UUID, SkinManager> hashMap = new HashMap<>();

    public Nick(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp) || TosoGameAPI.isBroadCaster(sp)) {
            if (args.length != 0) {
                if (Pattern.matches("^[0-9a-zA-Z_]+$", args[0])) {
                    sp.setDisplayName(args[0]);
                    sp.setPlayerListName(args[0]);

                    if (args.length != 1) {
                        if (Pattern.matches("^[0-9a-zA-Z_]+$", args[1])) {
                            Player target2 = Bukkit.getPlayerExact(args[1]);
                            if (target2 == null) {
                                OfflinePlayer skinPlayer = Bukkit.getOfflinePlayer(args[1]);

                                GameProfile gp = ((CraftPlayer) sp).getProfile();
                                gp.getProperties().clear();
                                SkinManager skin = hashMap.containsKey(skinPlayer.getUniqueId()) ? hashMap.get(skinPlayer.getUniqueId()) : new SkinManager(getUUID(skinPlayer.getUniqueId()));

                                if (!hashMap.containsKey(skinPlayer.getUniqueId()))
                                    hashMap.put(skinPlayer.getUniqueId(), skin);

                                if (skin.getSkinName() != null)
                                    gp.getProperties().put(skin.getSkinName(), new Property(skin.getSkinName(), skin.getSkinValue(), skin.getSkinSignatur()));

                                for (Player player : Bukkit.getOnlinePlayers())
                                    player.hidePlayer(sp);

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (Player player : Bukkit.getOnlinePlayers())
                                            player.showPlayer(sp);
                                    }
                                }.runTaskLater(Main.getInstance(), 5);

                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ニックネーム・スキンを設定しました。");
                                return;
                            } else {
                                Player skinPlayer = Bukkit.getPlayerExact(args[1]);
                                sp.setDisplayName(args[1]);
                                sp.setPlayerListName(args[1]);

                                GameProfile gp = ((CraftPlayer) sp).getProfile();
                                gp.getProperties().clear();
                                SkinManager skin = hashMap.containsKey(skinPlayer.getUniqueId()) ? hashMap.get(skinPlayer.getUniqueId()) : new SkinManager(getUUID(skinPlayer.getUniqueId()));

                                if (!hashMap.containsKey(skinPlayer.getUniqueId()))
                                    hashMap.put(skinPlayer.getUniqueId(), skin);

                                if (skin.getSkinName() != null)
                                    gp.getProperties().put(skin.getSkinName(), new Property(skin.getSkinName(), skin.getSkinValue(), skin.getSkinSignatur()));

                                for (Player player : Bukkit.getOnlinePlayers())
                                    player.hidePlayer(sp);

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (Player player : Bukkit.getOnlinePlayers())
                                            player.showPlayer(sp);
                                    }
                                }.runTaskLater(Main.getInstance(), 5);

                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ニックネーム・スキンを設定しました。");
                                return;
                            }
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。半角英数字で指定してください。");
                        return;
                    } else {
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ニックネームを設定しました。");
                        return;
                    }
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。半角英数字で指定してください。");
                return;
            } else {
                sp.setDisplayName(sp.getName());
                sp.setPlayerListName(sp.getName());

                GameProfile gp = ((CraftPlayer) sp).getProfile();
                gp.getProperties().clear();
                SkinManager skin = hashMap.containsKey(sp.getUniqueId()) ? hashMap.get(sp.getUniqueId()) : new SkinManager(getUUID(sp.getUniqueId()));

                if (!hashMap.containsKey(sp.getUniqueId()))
                    hashMap.put(sp.getUniqueId(), skin);

                if (skin.getSkinName() != null)
                    gp.getProperties().put(skin.getSkinName(), new Property(skin.getSkinName(), skin.getSkinValue(), skin.getSkinSignatur()));

                for (Player player : Bukkit.getOnlinePlayers())
                    player.hidePlayer(sp);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers())
                            player.showPlayer(sp);
                    }
                }.runTaskLater(Main.getInstance(), 5);
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ニックネーム・スキンをリセットしました。");
                return;
            }
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

    public String getUUID(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

}
