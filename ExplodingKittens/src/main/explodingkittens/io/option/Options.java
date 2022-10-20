package main.explodingkittens.io.option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Options implements Serializable {
    List<Option> options;

    public Options(String options) {
        this.options = new ArrayList<>();
        String[] tmp = options.split("\n");
        for (String s : tmp) {
            String[] opt = s.split(":");
            this.options.add(new Option(opt[0], opt[1]));
        }
    }

    public Options(List<Option> options) {
        this.options = options;
    }

    public boolean isKeyInOptions(String inputKey) {
        for (Option o : options) {
            if (o.getKey().equals(inputKey)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String s = "";
        for (Option o : options) {
            s += o.toString() + "\n";
        }
        return s.length() > 0 ? s.substring(0, s.length()-1) : s;
    }
}
