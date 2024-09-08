package io.github.jwdeveloper.commands.spigot.impl.parsers;

import io.github.jwdeveloper.commands.api.builders.arguments.ArgumentBuilder;
import io.github.jwdeveloper.commands.api.data.ActionResult;
import io.github.jwdeveloper.commands.api.argumetns.ArgumentType;
import io.github.jwdeveloper.commands.api.data.events.ArgumentParseEvent;
import io.github.jwdeveloper.commands.api.data.events.ArgumentSuggestionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerParser implements ArgumentType {
    @Override
    public String name() {
        return "Player";
    }

    @Override
    public void onArgumentBuilder(ArgumentBuilder builder) {

    }

    @Override
    public ActionResult<Object> onParse(ArgumentParseEvent event) {
        var playerName = event.iterator().next();
        return ActionResult.success(Bukkit.getPlayer(playerName));
    }

    @Override
    public ActionResult<List<String>> onSuggestion(ArgumentSuggestionEvent event) {
        var input = event.rawValue();
        var players = Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(input::contains)
                .toList();
        return ActionResult.success(players);
    }
}
