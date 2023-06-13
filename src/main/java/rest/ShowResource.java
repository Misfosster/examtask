package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Guest;
import facades.GuestFacade;
import facades.ShowFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("show")
public class ShowResource {
        private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
        private static final ShowFacade showFacade =  ShowFacade.getFacade(EMF);
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
}
