package main.explodingkittens.util.message;

import main.explodingkittens.io.option.Options;

import java.io.Serializable;

/**
 * An options message
 */
public class OptionsMessage implements IMessage, Serializable {

    private final String msg;
    private final Options options;
    private final boolean timed;

    /**
     * Create an options message with a message string, options that is also timed or not
     *
     * @param msg     the message
     * @param options the options
     * @param timed   timed or not
     */
    OptionsMessage(String msg, Options options, boolean timed) {
        this.msg = msg;
        this.options = options;
        this.timed = timed;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public Options getOptions() {
        return options;
    }

    /**
     * If the message is timed or not
     *
     * @return if timed
     */
    public boolean isTimed() {
        return timed;
    }

    @Override
    public String toString() {
        return msg + "\n" + options.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OptionsMessage)) {
            return false;
        }

        return this.equals((IMessage) o) && this.isTimed() == ((OptionsMessage) o).isTimed();
    }
}
