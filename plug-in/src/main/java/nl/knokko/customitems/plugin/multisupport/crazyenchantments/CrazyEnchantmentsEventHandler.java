package nl.knokko.customitems.plugin.multisupport.crazyenchantments;

import com.badbones69.crazyenchantments.api.CrazyManager;
import com.badbones69.crazyenchantments.api.enums.CEnchantments;
import com.badbones69.crazyenchantments.api.events.HellForgedUseEvent;
import com.badbones69.crazyenchantments.api.objects.CEnchantment;
import nl.knokko.customitems.item.CustomItemValues;
import nl.knokko.customitems.item.CustomToolValues;
import nl.knokko.customitems.plugin.set.item.CustomToolWrapper;
import nl.knokko.customitems.plugin.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import nl.knokko.customitems.plugin.CustomItemsPlugin;

import static nl.knokko.customitems.plugin.set.item.CustomToolWrapper.wrap;

public class CrazyEnchantmentsEventHandler implements Listener {

	private static CEnchantment fromName(String enchantmentName) {
		return CEnchantment.getCEnchantmentFromName(enchantmentName);
	}

	public CrazyEnchantmentsEventHandler() {
		CrazyEnchantmentsSupport.crazyEnchantmentsFunctions = new CrazyEnchantmentsFunctions() {

			@Override
			public int getLevel(ItemStack itemStack, String enchantmentName) {
				return CrazyManager.getInstance().getLevel(itemStack, fromName(enchantmentName));
			}

			@Override
			public ItemStack add(ItemStack itemStack, String enchantmentName, int level) {
				return CrazyManager.getInstance().addEnchantment(itemStack, fromName(enchantmentName), level);
			}

			@Override
			public ItemStack remove(ItemStack itemStack, String enchantmentName) {
				return CrazyManager.getInstance().removeEnchantment(itemStack, fromName(enchantmentName));
			}
		};
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onHellForge(HellForgedUseEvent event) {
		if (ItemUtils.isCustom(event.getItem())) {

			// Unfortunately, the HellForgedUseEvent doesn't allow us to replace the item, which is required to change
			// its custom durability. This is bypassed by manually repairing using the PlayerMoveEvent
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void manuallyApplyHellForged(PlayerMoveEvent event) {
		ItemStack[] contents = event.getPlayer().getInventory().getContents();
		boolean didChange = false;

		for (int index = 0; index < contents.length; index++) {
			ItemStack itemStack = contents[index];
			CustomItemValues customItem = CustomItemsPlugin.getInstance().getSet().getItem(itemStack);
			if (customItem instanceof CustomToolValues) {
				CustomToolValues customTool = (CustomToolValues) customItem;
				int hellForgedLevel = CEnchantments.HELLFORGED.getLevel(itemStack);

				if (hellForgedLevel > 0 && CEnchantments.HELLFORGED.chanceSuccessful()) {
					CustomToolWrapper.IncreaseDurabilityResult result = wrap(customTool).increaseDurability(itemStack, hellForgedLevel);
					if (result.increasedAmount > 0) {
						contents[index] = result.stack;
						didChange = true;
					}
				}
			}
		}

		if (didChange) {
			event.getPlayer().getInventory().setContents(contents);
		}
	}
}