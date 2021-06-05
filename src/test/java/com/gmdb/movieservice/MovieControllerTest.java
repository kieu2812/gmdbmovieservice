package com.gmdb.movieservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmdb.movieservice.bean.Movie;
import com.gmdb.movieservice.dao.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .andExpect( content().json("[]"));
    }

    @Test
    public void testCreateMovie() throws Exception {


        String movieStr =  " {\n" +
                "    \"title\": \"The Avengers\",\n" +
                "    \"director\": \"Joss Whedon\",\n" +
                "    \"actors\": \"Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth\",\n" +
                "    \"release\": \"2012\",\n" +
                "    \"description\": \"Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.\",\n" +
                "    \"rating\": null\n" +
                "  }";

        Movie movie =  mapper.readValue(movieStr, Movie.class);
        System.out.println(movie);

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


}
