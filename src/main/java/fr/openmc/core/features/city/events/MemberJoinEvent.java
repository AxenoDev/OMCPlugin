package fr.openmc.core.features.city.events;

import fr.openmc.core.features.city.City;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class MemberJoinEvent extends Event {

    private final OfflinePlayer player;
    private final City city;

    public MemberJoinEvent(OfflinePlayer player, City city) {
        this.player = player;
        this.city = city;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
