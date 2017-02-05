package com.markyhzhang.mbot.bots;

/**
 * Created by Mark on 2017-02-03.
 */
public class BotFactory {

    public static CleverBotIO getCleverBotIO(){
        return new CleverBotIO();
    }

    public static CleverBotCOM getCleverBotCOM(){
        return new CleverBotCOM();
    }

}
