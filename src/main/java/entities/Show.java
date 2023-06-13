package entities;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Date;
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
    private Date startDate;

    @Column(name = "startTime", nullable = false) // HH:MM:SS (18:30:00)
    private Time startTime;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "shows_guests",
            joinColumns = @JoinColumn(name = "show_id"),
            inverseJoinColumns = @JoinColumn(name = "guests_id"))
    private List<Guest> guests = new ArrayList<>();


    public Show () {
    }

    public Show (String name, Integer duration, Date startDate, Time startTime) {
        this.name = name;
        this.duration = duration;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    @Transient
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void addGuest(Guest guest) {
        guests.add(guest);
    }

}
