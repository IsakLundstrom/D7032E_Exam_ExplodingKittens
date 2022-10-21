package main.explodingkittens.util.message;

import main.explodingkittens.io.option.Options;

import java.io.Serializable;

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
        if (!(o instanceof InformationMessage)) {
            return false;
        }

        return this.equals((IMessage) o);
    }
}
