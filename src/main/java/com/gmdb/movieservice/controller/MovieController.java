package com.gmdb.movieservice.controller;

import com.gmdb.movieservice.bean.Movie;
import com.gmdb.movieservice.dao.MovieRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("")
    public Iterable<Movie> getMovies(){
        return this.movieRepository.findAll();
    }

    @PostMapping("")
    public Movie createMovie(@RequestBody Movie movie){
        return this.movieRepository.save(movie);
    }
}
