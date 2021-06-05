package com.gmdb.movieservice.request;

import com.gmdb.movieservice.bean.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRatingRequest {
    private String movieTitle;
    private String userName;
    private int rating;
    private String reviews;

}
