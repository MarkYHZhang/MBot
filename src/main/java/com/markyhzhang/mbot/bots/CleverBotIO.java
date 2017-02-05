package com.markyhzhang.mbot.bots;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Created by Mark on 2017-02-03.
 */
public class CleverBotIO {

    public void initialize(String uuid) throws Exception{
        JsonObject obj = new JsonObject();
        obj.addProperty("user", "maVlSDZ8lQWCF43O");
        obj.addProperty("key", "RvVh6PwJH57NnhYIt258WDhAf5kRTWIs");
        obj.addProperty("nick", uuid);

        String url = "https://cleverbot.io/1.0/create";
        URL urlObj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("content-type", "application/json");

        String urlParameters = obj.toString();

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();


        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

    public String respond(String msg, String uuid)throws Exception{
        JsonObject obj = new JsonObject();
        obj.addProperty("user", "maVlSDZ8lQWCF43O");
        obj.addProperty("key", "RvVh6PwJH57NnhYIt258WDhAf5kRTWIs");
        obj.addProperty("nick",uuid);
        obj.addProperty("text", msg);

        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://cleverbot.io/1.0/ask")
                .header("content-type", "application/json")
                .body(obj.toString())
                .asJson();
        JsonObject jsonObject = new JsonParser().parse(jsonResponse.getBody().toString()).getAsJsonObject();
        return jsonObject.get("response").getAsString();
    }
}
