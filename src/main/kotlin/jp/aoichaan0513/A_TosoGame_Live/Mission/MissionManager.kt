package jp.aoichaan0513.A_TosoGame_Live.Mission

import jp.aoichaan0513.A_TosoGame_Live.API.Enums.ItemType
import jp.aoichaan0513.A_TosoGame_Live.API.Interfaces.IMission
import jp.aoichaan0513.A_TosoGame_Live.API.Listener.MissionEndEvent
import jp.aoichaan0513.A_TosoGame_Live.API.Listener.MissionStartEvent
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.GameManager
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Player.PlayerManager
import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Listeners.Minecraft.onInteract
import jp.aoichaan0513.A_TosoGame_Live.Main
import jp.aoichaan0513.A_TosoGame_Live.Utils.DateTime.TimeFormat
import jp.aoichaan0513.A_TosoGame_Live.Utils.color
import jp.aoichaan0513.A_TosoGame_Live.Utils.isAdminTeam
import jp.aoichaan0513.A_TosoGame_Live.Utils.isHunterGroup
import jp.aoichaan0513.A_TosoGame_Live.Utils.isPlayerGroup
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
        // ボスバーに表示する内容
        var bossBarList = mutableListOf(-1)
            private set

        // 実行中のミッション
        var missionStates = mutableSetOf<Int>()
            private set

        val missionTitleMap = mutableMapOf<Int, String>()

        val missionMap = mutableMapOf<MissionType, MutableList<IMission>>(
                MissionType.MISSION to mutableListOf(),
                MissionType.TUTATU_HINT to mutableListOf(),
                MissionType.END to mutableListOf()
        )

        var bossBar: BossBar? = null
            private set

        var bossBarCount = 10

        fun isMission(id: Int): Boolean {
            return missionStates.isNotEmpty() && missionStates.contains(id)
        }

        fun isMission(missionState: MissionState): Boolean {
            return isMission(missionState.id)
        }

        val isMissions
            get() = missionStates.isNotEmpty()

        val isInventoryAllowOpenMission
            get() = isMission(MissionState.AREA_EXTEND)

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

            if (GameManager.isGame() && p.isAdminTeam) {
                p.inventory.setItem(PlayerManager.loadConfig(p).inventoryConfig.getSlot(ItemType.BOOK), itemStack)
            } else {
                p.inventory.addItem(itemStack)
            }

            p.updateInventory()
            return itemStack
        }

        fun setBook(p: Player): ItemStack? {
            if (p.isHunterGroup) return null

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

        fun getMission(count: Int, type: MissionType): IMission? {
            return getMissions(type).firstOrNull { it.count == count }
        }

        fun sendMission(sender: CommandSender, i: Int) {
            if (!GameManager.isGame(GameManager.GameState.GAME) || !hasFile(i)) return

            when (i) {
                MissionState.SUCCESS.id -> {
                    if (onInteract.successBlockLoc != null) {
                        sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}生存ミッションを開始しました。")
                        sendMissionAPI(sender, i)
                    } else {
                        sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}このミッションはコマンドから実行できません。")
                    }
                }
                MissionState.HUNTER_ZONE.id -> {
                    if (onInteract.hunterZoneBlockLoc != null) {
                        if (HunterZone.hunterSetCount > 0) {
                            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SECONDARY)}ハンターゾーンミッションを開始しました。")
                            sendMissionAPI(sender, i)
                        } else {
                            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}応募したハンターがいないためこのミッションは実行できません。")
                        }
                    } else {
                        sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}このミッションはコマンドから実行できません。")
                    }
                }
                MissionState.TIMED_DEVICE.id -> {
                    val playerList = mutableListOf<UUID>()
                    Bukkit.getOnlinePlayers().filter { it.isPlayerGroup }.forEach { playerList.add(it.uniqueId) }

                    val divideList = MainAPI.divide(playerList, 2)

                    if (divideList.size in 1..30 && playerList.size % 2 == 0) sendMissionAPI(sender, i)
                    else sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.ERROR)}このミッションは実行できません。")
                }
                else -> sendMissionAPI(sender, i)
            }
        }

        fun sendMission(sender: CommandSender, state: MissionState) {
            sendMission(sender, state.id)
        }

        fun sendMission(sender: CommandSender, id: Int, descriptions: List<String>, missionType: MissionType = MissionType.TUTATU_HINT, detailType: MissionDetailType = MissionDetailType.TUTATU, material: Material = Material.QUARTZ_BLOCK) {
            if (!GameManager.isGame(GameManager.GameState.GAME)) return

            val id = startMission(id, detailType.detailName, descriptions, missionType, material)
            TosoGameAPI.sendNotificationSound()

            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}${detailType.detailName}を送信しました。")
            Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}ミッションが通知されました。")

            for (player in Bukkit.getOnlinePlayers())
                player.sendTitle("", "${ChatColor.GRAY}${ChatColor.BOLD}ミッションが通知されたようだ…", 10, 50, 20)

            val textComponent1 = TextComponent(MainAPI.getPrefix(MainAPI.PrefixType.WARNING))
            val textComponent2 = TextComponent("${ChatColor.BOLD}${ChatColor.UNDERLINE}ここ${ChatColor.RESET}")
            textComponent2.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu open ${detailType.name.toLowerCase()} $id")
            val textComponent3 = TextComponent("${ChatColor.YELLOW}をクリックして詳細を確認できます。")
            textComponent1.addExtra(textComponent2)
            textComponent1.addExtra(textComponent3)

            Bukkit.getOnlinePlayers().filter { !it.isHunterGroup }.forEach { it.spigot().sendMessage(textComponent1) }
            return
        }

        fun sendMission(sender: CommandSender, id: Int, description: String, missionType: MissionType = MissionType.TUTATU_HINT, detailType: MissionDetailType = MissionDetailType.TUTATU, material: Material = Material.QUARTZ_BLOCK) {
            sendMission(sender, id, listOf(description.trim().color()), missionType, detailType, material)
        }


        fun setBossBar() {
            if (GameManager.isGame()) {
                if (bossBarList.filter { it == -1 || isMission(it) }.isEmpty()) return

                val result = bossBarList.filter { it == -1 || isMission(it) }[0]

                bossBarList.removeAt(0)
                bossBarList.add(result)

                when (result) {
                    -1 -> {
                        object : BukkitRunnable() {
                            var i = 10

                            override fun run() {
                                if (i < 0) cancel()

                                if (isMissions) {
                                    bossBar?.players?.filter { it.isHunterGroup }?.forEach { bossBar?.removePlayer(it) }

                                    val stringBuilder = StringBuilder()
                                    for (id in missionStates)
                                        stringBuilder.append("${ChatColor.GOLD}${ChatColor.BOLD}${missionTitleMap[id] ?: "Unknown"}${ChatColor.RESET}${ChatColor.GRAY}, ")

                                    bossBar = createBossBar(stringBuilder.toString().trim().substring(0, stringBuilder.toString().trim().length - 5), 1.toDouble(), BarColor.YELLOW, BarStyle.SOLID)

                                    Bukkit.getOnlinePlayers().filter { !it.isHunterGroup }.forEach { bossBar?.addPlayer(it) }
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
                                if (i < 0 || !isMission(MissionState.HUNTER_ZONE) || HunterZone.internalMissionState != InternalMissionState.START) cancel()

                                bossBar?.players?.filter { it.isHunterGroup }?.forEach { bossBar?.removePlayer(it) }

                                val bossBar = createBossBar(
                                        "${ChatColor.YELLOW}${ChatColor.BOLD}ハンターゾーンミッション終了まで${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.RESET}${TimeFormat.formatJapan(HunterZone.getMissionTime().toInt())}",
                                        if (isMission(MissionState.HUNTER_ZONE)) HunterZone.getMissionTime() / (HunterZone.getInitialMissionTime() / 20) else 1.toDouble(),
                                        MissionState.HUNTER_ZONE.barColor,
                                        BarStyle.SOLID
                                )

                                Bukkit.getOnlinePlayers().filter { !it.isHunterGroup }.forEach { bossBar?.addPlayer(it) }

                                i--
                            }
                        }.runTaskTimer(Main.pluginInstance, 0, 20)
                    }
                    MissionState.TIMED_DEVICE.id -> {
                        object : BukkitRunnable() {
                            var i = 10

                            override fun run() {
                                if (i < 0 || !isMission(MissionState.TIMED_DEVICE) || TimedDevice.internalMissionState != InternalMissionState.START) cancel()

                                bossBar?.players?.filter { it.isHunterGroup }?.forEach { bossBar?.removePlayer(it) }

                                val bossBar = createBossBar(
                                        "${ChatColor.YELLOW}${ChatColor.BOLD}時限装置解除ミッション終了まで${ChatColor.RESET}${ChatColor.GRAY}: ${ChatColor.RESET}${TimeFormat.formatJapan(TimedDevice.getMissionTime().toInt())}",
                                        if (isMission(MissionState.TIMED_DEVICE)) TimedDevice.getMissionTime() / (TimedDevice.getInitialMissionTime() / 20) else 1.toDouble(),
                                        MissionState.TIMED_DEVICE.barColor,
                                        BarStyle.SOLID
                                )

                                Bukkit.getOnlinePlayers().filter { !it.isHunterGroup }.forEach { bossBar?.addPlayer(it) }

                                i--
                            }
                        }.runTaskTimer(Main.pluginInstance, 0, 20)
                    }
                    else -> {
                        object : BukkitRunnable() {
                            var i = 10

                            override fun run() {
                                if (i < 0) cancel()

                                bossBar?.players?.filter { it.isHunterGroup }?.forEach { bossBar?.removePlayer(it) }

                                val bossBar = createBossBar("${ChatColor.BOLD}${missionTitleMap[result]}", 1.toDouble(), MissionState.getMission(result).barColor, BarStyle.SOLID)

                                Bukkit.getOnlinePlayers().filter { !it.isHunterGroup }.forEach { bossBar?.addPlayer(it) }

                                i--
                            }
                        }.runTaskTimer(Main.pluginInstance, 0, 20)
                    }
                }

                Bukkit.getScheduler().runTaskLater(Main.pluginInstance, Runnable { setBossBar() }, 20 * 10)
            } else {
                bossBar?.removeAll()
                bossBar = null
            }
        }

        fun resetBossBar() {
            bossBar?.removeAll()
            bossBar = null
            bossBarList = mutableListOf(-1)
        }

        private fun createBossBar(title: String?, progress: Double = 1.toDouble(), barColor: BarColor = BarColor.WHITE, barStyle: BarStyle = BarStyle.SOLID): BossBar? {
            if (bossBar == null)
                bossBar = Bukkit.createBossBar(title, barColor, barStyle)

            bossBar?.setTitle(title)
            bossBar?.progress = if (progress in 0.toDouble()..1.toDouble()) progress else 1.toDouble()
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

            return sendFileMissionAPI(id, missionState)
        }

        private fun startMission(missionState: MissionState): Int {
            if (missionState == MissionState.OTHER || missionState == MissionState.NONE
                    || !hasFile(missionState.id)) return -1

            when (missionState) {
                MissionState.HUNTER_ZONE -> HunterZone.startMission()
                MissionState.TIMED_DEVICE -> TimedDevice.startMission()
            }

            return sendFileMissionAPI(missionState.id, missionState)
        }

        private fun startMission(id: Int, title: String, descriptions: List<String>, type: MissionType, material: Material): Int {
            val count = if (type == MissionType.MISSION) getMissions(MissionType.MISSION).size + getMissions(MissionType.END).size else getMissions(type).size

            val list = getMissions(type)

            val mission = IMission(id, count + 1, title, descriptions, type, material)
            list.add(mission)
            missionMap[type] = list
            return mission.count
        }

        private fun endMission(id: Int) {
            if (!isMissions) return

            Bukkit.getPluginManager().callEvent(MissionEndEvent(id))

            val listMission = getMissions(MissionType.MISSION)
            val listEnd = getMissions(MissionType.END)

            val mission = listMission.firstOrNull { it.id == id } ?: return
            listEnd.add(mission)

            missionMap[MissionType.MISSION] = listMission.filter { it.id != id }.toMutableList()
            missionMap[MissionType.END] = listEnd

            bossBarList.remove(id)
            missionStates.remove(id)
        }

        fun endMission(missionState: MissionState) {
            endMission(missionState.id)
        }

        fun endMissions() {
            HunterZone.endMission()
            TimedDevice.endMission()

            val states = missionStates.toSet()
            for (id in states)
                endMission(id)
        }

        fun resetMission() {
            resetBossBar()
            missionStates.clear()

            for (key in missionMap.keys)
                missionMap[key] = mutableListOf()
        }

        private fun sendMissionAPI(sender: CommandSender, i: Int) {
            if (!GameManager.isGame(GameManager.GameState.GAME) || !hasFile(i)) return

            val id = startMission(i)
            TosoGameAPI.sendNotificationSound()

            sender.sendMessage("${MainAPI.getPrefix(MainAPI.PrefixType.SUCCESS)}ミッションを開始しました。")
            Bukkit.broadcastMessage("${MainAPI.getPrefix(MainAPI.PrefixType.WARNING)}ミッションが通知されました。")

            for (player in Bukkit.getOnlinePlayers())
                player.sendTitle("", "${ChatColor.GRAY}${ChatColor.BOLD}ミッションが通知されたようだ…", 10, 50, 20)

            val textComponent1 = TextComponent(MainAPI.getPrefix(MainAPI.PrefixType.WARNING))
            val textComponent2 = TextComponent("${ChatColor.BOLD}${ChatColor.UNDERLINE}ここ${ChatColor.RESET}")
            textComponent2.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu open mission $id")
            val textComponent3 = TextComponent("${ChatColor.YELLOW}をクリックして詳細を確認できます。")
            textComponent1.addExtra(textComponent2)
            textComponent1.addExtra(textComponent3)

            Bukkit.getOnlinePlayers().filter { !it.isHunterGroup }.forEach { it.spigot().sendMessage(textComponent1) }
        }

        private fun sendFileMissionAPI(id: Int, missionState: MissionState): Int {
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

            missionStates.add(id)
            bossBarList = Stream.concat(mutableSetOf(-1).stream(), missionStates.stream()).toList().toMutableList()

            val worldConfig = Main.worldConfig
            if (worldConfig.gameConfig.script)
                Bukkit.getPluginManager().callEvent(MissionStartEvent(id))

            return startMission(missionState.id, title, descriptions, MissionType.MISSION, missionState.material)
        }
    }

    // ミッションの列挙値
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

    // ミッションの大まかな種類
    enum class MissionType(val id: Int, val detailType: MutableSet<MissionDetailType>) {
        MISSION(1, mutableSetOf(MissionDetailType.MISSION)),
        TUTATU_HINT(2, mutableSetOf(MissionDetailType.TUTATU, MissionDetailType.HINT)),
        END(3, mutableSetOf(MissionDetailType.MISSION)),

        NONE(-1, mutableSetOf());
    }

    // ミッションの細かな種類
    enum class MissionDetailType(val id: Int, val detailName: String) {
        MISSION(1, "ミッション"),
        TUTATU(2, "通達"),
        HINT(3, "ヒント")
    }

    // 各ミッション内部の開始状態
    enum class InternalMissionState {
        START,
        END,
        NONE
    }
}