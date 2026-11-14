package fr.openmc.core.features.playerwarp.command.autocomplete;

import fr.openmc.core.features.playerwarp.PlayerWarpManager;
import fr.openmc.core.features.playerwarp.models.PlayerWarp;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.stream.StringStream;

import java.util.List;

public class PlayerWarpAutoComplete implements SuggestionProvider<BukkitCommandActor> {

    @Override
    public @NotNull List<String> getSuggestions(@NotNull ExecutionContext<BukkitCommandActor> context) {
        StringStream args = context.input();
        String typed = args.isEmpty() ? "" : args.peekString().toLowerCase();

        return PlayerWarpManager.getPlayerWarps().stream()
                .map(PlayerWarp::getName)
                .filter(name -> name.toLowerCase().startsWith(typed))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }
}
