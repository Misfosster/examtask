package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shows")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "startDate", nullable = false) // YYYY-MM-DD (2023-06-13)
    private String startDate;

    @Column(name = "startTime", nullable = false) // HH:MM:SS (18:30:00)
    private String startTime;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "shows_guests",
            joinColumns = @JoinColumn(name = "show_id"),
            inverseJoinColumns = @JoinColumn(name = "guests_id"))
    private List<Guest> guests = new ArrayList<>();


    public Show () {
    }

    public Show (String name, Integer duration, String startDate, String startTime) {
        this.name = name;
        this.duration = duration;
        this.startDate = startDate;
        this.startTime = startTime;
        this.guests = new ArrayList<>();
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }


    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void addGuest(Guest guest) {
        guests.add(guest);
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", startDate='" + startDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", guests=" + guests +
                '}';
    }

}
