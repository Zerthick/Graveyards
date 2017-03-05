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

package io.github.zerthick.graveyards.utils.config.serializers;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.graveyards.graveyard.Graveyard;
import io.github.zerthick.graveyards.graveyard.GraveyardGroup;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

import java.util.Map;
import java.util.UUID;

public class GraveyardGroupSerializer implements TypeSerializer<GraveyardGroup> {

    public static void register() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(GraveyardGroup.class), new GraveyardGroupSerializer());
    }

    @Override
    public GraveyardGroup deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String groupName = value.getNode("group").getString();
        Map<UUID, Map<String, Graveyard>> graveyardMap = value.getNode("graveyards").getValue(new TypeToken<Map<UUID, Map<String, Graveyard>>>() {
        });
        return new GraveyardGroup(groupName, graveyardMap);
    }

    @Override
    public void serialize(TypeToken<?> type, GraveyardGroup obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("group").setValue(obj.getGroupName());
        value.getNode("graveyards").setValue(new TypeToken<Map<UUID, Map<String, Graveyard>>>() {
        }, obj.getGraveyardMap());
    }
}
