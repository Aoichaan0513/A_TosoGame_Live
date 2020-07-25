package jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard

import jp.aoichaan0513.A_TosoGame_Live.API.TosoGameAPI
import jp.aoichaan0513.A_TosoGame_Live.Utils.PlayerUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

class Teams {
    companion object {

        lateinit var teamAdmin: Team
        lateinit var teamPlayer: Team
        lateinit var teamHunter: Team
        lateinit var teamJail: Team
        lateinit var teamSuccess: Team
        lateinit var teamTuho: Team

        val teamAdminObjectName = TosoGameAPI.Objective.TEAM_ADMIN.objectName
        val teamPlayerObjectName = TosoGameAPI.Objective.TEAM_PLAYER.objectName
        val teamHunterObjectName = TosoGameAPI.Objective.TEAM_HUNTER.objectName
        val teamJailObjectName = TosoGameAPI.Objective.TEAM_JAIL.objectName
        val teamSuccessObjectName = TosoGameAPI.Objective.TEAM_SUCCESS.objectName
        val teamTuhoObjectName = TosoGameAPI.Objective.TEAM_TUHO.objectName


        fun setScoreboard(): org.bukkit.scoreboard.Scoreboard {
            val scoreboard = Bukkit.getScoreboardManager()!!.mainScoreboard

            teamAdmin = scoreboard.getTeam(teamAdminObjectName) ?: scoreboard.registerNewTeam(teamAdminObjectName).run {
                prefix = OnlineTeam.TOSO_ADMIN.color.toString()
                suffix = ChatColor.RESET.toString()
                displayName = teamAdminObjectName
                setAllowFriendlyFire(false)
                setCanSeeFriendlyInvisibles(true)
                setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)
                this
            }

            teamPlayer = scoreboard.getTeam(teamPlayerObjectName)
                    ?: scoreboard.registerNewTeam(teamPlayerObjectName).run {
                        prefix = ChatColor.RESET.toString()
                        suffix = ChatColor.RESET.toString()
                        displayName = teamPlayerObjectName
                        setAllowFriendlyFire(false)
                        setCanSeeFriendlyInvisibles(true)
                        setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                        setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
                        this
                    }

            teamHunter = scoreboard.getTeam(teamHunterObjectName)
                    ?: scoreboard.registerNewTeam(teamHunterObjectName).run {
                        prefix = OnlineTeam.TOSO_HUNTER.color.toString()
                        suffix = ChatColor.RESET.toString()
                        displayName = teamHunterObjectName
                        setAllowFriendlyFire(false)
                        setCanSeeFriendlyInvisibles(true)
                        setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                        setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
                        this
                    }

            teamJail = scoreboard.getTeam(teamJailObjectName) ?: scoreboard.registerNewTeam(teamJailObjectName).run {
                prefix = OnlineTeam.TOSO_JAIL.color.toString()
                suffix = ChatColor.RESET.toString()
                displayName = teamJailObjectName
                setAllowFriendlyFire(false)
                setCanSeeFriendlyInvisibles(true)
                setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)
                this
            }

            teamSuccess = scoreboard.getTeam(teamSuccessObjectName)
                    ?: scoreboard.registerNewTeam(teamSuccessObjectName).run {
                        prefix = OnlineTeam.TOSO_SUCCESS.color.toString()
                        suffix = ChatColor.RESET.toString()
                        displayName = teamSuccessObjectName
                        setAllowFriendlyFire(false)
                        setCanSeeFriendlyInvisibles(true)
                        setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                        setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
                        this
                    }

            teamTuho = scoreboard.getTeam(teamTuhoObjectName) ?: scoreboard.registerNewTeam(teamTuhoObjectName).run {
                prefix = OnlineTeam.TOSO_TUHO.color.toString()
                suffix = ChatColor.RESET.toString()
                displayName = teamTuhoObjectName
                setAllowFriendlyFire(false)
                setCanSeeFriendlyInvisibles(true)
                setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
                this
            }

