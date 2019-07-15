package net.silexpvp.ranks.rank.command.argument;

import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.CommandParent;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankDeleteArgument extends CommandArgument<RanksPlugin> {

    public RankDeleteArgument(CommandParent parent) {
        super(parent, "delete", "Deletes a rank");
    }

    @Override
    public String getUsage(String label) {
        return super.getUsage(label) + " <rank>";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length != 2) {
            sendUsage(sender, label, getUsage(label));
            return;
        }

        Rank rank = plugin.getRankManager().getByName(args[1]);
        if (rank == null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name doesn't exist.");
            return;
        }

        rank.delete();

        broadcastCommandMessage(sender, ChatColor.YELLOW + "You have successfully deleted the rank '" + ChatColor.WHITE + args[1] + ChatColor.YELLOW + "'.");
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
