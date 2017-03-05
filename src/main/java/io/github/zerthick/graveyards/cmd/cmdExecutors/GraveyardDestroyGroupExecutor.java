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
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class GraveyardDestroyGroupExecutor extends AbstractCmdExecutor {

    public GraveyardDestroyGroupExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        Optional<String> groupNameOptional = args.getOne(CommandArgs.GROUP_NAME);

        if (groupNameOptional.isPresent()) {
            String cleanedGroupName = groupNameOptional.get().toLowerCase().replaceAll("\\s+", "");
            if (!manager.exists(cleanedGroupName)) {
                src.sendMessage(Text.of(TextColors.GREEN, "A group with the name ", TextColors.DARK_GREEN,
                        cleanedGroupName, TextColors.GREEN, " does not exist!"));
            } else {
                if (cleanedGroupName.equals(Graveyards.DEFAULT_GRAVEYARD_GROUP)) {
                    src.sendMessage(Text.of(TextColors.GREEN, "You can't destroy the default group!"));
                } else {
                    manager.removeGraveyardGroup(cleanedGroupName);
                    src.sendMessage(Text.of(TextColors.GREEN, "Successfully destroyed Graveyard Group ", TextColors.DARK_GREEN,
                            cleanedGroupName, TextColors.GREEN, "."));
                }
            }
            return CommandResult.success();
        }
        src.sendMessage(Text.of("You must specify a group name!"));
        return CommandResult.empty();
    }
}
