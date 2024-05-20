package kr.ac.sejong.ds.palette.review.service;

import kr.ac.sejong.ds.palette.common.exception.member.NotFoundMemberException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundRestaurantException;
import kr.ac.sejong.ds.palette.common.exception.review.NotFoundReviewException;
import kr.ac.sejong.ds.palette.common.exception.review.NotMatchingReviewException;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantRepository;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewCreateRequest;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewUpdateRequest;
import kr.ac.sejong.ds.palette.review.dto.response.ReviewResponse;
import kr.ac.sejong.ds.palette.review.entity.Review;
import kr.ac.sejong.ds.palette.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public List<ReviewResponse> getAllReviewByRestaurant(Long restaurantId){
        List<ReviewResponse> reviewResponseList = reviewRepository.findAllWithMemberByRestaurantId(restaurantId)
                .stream().map(review -> ReviewResponse.of(review)).collect(Collectors.toList());
        return reviewResponseList;
    }

    @Transactional
    public void createReview(ReviewCreateRequest reviewCreateRequest, Long memberId, Long restaurantId) {
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 레스토랑 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);

        Review review = new Review(reviewCreateRequest.content(), member, restaurant);
        reviewRepository.save(review);
    }

    @Transactional
    public void updateReview(ReviewUpdateRequest reviewUpdateRequest, Long memberId, Long reviewId){
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundReviewException::new);

        // 본인의 리뷰인지 검증
        if(review.getMember().getId() != memberId)
            throw new NotMatchingReviewException();

        review.updateReview(reviewUpdateRequest.content());
    }

    @Transactional
    public void deleteReview(Long memberId, Long reviewId){
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundReviewException::new);

        // 본인의 리뷰인지 검증
        if(review.getMember().getId() != memberId)
            throw new NotMatchingReviewException();

        reviewRepository.delete(review);
    }

}
