package jp.aoichaan0513.A_TosoGame_Live.Listeners;

import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.potion.PotionEffectType;

public class onEntity implements Listener {

    @EventHandler
    public void onEntityBreakDoor(EntityBreakDoorEvent e) {
        if (e.getEntityType() == EntityType.ZOMBIE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent e) {
        if (e.getEntity().getLocation().getWorld() == WorldManager.getWorld()) {
            if (e.getEntityType() == EntityType.ZOMBIE) {
                if (e.getTarget() == null) return;
                if (e.getTarget().getType() == EntityType.PLAYER) {
                    Player p = (Player) e.getTarget();

                    if (GameManager.isGame(GameManager.GameState.GAME)) {
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                            if (!p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                                e.setCancelled(false);
                            } else {
                                e.setTarget(null);
                                e.setCancelled(true);
                            }
                        } else {
                            e.setTarget(null);
                            e.setCancelled(true);
                        }
                    } else {
                        e.setTarget(null);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (e.getLocation().getWorld() == WorldManager.getWorld()) {
            if (e.getEntityType() != EntityType.ZOMBIE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent e) {
        World world = e.getWorld();
        Chunk chunk = e.getChunk();

        if (world == WorldManager.getWorld()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.ZOMBIE) {
                    if (chunk == entity.getLocation().getChunk()) {
                        world.setChunkForceLoaded(chunk.getX(), chunk.getZ(), true);
                    }
                }
            }
        }
    }
}
