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
import io.github.zerthick.graveyards.graveyard.Graveyard;
import io.github.zerthick.graveyards.graveyard.GraveyardGroup;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.*;
import java.util.stream.Collectors;

public class GraveyardListExecutor extends AbstractCmdExecutor implements CommandExecutor {

    public GraveyardListExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        Optional<WorldProperties> worldOptional = args.getOne(CommandArgs.WORLD);

        WorldProperties world = null;

        if (worldOptional.isPresent()) {
            world = worldOptional.get();
        } else if (src instanceof Player) {
            Player player = (Player) src;
            world = player.getWorld().getProperties();
        }

        if (world != null) {
            Map<String, GraveyardGroup> graveyardGroups = manager.getGraveyardGroupMap();
            Map<String, List<Graveyard>> applicableGraveyards = new HashMap<>();
            UUID finalWorldUUID = world.getUniqueId();
            graveyardGroups.forEach((name, group) -> {
                List<Graveyard> graveyardList = group.getGraveyardList(finalWorldUUID);
                applicableGraveyards.put(name, graveyardList);
            });

            if (!applicableGraveyards.isEmpty()) {
                listBuilder(world, applicableGraveyards, plugin).sendTo(src);
            } else {
                src.sendMessage(Text.of(TextColors.GREEN,
                        "There are no graveyards in World ",
                        TextColors.DARK_GREEN, world.getWorldName()));
            }
            return CommandResult.success();
        }
        src.sendMessage(Text.of("You must specify a world from the console!"));
        return CommandResult.empty();
    }

    private PaginationList.Builder listBuilder(WorldProperties world,
                                               Map<String, List<Graveyard>> graveyardMap, Graveyards plugin) {

        List<Text> graveyardInfo = new ArrayList<>();
        graveyardMap.forEach((name, list) -> {
            graveyardInfo.add(Text.of(TextColors.GREEN, name, ":"));
            graveyardInfo.addAll(list.stream().map(graveyard -> Text.of("  ", graveyard.getName(), ": ",
                    graveyard.getLocation().toString())).collect(Collectors.toList()));
        });

        PaginationService pagServ = plugin.getGame().getServiceManager().provide(PaginationService.class).get();
        PaginationList.Builder builder = pagServ.builder();
        builder.contents(graveyardInfo)
                .title(Text
                        .builder("Graveyards in ")
                        .color(TextColors.GREEN)
                        .append(Text.builder(world.getWorldName())
                                .color(TextColors.DARK_GREEN).build()).build())
                .padding(Text.of("-"));
        return builder;
    }
}
