package net.silexpvp.ranks.rank.command.argument;

import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.CommandParent;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankCreateArgument extends CommandArgument<RanksPlugin> {

    public RankCreateArgument(CommandParent parent) {
        super(parent, "create", "Creates a rank");
    }

    @Override
    public String getUsage(String label) {
        return super.getUsage(label) + " <name>";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length != 2) {
            sendUsage(sender, label, getUsage(label));
            return;
        }

        if (plugin.getRankManager().getByName(args[1]) != null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name already exists.");
            return;
        }

        Rank rank = new Rank(args[1]);
        rank.save();

        broadcastCommandMessage(sender, ChatColor.YELLOW + "You have successfully created the rank '" + ChatColor.WHITE + rank.getName() + ChatColor.YELLOW + "'.");
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
