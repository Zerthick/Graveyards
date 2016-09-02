/*
 * Copyright (C) 2016  Zerthick
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GraveyardListExecutor extends AbstractCmdExecutor implements CommandExecutor {

    public GraveyardListExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        Optional<WorldProperties> world = args.getOne(CommandArgs.WORLD);

        if (world.isPresent()) {
            List<Graveyard> graveyardList = manager.getGraveyardList(world.get().getUniqueId());
            if (!graveyardList.isEmpty()) {
                listBuilder(world.get(), graveyardList, plugin).sendTo(src);
            } else {
                src.sendMessage(Text.of(TextColors.GREEN,
                        "There are no graveyards in World ",
                        TextColors.DARK_GREEN, world.get().getWorldName()));
            }
            return CommandResult.success();
        }
        if (src instanceof Player) {
            Player player = (Player) src;
            List<Graveyard> graveyardList = manager.getGraveyardList(player.getWorld().getUniqueId());
            if (!graveyardList.isEmpty()) {
                listBuilder(player.getWorld().getProperties(), graveyardList, plugin).sendTo(src);
            } else {
                src.sendMessage(Text.of(TextColors.GREEN,
                        "There are no graveyards in World ",
                        TextColors.DARK_GREEN, player.getWorld().getName()));
            }
            return CommandResult.success();
        }
        src.sendMessage(Text.of("You must specify a world from the console!"));
        return CommandResult.empty();
    }

    private PaginationList.Builder listBuilder(WorldProperties world,
                                               List<Graveyard> graveyardList, Graveyards plugin) {

        List<Text> graveyardInfo = graveyardList.stream().map(graveyard -> Text.of(graveyard.getName(), ": ",
                graveyard.getLocation().toString())).collect(Collectors.toList());

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
