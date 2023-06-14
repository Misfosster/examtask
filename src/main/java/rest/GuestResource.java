package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import entities.Guest;
import entities.Show;
import entities.User;
import facades.GuestFacade;
import facades.ShowFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("guest")
public class GuestResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final GuestFacade guestFacade = GuestFacade.getFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    @POST
    @Path("/create")
    @RolesAllowed("admin")
    @Consumes("application/json")
    @Produces("application/json")
    public Response createGuest(String jsonGuest) {
        JsonObject jsonObject = GSON.fromJson(jsonGuest, JsonObject.class);
        User user = new User(jsonObject.getAsJsonObject("user").get("user_name").getAsString(), jsonObject.getAsJsonObject("user").get("user_password").getAsString());
        String phone = jsonObject.get("phone").getAsString();
        String email = jsonObject.get("email").getAsString();
        String status = jsonObject.get("status").getAsString();
        Guest guest = new Guest(user, phone, email, status);

        guestFacade.create(guest);

        return Response.ok().build();
    }

    @PUT
    @Path("/signup/{username}")
    @RolesAllowed("user")
    @Consumes("application/json")
    @Produces("application/json")
    public Response signUp(@PathParam("username") String username, String jsonShow)
    {
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();
        Show show = GSON.fromJson(jsonShow, Show.class);
        TypedQuery<Show> query = em.createQuery("SELECT s FROM Show s WHERE s.name = :name", Show.class).setParameter("name", show.getName());
        show = query.getSingleResult();
        TypedQuery<Guest> query1 = em.createQuery("SELECT g FROM Guest g WHERE g.user.userName = :username", Guest.class).setParameter("username", username);
        Guest guest = query1.getSingleResult();
        System.out.println(show);
        guest.addShow(show);
        System.out.println(guest);
        guestFacade.update(guest);
        em.getTransaction().commit();

        return Response.ok().build();
    }

    @PUT
    @Path("/update")
    @RolesAllowed("admin")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateGuest(String jsonGuest) {
        Guest guest = GSON.fromJson(jsonGuest, Guest.class);

        guestFacade.update(guest);

        return Response.ok().entity(GSON.toJson(guest)).build();
    }

}
