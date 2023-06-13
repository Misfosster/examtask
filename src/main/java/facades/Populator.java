/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

//import com.nimbusds.jose.shaded.json.JSONObject;
import dtos.RenameMeDTO;
import entities.RenameMe;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        FacadeExample fe = FacadeExample.getFacadeExample(emf);
        fe.create(new RenameMeDTO(new RenameMe("First 1", "Last 1")));
        fe.create(new RenameMeDTO(new RenameMe("First 2", "Last 2")));
        fe.create(new RenameMeDTO(new RenameMe("First 3", "Last 3")));
    }

    //public static void main(String[] args) {
    //  populate();
    //}


    /*public static void main(String[] args) throws Exception {


        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String url = "https://dad-jokes.p.rapidapi.com/random/joke";
        Request request = new RequestBuilder()
                .setMethod("GET")
                .setUrl(url)
                .addHeader("X-RapidAPI-Key", "bc6e2b57aamsh6d60c456fbf211ep17449djsn51cd9deb5e05")
                .addHeader("X-RapidAPI-Host", "dad-jokes.p.rapidapi.com")
                .build();

        CompletableFuture<Response> responseFuture = client.executeRequest(request)
                .toCompletableFuture();

        Response response = responseFuture.get();
        System.out.println(response.getStatusCode());
        System.out.println(response.getResponseBody());
        String punchline = response.getResponseBody().;
        System.out.println("XPC"+punchline);
        client.close();
    }
}*/
    public static void main(String[] args) {
        populate();
    }
}
