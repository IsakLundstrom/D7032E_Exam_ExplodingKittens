package main.explodingkittens.util.message;

import main.explodingkittens.io.option.Options;

import java.io.Serializable;

/**
 * A message interface
 */
public interface IMessage extends Serializable {

    /**
     * Get the message string of the message
     *
     * @return the message
     */
    String getMessage();

    /**
     * Get the options of the message
     *
     * @return the options
     */
    Options getOptions();

    default boolean equals(IMessage other) {
        return other != null
                && this.getMessage().equals(other.getMessage())
                && this.getOptions().equals(other.getOptions());
    }
}
