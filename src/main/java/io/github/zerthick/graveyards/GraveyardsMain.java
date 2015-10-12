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

package io.github.zerthick.graveyards;

import com.google.inject.Inject;
import io.github.zerthick.graveyards.utils.GraveyardManager;
import io.github.zerthick.graveyards.utils.Graveyard;
import io.github.zerthick.graveyards.utils.GraveyardsCommandRegister;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.data.manipulator.mutable.entity.RespawnLocationData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.File;
import java.util.logging.Logger;

@Plugin(id = "Graveyards", name = "Graveyards", version = "0.1")
public class GraveyardsMain {

    private GraveyardManager graveyardManager;

    public GraveyardManager getGraveyardManager(){
        return graveyardManager;
    }

    @Inject
    private Game game;

    public Game getGame() {
        return game;
    }

    @Inject
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    @Inject
    private PluginContainer instance;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File dConfig;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> confManager;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        // Initialize Manager
        graveyardManager = new GraveyardManager();

        // Register Commands
        GraveyardsCommandRegister commandRegister = new GraveyardsCommandRegister(
                instance);
        commandRegister.registerCmds();

        // Log Start Up to Console
        getLogger().info(
                instance.getName() + " version " + instance.getVersion()
                        + " enabled!");
    }

    @Listener
    public void onEnitityDeath(DestructEntityEvent.Death event) {
        if (event.getTargetEntity() instanceof Player) {
            Player player = (Player) event.getTargetEntity();
            Graveyard nearestGraveyard = graveyardManager.findNearestGraveyard(player.getLocation().getBlockPosition(), player.getWorld().getUniqueId());
            if (nearestGraveyard != null) {
                setRespawnLocation(player, new Location<>(player.getLocation().getExtent(), nearestGraveyard.getLocation()));
            }
        }
    }

    /**
     * Private helper method to reset a player's respawn location
     * @param player    the player to change respawn location of
     * @param location  the location to set the player's respawn
     */
    private void setRespawnLocation(Player player, Location<World> location) {
        RespawnLocationData data = player.getOrCreate(RespawnLocationData.class).get(); // It's a player, assume it can be created
        data.respawnLocation().put(location.getExtent().getUniqueId(), location.getPosition());
    }
}
