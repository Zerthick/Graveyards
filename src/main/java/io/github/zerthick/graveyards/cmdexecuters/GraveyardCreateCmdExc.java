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

import com.flowpowered.math.vector.Vector3d;
import io.github.zerthick.graveyards.GraveyardsMain;
import io.github.zerthick.graveyards.utils.GraveyardManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class GraveyardCreateCmdExc implements CommandExecutor {

    private PluginContainer container;

    public GraveyardCreateCmdExc(PluginContainer pluginContainer) {
        super();
        this.container = pluginContainer;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        GraveyardsMain plugin = (GraveyardsMain) (container.getInstance() instanceof GraveyardsMain ? container
                .getInstance() : null);
        GraveyardManager manager = plugin.getGraveyardManager();

        Optional<String> name = args.getOne("Name");
        Optional<WorldProperties> world = args.getOne("World");
        Optional<Vector3d> location = args.getOne("Location");

        if (name.isPresent()) {
            if (location.isPresent() && world.isPresent()) {

                manager.addGraveyard(name.get(), location.get().toInt(), world.get().getUniqueId());
                src.sendMessage(successMessageBuilder(name.get(), world.get(), location.get()));

                return CommandResult.success();
            }
            if (src instanceof Player) {
                Player player = (Player) src;

                manager.addGraveyard(name.get(), player.getLocation().getBlockPosition(), player.getWorld().getUniqueId());
                src.sendMessage(successMessageBuilder(name.get(), player.getWorld().getProperties(), player.getLocation().getPosition()));

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
                " at Location ", TextColors.DARK_GREEN, location.toString());
    }
}
