package fr.openmc.core.utils.customitems.items;

import dev.lone.itemsadder.api.CustomStack;
import fr.openmc.core.utils.customitems.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BuilderWand extends CustomItem {

    public BuilderWand() {
        super("omc_items:builder_wand");
    }

    @Override
    public ItemStack getVanilla() {
        return ItemStack.of(Material.WOODEN_SHOVEL);
    }

    @Override
    public ItemStack getItemsAdder() {
        CustomStack stack = CustomStack.getInstance(getName());
        return stack != null ? stack.getItemStack() : null;
    }
}
