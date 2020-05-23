package jp.aoichaan0513.A_TosoGame_Live.API.Interfaces;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class IConfig {

    public File file;
    public YamlConfiguration yamlConfiguration;

    public IConfig(File file, YamlConfiguration yamlConfiguration) {
        this.file = file;
        this.yamlConfiguration = yamlConfiguration;
    }

    public void save() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.ERROR) + "予期しないエラーが発生しました。\n" +
                    MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "位置: " + Main.PACKAGE_PATH + ".API.Manager.World.WorldConfig.IConfig (継承元・先クラス)");
            e.printStackTrace();
        }
    }
}