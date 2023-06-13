package facades;

import entities.Festival;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class FestivalFacade {
    private static FestivalFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private FestivalFacade() {
    }

    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static FestivalFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FestivalFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Festival create(Festival festival) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(festival);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return festival;
    }

    public Festival update(Festival festival) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            em.merge(festival);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return festival;
    }

}
