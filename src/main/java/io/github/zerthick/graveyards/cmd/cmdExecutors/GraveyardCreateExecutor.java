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
import java.util.UUID;

public class GraveyardCreateExecutor extends AbstractCmdExecutor implements CommandExecutor {


    public GraveyardCreateExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        Optional<String> name = args.getOne("Name");
        Optional<WorldProperties> world = args.getOne("World");
        Optional<Vector3d> location = args.getOne("Location");

        if (name.isPresent()) {
            if (location.isPresent() && world.isPresent()) {

                String graveyardName = name.get();
                UUID worldUUID = world.get().getUniqueId();

                if(!manager.exists(graveyardName, worldUUID)) {
                    manager.addGraveyard(graveyardName, location.get().toInt(), worldUUID);
                    src.sendMessage(successMessageBuilder(graveyardName, world.get(), location.get()));
                } else {
                    src.sendMessage(failureMessageBuilder(graveyardName, world.get()));
                }
                return CommandResult.success();
            }
            if (src instanceof Player) {
                Player player = (Player) src;

                String graveyardName = name.get();
                UUID worldUUID = player.getWorld().getUniqueId();

                if(!manager.exists(graveyardName, worldUUID)) {
                    manager.addGraveyard(graveyardName, player.getLocation().getBlockPosition(), worldUUID);
                    src.sendMessage(successMessageBuilder(graveyardName, player.getWorld().getProperties(), player.getLocation().getPosition()));
                } else {
                    src.sendMessage(failureMessageBuilder(graveyardName, world.get()));
                }
                return CommandResult.success();
            }
        }

        src.sendMessage(Texts
                .of(TextColors.GREEN,
                        "You must specify a Name, World, and Location for the graveyard!"));

        return CommandResult.empty();
    }

    private Text successMessageBuilder(String name, WorldProperties world,
                                       Vector3d location) {

        return Texts.of(TextColors.GREEN, "Created Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                " at Location ", TextColors.DARK_GREEN, location.toInt().toString());
    }

    private Text failureMessageBuilder(String name, WorldProperties world) {

        return Texts.of(TextColors.GREEN, "Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                " already exists!");
    }
}
