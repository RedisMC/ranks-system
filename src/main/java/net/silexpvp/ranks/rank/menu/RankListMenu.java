package net.silexpvp.ranks.rank.menu;

import net.silexpvp.nightmare.menu.type.PaginatedMenu;
import net.silexpvp.nightmare.util.ItemCreator;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.Rank;
import net.silexpvp.ranks.util.WoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Comparator;
import java.util.stream.Collectors;

public class RankListMenu extends PaginatedMenu<RanksPlugin> {

    private Inventory inventory;

    @Override
    public int getTotalPages() {
        return (plugin.getRankManager().getRanks().size() - 1) / 18 + 1;
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
        int index = page * 18 - 18;
        while (slot < inventory.getSize() && plugin.getRankManager().getRanks().size() > index) {
            Rank rank = plugin.getRankManager().getRanks().stream().sorted(Comparator.comparingInt(Rank::getWeight).reversed()).collect(Collectors.toList()).get(index);

            inventory.setItem(slot++, new ItemCreator(Material.STAINED_CLAY).setDurability(WoolUtil.convertChatColorToWoolData(rank.getMainColor())).setDisplayName(rank.getMainColor() + rank.getName())
                    .setLore(
                            "&7&m----------------------------------",
                            "&7Name: &f" + rank.getName(),
                            "&7Weight: &f" + rank.getWeight(),
                            "&7Prefix: &r" + (rank.getPrefix().isEmpty() ? "None" : rank.getPrefix()),
                            "&7Suffix: &r" + (rank.getSuffix().isEmpty() ? "None" : rank.getSuffix()),
                            "&7Parents: &r" + (rank.getParents().stream().map(Rank::getName).toArray().length == 0 ? "None" : StringUtils.join(rank.getParents().stream().map(Rank::getName).toArray(), "&7, &f")),
                            "&7&m----------------------------------"
                    ).create());

            index++;
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory = plugin.getServer().createInventory(null, 27, getTitle());
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (!topInventory.equals(inventory)) return;

        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return;

            Player clicker = (Player) event.getWhoClicked();

            int slot = event.getRawSlot();
            if (slot == 0) {
                if (page == 1) return;

                page--;
                open(clicker);
            } else if (slot == 8) {
                if (page + 1 > getTotalPages()) return;

                page++;
                open(clicker);
            }
        } else if (!topInventory.equals(clickedInventory) && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            event.setCancelled(true);
        }
    }
}
