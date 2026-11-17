package fr.openmc.core.features.playerwarp.command;

import fr.openmc.api.omcplayer.OMCPlayer;
import fr.openmc.core.features.homes.utils.HomeUtil;
import fr.openmc.core.features.playerwarp.PlayerWarpManager;
import fr.openmc.core.features.playerwarp.PlayerWarpManager.Result;
import fr.openmc.core.features.playerwarp.command.autocomplete.PlayerWarpAutoComplete;
import fr.openmc.core.features.playerwarp.menu.PlayerWarpConfigMenu;
import fr.openmc.core.features.playerwarp.menu.PlayerWarpMenu;
import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import fr.openmc.core.utils.text.messages.TranslationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command({"pw", "pwarp", "playerwarp"})
public class PlayerWarpCommand {

    @CommandPlaceholder
    @Description("Ouvre le menu des warps")
    public void open(OMCPlayer player) {
        new PlayerWarpMenu(player).open();
    }

    @Subcommand("create")
    @Description("Crée ton warp (1 maximum)")
    @CommandPermission("omc.playerwarp.create")
    public void create(OMCPlayer player, String name) {
        if (!HomeUtil.isValidHomeName(name)) {
            player.message().sendError(TranslationManager.translation("feature.playerwarp.command.invalid_name"));
            return;
        }
        switch (PlayerWarpManager.createWarp(player.getUniqueId(), name, player.getLocation())) {
            case SUCCESS -> player.message().sendSuccess(TranslationManager.translation(
                    "feature.playerwarp.command.create.success",
                    Component.text(name).color(NamedTextColor.YELLOW)));
            case ALREADY_HAS_WARP ->
                    player.message().sendError(TranslationManager.translation("feature.playerwarp.command.create.already_has"));
            case NAME_TAKEN ->
                    player.message().sendError(TranslationManager.translation("feature.playerwarp.command.name_taken"));
            default -> player.message().sendError(TranslationManager.translation("feature.playerwarp.command.error"));
        }
    }

    @Subcommand("delete")
    @Description("Supprime ton warp")
    @CommandPermission("omc.playerwarp.delete")
    public void delete(OMCPlayer player) {
        PlayerWarp warp = PlayerWarpManager.getWarp(player.getPlayer().getUniqueId());
        if (PlayerWarpManager.deleteWarp(player.getPlayer().getUniqueId()).equals(Result.NO_WARP))
            player.message().sendError(TranslationManager.translation("feature.playerwarp.command.no_warp"));
        else
            player.message().sendSuccess(TranslationManager.translation(
                    "feature.playerwarp.command.delete.success",
                    Component.text(warp.getName()).color(NamedTextColor.YELLOW)));
    }

    @Subcommand("rename")
    @Description("Renomme ton warp")
    @CommandPermission("omc.playerwarp.rename")
    public void rename(OMCPlayer player, String newName) {
        PlayerWarp warp = PlayerWarpManager.getWarp(player.getPlayer().getUniqueId());
        if (warp == null) {
            player.message().sendError(TranslationManager.translation("feature.playerwarp.command.no_warp"));
            return;
        }
        if (!HomeUtil.isValidHomeName(newName)) {
            player.message().sendError(TranslationManager.translation("feature.playerwarp.command.invalid_name"));
            return;
        }
        if (PlayerWarpManager.renameWarp(warp, newName).equals(Result.NAME_TAKEN)) {
            player.message().sendError(TranslationManager.translation("feature.playerwarp.command.name_taken"));
            return;
        }
        player.message().sendSuccess(TranslationManager.translation(
                "feature.playerwarp.command.rename.success",
                Component.text(newName).color(NamedTextColor.YELLOW)));
    }

    @Subcommand("relocate")
    @Description("Déplace ton warp à ta position")
    @CommandPermission("omc.playerwarp.relocate")
    public void relocate(OMCPlayer player) {
        PlayerWarp warp = PlayerWarpManager.getWarp(player.getPlayer().getUniqueId());
        if (warp == null) {
            player.message().sendError(TranslationManager.translation("feature.playerwarp.command.no_warp"));
            return;
        }
        PlayerWarpManager.relocateWarp(warp, player.getPlayer().getLocation());
        player.message().sendSuccess(TranslationManager.translation(
                "feature.playerwarp.command.relocate.success",
                Component.text(warp.getName()).color(NamedTextColor.YELLOW)));
    }

    @Subcommand("settings")
    @Description("Ouvre le menu de configuration de ton warp")
    @CommandPermission("omc.playerwarp.settings")
    public void settings(OMCPlayer player) {
        PlayerWarp warp = PlayerWarpManager.getWarp(player.getPlayer().getUniqueId());
        if (warp == null) {
            player.message().sendError(TranslationManager.translation("feature.playerwarp.command.no_warp"));
            return;
        }
        new PlayerWarpConfigMenu(player, warp).open();
    }

    @Subcommand("tp")
    @Description("Se téléporter à un warp")
    public void tp(OMCPlayer player, @SuggestWith(PlayerWarpAutoComplete.class) String name) {
        PlayerWarp warp = PlayerWarpManager.getWarpByName(name);
        if (warp == null) {
            player.message().sendError(TranslationManager.translation("feature.playerwarp.command.tp.not_found"));
            return;
        }
        if (PlayerWarpManager.teleport(player, warp).equals(Result.INVALID_WORLD)) {
            player.message().sendError(TranslationManager.translation("feature.playerwarp.command.tp.invalid_world"));
        }
    }
}