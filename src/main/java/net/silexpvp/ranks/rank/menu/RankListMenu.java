package net.silexpvp.ranks.rank.menu;

import net.silexpvp.nightmare.menu.type.PaginatedMenu;
import net.silexpvp.nightmare.util.ItemCreator;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.stream.Collectors;

public class RankListMenu extends PaginatedMenu<RanksPlugin> {

    private final Inventory inventory = plugin.getServer().createInventory(null, 27, getTitle());

    @Override
    public int getTotalPages() {
        return (plugin.getRankManager().getRanks().size() - 1) / 9;
    }

    @Override
    public String getTitle() {
        return "Ranks ┃ " + page + '/' + getTotalPages();
    }

    @Override
    public void update() {
        int totalPages = getTotalPages();

        if (page > totalPages) {
            page = totalPages;
        }

        inventory.clear();

        inventory.setItem(0, (new ItemCreator(Material.CARPET))
                .setDurability(page == 1 ? 7 : 14)
                .setDisplayName((page == 1 ? ChatColor.GRAY : ChatColor.RED) + "Previous Page")
                .create());

        inventory.setItem(8, (new ItemCreator(Material.CARPET))
                .setDurability(page + 1 > totalPages ? 7 : 13)
                .setDisplayName((page + 1 > totalPages ? ChatColor.GRAY : ChatColor.GREEN) + "Next Page")
                .create());

        int slot = 9;
        for (Rank rank : plugin.getRankManager().getRanks()) {
            inventory.setItem(slot++, new ItemCreator(Material.STAINED_CLAY).setDurability(1).setDisplayName(rank.getMainColor() + rank.getName())
                    .setLore(
                            "&7&m----------------------------------",
                            "&7Name: &f" + rank.getName(),
                            "&7Weight: &f" + rank.getWeight(),
                            "&7Prefix: &r" + (rank.getPrefix().isEmpty() ? "None" : rank.getPrefix()),
                            "&7Suffix: &r" + (rank.getSuffix().isEmpty() ? "None" : rank.getSuffix()),
                            "&7Parents: &r" + (rank.getParents().stream().map(Rank::getName).toArray().length == 0 ? "None" : StringUtils.join(rank.getParents().stream().map(Rank::getName).toArray(), "&7, &f")),
                            "&7&m----------------------------------"
                    ).create());
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
