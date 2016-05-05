package io.github.zerthick.graveyards.utils.config.serializers;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import io.github.zerthick.graveyards.graveyard.Graveyard;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class GraveyardSerializer implements TypeSerializer<Graveyard> {

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
