package fr.openmc.core.features.playerwarp.menu;

import fr.openmc.api.menulib.PaginatedMenu;
import fr.openmc.api.menulib.template.ItemMenuTemplate;
import fr.openmc.api.menulib.utils.InventorySize;
import fr.openmc.api.menulib.utils.ItemMenuBuilder;
import fr.openmc.api.menulib.utils.ItemUtils;
import fr.openmc.api.omcplayer.OMCPlayer;
import fr.openmc.core.features.playerwarp.PlayerWarpManager;
import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import fr.openmc.core.utils.text.messages.TranslationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayerWarpMenu extends PaginatedMenu {

    public PlayerWarpMenu(OMCPlayer player) {
        super(player);
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGEST;
    }

    @Override
    public int getSizeOfItems() {
        return PlayerWarpManager.getPlayerWarps().size();
    }

    @Override
    public @NotNull Component getName() {
        return TranslationManager.translation("feature.playerwarp.menu.title");
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
        UUID self = getOwner().getUniqueId();

        List<PlayerWarp> warps = new ArrayList<>(PlayerWarpManager.getPlayerWarps());
        warps.sort(Comparator
                .comparing((PlayerWarp w) -> !w.getOwner().equals(self))
                .thenComparing(w -> w.getName().toLowerCase()));

        List<ItemStack> items = new ArrayList<>();
        for (PlayerWarp pw : warps) {
            boolean mine = pw.getOwner().equals(self);
            String ownerName = Optional.ofNullable(Bukkit.getOfflinePlayer(pw.getOwner()).getName()).orElse("?");

            items.add(new ItemMenuBuilder(this, pw.getIcon().getItemStack().clone(), meta -> {
                meta.displayName(TranslationManager.translation(
                        "feature.playerwarp.menu.item.name",
                        Component.text(pw.getName()).color(mine ? NamedTextColor.GREEN : NamedTextColor.YELLOW)
                ));
                meta.lore(TranslationManager.translationLore(
                        mine ? "feature.playerwarp.menu.item.lore.owner" : "feature.playerwarp.menu.item.lore",
                        Component.text(ownerName).color(NamedTextColor.GRAY)
                ));
            }).hide(ItemUtils.getDataComponentType()).setOnClick(event -> {
                if (event.isLeftClick()) {
                    getOwner().closeInventory();
                    PlayerWarpManager.teleport(getOwner(), pw);
                } else if (event.isRightClick() && mine) {
                    new PlayerWarpConfigMenu(getOwner(), pw).open();
                }
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