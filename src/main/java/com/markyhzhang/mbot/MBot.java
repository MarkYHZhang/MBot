package com.markyhzhang.mbot;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;


public class MBot {

    static ConcurrentHashMap<String, Conversation> sessionModels = new ConcurrentHashMap<>();

    public static void main(String[] args){

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new SessionCleaner(), 0, 1, TimeUnit.SECONDS);

        port(8888);

        get("/mbot", (request, response) -> {
            Session session = request.session();

            HashMap<String, Conversation> model = new HashMap<>();
            if (session.isNew()||!sessionModels.containsKey(session.id())) {
                Conversation conversation = new Conversation(session);
                sessionModels.put(session.id(), conversation);
                model.put("conversation", conversation);
            }else{
                model.put("conversation",sessionModels.get(session.id()));
            }

            // The wm files are located under the resources director
            return new ModelAndView(model, "page.vm");
        }, new VelocityTemplateEngine());

        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        get("/mbot/getChat", (request, response) -> {
            Conversation conversation = sessionModels.get(request.session().id());
            sessionModels.put(request.session().id(), conversation);
            HashMap<String, Conversation> model = new HashMap<>();
            model.put("conversation", conversation);
            return new ModelAndView(model, "chat.vm");
        }, new VelocityTemplateEngine());

        post("/mbot/updateChat", (request, response) -> {

            String ip = request.headers("X-Real-IP");
            String input = request.queryParams("input");

            Conversation conversation = sessionModels.get(request.session().id());

            conversation.setIp(ip);
            conversation.msgAI(input);

            sessionModels.put(request.session().id(), conversation);

            HashMap<String, Conversation> model = new HashMap<>();
            model.put("conversation",conversation);
            return new ModelAndView(model, "chat.vm");
        }, new VelocityTemplateEngine());

    }

}