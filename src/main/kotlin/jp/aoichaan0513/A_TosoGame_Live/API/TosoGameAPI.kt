package jp.aoichaan0513.A_TosoGame_Live.API

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.ItemType
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager.GameState
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MoneyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.DifficultyManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerConfig
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.VisibilityManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.World.WorldManager.GameType
import jp.aoichaan0513.A_TosoGame_Live.API.Map.MapUtility
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Mission.MissionManager
import jp.aoichaan0513.A_TosoGame_Live.Utils.*
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.ThreadLocalRandom

class TosoGameAPI {
    companion object {

        var isRespawn = true
        var isRunnedBonusMission = false

        // アイテム配布
        fun setItem(type: GameType, p: Player) {
            val inv = p.inventory

            inv.clear()

            if (p.isAdminTeam) {
                val infoStack = ItemStack(Material.GOLD_NUGGET)
                val infoMeta = infoStack.itemMeta!!
                infoMeta.setDisplayName(ItemUtil.getItemName("プレイヤー情報"))
                infoMeta.lore = listOf("${ChatColor.YELLOW}プレイヤーを右クリックして", "${ChatColor.YELLOW}そのプレイヤーの情報が確認できます。")
                infoStack.itemMeta = infoMeta
                inv.setItem(1, infoStack)

                if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
                    val editStack = ItemStack(Material.COMPASS)
                    val editMeta = editStack.itemMeta!!
                    editMeta.setDisplayName(ItemUtil.getItemName("コンパス (WorldEdit)"))
                    editMeta.lore = listOf("${ChatColor.YELLOW}右クリックした方向にテレポートができます。")
                    editStack.itemMeta = editMeta
                    inv.setItem(2, editStack)
                }

                MissionManager.setBook(p)
            } else {
                if (GameManager.isGame(GameState.GAME)) {
                    when {
                        p.isHunterGroup -> {
                            setArmor(p)
                        }
                        p.isJailTeam -> {
                            MissionManager.setBook(p)
                            val itemStack = ItemStack(Material.ENDER_PEARL)
                            val itemMeta = itemStack.itemMeta!!
                            itemMeta.setDisplayName(ItemUtil.getItemName("プレイヤーを非表示"))
                            itemMeta.lore = listOf("${ChatColor.YELLOW}クリックして運営以外のプレイヤーを非表示にします。")
                            itemStack.itemMeta = itemMeta

                            inv.setItem(8, itemStack)
                        }
                        else -> {
                            val worldConfig = Main.worldConfig
                            val difficultyConfig = worldConfig.getDifficultyConfig(p)

                            if (MapUtility.map != null) {
                                val mapStack = MapUtility.map!!.clone()
                                val mapMeta = mapStack.itemMeta!!
                                mapMeta.setDisplayName(ItemUtil.getItemName("地図"))
                                mapMeta.lore = listOf("${ChatColor.YELLOW}なんかすごいやつ (語彙力)")
                                mapStack.itemMeta = mapMeta

                                inv.setItem(40, mapStack)
                            }

                            val boneItem = difficultyConfig.getBone(type)
                            val boneStack = ItemStack(Material.BONE, boneItem.count)
                            val boneMeta = boneStack.itemMeta!!
                            boneMeta.setDisplayName(ItemUtil.getItemName("骨 (透明化)"))
                            boneMeta.lore = listOf(
                                    "${ChatColor.YELLOW}クリックで${DateTimeUtil.formatTimestamp(boneItem.duration).rawSeconds}秒間透明になります。",
                                    "",
                                    "${ChatColor.GREEN}${ChatColor.UNDERLINE}クールタイム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${DateTimeUtil.formatTimestamp(boneItem.duration + 5).rawSeconds}${ChatColor.GRAY}秒"
                            )
                            boneStack.itemMeta = boneMeta

                            val featherItem = difficultyConfig.getFeather(type)
                            val featherStack = ItemStack(Material.FEATHER, featherItem.count)
                            val featherMeta = featherStack.itemMeta!!
                            featherMeta.setDisplayName(ItemUtil.getItemName("羽 (移動速度上昇)"))
                            featherMeta.lore = listOf(
                                    "${ChatColor.YELLOW}クリックで${DateTimeUtil.formatTimestamp(featherItem.duration).rawSeconds}秒間移動速度が上昇します。",
                                    "",
                                    "${ChatColor.GREEN}${ChatColor.UNDERLINE}クールタイム${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}${DateTimeUtil.formatTimestamp(featherItem.duration + 5).rawSeconds}${ChatColor.GRAY}秒"
                            )
                            featherStack.itemMeta = featherMeta

                            val eggStack = ItemStack(Material.EGG, difficultyConfig.getEgg(type).count)
                            val eggMeta = eggStack.itemMeta!!
                            eggMeta.setDisplayName(ItemUtil.getItemName("卵 (盲目・移動速度低下)"))
                            eggMeta.lore = listOf("${ChatColor.YELLOW}ハンターに当てると盲目と移動速度低下を5秒間与えます。")
                            eggStack.itemMeta = eggMeta

                            val items = PlayerManager.loadConfig(p).inventoryConfig.items
                            inv.setItem(items[ItemType.BONE] ?: 0, boneStack)
                            inv.setItem(items[ItemType.FEATHER] ?: 1, featherStack)
                            inv.setItem(items[ItemType.EGG] ?: 2, eggStack)

                            MissionManager.setBook(p)
                        }
                    }
                } else {
                    if (p.isHunterGroup) {
                        setArmor(p)
                    } else {
                        MissionManager.setBook(p)
                    }
                }
            }
        }

