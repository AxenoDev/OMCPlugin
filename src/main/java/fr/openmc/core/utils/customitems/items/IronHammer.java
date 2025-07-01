package fr.openmc.core.utils.customitems.items;

import dev.lone.itemsadder.api.CustomStack;
import fr.openmc.core.utils.customitems.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IronHammer extends CustomItem {

    public IronHammer() {
        super("omc_items:iron_hammer");
    }

    @Override
    public ItemStack getVanilla() {
        return ItemStack.of(Material.IRON_PICKAXE);
    }

    @Override
    public ItemStack getItemsAdder() {
        CustomStack stack = CustomStack.getInstance(getName());
        return stack != null ? stack.getItemStack() : null;
    }
}
