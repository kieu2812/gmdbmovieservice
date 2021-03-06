package com.gmdb.movieservice.controller;

import com.gmdb.movieservice.bean.Movie;
import com.gmdb.movieservice.bean.MovieRating;
import com.gmdb.movieservice.bean.User;
import com.gmdb.movieservice.dao.MovieRatingRepository;
import com.gmdb.movieservice.dao.MovieRepository;
import com.gmdb.movieservice.dao.UserRepository;
import com.gmdb.movieservice.request.MovieRatingRequest;
import com.gmdb.movieservice.response.MovieDetailResponse;
import com.gmdb.movieservice.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private MovieRepository movieRepository;
    private UserRepository userRepository;
    private MovieRatingRepository movieRatingRepository;

    public MovieController(MovieRepository movieRepository, UserRepository userRepository, MovieRatingRepository movieRatingRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.movieRatingRepository = movieRatingRepository;
    }

    @GetMapping("")
    public Iterable<Movie> getMovies() {
        return this.movieRepository.findAll();
    }

    @PostMapping("")
    public Movie createMovie(@RequestBody Movie movie) {
        return this.movieRepository.save(movie);
    }

    @GetMapping("/title")
    public MovieDetailResponse getMovieDetailByTitle(@RequestParam String title) {
        Optional<Movie> movie = this.movieRepository.findByTitle(title);
        if (movie.isPresent())
            return new MovieDetailResponse(HttpStatus.FOUND.value(), Constants.SUCCESS, movie.get());

        return new MovieDetailResponse(HttpStatus.NOT_FOUND.value(), Constants.NOT_FOUND, null);

    }

    @PostMapping("/userRating")
    public MovieDetailResponse postRatingToMovie(@RequestBody MovieRatingRequest movieRatingRequest) {

        if (movieRatingRequest.getRating() == 0) {
            return new MovieDetailResponse(HttpStatus.BAD_REQUEST.value(), Constants.RATING_REQUIRE, null);
        }
        Optional<Movie> movie = this.movieRepository.findByTitle(movieRatingRequest.getMovieTitle());

        if (movie.isPresent()) {
            MovieRating movieRating = new MovieRating();
            movieRating.setMovieId(movie.get().getMovieId());
            User user = this.userRepository.findByName(movieRatingRequest.getUserName());
            if(user==null || user.getId() == 0){
                return new MovieDetailResponse(HttpStatus.BAD_REQUEST.value(), Constants.USER_NOT_FOUND, null);
            }
            movieRating.setUserId(user.getId());
            movieRating.setRating(movieRatingRequest.getRating());
            movieRating.setUserName(user.getName());
            movieRating.setReview(movieRatingRequest.getReviews());
            this.movieRatingRepository.save(movieRating);
            double averageRating = this.movieRatingRepository.calculateAvgRating(movieRating.getMovieId());
            movie.get().setRating(averageRating);

            List<MovieRating> movieRatings = movie.get().getMovieRatings() == null ? new ArrayList<>() : movie.get().getMovieRatings();
            movieRatings.add(movieRating);
            movie.get().setMovieRatings(movieRatings);

            this.movieRepository.save(movie.get());
            return new MovieDetailResponse(HttpStatus.CREATED.value(), Constants.CREATED, movie.get());
        }
        return new MovieDetailResponse(HttpStatus.NOT_FOUND.value(), Constants.NOT_FOUND, null);

    }

}
