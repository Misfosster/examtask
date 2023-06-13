package dtos;

import entities.Show;

import java.sql.Date;
import java.sql.Time;

public class ShowDTO {
    private Long id;
    private String name;
    private Integer duration;
    private String startDate;
    private String startTime;

    public ShowDTO() {
    }

    public ShowDTO(String name, Integer duration, String startDate, String startTime) {
        this.name = name;
        this.duration = duration;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    public ShowDTO(Show show) {
        if (show.getId() != null)
            this.id = show.getId();
        this.name = show.getName();
        this.duration = show.getDuration();
        this.startDate = show.getStartDate();
        this.startTime = show.getStartTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public Show toEntity() {
        return new Show(this.name, this.duration, this.startDate, this.startTime);
    }

    @Override
    public String toString() {
        return "ShowDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                '}';
    }
}
