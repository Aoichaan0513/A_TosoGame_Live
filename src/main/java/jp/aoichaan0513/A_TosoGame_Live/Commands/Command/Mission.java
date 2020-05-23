package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Right.MissionInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Listeners.onInventory;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Mission extends ICommand {

    public Mission(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (TosoGameAPI.isAdmin(sp)) {
            if (GameManager.isGame(GameManager.GameState.GAME)) {
                if (args.length != 0) {
                    if (args[0].equalsIgnoreCase("send")) {
                        if (!MissionManager.isMission()) {
                            if (args.length != 1) {
                                try {
                                    int i = Integer.parseInt(args[1]);
                                    if (MissionManager.hasFile(i)) {
                                        MissionManager.sendFileMission(i, sp);

                                        TosoGameAPI.sendNotificationSound();

                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ミッションを開始しました。");
                                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが通知されました。");

                                        TextComponent textComponent1 = new TextComponent(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY));
                                        TextComponent textComponent2 = new TextComponent("" + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + "ここ" + ChatColor.RESET);
                                        textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu open mission " + ((MissionInventory.getAmount(MissionManager.MissionBookType.MISSION) - 1) * (MissionInventory.getSlot(MissionManager.MissionBookType.MISSION) - 1))));
                                        TextComponent textComponent3 = new TextComponent(ChatColor.GRAY + "をクリックして詳細を確認できます。");
                                        textComponent1.addExtra(textComponent2);
                                        textComponent1.addExtra(textComponent3);

                                        for (Player player : Bukkit.getOnlinePlayers())
                                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                                                player.spigot().sendMessage(textComponent1);
                                        return;
                                    } else {
                                        StringBuffer stringBuffer = new StringBuffer();

                                        File folder = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions");
                                        for (String fileName : folder.list()) {
                                            if (!fileName.endsWith(".txt")) continue;
                                            try {
                                                File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + fileName.substring(0, fileName.length() - 4) + ".txt");
                                                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), System.getProperty("os.name").toLowerCase().startsWith("windows") ? "Shift-JIS" : "UTF-8"));
                                                String line = br.readLine();
                                                br.close();

                                                stringBuffer.append(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ID" + fileName.substring(0, fileName.length() - 4) + ": " + line + "\n");
                                            } catch (IOException err) {
                                                System.out.println(err);
                                            }
                                        }

                                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。そのようなファイルはありません。\n" +
                                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ミッションリスト:\n" +
                                                stringBuffer.toString().trim());
                                        return;
                                    }
                                } catch (NumberFormatException e) {
                                    StringBuffer stringBuffer = new StringBuffer();

                                    File folder = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions");
                                    for (String fileName : folder.list()) {
                                        if (!fileName.endsWith(".txt")) continue;
                                        try {
                                            File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + fileName.substring(0, fileName.length() - 4) + ".txt");
                                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), System.getProperty("os.name").toLowerCase().startsWith("windows") ? "Shift-JIS" : "UTF-8"));
                                            String line = br.readLine();
                                            br.close();

                                            stringBuffer.append(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ID" + fileName.substring(0, fileName.length() - 4) + ": " + line + "\n");
                                        } catch (IOException err) {
                                            System.out.println(err);
                                        }
                                    }

                                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。そのようなファイルはありません。\n" +
                                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ミッションリスト:\n" +
                                            stringBuffer.toString().trim());
                                    return;
                                }
                            } else {
                                StringBuffer stringBuffer = new StringBuffer();

                                File folder = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions");
                                for (String fileName : folder.list()) {
                                    if (!fileName.endsWith(".txt")) continue;
                                    try {
                                        File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + fileName.substring(0, fileName.length() - 4) + ".txt");
                                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), System.getProperty("os.name").toLowerCase().startsWith("windows") ? "Shift-JIS" : "UTF-8"));
                                        String line = br.readLine();
                                        br.close();

                                        stringBuffer.append(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ID" + fileName.substring(0, fileName.length() - 4) + ": " + line + "\n");
                                    } catch (IOException err) {
                                        System.out.println(err);
                                    }
                                }

                                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。そのようなファイルはありません。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ミッションリスト:\n" +
                                        stringBuffer.toString().trim());
                                return;
                            }
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のミッションが実施されているため開始できません。");
                        return;
                    } else if (args[0].equalsIgnoreCase("tutatu")) {
                        if (args.length != 1) {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (int i = 1; i < args.length; i++)
                                stringBuffer.append(args[i].replace("&n", "\n") + " ");

                            TosoGameAPI.sendNotificationSound();

                            MissionManager.sendMission(MissionManager.MissionBookType.TUTATU.getName(), Collections.singletonList(stringBuffer.toString().trim()), MissionManager.MissionBookType.TUTATU, Material.QUARTZ_BLOCK);
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "通達を送信しました。");
                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが通知されました。");

                            TextComponent textComponent1 = new TextComponent(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY));
                            TextComponent textComponent2 = new TextComponent("" + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + "ここ" + ChatColor.RESET);
                            textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu open tutatu " + ((MissionInventory.getAmount(MissionManager.MissionBookType.TUTATU) - 1) * (MissionInventory.getSlot(MissionManager.MissionBookType.TUTATU) - 1))));
                            TextComponent textComponent3 = new TextComponent(ChatColor.GRAY + "をクリックして詳細を確認できます。");
                            textComponent1.addExtra(textComponent2);
                            textComponent1.addExtra(textComponent3);

                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                                    player.spigot().sendMessage(textComponent1);
                            return;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。説明を指定してください。");
                        return;
                    } else if (args[0].equalsIgnoreCase("hint")) {
                        if (args.length != 1) {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (int i = 1; i < args.length; i++)
                                stringBuffer.append(args[i].replace("&n", "\n") + " ");

                            TosoGameAPI.sendNotificationSound();

                            MissionManager.sendMission(MissionManager.MissionBookType.HINT.getName(), Collections.singletonList(stringBuffer.toString().trim()), MissionManager.MissionBookType.HINT, Material.QUARTZ_BLOCK);
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ヒントを送信しました。");
                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが通知されました。");

                            TextComponent textComponent1 = new TextComponent(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY));
                            TextComponent textComponent2 = new TextComponent("" + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + "ここ" + ChatColor.RESET);
                            textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu open hint " + ((MissionInventory.getAmount(MissionManager.MissionBookType.HINT) - 1) * (MissionInventory.getSlot(MissionManager.MissionBookType.HINT) - 1))));
                            TextComponent textComponent3 = new TextComponent(ChatColor.GRAY + "をクリックして詳細を確認できます。");
                            textComponent1.addExtra(textComponent2);
                            textComponent1.addExtra(textComponent3);

                            for (Player player : Bukkit.getOnlinePlayers())
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                                    player.spigot().sendMessage(textComponent1);
                            return;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。説明を指定してください。");
                        return;
                    } else if (args[0].equalsIgnoreCase("chest")) {
                        onInventory.isAllowOpen = !onInventory.isAllowOpen;
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "チェストの開放を" + (onInventory.isAllowOpen ? "" + ChatColor.GREEN + ChatColor.UNDERLINE + "有効" : "" + ChatColor.RED + ChatColor.UNDERLINE + "無効") + ChatColor.RESET + ChatColor.GRAY + "にしました。");
                        return;
                    } else if (args[0].equalsIgnoreCase("end")) {
                        if (MissionManager.isMission()) {
                            MissionManager.endMission();
                            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ミッションを終了しました。");
                            Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが終了しました。");
                            return;
                        }
                        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ミッションが開始されていないため終了できません。");
                        return;
                    }
                    sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission send <ファイルID>\" - ファイルからミッション追加\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission tutatu <説明>\" - 説明から通達追加\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission hint <説明>\" - 説明からヒント追加\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission chest\" - チェストの開放を有効・無効化\n" +
                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission end\" - 実行中のミッションを終了");
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission send <ファイルID>\" - ファイルからミッション追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission tutatu <説明>\" - 説明から通達追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission hint <説明>\" - 説明からヒント追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission chest\" - チェストの開放を有効・無効化\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission end\" - 実行中のミッションを終了");
                return;
            }
            MainAPI.sendMessage(sp, MainAPI.ErrorMessage.NOT_GAME);
            return;
        }
        MainAPI.sendMessage(sp, MainAPI.ErrorMessage.PERMISSIONS);
        return;
    }

    @Override
    public void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args) {
        if (GameManager.isGame(GameManager.GameState.GAME)) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("send")) {
                    if (!MissionManager.isMission()) {
                        if (args.length != 1) {
                            try {
                                int i = Integer.parseInt(args[1]);
                                if (MissionManager.hasFile(i)) {
                                    MissionManager.sendFileMission(i);

                                    TosoGameAPI.sendNotificationSound();

                                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ミッションを開始しました。");
                                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが通知されました。");
                                    return;
                                } else {
                                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。そのようなファイルはありません。\n" +
                                            MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ミッションリスト:");

                                    File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions");
                                    for (String fileName : file.list()) {
                                        if (!fileName.endsWith(".txt")) continue;
                                        try {
                                            File file2 = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + fileName.substring(0, fileName.length() - 4) + ".txt");
                                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file2), System.getProperty("os.name").toLowerCase().startsWith("windows") ? "Shift-JIS" : "UTF-8"));
                                            String line = br.readLine();
                                            br.close();

                                            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ID" + fileName.substring(0, fileName.length() - 4) + ": " + line);
                                        } catch (IOException err) {
                                            System.out.println(err);
                                        }
                                    }
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数字を指定してください。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ミッションリスト:");

                                File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions");
                                for (String fileName : file.list()) {
                                    if (!fileName.endsWith(".txt")) continue;
                                    try {
                                        File file2 = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + fileName.substring(0, fileName.length() - 4) + ".txt");
                                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file2), System.getProperty("os.name").toLowerCase().startsWith("windows") ? "Shift-JIS" : "UTF-8"));
                                        String line = br.readLine();
                                        br.close();

                                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ID" + fileName.substring(0, fileName.length() - 4) + ": " + line);
                                    } catch (IOException err) {
                                        System.out.println(err);
                                    }
                                }
                                return;
                            }
                        }
                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。数字を指定してください。\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ミッションリスト:");

                        File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions");
                        for (String fileName : file.list()) {
                            if (!fileName.endsWith(".txt")) continue;
                            try {
                                File file2 = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + fileName.substring(0, fileName.length() - 4) + ".txt");
                                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file2), System.getProperty("os.name").toLowerCase().startsWith("windows") ? "Shift-JIS" : "UTF-8"));
                                String line = br.readLine();
                                br.close();

                                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ID" + fileName.substring(0, fileName.length() - 4) + ": " + line);
                            } catch (IOException err) {
                                System.out.println(err);
                            }
                        }
                        return;
                    }
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "他のミッションが実施されているため開始できません。");
                    return;
                } else if (args[0].equalsIgnoreCase("tutatu")) {
                    if (args.length != 1) {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 1; i < args.length; i++)
                            stringBuffer.append(args[i].replace("&n", "\n") + " ");

                        TosoGameAPI.sendNotificationSound();

                        MissionManager.sendMission(MissionManager.MissionBookType.TUTATU.getName(), Collections.singletonList(stringBuffer.toString().trim()), MissionManager.MissionBookType.TUTATU, Material.QUARTZ_BLOCK);

                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "通達を送信しました。");
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが通知されました。");
                        return;
                    }
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。説明を指定してください。");
                    return;
                } else if (args[0].equalsIgnoreCase("hint")) {
                    if (args.length != 1) {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 1; i < args.length; i++)
                            stringBuffer.append(args[i].replace("&n", "\n") + " ");

                        TosoGameAPI.sendNotificationSound();

                        MissionManager.sendMission(MissionManager.MissionBookType.HINT.getName(), Collections.singletonList(stringBuffer.toString().trim()), MissionManager.MissionBookType.HINT, Material.QUARTZ_BLOCK);

                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ヒントを送信しました。");
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが通知されました。");
                        return;
                    }
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。説明を指定してください。");
                    return;
                } else if (args[0].equalsIgnoreCase("chest")) {
                    onInventory.isAllowOpen = !onInventory.isAllowOpen;
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "チェストの開放を" + (onInventory.isAllowOpen ? "" + ChatColor.GREEN + ChatColor.UNDERLINE + "有効" : "" + ChatColor.RED + ChatColor.UNDERLINE + "無効") + ChatColor.RESET + ChatColor.GRAY + "にしました。");
                    return;
                } else if (args[0].equalsIgnoreCase("end")) {
                    if (MissionManager.isMission()) {
                        MissionManager.endMission();

                        bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "ミッションを終了しました。");
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ミッションが終了しました。");
                        return;
                    }
                    bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "ミッションが開始されていないため終了できません。");
                    return;
                }
                bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission send <ファイルID>\" - ファイルからミッション追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission tutatu <説明>\" - 説明から通達追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission hint <説明>\" - 説明からヒント追加\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission chest\" - チェストの開放を有効・無効化\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission end\" - 実行中のミッションを終了");
                return;
            }
            bs.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "コマンドの使い方:\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission send <ファイルID>\" - ファイルからミッション追加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission tutatu <説明>\" - 説明から通達追加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission hint <説明>\" - 説明からヒント追加\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission chest\" - チェストの開放を有効・無効化\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "\"/mission end\" - 実行中のミッションを終了");
            return;
        }
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.NOT_GAME);
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
                    return Arrays.asList("send", "tutatu", "hint", "chest", "end");
                } else {
                    if ("send".startsWith(args[0])) {
                        return Collections.singletonList("send");
                    } else if ("tutatu".startsWith(args[0])) {
                        return Collections.singletonList("tutatu");
                    } else if ("hint".startsWith(args[0])) {
                        return Collections.singletonList("hint");
                    } else if ("chest".startsWith(args[0])) {
                        return Collections.singletonList("chest");
                    } else if ("end".startsWith(args[0])) {
                        return Collections.singletonList("end");
                    }
                }
            } else if (args.length == 2) {
                if (args[1].length() == 0) {
                    if (args[0].equalsIgnoreCase("send")) {
                        ArrayList<String> list = new ArrayList<>();

                        File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions");
                        for (String fileName : file.list()) {
                            if (!fileName.endsWith(".txt")) continue;
                            list.add(fileName.substring(0, fileName.length() - 4));
                        }
                        return list;
                    }
                } else {
                    if (args[0].equalsIgnoreCase("send")) {
                        ArrayList<String> list = new ArrayList<>();

                        if (list.isEmpty()) {
                            File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions");
                            for (String fileName : file.list()) {
                                if (!fileName.endsWith(".txt")) continue;
                                list.add(fileName.substring(0, fileName.length() - 4));
                            }
                            for (String str : list) {
                                if (str.startsWith(args[1])) {
                                    return Collections.singletonList(str);
                                }
                            }
                        } else {
                            for (String str : list) {
                                if (str.startsWith(args[1])) {
                                    return Collections.singletonList(str);
                                }
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
        if (args.length == 1) {
            if (args[0].length() == 0) {
                return Arrays.asList("send", "tutatu", "hint", "chest", "end");
            } else {
                if ("send".startsWith(args[0])) {
                    return Collections.singletonList("send");
                } else if ("tutatu".startsWith(args[0])) {
                    return Collections.singletonList("tutatu");
                } else if ("hint".startsWith(args[0])) {
                    return Collections.singletonList("hint");
                } else if ("chest".startsWith(args[0])) {
                    return Collections.singletonList("chest");
                } else if ("end".startsWith(args[0])) {
                    return Collections.singletonList("end");
                }
            }
        } else if (args.length == 2) {
            if (args[1].length() == 0) {
                if (args[0].equalsIgnoreCase("send")) {
                    ArrayList<String> list = new ArrayList<>();

                    File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "missions");
                    for (String fileName : file.list()) {
                        if (fileName.endsWith(".txt")) {
                            list.add(fileName.substring(0, fileName.length() - 4));
                        }
                    }
                    return list;
                }
            }
        }
        return null;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        return null;
    }
}
