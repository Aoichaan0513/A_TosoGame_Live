package jp.aoichaan0513.A_TosoGame_Live.Mission

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.ItemType
import jp.aoichaan0513.A_TosoGame_Live.API.Interfaces.IMission
import jp.aoichaan0513.A_TosoGame_Live.API.Listener.MissionEndEvent
import jp.aoichaan0513.A_TosoGame_Live.API.Listener.MissionStartEvent
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams.OnlineTeam
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

class MissionManager {
    companion object {

        var bossBarList = mutableListOf(-1)
            private set

        var missionStates = mutableSetOf(MissionState.NONE.id)
            private set

        val missionTitleMap = mutableMapOf<Int, String>()

        val missionMap = mutableMapOf<MissionType, MutableList<IMission>>(
                MissionType.MISSION to mutableListOf(),
                MissionType.TUTATU_HINT to mutableListOf(),
                MissionType.END to mutableListOf()
        )

        var bossBar: BossBar? = null
            private set

        fun isMission(id: Int): Boolean {
            return missionStates.contains(id)
        }

        fun isMission(missionState: MissionState): Boolean {
            return isMission(missionState.id)
        }

        val isMission: Boolean
            get() = missionStates.isNotEmpty() && !isMission(MissionState.NONE)

        val isBossBar: Boolean
            get() = bossBar != null

        private fun addBook(p: Player): ItemStack {
            val itemStack = ItemStack(ItemType.BOOK.material)
            val itemMeta = itemStack.itemMeta!!
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
            itemMeta.setCustomModelData(1001)
            itemMeta.setDisplayName("${ChatColor.AQUA}${ChatColor.BOLD}${Main.PHONE_ITEM_NAME}")
            itemMeta.lore = Arrays.asList(
                    "${ChatColor.GOLD}${ChatColor.UNDERLINE}左クリック${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}ホーム画面",
                    "${ChatColor.GOLD}${ChatColor.UNDERLINE}右クリック${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.YELLOW}ミッションアプリ"
            )
            itemStack.itemMeta = itemMeta

            if (GameManager.isGame() && Teams.hasJoinedTeam(OnlineTeam.TOSO_ADMIN, p)) {
                p.inventory.setItem(PlayerManager.loadConfig(p).inventoryConfig.getSlot(ItemType.BOOK), itemStack)
            } else {
                p.inventory.addItem(itemStack)
            }

            p.updateInventory()
            return itemStack
        }

        fun setBook(p: Player): ItemStack? {
            if (Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, p) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, p)) return null

