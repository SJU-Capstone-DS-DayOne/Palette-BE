package kr.ac.sejong.ds.palette.review.controller;

import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.jwt.dto.CustomUserDetails;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewCreateRequest;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewUpdateRequest;
import kr.ac.sejong.ds.palette.review.dto.response.ReviewResponse;
import kr.ac.sejong.ds.palette.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/restaurants/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurant(@PathVariable(name = "restaurantId") Long restaurantId){
        List<ReviewResponse> reviewResponseList = reviewService.getAllReviewByRestaurant(restaurantId);
        return ResponseEntity.ok().body(reviewResponseList);
    }
}
