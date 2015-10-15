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

package io.github.zerthick.graveyards.cmdexecuters;

import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class GraveyardExecutor implements CommandExecutor {

    private PluginContainer container;

    public GraveyardExecutor(PluginContainer pluginContainer) {
        super();
        container = pluginContainer;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        src.sendMessage(Texts.of(TextColors.DARK_GREEN, container.getName(),
                TextColors.GREEN, " version: ", TextColors.DARK_GREEN,
                container.getVersion(), TextColors.GREEN, " by ",
                TextColors.DARK_GREEN, "Zerthick"));

        return CommandResult.success();
    }
}
