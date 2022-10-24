package main.explodingkittens.util.option;

/**
 * An interface for creating an option
 */
public interface IOption {

    /**
     * Get the key of an option
     *
     * @return the key
     */
    String getKey();

    /**
     * Get the possible description
     *
     * @return the description
     */
    String getDescription();

}
