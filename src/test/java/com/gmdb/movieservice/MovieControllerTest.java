package com.gmdb.movieservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmdb.movieservice.bean.Movie;
import com.gmdb.movieservice.bean.User;
import com.gmdb.movieservice.dao.MovieRepository;
import com.gmdb.movieservice.dao.UserRepository;
import com.gmdb.movieservice.request.MovieRatingRequest;
import com.gmdb.movieservice.util.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {
    @Autowired
    MockMvc mvc;

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;


    public String getJSON(String path) throws Exception {
        Path paths = Paths.get(path);
        return new String(Files.readAllBytes(paths));
    }

    private void createMockData() throws Exception {
        String movieStr = getJSON("src/test/resources/oneMovie.json");
        Movie movie = mapper.readValue(movieStr, Movie.class);
        this.movieRepository.save(movie);

    }

    private void createMockManyMovies() throws Exception {
        String movieStr = getJSON("src/test/resources/manyMovies.json");
        TypeReference<List<Movie>> movies = new TypeReference<List<Movie>>() {
        };

        List<Movie> jsonToMovieList = mapper.readValue(movieStr, movies);
        this.movieRepository.saveAll(jsonToMovieList);

    }

    @Test
    public void testGetEmptyMovies() throws Exception {
        this.mvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateMovie() throws Exception {


        String movieStr = getJSON("src/test/resources/oneMovie.json");

        Movie movie = mapper.readValue(movieStr, Movie.class);

        RequestBuilder request = post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieStr);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(movie.getTitle())))
                .andExpect(jsonPath("$.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", is(movie.getActors())))
                .andExpect(jsonPath("$.release", is(movie.getRelease())))
                .andExpect(jsonPath("$.description", is(movie.getDescription())));
    }


    @Test
    @Transactional
    @Rollback
    public void testGetMovies() throws Exception {

        createMockData();
        Movie movie = this.movieRepository.findAll().iterator().next();
        this.mvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is(movie.getTitle())))
                .andExpect(jsonPath("$[0].director", is(movie.getDirector())))
                .andExpect(jsonPath("$[0].actors", is(movie.getActors())))
                .andExpect(jsonPath("$[0].release", is(movie.getRelease())))
                .andExpect(jsonPath("$[0].description", is(movie.getDescription())));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetManyMovies() throws Exception {

        createMockManyMovies();
        List<Movie> moviesList = (List<Movie>) this.movieRepository.findAll();
        Movie movie = moviesList.get(0);
        Movie movie2 = moviesList.get(1);

        this.mvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is(movie.getTitle())))
                .andExpect(jsonPath("$[0].director", is(movie.getDirector())))
                .andExpect(jsonPath("$[0].actors", is(movie.getActors())))
                .andExpect(jsonPath("$[0].release", is(movie.getRelease())))
                .andExpect(jsonPath("$[0].description", is(movie.getDescription())))
                .andExpect(jsonPath("$[1].title", is(movie2.getTitle())))
                .andExpect(jsonPath("$[1].director", is(movie2.getDirector())))
                .andExpect(jsonPath("$[1].actors", is(movie2.getActors())))
                .andExpect(jsonPath("$[1].release", is(movie2.getRelease())))
                .andExpect(jsonPath("$[1].description", is(movie2.getDescription())));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetExistingMovieDetails() throws Exception {

        createMockManyMovies();
        List<Movie> moviesList = (List<Movie>) this.movieRepository.findAll();
        Movie movie = moviesList.get(0);

        RequestBuilder request = get("/movies/title")
                .param("title", movie.getTitle());

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(HttpStatus.FOUND.value())))
                .andExpect(jsonPath("$.message", is(Constants.SUCCESS)))
                .andExpect(jsonPath("$.data.title", is(movie.getTitle())))
                .andExpect(jsonPath("$.data.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.data.actors", is(movie.getActors())))
                .andExpect(jsonPath("$.data.release", is(movie.getRelease())))
                .andExpect(jsonPath("$.data.description", is(movie.getDescription())));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetNonExistingMovieDetails() throws Exception {

        createMockManyMovies();
        List<Movie> moviesList = (List<Movie>) this.movieRepository.findAll();

        RequestBuilder request = get("/movies/title")
                .param("title", "SUPERMAN");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(Constants.NOT_FOUND)))
                .andExpect(jsonPath("$.data", is(nullValue())));

    }

    @Test
    @Transactional
    @Rollback
    public void testSubmitRatingToMovie() throws Exception {

        createMockManyMovies();
        List<Movie> moviesList = (List<Movie>) this.movieRepository.findAll();
        Movie movie = moviesList.get(0);

        User user = new User("nicky");
        this.userRepository.save(user);

        String movieRatingStr = getJSON("src/test/resources/userRating.json");

        MovieRatingRequest movieRating = mapper.readValue(movieRatingStr, MovieRatingRequest.class);

        RequestBuilder request = post("/movies/userRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieRatingStr);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.message", is(Constants.CREATED)))
                .andExpect(jsonPath("$.data.title", is(movie.getTitle())))
                .andExpect(jsonPath("$.data.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.data.actors", is(movie.getActors())))
                .andExpect(jsonPath("$.data.release", is(movie.getRelease())))
                .andExpect(jsonPath("$.data.description", is(movie.getDescription())))
                .andExpect(jsonPath("$.data.rating", is((double) movieRating.getRating())));

    }

    @Test
    @Transactional
    @Rollback
    public void testManyMovieRatings() throws Exception {

        createMockManyMovies();
        List<Movie> moviesList = (List<Movie>) this.movieRepository.findAll();
        Movie movie = moviesList.get(0);

        User user1 = new User("nicky");
        User user2 = new User("mike");
        this.userRepository.save(user1);
        this.userRepository.save(user2);
        String movieRatingStr1 = getJSON("src/test/resources/userRating.json");

        String movieRatingStr2 = getJSON("src/test/resources/mikeUserRating.json");


        RequestBuilder request1 = post("/movies/userRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieRatingStr1);

        RequestBuilder request2 = post("/movies/userRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieRatingStr2);

        this.mvc.perform(request1)
                .andExpect(status().isOk());

        this.mvc.perform(request2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.message", is(Constants.CREATED)))
                .andExpect(jsonPath("$.data.title", is(movie.getTitle())))
                .andExpect(jsonPath("$.data.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.data.actors", is(movie.getActors())))
                .andExpect(jsonPath("$.data.release", is(movie.getRelease())))
                .andExpect(jsonPath("$.data.description", is(movie.getDescription())))
                .andExpect(jsonPath("$.data.rating", is(4.0)));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetMovieDetailsWithManyMovieRatings() throws Exception {

        createMockManyMovies();
        List<Movie> moviesList = (List<Movie>) this.movieRepository.findAll();
        Movie movie = moviesList.get(0);

        User user1 = new User("nicky");
        User user2 = new User("mike");
        this.userRepository.save(user1);
        this.userRepository.save(user2);
        String movieRatingStr1 = getJSON("src/test/resources/userRating.json");
        MovieRatingRequest movieRating1 = mapper.readValue(movieRatingStr1, MovieRatingRequest.class);

        String movieRatingStr2 = getJSON("src/test/resources/mikeUserRating.json");
        MovieRatingRequest movieRating2 = mapper.readValue(movieRatingStr2, MovieRatingRequest.class);


        RequestBuilder request1 = post("/movies/userRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieRatingStr1);

        RequestBuilder request2 = post("/movies/userRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieRatingStr2);

        RequestBuilder request3 = get("/movies/title")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", movie.getTitle());

        this.mvc.perform(request1)
                .andExpect(status().isOk());

        this.mvc.perform(request2)
                .andExpect(status().isOk());

        movie = moviesList.get(0);

        this.mvc.perform(request3)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(HttpStatus.FOUND.value())))
                .andExpect(jsonPath("$.message", is(Constants.SUCCESS)))
                .andExpect(jsonPath("$.data.title", is(movie.getTitle())))
                .andExpect(jsonPath("$.data.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.data.actors", is(movie.getActors())))
                .andExpect(jsonPath("$.data.release", is(movie.getRelease())))
                .andExpect(jsonPath("$.data.description", is(movie.getDescription())))
                .andExpect(jsonPath("$.data.rating", is(4.0)))
                .andExpect(jsonPath("$.data.movieRatings.[0].review", is(movieRating1.getReviews())))
                .andExpect(jsonPath("$.data.movieRatings.[1].review", is(movieRating2.getReviews())))
                .andExpect(jsonPath("$.data.movieRatings.[0].userName", is(user1.getName())))
                .andExpect(jsonPath("$.data.movieRatings.[1].userName", is(user2.getName())))
                .andExpect(jsonPath("$.data.movieRatings.[0].rating", is(movieRating1.getRating())))
                .andExpect(jsonPath("$.data.movieRatings.[1].rating", is(movieRating2.getRating())));


    }

    @Test
    @Transactional
    @Rollback
    public void testSubmitReviewWithoutRating() throws Exception {

        createMockManyMovies();
        List<Movie> moviesList = (List<Movie>) this.movieRepository.findAll();
        Movie movie = moviesList.get(0);

        User user1 = new User("Taylor");
        this.userRepository.save(user1);
        String movieRatingStr = getJSON("src/test/resources/userReviewWithoutRating.json");
        MovieRatingRequest movieRating1 = mapper.readValue(movieRatingStr, MovieRatingRequest.class);


        RequestBuilder request = post("/movies/userRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieRatingStr);


        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is(Constants.RATING_REQUIRE)))
                .andExpect(jsonPath("$.data", is(nullValue())));

    }


}
