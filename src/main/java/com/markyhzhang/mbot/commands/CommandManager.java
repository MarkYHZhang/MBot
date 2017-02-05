package com.markyhzhang.mbot.commands;

import com.markyhzhang.mbot.Conversation;
import com.markyhzhang.mbot.commands.cmds.NewsCMD;
import com.markyhzhang.mbot.commands.cmds.WeatherCMD;

/**
 * Created by Mark on 2017-02-04.
 */
public class CommandManager {

    private static Command[] commands = {
            new WeatherCMD(),
            new NewsCMD()
    };

    public static boolean process(Conversation conversation, String msg){
        if (!msg.startsWith("/")) return false;
        msg = msg.substring(1).trim().toLowerCase();
        if (msg.equals("help")){
            help(conversation);
            return true;
        }
        for (Command command : commands) {
            if (command.process(conversation,msg)) return true;
        }
        conversation.newMsg(false, "Unknown command, try /help");
        return true;
    }

    private static void help(Conversation conversation){
        conversation.newMsg(false, "Commands that you can try:");
        for (Command command : commands) {
            conversation.newMsg(false, command.help());
        }
    }

}
