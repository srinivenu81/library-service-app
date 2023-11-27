package com.tech.springbootlibraryservice.requestModels;

import lombok.Data;

import java.util.Optional;

@Data
public class ReviewRequest {

    private Long bookId;

    private double rating;

    private Optional<String> bookDescription;
}
