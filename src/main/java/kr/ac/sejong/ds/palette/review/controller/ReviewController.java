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

    @PostMapping("/restaurants/{restaurantId}/reviews")
    public ResponseEntity<Void> createReview(Authentication authentication, @PathVariable(name = "restaurantId") Long restaurantId, @RequestBody @Valid ReviewCreateRequest reviewCreateRequest){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        reviewService.createReview(reviewCreateRequest, memberId, restaurantId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> updateReviewContent(Authentication authentication, @PathVariable(name = "reviewId") Long reviewId, @RequestBody @Valid ReviewUpdateRequest reviewUpdateRequest) {
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        reviewService.updateReview(reviewUpdateRequest, memberId, reviewId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(Authentication authentication, @PathVariable(name = "reviewId") Long reviewId) {
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        reviewService.deleteReview(memberId, reviewId);
        return ResponseEntity.ok().build();
    }
}
