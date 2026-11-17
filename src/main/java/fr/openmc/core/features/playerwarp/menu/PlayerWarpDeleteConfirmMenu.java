package fr.openmc.core.features.playerwarp.menu;

import fr.openmc.api.menulib.Menu;
import fr.openmc.api.menulib.utils.InventorySize;
import fr.openmc.api.menulib.utils.ItemMenuBuilder;
import fr.openmc.api.menulib.utils.ItemUtils;
import fr.openmc.api.omcplayer.OMCPlayer;
import fr.openmc.core.OMCRegistry;
import fr.openmc.core.features.playerwarp.PlayerWarpManager;
import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import fr.openmc.core.utils.text.messages.Prefix;
import fr.openmc.core.utils.text.messages.TranslationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerWarpDeleteConfirmMenu extends Menu {

    private final PlayerWarp playerWarp;

    public PlayerWarpDeleteConfirmMenu(OMCPlayer owner, PlayerWarp playerWarp) {
        super(owner);
        this.playerWarp = playerWarp;
    }

    @Override
    public @NotNull Component getName() {
        return TranslationManager.translation("feature.playerwarp.delete.menu.title");
    }

    @Override
    public String getTexture() {
        return null;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.SMALLEST;
    }

    @Override
    public @NotNull Map<Integer, ItemMenuBuilder> getContent() {
        Map<Integer, ItemMenuBuilder> content = new HashMap<>();
        OMCPlayer player = getOwner();

        content.put(2, new ItemMenuBuilder(this, OMCRegistry.CUSTOM_ITEMS.HOMES_ICON_BIN_RED, meta -> {
            meta.displayName(TranslationManager.translation("feature.playerwarp.delete.confirm.name"));
            meta.lore(TranslationManager.translationLore("feature.playerwarp.delete.confirm.lore"));
        }).setOnClick(event -> {
            PlayerWarpManager.deleteWarp(playerWarp.getOwner());
            player.message().sendSuccess(
                    TranslationManager.translation(
                            "feature.playerwarp.delete.success",
                            Component.text(playerWarp.getName()).color(NamedTextColor.YELLOW)
                    ),
                    Prefix.HOME,
                    true
            );
            player.closeInventory();
        }));
        content.put(4, new ItemMenuBuilder(this, playerWarp.getIconItem()).hide(ItemUtils.getDataComponentType()));
        content.put(6, new ItemMenuBuilder(this, OMCRegistry.CUSTOM_ITEMS.HOMES_ICON_BIN, true));

        return content;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    @Override
    public List<Integer> getTakableSlot() {
        return List.of();
    }
}