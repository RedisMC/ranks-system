package net.silexpvp.ranks.rank.command.argument;

import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.CommandParent;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankSuffixArgument extends CommandArgument<RanksPlugin> {

    public RankSuffixArgument(CommandParent parent) {
        super(parent, "suffix", "Manages the suffix of a rank");
    }

    @Override
    public String getUsage(String label) {
        return super.getUsage(label) + " <rank> <set/remove> [suffix]";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 4) {
            sendUsage(sender, label, getUsage(label));
            return;
        }

        Rank rank = plugin.getRankManager().getByName(args[1]);
        if (rank == null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name doesn't exist.");
            return;
        }

        switch (args[2]) {
            case "set": {
                rank.setSuffix(StringUtils.join(args, " ", 3, args.length));
                rank.save();
                broadcastCommandMessage(sender, ChatColor.YELLOW + "You have set the suffix of the rank '" + ChatColor.WHITE + rank.getName() + ChatColor.YELLOW + "' to '" + ChatColor.translateAlternateColorCodes('&', rank.getPrefix()) + ChatColor.YELLOW + "'.");
                break;
            }

            case "remove":
            case "delete": {
                rank.setSuffix("");
                rank.save();
                broadcastCommandMessage(sender, ChatColor.YELLOW + "You have removed the suffix of the rank '" + ChatColor.WHITE + rank.getName() + ChatColor.YELLOW + "'.");
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
