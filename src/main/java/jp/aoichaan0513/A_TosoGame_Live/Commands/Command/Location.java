package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Location extends ICommand {

    public Location(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            WorldConfig worldConfig = Main.getWorldConfig();

            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("opgame") || args[0].equalsIgnoreCase("opg")) {
                    worldConfig.getOPGameLocationConfig().setOPLocation(sp.getLocation());
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "オープニングゲーム地点を設定しました。");
                    return;
                } else if (args[0].equalsIgnoreCase("gopgame") || args[0].equalsIgnoreCase("gopg")) {
                    if (args.length != 1) {
                        try {
                            int i = Integer.parseInt(args[1]);
                            if (i >= 1) {
                                worldConfig.getOPGameLocationConfig().setGOPLocation(i, sp.getLocation());
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "オープニングゲーム集合地点の位置" + i + "を設定しました。");
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                            return;
                        } catch (NumberFormatException e) {
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                            return;
                        }
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location " + args[0] + " <数値>\" - オープニングゲーム集合地点の設定");
                    return;
                } else if (args[0].equalsIgnoreCase("hunter")) {
                    if (args.length != 1) {
                        try {
                            int i = Integer.parseInt(args[1]);
                            if (i >= 1) {
                                worldConfig.getHunterLocationConfig().setLocation(i, sp.getLocation());
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ハンター集合地点の位置" + i + "を設定しました。");
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                            return;
                        } catch (NumberFormatException e) {
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                            return;
                        }
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location " + args[0] + " <数値>\" - ハンター集合地点の設定");
                    return;
                } else if (args[0].equalsIgnoreCase("door")) {
                    if (args.length != 1) {
                        if (args[1].equalsIgnoreCase("open")) {
                            if (args.length != 2) {
                                if (args[2].equalsIgnoreCase("all")) {
                                    worldConfig.getHunterDoorConfig().openHunterDoors();
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "設定したハンターボックスのドアすべてを開きました。");
                                    return;
                                } else {
                                    try {
                                        int i = Integer.parseInt(args[2]);
                                        if (i >= 1) {
                                            if (worldConfig.getConfig().contains(WorldManager.PathType.DOOR_HUNTER.getPath() + ".p" + i)) {
                                                worldConfig.getHunterDoorConfig().openHunterDoor(i);
                                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ハンターボックスのドア" + i + "を開きました。");
                                                return;
                                            }
                                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ハンターボックスのドア" + i + "は設定されていないため開くことができません。");
                                            return;
                                        }
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                        return;
                                    } catch (NumberFormatException e) {
                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                        return;
                                    }
                                }
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location door open <数値>\" - 指定したドアを開く\n" +
                                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location door open all\" - 設定したすべてのドアを開く");
                            return;
                        } else {
                            try {
                                int i = Integer.parseInt(args[1]);
                                if (i >= 1) {
                                    worldConfig.getHunterDoorConfig().setLocation(i, sp.getLocation());
                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ハンターボックスのドア位置" + i + "を設定しました。");
                                    return;
                                }
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                return;
                            } catch (NumberFormatException e) {
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                                return;
                            }
                        }
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location door <数値>\" - ドア位置の設定\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location door open <引数>\" - ドアを開く");
                    return;
                } else if (args[0].equalsIgnoreCase("jail")) {
                    if (args.length != 1) {
                        try {
                            int i = Integer.parseInt(args[1]);
                            if (i >= 1) {
                                worldConfig.getJailLocationConfig().setLocation(i, sp.getLocation());
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "牢獄地点の位置" + i + "を設定しました。");
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                            return;
                        } catch (NumberFormatException e) {
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                            return;
                        }
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location jail <数値>\" - 牢獄地点の設定");
                    return;
                } else if (args[0].equalsIgnoreCase("respawn") || args[0].equalsIgnoreCase("res")) {
                    if (args.length != 1) {
                        try {
                            int i = Integer.parseInt(args[1]);
                            if (i >= 1) {
                                worldConfig.getRespawnLocationConfig().setLocation(i, sp.getLocation());
                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "復活地点の位置" + i + "を設定しました。");
                                return;
                            }
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                            return;
                        } catch (NumberFormatException e) {
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。1以上で数字を指定してください。");
                            return;
                        }
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location " + args[0] + " <数値>\" - 復活地点の設定");
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location opgame\" または \"/location opg\" - オープニングゲーム地点の設定\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location gopgame <数値>\" または \"/location gopg <数値>\" - オープニングゲーム集合地点の設定\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location hunter\" - ハンター集合地点の設定\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location door <引数…>\" - ドア位置の設定\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location jail <数値>\" - 牢獄地点の設定\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location respawn <数値>\" または \"/location res <数値>\" - 復活地点の設定");
                return;
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location opgame\" または \"/location opg\" - オープニングゲーム地点の設定\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location gopgame <数値>\" または \"/location gopg <数値>\" - オープニングゲーム集合地点の設定\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location hunter\" - ハンター集合地点の設定\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location door <引数…>\" - ドア位置の設定\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location jail <数値>\" - 牢獄地点の設定\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/location respawn <数値>\" または \"/location res <数値>\" - 復活地点の設定");
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
                    return Arrays.asList("opgame", "opg", "gopgame", "gopg", "hunter", "door", "jail", "respawn", "res");
                } else {
                    if ("opgame".startsWith(args[0])) {
                        return Collections.singletonList("opgame");
                    } else if ("opg".startsWith(args[0])) {
                        return Collections.singletonList("opg");
                    } else if ("gopgame".startsWith(args[0])) {
                        return Collections.singletonList("gopgame");
                    } else if ("gopg".startsWith(args[0])) {
                        return Collections.singletonList("gopg");
                    } else if ("hunter".startsWith(args[0])) {
                        return Collections.singletonList("hunter");
                    } else if ("door".startsWith(args[0])) {
                        return Collections.singletonList("door");
                    } else if ("jail".startsWith(args[0])) {
                        return Collections.singletonList("jail");
                    } else if ("respawn".startsWith(args[0])) {
                        return Collections.singletonList("respawn");
                    } else if ("res".startsWith(args[0])) {
                        return Collections.singletonList("res");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("door")) {
                    if (args[1].length() == 0) {
                        return Arrays.asList("open");
                    } else {
                        if ("open".startsWith(args[1])) {
                            return Collections.singletonList("open");
                        }
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("door")) {
                    if (args[1].equalsIgnoreCase("open")) {
                        if (args[2].length() == 0) {
                            return Arrays.asList("all");
                        } else {
                            if ("all".startsWith(args[2])) {
                                return Collections.singletonList("all");
                            }
                        }
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
