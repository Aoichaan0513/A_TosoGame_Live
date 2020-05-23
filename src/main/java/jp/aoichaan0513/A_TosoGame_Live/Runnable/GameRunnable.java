package jp.aoichaan0513.A_TosoGame_Live.Runnable;

import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.BossBarManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldConfig;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Scoreboard;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI;
import jp.aoichaan0513.A_TosoGame_Live.Listeners.onDamage;
import jp.aoichaan0513.A_TosoGame_Live.Listeners.onInteract;
import jp.aoichaan0513.A_TosoGame_Live.Main;
import jp.aoichaan0513.A_TosoGame_Live.Mission.HunterZone;
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.*;

public class GameRunnable extends BukkitRunnable {

    public static int countDown;
    public static int gameTime;

    public static int settingCountDown;
    public static int settingGameTime;

    private static HashMap<UUID, Location> hashMap = new HashMap<>();

    public GameRunnable(int countDown, int gameTime) {
        this.countDown = countDown;
        this.settingCountDown = countDown;

        this.gameTime = gameTime;
        this.settingGameTime = gameTime;
    }

    @Override
    public void run() {
        if (!GameManager.isGame()) return;

        WorldConfig worldConfig = Main.getWorldConfig();
        if (countDown <= 0) {
            gameTime--;

            BossBarManager.showBar(gameTime, settingGameTime);

            MoneyManager.addReward();

            for (Player player : Bukkit.getOnlinePlayers())
                setScoreboard(player);

            int respawnDenyTime = worldConfig.getGameConfig().getRespawnDeny();
            TosoGameAPI.isRes = gameTime > respawnDenyTime;

            if (gameTime > 0) {
                if (gameTime < 16) {
                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム終了まで残り" + TimeFormat.formatSec(gameTime) + "秒");

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + gameTime, ChatColor.GRAY + "ゲーム終了まで", 20, 40, 20);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
                    }
                }

                if (TimeFormat.formatSec(gameTime) == 0) {
                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム終了まで残り" + TimeFormat.formatMin(gameTime) + "分");

                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
                }

                if (TimeFormat.formatMin(gameTime) % 2 != 0 && TimeFormat.formatSec(gameTime) == 30) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_JAIL, player)) {
                            if (!TosoGameAPI.isRes) {
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "\"/hide\"で周りを非表示に、\"/show\"で周りを表示できます。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "また、\"/spec\"で観戦モードにできます。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "このメッセージは確保者にのみ表示されます。");
                            } else {
                                player.sendMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "\"/hide\"で周りを非表示に、\"/show\"で周りを表示できます。\n" +
                                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "このメッセージは確保者にのみ表示されます。");
                            }
                        }
                    }
                }

                if (TimeFormat.formatSec(gameTime) % 5 == 0) {
                    setHealths(worldConfig, TimeFormat.formatSec(gameTime) % 10 == 0);
                } else {
                    for (Player player : Bukkit.getOnlinePlayers())
                        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player))
                            setHealth(player);
                }

                if (gameTime == respawnDenyTime) {
                    Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "残り" + TimeFormat.formatMin(respawnDenyTime) + "分になったため途中参加・復活を禁止します。");

                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
                }
            }

            if (gameTime == 0 || Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) == 0) {
                // ゲーム時間が0になるか、逃走者が0人になった場合

                HunterZone.end();

                sendResult();
                BossBarManager.showBar();

                if (worldConfig.getGameConfig().getSuccessMission()) {
                    // 生存ミッションが有効の場合
                    if (Teams.getOnlineCount(Teams.OnlineTeam.TOSO_SUCCESS) > 0) {
                        // 生存者チームにプレイヤーがいる場合
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200000, 1, false, false));
                                p.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Task Success.", ChatColor.BOLD + "逃走者側の勝利", 10, 70, 20);
                            } else {
                                MoneyManager.setReward(p, 0);
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p))
                                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Task Failed...", ChatColor.BOLD + "逃走者側の勝利", 10, 70, 20);
                                else
                                    p.sendTitle("", ChatColor.BOLD + "逃走者側の勝利", 10, 70, 20);
                            }
                        }
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム終了\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "今回の生存者は" + Teams.getOnlineTeam(Teams.OnlineTeam.TOSO_SUCCESS).getEntries() + "です。");
                    } else {
                        // 生存者チームにプレイヤーがいない場合
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                long reward = worldConfig.getDifficultyConfig(p).getRate() * settingGameTime;

                                MoneyManager.setReward(p, Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) ? reward : reward / 2);

                                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                                p.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Task Success.", ChatColor.BOLD + "ハンター・通報部隊側の勝利", 10, 70, 20);
                            } else {
                                MoneyManager.setReward(p, 0);

                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p))
                                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Task Failed...", ChatColor.BOLD + "ハンター・通報部隊側の勝利", 10, 70, 20);
                                else
                                    p.sendTitle("", ChatColor.BOLD + "ハンター・通報部隊側の勝利", 10, 70, 20);
                            }
                        }
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム終了\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "今回の生存者はいませんでした。");
                    }
                } else {
                    // 生存ミッションが無効の場合
                    if (Teams.getOnlineCount(Teams.OnlineTeam.TOSO_PLAYER) > 0) {
                        // 逃走者チームにプレイヤーがいる場合
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p))
                                Teams.joinTeam(Teams.OnlineTeam.TOSO_SUCCESS, p);

                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
                                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200000, 1, false, false));
                                p.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Task Success.", ChatColor.BOLD + "逃走者側の勝利", 10, 70, 20);
                            } else {
                                MoneyManager.setReward(p, 0);
                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p))
                                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Task Failed...", ChatColor.BOLD + "逃走者側の勝利", 10, 70, 20);
                                else
                                    p.sendTitle("", ChatColor.BOLD + "逃走者側の勝利", 10, 70, 20);
                            }
                        }
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム終了\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "今回の生存者は" + Teams.getOnlineTeam(Teams.OnlineTeam.TOSO_SUCCESS).getEntries() + "です。");
                    } else {
                        // 逃走者チームにプレイヤーがいない場合
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_TUHO, p)) {
                                long reward = worldConfig.getDifficultyConfig(p).getRate() * settingGameTime;

                                MoneyManager.setReward(p, Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_HUNTER, p) ? reward : reward / 2);

                                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                                p.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Task Success.", ChatColor.BOLD + "ハンター・通報部隊側の勝利", 10, 70, 20);
                            } else {
                                MoneyManager.setReward(p, 0);

                                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p))
                                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Task Failed...", ChatColor.BOLD + "ハンター・通報部隊側の勝利", 10, 70, 20);
                                else
                                    p.sendTitle("", ChatColor.BOLD + "ハンター・通報部隊側の勝利", 10, 70, 20);
                            }
                        }
                        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム終了\n" +
                                MainAPI.getPrefix(MainAPI.PrefixType.WARNING) + "今回の生存者はいませんでした。");
                    }
                }

                GameManager.endGame();

                // ブロックにテレポートするメッセージを送信
                if (onInteract.successBlockLoc != null) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (TosoGameAPI.isAdmin(p)) {
                            TextComponent textComponent1 = new TextComponent();
                            textComponent1.setText(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "生存ブロックの位置にテレポートする場合は");
                            TextComponent textComponent2 = new TextComponent();
                            textComponent2.setText(ChatColor.GRAY + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "ここ");
                            textComponent2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GOLD + "ブロックの位置にテレポートする場合はここをクリックしてください。").create()));
                            textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp"));
                            textComponent1.addExtra(textComponent2);
                            TextComponent textComponent3 = new TextComponent();
                            textComponent3.setText(ChatColor.GRAY + "をクリックしてください。");
                            textComponent1.addExtra(textComponent3);
                            p.spigot().sendMessage(textComponent1);
                        }
                    }
                }
            }
        } else {
            countDown--;

            BossBarManager.showBar(countDown, settingCountDown);

            for (Player player : Bukkit.getOnlinePlayers())
                setScoreboard(player);

            if (countDown == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    MoneyManager.setRate(player);
                    hashMap.put(player.getUniqueId(), player.getLocation());

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    player.sendTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "逃走中", ChatColor.GRAY + "Run for money", 20, 50, 20);
                }

                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム開始\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "マップ情報\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "・マップ名: " + worldConfig.getMapConfig().getName() + "\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "・マップ説明: " + worldConfig.getMapConfig().getDescription() + "\n" +
                        MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "・マップ製作者: " + worldConfig.getMapConfig().getAuthors());

                worldConfig.getHunterDoorConfig().openHunterDoors();
                for (Location loc : worldConfig.getHunterDoorConfig().getLocations().values())
                    WorldManager.getWorld().createExplosion(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 4, false, false);

                GameManager.setGameState(GameManager.GameState.GAME);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p)) {
                        TosoGameAPI.setItem(WorldManager.GameType.START, p);

                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 1, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 1, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1, false, false));

                        Main.invisibleSet.add(p.getUniqueId());
                        for (Player player : Bukkit.getOnlinePlayers())
                            TosoGameAPI.showPlayers(player);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Main.invisibleSet.remove(p.getUniqueId());
                                for (Player player : Bukkit.getOnlinePlayers())
                                    TosoGameAPI.showPlayers(player);
                            }
                        }.runTaskLater(Main.getInstance(), 200);
                    }
                }
            } else if (countDown <= 5) {
                Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "ゲーム開始まで残り" + countDown + "秒");

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + countDown, ChatColor.GRAY + "ゲーム開始まで", 20, 40, 20);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
                }
            }
        }
    }

    /**
     * カウントダウン・ゲーム時間
     */
    public static int getTime() {
        return countDown <= 0 ? gameTime : countDown;
    }

    public static int getCountDown() {
        return countDown;
    }

    public static void setCountDown(int countDown) {
        GameRunnable.countDown = countDown;
    }

    public static int getGameTime() {
        return gameTime;
    }

    public static void setGameTime(int gameTime) {
        GameRunnable.gameTime = gameTime;
        GameRunnable.settingGameTime = gameTime;
    }

    public static void addGameTime(int gameTime) {
        GameRunnable.gameTime += gameTime;
        GameRunnable.settingGameTime = GameRunnable.gameTime;
    }

    public static void removeGameTime(int gameTime) {
        GameRunnable.gameTime -= gameTime;
        GameRunnable.settingGameTime = GameRunnable.gameTime;
    }


    /**
     * 体力システム
     */
    public void setHealth(Player p) {
        hashMap.put(p.getUniqueId(), p.getLocation());
    }

    public void setHealths(WorldConfig worldConfig, boolean isTenSecond) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (GameManager.isGame(GameManager.GameState.GAME)) {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, player) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, player)) {
                    WorldConfig.DifficultyConfig difficultyConfig = worldConfig.getDifficultyConfig(player);

                    if (difficultyConfig.getHealth()) {
                        Location loc = player.getLocation();
                        double health = player.getHealth();
                        float walkSpeed = player.getWalkSpeed();

                        if (hashMap.containsKey(player.getUniqueId())) {
                            Location location = hashMap.get(player.getUniqueId());

                            if (isTenSecond) {
                                // 10秒おき
                                if (player.isSneaking()) {
                                    if (location.getBlockX() == loc.getBlockX() && location.getBlockY() == loc.getBlockY() && location.getBlockZ() == loc.getBlockZ()) {
                                        if (health < 20) {
                                            if (health % 2 == 0) {
                                                player.setWalkSpeed(walkSpeed + 0.02f);
                                                player.setHealth(health + 2);
                                                player.spawnParticle(Particle.HEART, loc.getX(), loc.getBlockY() + 1.6, loc.getZ(), 3, 0.1, 0, 0.1);
                                            } else {
                                                player.setWalkSpeed(walkSpeed + 0.01f);
                                                player.setHealth(health + 1);
                                                player.spawnParticle(Particle.HEART, loc.getX(), loc.getBlockY() + 1.6, loc.getZ(), 3, 0.1, 0, 0.1);
                                            }
                                        }
                                    } else {
                                        if (health < 20) {
                                            player.setWalkSpeed(walkSpeed + 0.01f);
                                            player.setHealth(health + 1);
                                            player.spawnParticle(Particle.HEART, loc.getX(), loc.getBlockY() + 1.6, loc.getZ(), 3, 0.1, 0, 0.1);
                                        }
                                    }
                                } else {
                                    if (!(location.getBlockX() == loc.getBlockX() && location.getBlockY() == loc.getBlockY() && location.getBlockZ() == loc.getBlockZ())) {
                                        if (health > 6) {
                                            if (player.isSprinting()) {
                                                if (health % 2 == 0) {
                                                    player.setWalkSpeed(walkSpeed - 0.02f);
                                                    player.setHealth(health - 2);
                                                } else {
                                                    player.setWalkSpeed(walkSpeed - 0.01f);
                                                    player.setHealth(health - 1);
                                                }
                                            } else {
                                                player.setWalkSpeed(walkSpeed - 0.01f);
                                                player.setHealth(health - 1);
                                            }
                                        }
                                    } else {
                                        if (health < 20) {
                                            player.setWalkSpeed(walkSpeed + 0.01f);
                                            player.setHealth(health + 1);
                                            player.spawnParticle(Particle.HEART, loc.getX(), loc.getBlockY() + 2, loc.getZ(), 3, 0.1, 0, 0.1);
                                        }
                                    }
                                }
                            } else {
                                if (player.isSneaking()) {
                                    if (location.getBlockX() == loc.getBlockX() && location.getBlockY() == loc.getBlockY() && location.getBlockZ() == loc.getBlockZ()) {
                                        if (health < 20) {
                                            if (health % 2 == 0) {
                                                player.setWalkSpeed(walkSpeed + 0.02f);
                                                player.setHealth(health + 2);
                                                player.spawnParticle(Particle.HEART, loc.getX(), loc.getBlockY() + 1.6, loc.getZ(), 3, 0.1, 0, 0.1);
                                            } else {
                                                player.setWalkSpeed(walkSpeed + 0.01f);
                                                player.setHealth(health + 1);
                                                player.spawnParticle(Particle.HEART, loc.getX(), loc.getBlockY() + 1.6, loc.getZ(), 3, 0.1, 0, 0.1);
                                            }
                                        }
                                    } else {
                                        if (health < 20) {
                                            player.setWalkSpeed(walkSpeed + 0.01f);
                                            player.setHealth(health + 1);
                                            player.spawnParticle(Particle.HEART, loc.getX(), loc.getBlockY() + 1.6, loc.getZ(), 3, 0.1, 0, 0.1);
                                        }
                                    }
                                } else {
                                    if (location.getBlockX() == loc.getBlockX() && location.getBlockY() == loc.getBlockY() && location.getBlockZ() == loc.getBlockZ()) {
                                        if (!player.isSprinting()) {
                                            if (health < 20) {
                                                player.setWalkSpeed(walkSpeed + 0.01f);
                                                player.setHealth(health + 1);
                                                player.spawnParticle(Particle.HEART, loc.getX(), loc.getBlockY() + 2, loc.getZ(), 3, 0.1, 0, 0.1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    player.setWalkSpeed(0.2f);
                    player.setHealth(20);
                }
            } else {
                player.setWalkSpeed(0.2f);
                player.setHealth(20);
            }

            setHealth(player);
            continue;
        }
    }


    /**
     * ゲーム終了
     */
    private void sendResult() {
        List<Map.Entry<UUID, Long>> rewardList = new ArrayList<>(MoneyManager.getRewardMap().entrySet());
        Collections.sort(rewardList, (obj1, obj2) -> obj2.getValue().compareTo(obj1.getValue()));

        int rewardCount = 1;

        StringBuffer rewardBuffer = new StringBuffer();
        for (Map.Entry<UUID, Long> entry : rewardList)
            rewardBuffer.append(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + rewardCount++ + "位" + ChatColor.GRAY + ": " + ChatColor.YELLOW + Bukkit.getPlayer(entry.getKey()).getName() + ChatColor.GRAY + " (" + MoneyManager.formatMoney(entry.getValue()) + ")\n");

        String rewardResult = rewardBuffer.toString().trim();
        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "賞金ランキング\n"
                + (!rewardResult.isEmpty() ? rewardResult : MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "なし"));

        List<Map.Entry<UUID, Integer>> hunterList = new ArrayList<>(onDamage.hunterMap.entrySet());
        Collections.sort(hunterList, (obj1, obj2) -> obj2.getValue().compareTo(obj1.getValue()));

        int hunterCount = 1;

        StringBuffer hunterBuffer = new StringBuffer();
        for (Map.Entry<UUID, Integer> entry : hunterList)
            hunterBuffer.append(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + ChatColor.GOLD + hunterCount++ + "位" + ChatColor.GRAY + ": " + ChatColor.YELLOW + Bukkit.getPlayer(entry.getKey()).getName() + ChatColor.GRAY + " (" + entry.getValue() + ")\n");

        String hunterResult = hunterBuffer.toString().trim();
        Bukkit.broadcastMessage(MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS) + "確保数ランキング\n"
                + (!hunterResult.isEmpty() ? hunterResult : MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY) + "なし"));
    }

    private void setScoreboard(Player p) {
        if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, p) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, p)) {
            org.bukkit.scoreboard.Scoreboard board = Scoreboard.getBoard(p);

            if (!Scoreboard.isHidePlayer(p)) {
                if (p.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = p.getInventory().getItemInMainHand().getItemMeta();
                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(DisplaySlot.SIDEBAR);
                    }
                } else if (p.getInventory().getItemInOffHand().getType() == Material.BOOK) {
                    ItemMeta itemMeta = p.getInventory().getItemInOffHand().getItemMeta();
                    if (ChatColor.stripColor(itemMeta.getDisplayName()).equals(Main.PHONE_ITEM_NAME)) {
                        board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                    } else {
                        board.clearSlot(DisplaySlot.SIDEBAR);
                    }
                } else {
                    board.clearSlot(DisplaySlot.SIDEBAR);
                }
            } else {
                board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
            }
        } else {
            org.bukkit.scoreboard.Scoreboard board = Scoreboard.getBoard(p);
            board.getObjective(TosoGameAPI.Objective.SIDEBAR.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }
}
