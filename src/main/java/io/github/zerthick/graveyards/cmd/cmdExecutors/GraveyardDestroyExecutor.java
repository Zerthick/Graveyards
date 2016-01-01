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

        Optional<String> name = args.getOne("Name");
        Optional<WorldProperties> world = args.getOne("World");

        if (name.isPresent()) {
            if (world.isPresent()) {

                manager.removeGraveyard(name.get(), world.get().getUniqueId());
                src.sendMessage(successMessageBuilder(name.get(), world.get()));

                return CommandResult.success();
            }
            if (src instanceof Player) {
                Player player = (Player) src;

                manager.removeGraveyard(name.get(), player.getWorld().getUniqueId());
                src.sendMessage(successMessageBuilder(name.get(), player.getWorld().getProperties()));

                return CommandResult.success();
            }
        }
        src.sendMessage(Text.of(TextColors.GREEN,
                "You must specify a Name and World for the graveyard!"));

        return CommandResult.empty();
    }

    private Text successMessageBuilder(String name, WorldProperties world) {

        return Text.of(TextColors.GREEN, "Destroyed Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " in World ",
                TextColors.DARK_GREEN, world.getWorldName());
    }
}