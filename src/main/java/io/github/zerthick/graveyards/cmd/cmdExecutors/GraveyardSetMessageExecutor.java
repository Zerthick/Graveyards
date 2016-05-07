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

import io.github.zerthick.graveyards.graveyard.Graveyard;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class GraveyardSetMessageExecutor extends AbstractCmdExecutor {

    public GraveyardSetMessageExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {

        Optional<String> name = args.getOne("Name");
        Optional<WorldProperties> world = args.getOne("World");
        Optional<String> message = args.getOne("Message");

        if (name.isPresent() && message.isPresent()) {
            if (world.isPresent()) {

                Optional<Graveyard> graveyardOptional = manager.getGraveyard(name.get(), world.get().getUniqueId());

                if (graveyardOptional.isPresent()) {
                    graveyardOptional.get().setMessage(TextSerializers.FORMATTING_CODE.deserialize(message.get()));
                    src.sendMessage(successMessageBuilder(name.get(), world.get()));
                } else {
                    src.sendMessage(failureMessageBuilder(name.get(), world.get()));
                }

                return CommandResult.success();
            }
            if (src instanceof Player) {
                Player player = (Player) src;

                Optional<Graveyard> graveyardOptional = manager.getGraveyard(name.get(), player.getWorld().getUniqueId());

                if (graveyardOptional.isPresent()) {
                    graveyardOptional.get().setMessage(TextSerializers.FORMATTING_CODE.deserialize(message.get()));
                    src.sendMessage(successMessageBuilder(name.get(), player.getWorld().getProperties()));
                } else {
                    player.sendMessage(failureMessageBuilder(name.get(), player.getWorld().getProperties()));
                }

                return CommandResult.success();
            }
        }
        src.sendMessage(Text.of(TextColors.GREEN,
                "You must specify a Name and World and Message for the graveyard!"));

        return CommandResult.empty();
    }

    private Text successMessageBuilder(String name, WorldProperties world) {

        return Text.of(TextColors.GREEN, "Successfully set message of Graveyard ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " in World ",
                TextColors.DARK_GREEN, world.getWorldName());
    }

    private Text failureMessageBuilder(String name, WorldProperties world) {

        return Text.of(TextColors.GREEN, "There is no graveyard  ",
                TextColors.DARK_GREEN, name, TextColors.GREEN, " in World ",
                TextColors.DARK_GREEN, world.getWorldName(), TextColors.GREEN,
                "!");
    }
}
