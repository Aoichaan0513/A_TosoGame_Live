package jp.aoichaan0513.A_TosoGame_Live.API.Maps;

import org.bukkit.Location;
import org.bukkit.map.MapCursor;

public class PlotInfo {

    public Location loc;
    public boolean setup;
    public MapCursor cursor;
    public MapCursor.Type type;

    public PlotInfo(Location loc, MapCursor.Type color) {
        this.loc = loc;
        setup = false;
        type = color;
    }

    public boolean isSetup() {
        return setup;
    }

    public void setSetup(boolean setup) {
        this.setup = setup;
    }

}
