package com.markyhzhang.mbot.commands.cmds;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.markyhzhang.mbot.Conversation;
import com.markyhzhang.mbot.commands.Command;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

/**
 * This class processes the weather command
 * @author Yi Han (Mark) Zhang
 * @since 2017-02-04.
 */

public class WeatherCMD implements Command{

    @Override
    public boolean process(Conversation conversation, String msg) {
        //http://freegeoip.net/json/
        //https://api.darksky.net/forecast/27bbff18a82b3ba7ccb013add299222e/43.8669,-79.4414?exclude=[minutely,hourly,flags]&units=auto
        //http://api.openweathermap.org/data/2.5/weather?lat=43.8669&lon=-79.4414&appid=95f11743b6e14e0108db85d5a38b743a&units=metric
        if (msg.equals("weather")){
            GetRequest geoLocReq = Unirest.get("http://freegeoip.net/json/"+conversation.getIp());
            JsonObject geoLocJsonObj = null;
            try{
                geoLocJsonObj = new JsonParser().parse(geoLocReq.asString().getBody()).getAsJsonObject();
            }catch (Exception e){
                conversation.newMsg(false, "I can't find any weather information from your IP.");
                return true;
            }
            String city = geoLocJsonObj.get("city").getAsString();
            double latitude = geoLocJsonObj.get("latitude").getAsDouble();
            double longitude = geoLocJsonObj.get("longitude").getAsDouble();

            GetRequest weatherReq = Unirest.get("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=95f11743b6e14e0108db85d5a38b743a&units=metric");
            JsonObject weatherJsonObj;
            try {
                weatherJsonObj = new JsonParser().parse(weatherReq.asString().getBody()).getAsJsonObject();
            } catch (UnirestException e) {
                e.printStackTrace();
                conversation.newMsg(false, "Woop, some error occurred");
                return true;
            }
            String desc = weatherJsonObj.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
            int curTmp = Math.round(weatherJsonObj.getAsJsonObject("main").get("temp").getAsFloat());
//            int maxTmp = weatherJsonObj.getAsJsonObject("main").get("temp_max").getAsInt();
//            int minTmp = weatherJsonObj.getAsJsonObject("main").get("temp_min").getAsInt();

            conversation.newMsg(false, "It is currently " + curTmp + " degrees in " + city + " with " + desc + ".");
            return true;
        }
        return false;
    }

    @Override
    public String help() {
        return "/weather - shows weather forecast geo located by your IP";
    }

}
