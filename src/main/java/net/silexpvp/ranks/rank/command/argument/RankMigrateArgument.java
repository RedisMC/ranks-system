package net.silexpvp.ranks.rank.command.argument;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.caching.MetaData;
import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.CommandParent;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.OptionalInt;
import java.util.Set;

public class RankMigrateArgument extends CommandArgument<RanksPlugin> {

    public RankMigrateArgument(CommandParent parent) {
        super(parent, "migrate", "Migrates ranks from LuckPerms");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        LuckPermsApi api = LuckPerms.getApi();
        Set<Group> groups = api.getGroups();

        broadcastCommandMessage(sender, ChatColor.YELLOW + "*** Started creating all the ranks ***");

        for (Group group : groups) {
            Rank rank = new Rank(group.getName());

            OptionalInt weight = group.getWeight();
            if (weight.isPresent()) {
                rank.setWeight(weight.getAsInt());
            }

            MetaData metaData = group.getCachedData().getMetaData(api.getContextsForPlayer(sender));

            String prefix = metaData.getPrefix();
            if (prefix != null) {
                rank.setPrefix(prefix);
            }

            String suffix = metaData.getSuffix();
            if (suffix != null) {
                rank.setSuffix(suffix);
            }

            for (Node node : group.getOwnNodes()) {
                String permission = node.getPermission();
                if (!rank.hasPermission(permission)) {
                    rank.addPermission(permission);
                }
            }
        }

        broadcastCommandMessage(sender, ChatColor.YELLOW + "*** Finished creating all the ranks ***");

        for (Group group : groups) {
            Rank rank = plugin.getRankManager().getByName(group.getName());

            for (Group parentGroup : groups) {
                Rank parent = plugin.getRankManager().getByName(parentGroup.getName());

                if (group.inheritsGroup(parentGroup)) {
                    if (rank.canParent(parent)) {
                        rank.getParents().add(parent);
                    }
                }
            }
        }

        broadcastCommandMessage(sender, ChatColor.YELLOW + "*** Finished resolving inheritances ***");

        plugin.getRankManager().getRanks().forEach(Rank::asyncSave);

        broadcastCommandMessage(sender, ChatColor.YELLOW + "*** Process Complete ***");
    }
}
