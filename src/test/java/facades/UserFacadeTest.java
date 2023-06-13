package facades;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getFacade(emf);
    }

    @AfterAll
    static void tearDownClass() {

    }

    @BeforeEach
    void setUp() {
        System.out.println("_______________________________________________________\nTESTING:\n");
        EntityManager em = emf.createEntityManager();
        try {
            User foundUser = em.find(User.class, "test");
            em.getTransaction().begin();
            if (foundUser != null) em.remove(foundUser);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void createUser() {
        EntityManager em = emf.createEntityManager();
        User user = facade.createUser(new User("test", "test"));
        assertEquals("test", user.getUserName());
    }




}