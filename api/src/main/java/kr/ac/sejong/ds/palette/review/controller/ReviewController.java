package kr.ac.sejong.ds.palette.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.jwt.dto.CustomUserDetails;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewCreateRequest;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewUpdateRequest;
import kr.ac.sejong.ds.palette.review.dto.response.ReviewResponse;
import kr.ac.sejong.ds.palette.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "리뷰 (Review)")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "레스토랑 리뷰 조회 (회원 정보 포함)")
    @GetMapping("/restaurants/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurant(@PathVariable(name = "restaurantId") Long restaurantId, Pageable pageable){
        List<ReviewResponse> reviewResponseList = reviewService.getAllReviewByRestaurant(restaurantId, pageable);
        return ResponseEntity.ok().body(reviewResponseList);
    }

    @Operation(summary = "유사 취향 유저의 리뷰 조회")
    @GetMapping("/restaurants/{restaurantId}/similar-member-reviews")
    public ResponseEntity<List<ReviewResponse>> getSimilarMemberReviewsByRestaurant(Authentication authentication, @PathVariable(name = "restaurantId") Long restaurantId){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        List<ReviewResponse> reviewResponseList = reviewService.getAllSimilarMemberReviewByRestaurant(memberId, restaurantId);
        return ResponseEntity.ok().body(reviewResponseList);
    }

    @Operation(summary = "레스토랑 리뷰 생성")
    @PostMapping("/restaurants/{restaurantId}/reviews")
    public ResponseEntity<Void> createReview(Authentication authentication, @PathVariable(name = "restaurantId") Long restaurantId, @RequestBody @Valid ReviewCreateRequest reviewCreateRequest){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        reviewService.createReview(reviewCreateRequest, memberId, restaurantId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리뷰 수정")
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> updateReviewContent(Authentication authentication, @PathVariable(name = "reviewId") Long reviewId, @RequestBody @Valid ReviewUpdateRequest reviewUpdateRequest) {
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        reviewService.updateReview(reviewUpdateRequest, memberId, reviewId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(Authentication authentication, @PathVariable(name = "reviewId") Long reviewId) {
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        reviewService.deleteReview(memberId, reviewId);
        return ResponseEntity.ok().build();
    }
}
