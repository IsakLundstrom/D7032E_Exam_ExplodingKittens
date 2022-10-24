package main.explodingkittens.util.option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Options implements Serializable {
    List<Option> options;

    /**
     * Create options from a list of options
     *
     * @param options a list of options
     */
    public Options(List<Option> options) {
        this.options = options;
    }

    /**
     * Special case to create options from string
     *
     * @param options the string of options
     */
    public Options(String options) {
        this.options = new ArrayList<>();
        String[] tmp = options.split("\n");
        for (String s : tmp) {
            String[] opt = s.split(":");
            this.options.add(new Option(opt[0], opt[1]));
        }
    }

    /**
     * Is the key in any of the options
     *
     * @param inputKey the input key
     * @return true if key is in any of the options, otherwise false
     */
    public boolean isKeyInOptions(String inputKey) {
        for (Option o : options) {
            if (o instanceof RangeOption) {
                String[] list = inputKey.split(" ");
                if (list.length == 3) {
                    if (o.getKey().equals(list[0] + " " + list[1]) && ((RangeOption) o).getValues().contains(Integer.parseInt(list[2]))) {
                        return true;
                    }
                }
                if (list.length == 4) {
                    if (o.getKey().equals(list[0] + " " + list[1]) && ((RangeOption) o).getValues().contains(Integer.parseInt(list[2]))) {
                        return true;
                    }
                }
            }
            if (o.getKey().equals(inputKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the list of options
     *
     * @return the list
     */
    public List<Option> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        String s = "";
        for (Option o : options) {
            s += o.toString() + "\n";
        }
        return s.length() > 0 ? s.substring(0, s.length() - 1) : s;
    }
}
