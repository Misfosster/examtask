package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dtos.ShowDTO;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

@Path("show")
public class ShowResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final ShowFacade showFacade = ShowFacade.getFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    public String getAllShows() {
        return GSON.toJson(showFacade.getAvailableShows());
    }

    @GET
    @Path("/{guestname}")
    public String getShowsByGuest(@PathParam("guestname") String guestname) {
        EntityManager em = EMF.createEntityManager();
        TypedQuery<Guest> query = em.createQuery("SELECT g FROM Guest g WHERE g.user.userName = :guestname", Guest.class).setParameter("guestname", guestname);
        Guest guest = query.getSingleResult();
        return GSON.toJson(showFacade.getShowsByGuest(guest));
    }

    @POST
    @Path("/create")
    @RolesAllowed("admin")
    @Consumes("application/json")
    @Produces("application/json")
    public Response createShow(String jsonShow) {
        Show show = GSON.fromJson(jsonShow, Show.class);


        showFacade.create(show);
        ShowDTO showDTO = new ShowDTO(show);

        return Response.ok().entity(GSON.toJson(showDTO)).build();
    }

    @PUT
    @Path("/update")
    @RolesAllowed("admin")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateShow(String jsonShow) {
        Show show = GSON.fromJson(jsonShow, Show.class);

        showFacade.update(show);
        ShowDTO showDTO = new ShowDTO(show);

        return Response.ok().entity(GSON.toJson(showDTO)).build();
    }


    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("admin")
    @Consumes("application/json")
    @Produces("application/json")
    public Response deleteShow(@PathParam("id") Long id) {
        EntityManager em = EMF.createEntityManager();
        TypedQuery<Show> query = em.createQuery("SELECT s FROM Show s WHERE s.id = :id", Show.class).setParameter("id", id);
        Show show = query.getSingleResult();

        showFacade.delete(show);

        return Response.ok().build();
    }
}
