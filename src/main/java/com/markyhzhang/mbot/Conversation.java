package com.markyhzhang.mbot;

import com.markyhzhang.mbot.bots.BotFactory;
import com.markyhzhang.mbot.commands.CommandManager;
import spark.Session;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Mark on 2017-02-01.
 */
public class Conversation {

    private String uuid = "";
    private String cs = "";
    private String ip = "";
    private Session session;

    private ArrayList<String> conversationList;

    Conversation(Session session)throws Exception{
        conversationList = new ArrayList<String>(){{add("MBot: Hello there!");}};
        uuid = UUID.randomUUID().toString();
        this.session = session;

        BotFactory.getCleverBotIO().initialize(uuid);
    }

    public Session getSession() {
        return session;
    }

    public ArrayList<String> getConversationList(){
        return conversationList;
    }

    void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    void msgAI(String msg) throws Exception{
        newMsg(true, msg);

        if (CommandManager.process(this, msg))return;


        String response = BotFactory.getCleverBotIO().respond(msg, uuid);

//        String[] com = BotFactory.getCleverBotCOM().respond(msg,cs).split("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        response = com[0];
//        cs = com[1];

        newMsg(false, response);
    }

    public void newMsg(boolean isHuman, String msg){
        if (isHuman) conversationList.add("You: " + msg);
        else conversationList.add("MBot: " + msg);
    }
}
