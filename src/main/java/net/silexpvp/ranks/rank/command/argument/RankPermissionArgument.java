package net.silexpvp.ranks.rank.command.argument;

import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.CommandParent;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankPermissionArgument extends CommandArgument<RanksPlugin> {

    public RankPermissionArgument(CommandParent parent) {
        super(parent, "permission", "Manages the permissions of a rank");
    }

    @Override
    public String getUsage(String label) {
        return super.getUsage(label) + " <rank> <add/remove> <permission>";
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

        String permission = args[3];

        switch (args[2]) {
            case "set":
            case "add": {
                if (rank.hasPermission(permission)) {
                    sender.sendMessage(ChatColor.RED + "The rank '" + rank.getName() + "' already has the permission '" + permission + "'.");
                    return;
                }

                rank.addPermission(permission);
                rank.save();

                broadcastCommandMessage(sender, ChatColor.YELLOW + "You have successfully added the permission '" + ChatColor.WHITE + permission + ChatColor.YELLOW + "' to the rank '" + ChatColor.WHITE + rank.getName() + ChatColor.YELLOW + "'.");

                break;
            }

            case "unset":
            case "delete":
            case "remove": {
                if (!rank.hasPermission(permission)) {
                    sender.sendMessage(ChatColor.RED + "The rank '" + rank.getName() + "' doesn't have the permission '" + permission + "'.");
                    return;
                }

                rank.removePermission(permission);
                rank.save();

                broadcastCommandMessage(sender, ChatColor.YELLOW + "You have successfully removed the permission '" + ChatColor.WHITE + permission + ChatColor.YELLOW + "' from the rank '" + ChatColor.WHITE + rank.getName() + ChatColor.YELLOW + "'.");

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
