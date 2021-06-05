package com.gmdb.movieservice.response;

import com.gmdb.movieservice.bean.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailResponse {
    private int status;
    private String message;
    private Movie data;

}
