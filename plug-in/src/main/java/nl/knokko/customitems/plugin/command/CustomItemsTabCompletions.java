package nl.knokko.customitems.plugin.command;

import com.google.common.collect.Lists;
import nl.knokko.customitems.block.CustomBlockValues;
import nl.knokko.customitems.item.CustomItemValues;
import nl.knokko.customitems.plugin.set.ItemSetWrapper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomItemsTabCompletions implements TabCompleter {

    private static List<String> filter(List<String> full, String prefix) {
        return full.stream().filter(element -> element.startsWith(prefix)).collect(Collectors.toList());
    }

    private static List<String> getRootCompletions(CommandSender sender) {
        return Lists.newArrayList("give", "take", "list", "debug", "encode", "reload", "repair", "damage", "setblock")
                .stream().filter(element -> sender.hasPermission("customitems." + element))
                .collect(Collectors.toList()
        );
    }

    private final ItemSetWrapper itemSet;

    public CustomItemsTabCompletions(ItemSetWrapper itemSet) {
        this.itemSet = itemSet;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return getRootCompletions(sender);
        } else if (args.length == 1) {
            String prefix = args[0];
            return filter(getRootCompletions(sender), prefix);
        } else if (args.length == 2) {
            String first = args[0];
            String prefix = args[1];

            if (first.equals("give") && sender.hasPermission("customitems.give")) {
                List<String> result = new ArrayList<>(itemSet.get().getItems().size());
                for (CustomItemValues item : itemSet.get().getItems()) {
                    result.add(item.getName());
                    if (!item.getAlias().isEmpty()) {
                        result.add(item.getAlias());
                    }
                }
                return filter(result, prefix);
            }

            if (first.equals("take") && sender.hasPermission("customitems.take")) {
                return Lists.newArrayList("1", "2", "3", "4");
            }

            if (
                    (first.equals("repair") && sender.hasPermission("customitems.repair"))
                            || (first.equals("damage") && sender.hasPermission("customitems.damage"))
            ) {
                return Lists.newArrayList("1", "2", "3", "4");
            }

            if (first.equals("setblock") && sender.hasPermission("customitems.setblock")) {
                List<String> result = new ArrayList<>(itemSet.get().getBlocks().size());
                for (CustomBlockValues block : itemSet.get().getBlocks()) {
                    result.add(block.getName());
                }
                return filter(result, prefix);
            }
        } else if (args.length == 3) {
            String first = args[0];
            String prefix = args[2];
            if (
                    (first.equals("give") && sender.hasPermission("customitems.give"))
                            || first.equals("take") && sender.hasPermission("customitems.take")
                            || (first.equals("damage") && sender.hasPermission("customitems.damage"))
                            || (first.equals("repair") && sender.hasPermission("customitems.repair"))
            ) {
                List<String> names = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
                return filter(names, prefix);
            }

            if (first.equals("setblock") && sender.hasPermission("customitems.setblock")) {
                return Lists.newArrayList("~");
            }
        } else if (args.length == 4) {
            String first = args[0];
            if (first.equals("give") && sender.hasPermission("customitems.give")) {
                String itemName = args[1];
                CustomItemValues item = itemSet.getItem(itemName);
                if (item != null) {
                    if (item.canStack()) {
                        return Lists.newArrayList("1", item.getMaxStacksize() + "");
                    } else {
                        return Lists.newArrayList("1");
                    }
                }
            }

            if (first.equals("setblock") && sender.hasPermission("customitems.setblock")) {
                return Lists.newArrayList("~");
            }
        } else if (args.length == 5) {

            String first = args[0];
            if (first.equals("setblock") && sender.hasPermission("customitems.setblock")) {
                return Lists.newArrayList("~");
            }
        } else if (args.length == 6) {

            String first = args[0];
            String prefix = args[5];
            if (first.equals("setblock") && sender.hasPermission("customitems.setblock")) {
                List<String> result = new ArrayList<>(Bukkit.getWorlds().size());
                for (World world : Bukkit.getWorlds()) {
                    result.add(world.getName());
                }
                return filter(result, prefix);
            }
        }
        return Collections.emptyList();
    }
}
