package rest;

import entities.Guest;
import entities.Role;
import entities.Show;
import entities.User;
import facades.GuestFacade;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.sql.Date;
import java.sql.Time;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.fail;

public class GuestResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private Show s1, s2;
    private Guest g1;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static GuestFacade guestFacade;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        guestFacade = GuestFacade.getFacade(emf);

        httpServer = startServer();
        // Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Show").executeUpdate();
            em.createQuery("DELETE FROM Guest").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Festival").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
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
            em.createQuery("DELETE FROM Role").executeUpdate();
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
            User admin = new User("admin", "adminpw");
            Role role = new Role("admin");
            admin.addRole(role);
            em.persist(admin);

            s1 = new Show("Resourcetest show", 120, new Date(System.currentTimeMillis()).toString(), new Time(System.currentTimeMillis()).toString());
            em.persist(s1);


            s2 = new Show("Resourcetest show2", 120, new Date(System.currentTimeMillis()).toString(), new Time(System.currentTimeMillis()).toString());
            em.persist(s2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String username, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    @Test
    void signUp() {
        User user = new User("testname", "testpw");
        guestFacade.create(new Guest(user, "12214589632145896", "testemail", "teststatus"));
        login("testname", "testpw");


        try {
            given()
                    .contentType("application/json")
                    .header("x-access-token", securityToken)
                    .queryParam("username", "testname")
                    .body(s2)
                    .when()
                    .put("/guest/signup/{username}", "testname")
                    .then()
                    .statusCode(200);
        } catch (Exception e) {
            fail("An error occurred while signing up: " + e.getMessage());
        }
    }

    @Test
    void create() {
        login("admin", "adminpw");
        EntityManager em = emf.createEntityManager();
        User user = new User("testname2", "testpw");
//        Role role = new Role("user");
//        user.addRole(role);
//        em.getTransaction().begin();
//        em.merge(user);
//        em.getTransaction().commit();

        Guest guest = new Guest(user, "12214589632145896", "testemail", "teststatus");
        em.persist(guest);

        try {
            given()
                    .contentType("application/json")
                    .header("x-access-token", securityToken)
                    .body(guest)
                    .when()
                    .post("/guest/create")
                    .then()
                    .statusCode(200);
        } catch (Exception e) {
            fail("An error occurred while creating: " + e.getMessage());
        }
    }




}
