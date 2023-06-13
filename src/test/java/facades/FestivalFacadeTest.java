package facades;

import entities.Festival;
import entities.Guest;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class FestivalFacadeTest {
    private static EntityManagerFactory emf;
    private static FestivalFacade facade;
    private static EntityManager em;

    public FestivalFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = FestivalFacade.getFacade(emf);
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Show").executeUpdate();
            em.createQuery("DELETE FROM Guest").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Festival").executeUpdate();


            Festival festival = new Festival();
            festival.setName("Test festival");
            festival.setCity("Test city");
            festival.setStartDate(new Date(System.currentTimeMillis()));
            festival.setDuration(120);

            User user = new User("testname", "testpw");
            em.persist(user);

            Guest guest = new Guest(user, "136987891", "testemail", "teststatus");
            em.persist(guest);

            festival.addGuest(guest);
            em.persist(festival);
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
        Festival festival = new Festival("Test festival", "Test city", new Date(System.currentTimeMillis()), 120);

        User user = new User("testname", "testpw");
        em.persist(user);

        Guest guest = new Guest(user, "128312983", "testemail2", "teststatus");
        em.persist(guest);
        festival.addGuest(guest);

        festival = facade.create(festival);

        assertNotNull(festival.getId());
    }

    @Test
    void update() {
        TypedQuery<Festival> query = em.createQuery("SELECT f FROM Festival f", Festival.class);
        Festival festival = query.getResultList().get(0);

        festival.setName("Updated festival");
        festival = facade.update(festival);

        assertEquals("Updated festival", em.find(Festival.class, festival.getId()).getName());
    }
}