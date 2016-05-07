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

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class ConfigValues {

    private static ConfigValues instance = null;
    private String defaultGraveyardMessage;

    protected ConfigValues() {
        // Singleton Design Pattern
    }

    public static ConfigValues getInstance() {
        if (instance == null) {
            instance = new ConfigValues();
        }
        return instance;
    }

    public Text getDefaultGraveyardMessage(String graveyardName) {
        return TextSerializers.FORMATTING_CODE.deserialize(defaultGraveyardMessage.replaceAll("\\{GRAVEYARD_NAME\\}", graveyardName));
    }

    public void setDefaultGraveyardMessage(String defaultGraveyardMessage) {
        this.defaultGraveyardMessage = defaultGraveyardMessage;
    }
}
