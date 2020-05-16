package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.Listener.MissionStartEvent;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;

public class onMissionStart implements Listener {
    @EventHandler
    public void onMissionStart(MissionStartEvent e) {
        if (hasFile(String.valueOf(e.getId()))) {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("nashorn");
            load(engine, getFile(String.valueOf(e.getId())));
            //スクリプトを実行
            Invocable invocable = (Invocable) engine;
            try {
                invocable.invokeFunction("onMissionStart", (Object) null);
            } catch (NoSuchMethodException | ScriptException err) {
                err.printStackTrace();
            }
            return;
        }
        return;
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
        return new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "scripts" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + fileName + ".js");
    }

    private static boolean hasFile(String fileName) {
        File file = new File("plugins" + Main.FILE_SEPARATOR + "A_TosoGame_Live" + Main.FILE_SEPARATOR + "scripts" + Main.FILE_SEPARATOR + "missions" + Main.FILE_SEPARATOR + fileName + ".js");
        if (file.exists())
            return true;
        else
            return false;
    }
}
