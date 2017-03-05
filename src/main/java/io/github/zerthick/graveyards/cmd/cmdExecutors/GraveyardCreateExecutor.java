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
import io.github.zerthick.graveyards.Graveyards;
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

public class GraveyardCreateExecutor extends AbstractCmdExecutor implements CommandExecutor {


    public GraveyardCreateExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        Optional<String> nameOptional = args.getOne(CommandArgs.NAME);
        Optional<String> graveyardGroupOptional = args.getOne(CommandArgs.GROUP);
        Optional<WorldProperties> worldOptional = args.getOne(CommandArgs.WORLD);
        Optional<Vector3d> locationOptional = args.getOne(CommandArgs.LOCATION);
        Optional<Vector3d> rotationOptional = args.getOne(CommandArgs.ROTATION);


        if (nameOptional.isPresent()) {

            GraveyardGroup graveyardGroup = manager.getGraveyardGroup(Graveyards.DEFAULT_GRAVEYARD_GROUP).get();
            if (graveyardGroupOptional.isPresent()) {
                Optional<GraveyardGroup> groupOptional = manager.getGraveyardGroup(graveyardGroupOptional.get().toLowerCase().replaceAll("\\s+", ""));
                if (groupOptional.isPresent()) {
                    graveyardGroup = groupOptional.get();
                } else {
                    src.sendMessage(Text.of(TextColors.GREEN, "The Group ", TextColors.DARK_GREEN, graveyardGroupOptional.get(),
                            TextColors.GREEN, " does not exist!"));
                    return CommandResult.success();
                }
            }

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

            Vector3d rotation = null;
            if (rotationOptional.isPresent()) {
                rotation = rotationOptional.get();
            } else if (src instanceof Player) {
                rotation = ((Player) src).getRotation();
            }

            if (world != null && location != null && rotation != null) {

                String graveyardName = nameOptional.get();

                if (!graveyardGroup.exists(graveyardName, world.getUniqueId())) {
                    graveyardGroup.addGraveyard(graveyardName, location, rotation, world.getUniqueId());
                    src.sendMessage(successMessageBuilder(graveyardName, world, location, rotation, graveyardGroup.getGroupName()));
                } else {
                    src.sendMessage(failureMessageBuilder(graveyardName, world, graveyardGroup.getGroupName()));
                }
                return CommandResult.success();
            }
        }

        src.sendMessage(Text
                .of(TextColors.GREEN,
                        "You must specify a Name, World, Location, and Rotation for the graveyard!"));

        return CommandResult.empty();
    }

    private Text successMessageBuilder(String name, WorldProperties world,
                                       Vector3i location, Vector3d rotation, String groupName) {

        return Text.of(TextColors.GREEN, "Created Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                " at Location ", TextColors.DARK_GREEN, location.toString(),
                TextColors.GREEN, " with rotation ", TextColors.DARK_GREEN, rotation,
                TextColors.GREEN, " in Group ", TextColors.DARK_GREEN, groupName);
    }

    private Text failureMessageBuilder(String name, WorldProperties world, String groupName) {

        return Text.of(TextColors.GREEN, "Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                " in Group ", TextColors.DARK_GREEN, groupName, TextColors.GREEN,
                " already exists!");
    }
}
