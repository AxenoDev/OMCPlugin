package fr.openmc.core.utils.customitems.items;

import dev.lone.itemsadder.api.CustomStack;
import fr.openmc.core.utils.customitems.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NetheriteHammer extends CustomItem {

    public NetheriteHammer() {
        super("omc_items:netherite_hammer");
    }

    @Override
    public ItemStack getVanilla() {
        return ItemStack.of(Material.NETHERITE_PICKAXE);
    }

    @Override
    public ItemStack getItemsAdder() {
        CustomStack stack = CustomStack.getInstance(getName());
        return stack != null ? stack.getItemStack() : null;
    }
}
