package main.explodingkittens.io.message;

import main.explodingkittens.io.option.Options;

import java.io.Serial;
import java.io.Serializable;

public interface IMessage extends Serializable {

    String getMessage();

    Options getOptions();

    default boolean equals(IMessage other) {
        return other != null
                && this.getMessage().equals(other.getMessage())
                && this.getOptions().equals(other.getOptions());
    }
}
