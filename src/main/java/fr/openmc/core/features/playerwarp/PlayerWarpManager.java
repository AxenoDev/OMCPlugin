package fr.openmc.core.features.playerwarp;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fr.openmc.core.bootstrap.features.Feature;
import fr.openmc.core.bootstrap.features.annotations.Credit;
import fr.openmc.core.bootstrap.features.types.HasCommands;
import fr.openmc.core.bootstrap.features.types.HasDatabase;
import fr.openmc.core.bootstrap.integration.DatabaseManager;
import fr.openmc.core.features.homes.command.*;
import fr.openmc.core.features.homes.models.Home;
import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import lombok.Getter;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Credit(developers = { "Axeno" })
@Getter
public class PlayerWarpManager extends Feature implements HasDatabase, HasCommands {

    public static final Set<PlayerWarp> playerwarps = new HashSet<>();

    @Override
    public void init() {
        loadPlayerWarp();
    }

    @Override
    public Set<Object> getCommands() {
        return Set.of(
                new SetHomeCommand(),
                new RenameHomeCommand(),
                new DelHomeCommand(),
                new RelocateHomeCommand(),
                new TpHomeCommand(),
                new HomeWorldCommand());
    }

    @Override
    public void save() {
        savePlayerWarp();
    }

    // DB methods

    private static Dao<PlayerWarp, UUID> playerwarpDao;

    @Override
    public void initDB(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, PlayerWarp.class);
        playerwarpDao = DaoManager.createDao(connectionSource, PlayerWarp.class);
    }

    private static void loadPlayerWarp() {
        try {
            playerwarps.addAll(playerwarpDao.queryForAll());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de chargement des PlayerWarp ", e);
        }
    }

    private static void savePlayerWarp() {
        try {
            TableUtils.clearTable(DatabaseManager.getConnectionSource(), Home.class);
            for (PlayerWarp pw : playerwarps) {
                playerwarpDao.createOrUpdate(pw);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de sauvegarde des PlayerWarp ", e);
        }
    }
}
