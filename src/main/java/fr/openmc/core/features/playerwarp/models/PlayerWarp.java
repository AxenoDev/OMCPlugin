package fr.openmc.core.features.playerwarp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import fr.openmc.core.features.homes.icons.HomeIcon;
import fr.openmc.core.features.homes.icons.HomeIconRegistry;
import fr.openmc.core.features.homes.icons.LegacyHomeIcon;
import fr.openmc.core.features.homes.utils.HomeUtil;
import fr.openmc.core.utils.text.messages.TranslationManager;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@DatabaseTable(tableName = "playerwarps")
public class PlayerWarp {

    @DatabaseField(id = true, canBeNull = false)
    private UUID owner;
    @Setter
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField(canBeNull = false)
    private String iconId;

    // Location
    @DatabaseField(canBeNull = false)
    private String world;
    @DatabaseField(canBeNull = false)
    private double x;
    @DatabaseField(canBeNull = false)
    private double y;
    @DatabaseField(canBeNull = false)
    private double z;
    @DatabaseField(canBeNull = false)
    private float yaw;
    @DatabaseField(canBeNull = false)
    private float pitch;

    PlayerWarp() {
        // required for ORMLite
    }

    public PlayerWarp(UUID owner, String name, Location location, HomeIcon icon) {
        this.owner = owner;
        this.name = name;
        this.iconId = icon.getSaveId();
        setLocation(location);
    }

    public @Nullable Location getLocation() {
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null)
            return null;
        return new Location(bukkitWorld, x, y, z, yaw, pitch);
    }

    public void setLocation(Location location) {
        world = location.getWorld().getName();
        x = round(location.getX());
        y = round(location.getY());
        z = round(location.getZ());
        yaw = location.getYaw();
        pitch = location.getPitch();
    }

    private static double round(double value) {
        return Math.round(value * 10d) / 10d;
    }

    public HomeIcon getIcon() {
        if (iconId == null || iconId.isEmpty())
            return HomeIconRegistry.getDefaultIcon();

        HomeIcon icon = HomeIconRegistry.getIcon(iconId);
        if (icon != null)
            return icon;

        try {
            LegacyHomeIcon legacyIcon = LegacyHomeIcon.valueOf(iconId.toUpperCase());
            return HomeIconRegistry.fromLegacyHomeIcon(legacyIcon);
        } catch (IllegalArgumentException e) {
            return HomeUtil.mapLegacyCustomId(iconId);
        }
    }

    public void setIcon(HomeIcon icon) {
        this.iconId = icon.getSaveId();
    }

    public ItemStack getIconItem() {
        ItemStack item = getIcon().getItemStack().clone();
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TranslationManager.translation(
                "feature.playerwarp.item.name",
                Component.text(name).color(NamedTextColor.GREEN)));
        item.setItemMeta(meta);
        item.lore(TranslationManager.translationLore(
                "feature.playerwarp.item.lore",
                Component.text(world).color(NamedTextColor.YELLOW),
                Component.text(x).color(NamedTextColor.YELLOW),
                Component.text(y).color(NamedTextColor.YELLOW),
                Component.text(z).color(NamedTextColor.YELLOW)));
        return item;
    }

    @Override
    public String toString() {
        return "PlayerWarp{owner=" + owner + ", name='" + name + "', world=" + world +
                ", x=" + x + ", y=" + y + ", z=" + z + ", icon=" + iconId + '}';
    }
}