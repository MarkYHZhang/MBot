package com.markyhzhang.mbot.commands;

import com.markyhzhang.mbot.Conversation;

/**
 * Created by Mark on 2017-02-04.
 */
public interface Command {

    boolean process(Conversation conversation, String msg);

    String help();

}
