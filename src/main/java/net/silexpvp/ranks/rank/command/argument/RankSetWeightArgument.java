package net.silexpvp.ranks.rank.command.argument;

import net.silexpvp.nightmare.util.JavaUtils;
import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.CommandParent;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankSetWeightArgument extends CommandArgument<RanksPlugin> {

    public RankSetWeightArgument(CommandParent parent) {
        super(parent, "setweight", "Sets the weight a rank", "weight");
    }

    @Override
    public String getUsage(String label) {
        return super.getUsage(label) + " <rank> <weight>";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length != 3) {
            sendUsage(sender, label, getUsage(label));
            return;
        }

        Rank rank = plugin.getRankManager().getByName(args[1]);
        if (rank == null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name doesn't exist.");
            return;
        }

        Integer weight = JavaUtils.tryParseInteger(args[2]);
        if (weight == null) {
            sender.sendMessage(ChatColor.RED + "'" + weight + "' is valid weight.");
            return;
        }

        rank.setWeight(weight);
        rank.save();

        broadcastCommandMessage(sender, ChatColor.YELLOW + "You have successfully set the weight of '" + ChatColor.WHITE + args[1] + ChatColor.YELLOW + "' to '" + ChatColor.WHITE + weight + ChatColor.YELLOW + "'.");
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
