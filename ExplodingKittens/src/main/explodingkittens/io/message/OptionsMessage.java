package main.explodingkittens.io.message;

import main.explodingkittens.io.option.Options;

import java.io.Serial;
import java.io.Serializable;

public class OptionsMessage implements IMessage, Serializable {

    private final String msg;
    private final Options options;
    private final boolean timed;

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

        return this.equals((IMessage)o) && this.isTimed() == ((OptionsMessage) o).isTimed();
    }
}