            return scoreboard
        }

        fun setScoreboard(scoreboard: org.bukkit.scoreboard.Scoreboard): org.bukkit.scoreboard.Scoreboard {
            scoreboard.getTeam(teamAdminObjectName) ?: scoreboard.registerNewTeam(teamAdminObjectName).run {
                prefix = OnlineTeam.TOSO_ADMIN.color.toString()
                suffix = ChatColor.RESET.toString()
                displayName = teamAdminObjectName
                setAllowFriendlyFire(false)
                setCanSeeFriendlyInvisibles(true)
                setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)

                teamAdmin.entries.forEach { addEntry(it) }

                this
            }

            scoreboard.getTeam(teamPlayerObjectName) ?: scoreboard.registerNewTeam(teamPlayerObjectName).run {
                prefix = ChatColor.RESET.toString()
                suffix = ChatColor.RESET.toString()
                displayName = teamPlayerObjectName
                setAllowFriendlyFire(false)
                setCanSeeFriendlyInvisibles(true)
                setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)

                teamPlayer.entries.forEach { addEntry(it) }

                this
            }

            scoreboard.getTeam(teamHunterObjectName) ?: scoreboard.registerNewTeam(teamHunterObjectName).run {
                prefix = OnlineTeam.TOSO_HUNTER.color.toString()
                suffix = ChatColor.RESET.toString()
                displayName = teamHunterObjectName
                setAllowFriendlyFire(false)
                setCanSeeFriendlyInvisibles(true)
                setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)

                teamHunter.entries.forEach { addEntry(it) }

                this
            }

            scoreboard.getTeam(teamJailObjectName) ?: scoreboard.registerNewTeam(teamJailObjectName).run {
                prefix = OnlineTeam.TOSO_JAIL.color.toString()
                suffix = ChatColor.RESET.toString()
                displayName = teamJailObjectName
                setAllowFriendlyFire(false)
                setCanSeeFriendlyInvisibles(true)
                setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)

                teamJail.entries.forEach { addEntry(it) }

                this
            }

            scoreboard.getTeam(teamSuccessObjectName) ?: scoreboard.registerNewTeam(teamSuccessObjectName).run {
                prefix = OnlineTeam.TOSO_SUCCESS.color.toString()
                suffix = ChatColor.RESET.toString()
                displayName = teamSuccessObjectName
                setAllowFriendlyFire(false)
                setCanSeeFriendlyInvisibles(true)
                setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)

                teamSuccess.entries.forEach { addEntry(it) }

                this
            }

            scoreboard.getTeam(teamTuhoObjectName) ?: scoreboard.registerNewTeam(teamTuhoObjectName).run {
                prefix = OnlineTeam.TOSO_TUHO.color.toString()
                suffix = ChatColor.RESET.toString()
                displayName = teamTuhoObjectName
                setAllowFriendlyFire(false)
                setCanSeeFriendlyInvisibles(true)
                setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)

                teamTuho.entries.forEach { addEntry(it) }

                this
            }

            return scoreboard
        }

        fun resetScoreboard() {
            teamAdmin.unregister()
            teamPlayer.unregister()
            teamHunter.unregister()
            teamJail.unregister()
            teamSuccess.unregister()
            teamTuho.unregister()
            setScoreboard()
        }

        fun resetScoreboard(scoreboard: org.bukkit.scoreboard.Scoreboard) {
            scoreboard.getTeam(teamAdminObjectName)!!.unregister()
            scoreboard.getTeam(teamPlayerObjectName)!!.unregister()
            scoreboard.getTeam(teamHunterObjectName)!!.unregister()
            scoreboard.getTeam(teamJailObjectName)!!.unregister()
            scoreboard.getTeam(teamSuccessObjectName)!!.unregister()
            scoreboard.getTeam(teamTuhoObjectName)!!.unregister()
            setScoreboard(scoreboard)
        }

        fun getJoinedTeam(p: Player): OnlineTeam {
            return if (teamAdmin.hasEntry(p.name)) OnlineTeam.TOSO_ADMIN
            else if (teamHunter.hasEntry(p.name)) OnlineTeam.TOSO_HUNTER
            else if (teamJail.hasEntry(p.name)) OnlineTeam.TOSO_JAIL
            else if (teamSuccess.hasEntry(p.name)) OnlineTeam.TOSO_SUCCESS
            else if (teamTuho.hasEntry(p.name)) OnlineTeam.TOSO_TUHO
            else OnlineTeam.TOSO_PLAYER
        }

        fun getJoinedTeam(p: Player, board: org.bukkit.scoreboard.Scoreboard): OnlineTeam {
            return if (board.getTeam(OnlineTeam.TOSO_ADMIN.name)!!.hasEntry(p.name)) OnlineTeam.TOSO_ADMIN
            else if (board.getTeam(OnlineTeam.TOSO_HUNTER.name)!!.hasEntry(p.name)) OnlineTeam.TOSO_HUNTER
            else if (board.getTeam(OnlineTeam.TOSO_JAIL.name)!!.hasEntry(p.name)) OnlineTeam.TOSO_JAIL
            else if (board.getTeam(OnlineTeam.TOSO_SUCCESS.name)!!.hasEntry(p.name)) OnlineTeam.TOSO_SUCCESS
            else if (board.getTeam(OnlineTeam.TOSO_TUHO.name)!!.hasEntry(p.name)) OnlineTeam.TOSO_TUHO
            else OnlineTeam.TOSO_PLAYER
        }

        fun getOnlineCount(team: OnlineTeam): Int {
            return team.team.entries.filter { Bukkit.getPlayerExact(it) != null }.size
        }

        fun hasJoinedTeam(team: OnlineTeam, p: Player): Boolean {
            return team.team.hasEntry(p.name)
        }

        fun hasJoinedTeams(p: Player): Boolean {
            return OnlineTeam.values().any { hasJoinedTeam(it, p) }
        }

        fun joinTeam(team: OnlineTeam, p: Player) {
            team.team.addEntry(p.name)

            setTeamOption(p)
            Scoreboard.boardMap.filter { Bukkit.getPlayer(it.key) != null }.values.forEach {
                team.getTeam(it).addEntry(p.name)
                setTeamOption(it)
            }

            PlayerUtil.setSidebar()
            return
        }

        fun leaveTeam(team: OnlineTeam, p: Player) {
            team.team.removeEntry(p.name)

            // setTeamOptions();
            setTeamOption(p)
            for (board in Scoreboard.boardMap.values)
                team.getTeam(board).removeEntry(p.name)

            PlayerUtil.setSidebar()
            return
        }

        fun setTeamOptions() {
            for (scoreboard in Scoreboard.boardMap.values)
                setTeamOption(scoreboard)
        }

        fun setTeamOption(p: Player) {
            setTeamOption(Scoreboard.getBoard(p))
        }

        fun setTeamOption(scoreboard: org.bukkit.scoreboard.Scoreboard) {
            for (team in scoreboard.teams) {
                team.color = OnlineTeam.getOnlineTeam(team.displayName).color
                team.prefix = OnlineTeam.getOnlineTeam(team.displayName).color.toString()
                team.suffix = ChatColor.RESET.toString()
                team.setAllowFriendlyFire(false)
                team.setCanSeeFriendlyInvisibles(true)
                team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                team.setOption(Team.Option.NAME_TAG_VISIBILITY, if (team.displayName.equals(OnlineTeam.TOSO_ADMIN.name, true) || team.displayName.equals(OnlineTeam.TOSO_JAIL.name, true)) Team.OptionStatus.ALWAYS else Team.OptionStatus.NEVER)

                for (entry in Bukkit.getScoreboardManager()!!.mainScoreboard.getTeam(team.name)?.entries ?: continue)
                    team.addEntry(entry)
            }
        }

        fun getTeamLabel(slot: DisplaySlot, p: Player): String {
            return if (slot == DisplaySlot.CHAT) {
                if (teamAdmin.hasEntry(p.name)) "${OnlineTeam.TOSO_ADMIN.color}[${OnlineTeam.TOSO_ADMIN.displayName}]${ChatColor.RESET}"
                else if (teamPlayer.hasEntry(p.name)) "${OnlineTeam.TOSO_PLAYER.color}[${OnlineTeam.TOSO_PLAYER.displayName}]${ChatColor.RESET}"
                else if (teamHunter.hasEntry(p.name)) "${OnlineTeam.TOSO_HUNTER.color}[${OnlineTeam.TOSO_HUNTER.displayName}]${ChatColor.RESET}"
                else if (teamJail.hasEntry(p.name)) "${OnlineTeam.TOSO_JAIL.color}[${OnlineTeam.TOSO_JAIL.displayName}]${ChatColor.RESET}"
                else if (teamSuccess.hasEntry(p.name)) "${OnlineTeam.TOSO_SUCCESS.color}[${OnlineTeam.TOSO_SUCCESS.displayName}]${ChatColor.RESET}"
                else if (teamTuho.hasEntry(p.name)) "${OnlineTeam.TOSO_TUHO.color}[${OnlineTeam.TOSO_TUHO.displayName}]${ChatColor.RESET}"
                else ""
            } else if (slot == DisplaySlot.SIDEBAR) {
                if (teamAdmin.hasEntry(p.name)) "${OnlineTeam.TOSO_ADMIN.displayName}${ChatColor.RESET}"
                else if (teamPlayer.hasEntry(p.name)) "${OnlineTeam.TOSO_PLAYER.displayName}${ChatColor.RESET}"
                else if (teamHunter.hasEntry(p.name)) "${OnlineTeam.TOSO_HUNTER.displayName}${ChatColor.RESET}"
                else if (teamJail.hasEntry(p.name)) "${OnlineTeam.TOSO_JAIL.displayName}${ChatColor.RESET}"
                else if (teamSuccess.hasEntry(p.name)) "${OnlineTeam.TOSO_SUCCESS.displayName}${ChatColor.RESET}"
                else if (teamTuho.hasEntry(p.name)) "${OnlineTeam.TOSO_TUHO.displayName}${ChatColor.RESET}"
                else ""
            } else {
                ""
            }
        }
    }

    enum class OnlineTeam(val teamName: String, val color: ChatColor, val displayName: String) {
        TOSO_ADMIN(TosoGameAPI.Objective.TEAM_ADMIN.objectName, ChatColor.GOLD, "${ChatColor.GOLD}運営"),
        TOSO_PLAYER(TosoGameAPI.Objective.TEAM_PLAYER.objectName, ChatColor.WHITE, "${ChatColor.WHITE}逃走者"),
        TOSO_HUNTER(TosoGameAPI.Objective.TEAM_HUNTER.objectName, ChatColor.RED, "${ChatColor.RED}ハンター"),
        TOSO_JAIL(TosoGameAPI.Objective.TEAM_JAIL.objectName, ChatColor.BLACK, "${ChatColor.BLACK}牢獄"),
        TOSO_SUCCESS(TosoGameAPI.Objective.TEAM_SUCCESS.objectName, ChatColor.BLUE, "${ChatColor.BLUE}生存者"),
        TOSO_TUHO(TosoGameAPI.Objective.TEAM_TUHO.objectName, ChatColor.GREEN, "${ChatColor.GREEN}通報部隊"),

        UNKNOWN("", ChatColor.GRAY, "${ChatColor.GRAY}不明");

        val team: Team
            get() = when (this) {
                TOSO_ADMIN -> teamAdmin
                TOSO_PLAYER -> teamPlayer
                TOSO_HUNTER -> teamHunter
                TOSO_JAIL -> teamJail
                TOSO_SUCCESS -> teamSuccess
                TOSO_TUHO -> teamTuho
                UNKNOWN -> teamPlayer
            }

        fun getTeam(scoreboard: org.bukkit.scoreboard.Scoreboard): Team {
            return scoreboard.getTeam(teamName)!!
        }

        companion object {
            fun getOnlineTeam(name: String): OnlineTeam {
                return values().firstOrNull { it.teamName.equals(name, true) } ?: UNKNOWN
            }
        }
    }

    enum class OfflineTeam {
        TOSO_ADMIN,
        TOSO_PLAYER,
        TOSO_HUNTER,
        TOSO_JAIL,
        TOSO_TUHO
    }

    enum class DisplaySlot {
        CHAT,
        SIDEBAR
    }
}