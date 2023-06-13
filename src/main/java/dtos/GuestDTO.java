package dtos;

import entities.Guest;

public class GuestDTO {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String status;
    private String festival;

    public GuestDTO() {
    }

    public GuestDTO(String username, String phone, String email, String status) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }

    public GuestDTO(Guest guest) {
        if (guest.getId() != null)
            this.id = guest.getId();
        this.username = guest.getUser().getUserName();
        this.phone = guest.getPhone();
        this.email = guest.getEmail();
        this.status = guest.getStatus();
        this.festival = guest.getFestival().getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    @Override
    public String toString() {
        return "GuestDTO{" +
                "id=" + id +
                ", user=" + username +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", festival=" + festival +
                '}';
    }
}
