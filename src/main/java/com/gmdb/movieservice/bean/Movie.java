package com.gmdb.movieservice.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Movie")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int movieId;
    private String title;
    private String director;
    private String actors;
    private String release;
    private String description;
    private Double rating;
//    @OneToMany(mappedBy="movie")
//    List<MovieRatingReview> movieRatingReviews;

    @OneToMany(mappedBy="movieId")
    List<MovieRating> movieRatings;

}
