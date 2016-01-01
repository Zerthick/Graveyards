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

package io.github.zerthick.graveyards.cmd;

import io.github.zerthick.graveyards.cmd.cmdExecutors.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;


public class GraveyardsCommandRegister {

    private PluginContainer container;

    public GraveyardsCommandRegister(PluginContainer pluginContainer) {
        super();
        container = pluginContainer;
    }

    public void registerCmds() {

        // gy tp <Name> [World]
        CommandSpec graveyardTeleportCommand = CommandSpec
                .builder()
                .description(
                        Text.of("Teleports you to the graveyard with the given name at the provided world or your current world if none is provided."))
                .permission("graveyards.command.teleport")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text
                                .of("Name"))),
                        GenericArguments.optional(GenericArguments.world(
                                Text.of("World"))))
                .executor(new GraveyardTeleportExecutor(container)).build();

        // gy nearest [World] [x, y, z]
        CommandSpec graveyardNearestCommand = CommandSpec
                .builder()
                .description(
                        Text.of("Identifies the nearest graveyard from the provided world and location or your current world and location if neither is provided."))
                .permission("graveyards.command.nearest")
                .arguments(
                        GenericArguments.optional(GenericArguments.world(
                                Text.of("World"))),
                        GenericArguments.optional(GenericArguments
                                .vector3d(Text.of("Location"))))
                .executor(new GraveyardNearestExecutor(container)).build();

        // gy create <Name> [World] [x, y, z]
        CommandSpec graveyardCreateCommand = CommandSpec
                .builder()
                .description(
                        Text.of("Creates a graveyard with the given name at the provided world and location or your current world and location if neither is provided."))
                .permission("graveyards.command.create")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text
                                .of("Name"))),
                        GenericArguments.optional(GenericArguments.world(
                                Text.of("World"))),
                        GenericArguments.optional(GenericArguments
                                .vector3d(Text.of("Location"))))
                .executor(new GraveyardCreateExecutor(container)).build();

        // gy destroy <Name> [World]
        CommandSpec graveyardDestroyCommand = CommandSpec
                .builder()
                .description(
                        Text.of("Destroys a graveyard with the given name in the provided world or your current world if none is provided."))
                .permission("graveyards.command.destroy")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text
                                .of("Name"))),
                        GenericArguments.optional(GenericArguments.world(
                                Text.of("World"))))
                .executor(new GraveyardDestroyExecutor(container)).build();

        // gy list [World]
        CommandSpec graveyardListCommand = CommandSpec
                .builder()
                .description(
                        Text.of("Lists all graveyards in the provided world or your current world if none is provided."))
                .permission("graveyards.command.list")
                .arguments(
                        GenericArguments.optional(GenericArguments
                                .onlyOne(GenericArguments.world(
                                        Text.of("World")))))
                .executor(new GraveyardListExecutor(container)).build();

        // gy
        CommandSpec graveyardCommand = CommandSpec.builder()
                .description(Text.of("/gy [list|create|destroy]"))
                .permission("graveyards.command.help")
                .executor(new GraveyardExecutor(container))
                .child(graveyardTeleportCommand, "teleport", "tp")
                .child(graveyardNearestCommand, "nearest", "closest", "fd")
                .child(graveyardCreateCommand, "create", "add", "mk")
                .child(graveyardDestroyCommand, "destroy", "remove", "rm")
                .child(graveyardListCommand, "list", "ls").build();
        Sponge.getGame().getCommandManager().register(container.getInstance().get(),
                graveyardCommand, "graveyard", "gy");
    }
}
