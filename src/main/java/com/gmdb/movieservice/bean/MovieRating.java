package com.gmdb.movieservice.bean;

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
@Getter
@Setter
public class MovieRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int movieRatingId;
    private int movieId;
    private int userId;
    private int rating;
    private String review;
}
