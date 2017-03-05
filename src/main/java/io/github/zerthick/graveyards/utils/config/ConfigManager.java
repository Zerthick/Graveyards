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

package io.github.zerthick.graveyards.utils.config;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.graveyards.Graveyards;
import io.github.zerthick.graveyards.RespawnDataPacket;
import io.github.zerthick.graveyards.graveyard.Graveyard;
import io.github.zerthick.graveyards.graveyard.GraveyardGroup;
import io.github.zerthick.graveyards.graveyard.GraveyardGroupManager;
import io.github.zerthick.graveyards.utils.config.serializers.GraveyardGroupSerializer;
import io.github.zerthick.graveyards.utils.config.serializers.GraveyardSerializer;
import io.github.zerthick.graveyards.utils.config.serializers.RespawnDataPacketSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfigManager {

    public static void regsisterSerializers() {
        GraveyardSerializer.register();
        RespawnDataPacketSerializer.register();
        GraveyardGroupSerializer.register();
    }

    public static GraveyardGroupManager loadGraveyards(Graveyards plugin) {

        //Load old file if it exists
        File graveyardsFile = new File(plugin.getDefaultConfigDir().toFile(), "graveyardsData.config");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(graveyardsFile).build();

        //Convert old data over to new format
        if (graveyardsFile.exists()) {
            try {
                CommentedConfigurationNode graveyardsData = loader.load();

                Map<UUID, Map<String, Graveyard>> graveyardsMap = graveyardsData
                        .getValue(new TypeToken<Map<UUID, Map<String, Graveyard>>>() {
                        });

                if(graveyardsMap != null) {
                    GraveyardGroupManager groupManager = new GraveyardGroupManager(new HashMap<>());
                    groupManager.addGraveyardGroup(Graveyards.DEFAULT_GRAVEYARD_GROUP, new GraveyardGroup(Graveyards.DEFAULT_GRAVEYARD_GROUP, graveyardsMap));
                    graveyardsFile.renameTo(new File(plugin.getDefaultConfigDir().toFile(), "graveyardsData.conf"));
                    return groupManager;
                }
            } catch (IOException | ObjectMappingException e) {
                plugin.getLogger().error("Error loading graveyard data! Error:" + e.getMessage());
            }
        } else {

            //Load new file
            graveyardsFile = new File(plugin.getDefaultConfigDir().toFile(), "graveyardsData.conf");
            loader = HoconConfigurationLoader.builder().setFile(graveyardsFile).build();

            if (graveyardsFile.exists()) {
                try {
                    CommentedConfigurationNode graveyardGroupData = loader.load();

                    Map<String, GraveyardGroup> graveyardGroupMap = graveyardGroupData
                            .getValue(new TypeToken<Map<String, GraveyardGroup>>() {
                            });

                    if (graveyardGroupMap != null) {
                        return new GraveyardGroupManager(graveyardGroupMap);
                    }
                } catch (IOException | ObjectMappingException e) {
                    plugin.getLogger().error("Error loading graveyard data! Error:" + e.getMessage());
                }
            }
        }
        GraveyardGroupManager defaultManager = new GraveyardGroupManager(new HashMap<>());
        defaultManager.addGraveyardGroup(Graveyards.DEFAULT_GRAVEYARD_GROUP, new GraveyardGroup(Graveyards.DEFAULT_GRAVEYARD_GROUP, new HashMap<>()));
        return defaultManager;
    }

    public static void saveGraveyards(Graveyards plugin) {

        File graveyardsFile = new File(plugin.getDefaultConfigDir().toFile(), "graveyardsData.conf");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(graveyardsFile).build();

        try {
            CommentedConfigurationNode graveyardsData = loader.load();

            graveyardsData.setValue(new TypeToken<Map<String, GraveyardGroup>>() {
                                    },
                    plugin.getGraveyardGroupManager().getGraveyardGroupMap());

            loader.save(graveyardsData);
        } catch (IOException | ObjectMappingException e) {
            plugin.getLogger().error("Error saving graveyard data! Error:" + e.getMessage());
        }
    }

    public static void loadConfigValues(Graveyards plugin) {

        ConfigurationLoader<CommentedConfigurationNode> loader = plugin.getConfigLoader();

        try {
            CommentedConfigurationNode graveyardsConfig = loader.load();

            if (!plugin.getDefaultConfig().toFile().exists()) {
                graveyardsConfig.getNode("defaultMessage")
                        .setComment("Default message that will be set to the player upon spawning at a graveyard")
                        .setValue(TypeToken.of(String.class), "&2Welcome to the &a{GRAVEYARD_NAME} &2graveyard.");
                loader.save(graveyardsConfig);
            }

            ConfigValues.getInstance().setDefaultGraveyardMessage(graveyardsConfig.getNode("defaultMessage").getString());

        } catch (IOException | ObjectMappingException e) {
            plugin.getLogger().error("Error loading graveyard config! Error:" + e.getMessage());
        }
    }

    public static Map<UUID, RespawnDataPacket> loadRespawnPackets(Graveyards plugin) {

        //Rename old file if it exists
        File respawnDataFile = new File(plugin.getDefaultConfigDir().toFile(), "respawnData.config");
        if (respawnDataFile.exists()) {
            respawnDataFile.renameTo(new File(plugin.getDefaultConfigDir().toFile(), "respawnData.conf"));
        }

        respawnDataFile = new File(plugin.getDefaultConfigDir().toFile(), "respawnData.conf");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(respawnDataFile).build();

        if (respawnDataFile.exists()) {
            try {
                CommentedConfigurationNode respawnData = loader.load();

                Map<UUID, RespawnDataPacket> respawnDataMap = respawnData
                        .getValue(new TypeToken<Map<UUID, RespawnDataPacket>>() {
                        });

                if (respawnDataMap != null) {
                    return respawnDataMap;
                }
            } catch (IOException | ObjectMappingException e) {
                plugin.getLogger().error("Error loading respawn data! Error:" + e.getMessage());
            }
        }
        return new HashMap<>();
    }

    public static void saveRespawnPackets(Graveyards plugin) {

        File respawnDataFile = new File(plugin.getDefaultConfigDir().toFile(), "respawnData.conf");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(respawnDataFile).build();

        try {
            CommentedConfigurationNode respawnData = loader.load();

            respawnData.setValue(new TypeToken<Map<UUID, RespawnDataPacket>>() {
                                 },
                    plugin.getRespawnDataPacketMap());

            loader.save(respawnData);
        } catch (IOException | ObjectMappingException e) {
            plugin.getLogger().error("Error saving respawn data! Error:" + e.getMessage());
        }
    }
}
