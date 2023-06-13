package facades;

import entities.Guest;
import entities.Show;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class GuestFacade {
    private static GuestFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private GuestFacade() {
    }

    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static GuestFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuestFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Guest create(Guest guest) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Guest> query = em.createQuery("SELECT g FROM Guest g WHERE g.user = :user", Guest.class).setParameter("user", guest.getUser());
            Guest existingGuest = null;
            try {
                existingGuest = query.getSingleResult();
                guest = existingGuest;
                System.out.println("Show already exists");
            } catch (Exception ex) {
                System.out.println("Creating show");
            }
            em.persist(guest);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return guest;
    }

    public Guest update(Guest guest) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            em.merge(guest);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return guest;
    }


    public List<Show> getShows(Guest guest) {
        EntityManager em = getEntityManager();
        List<Show> showList = null;
        try {
            guest = em.find(Guest.class, guest.getId());
            showList = guest.getShows();
        } catch (Exception e) {
        }
        return showList;
    }

    public List<Show> getAvailableShows() {
        EntityManager em = getEntityManager();
        List<Show> showList = null;
        try {
            TypedQuery<Show> query = em.createQuery("SELECT s FROM Show s", Show.class);
            showList = query.getResultList();
        } catch (Exception e) {
        }
        return showList;
    }
}