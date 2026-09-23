package com.alwartours3.test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

public class TestAPI {
    public static void main(String[] args) {
        String json = getStringByPageNumber(1);

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        System.out.println(json);
        Object o = null;
        try {
            o = engine.eval(String.format("JSON.parse('%s')", json));
            Map<String, String> map = (Map<String, String>) o;
            System.out.println(Arrays.toString(map.entrySet().toArray()));
            for (Map.Entry<String, String>  entry: map.entrySet() ) {
                System.out.println(entry);
                if(entry.getKey().equals("data")){
                    //System.out.println(entry.getValue());
                    Object o2 = engine.eval(String.format("JSON.parse('%s')", entry.getValue()));
                    Map<String, String> map2 = (Map<String, String>) o2;
                    System.out.println(Arrays.toString(map2.entrySet().toArray()));
                }
            }

        } catch (ScriptException e) {
            e.printStackTrace();
        }

    }

    public static String getStringByPageNumber(int pageNumber) {
        String result = null;
        //https://jsonmock.hackerrank.com/api/article_users?page=<pageNumber>
        try {
            String urlStr = "https://jsonmock.hackerrank.com/api/article_users?page=" + pageNumber;
            //URL url = new URL("http://localhost:8080/RESTfulExample/json/product/get");
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                System.out.println("#");
                result = output;
            }

            conn.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
