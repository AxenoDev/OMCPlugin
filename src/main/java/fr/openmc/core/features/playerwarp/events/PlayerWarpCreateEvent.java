package fr.openmc.core.features.playerwarp.events;

import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class PlayerWarpCreateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerWarp playerWarp;
    private final UUID owner;

    public PlayerWarpCreateEvent(PlayerWarp playerWarp, UUID owner) {
        this.playerWarp = playerWarp;
        this.owner = owner;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}