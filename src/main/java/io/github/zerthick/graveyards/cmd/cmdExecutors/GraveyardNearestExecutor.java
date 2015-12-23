/*
 * Copyright (C) 2015  Zerthick
 *
 * This file is part of Graveyards.
 *
 * Graveyards is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Graveyards is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graveyards.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.graveyards.cmd.cmdExecutors;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import io.github.zerthick.graveyards.utils.Graveyard;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class GraveyardNearestExecutor extends AbstractCmdExecutor implements CommandExecutor {

    public GraveyardNearestExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        Optional<WorldProperties> world = args.getOne("World");
        Optional<Vector3d> location = args.getOne("Location");

        if (location.isPresent() && world.isPresent()) {

            Graveyard nearestGraveyard = manager.findNearestGraveyard(location.get().toInt(), world.get().getUniqueId());
            if (nearestGraveyard != null) {
                src.sendMessage(successMessageBuilder(world.get(), nearestGraveyard.getName(), nearestGraveyard.getLocation()));
            } else {
                src.sendMessage(failureMessageBuilder(world.get()));
            }
            return CommandResult.success();
        }
        if (src instanceof Player) {
            Player player = (Player) src;

            Graveyard nearestGraveyard = manager.findNearestGraveyard(player.getLocation().getBlockPosition(), player.getWorld().getUniqueId());
            if (nearestGraveyard != null) {
                src.sendMessage(successMessageBuilder(player.getWorld().getProperties(), nearestGraveyard.getName(), nearestGraveyard.getLocation()));
            } else {
                src.sendMessage(failureMessageBuilder(player.getWorld().getProperties()));
            }

            return CommandResult.success();
        }

        src.sendMessage(Texts.of(TextColors.GREEN,
                "You must specify a Name and World for the graveyard!"));

        return CommandResult.empty();
    }

    private Text successMessageBuilder(WorldProperties world, String name, Vector3i location) {

        return Texts.of(TextColors.GREEN, "The nearest Graveyard in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN, " is Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " at Location ", TextColors.DARK_GREEN, location.toString());
    }

    private Text failureMessageBuilder(WorldProperties world) {

        return Texts.of(TextColors.GREEN,
                "There are no graveyards in World ",
                TextColors.DARK_GREEN, world.getWorldName());
    }
}
