package facades;

import entities.Guest;
import entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

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
                System.out.println("Guest already exists");
            } catch (Exception ex) {
                System.out.println("Creating guest");
            }
            try {
                TypedQuery<Role> query1 = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class).setParameter("roleName", "user");
                Role existingRole = query1.getSingleResult();
                guest.getUser().addRole(existingRole);
            } catch (Exception ex) {
                System.out.println("Creating role");
                guest.getUser().addRole(new Role("user"));
                em.persist(guest);
            }
            em.merge(guest);
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
}
