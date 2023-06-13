package rest;

import entities.*;
import facades.FestivalFacade;
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

public class FestivalResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static FestivalFacade festivalFacade;
    private Guest g1;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        festivalFacade = FestivalFacade.getFacade(emf);

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
            g1 = new Guest(admin, "12585562328", "testemail", "teststatus");
            em.persist(admin);

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
    void createFestival() {
        login("admin", "adminpw");

        // Create a Festival object
        Festival festival = new Festival("Summer Music Festival", "Cityville", "2023-06-13", 4);

        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(festival)
                .when()
                .post("/festival/create")
                .then()
                .statusCode(200);
    }

    @Test
    void updateFestival() {
        login("admin", "adminpw");

        // Create a Festival object
        Festival festival = new Festival("Summer Music Festival", "Cityville", "2023-06-13", 3);

        // Persist the festival in the database before updating
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(festival);
        em.getTransaction().commit();
        em.close();

        // Update the festival object
        festival.setName("Updated Festival Name");
        festival.setCity("Updated City");
        festival.setStartDate("2023-06-15");
        festival.setDuration(5);

        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(festival)
                .when()
                .put("/festival/update")
                .then()
                .statusCode(200);
    }
}