package fr.openmc.core.features.playerwarp;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fr.openmc.api.omcplayer.OMCPlayer;
import fr.openmc.core.OMCPlugin;
import fr.openmc.core.bootstrap.features.Feature;
import fr.openmc.core.bootstrap.features.annotations.Credit;
import fr.openmc.core.bootstrap.features.types.HasCommands;
import fr.openmc.core.bootstrap.features.types.HasDatabase;
import fr.openmc.core.bootstrap.integration.DatabaseManager;
import fr.openmc.core.features.homes.icons.HomeIcon;
import fr.openmc.core.features.homes.icons.HomeIconRegistry;
import fr.openmc.core.features.playerwarp.command.PlayerWarpCommand;
import fr.openmc.core.features.playerwarp.events.PlayerWarpCreateEvent;
import fr.openmc.core.features.playerwarp.events.PlayerWarpTpEvent;
import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import fr.openmc.core.utils.text.messages.Prefix;
import fr.openmc.core.utils.text.messages.TranslationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Credit(developers = {"Axeno"})
public class PlayerWarpManager extends Feature implements HasDatabase, HasCommands {

    private static final Map<UUID, PlayerWarp> WARPS = new ConcurrentHashMap<>();
    private static Dao<PlayerWarp, UUID> playerWarpDao;

    private static void load() {
        try {
            for (PlayerWarp pw : playerWarpDao.queryForAll()) {
                WARPS.put(pw.getOwner(), pw);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de chargement des PlayerWarp", e);
        }
    }

    public static Collection<PlayerWarp> getPlayerWarps() {
        return Collections.unmodifiableCollection(WARPS.values());
    }

    public static @Nullable PlayerWarp getWarp(UUID owner) {
        return WARPS.get(owner);
    }

    public static boolean hasWarp(UUID owner) {
        return WARPS.containsKey(owner);
    }

    public static @Nullable PlayerWarp getWarpByName(String name) {
        for (PlayerWarp pw : WARPS.values()) {
            if (pw.getName().equalsIgnoreCase(name))
                return pw;
        }
        return null;
    }

    private static boolean isNameTaken(String name, @Nullable UUID ignoredOwner) {
        PlayerWarp existing = getWarpByName(name);
        return existing != null && !existing.getOwner().equals(ignoredOwner);
    }

    public static Result createWarp(UUID owner, String name, Location location) {
        if (hasWarp(owner))
            return Result.ALREADY_HAS_WARP;
        if (isNameTaken(name, null))
            return Result.NAME_TAKEN;

        HomeIcon icon = HomeIconRegistry.getDefaultIcon();
        PlayerWarp warp = new PlayerWarp(owner, name, location, icon);
        WARPS.put(owner, warp);

        Bukkit.getPluginManager().callEvent(new PlayerWarpCreateEvent(warp, owner));
        return Result.SUCCESS;
    }

    public static Result deleteWarp(UUID owner) {
        PlayerWarp warp = WARPS.remove(owner);
        if (warp == null)
            return Result.NO_WARP;

        return Result.SUCCESS;
    }

    public static Result renameWarp(PlayerWarp warp, String newName) {
        if (isNameTaken(newName, warp.getOwner()))
            return Result.NAME_TAKEN;
        warp.setName(newName);
        return Result.SUCCESS;
    }

    public static void relocateWarp(PlayerWarp warp, Location location) {
        warp.setLocation(location);
    }

    public static void changeIcon(PlayerWarp warp, HomeIcon icon) {
        warp.setIcon(icon);
    }

    public static Result teleport(OMCPlayer player, PlayerWarp warp) {
        Location location = warp.getLocation();
        if (location == null)
            return Result.INVALID_WORLD;
        if (player == null)
            return Result.PLAYER_OFFLINE;

        player.teleportAsync(location).thenAccept(success -> {
            if (!success)
                return;
            Bukkit.getScheduler().runTask(OMCPlugin.getInstance(), () -> {
                Bukkit.getPluginManager().callEvent(new PlayerWarpTpEvent(warp, player));
                player.message().sendSuccess(
                        TranslationManager.translation(
                                "feature.playerwarp.teleport.success",
                                Component.text(warp.getName()).color(NamedTextColor.YELLOW)),
                        Prefix.HOME,
                        true);
            });
        });
        return Result.SUCCESS;
    }

    @Override
    public void init() {
        load();
    }

    @Override
    public Set<Object> getCommands() {
        return Set.of(new PlayerWarpCommand());
    }

    @Override
    public void save() {
        try {
            TableUtils.clearTable(DatabaseManager.getConnectionSource(), PlayerWarp.class);
            for (PlayerWarp pw : WARPS.values()) {
                playerWarpDao.create(pw);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de sauvegarde des PlayerWarp", e);
        }
    }

    @Override
    public void initDB(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, PlayerWarp.class);
        playerWarpDao = DaoManager.createDao(connectionSource, PlayerWarp.class);
    }

    public enum Result {
        SUCCESS, ALREADY_HAS_WARP, NO_WARP, NAME_TAKEN, INVALID_WORLD, PLAYER_OFFLINE
    }
}