package com.tech.springbootlibraryservice.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJwt {

    public static String payLoadJwtExtraction(String token, String extractKey) {

        token.replace("Bearer", "");
        String[] tokens = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payLoad = new String(decoder.decode(tokens[1]));
        String[] entries = payLoad.split(",");
        String userName = "";
        Map<String, Object> extractMap = new HashMap<>();
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            if (keyValue[0].equals(extractKey)) {
                int remove = 0;
                if (keyValue[1].endsWith("}")) {
                    keyValue[1] = keyValue[1].replace("}", "");

                }
                keyValue[1] = keyValue[1].replace("\"", "");
                extractMap.put(keyValue[0], keyValue[1]);


            }


        }
        if (extractMap.containsKey(extractKey)) {
            System.out.println("userName extracted from Jwt token " + (String) extractMap.get(extractKey));
            return (String) extractMap.get(extractKey);
        }


        return null;
    }
}
