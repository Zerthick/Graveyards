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

import io.github.zerthick.graveyards.GraveyardsMain;
import io.github.zerthick.graveyards.utils.Graveyard;
import io.github.zerthick.graveyards.utils.GraveyardManager2;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GraveyardListCmdExc implements CommandExecutor {

    private PluginContainer container;


    public GraveyardListCmdExc(PluginContainer pluginContainer) {
        super();
        this.container = pluginContainer;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        GraveyardsMain plugin = (GraveyardsMain) (container.getInstance() instanceof GraveyardsMain ? container
                .getInstance() : null);
        GraveyardManager2 manager = plugin.getGraveyardManager();

        Optional<WorldProperties> world = args.getOne("World");

        if (world.isPresent()) {
            List<Graveyard> graveyardList = manager.getGraveyardList(world.get().getUniqueId());
            if (graveyardList != null) {
                listBuilder(world.get(), graveyardList, plugin).sendTo(src);
            } else {
                src.sendMessage(Texts.of(TextColors.GREEN,
                        "There are no graveyards in World ",
                        TextColors.DARK_GREEN, world.get().getWorldName()));
            }
            return CommandResult.success();
        }
        if (src instanceof Player) {
            Player player = (Player) src;
            List<Graveyard> graveyardList = manager.getGraveyardList(player.getWorld().getUniqueId());
            if (graveyardList != null) {
                listBuilder(player.getWorld().getProperties(), graveyardList, plugin).sendTo(src);
            } else {
                src.sendMessage(Texts.of(TextColors.GREEN,
                        "There are no graveyards in World ",
                        TextColors.DARK_GREEN, player.getWorld().getName()));
            }
            return CommandResult.success();
        }
        src.sendMessage(Texts.of("You must specify a world from the console!"));
        return CommandResult.empty();
    }

    private PaginationBuilder listBuilder(WorldProperties world,
                                          List<Graveyard> graveyardList, GraveyardsMain plugin) {

        List<Text> graveyardInfo = graveyardList.stream().map(graveyard -> Texts.of(graveyard.getName(), ": ",
                graveyard.getLocation().toString())).collect(Collectors.toList());

        PaginationService pagServ = plugin.getGame().getServiceManager().provide(PaginationService.class).get();
        PaginationBuilder builder = pagServ.builder();
        builder.contents(graveyardInfo)
                .title(Texts
                        .builder("Graveyards in ")
                        .color(TextColors.GREEN)
                        .append(Texts.builder(world.getWorldName())
                                .color(TextColors.DARK_GREEN).build()).build())
                .paddingString("-");
        return builder;
    }
}
