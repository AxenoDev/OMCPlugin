package fr.openmc.core.features.playerwarp.command;

import fr.openmc.api.omcplayer.OMCPlayer;
import fr.openmc.core.features.homes.utils.HomeUtil;
import fr.openmc.core.features.playerwarp.PlayerWarpManager;
import fr.openmc.core.features.playerwarp.PlayerWarpManager.Result;
import fr.openmc.core.features.playerwarp.menu.PlayerWarpConfigMenu;
import fr.openmc.core.features.playerwarp.menu.PlayerWarpMenu;
import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import fr.openmc.core.utils.text.messages.Prefix;
import fr.openmc.core.utils.text.messages.TranslationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

public class PlayerWarpCommand {

    @Command({"pw", "pwarp", "playerwarp"})
    @Description("Ouvre le menu des warps")
    public void open(Player player) {
        new PlayerWarpMenu(OMCPlayer.of(player)).open();
    }

    @Command({"pw set", "pwarp set", "playerwarp set"})
    @Description("Crée ton warp (1 maximum)")
    public void set(Player player, String name) {
        OMCPlayer omc = OMCPlayer.of(player);
        if (!HomeUtil.isValidHomeName(name)) {
            error(omc, "feature.playerwarp.command.invalid_name");
            return;
        }
        switch (PlayerWarpManager.createWarp(player, name)) {
            case SUCCESS -> success(omc, "feature.playerwarp.command.set.success", name);
            case ALREADY_HAS_WARP -> error(omc, "feature.playerwarp.command.set.already_has");
            case NAME_TAKEN -> error(omc, "feature.playerwarp.command.name_taken");
            default -> error(omc, "feature.playerwarp.command.error");
        }
    }

    @Command({"pw delete", "pwarp delete", "playerwarp delete"})
    @Description("Supprime ton warp")
    public void delete(Player player) {
        OMCPlayer omc = OMCPlayer.of(player);
        PlayerWarp warp = PlayerWarpManager.getWarp(player.getUniqueId());
        if (warp == null) {
            error(omc, "feature.playerwarp.command.no_warp");
            return;
        }
        PlayerWarpManager.deleteWarp(player.getUniqueId());
        success(omc, "feature.playerwarp.delete.success", warp.getName());
    }

    @Command({"pw rename", "pwarp rename", "playerwarp rename"})
    @Description("Renomme ton warp")
    public void rename(Player player, String newName) {
        OMCPlayer omc = OMCPlayer.of(player);
        PlayerWarp warp = PlayerWarpManager.getWarp(player.getUniqueId());
        if (warp == null) {
            error(omc, "feature.playerwarp.command.no_warp");
            return;
        }
        if (!HomeUtil.isValidHomeName(newName)) {
            error(omc, "feature.playerwarp.command.invalid_name");
            return;
        }
        if (PlayerWarpManager.renameWarp(warp, newName) == Result.NAME_TAKEN) {
            error(omc, "feature.playerwarp.command.name_taken");
            return;
        }
        success(omc, "feature.playerwarp.rename.success", newName);
    }

    @Command({"pw relocate", "pwarp relocate", "playerwarp relocate"})
    @Description("Déplace ton warp à ta position")
    public void relocate(Player player) {
        OMCPlayer omc = OMCPlayer.of(player);
        PlayerWarp warp = PlayerWarpManager.getWarp(player.getUniqueId());
        if (warp == null) {
            error(omc, "feature.playerwarp.command.no_warp");
            return;
        }
        PlayerWarpManager.relocateWarp(warp, player.getLocation());
        success(omc, "feature.playerwarp.relocate.success", warp.getName());
    }

    @Command({"pw settings", "pwarp settings", "playerwarp settings"})
    @Description("Ouvre le menu de configuration de ton warp")
    public void settings(Player player) {
        OMCPlayer omc = OMCPlayer.of(player);
        PlayerWarp warp = PlayerWarpManager.getWarp(player.getUniqueId());
        if (warp == null) {
            error(omc, "feature.playerwarp.command.no_warp");
            return;
        }
        new PlayerWarpConfigMenu(omc, warp).open();
    }

    @Command({"pw tp", "pwarp tp", "playerwarp tp"})
    @Description("Se téléporter à un warp")
    public void tp(Player player, String name) {
        OMCPlayer omc = OMCPlayer.of(player);
        PlayerWarp warp = PlayerWarpManager.getWarpByName(name);
        if (warp == null) {
            error(omc, "feature.playerwarp.command.tp.not_found");
            return;
        }
        if (PlayerWarpManager.teleport(player, warp) == Result.INVALID_WORLD) {
            error(omc, "feature.playerwarp.command.tp.invalid_world");
        }
    }

    // ---- helpers ----

    private static void error(OMCPlayer player, String key) {
        player.message().sendError(TranslationManager.translation(key), Prefix.HOME, true);
    }

    private static void success(OMCPlayer player, String key, String name) {
        player.message().sendSuccess(
                TranslationManager.translation(key, Component.text(name).color(NamedTextColor.YELLOW)),
                Prefix.HOME,
                true
        );
    }
}