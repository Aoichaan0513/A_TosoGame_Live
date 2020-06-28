package jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft

import jp.aoichaan0513.A_TosoGame_Live.API.Listener.MissionEndEvent
import jp.aoichaan0513.A_TosoGame_Live.API.Listener.MissionStartEvent
import jp.aoichaan0513.A_TosoGame_Live.Commands.Command.Script
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import javax.script.Invocable
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class onMission : Listener {

    @EventHandler
    fun onMissionStart(e: MissionStartEvent) {
        if (Script.hasFile(e.id.toString())) {
            val manager = ScriptEngineManager()
            val engine = manager.getEngineByName("nashorn")
            Script.load(engine, Script.getFile(e.id.toString()))

            val invocable = engine as Invocable
            try {
                invocable.invokeFunction("onMissionStart", null as Any?)
            } catch (err: NoSuchMethodException) {
                err.printStackTrace()
            } catch (err: ScriptException) {
                err.printStackTrace()
            }
            return
        }
        return
    }

    @EventHandler
    fun onMissionEnd(e: MissionEndEvent) {
        if (Script.hasFile(e.id.toString())) {
            val manager = ScriptEngineManager()
            val engine = manager.getEngineByName("nashorn")
            Script.load(engine, Script.getFile(e.id.toString()))
            //スクリプトを実行
            val invocable = engine as Invocable
            try {
                invocable.invokeFunction("onMissionEnd", null as Any?)
            } catch (err: NoSuchMethodException) {
                err.printStackTrace()
            } catch (err: ScriptException) {
                err.printStackTrace()
            }
            return
        }
        return
    }
}