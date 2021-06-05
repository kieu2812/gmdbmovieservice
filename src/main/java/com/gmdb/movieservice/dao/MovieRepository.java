package com.gmdb.movieservice.dao;

import com.gmdb.movieservice.bean.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
    Optional<Movie> findByTitle(String title);
}
