package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Script extends ICommand {

    public Script(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        WorldConfig worldConfig = Main.getWorldConfig();

        if (worldConfig.getGameConfig().getScript()) {
            if (args.length != 0) {
                if (hasFile(args[0])) {
                    String[] strs = null;
                    if (args.length > 1) {
                        StringBuffer stringBuffer = new StringBuffer();

                        for (int i = 1; i < args.length; i++)
                            stringBuffer.append(args[i] + " ");

                        strs = stringBuffer.toString().split(" ");
                    }

                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine engine = manager.getEngineByName("nashorn");
                    load(engine, getFile(args[0]));
                    //スクリプトを実行
                    Invocable invocable = (Invocable) engine;
                    try {
                        if (strs == null)
                            invocable.invokeFunction("onCommand", sp, new String[0]);
                        else
                            invocable.invokeFunction("onCommand", sp, strs);
                    } catch (NoSuchMethodException | ScriptException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。そのようなスクリプトはありません。");
                return;
            }
            sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "引数が不正です。スクリプト名を指定してください。");
            return;
        }
        sp.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "管理者によってスクリプト機能が無効にされています。");
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
        WorldConfig worldConfig = Main.getWorldConfig();
        if (!worldConfig.getGameConfig().getScript()) return null;

        if (args.length == 1) {
            if (args[0].length() == 0) {
                ArrayList<String> list = new ArrayList<>();

                File file = new File(Main.getInstance().getDataFolder() + Main.FILE_SEPARATOR + "scripts" + Main.FILE_SEPARATOR + "commands");
                for (String fileName : file.list())
                    if (fileName.endsWith(".js"))
                        list.add(fileName.substring(0, fileName.length() - 3));
                return list;
            } else {
                ArrayList<String> list = new ArrayList<>();

                if (list.isEmpty()) {
                    File file = new File(Main.getInstance().getDataFolder() + Main.FILE_SEPARATOR + "scripts" + Main.FILE_SEPARATOR + "commands");
                    for (String fileName : file.list())
                        if (fileName.endsWith(".js"))
                            list.add(fileName.substring(0, fileName.length() - 3));
                    for (String str : list)
                        if (str.startsWith(args[0]))
                            return Collections.singletonList(str);
                } else {
                    for (String str : list)
                        if (str.startsWith(args[0]))
                            return Collections.singletonList(str);
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

    private static void load(ScriptEngine engine, File jsFile) {
        try {
            String buffer;
            String out = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsFile), "UTF-8"));
            while ((buffer = reader.readLine()) != null) out += (buffer + "\n");
            reader.close();
            engine.eval(out);
        } catch (IOException | ScriptException ioe) {
            ioe.printStackTrace();
        }
    }

    private static File getFile(String fileName) {
        return new File(Main.getInstance().getDataFolder() + Main.FILE_SEPARATOR + "scripts" + Main.FILE_SEPARATOR + "commands" + Main.FILE_SEPARATOR + fileName + ".js");
    }

    public static boolean hasFile(String fileName) {
        File file = new File(Main.getInstance().getDataFolder() + Main.FILE_SEPARATOR + "scripts" + Main.FILE_SEPARATOR + "commands" + Main.FILE_SEPARATOR + fileName + ".js");
        return file.exists();
    }
}
