package jp.aoichaan0513.A_TosoGame_Live.API.Manager;

import org.bukkit.Bukkit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.logging.Level;

public class SkinManager {

    String uuid;
    String name;
    String value;
    String signatur;

    public SkinManager(String uuid) {
        this.uuid = uuid;
        load();
    }

    private void load() {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            URLConnection uc = url.openConnection();
            uc.setUseCaches(false);
            uc.setDefaultUseCaches(false);
            uc.addRequestProperty("User-Agent", "Mozilla/5.0");
            uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
            uc.addRequestProperty("Pragma", "no-cache");

            String json = new Scanner(uc.getInputStream(), "UTF-8").useDelimiter("\\A").next();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("properties");

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject property = jsonArray.getJSONObject(i);
                    this.name = property.getString("name");
                    this.value = property.getString("value");
                    this.signatur = property.has("signature") ? property.getString("signature") : null;
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e);
                }
            }
        } catch (Exception e) {
            ; // Failed to load skin
        }
    }

    public String getSkinValue() {
        return value;
    }

    public String getSkinName() {
        return name;
    }

    public String getSkinSignatur() {
        return signatur;
    }

}
