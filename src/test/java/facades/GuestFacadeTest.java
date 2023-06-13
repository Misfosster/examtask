package facades;

import entities.Guest;
import entities.Show;
import entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

class GuestFacadeTest {
    private static EntityManagerFactory emf;
    private static GuestFacade facade;
    private static EntityManager em;

    public GuestFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = GuestFacade.getFacade(emf);
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Show show = new Show();
            show.setName("Test show");
            show.setDuration(120);
            show.setStartDate(new Date(System.currentTimeMillis()).toString());
            show.setStartTime(new Time(System.currentTimeMillis()).toString());

            Show show2 = new Show();
            show2.setName("Test show2");
            show2.setDuration(120);
            show2.setStartDate(new Date(System.currentTimeMillis()).toString());
            show2.setStartTime(new Time(System.currentTimeMillis()).toString());

            Show show3 = new Show();
            show3.setName("Test show3");
            show3.setDuration(120);
            show3.setStartDate(new Date(System.currentTimeMillis()).toString());
            show3.setStartTime(new Time(System.currentTimeMillis()).toString());

            User user = new User("testname", "testpw");
            em.persist(user);

            Guest guest = new Guest();
            guest.setEmail("testemail");
            guest.setPhone("12345678");
            guest.setStatus("teststatus");
            guest.setUser(user);

            show.addGuest(guest);
            show2.addGuest(guest);

            em.persist(show);
            em.persist(show2);
            em.persist(show3);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void tearDownClass() {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Show").executeUpdate();
            em.createQuery("DELETE FROM Guest").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Festival").executeUpdate();

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
    }



    @Test
    void create() {
        User user = new User("testname2", "testpw2");

        Guest guest = new Guest();
        guest.setEmail("testemail2");
        guest.setPhone("87654321");
        guest.setStatus("teststatus");
        guest.setUser(user);

        guest = facade.create(guest);
        assertNotNull(guest.getId());
    }

    @Test
    void update() {
        TypedQuery<Show> query = em.createQuery("SELECT s FROM Show s", Show.class);
        TypedQuery<Guest> query2 = em.createQuery("SELECT g FROM Guest g", Guest.class);
        Show show = query.getResultList().get(1);
        Guest guest = query2.getResultList().get(0);

        guest.addShow(show);
        facade.update(guest);
        em.refresh(guest);
        assertTrue(guest.getShows().size()>1);
    }
}