package main.explodingkittens.util.option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RangeOption extends Option implements Serializable {

    private final String key;
    private final String description;
    private final String valueName;
    private List<Integer> values;

    /**
     * Create a RangeOption
     * A special variant of Option, also takes a list of integers for a possibility of look up "key + {any integer in values}"
     *
     * @param key         the key
     * @param description the description
     * @param valueName   the value name
     * @param values      the values
     */
    public RangeOption(String key, String description, String valueName, List<Integer> values) {
        super(key, description);
        this.key = key;
        this.description = description;
        this.valueName = valueName;
        this.values = values;
    }

    /**
     * Create a RangeOption
     * A special variant of Option, also takes a list of integers for a possibility of look up "key + {any integer in values}"
     *
     * @param key       the key
     * @param valueName the value name
     * @param values    the values
     */
    public RangeOption(String key, String valueName, List<Integer> values) {
        this(key, "", valueName, values);
    }

    /**
     * Create a RangeOption
     * A special variant of Option, also takes a min and max of integers for a possibility of look up "key + {any integer in range min to max}"
     *
     * @param key         the key
     * @param description the description
     * @param valueName   the value name
     * @param min         the min value
     * @param max         the max value
     */
    public RangeOption(String key, String description, String valueName, int min, int max) {
        this(key, description, valueName, null);
        List<Integer> values = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            values.add(i);
        }
        this.values = values;
    }

    /**
     * Create a RangeOption
     * A special variant of Option, also takes a min and max of integers for a possibility of look up "key + {any integer in range min to max}"
     *
     * @param key       the key
     * @param valueName the value name
     * @param min       the min value
     * @param max       the max value
     */
    public RangeOption(String key, String valueName, int min, int max) {
        this(key, "", valueName, min, max);
    }

    /**
     * Get the list of possible values that can be used with the key
     *
     * @return the list
     */
    public List<Integer> getValues() {
        return values;
    }

    @Override
    public String toString() {
        if (description.equals("")) {
            return "\t[" + key + " {" + valueName + "}]";
        }
        return "\t[" + key + " {" + valueName + "}] : " + description;
    }
}
