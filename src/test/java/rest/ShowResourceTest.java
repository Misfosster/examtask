package rest;

import entities.Guest;
import entities.RenameMe;
import entities.Show;
import entities.User;
import facades.ShowFacade;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

class ShowResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private Show s1, s2;
    private Guest g1;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static ShowFacade showFacade;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        showFacade = ShowFacade.getFacade(emf);

        httpServer = startServer();
        // Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }


    @AfterEach
    public void tearDown() {
        EntityManager em = emf.createEntityManager();
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

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            User user = new User("testname", "testpw");
            em.persist(user);
            g1 = new Guest(user, "1280931289985", "test@email.com", "teststatus");
            em.persist(g1);

            s1 = new Show("Resourcetest show", 120, new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()));
            em.persist(s1);

            s1.addGuest(g1);
            em.merge(g1);

            s2 = new Show("Resourcetest show2", 120, new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()));
            em.persist(s2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @Test
    void getAvailableShows() {
        int expected = 2;
        int actual = 0;
        try {
            actual = RestAssured.given().when().get("/show").then().extract().body().jsonPath().getList("name").size();
        } catch (Exception e) {
            fail("An error occurred while retrieving available shows: " + e.getMessage());
        }
        assertEquals(expected, actual, "Number of available shows does not match the expected value.");
    }

    @Test
    void getShowsByGuest() {
        int expected = 1;
        int actual = 0;
        try {
            actual = RestAssured.given().when().get("/show/testname/").then().extract().body().jsonPath().getList("name").size();
        } catch (Exception e) {
            fail("An error occurred while retrieving shows for the guest: " + e.getMessage());
        }
        assertEquals(expected, actual, "Number of shows for the guest does not match the expected value.");
    }


}


