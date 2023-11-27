package com.tech.springbootlibraryservice.service;

import com.tech.springbootlibraryservice.dao.ReviewRepository;
import com.tech.springbootlibraryservice.entity.Review;
import com.tech.springbootlibraryservice.requestModels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;


@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review postReview(String userEmail, ReviewRequest reviewRequest) throws Exception{
        Review checkReviewAlreadyExists = reviewRepository.findByUserEmailAndBookId(userEmail,reviewRequest.getBookId() );
        if(checkReviewAlreadyExists != null){
                throw new Exception("Review Already Created");
        }

        Review review = new Review();
        if(reviewRequest.getBookDescription().isPresent()) {
            review.setReviewDescription(reviewRequest.getBookDescription().map(Object::toString)
                    .orElse(null));
        }
        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        review.setDate(Date.valueOf(LocalDate.now()));
        review.setUserEmail(userEmail);
        return reviewRepository.save(review);

    }

    public Boolean userReviewListed(String userEmail, Long bookId){
        Review checkReviewAlreadyExists = reviewRepository.findByUserEmailAndBookId(userEmail,bookId);
        if(checkReviewAlreadyExists != null){
            return true;
        }else {
            return false;
        }

    }



}
