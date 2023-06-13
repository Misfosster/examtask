package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Guest;
import entities.Show;
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
        Guest guest = GSON.fromJson(jsonGuest, Guest.class);

        guestFacade.create(guest);

        return Response.ok().entity(GSON.toJson(guest)).build();
    }

    @PUT
    @Path("/signup/{username}")
    @RolesAllowed("user")
    @Consumes("application/json")
    @Produces("application/json")
    public Response signUp(@PathParam("username") String username, String jsonShow)
    {
        EntityManager em = EMF.createEntityManager();
        Show show = GSON.fromJson(jsonShow, Show.class);
        TypedQuery<Guest> query = em.createQuery("SELECT g FROM Guest g WHERE g.user.userName = :username", Guest.class).setParameter("username", username);
        Guest guest = query.getSingleResult();
        guest.addShow(show);
        guestFacade.update(guest);

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
