package net.silexpvp.ranks.rank.command;

import net.md_5.bungee.api.chat.*;
import net.silexpvp.nightmare.util.ChatUtils;
import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.CommandParent;
import net.silexpvp.ranks.RanksPlugin;
import net.silexpvp.ranks.rank.command.argument.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankCommand extends CommandParent<RanksPlugin> {

    public RankCommand() {
        super("rank");

        addArgument(new RankCreateArgument(this));
        addArgument(new RankDeleteArgument(this));
        addArgument(new RankPermissionArgument(this));
        addArgument(new RankParentArgument(this));
        addArgument(new RankSetWeightArgument(this));
        addArgument(new RankPrefixArgument(this));
        addArgument(new RankSuffixArgument(this));
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {

            sender.sendMessage(ChatUtils.centerMessage(ChatColor.BLUE + "---------- " + ChatColor.GOLD + ChatColor.BOLD + "Rank Help" + ChatColor.BLUE + " ----------"));

            for (CommandArgument argument : getArguments()) {
                BaseComponent[] command = new ComponentBuilder(argument.getUsage(label))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.GREEN + "Click to prepare the " + label + ' ' + argument.getName() + " command.")))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, argument.getUsage(label) + ' '))
                        .append(" - ").color(ChatColor.GRAY.asBungee())
                        .retain(ComponentBuilder.FormatRetention.FORMATTING)
                        .append(argument.getDescription()).color(ChatColor.YELLOW.asBungee()).create();

                sender.sendMessage(command);
            }

            return;
        }

        super.execute(sender, label, args);
    }

    @Override
    public boolean isRequiresPermission() {
        return true;
    }
}
