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

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import io.github.zerthick.graveyards.graveyard.Graveyard;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class GraveyardSerializer implements TypeSerializer<Graveyard> {

    public static void register() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Graveyard.class), new GraveyardSerializer());
    }

    @Override
    public Graveyard deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        String name = value.getNode("name").getString();

        int locationX = value.getNode("location", "x").getInt();
        int locationY = value.getNode("location", "y").getInt();
        int locationZ = value.getNode("location", "z").getInt();
        Vector3i location = new Vector3i(locationX, locationY, locationZ);

        double rotationX = value.getNode("rotation", "x").getDouble();
        double rotationY = value.getNode("rotation", "y").getDouble();
        double rotationZ = value.getNode("rotation", "z").getDouble();
        Vector3d rotation = new Vector3d(rotationX, rotationY, rotationZ);

        Text message = TextSerializers.FORMATTING_CODE.deserialize(value.getNode("message").getString());

        int distance = value.getNode("discoverDistance").getInt();

        return new Graveyard(name, location, rotation, message, distance);
    }

    @Override
    public void serialize(TypeToken<?> type, Graveyard obj, ConfigurationNode value) throws ObjectMappingException {

        value.getNode("name").setValue(TypeToken.of(String.class), obj.getName());

        value.getNode("location", "x").setValue(TypeToken.of(Integer.class), obj.getLocation().getX());
        value.getNode("location", "y").setValue(TypeToken.of(Integer.class), obj.getLocation().getY());
        value.getNode("location", "z").setValue(TypeToken.of(Integer.class), obj.getLocation().getZ());

        value.getNode("rotation", "x").setValue(TypeToken.of(Double.class), obj.getRotation().getX());
        value.getNode("rotation", "y").setValue(TypeToken.of(Double.class), obj.getRotation().getY());
        value.getNode("rotation", "z").setValue(TypeToken.of(Double.class), obj.getRotation().getZ());

        value.getNode("message").setValue(TypeToken.of(String.class), TextSerializers.FORMATTING_CODE.serialize(obj.getMessage()));

        value.getNode("discoverDistance").setValue(TypeToken.of(Integer.class), obj.getDiscoverDistance());

    }
}
