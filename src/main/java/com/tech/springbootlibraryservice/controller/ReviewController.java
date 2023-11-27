package com.tech.springbootlibraryservice.controller;

import com.tech.springbootlibraryservice.entity.Review;
import com.tech.springbootlibraryservice.requestModels.ReviewRequest;
import com.tech.springbootlibraryservice.service.ReviewService;
import com.tech.springbootlibraryservice.utils.ExtractJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://libraryservice-demo.azurewebsites.net:8080")
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @PostMapping("/secure/postReview")
    public Review createReview(@RequestHeader(value = "Authorization") String token, @RequestBody ReviewRequest requestMapping) throws Exception {
        String userEmail = ExtractJwt.payLoadJwtExtraction(token,"\"sub\"");
        if(userEmail == null){
            throw new Exception("User Email is missing");
        }
        return reviewService.postReview(userEmail,requestMapping );

    }

    @GetMapping("/secure/isUserReviewExists")
    public Boolean userReviewListed(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId ) throws Exception{
        String userEmail = ExtractJwt.payLoadJwtExtraction(token,"\"sub\"");
        if(userEmail == null){
            throw new Exception("User Email is missing");
        }
        return reviewService.userReviewListed(userEmail,bookId);


    }








}
