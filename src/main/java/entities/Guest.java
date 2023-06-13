package entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guests")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "name", referencedColumnName = "user_name")
    private User user;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToMany(mappedBy = "guests")
    private List<Show> shows = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "festival_id")
    private Festival festival;


    public Guest() {
    }

    public Guest(User user, String phone, String email, String status) {
        this.user = user;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }

    public void addShow(Show show) {
        shows.add(show);
        show.getGuests().add(this);
    }


    public List<Show> getShows() {
        return shows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