        // 装備
        private fun setArmor(p: Player) {
            if (p.isHunterTeam) {
                p.inventory.clear()
                p.inventory.helmet = ItemStack(Material.DIAMOND_HELMET)
                p.inventory.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
                p.inventory.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
                p.inventory.boots = ItemStack(Material.DIAMOND_BOOTS)
            } else if (p.isTuhoTeam) {
                p.inventory.clear()

                val itemStack = ItemStack(Material.HONEYCOMB)
                val itemMeta = itemStack.itemMeta!!
                itemMeta.setCustomModelData(1002)
                itemStack.itemMeta = itemMeta

                p.inventory.helmet = itemStack
                p.inventory.chestplate = ItemStack(Material.GOLDEN_CHESTPLATE)
                p.inventory.leggings = ItemStack(Material.GOLDEN_LEGGINGS)
                p.inventory.boots = ItemStack(Material.GOLDEN_BOOTS)
            }
        }

        fun removeArmor(p: Player) {
            p.inventory.helmet = ItemStack(Material.AIR)
            p.inventory.chestplate = ItemStack(Material.AIR)
            p.inventory.leggings = ItemStack(Material.AIR)
            p.inventory.boots = ItemStack(Material.AIR)
        }

        // エフェクト
        fun setPotionEffect(p: Player, isRemove: Boolean = true) {
            if (isRemove)
                for (effect in p.activePotionEffects)
                    p.removePotionEffect(effect.type)

            p.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 1, false, false))

            if (p.isPlayerGroup) {
                if (GameManager.isGame()) {
                    p.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 200, 1, false, false))
                    p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200, 1, false, false))
                }
            } else if (p.isHunterTeam) {
                p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200000, 0, false, false))
            }
        }

        fun hidePlayer(p: Player, time: Long = 20 * 10) {
            if (!p.isPlayerGroup) return

            VisibilityManager.add(p, VisibilityManager.VisibilityType.ITEM)
            Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable { VisibilityManager.remove(p, VisibilityManager.VisibilityType.ITEM) }, time)
        }


        // 権限管理
        fun isAdmin(p: Player): Boolean {
            return p.isOp && (isBroadCaster(p) || hasPermission(p) || p.isAdminTeam)
        }

        fun hasPermission(p: Player): Boolean {
            return PlayerManager.loadConfig(p).permission
        }

        fun isBroadCaster(p: Player): Boolean {
            return PlayerManager.loadConfig(p).broadCaster
        }

        fun addOp(p: Player) {
            if (p.isAdminTeam || hasPermission(p) || isBroadCaster(p))
                p.isOp = true
        }

        fun removeOp(p: Player) {
            if (!p.isAdminTeam || hasPermission(p) || isBroadCaster(p))
                p.isOp = false
        }

        // テレポート
        fun teleport(p: Player, location: Location) {
            Bukkit.getScheduler().runTask(Main.pluginInstance, Runnable { p.teleport(location) })
        }

        fun teleport(p: Player, collection: Collection<Location>) {
            if (collection.isEmpty()) return
            Bukkit.getScheduler().runTask(Main.pluginInstance, Runnable { p.teleport(collection.shuffled(ThreadLocalRandom.current())[0]) })
        }

        // 通知・サウンド
        fun sendInformationText(p: Player) {
            val team = Teams.getJoinedTeam(p)
            val difficulty = DifficultyManager.getDifficulty(p)

            val (color, name) = when (PlayerManager.loadConfig(p).bookForegroundColor) {
                PlayerConfig.BookForegroundColor.BLACK -> PlayerConfig.BookForegroundColor.BLACK to "黒"
                PlayerConfig.BookForegroundColor.WHITE -> PlayerConfig.BookForegroundColor.WHITE to "白"
            }

            p.sendMessage("""
                ${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}現在の設定
                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}チーム: ${team.color}${ChatColor.UNDERLINE}${ChatColor.stripColor(team.displayName)}
                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}難易度: ${difficulty.color}${ChatColor.UNDERLINE}${ChatColor.stripColor(difficulty.displayName)}
                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}レート: ${ChatColor.YELLOW}${ChatColor.UNDERLINE}${MoneyManager.getRate(p)}${ChatColor.RESET}${ChatColor.GRAY}円/秒
                ${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}本の文字色: ${color.color}${ChatColor.UNDERLINE}$name
            """.trimIndent())
        }

        fun sendNotificationSound() {
            for (p in Bukkit.getOnlinePlayers())
                sendNotificationSound(p)
        }

        private fun sendNotificationSound(p: Player) {
            /*
            new BukkitRunnable() {
                int count = 0;

                public void run() {
                    count++;
                    if (count == 10 || count == 11 || count == 12 || count == 13 || count == 14 || count == 15) {
                    } else if (count == 25) {
                        cancel();
                    } else {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 2);
                    }
                }
            }.runTaskTimer(Main.pluginInstance, 3, 3);
            */
            object : BukkitRunnable() {
                var c = 26

                override fun run() {
                    if (c in 18..26 || c in 1..9)
                        p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f)
                    if (c < 0)
                        cancel()
                    c--
                }
            }.runTaskTimerAsynchronously(Main.pluginInstance, 0, 2)
        }
    }

    enum class Objective(val objectName: String) {
        // サイドバー
        SIDEBAR("Toso_Sidebar"),

        // サイドバー 表示用チーム
        SIDEBAR_STATUS("T_SB_Status"),
        SIDEBAR_TEAM("T_SB_Team"),
        SIDEBAR_DIFFICULTY("T_SB_Difficulty"),
        SIDEBAR_REWARD("T_SB_Reward"),
        SIDEBAR_RATE("T_SB_Rate"),
        SIDEBAR_TEAM_PLAYER("T_SB_T_Player"),
        SIDEBAR_TEAM_HUNTER("T_SB_T_Hunter"),
        SIDEBAR_TEAM_JAIL("T_SB_T_Jail"),
        SIDEBAR_TEAM_SUCCESS("TSB_T_Success"),
        SIDEBAR_TEAM_TUHO("T_SB_T_Tuho"),

        // チーム
        TEAM_ADMIN("Toso_Admin"),
        TEAM_PLAYER("Toso_Player"),
        TEAM_HUNTER("Toso_Hunter"),
        TEAM_JAIL("Toso_Jail"),
        TEAM_SUCCESS("Toso_Success"),
        TEAM_TUHO("Toso_Tuho");

    }
}