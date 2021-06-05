package com.gmdb.movieservice.dao;

import com.gmdb.movieservice.bean.MovieRating;
import org.springframework.data.repository.CrudRepository;

public interface MovieRatingRepository extends CrudRepository<MovieRating, Integer> {

}
