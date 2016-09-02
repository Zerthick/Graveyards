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

package io.github.zerthick.graveyards;

import com.google.inject.Inject;
import io.github.zerthick.graveyards.cmd.GraveyardsCommandRegister;
import io.github.zerthick.graveyards.graveyard.Graveyard;
import io.github.zerthick.graveyards.graveyard.GraveyardsManager;
import io.github.zerthick.graveyards.utils.config.ConfigManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Plugin(id = "graveyards",
        name = "Graveyards",
        version = "2.1.2",
        description = "A player spawn-point plugin.")
public class Graveyards {

    private GraveyardsManager graveyardsManager;
    private ConfigManager configManager;
    private Map<UUID, RespawnDataPacket> respawnDataPackets;

    @Inject
    private Game game;
    @Inject
    private Logger logger;
    @Inject
    private PluginContainer instance;

    //Config Stuff
    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path defaultConfigDir;

    public Path getDefaultConfig() {
        return defaultConfig;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getConfigLoader() {
        return configLoader;
    }

    public Path getDefaultConfigDir() {
        return defaultConfigDir;
    }

    public GraveyardsManager getGraveyardsManager() {
        return graveyardsManager;
    }

    public Game getGame() {
        return game;
    }

    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {

        //Set up config manager
        configManager = new ConfigManager(this);

        // Load graveyards from file
        graveyardsManager = configManager.loadGraveyards();

        // Load config values from file
        configManager.loadConfigValues();

        // Load respawn data from file
        respawnDataPackets = configManager.loadRespawnPackets();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        // Register Commands
        GraveyardsCommandRegister commandRegister = new GraveyardsCommandRegister(
                instance);
        commandRegister.registerCmds();

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
            Optional<Graveyard> nearestGraveyardOptional = graveyardsManager.findNearestGraveyard(player.getLocation().getBlockPosition(), player.getWorld().getUniqueId());
            if (nearestGraveyardOptional.isPresent()) {
                Graveyard nearestGraveyard = nearestGraveyardOptional.get();
                respawnDataPackets.put(player.getUniqueId(), new RespawnDataPacket(nearestGraveyard.getMessage(), nearestGraveyard.getRotation(), nearestGraveyard.getLocation()));
            }
        }
    }

    @Listener
    public void onPlayerRespawn(RespawnPlayerEvent event) {
        Player player = event.getTargetEntity();
        if (respawnDataPackets.containsKey(player.getUniqueId())) {
            RespawnDataPacket packet = respawnDataPackets.remove(player.getUniqueId());
            event.setToTransform(event.getToTransform().setRotation(packet.getRespawnRotation()).setPosition(packet.getRespawnLocation().toDouble()));
            player.sendMessage(packet.getRespawnMessage());
        }
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {

        // Save graveyards to file
        configManager.saveGraveyards();

        // Save respawn data to file
        configManager.saveRespawnPackets();
    }

    public Map<UUID, RespawnDataPacket> getRespawnDataPacketMap() {
        return respawnDataPackets;
    }
}
