package facades;

import entities.Guest;
import entities.Show;
import entities.User;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ShowFacadeTest {

    private static EntityManagerFactory emf;
    private static ShowFacade facade;
    private static EntityManager em;

    public ShowFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ShowFacade.getFacade(emf);
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Show show = new Show();
            show.setName("Test show");
            show.setDuration(120);
            show.setStartDate(new Date(System.currentTimeMillis()));
            show.setStartTime(new Time(System.currentTimeMillis()));

            User user = new User("testname", "testpw");

            em.persist(user);

            Guest guest = new Guest();
            guest.setEmail("testemail");
            guest.setPhone("12345678");
            guest.setStatus("teststatus");
            guest.setUser(user);

            show.addGuest(guest);

            em.persist(show);
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
    public void create() {
        Show show = new Show();
        show.setName("Test show2");
        show.setDuration(120);
        show.setStartDate(new Date(System.currentTimeMillis()));
        show.setStartTime(new Time(System.currentTimeMillis()));

        show = facade.create(show);

        assertNotNull(show.getId());
    }

    @Test
    public void update() {
        Show show = new Show();
        show.setName("Test showfkasdkasd");
        show.setDuration(120);
        show.setStartDate(new Date(System.currentTimeMillis()));
        show.setStartTime(new Time(System.currentTimeMillis()));

        show = facade.create(show);
        assertEquals("Test showfkasdkasd", show.getName());
        show.setName("Test show3");
        show = facade.update(show);

        assertEquals("Test show3", show.getName());
    }

    @Test
    public void delete() {
        Show show = new Show();
        show.setName("Test show4deletion");
        show.setDuration(120); // Set the duration in minutes
        show.setStartDate(new Date(System.currentTimeMillis())); // Set the current date as the start date
        show.setStartTime(new Time(System.currentTimeMillis())); // Set the current time as the start time
        show = facade.create(show);
        assertTrue(facade.delete(show));
    }

    @Test
    public void getGuests() {
        TypedQuery<Show> typedQuery = em.createQuery("SELECT s FROM Show s WHERE s.name = :name", Show.class).setParameter("name", "Test show");
        Show show = typedQuery.getSingleResult();

        assertTrue(facade.getGuests(show).size() > 0);
    }

}
