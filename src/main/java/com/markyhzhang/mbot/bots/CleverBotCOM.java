package com.markyhzhang.mbot.bots;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;

/**
 * Created by Mark on 2017-02-03.
 */
public class CleverBotCOM {

    public String respond(String msg, String cs)throws Exception{

        GetRequest responseObj = Unirest.get("http://www.cleverbot.com/getreply?key=57e22f130622ed3d99709f915b8c1b29&input="+msg.trim().replace(" ","%20")+"&cs="+cs);

        JsonObject jsonObject = new JsonParser().parse(responseObj.asString().getBody()).getAsJsonObject();
        return jsonObject.get("output").getAsString()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+jsonObject.get("cs").getAsString();

    }

}
