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

package io.github.zerthick.graveyards.utils;

import io.github.zerthick.graveyards.GraveyardsMain;
import io.github.zerthick.graveyards.cmdexecuters.GraveyardExecutor;
import io.github.zerthick.graveyards.cmdexecuters.GraveyardCreateExecutor;
import io.github.zerthick.graveyards.cmdexecuters.GraveyardDestroyExecutor;
import io.github.zerthick.graveyards.cmdexecuters.GraveyardListExecutor;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

public class GraveyardsCommandRegister {

    private PluginContainer container;
    private Game game;

    public GraveyardsCommandRegister(PluginContainer pluginContainer) {
        super();
        container = pluginContainer;
        GraveyardsMain plugin = (GraveyardsMain) (container.getInstance() instanceof GraveyardsMain ? container
                .getInstance() : null);
        game = plugin.getGame();
    }

    public void registerCmds() {
        // gy create <Name> [World] [x, y, z]
        CommandSpec graveyardCreateCommand = CommandSpec
                .builder()
                .description(
                        Texts.of("Creates a graveyard with the given name at the provided world and location or your current world and location if neither is provided."))
                .permission("graveyards.command.create")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Texts
                                .of("Name"))),
                        GenericArguments.optional(GenericArguments.world(
                                Texts.of("World"), game)),
                        GenericArguments.optional(GenericArguments
                                .vector3d(Texts.of("Location"))))
                .executor(new GraveyardCreateExecutor(container)).build();
        // gy destroy <Name> [World]
        CommandSpec graveyardDestroyCommand = CommandSpec
                .builder()
                .description(
                        Texts.of("Destroys a graveyard with the given name in the provided world or your current world if none is provided."))
                .permission("graveyards.command.destroy")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Texts
                                .of("Name"))),
                        GenericArguments.optional(GenericArguments.world(
                                Texts.of("World"), game)))
                .executor(new GraveyardDestroyExecutor(container)).build();
        // gy list [World]
        CommandSpec graveyardListCommand = CommandSpec
                .builder()
                .description(
                        Texts.of("Lists all graveyards in the provided world or your current world if none is provided."))
                .permission("graveyards.command.list")
                .arguments(
                        GenericArguments.optional(GenericArguments
                                .onlyOne(GenericArguments.world(
                                        Texts.of("World"), game))))
                .executor(new GraveyardListExecutor(container)).build();
        // gy
        CommandSpec graveyardCommand = CommandSpec.builder()
                .description(Texts.of("/gy [list|create|destroy]"))
                .permission("graveyards.command")
                .executor(new GraveyardExecutor(container))
                .child(graveyardCreateCommand, "create", "add")
                .child(graveyardDestroyCommand, "destroy", "remove")
                .child(graveyardListCommand, "list").build();
        game.getCommandDispatcher().register(container.getInstance(),
                graveyardCommand, "graveyard", "gy");
    }
}
