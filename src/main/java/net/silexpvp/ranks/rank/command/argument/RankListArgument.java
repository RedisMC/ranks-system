package net.silexpvp.ranks.rank.command.argument;

import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.CommandParent;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.menu.RankListMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankListArgument extends CommandArgument<RanksPlugin> {
    public RankListArgument(CommandParent parent) {
        super(parent, "list", "Lists all the ranks");
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        new RankListMenu().open((Player) sender);
    }
}
