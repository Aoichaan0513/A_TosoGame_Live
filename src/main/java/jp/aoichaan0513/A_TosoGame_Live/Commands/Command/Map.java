package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.MapInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Maps.MapUtility;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Map extends ICommand {

    public Map(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length != 0) {
                WorldConfig worldConfig = Main.getWorldConfig();

                if (args[0].equalsIgnoreCase("load")) {
                    if (args.length != 1) {
                        if (new File(args[1]).exists()) {
                            if (!Bukkit.getWorlds().contains(args[1])) {
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "マップを読み込んでいます…");
                                WorldManager.setWorld(args[1]);
                                World world = Bukkit.createWorld(new WorldCreator(args[1]));
                                sp.teleport(world.getSpawnLocation());
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "マップを読み込みました。");
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "指定したマップはすでに読み込まれています。");
                            return;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。存在するマップ名を指定してください。");
                        return;
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。マップ名を指定してください。");
                    return;
                } else if (args[0].equalsIgnoreCase("unload")) {
                    if (args.length != 1) {
                        if (new File(args[1]).exists()) {
                            if (Bukkit.getWorlds().contains(args[1])) {
                                if (Bukkit.getWorld(args[1]).getPlayers().size() == 0) {
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "読み込んだマップを破棄しています…");
                                    World world = Bukkit.getWorld("world");
                                    sp.teleport(world.getSpawnLocation());
                                    WorldManager.setWorld("world");
                                    Bukkit.unloadWorld(args[1], true);
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "読み込んだマップを破棄しました。");
                                    return;
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "指定したマップにプレイヤーがいるため読み込みを破棄することができません。");
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "指定したマップはすでに読み込まれていません。");
                            return;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。存在するマップ名を指定してください。");
                        return;
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。マップ名を指定してください。");
                    return;
                } else if (args[0].equalsIgnoreCase("generate")) {
                    if (worldConfig.getConfig().contains(WorldManager.PathType.BORDER_MAP.getPath() + ".p1.x") && worldConfig.getConfig().contains(WorldManager.PathType.BORDER_MAP.getPath() + ".p1.z")
                            && worldConfig.getConfig().contains(WorldManager.PathType.BORDER_MAP.getPath() + ".p2.x") && worldConfig.getConfig().contains(WorldManager.PathType.BORDER_MAP.getPath() + ".p2.z")) {
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "地図を生成しています…\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ラグが発生しますのでご注意ください。");
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "地図を生成しています…");
                        if (MapUtility.generateMap()) {
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "地図の生成が完了しました。");
                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "地図の生成が完了しました。");
                        } else {
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "地図の生成ができませんでした。");
                        }
                        return;
                    } else {
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "マップの設定が完了していないため地図の生成ができませんでした。\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "マップの設定をした後に\"/map generate\"を実行してください。");
                        return;
                    }
                } else if (args[0].equalsIgnoreCase("edit")) {
                    sp.openInventory(MapInventory.getEditInventory());
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "マップ設定を開きました。");
                    return;
                } else if (args[0].equalsIgnoreCase("list")) {
                    sp.openInventory(MapInventory.getListInventory());
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "マップリストを開きました。");
                    return;
                }
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/map load <マップ名>\" - 指定したマップを読み込む\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/map unload <マップ名>\" - 指定したマップの読み込みを破棄\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/map generate\" - マップの地図を生成\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/map edit\" - マップの設定を変更\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/map list\" - マップリストを表示");
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
        if (TosoGameAPI.isAdmin(sp)) {
            if (args.length == 1) {
                if (args[0].length() == 0) {
                    return Arrays.asList("load", "unload", "generate", "edit", "list");
                } else {
                    if ("load".startsWith(args[0])) {
                        return Collections.singletonList("load");
                    } else if ("unload".startsWith(args[0])) {
                        return Collections.singletonList("unload");
                    } else if ("generate".startsWith(args[0])) {
                        return Collections.singletonList("generate");
                    } else if ("edit".startsWith(args[0])) {
                        return Collections.singletonList("edit");
                    } else if ("list".startsWith(args[0])) {
                        return Collections.singletonList("list");
                    }
                }
            } else if (args.length == 2) {
                if (args[1].length() == 0) {
                    if (args[0].equalsIgnoreCase("load")) {
                        ArrayList<String> list = new ArrayList<>();

                        if (Bukkit.getWorldContainer().listFiles() != null) {
                            for (File file : Bukkit.getWorldContainer().listFiles()) {
                                if (file.isDirectory()) {
                                    File configFile = new File(file.getName() + Main.FILE_SEPARATOR + "map.yml");
                                    if (configFile.exists()) {
                                        list.add(file.getName());
                                    }
                                }
                            }
                        }
                        return list;
                    }
                }
            }
        }
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
