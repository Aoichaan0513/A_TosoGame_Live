package jp.aoichaan0513.A_TosoGame_Live.API.Listener;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class MissionStartEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private final int id;

    public MissionStartEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    // イベント管理系 (いじらないように！)
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
