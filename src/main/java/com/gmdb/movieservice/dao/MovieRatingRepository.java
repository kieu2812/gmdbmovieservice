package com.gmdb.movieservice.dao;

import com.gmdb.movieservice.bean.MovieRating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MovieRatingRepository extends CrudRepository<MovieRating, Integer> {

    @Query("Select avg(m.rating) from MovieRating m where m.movieId=?1")
    double calculateAvgRating(int movieId);
}