            val itemStack = p.inventory.contents.firstOrNull { it != null && it.type == ItemType.BOOK.material && it.itemMeta != null && it.itemMeta!!.hasCustomModelData() && it.itemMeta!!.customModelData == 1001 }
                    ?: addBook(p)
            p.updateInventory()
            return itemStack
        }

        fun hasFile(i: Int): Boolean {
            val file = File("${Main.pluginInstance.dataFolder}${Main.FILE_SEPARATOR}missions${Main.FILE_SEPARATOR}$i.txt")
            return file.exists()
        }

        fun getMissions(type: MissionType): MutableList<IMission> {
            return missionMap[type] ?: mutableListOf()
        }

        fun getMission(id: Int, type: MissionType): IMission? {
            return getMissions(type).firstOrNull { it.id == id }
        }

        fun sendMission(sender: CommandSender, i: Int) {
            if (!GameManager.isGame(GameManager.GameState.GAME) || !hasFile(i)) return

            val id = startMission(i)
            TosoGameAPI.sendNotificationSound()

            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}ミッションを開始しました。")
            Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ミッションが通知されました。")

            val textComponent1 = TextComponent(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY))
            val textComponent2 = TextComponent("${ChatColor.GRAY}${ChatColor.BOLD}${ChatColor.UNDERLINE}ここ${ChatColor.RESET}")
            textComponent2.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu open mission $id")
            val textComponent3 = TextComponent("${ChatColor.GRAY}をクリックして詳細を確認できます。")
            textComponent1.addExtra(textComponent2)
            textComponent1.addExtra(textComponent3)

            Bukkit.getOnlinePlayers().filterNot { Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, it) }.forEach { it.spigot().sendMessage(textComponent1) }
            return
        }

        fun sendMission(sender: CommandSender, state: MissionState) {
            sendMission(sender, state.id)
        }

        fun sendMission(sender: CommandSender, missionType: MissionType = MissionType.TUTATU_HINT, detailType: MissionDetailType = MissionDetailType.TUTATU, list: List<String>, material: Material = Material.QUARTZ_BLOCK) {
            if (!GameManager.isGame(GameManager.GameState.GAME)) return

            val id = startMission(detailType.detailName, list, missionType, material)
            TosoGameAPI.sendNotificationSound()

            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${detailType.detailName}を送信しました。")
            Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ミッションが通知されました。")

            val textComponent1 = TextComponent(MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY))
            val textComponent2 = TextComponent("${ChatColor.GRAY}${ChatColor.BOLD}${ChatColor.UNDERLINE}ここ${ChatColor.RESET}")
            textComponent2.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu open ${detailType.name.toLowerCase()} $id")
            val textComponent3 = TextComponent("${ChatColor.GRAY}をクリックして詳細を確認できます。")
            textComponent1.addExtra(textComponent2)
            textComponent1.addExtra(textComponent3)

            Bukkit.getOnlinePlayers().filterNot { Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, it) || Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, it) }.forEach { it.spigot().sendMessage(textComponent1) }
            return
        }

        fun sendMission(sender: CommandSender, missionType: MissionType = MissionType.TUTATU_HINT, detailType: MissionDetailType = MissionDetailType.TUTATU, text: String, material: Material = Material.QUARTZ_BLOCK) {
            sendMission(sender, missionType, detailType, listOf(text.trim()), material)
        }


        fun setBossBar() {
            if (bossBarList.filter { isMission(it) }.isEmpty()) return

            val result = bossBarList.filter { isMission(it) }[0]

            bossBarList.removeAt(0)
            bossBarList.add(result)

            when (result) {
                -1 -> {
                    object : BukkitRunnable() {
                        var i = 10

                        override fun run() {
                            if (i < 0) cancel()

                            if (isMission) {
                                val stringBuilder = StringBuilder()
                                for (id in missionStates)
                                    stringBuilder.append("${ChatColor.GOLD}${ChatColor.BOLD}${missionTitleMap[id] ?: "Unknown"}${ChatColor.RESET}${ChatColor.GRAY}, ")

                                bossBar = createBossBar(stringBuilder.toString().trim().substring(0, stringBuilder.toString().trim().length - 5), 1.toDouble(), BarColor.YELLOW, BarStyle.SOLID)

                                Bukkit.getOnlinePlayers()
                                        .filter { !Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, it) && !Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, it) }
                                        .forEach { bossBar?.addPlayer(it) }
                            } else {
                                bossBar?.removeAll()
                                bossBar = null
                            }

                            i--
                        }
                    }.runTaskTimer(Main.pluginInstance, 0, 20)
                }
                MissionState.HUNTER_ZONE.id -> {
                    object : BukkitRunnable() {
                        var i = 10

                        override fun run() {
                            if (i < 0) cancel()

                            val bossBar = createBossBar(
                                    "${ChatColor.YELLOW}${ChatColor.BOLD}ハンターゾーンミッション終了まで${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.RESET}${TimeFormat.formatJapan(HunterZone.getMissionTime().toInt())}",
                                    if (isMission(MissionState.HUNTER_ZONE)) HunterZone.getMissionTime() / (HunterZone.getInitialMissionTime() / 20) else 1.toDouble(),
                                    MissionState.HUNTER_ZONE.barColor,
                                    BarStyle.SOLID
                            )

                            Bukkit.getOnlinePlayers()
                                    .filter { !Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, it) && !Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, it) }
                                    .forEach { bossBar?.addPlayer(it) }

                            i--
                        }
                    }.runTaskTimer(Main.pluginInstance, 0, 20)
                }
                MissionState.TIMED_DEVICE.id -> {
                    object : BukkitRunnable() {
                        var i = 10

                        override fun run() {
                            if (i < 0) cancel()

                            val bossBar = createBossBar(
                                    "${ChatColor.YELLOW}${ChatColor.BOLD}時限装置解除ミッション終了まで${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.RESET}${TimeFormat.formatJapan(TimedDevice.getMissionTime().toInt())}",
                                    if (isMission(MissionState.TIMED_DEVICE)) TimedDevice.getMissionTime() / (TimedDevice.getInitialMissionTime() / 20) else 1.toDouble(),
                                    MissionState.TIMED_DEVICE.barColor,
                                    BarStyle.SOLID
                            )

                            Bukkit.getOnlinePlayers()
                                    .filter { !Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, it) && !Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, it) }
                                    .forEach { bossBar?.addPlayer(it) }

                            i--
                        }
                    }.runTaskTimer(Main.pluginInstance, 0, 20)
                }
                else -> {
                    object : BukkitRunnable() {
                        var i = 10

                        override fun run() {
                            if (i < 0) cancel()

                            val bossBar = createBossBar("${ChatColor.BOLD}${missionTitleMap[result]}", 1.toDouble(), MissionState.getMission(result).barColor, BarStyle.SOLID)

                            Bukkit.getOnlinePlayers()
                                    .filter { !Teams.hasJoinedTeam(OnlineTeam.TOSO_HUNTER, it) && !Teams.hasJoinedTeam(OnlineTeam.TOSO_TUHO, it) }
                                    .forEach { bossBar?.addPlayer(it) }

                            i--
                        }
                    }.runTaskTimer(Main.pluginInstance, 0, 20)
                }
            }

            if (GameManager.isGame())
                Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable { setBossBar() }, 20 * 10)
        }

        fun resetBossBar() {
            bossBar?.removeAll()
            bossBar = null
            bossBarList = mutableListOf(MissionState.NONE.id)
        }

        private fun createBossBar(title: String?, progress: Double = 1.toDouble(), barColor: BarColor = BarColor.WHITE, barStyle: BarStyle = BarStyle.SOLID): BossBar? {
            if (bossBar == null)
                bossBar = Bukkit.createBossBar(title, barColor, barStyle)

            bossBar?.setTitle(title)
            bossBar?.progress = progress
            bossBar?.color = barColor
            bossBar?.style = barStyle

            return bossBar
        }


        fun initMissions() {
            val folder = File("${Main.pluginInstance.dataFolder}${Main.FILE_SEPARATOR}missions")
            folder.listFiles().filter { it.isFile && it.extension.equals("txt", true) }.forEach {
                var title = ""
                it.forEachLine(Main.CHARSET) {
                    if (title.isNotEmpty()) return@forEachLine
                    title = it
                }
                missionTitleMap[it.nameWithoutExtension.toInt()] = title
            }
        }


        private fun startMission(id: Int): Int {
            if (!hasFile(id)) return -1

            val missionState = MissionState.getMission(id)
            when (missionState) {
                MissionState.HUNTER_ZONE -> HunterZone.startMission()
                MissionState.TIMED_DEVICE -> TimedDevice.startMission()
            }

            return sendFileMissionAPI(id, missionState.material)
        }

        private fun startMission(missionState: MissionState): Int {
            if (missionState == MissionState.OTHER || missionState == MissionState.NONE
                    || !hasFile(missionState.id)) return -1

            when (missionState) {
                MissionState.HUNTER_ZONE -> HunterZone.startMission()
                MissionState.TIMED_DEVICE -> TimedDevice.startMission()
            }

            return sendFileMissionAPI(missionState.id, missionState.material)
        }

        private fun startMission(title: String, descriptions: List<String>, type: MissionType, material: Material): Int {
            val id = if (type == MissionType.MISSION) getMissions(MissionType.MISSION).size + getMissions(MissionType.END).size else getMissions(type).size

            val list = getMissions(type)

            val mission = IMission(id + 1, title, descriptions, type, material)
            list.add(mission)
            missionMap[type] = list
            return mission.id
        }

        private fun endMission(id: Int) {
            if (!isMission) return

            Bukkit.getPluginManager().callEvent(MissionEndEvent(id))

            val listMission = getMissions(MissionType.MISSION)
            val listEnd = getMissions(MissionType.END)
            missionMap[MissionType.MISSION] = mutableListOf()
            missionMap[MissionType.END] = Stream.concat(listEnd.stream(), listMission.stream()).toList().toMutableList()

            HunterZone.endMission()
            bossBarList.remove(id)
            missionStates.remove(id)
        }

        fun endMission(missionState: MissionState) {
            endMission(missionState.id)
        }

        fun endMissions() {
            val missionStates = missionStates
            for (id in missionStates)
                endMission(id)
        }

        fun resetMission() {
            resetBossBar()
            missionStates = mutableSetOf(MissionState.NONE.id)

            for (key in missionMap.keys)
                missionMap[key] = mutableListOf()
        }

        private fun sendFileMissionAPI(id: Int, material: Material): Int {
            val file = File("${Main.pluginInstance.dataFolder}${Main.FILE_SEPARATOR}missions${Main.FILE_SEPARATOR}$id.txt")

            var title = ""
            val descriptions = mutableListOf<String>()

            val descriptionBuilder = StringBuilder()
            file.forEachLine(Main.CHARSET) {
                if (title.isEmpty()) {
                    title = it
                } else {
                    if (it.equals("---", true)) {
                        descriptionBuilder.append("${ChatColor.DARK_GRAY} (続く)")
                        descriptions.add(descriptionBuilder.toString().trim { it <= ' ' })
                        descriptionBuilder.setLength(0)
                    } else {
                        descriptionBuilder.append(it)
                    }
                }
            }

            descriptions.add(descriptionBuilder.toString().trim { it <= ' ' })

            missionStates.remove(MissionState.NONE.id)
            missionStates.add(id)
            bossBarList.add(id)

            val worldConfig = Main.worldConfig
            if (worldConfig.gameConfig.script)
                Bukkit.getPluginManager().callEvent(MissionStartEvent(id))

            return startMission(title, descriptions, MissionType.MISSION, material)
        }
    }

    enum class MissionState(val id: Int, val material: Material, val barColor: BarColor) {
        // プラグインで登録されているミッション
        SUCCESS(1, Material.EMERALD_BLOCK, BarColor.GREEN),
        AREA_EXTEND(2, Material.GOLD_BLOCK, BarColor.YELLOW),
        HUNTER_ZONE(3, Material.BONE_BLOCK, BarColor.BLUE),
        TIMED_DEVICE(4, Material.PAPER, BarColor.PURPLE),

        OTHER(0, Material.QUARTZ_BLOCK, BarColor.WHITE),
        NONE(-1, Material.QUARTZ_BLOCK, BarColor.WHITE);

        companion object {
            fun getMission(id: Int): MissionState {
                return values().firstOrNull { it.id == id } ?: NONE
            }
        }
    }

    enum class MissionType(val id: Int, val detailType: MutableSet<MissionDetailType>) {
        MISSION(1, mutableSetOf(MissionDetailType.MISSION)),
        TUTATU_HINT(2, mutableSetOf(MissionDetailType.TUTATU, MissionDetailType.HINT)),
        END(3, mutableSetOf(MissionDetailType.MISSION)),

        NONE(-1, mutableSetOf());
    }

    enum class MissionDetailType(val id: Int, val detailName: String) {
        MISSION(1, "ミッション"),
        TUTATU(2, "通達"),
        HINT(3, "ヒント")
    }
}