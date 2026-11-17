package fr.openmc.core.features.playerwarp.menu;

import fr.openmc.api.menulib.PaginatedMenu;
import fr.openmc.api.menulib.template.ItemMenuTemplate;
import fr.openmc.api.menulib.utils.InventorySize;
import fr.openmc.api.menulib.utils.ItemMenuBuilder;
import fr.openmc.api.menulib.utils.ItemUtils;
import fr.openmc.api.omcplayer.OMCPlayer;
import fr.openmc.core.features.homes.icons.HomeIcon;
import fr.openmc.core.features.homes.icons.HomeIconRegistry;
import fr.openmc.core.features.playerwarp.PlayerWarpManager;
import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import fr.openmc.core.utils.text.messages.Prefix;
import fr.openmc.core.utils.text.messages.TranslationManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerWarpIconMenu extends PaginatedMenu {

    private final PlayerWarp warp;

    public PlayerWarpIconMenu(OMCPlayer player, PlayerWarp warp) {
        super(player);
        this.warp = warp;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGEST;
    }

    @Override
    public int getSizeOfItems() {
        return HomeIconRegistry.getAllIcons().size();
    }

    @Override
    public @NotNull Component getName() {
        return TranslationManager.translation("feature.playerwarp.icon.menu.title");
    }

    @Override
    public String getTexture() {
        return null;
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return null;
    }

    @Override
    public @NotNull List<Integer> getStaticSlots() {
        return List.of(45, 48, 49, 50, 53);
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (HomeIcon icon : HomeIconRegistry.getAllIcons()) {
            items.add(new ItemMenuBuilder(this, icon.getItemStack().clone(), meta -> {
            }).hide(ItemUtils.getDataComponentType()).setOnClick(event -> {
                PlayerWarpManager.changeIcon(warp, icon);
                getOwner().message().sendSuccess(
                        TranslationManager.translation("feature.playerwarp.icon.success"),
                        Prefix.HOME, true);
                new PlayerWarpConfigMenu(getOwner(), warp).open();
            }));
        }
        return items;
    }

    @Override
    public List<Integer> getTakableSlot() {
        return List.of();
    }

    @Override
    public Map<Integer, ItemMenuBuilder> getButtons() {
        Map<Integer, ItemMenuBuilder> map = new HashMap<>();
        map.put(45, ItemMenuTemplate.BTN_PREVIOUS_PAGE_WHITE.apply(this).setBackButton());
        map.put(48, ItemMenuTemplate.BTN_PREVIOUS_PAGE_WHITE.apply(this));
        map.put(49, ItemMenuTemplate.BTN_CLOSE.apply(this));
        map.put(50, ItemMenuTemplate.BTN_NEXT_PAGE_WHITE.apply(this));
        return map;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }
}