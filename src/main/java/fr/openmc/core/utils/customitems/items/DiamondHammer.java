package fr.openmc.core.utils.customitems.items;

import dev.lone.itemsadder.api.CustomStack;
import fr.openmc.core.utils.customitems.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondHammer extends CustomItem {

    public DiamondHammer() {
        super("omc_items:diamond_hammer");
    }

    @Override
    public ItemStack getVanilla() {
        return ItemStack.of(Material.DIAMOND_PICKAXE);
    }

    @Override
    public ItemStack getItemsAdder() {
        CustomStack stack = CustomStack.getInstance(getName());
        return stack != null ? stack.getItemStack() : null;
    }
}
