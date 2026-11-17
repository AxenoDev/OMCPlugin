package fr.openmc.core.features.playerwarp.menu;

import fr.openmc.api.input.dialog.DialogInput;
import fr.openmc.api.menulib.Menu;
import fr.openmc.api.menulib.template.ItemMenuTemplate;
import fr.openmc.api.menulib.utils.InventorySize;
import fr.openmc.api.menulib.utils.ItemMenuBuilder;
import fr.openmc.api.menulib.utils.ItemUtils;
import fr.openmc.api.omcplayer.OMCPlayer;
import fr.openmc.core.OMCRegistry;
import fr.openmc.core.features.homes.utils.HomeUtil;
import fr.openmc.core.features.playerwarp.PlayerWarpManager;
import fr.openmc.core.features.playerwarp.PlayerWarpManager.Result;
import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import fr.openmc.core.utils.text.messages.Prefix;
import fr.openmc.core.utils.text.messages.TranslationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.openmc.core.features.homes.utils.HomeUtil.MAX_LENGTH_HOME_NAME;

public class PlayerWarpConfigMenu extends Menu {

    private final PlayerWarp warp;

    public PlayerWarpConfigMenu(OMCPlayer owner, PlayerWarp warp) {
        super(owner);
        this.warp = warp;
    }

    @Override
    public @NotNull Component getName() {
        return TranslationManager.translation("feature.playerwarp.config.title");
    }

    @Override
    public String getTexture() {
        return null;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGER;
    }

    @Override
    public @NotNull Map<Integer, ItemMenuBuilder> getContent() {
        Map<Integer, ItemMenuBuilder> content = new HashMap<>();
        OMCPlayer player = getOwner();

        content.put(4, new ItemMenuBuilder(this, warp.getIconItem()).hide(ItemUtils.getDataComponentType()));
        content.put(20, new ItemMenuBuilder(this, Material.NAME_TAG, meta -> {
            meta.displayName(TranslationManager.translation("feature.playerwarp.config.rename.name"));
            meta.lore(TranslationManager.translationLore("feature.playerwarp.config.rename.lore"));
        }).setOnClick(_ -> DialogInput.send(player, TranslationManager.translation("feature.playerwarp.config.rename.prompt"), MAX_LENGTH_HOME_NAME, input -> {
            if (!HomeUtil.isValidHomeName(input)) return;

            if (PlayerWarpManager.renameWarp(warp, input).equals(Result.NAME_TAKEN)) {
                player.message().sendError(TranslationManager.translation("feature.playerwarp.command.name_taken"), Prefix.HOME, true);
                return;
            }
            player.message().sendSuccess(TranslationManager.translation(
                    "feature.playerwarp.rename.success",
                    Component.text(input).color(NamedTextColor.YELLOW)
            ), Prefix.HOME, true);
        })));
        content.put(21, new ItemMenuBuilder(this, Material.ITEM_FRAME, meta -> {
            meta.displayName(TranslationManager.translation("feature.playerwarp.config.icon.name"));
            meta.lore(TranslationManager.translationLore("feature.playerwarp.config.icon.lore"));
        }).setOnClick(_ -> new PlayerWarpIconMenu(player, warp).open()));
        content.put(22, new ItemMenuBuilder(this, Material.COMPASS, meta -> {
            meta.displayName(TranslationManager.translation("feature.playerwarp.config.relocate.name"));
            meta.lore(TranslationManager.translationLore("feature.playerwarp.config.relocate.lore"));
        }).setOnClick(_ -> {
            PlayerWarpManager.relocateWarp(warp, player.getLocation());
            player.message().sendSuccess(TranslationManager.translation(
                    "feature.playerwarp.relocate.success",
                    Component.text(warp.getName()).color(NamedTextColor.YELLOW)
            ), Prefix.HOME, true);
            player.closeInventory();
        }));

        content.put(24, new ItemMenuBuilder(this, OMCRegistry.CUSTOM_ITEMS.HOMES_ICON_BIN_RED, meta -> {
            meta.displayName(TranslationManager.translation("feature.playerwarp.config.delete.name"));
            meta.lore(TranslationManager.translationLore("feature.playerwarp.config.delete.lore"));
        }).setOnClick(e -> new PlayerWarpDeleteConfirmMenu(player, warp).open()));

        content.put(36, ItemMenuTemplate.BTN_PREVIOUS_PAGE_WHITE.apply(this).setBackButton());
        content.put(44, ItemMenuTemplate.BTN_CLOSE.apply(this));
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