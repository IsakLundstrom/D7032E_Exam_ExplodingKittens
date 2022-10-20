package main.explodingkittens.io.message;

import main.explodingkittens.io.option.Options;

public class MessageFactory {

    public static IMessage createMsg(String msg, Options options, boolean isTimed){
        if (options == null) {
            return new InformationMessage(msg);
        }
        return new OptionsMessage(msg, options, isTimed);
    }

    public static IMessage createMsg(String msg, Options options){
        return createMsg(msg, options, false);
    }

    public static IMessage createMsg(String msg){
        return createMsg(msg, null, false);
    }
}
