package com.gmdb.movieservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmdb.movieservice.bean.Movie;
import com.gmdb.movieservice.dao.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    private void createMockData() throws Exception {
        String movieStr =getJSON("src/test/resources/oneMovie.json");
        Movie movie = mapper.readValue(movieStr, Movie.class);
        this.movieRepository.save(movie);

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

    private void createMockManyMovies() throws Exception {
        String movieStr =getJSON("src/test/resources/manyMovies.json");
        TypeReference<List<Movie>> movies = new TypeReference<List<Movie>>() {};

        List<Movie> jsonToMovieList = mapper.readValue(movieStr, movies);
        this.movieRepository.saveAll(jsonToMovieList);

    }

    @Test
    @Transactional
    @Rollback
    public void testGetManyMovies() throws Exception {

        createMockManyMovies();
        List<Movie> moviesList =(List<Movie>) this.movieRepository.findAll();
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
    public String getJSON(String path) throws  Exception{
        Path paths = Paths.get(path);
        return new String(Files.readAllBytes(paths));
    }
}
