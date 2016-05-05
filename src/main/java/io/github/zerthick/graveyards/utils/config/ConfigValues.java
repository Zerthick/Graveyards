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
