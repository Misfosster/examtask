package facades;

import dtos.RenameMeDTO;
import entities.Guest;
import entities.RenameMe;
import entities.Show;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class ShowFacade {

    private static ShowFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private ShowFacade() {
    }

    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static ShowFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ShowFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Show create(Show show) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Show> query = em.createQuery("SELECT s FROM Show s WHERE s.name = :name", Show.class).setParameter("name", show.getName());
            Show existingShow = null;
            try {
                existingShow = query.getSingleResult();
                show = existingShow;
                System.out.println("Show already exists");
            } catch (Exception ex) {
                System.out.println("Creating show");
            }
            em.persist(show);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return show;
    }

    public Show update(Show show) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            em.merge(show);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return show;
    }

    public Boolean delete(Show show) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            try {
                show = em.find(Show.class, show.getId());
                em.remove(show);
            } catch (Exception e) {
                System.out.println("Show with name: " + show.getName() + " does not exist");
                return false;
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return true;
    }

    public List<Guest> getGuests(Show show) {
        EntityManager em = getEntityManager();
        List<Guest> guestList = null;
        try {
            show = em.find(Show.class, show.getId());
            guestList = show.getGuests();
        } catch (Exception e) {
        }
        return guestList;
    }
}