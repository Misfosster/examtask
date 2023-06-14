/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

//import com.nimbusds.jose.shaded.json.JSONObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import entities.Guest;
import entities.Role;
import entities.User;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        GuestFacade guestFacade = GuestFacade.getFacade(emf);
//        ShowFacade showFacade = ShowFacade.getFacade(emf);
//        UserFacade userFacade = UserFacade.getFacade(emf);
//        FestivalFacade festivalFacade = FestivalFacade.getFacade(emf);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User admin = new User("admin", "test123");
        admin.addRole(new Role("admin"));
        em.persist(admin);
        em.getTransaction().commit();
        guestFacade.create(new Guest(new User("user", "test123"), "12345678", "testemail", "teststatus"));
    }

    public static void main(String[] args) {
        populate();
    }
}
