package com.markyhzhang.mbot;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yi Han (Mark) Zhang
 * @since 2017-02-05.
 */
public class SessionCleaner implements Runnable{

    private static final double SESSION_TIMEOUT = 1.08*Math.pow(10,7);//3 hours

    @Override
    public void run() {
        ConcurrentHashMap<String, Conversation> conversations = MBot.sessionModels;
        for (String id : conversations.keySet()) {
            Conversation conversation = conversations.get(id);
            if (System.currentTimeMillis()-conversation.getSession().lastAccessedTime()>SESSION_TIMEOUT){
                conversations.remove(id);
            }
        }
    }
}
