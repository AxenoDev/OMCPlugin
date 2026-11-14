package fr.openmc.core.features.playerwarp.events;

import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerWarpCreateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerWarp playerWarp;
    private final Player player;

    public PlayerWarpCreateEvent(PlayerWarp playerWarp, Player player) {
        this.playerWarp = playerWarp;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}