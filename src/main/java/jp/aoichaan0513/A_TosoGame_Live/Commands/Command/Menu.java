package jp.aoichaan0513.A_TosoGame_Live.Commands.Command;

import jp.aoichaan0513.A_TosoGame_Live.API.Interfaces.IMission;
import jp.aoichaan0513.A_TosoGame_Live.API.MainAPI;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.Inventory.Right.MissionInventory;
import jp.aoichaan0513.A_TosoGame_Live.API.Manager.MissionManager;
import jp.aoichaan0513.A_TosoGame_Live.API.Scoreboard.Teams;
import jp.aoichaan0513.A_TosoGame_Live.Commands.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class Menu extends ICommand {

    public Menu(String name) {
        super(name);
    }

    @Override
    public void onPlayerCommand(Player sp, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("open")) {
                if (Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_ADMIN, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_PLAYER, sp) || Teams.hasJoinedTeam(Teams.OnlineTeam.TOSO_SUCCESS, sp)) {
                    if (args.length != 1) {
                        if (args[1].equalsIgnoreCase("mission")) {
                            if (args.length != 2) {
                                MissionManager.MissionBookType type = MissionManager.MissionBookType.MISSION;

                                try {
                                    int i = Integer.parseInt(args[2]);

                                    if (MissionInventory.getMissions(type).get(type.getId() + i) != null) {
                                        IMission mission = MissionInventory.getMissions(type).get(type.getId() + i);

                                        if (mission.getType() == MissionManager.MissionBookType.MISSION) {
                                            ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
                                            BookMeta bookMeta = (BookMeta) book.getItemMeta();
                                            bookMeta.setTitle("逃走中");
                                            bookMeta.setAuthor("A_TosoGame_Live");
                                            for (int v = 0; v < mission.getDescriptions().size(); v++)
                                                bookMeta.addPage((v == 0 ? ChatColor.DARK_RED + mission.getTitle() + ChatColor.RESET + "\n\n" : "") + mission.getDescriptions().get(v));
                                            book.setItemMeta(bookMeta);

                                            MissionInventory.openBook(book, sp);
                                            return;
                                        }
                                    }
                                } catch (NumberFormatException err) {
                                }
                            }
                            return;
                        } else if (args[1].equalsIgnoreCase("tutatu") || args[1].equalsIgnoreCase("hint")) {
                            if (args.length != 2) {
                                MissionManager.MissionBookType type = MissionManager.MissionBookType.TUTATU;

                                try {
                                    int i = Integer.parseInt(args[2]);

                                    if (MissionInventory.getMissions(type).get(type.getId() + i) != null) {
                                        IMission mission = MissionInventory.getMissions(type).get(type.getId() + i);

                                        if (mission.getType() == MissionManager.MissionBookType.TUTATU || mission.getType() == MissionManager.MissionBookType.HINT) {
                                            ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
                                            BookMeta bookMeta = (BookMeta) book.getItemMeta();
                                            bookMeta.setTitle("逃走中");
                                            bookMeta.setAuthor("A_TosoGame_Live");
                                            for (int v = 0; v < mission.getDescriptions().size(); v++)
                                                bookMeta.addPage((v == 0 ? ChatColor.DARK_RED + mission.getTitle() + ChatColor.RESET + "\n\n" : "") + mission.getDescriptions().get(v));
                                            book.setItemMeta(bookMeta);

                                            MissionInventory.openBook(book, sp);
                                            return;
                                        }
                                    }
                                } catch (NumberFormatException err) {
                                }
                            }
                            return;
                        }
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void onBlockCommand(BlockCommandSender bs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(bs, MainAPI.ErrorMessage.NOT_PLAYER);
        return;
    }

    @Override
    public void onConsoleCommand(ConsoleCommandSender cs, Command cmd, String label, String[] args) {
        MainAPI.sendMessage(cs, MainAPI.ErrorMessage.NOT_PLAYER);
        return;
    }

    @Override
    public List<String> onPlayerTabComplete(Player sp, Command cmd, String alias, String[] args) {
        return null;
    }

    @Override
    public List<String> onBlockTabComplete(BlockCommandSender bs, Command cmd, String alias, String[] args) {
        return null;
    }

    @Override
    public List<String> onConsoleTabComplete(ConsoleCommandSender cs, Command cmd, String alias, String[] args) {
        return null;
    }
}
