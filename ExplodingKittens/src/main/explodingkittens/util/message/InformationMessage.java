package main.explodingkittens.util.message;

import main.explodingkittens.util.option.Options;

import java.io.Serializable;
import java.util.Objects;

/**
 * An information message without options
 */
public class InformationMessage implements IMessage, Serializable {

    private final String msg;
    private final Options options;

    /**
     * Create an information message
     *
     * @param msg the message string
     */
    InformationMessage(String msg) {
        this.msg = msg;
        options = null;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public Options getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationMessage that = (InformationMessage) o;
        return Objects.equals(msg, that.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(msg, options);
    }
}
