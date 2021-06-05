package com.gmdb.movieservice.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor

@Getter
@Setter
public class MovieRatingReview {
    private String review;
    private String userName;
    private int rating;
    @ManyToOne
    @JoinColumn(name="id", nullable=false)
    private Movie movie;

    public MovieRatingReview(String review, String userName, int rating) {
        this.review = review;
        this.userName = userName;
        this.rating = rating;
    }
}
