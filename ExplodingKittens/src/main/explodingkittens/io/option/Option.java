package main.explodingkittens.io.option;

import java.io.Serializable;

public class Option implements Serializable {

    private final String key;
    private final String description;

    public Option(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public Option(String key) {
        this.key = key;
        this.description = "";
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        if (description == "") {
            return "\t[" + key + "]";
        }
        return "\t[" + key + "] : " + description;
    }
}
