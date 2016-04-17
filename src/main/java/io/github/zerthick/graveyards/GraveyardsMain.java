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
import io.github.zerthick.graveyards.cmd.GraveyardsCommandRegister;
import io.github.zerthick.graveyards.graveyard.Graveyard;
import io.github.zerthick.graveyards.graveyard.GraveyardManager;
import io.github.zerthick.graveyards.utils.DbUtils;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Plugin(id = "graveyards", name = "Graveyards", version = "1.2.0")
public class GraveyardsMain {

    private GraveyardManager graveyardManager;
    @Inject
    private Game game;
    @Inject
    private Logger logger;
    @Inject
    private PluginContainer instance;
    private Map<UUID, Text> deathMessages;

    public GraveyardManager getGraveyardManager() {
        return graveyardManager;
    }

    public Game getGame() {
        return game;
    }

    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        // Initialize Manager with Graveyards from db
        graveyardManager = new GraveyardManager(DbUtils.readGraveyards());

        // Register Commands
        GraveyardsCommandRegister commandRegister = new GraveyardsCommandRegister(
                instance);
        commandRegister.registerCmds();

        // Initialize Death Messages Map
        deathMessages = new HashMap<>();

        // Log Start Up to Console
        getLogger().info(
                instance.getName() + " version " + instance.getVersion().orElse("")
                        + " enabled!");
    }

    @Listener
    public void onEntityDeath(DestructEntityEvent.Death event) {
        Entity entity = event.getTargetEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            Graveyard nearestGraveyard = graveyardManager.findNearestGraveyard(player.getLocation().getBlockPosition(), player.getWorld().getUniqueId());
            if (nearestGraveyard != null) {
                setRespawnLocation(player, new Location<>(player.getWorld(), nearestGraveyard.getLocation()));
                deathMessages.put(player.getUniqueId(), Text.of(TextColors.GREEN, "Welcome to the ", TextColors.DARK_GREEN,
                        nearestGraveyard.getName(), TextColors.GREEN, " graveyard."));
            }
        }
    }

    @Listener
    public void onPlayerRespawn(RespawnPlayerEvent event) {
        Player player = event.getTargetEntity();
        if (deathMessages.containsKey(player.getUniqueId())) {
            player.sendMessage(deathMessages.remove(player.getUniqueId()));
        }
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {

        // Save Graveyards to db
        DbUtils.writeGraveyards(graveyardManager.getGraveyardMap());
    }

    /**
     * Private helper method to reset a player's respawn location
     *
     * @param player   the player to change respawn location of
     * @param location the location to set the player's respawn
     */
    private void setRespawnLocation(Player player, Location<World> location) {
        getGame().getCommandManager().process(getGame().getServer().getConsole(), "minecraft:spawnpoint " +  player.getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
        /*Map<UUID, RespawnLocation> respawnLocationMap = player.get(Keys.RESPAWN_LOCATIONS).orElse(new HashMap<>());
        respawnLocationMap.put(location.getExtent().getUniqueId(), RespawnLocation.builder().location(location).forceSpawn(true).build());
        player.offer(Keys.RESPAWN_LOCATIONS, respawnLocationMap);*/
    }
}
