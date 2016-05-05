package io.github.zerthick.graveyards.utils.config;

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

    public String getDefaultGraveyardMessage() {
        return defaultGraveyardMessage;
    }

    public void setDefaultGraveyardMessage(String defaultGraveyardMessage) {
        this.defaultGraveyardMessage = defaultGraveyardMessage;
    }
}
