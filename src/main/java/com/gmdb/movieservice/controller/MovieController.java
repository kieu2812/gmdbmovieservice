package com.gmdb.movieservice.controller;

import com.gmdb.movieservice.bean.Movie;
import com.gmdb.movieservice.dao.MovieRepository;
import com.gmdb.movieservice.response.MovieDetailResponse;
import com.gmdb.movieservice.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Optional;

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

    @GetMapping("/title")
    public MovieDetailResponse getMovieDetailByTitle(@RequestParam String title){
        Optional<Movie> movie = this.movieRepository.findByTitle(title);
        if(movie.isPresent())
            return new MovieDetailResponse(HttpStatus.FOUND.value(), Constants.SUCCESS, movie.get());

        return new MovieDetailResponse( HttpStatus.NOT_FOUND.value(), Constants.NOT_FOUND, null);

    }

}
