package main.explodingkittens.util.message;

import main.explodingkittens.io.option.Options;

/**
 * A message factory for creating different messages
 */
public class MessageFactory {

    /**
     * Create a message with a message, options and if its timed or not
     *
     * @param msg     the message
     * @param options the options
     * @param isTimed timed or not
     * @return a new message
     */
    public static IMessage createMsg(String msg, Options options, boolean isTimed) {
        if (options == null) {
            return new InformationMessage(msg);
        }
        return new OptionsMessage(msg, options, isTimed);
    }

    /**
     * Create a message with a message and options
     *
     * @param msg     the message
     * @param options the options
     * @return a new message
     */
    public static IMessage createMsg(String msg, Options options) {
        return createMsg(msg, options, false);
    }

    /**
     * Create a message with a message
     *
     * @param msg the message
     * @return a new message
     */
    public static IMessage createMsg(String msg) {
        return createMsg(msg, null, false);
    }
}
