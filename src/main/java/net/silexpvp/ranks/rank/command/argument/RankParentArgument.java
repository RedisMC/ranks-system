package net.silexpvp.ranks.rank.command.argument;

import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.CommandParent;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankParentArgument extends CommandArgument<RanksPlugin> {

    public RankParentArgument(CommandParent parent) {
        super(parent, "parent", "Manages the parents of a rank", "parents");
    }

    @Override
    public String getUsage(String label) {
        return super.getUsage(label) + " <rank> <add/remove> <parent>";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length != 4) {
            sendUsage(sender, label, getUsage(label));
            return;
        }

        Rank rank = plugin.getRankManager().getByName(args[1]);
        if (rank == null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name doesn't exist.");
            return;
        }

        Rank parent = plugin.getRankManager().getByName(args[3]);
        if (parent == null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name doesn't exist.");
            return;
        }

        switch (args[2]) {
            case "set":
            case "add": {
                if (rank.canParent(parent)) {
                    rank.getParents().add(parent);
                    rank.save();

                    broadcastCommandMessage(sender, ChatColor.YELLOW + "You have successfully added the parent '" + ChatColor.WHITE + parent.getName() + ChatColor.YELLOW + "' to the rank '" + ChatColor.WHITE + rank.getName() + ChatColor.YELLOW + "'.");

                    return;
                }

                sender.sendMessage(ChatColor.RED + "You can't add the parent '" + parent.getName() + "' to the rank '" + rank.getName() + "'.");

                break;
            }

            case "unset":
            case "delete":
            case "remove": {
                if (rank.isParent(parent)) {
                    rank.getParents().remove(parent);
                    rank.save();

                    broadcastCommandMessage(sender, ChatColor.YELLOW + "You have successfully removed the parent '" + ChatColor.WHITE + parent.getName() + ChatColor.YELLOW + "' from the rank '" + ChatColor.WHITE + rank.getName() + ChatColor.YELLOW + "'.");

                    return;
                }

                sender.sendMessage(ChatColor.RED + "You can't remove parent '" + parent.getName() + "' from the rank '" + rank.getName() + "'.");

                break;
            }

            default: {
                sendUsage(sender, label, getUsage(label));
                break;
            }
        }
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
