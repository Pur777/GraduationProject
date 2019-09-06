package ru.javawebinar.topjava.to;

import java.time.LocalDate;

public class VoteTo {

    private LocalDate date;

    private Integer rating;

    public VoteTo() {
    }

    public VoteTo(LocalDate date, Integer rating) {
        this.date = date;
        this.rating = rating;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
