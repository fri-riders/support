package com.fri.rso.fririders.support.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class Helpers {

    /**
     * @param message Error message
     * @return Json formatted string
     */
    public static String buildErrorJson(String message) {
        return "{ " +
                "\"error\": true," +
                "\"message\": \"" + message + "\"" +
                " }";
    }

    /**
     * @param jsonString String to parse
     * @return HashMap in JSON form
     */
    public static HashMap<String, String> jsonToMap(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(jsonString, new TypeReference<HashMap<String, String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param message Message to send
     * @return JSON object with message
     */
    public static String buildMessageJson(String message) {
        return "{ " +
                "\"message\": \"" + message + "\"" +
                " }";
    }


    /**
     * @param recipient
     * @param subject
     * @param message
     * @return JSON object with message
     */
    public static String buildNotificationsJson(String recipient, String subject, String message) {
        return "{ " +
                    "\"recipient\": \"" + recipient + "\"," +
                    "\"subject\": \"" + subject + "\"," +
                    "\"message\": \"" + message + "\"" +
                " }";
    }

}
