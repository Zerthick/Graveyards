/*
 * Copyright (C) 2017  Zerthick
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
import io.github.zerthick.graveyards.graveyard.Graveyard;
import io.github.zerthick.graveyards.graveyard.GraveyardGroup;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
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

        Optional<String> graveyardGroupOptional = args.getOne(CommandArgs.GROUP);
        Optional<WorldProperties> worldOptional = args.getOne(CommandArgs.WORLD);
        Optional<Vector3d> locationOptional = args.getOne(CommandArgs.LOCATION);

        WorldProperties world = null;
        if (worldOptional.isPresent()) {
            world = worldOptional.get();
        } else if (src instanceof Player) {
            world = ((Player) src).getWorld().getProperties();
        }

        Vector3i location = null;
        if (locationOptional.isPresent()) {
            location = locationOptional.get().toInt();
        } else if (src instanceof Player) {
            location = ((Player) src).getLocation().getBlockPosition();
        }

        if (location != null && world != null) {

            Optional<Graveyard> nearestGraveyardOptional;
            if (graveyardGroupOptional.isPresent()) {
                Optional<GraveyardGroup> groupOptional = manager.getGraveyardGroup(graveyardGroupOptional.get().toLowerCase().replaceAll("\\s+", ""));
                if (groupOptional.isPresent()) {
                    GraveyardGroup graveyardGroup = groupOptional.get();
                    nearestGraveyardOptional = manager
                            .findNearestGraveyardWithFilter(location, world.getUniqueId(), e -> e.getKey().equals(graveyardGroup.getGroupNameCleaned()));
                    if (nearestGraveyardOptional.isPresent()) {
                        Graveyard nearestGraveyard = nearestGraveyardOptional.get();
                        src.sendMessage(successMessageBuilder(world, nearestGraveyard.getName(), nearestGraveyard.getLocation(), graveyardGroup.getGroupName()));
                    } else {
                        src.sendMessage(failureMessageBuilder(world, graveyardGroup.getGroupName()));
                    }
                } else {
                    src.sendMessage(Text.of(TextColors.GREEN, "The Group ", TextColors.DARK_GREEN, graveyardGroupOptional.get(),
                            TextColors.GREEN, " does not exist!"));
                    return CommandResult.success();
                }
            } else {
                nearestGraveyardOptional = manager.findNearestGraveyard(location, world.getUniqueId());
                if (nearestGraveyardOptional.isPresent()) {
                    Graveyard nearestGraveyard = nearestGraveyardOptional.get();
                    src.sendMessage(successMessageBuilder(world, nearestGraveyard.getName(), nearestGraveyard.getLocation()));
                } else {
                    src.sendMessage(failureMessageBuilder(world));
                }
            }
            return CommandResult.success();
        }

        src.sendMessage(Text.of(TextColors.GREEN,
                "You must specify a Name and World for the graveyard!"));

        return CommandResult.success();
    }

    private Text successMessageBuilder(WorldProperties world, String name, Vector3i location, String groupName) {

        return Text.of(TextColors.GREEN, "The nearest Graveyard in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                " in Group ", TextColors.DARK_GREEN, groupName, TextColors.GREEN, " is Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " at Location ", TextColors.DARK_GREEN, location.toString());
    }

    private Text successMessageBuilder(WorldProperties world, String name, Vector3i location) {

        return Text.of(TextColors.GREEN, "The nearest Graveyard in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN, " is Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " at Location ", TextColors.DARK_GREEN, location.toString());
    }

    private Text failureMessageBuilder(WorldProperties world, String groupName) {

        return Text.of(TextColors.GREEN,
                "There are no graveyards in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                " in Group ", TextColors.DARK_GREEN, groupName, TextColors.GREEN, ".");
    }

    private Text failureMessageBuilder(WorldProperties world) {

        return Text.of(TextColors.GREEN,
                "There are no graveyards in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                ".");
    }
}
