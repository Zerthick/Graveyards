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

package io.github.zerthick.graveyards.utils.config;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.graveyards.Graveyards;
import io.github.zerthick.graveyards.graveyard.Graveyard;
import io.github.zerthick.graveyards.graveyard.GraveyardsManager;
import io.github.zerthick.graveyards.utils.config.serializers.GraveyardSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfigManager {

    private Graveyards plugin;
    private Logger logger;

    public ConfigManager(Graveyards plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();

        TypeSerializers.getDefaultSerializers()
                .registerType(TypeToken.of(Graveyard.class), new GraveyardSerializer());
    }

    public GraveyardsManager loadGraveyards() {

        File graveyardsFile = new File(plugin.getDefaultConfigDir().toFile(), "graveyardsData.config");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(graveyardsFile).build();

        if (graveyardsFile.exists()) {
            try {
                CommentedConfigurationNode graveyardsData = loader.load();

                Map<UUID, Map<String, Graveyard>> graveyardsMap = graveyardsData
                        .getValue(new TypeToken<Map<UUID, Map<String, Graveyard>>>() {
                        });

                return new GraveyardsManager(graveyardsMap);
            } catch (IOException e) {
                logger.warn("Error loading graveyard data! Error:" + e.getMessage());
            } catch (ObjectMappingException e) {
                logger.warn("Error mapping graveayrd data! Error:" + e.getMessage());
            }
        }
        return new GraveyardsManager(new HashMap<>());
    }


    public void saveGraveyards() {
        File graveyardsFile = new File(plugin.getDefaultConfigDir().toFile(), "graveyardsData.config");
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(graveyardsFile).build();

        try {
            CommentedConfigurationNode graveyardsData = loader.load();

            graveyardsData.setValue(new TypeToken<Map<UUID, Map<String, Graveyard>>>() {
                                    },
                    plugin.getGraveyardsManager().getGraveyardMap());

            loader.save(graveyardsData);
        } catch (IOException | ObjectMappingException e) {
            logger.warn("Error saving graveyard data! Error:" + e.getMessage());
        }
    }

    public void loadConfigValues() {

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

        } catch (IOException e) {
            logger.warn("Error loading graveyard config! Error:" + e.getMessage());
        } catch (ObjectMappingException e) {
            logger.warn("Error mapping graveyard config! Error:" + e.getMessage());
        }
    }
}
