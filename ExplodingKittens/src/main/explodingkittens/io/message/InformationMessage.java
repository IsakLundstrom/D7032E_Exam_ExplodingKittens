package main.explodingkittens.io.message;

import main.explodingkittens.io.option.Options;

import java.io.Serial;
import java.io.Serializable;

public class InformationMessage implements IMessage, Serializable {

    private final String msg;
    private final Options options;

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

        return this.equals((IMessage)o);
    }
}
