package jp.aoichaan0513.A_TosoGame_Live.API.Interfaces

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI.PrefixType
import jp.aoichaan0513.A_TosoGame_Live.Main
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

abstract class IConfig(var f: File, var c: YamlConfiguration) {

    open fun save() {
        try {
            c.save(f)
        } catch (e: IOException) {
            Bukkit.getConsoleSender().sendMessage("""
                ${MainAPI.getPrefix(PrefixType.ERROR)}予期しないエラーが発生しました。
                ${MainAPI.getPrefix(PrefixType.SECONDARY)}位置: ${Main.PACKAGE_PATH}.API.Manager.World.WorldConfig.IConfig (継承元・先クラス)
            """.trimIndent())
            e.printStackTrace()
        }
    }
}