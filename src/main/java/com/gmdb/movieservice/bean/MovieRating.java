package com.gmdb.movieservice.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor

public class MovieRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int movieRatingId;
    private int movieId;
    private int userId;
    private int rating;
    private String review;

    private String userName;

    @JsonIgnore
    public int getMovieRatingId() {
        return movieRatingId;
    }

    @JsonProperty
    public void setMovieRatingId(int movieRatingId) {
        this.movieRatingId = movieRatingId;
    }

    @JsonIgnore

    public int getMovieId() {
        return movieId;
    }

    @JsonProperty

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @JsonIgnore

    public int getUserId() {
        return userId;
    }

    @JsonProperty

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
