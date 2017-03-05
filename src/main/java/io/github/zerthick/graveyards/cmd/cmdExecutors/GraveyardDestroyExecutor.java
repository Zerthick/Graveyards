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

public class GraveyardDestroyExecutor extends AbstractCmdExecutor implements CommandExecutor {


    public GraveyardDestroyExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        Optional<String> nameOptional = args.getOne(CommandArgs.NAME);
        Optional<String> graveyardGroupOptional = args.getOne(CommandArgs.GROUP);
        Optional<WorldProperties> worldOptional = args.getOne(CommandArgs.WORLD);

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

            if (world != null) {

                if (graveyardGroup.removeGraveyard(nameOptional.get(), world.getUniqueId()).isPresent()) {
                    src.sendMessage(successMessageBuilder(nameOptional.get(), world, graveyardGroup.getGroupName()));
                } else {
                    src.sendMessage(failureMessageBuilder(nameOptional.get(), world, graveyardGroup.getGroupName()));
                }

                return CommandResult.success();
            }
        }
        src.sendMessage(Text.of(TextColors.GREEN,
                "You must specify a Name and World for the graveyard!"));

        return CommandResult.empty();
    }

    private Text successMessageBuilder(String name, WorldProperties world, String groupName) {

        return Text.of(TextColors.GREEN, "Destroyed Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                " in Group ", TextColors.DARK_GREEN, groupName, TextColors.GREEN, ".");
    }

    private Text failureMessageBuilder(String name, WorldProperties world, String groupName) {

        return Text.of(TextColors.GREEN, "There is no graveyard  ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                " in Group ", TextColors.DARK_GREEN, groupName, TextColors.GREEN,
                "!");
    }
}
