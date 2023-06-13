package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.FestivalDTO;
import dtos.ShowDTO;
import entities.Festival;
import entities.Guest;
import entities.Show;
import facades.FestivalFacade;
import facades.GuestFacade;
import facades.ShowFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("festival")
public class FestivalResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final FestivalFacade festivalFacade = FestivalFacade.getFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    @POST
    @Path("/create")
    @RolesAllowed("admin")
    @Consumes("application/json")
    @Produces("application/json")
    public Response createFestival(String jsonFestival) {
        Festival festival = GSON.fromJson(jsonFestival, Festival.class);

        festivalFacade.create(festival);

        FestivalDTO festivalDTO = new FestivalDTO(festival);

        return Response.ok().entity(GSON.toJson(festivalDTO)).build();
    }

    @PUT
    @Path("/update")
    @RolesAllowed("admin")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateFestival(String jsonFestival) {
        Festival festival = GSON.fromJson(jsonFestival, Festival.class);

        festivalFacade.update(festival);

        FestivalDTO festivalDTO = new FestivalDTO(festival);

        return Response.ok().entity(GSON.toJson(festivalDTO)).build();
    }


}
