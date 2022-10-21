package main.explodingkittens.io.option;

import java.io.Serializable;

public class Option implements IOption, Serializable {

    private final String key;
    private final String description;

    /**
     * Create an option with a key and description
     *
     * @param key         the key
     * @param description the description
     */
    public Option(String key, String description) {
        this.key = key;
        this.description = description;
    }

    /**
     * Create an option with only a key
     *
     * @param key the key
     */
    public Option(String key) {
        this.key = key;
        this.description = "";
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        if (description.equals("")) {
            return "\t[" + key + "]";
        }
        return "\t[" + key + "] : " + description;
    }
}
