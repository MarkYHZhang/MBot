package com.markyhzhang.mbot.commands.cmds;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.markyhzhang.mbot.Conversation;
import com.markyhzhang.mbot.commands.Command;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;

import java.util.HashMap;

/**
 * Manages the news command
 * @author Yi Han (Mark) Zhang
 * @since 2017-02-04.
 */
public class NewsCMD implements Command{

    private HashMap<String, String> sources = new HashMap<String, String>(){{
        put("bbc", "bbc-news,BBC News");
        put("cnn", "cnn,CNN News");
        put("econ", "the-economist,The Economist");
        put("nyt", "the-new-york-times,New York Times");
        put("verge", "the-verge,The Verge");
        put("wsj", "the-wall-street-journal,The Wall Street Journal");
        put("huff", "the-huffington-post,The Huffington Post");
        put("ap", "associated-press,Associated Press");
    }};

    @Override
    public boolean process(Conversation conversation, String msg) {
        //https://newsapi.org/v1/articles?source=the-econmist&apiKey=00ffb62cd52b44e8b4a3ca286bd115f8
        String[] args = msg.split(" ");
        if (!args[0].equals("news")) return false;
        if (args.length<2){
            conversation.newMsg(false, "Try [/news list] for a list of news sources");
            return true;
        }
        if (args[1].equals("list")){
            conversation.newMsg(false, "News sources:");
            for (String source : sources.keySet()) {
                conversation.newMsg(false, source + " - " +sources.get(source).split(",")[1]);
            }
        }else if (sources.keySet().contains(args[1])){
            GetRequest newsReq = Unirest.get("https://newsapi.org/v1/articles?source="+sources.get(args[1]).split(",")[0]+"&apiKey=00ffb62cd52b44e8b4a3ca286bd115f8");
            JsonObject newsJsonObj;
            try{
                newsJsonObj = new JsonParser().parse(newsReq.asString().getBody()).getAsJsonObject();
            }catch (Exception e){
                conversation.newMsg(false, "Woop, some errors just occurred.");
                return true;
            }

            conversation.newMsg(false, sources.get(args[1]).split(",")[1]+":");
            JsonArray articles = newsJsonObj.getAsJsonArray("articles");
            for (int i = 0; i < 3; i++) {
                if (i<articles.size()){
                    String desc = articles.get(i).getAsJsonObject().get("description").getAsString();
                    conversation.newMsg(false, desc);
                }
            }
        }else{
            conversation.newMsg(false, "Try [/news list] for a list of news sources");
        }
        return true;
    }

    @Override
    public String help() {
        return "/news [source] - Show you news from sources of your liking";
    }
}
