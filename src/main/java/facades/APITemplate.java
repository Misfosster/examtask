package facades;

import org.asynchttpclient.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;

public class APITemplate {
    public static void runMultiKeyAPI() throws Exception {

        AsyncHttpClient client = new DefaultAsyncHttpClient();
        //YOUR API URL
        String url = "YOUR API URL";
        Request request = new RequestBuilder()
                .setMethod("GET")
                .setUrl(url)
                //Your headerkeys from RapidAPI
                .addHeader("X-RapidAPI-Key", "YOUR VALUE")
                .addHeader("X-RapidAPI-Host", "YOUR VALUE")
                .build();

        CompletableFuture<Response> responseFuture = client.executeRequest(request)
                .toCompletableFuture();

        responseFuture.thenAccept(response ->

        {
            try {

                JSONObject jsonResponse = new JSONObject(response.getResponseBody());
                JSONArray jokesArray = jsonResponse.getJSONArray("body");
                JSONObject joke = jokesArray.getJSONObject(0);
                // body parameter names
                String parameter1 = joke.getString("parameter1");
                String parameter2 = joke.getString("parameter2");
                System.out.println("Parameter 1: " + parameter1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        responseFuture.join();
        client.close();
    }
    public static void runSingleKeyAPI() throws Exception {

        AsyncHttpClient client = new DefaultAsyncHttpClient();
        //YOUR API URL
        String url = "YOUR API URL";
        Request request = new RequestBuilder()
                .setMethod("GET")
                .setUrl(url)
                //Your headerkeys from RapidAPI
                .addHeader("X-RapidAPI-Key", "YOUR VALUE")
                .addHeader("X-RapidAPI-Host", "YOUR VALUE")
                .build();

        CompletableFuture<Response> responseFuture = client.executeRequest(request)
                .toCompletableFuture();

        responseFuture.thenAccept(response ->

        {
            try {

                JSONObject jsonResponse = new JSONObject(response.getResponseBody());
                // body parameter names
                String parameter = jsonResponse.getString("KEY NAME");
                System.out.println("Parameter: " + parameter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        responseFuture.join();
        client.close();
    }


}

